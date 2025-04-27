package lib.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import lib.Database;

import java.awt.*;
import java.sql.*;

public class PersetujuanCuti extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public PersetujuanCuti() {
        setTitle("Pengajuan Cuti dan Izin");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new String[] {
                "No", "NRP", "Jenis", "Mulai", "Selesai", "Alasan", "Pengajuan", "Status", "Aksi"
        }, 0);

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Kolom Aksi pakai ButtonRenderer dan ButtonEditor
        table.getColumn("Aksi").setCellRenderer(new ButtonRenderer());
        table.getColumn("Aksi").setCellEditor(new ButtonEditor(new JCheckBox(), this));

        add(scrollPane, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        model.setRowCount(0); // Clear data tabel

        try (Connection conn = Database.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT id, nrp, jenis, tgl_mulai, tgl_selesai, alasan, tgl_pengajuan, status_pengajuan FROM cuti_izin")) {

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("id"), // Simpan id untuk update status
                        rs.getString("nrp"),
                        rs.getString("jenis"),
                        rs.getDate("tgl_mulai"),
                        rs.getDate("tgl_selesai"),
                        rs.getString("alasan"),
                        rs.getDate("tgl_pengajuan"),
                        rs.getString("status_pengajuan"),
                        "View"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal mengambil data: " + ex.getMessage());
        }
    }

    public void lihatDetail(int row) {
        int idCuti = (int) model.getValueAt(row, 0); // kolom 0 adalah id
        String[] options = { "Setujui", "Tolak" };
        int pilihan = JOptionPane.showOptionDialog(this,
                "Apakah Anda ingin menyetujui atau menolak pengajuan ini?",
                "Persetujuan",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (pilihan == 0 || pilihan == 1) {
            String statusBaru = (pilihan == 0) ? "Disetujui" : "Ditolak";
            updateStatus(idCuti, statusBaru);
        }
    }

    private void updateStatus(int idCuti, String status) {
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn
                        .prepareStatement("UPDATE cuti_izin SET status_pengajuan = ? WHERE id = ?")) {

            ps.setString(1, status);
            ps.setInt(2, idCuti);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Status berhasil diperbarui menjadi " + status);
            loadData(); // refresh tabel
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal update status: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PersetujuanCuti().setVisible(true));
    }
}
