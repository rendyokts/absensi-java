package lib;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LaporanCutiIzinPanel extends JPanel {
    private JButton generateBtn;
    private JTable table;
    private DefaultTableModel tableModel;

    public LaporanCutiIzinPanel() {
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);

        generateBtn = new JButton("Generate PDF");
        generateBtn.addActionListener(e -> generatePDF());

        filterPanel.add(generateBtn);

        tableModel = new DefaultTableModel(new String[] {
                "NRP", "Nama", "Jenis", "Tanggal Mulai", "Tanggal Selesai", "Alasan", "Status", "Tgl Pengajuan"
        }, 0);

        table = new JTable(tableModel);
        table.setRowHeight(25);

        add(filterPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT c.nrp, k.nama, c.jenis, c.tgl_mulai, c.tgl_selesai, c.alasan, c.status_pengajuan, c.tgl_pengajuan "
                    +
                    "FROM cuti_izin c " +
                    "LEFT JOIN karyawan k ON c.nrp = k.nrp " +
                    "ORDER BY c.tgl_pengajuan DESC";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0);
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            
            while (rs.next()) {
                Object[] row = {
                        rs.getString("nrp"),
                        rs.getString("nama"),
                        rs.getString("jenis"),
                        rs.getDate("tgl_mulai")!= null ? dateFormat.format(rs.getDate("tgl_mulai")) : "",
                        rs.getDate("tgl_selesai")!= null ? dateFormat.format(rs.getDate("tgl_selesai")) : "",
                        rs.getString("alasan"),
                        rs.getString("status_pengajuan"),
                        rs.getDate("tgl_pengajuan")!= null ? dateFormat.format(rs.getDate("tgl_pengajuan")) : "" 
                };
                tableModel.addRow(row);
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
            
            // === Logo di pojok kanan atas ===
            Image logo = Image.getInstance(getClass().getResource("/images/ok.png").toURI().toString());
            logo.scaleAbsolute(80, 80); // atur ukuran logo
            logo.setAbsolutePosition(doc.right() - 100, doc.top() - 80); // posisikan di kanan atas
            doc.add(logo);

            String title = "LAPORAN CUTI & IZIN KARYAWAN";

            Paragraph pTitle = new Paragraph(title, new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            pTitle.setAlignment(Element.ALIGN_CENTER);
            pTitle.setSpacingAfter(10f);
            doc.add(pTitle);
            
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
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font contentFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);

            // Header
            for (int i = 0; i < table.getColumnCount(); i++) {
                PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i), headerFont));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                pdfTable.addCell(cell);
            }

            // Isi
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    Object val = table.getValueAt(row, col);
                    PdfPCell cell = new PdfPCell(new Phrase(val != null ? val.toString() : "", contentFont));
                    pdfTable.addCell(cell);
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

            Paragraph printDate = new Paragraph("Jakarta, " + day + " " + date, userFont);
            printDate.setAlignment(Element.ALIGN_RIGHT);
            printDate.setSpacingBefore(10f);
            
            doc.add(printDate);
            doc.add(generatedBy);
            
            doc.close();

            JOptionPane.showMessageDialog(this,
                    "PDF berhasil disimpan:\n" + path,
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal generate PDF: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
