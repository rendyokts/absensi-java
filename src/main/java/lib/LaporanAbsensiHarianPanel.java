package lib;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jdatepicker.impl.DateComponentFormatter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import java.util.Properties;

public class LaporanAbsensiHarianPanel extends JPanel {
    private JDatePickerImpl datePicker;
    private JButton generateBtn;
    private JTable table;
    private DefaultTableModel tableModel;
    private User currentUser;

    public LaporanAbsensiHarianPanel() {
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);

        // Panel Pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);

        JLabel dateLabel = new JLabel("Tanggal:");
        datePicker = createDatePicker();

        generateBtn = new JButton("Generate PDF");
        generateBtn.addActionListener(e -> generatePDF());

        searchPanel.add(dateLabel);
        searchPanel.add(datePicker);
        searchPanel.add(generateBtn);

        // Table Model
        String[] columnNames = { "NRP", "Nama", "Jam Masuk", "Jam Keluar", "Status", "Keterangan" };
        tableModel = new DefaultTableModel(columnNames, 0);

        table = new JTable(tableModel);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Load data for today by default
        loadData(new Date());
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl picker = new JDatePickerImpl(datePanel, new DateComponentFormatter());

        model.addChangeListener(e -> {
            Date selectedDate = (Date) model.getValue();
            if (selectedDate != null) {
                loadData(selectedDate);
            }
        });

        return picker;
    }

    private void loadData(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);

        try (Connection conn = Database.getConnection()) {
            String sql = """
                    SELECT
                        a.nrp,
                        k.nama,
                        a.jam_masuk,
                        a.jam_keluar,
                        a.status,
                        a.keterangan
                    FROM absensi a
                    JOIN karyawan k ON a.nrp = k.nrp
                    WHERE a.tgl = ?
                    ORDER BY k.nama
                    """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, dateStr);

            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                        rs.getString("nrp"),
                        rs.getString("nama"),
                        rs.getTime("jam_masuk"),
                        rs.getTime("jam_keluar"),
                        rs.getString("status"),
                        rs.getString("keterangan")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generatePDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan PDF");
        fileChooser.setSelectedFile(new File("Laporan_Absensi_Harian_" +
                new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();

        if (!filePath.toLowerCase().endsWith(".pdf")) {
            filePath += ".pdf";
        }

        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // === Logo di pojok kanan atas ===
            Image logo = Image.getInstance(getClass().getResource("/images/ok.png").toURI().toString());
            logo.scaleAbsolute(100, 50); // atur ukuran logo
            logo.setAbsolutePosition(document.right() - 100, document.top() - 50); // posisikan di kanan atas
            document.add(logo);

            // === Judul Laporan di tengah ===
            Date selectedDate = (Date) datePicker.getModel().getValue();
            String title = "LAPORAN ABSENSI HARIAN";

            com.itextpdf.text.Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titlePara = new Paragraph(title, titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            titlePara.setSpacingAfter(10f);
            document.add(titlePara);

            // === Info Perusahaan ===
            Font companyFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            Paragraph companyInfo = new Paragraph();
            companyInfo.add(new Chunk("PT Zona Kreatif Indonesia\n", companyFont));
            companyInfo.add(new Chunk("Jl. Raya Bogor No. 123, Jakarta\n\n", companyFont));
            companyInfo.setAlignment(Element.ALIGN_CENTER);
            companyInfo.setSpacingAfter(15f);
            document.add(companyInfo);

            // === Tabel Data ===
            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            pdfTable.setWidthPercentage(100);

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            for (int i = 0; i < table.getColumnCount(); i++) {
                PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i), headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new BaseColor(200, 200, 200));
                pdfTable.addCell(cell);
            }

            Font contentFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    Object value = table.getValueAt(row, col);
                    String text = "";

                    if (value instanceof Time) {
                        text = timeFormat.format(value);
                    } else if (value != null) {
                        text = value.toString();
                    }

                    PdfPCell cell = new PdfPCell(new Phrase(text, contentFont));

                    if (col == 4) {
                        if ("Alpha".equalsIgnoreCase(text)) {
                            cell.setBackgroundColor(new BaseColor(255, 150, 150));
                        } else if ("Telat".equalsIgnoreCase(text)) {
                            cell.setBackgroundColor(new BaseColor(255, 255, 150));
                        } else if ("Hadir".equalsIgnoreCase(text)) {
                            cell.setBackgroundColor(new BaseColor(150, 255, 150));
                        }
                    }

                    pdfTable.addCell(cell);
                }
            }

            document.add(pdfTable);

            String namaUser = Session.getCurrentUser();
            Font userFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
            Paragraph generatedBy = new Paragraph("Dicetak Oleh: " + namaUser, userFont);
            generatedBy.setAlignment(Element.ALIGN_RIGHT);
            generatedBy.setSpacingBefore(15f);
            document.add(generatedBy);

            // === Tanggal dan Footer di pojok kanan bawah ===
            Paragraph footer = new Paragraph();
            footer.setAlignment(Element.ALIGN_RIGHT);
            footer.setSpacingBefore(30f);
            footer.add(new Phrase(getTanggalIndonesia(selectedDate), companyFont));
            document.add(footer);

            document.close();

            JOptionPane.showMessageDialog(this,
                    "Laporan berhasil disimpan di:\n" + filePath,
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error generating PDF: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getTanggalIndonesia(Date date) {
        SimpleDateFormat sdfHari = new SimpleDateFormat("EEEE");
        SimpleDateFormat sdfTanggal = new SimpleDateFormat("dd");
        SimpleDateFormat sdfBulan = new SimpleDateFormat("MMMM");
        SimpleDateFormat sdfTahun = new SimpleDateFormat("yyyy");

        String hari = sdfHari.format(date);
        String tanggal = sdfTanggal.format(date);
        String bulan = sdfBulan.format(date);
        String tahun = sdfTahun.format(date);

        // Ganti ke bahasa Indonesia
        hari = hari.replace("Monday", "Senin").replace("Tuesday", "Selasa").replace("Wednesday", "Rabu")
                .replace("Thursday", "Kamis").replace("Friday", "Jumat").replace("Saturday", "Sabtu")
                .replace("Sunday", "Minggu");

        bulan = bulan.replace("January", "Januari").replace("February", "Februari")
                .replace("March", "Maret").replace("April", "April").replace("May", "Mei")
                .replace("June", "Juni").replace("July", "Juli").replace("August", "Agustus")
                .replace("September", "September").replace("October", "Oktober")
                .replace("November", "November").replace("December", "Desember");

        return hari + ", " + tanggal + " " + bulan + " " + tahun;
    }
}