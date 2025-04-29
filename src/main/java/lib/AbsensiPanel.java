package lib;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbsensiPanel extends JPanel {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final boolean isAdmin;
    private final String nrp;

    public AbsensiPanel(boolean isAdmin, String nrp) {
        this.isAdmin = isAdmin;
        this.nrp = nrp;
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel(isAdmin ? "DATA ABSENSI KARYAWAN" : "HISTORI ABSENSI SAYA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titlePanel.add(titleLabel);

        // Table Model
        String[] columnNames = { "ID", "NRP", "Tanggal", "Jam Masuk", "Jam Keluar", "Status", "Keterangan" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0)
                    return Integer.class; // ID
                if (columnIndex == 2)
                    return Date.class; // Tanggal
                if (columnIndex == 3 || columnIndex == 4)
                    return Time.class; // Jam
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

        // Format tanggal dan waktu
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(2).setCellRenderer(new DateCellRenderer());
        columnModel.getColumn(3).setCellRenderer(new TimeCellRenderer());
        columnModel.getColumn(4).setCellRenderer(new TimeCellRenderer());

        JScrollPane scrollPane = new JScrollPane(table);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadData());

        JButton checkInBtn = new JButton("Check In");
        checkInBtn.addActionListener(e -> doCheckIn());

        JButton checkOutBtn = new JButton("Check Out");
        checkOutBtn.addActionListener(e -> doCheckOut());

        buttonPanel.add(refreshBtn);
        if (!isAdmin) {
            buttonPanel.add(checkInBtn);
            buttonPanel.add(checkOutBtn);
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
                sql = "SELECT * FROM absensi ORDER BY tgl DESC, nrp";
                stmt = conn.prepareStatement(sql);
            } else {
                sql = "SELECT * FROM absensi WHERE nrp = ? ORDER BY tgl DESC";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, nrp);
            }

            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0); // Clear existing data

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("nrp"),
                        rs.getDate("tgl"),
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

    private void doCheckIn() {
        try (Connection conn = Database.getConnection()) {
            // Cek apakah sudah check in hari ini
            String checkSql = "SELECT * FROM absensi WHERE nrp = ? AND tgl = CURDATE()";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, nrp);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this,
                        "Anda sudah melakukan check in hari ini",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Insert data check in
            String insertSql = "INSERT INTO absensi (nrp, tgl, jam_masuk, status) VALUES (?, CURDATE(), CURTIME(), 'Hadir')";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, nrp);
            insertStmt.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Check in berhasil dicatat",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);

            loadData(); // Refresh data
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error saat check in: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doCheckOut() {
        try (Connection conn = Database.getConnection()) {
            // Cek apakah sudah check in hari ini
            String checkSql = "SELECT * FROM absensi WHERE nrp = ? AND tgl = CURDATE()";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, nrp);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this,
                        "Anda belum melakukan check in hari ini",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (rs.getTime("jam_keluar") != null) {
                JOptionPane.showMessageDialog(this,
                        "Anda sudah melakukan check out hari ini",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Update data check out
            String updateSql = "UPDATE absensi SET jam_keluar = CURTIME() WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setInt(1, rs.getInt("id"));
            updateStmt.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Check out berhasil dicatat",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);

            loadData(); // Refresh data
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error saat check out: " + e.getMessage(),
                    "Error",
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

    // Custom cell renderer untuk waktu
    private static class TimeCellRenderer extends DefaultTableCellRenderer {
        private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Time) {
                value = sdf.format((Time) value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}
