package lib;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class JadwalKerjaPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private boolean isAdmin;
    private String nrp;
    private JButton addButton, editButton, deleteButton;

    public JadwalKerjaPanel(boolean isAdmin, String nrp) {
        this.isAdmin = isAdmin;
        this.nrp = nrp;
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel(isAdmin ? "SEMUA DATA JADWAL KERJA (ADMIN)" : "JADWAL KERJA SAYA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titlePanel.add(titleLabel);

        if (isAdmin) {
            addButton = new JButton("Tambah");
            editButton = new JButton("Edit");
            deleteButton = new JButton("Hapus");
            titlePanel.add(addButton);
            titlePanel.add(editButton);
            titlePanel.add(deleteButton);

            addButton.addActionListener(e -> showAddDialog());
            editButton.addActionListener(e -> showEditDialog());
            deleteButton.addActionListener(e -> deleteData());
        }

        String[] columnNames = { "ID", "NRP", "Nama", "Tanggal", "Shift", "Jam Masuk", "Jam Keluar" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.removeColumn(table.getColumnModel().getColumn(0)); // Hide ID column
        JScrollPane scrollPane = new JScrollPane(table);

        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = Database.getConnection()) {
            String sql;
            if (isAdmin) {
                sql = "SELECT jk.id, jk.nrp, k.nama, jk.tanggal, jk.shift, jk.jam_masuk, jk.jam_keluar " +
                      "FROM jadwal_kerja jk JOIN karyawan k ON jk.nrp = k.nrp";
            } else {
                sql = "SELECT jk.id, jk.nrp, k.nama, jk.tanggal, jk.shift, jk.jam_masuk, jk.jam_keluar " +
                      "FROM jadwal_kerja jk JOIN karyawan k ON jk.nrp = k.nrp WHERE jk.nrp = ?";
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            if (!isAdmin) {
                stmt.setString(1, nrp);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("nrp"),
                    rs.getString("nama"),
                    rs.getDate("tanggal"),
                    rs.getString("shift"),
                    rs.getString("jam_masuk"),
                    rs.getString("jam_keluar")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage());
        }
    }

    private void showAddDialog() {
        showFormDialog("Tambah Jadwal Kerja", null);
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!");
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int id = (int) tableModel.getValueAt(modelRow, 0);
        showFormDialog("Edit Jadwal Kerja", id);
    }

    private void showFormDialog(String title, Integer idEdit) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(7, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JComboBox<String> nrpComboBox = new JComboBox<>();
        List<String> nrpList = new ArrayList<>();
        List<String> namaList = new ArrayList<>();

        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT nrp, nama FROM karyawan ORDER BY nama ASC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nrp = rs.getString("nrp");
                String nama = rs.getString("nama");
                nrpList.add(nrp);
                namaList.add(nama);
                nrpComboBox.addItem(nrp + " - " + nama);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error load NRP/Nama: " + ex.getMessage());
        }

        JTextField tanggalField = new JTextField(LocalDate.now().toString()); // Format: yyyy-MM-dd
        JTextField shiftField = new JTextField();
        JTextField jamMasukField = new JTextField("08:00:00");
        JTextField jamKeluarField = new JTextField("17:00:00");

        JButton saveButton = new JButton("Simpan");
        JButton cancelButton = new JButton("Batal");

        dialog.add(new JLabel("NRP - Nama:"));
        dialog.add(nrpComboBox);
        dialog.add(new JLabel("Tanggal (yyyy-MM-dd):"));
        dialog.add(tanggalField);
        dialog.add(new JLabel("Shift:"));
        dialog.add(shiftField);
        dialog.add(new JLabel("Jam Masuk (HH:mm:ss):"));
        dialog.add(jamMasukField);
        dialog.add(new JLabel("Jam Keluar (HH:mm:ss):"));
        dialog.add(jamKeluarField);
        dialog.add(saveButton);
        dialog.add(cancelButton);

        // Kalau Edit, Load data lama
        if (idEdit != null) {
            try (Connection conn = Database.getConnection()) {
                String sql = "SELECT * FROM jadwal_kerja WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idEdit);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int idx = nrpList.indexOf(rs.getString("nrp"));
                    if (idx >= 0) nrpComboBox.setSelectedIndex(idx);
                    tanggalField.setText(rs.getDate("tanggal").toString());
                    shiftField.setText(rs.getString("shift"));
                    jamMasukField.setText(rs.getString("jam_masuk"));
                    jamKeluarField.setText(rs.getString("jam_keluar"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal load data: " + ex.getMessage());
            }
        }

        saveButton.addActionListener(e -> {
            try (Connection conn = Database.getConnection()) {
                String selectedNrp = nrpList.get(nrpComboBox.getSelectedIndex());
                LocalDate tanggal = LocalDate.parse(tanggalField.getText());
                String shift = shiftField.getText();
                LocalTime jamMasuk = LocalTime.parse(jamMasukField.getText());
                LocalTime jamKeluar = LocalTime.parse(jamKeluarField.getText());

                if (idEdit == null) {
                    String insertSQL = "INSERT INTO jadwal_kerja (nrp, tanggal, shift, jam_masuk, jam_keluar) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(insertSQL);
                    stmt.setString(1, selectedNrp);
                    stmt.setDate(2, java.sql.Date.valueOf(tanggal));
                    stmt.setString(3, shift);
                    stmt.setTime(4, java.sql.Time.valueOf(jamMasuk));
                    stmt.setTime(5, java.sql.Time.valueOf(jamKeluar));
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan.");
                } else {
                    String updateSQL = "UPDATE jadwal_kerja SET nrp=?, tanggal=?, shift=?, jam_masuk=?, jam_keluar=? WHERE id=?";
                    PreparedStatement stmt = conn.prepareStatement(updateSQL);
                    stmt.setString(1, selectedNrp);
                    stmt.setDate(2, java.sql.Date.valueOf(tanggal));
                    stmt.setString(3, shift);
                    stmt.setTime(4, java.sql.Time.valueOf(jamMasuk));
                    stmt.setTime(5, java.sql.Time.valueOf(jamKeluar));
                    stmt.setInt(6, idEdit);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data berhasil diupdate.");
                }

                dialog.dispose();
                loadData();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal simpan: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void deleteData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!");
            return;
        }
        int modelRow = table.convertRowIndexToModel(selectedRow);
        int id = (int) tableModel.getValueAt(modelRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin mau menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = Database.getConnection()) {
                String deleteSQL = "DELETE FROM jadwal_kerja WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(deleteSQL);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                loadData();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal hapus data: " + ex.getMessage());
            }
        }
    }
}
