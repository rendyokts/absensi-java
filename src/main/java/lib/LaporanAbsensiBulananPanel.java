package lib;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

public class LaporanAbsensiBulananPanel extends JPanel {
    private JComboBox<String> monthCombo;
    private JComboBox<Integer> yearCombo;
    private JButton generateBtn;
    private JTable table;
    private DefaultTableModel tableModel;
    private User currentUser;

    public LaporanAbsensiBulananPanel() {
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);

        monthCombo = new JComboBox<>(new String[] {
                "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        });

        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        yearCombo = new JComboBox<>();
        for (int i = currentYear - 5; i <= currentYear + 1; i++) {
            yearCombo.addItem(i);
        }
        yearCombo.setSelectedItem(currentYear);

        generateBtn = new JButton("Generate PDF");
        generateBtn.addActionListener(e -> generatePDF());

        filterPanel.add(new JLabel("Bulan:"));
        filterPanel.add(monthCombo);
        filterPanel.add(new JLabel("Tahun:"));
        filterPanel.add(yearCombo);
        filterPanel.add(generateBtn);

        tableModel = new DefaultTableModel(new String[] {
                "Tanggal", "NRP", "Nama", "Jam Masuk", "Jam Keluar", "Status", "Keterangan"
        }, 0);

        table = new JTable(tableModel);
        table.setRowHeight(25);

        add(filterPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Tambahkan listener agar data dimuat ulang saat bulan/tahun diubah
        monthCombo.addActionListener(e -> loadData(getSelectedMonth(), getSelectedYear()));
        yearCombo.addActionListener(e -> loadData(getSelectedMonth(), getSelectedYear()));

        // Load data saat panel dibuka
        loadData(getSelectedMonth(), getSelectedYear());
    }

    private int getSelectedMonth() {
        return monthCombo.getSelectedIndex() + 1;
    }

    private int getSelectedYear() {
        return (int) yearCombo.getSelectedItem();
    }

    private void loadData(int month, int year) {
        try (Connection conn = Database.getConnection()) {
            String sql = """
                        SELECT a.tgl, a.nrp, k.nama, a.jam_masuk, a.jam_keluar, a.status, a.keterangan
                        FROM absensi a
                        JOIN karyawan k ON a.nrp = k.nrp
                        WHERE MONTH(a.tgl) = ? AND YEAR(a.tgl) = ?
                        ORDER BY a.tgl, k.nama
                    """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                        rs.getDate("tgl"),
                        rs.getString("nrp"),
                        rs.getString("nama"),
                        rs.getTime("jam_masuk"),
                        rs.getTime("jam_keluar"),
                        rs.getString("status"),
                        rs.getString("keterangan")
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

            Image logo = Image.getInstance(getClass().getResource("/images/ok.png").toURI().toString());
            logo.scaleAbsolute(80, 80);
            logo.setAbsolutePosition(doc.right() - 100, doc.top() - 80);
            doc.add(logo);

            int month = getSelectedMonth();
            int year = getSelectedYear();

            String title = "LAPORAN ABSENSI BULANAN\n" +
                    new SimpleDateFormat("MMMM yyyy").format(new SimpleDateFormat("MM-yyyy").parse(month + "-" + year));

            Paragraph pTitle = new Paragraph(title, new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            pTitle.setAlignment(Element.ALIGN_CENTER);
            pTitle.setSpacingAfter(10f);
            doc.add(pTitle);
            
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
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    Object val = table.getValueAt(row, col);
                    String text = (val == null) ? "" : (val instanceof Time) ? timeFormat.format(val) : val.toString();
                    PdfPCell cell = new PdfPCell(new Phrase(text, contentFont));
                    if (col == 5 && text != null) {
                        switch (text) {
                            case "Alpha" -> cell.setBackgroundColor(new BaseColor(255, 150, 150));
                            case "Hadir" -> cell.setBackgroundColor(new BaseColor(150, 255, 150));
                            case "Telat" -> cell.setBackgroundColor(new BaseColor(255, 255, 150));
                        }
                    }
                    pdfTable.addCell(cell);
                }
            }

            doc.add(pdfTable);

            // User information
            String namaUser = Session.getCurrentUser();
            Font userFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
            Paragraph generatedBy = new Paragraph( namaUser, userFont);
            generatedBy.setAlignment(Element.ALIGN_RIGHT);
            generatedBy.setSpacingBefore(50f);

            // Add date and day information
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