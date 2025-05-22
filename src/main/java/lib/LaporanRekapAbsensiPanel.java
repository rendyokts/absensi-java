package lib;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import com.itextpdf.text.Font;
import com.itextpdf.text.Image;

public class LaporanRekapAbsensiPanel extends JPanel {
    private JButton generateBtn;
    private JTable table;
    private DefaultTableModel tableModel;

    public LaporanRekapAbsensiPanel() {
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        generateBtn = new JButton("Generate PDF");
        generateBtn.addActionListener(e -> generatePDF());

        topPanel.add(generateBtn);

        tableModel = new DefaultTableModel(new String[] {
                "NRP", "Nama", "Hadir", "Telat", "Alpha", "Izin", "Sakit"
        }, 0);

        table = new JTable(tableModel);
        table.setRowHeight(25);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT a.nrp, k.nama, a.status, COUNT(*) as jumlah " +
                    "FROM absensi a " +
                    "LEFT JOIN karyawan k ON a.nrp = k.nrp " +
                    "GROUP BY a.nrp, a.status";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            HashMap<String, int[]> rekap = new HashMap<>();
            HashMap<String, String> namaMap = new HashMap<>();

            while (rs.next()) {
                String nrp = rs.getString("nrp");
                String status = rs.getString("status");
                int jumlah = rs.getInt("jumlah");
                String nama = rs.getString("nama");

                if (!rekap.containsKey(nrp)) {
                    rekap.put(nrp, new int[5]); // 0: Hadir, 1: Telat, 2: Alpha, 3: Izin, 4: Sakit
                }
                namaMap.put(nrp, nama);
                switch (status) {
                    case "Hadir":
                        rekap.get(nrp)[0] += jumlah;
                        break;
                    case "Telat":
                        rekap.get(nrp)[1] += jumlah;
                        break;
                    case "Alpha":
                        rekap.get(nrp)[2] += jumlah;
                        break;
                    case "Izin":
                        rekap.get(nrp)[3] += jumlah;
                        break;
                    case "Sakit":
                        rekap.get(nrp)[4] += jumlah;
                        break;
                }
            }

            tableModel.setRowCount(0);
            for (String nrp : rekap.keySet()) {
                int[] data = rekap.get(nrp);
                tableModel.addRow(new Object[] {
                        nrp,
                        namaMap.getOrDefault(nrp, "-"),
                        data[0], data[1], data[2], data[3], data[4]
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generatePDF() {
        JFileChooser chooser = new JFileChooser();
        int opt = chooser.showSaveDialog(this);
        if (opt != JFileChooser.APPROVE_OPTION)
            return;

        File file = chooser.getSelectedFile();
        String path = file.getAbsolutePath();
        if (!path.toLowerCase().endsWith(".pdf")) {
            path += ".pdf";
        }

        try {
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, new FileOutputStream(path));
            doc.open();

            Image logo = Image.getInstance(getClass().getResource("/images/ok.png").toURI().toString());
            logo.scaleToFit(80, 80);
            logo.setAbsolutePosition(doc.right() - 100, doc.top() - 80); // posisikan di kanan atas
            doc.add(logo);

            Paragraph title = new Paragraph("LAPORAN REKAPITULASI KEHADIRAN",
                    new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10f);
            doc.add(title);
            
            // === Info Perusahaan ===
            Font companyFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            Paragraph companyInfo = new Paragraph();
            companyInfo.add(new Chunk("PT Zona Kreatif Indonesia\n", companyFont));
            companyInfo.add(new Chunk("Jl. Raya Bogor No. 123, Jakarta\n\n", companyFont));
            companyInfo.setAlignment(Element.ALIGN_CENTER);
            companyInfo.setSpacingAfter(15f);
            doc.add(companyInfo);

            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            pdfTable.setWidthPercentage(100);
            Font headFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font dataFont = new Font(Font.FontFamily.HELVETICA, 9);

            // Header
            for (int i = 0; i < table.getColumnCount(); i++) {
                PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i), headFont));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                pdfTable.addCell(cell);
            }

            // Data
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    Object val = table.getValueAt(row, col);
                    pdfTable.addCell(new PdfPCell(new Phrase(val != null ? val.toString() : "", dataFont)));
                }
            }

            doc.add(pdfTable);

            String namaUser = Session.getCurrentUser();
            Font userFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
            Paragraph generatedBy = new Paragraph( namaUser, userFont);
            generatedBy.setAlignment(Element.ALIGN_RIGHT);
            generatedBy.setSpacingBefore(50f);

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("id", "ID"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
            String day = dayFormat.format(cal.getTime());
            String date = dateFormat.format(cal.getTime());

            Paragraph printDate = new Paragraph("Jakarta, " + day + ", " + date, userFont);
            printDate.setAlignment(Element.ALIGN_RIGHT);
            printDate.setSpacingBefore(10f);
            
            doc.add(printDate);
            doc.add(generatedBy);

            doc.close();

            JOptionPane.showMessageDialog(this, "PDF berhasil disimpan:\n" + path,
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal generate PDF: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
