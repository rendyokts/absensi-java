package lib;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DataKaryawanPanel extends JPanel {
    private JButton tambahBtn;
    private JTable table;
    private DefaultTableModel tableModel;

    public DataKaryawanPanel() {
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel( "DATA KARYAWAN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        filterPanel.add(titleLabel);
        
        tambahBtn = new JButton("Tambah Karyawan");
        tambahBtn.addActionListener(e -> tambahKaryawan());
        
        filterPanel.add(tambahBtn);
        
        tableModel = new DefaultTableModel(new String[] {
                "NRP", "Nama", "Alamat", "Status", "Jabatan"
        }, 0);

        table = new JTable(tableModel);
        table.setRowHeight(25);

        add(filterPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT k.nrp, k.nama, k.alamat, k.status, j.nama_jabatan " +
                    "FROM karyawan k " +
                    "JOIN jabatan j ON k.jabatan_id = j.id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0);
            while (rs.next()) {
                String status = rs.getInt("status") == 1 ? "Aktif" : "Tidak Aktif";
                Object[] row = {
                        rs.getString("nrp"),
                        rs.getString("nama"),
                        rs.getString("alamat"),
                        status,
                        rs.getString("nama_jabatan")
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tambahKaryawan() {
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

        JComboBox<String> userCombo = new JComboBox<>();
        JTextField nrpField = new JTextField();
        JTextField namaField = new JTextField();
        JTextField alamatField = new JTextField();
        JComboBox<String> jabatanCombo = new JComboBox<>();
        JComboBox<String> divisiCombo = new JComboBox<>();
        JComboBox<String> statusCombo = new JComboBox<>(new String[] { "1 - Aktif", "0 - Tidak Aktif" });

        nrpField.setEditable(false);
        namaField.setEditable(false);

        Map<String, Integer> userMap = new HashMap<>();
        Map<String, Integer> jabatanMap = new HashMap<>();
        Map<String, Integer> divisiMap = new HashMap<>();

        try (Connection conn = Database.getConnection()) {
            // Load users who don't already have karyawan records
            String userSql = "SELECT u.id, u.nrp, u.name AS nama FROM users u " +
                    "WHERE NOT EXISTS (SELECT 1 FROM karyawan k WHERE k.user_id = u.id)";
            PreparedStatement userStmt = conn.prepareStatement(userSql);
            ResultSet userRs = userStmt.executeQuery();
            while (userRs.next()) {
                String nama = userRs.getString("nama");
                int id = userRs.getInt("id");
                userCombo.addItem(nama);
                userMap.put(nama, id);
            }

            // Load jabatan
            String jabatanSql = "SELECT id, nama_jabatan FROM jabatan";
            PreparedStatement jabatanStmt = conn.prepareStatement(jabatanSql);
            ResultSet jabatanRs = jabatanStmt.executeQuery();
            while (jabatanRs.next()) {
                String nama = jabatanRs.getString("nama_jabatan");
                int id = jabatanRs.getInt("id");
                jabatanCombo.addItem(nama);
                jabatanMap.put(nama, id);
            }

            // Load divisi
            String divisiSql = "SELECT id, nama_divisi FROM divisi";
            PreparedStatement divisiStmt = conn.prepareStatement(divisiSql);
            ResultSet divisiRs = divisiStmt.executeQuery();
            while (divisiRs.next()) {
                String nama = divisiRs.getString("nama_divisi");
                int id = divisiRs.getInt("id");
                divisiCombo.addItem(nama);
                divisiMap.put(nama, id);
            }

            // Generate unique NRP
            String newNrp = generateUniqueNrp(conn);
            nrpField.setText(newNrp);

            // Update namaField ketika user dipilih
            userCombo.addActionListener(e -> {
                String selectedUser = (String) userCombo.getSelectedItem();
                if (selectedUser != null) {
                    try {
                        PreparedStatement stmt = conn.prepareStatement("SELECT name FROM users WHERE id = ?");
                        stmt.setInt(1, userMap.get(selectedUser));
                        ResultSet rs = stmt.executeQuery();
                        if (rs.next()) {
                            namaField.setText(rs.getString("name"));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // Inisialisasi data pertama
            if (userCombo.getItemCount() > 0) {
                userCombo.setSelectedIndex(0);
            }

            form.add(new JLabel("NRP:"));
            form.add(nrpField);
            form.add(new JLabel("Pilih User:"));
            form.add(userCombo);
            form.add(new JLabel("Nama:"));
            form.add(namaField);
            form.add(new JLabel("Alamat:"));
            form.add(alamatField);
            form.add(new JLabel("Jabatan:"));
            form.add(jabatanCombo);
            form.add(new JLabel("Divisi:"));
            form.add(divisiCombo);
            form.add(new JLabel("Status:"));
            form.add(statusCombo);

            int result = JOptionPane.showConfirmDialog(this, form, "Tambah Karyawan", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String nrp = nrpField.getText();
                String nama = namaField.getText();
                String alamat = alamatField.getText();
                int jabatanId = jabatanMap.get((String) jabatanCombo.getSelectedItem());
                int divisiId = divisiMap.get((String) divisiCombo.getSelectedItem());
                String statusText = (String) statusCombo.getSelectedItem();
                int status = Integer.parseInt(statusText.split(" - ")[0]);
                int userId = userMap.get((String) userCombo.getSelectedItem());

                try {
                    conn.setAutoCommit(false); // Mulai transaksi

                    // Insert ke tabel karyawan
                    String insertSql = "INSERT INTO karyawan (nrp, user_id, nama, alamat, divisi_id, jabatan_id, status) "
                            +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                    insertStmt.setString(1, nrp);
                    insertStmt.setInt(2, userId);
                    insertStmt.setString(3, nama);
                    insertStmt.setString(4, alamat);
                    insertStmt.setInt(5, divisiId);
                    insertStmt.setInt(6, jabatanId);
                    insertStmt.setInt(7, status);
                    insertStmt.executeUpdate();

                    // Update field NRP di tabel users
                    String updateSql = "UPDATE users SET nrp = ? WHERE id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setString(1, nrp);
                    updateStmt.setInt(2, userId);
                    updateStmt.executeUpdate();

                    conn.commit(); // Commit transaksi

                    JOptionPane.showMessageDialog(this, "Data karyawan berhasil ditambahkan.", "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } catch (Exception ex) {
                    conn.rollback(); // Rollback jika ada error
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Gagal tambah karyawan: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    conn.setAutoCommit(true); // Kembalikan ke default
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal tambah karyawan: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateUniqueNrp(Connection conn) throws SQLException {
        // First check the maximum NRP in karyawan table
        String maxNrpQuery = "SELECT MAX(nrp) FROM karyawan WHERE nrp LIKE 'NRP%'";
        PreparedStatement maxNrpStmt = conn.prepareStatement(maxNrpQuery);
        ResultSet maxNrpRs = maxNrpStmt.executeQuery();

        int nextNumber = 1; // Default starting number

        if (maxNrpRs.next()) {
            String maxNrp = maxNrpRs.getString(1);
            if (maxNrp != null && maxNrp.startsWith("NRP")) {
                try {
                    int lastNumber = Integer.parseInt(maxNrp.substring(3));
                    nextNumber = lastNumber + 1;
                } catch (NumberFormatException e) {
                    // If parsing fails, keep default number
                }
            }
        }

        // Format with leading zeros (NRP001, NRP002, etc.)
        return String.format("NRP%03d", nextNumber);
    }
}