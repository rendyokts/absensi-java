package lib;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CutiIzinPanel extends JPanel {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final boolean isAdmin;
    private final String nrp;

    public CutiIzinPanel(boolean isAdmin, String nrp) {
        this.isAdmin = isAdmin;
        this.nrp = nrp;
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel(isAdmin ? "MANAJEMEN CUTI/IZIN KARYAWAN" : "PENGAJUAN CUTI/IZIN SAYA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titlePanel.add(titleLabel);

        // Table Model
        String[] columnNames = { "ID", "NRP", "Nama", "Jenis", "Tgl Mulai", "Tgl Selesai", "Alasan", "Status",
                "Tgl Pengajuan" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0)
                    return Integer.class; // ID
                if (columnIndex == 3 || columnIndex == 4 || columnIndex == 7)
                    return Date.class; // Tanggal
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Non-editable table
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(true);

        // Format tanggal
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(3).setCellRenderer(new DateCellRenderer());
        columnModel.getColumn(4).setCellRenderer(new DateCellRenderer());
        columnModel.getColumn(7).setCellRenderer(new DateCellRenderer());

        JScrollPane scrollPane = new JScrollPane(table);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadData());

        JButton ajukanBtn = new JButton("Ajukan Baru");
        ajukanBtn.addActionListener(e -> showPengajuanDialog());

        buttonPanel.add(refreshBtn);
        if (!isAdmin) {
            buttonPanel.add(ajukanBtn);
        } else {
            JButton approveBtn = new JButton("Setujui");
            approveBtn.addActionListener(e -> updateStatus("Disetujui"));

            JButton rejectBtn = new JButton("Tolak");
            rejectBtn.addActionListener(e -> updateStatus("Ditolak"));

            buttonPanel.add(approveBtn);
            buttonPanel.add(rejectBtn);
        }

        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        try (Connection conn = Database.getConnection()) {
            String sql;
            PreparedStatement stmt;

            if (isAdmin) {
                sql = "SELECT c.*, k.nama FROM cuti_izin c JOIN karyawan k ON c.nrp = k.nrp ORDER BY status_pengajuan, tgl_pengajuan DESC";
                stmt = conn.prepareStatement(sql);
            } else {
                sql = "SELECT c.*, k.nama FROM cuti_izin c JOIN karyawan k ON c.nrp = k.nrp WHERE c.nrp = ? ORDER BY tgl_pengajuan DESC";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, nrp);
            }

            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0); // Clear existing data

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("nrp"),
                        rs.getString("nama"), // ← nama dari tabel karyawan
                        rs.getString("jenis"),
                        rs.getDate("tgl_mulai"),
                        rs.getDate("tgl_selesai"),
                        rs.getString("alasan"),
                        rs.getString("status_pengajuan"),
                        rs.getDate("tgl_pengajuan")
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

    private void showPengajuanDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Pengajuan Cuti/Izin");
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));

        // Jenis Cuti/Izin
        JLabel jenisLabel = new JLabel("Jenis:");
        JComboBox<String> jenisCombo = new JComboBox<>(new String[] { "Cuti", "Izin", "Sakit" });

        // Tanggal Mulai
        JLabel mulaiLabel = new JLabel("Tanggal Mulai:");
        JTextField mulaiField = new JTextField();
        mulaiField.setEditable(false);
        JButton mulaiBtn = new JButton("Pilih Tanggal");
        mulaiBtn.addActionListener(e -> {
            String date = JOptionPane.showInputDialog(dialog, "Tanggal Mulai (YYYY-MM-DD):", "2023-01-01");
            if (date != null) {
                mulaiField.setText(date);
            }
        });

        JPanel mulaiPanel = new JPanel(new BorderLayout());
        mulaiPanel.add(mulaiField, BorderLayout.CENTER);
        mulaiPanel.add(mulaiBtn, BorderLayout.EAST);

        // Tanggal Selesai
        JLabel selesaiLabel = new JLabel("Tanggal Selesai:");
        JTextField selesaiField = new JTextField();
        selesaiField.setEditable(false);
        JButton selesaiBtn = new JButton("Pilih Tanggal");
        selesaiBtn.addActionListener(e -> {
            String date = JOptionPane.showInputDialog(dialog, "Tanggal Selesai (YYYY-MM-DD):", "2023-01-01");
            if (date != null) {
                selesaiField.setText(date);
            }
        });

        JPanel selesaiPanel = new JPanel(new BorderLayout());
        selesaiPanel.add(selesaiField, BorderLayout.CENTER);
        selesaiPanel.add(selesaiBtn, BorderLayout.EAST);

        // Alasan
        JLabel alasanLabel = new JLabel("Alasan:");
        JTextArea alasanField = new JTextArea(3, 20);
        alasanField.setLineWrap(true);
        JScrollPane alasanScroll = new JScrollPane(alasanField);

        formPanel.add(jenisLabel);
        formPanel.add(jenisCombo);
        formPanel.add(mulaiLabel);
        formPanel.add(mulaiPanel);
        formPanel.add(selesaiLabel);
        formPanel.add(selesaiPanel);
        formPanel.add(alasanLabel);
        formPanel.add(alasanScroll);

        JButton submitBtn = new JButton("Submit");
        submitBtn.addActionListener(e -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date tglMulai = sdf.parse(mulaiField.getText());
                Date tglSelesai = sdf.parse(selesaiField.getText());

                submitPengajuan(
                        (String) jenisCombo.getSelectedItem(),
                        tglMulai,
                        tglSelesai,
                        alasanField.getText());
                dialog.dispose();
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(submitBtn, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void submitPengajuan(String jenis, Date tglMulai, Date tglSelesai, String alasan) {
        if (tglMulai == null || tglSelesai == null) {
            throw new IllegalArgumentException("Tanggal harus diisi");
        }

        if (tglSelesai.before(tglMulai)) {
            throw new IllegalArgumentException("Tanggal selesai harus setelah tanggal mulai");
        }

        if (alasan == null || alasan.trim().isEmpty()) {
            throw new IllegalArgumentException("Alasan harus diisi");
        }

        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO cuti_izin (nrp, jenis, tgl_mulai, tgl_selesai, alasan, status_pengajuan, tgl_pengajuan) "
                    +
                    "VALUES (?, ?, ?, ?, ?, 'Diproses', CURDATE())";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nrp);
            stmt.setString(2, jenis);
            stmt.setDate(3, new java.sql.Date(tglMulai.getTime()));
            stmt.setDate(4, new java.sql.Date(tglSelesai.getTime()));
            stmt.setString(5, alasan);

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Pengajuan berhasil dikirim",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan pengajuan: " + e.getMessage());
        }
    }

    private void updateStatus(String status) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data yang akan diupdate",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE cuti_izin SET status_pengajuan = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, id);

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Status berhasil diupdate",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);

            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Custom cell renderer untuk tanggal
    private static class DateCellRenderer extends DefaultTableCellRenderer {
        private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Date) {
                value = sdf.format((Date) value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}