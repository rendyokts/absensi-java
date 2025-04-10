package lib;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class RekapAbsensi extends JFrame {

    private JLabel lblBulan;
    private JLabel lblTahun;
    private JTextField txtFilter;
    private JTable table;

    public RekapAbsensi() {
        initUI();
    }
    
    private Font loadCustomFont(float size, int style) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/font/BebasNeue-Regular.ttf"));
            return font.deriveFont(style, size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", style, (int) size); // fallback
        }
    }

    private void initUI() {
        FlatLightLaf.setup();
        UIManager.put("defaultFont", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("Table.alternateRowColor", new Color(245, 245, 245)); // warna strip alternatif

        setTitle("Rekap Absensi Karyawan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 680);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== Header =====
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel("REKAP ABSENSI KARYAWAN");
        lblTitle.setFont(loadCustomFont(42f, Font.PLAIN));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("PT. ZONA KREATIF INDONESIA");
        lblSubtitle.setFont(loadCustomFont(18f, Font.PLAIN));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(lblTitle);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(lblSubtitle);

        // ===== Filter & Info Panel =====
        JPanel filterPanel = new JPanel(new BorderLayout());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        lblBulan = new JLabel("BULAN: -");
        lblTahun = new JLabel("TAHUN: -");
        lblBulan.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTahun.setFont(new Font("SansSerif", Font.BOLD, 14));
        infoPanel.add(lblBulan);
        infoPanel.add(lblTahun);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtFilter = new JTextField(20);
        JButton btnFilter = new JButton("Filter");
        inputPanel.add(txtFilter);
        inputPanel.add(btnFilter);
        
        JButton btnExport = new JButton("Export");
        inputPanel.add(btnExport);
        btnExport.addActionListener(e -> exportToCSV());

        filterPanel.add(infoPanel, BorderLayout.WEST);
        filterPanel.add(inputPanel, BorderLayout.EAST);

        // ===== Tabel =====
        // === Setup tabel ===
        String[] columnNames = {"No.", "Nama Pegawai", "Departemen", "Tanggal", "Kehadiran", "Jam Kerja"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
        scrollPane.getVerticalScrollBar().setBackground(new Color(250, 250, 250));

        // Tabel Style - HALUS
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setFillsViewportHeight(true);

        // Grid tipis seperti Excel
        table.setIntercellSpacing(new Dimension(1, 1)); // jarak antar cell kecil
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setShowGrid(true);
        table.setGridColor(new Color(235, 235, 235)); // grid sangat terang, halus

        // Header custom
        // HEADER: rata kiri + padding
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.LEFT); // ⬅️ rata kiri
                label.setFont(new Font("SansSerif", Font.BOLD, 14));
                label.setBackground(new Color(245, 245, 245)); // ⬅️ tetap pakai warna sebelumnya
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)), // garis tepi
                        BorderFactory.createEmptyBorder(0, 8, 0, 0) // padding kiri
                ));
                return label;
            }
        });


        // === ISI TABEL: rata kiri + padding ===
        DefaultTableCellRenderer isiRataKiri = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.LEFT);
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(235, 235, 235)), // grid
                        BorderFactory.createEmptyBorder(0, 8, 0, 0) // padding kiri
                ));
                return label;
            }
        };

        // Terapkan ke semua kolom isi tabel
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(isiRataKiri);
        }


        // Tambah dummy data
        for (int i = 1; i <= 20; i++) {
            tableModel.addRow(new Object[]{i + ".", "John Doe", "IT", "10-04-2025", "Hadir", "08:00 - 17:00"});
        }
        

        // Panel Utama
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(filterPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);

        // Filter bulan-tahun
        btnFilter.addActionListener(e -> {
            String input = txtFilter.getText().trim();
            if (!input.isEmpty()) {
                String[] parts = input.split(" ");
                if (parts.length == 2) {
                    lblBulan.setText("BULAN: " + parts[0].toUpperCase());
                    lblTahun.setText("TAHUN: " + parts[1]);
                }
            }
        });
    }
    
    private void exportToCSV() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Simpan sebagai");
    int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getName().endsWith(".csv")) {
                    fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".csv");
                }

                java.io.FileWriter fw = new java.io.FileWriter(fileToSave);

                // Header
                for (int i = 0; i < table.getColumnCount(); i++) {
                    fw.write(table.getColumnName(i));
                    if (i < table.getColumnCount() - 1) fw.write(",");
                }
                fw.write("\n");

                // Data
                for (int i = 0; i < table.getRowCount(); i++) {
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        fw.write("\"" + table.getValueAt(i, j).toString() + "\"");
                        if (j < table.getColumnCount() - 1) fw.write(",");
                    }
                    fw.write("\n");
                }

                fw.flush();
                fw.close();

                JOptionPane.showMessageDialog(this, "Data berhasil diexport ke file CSV!");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat export!");
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new RekapAbsensi().setVisible(true);
        });
    }
}