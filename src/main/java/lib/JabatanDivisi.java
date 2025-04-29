package lib;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

public class JabatanDivisi extends JPanel {
    private final JTable tableJabatan;
    private final JTable tableDivisi;
    private final DefaultTableModel modelJabatan;
    private final DefaultTableModel modelDivisi;

    public JabatanDivisi() {
        setLayout(new GridLayout(2, 1, 10, 10)); // 2 tabel dibagi vertikal
        setBackground(Color.WHITE);

        // --- Panel atas: Jabatan ---
        JPanel panelJabatan = new JPanel(new BorderLayout(5, 5));
        panelJabatan.setBackground(Color.WHITE);
        JLabel labelJabatan = new JLabel("Data Jabatan");
        labelJabatan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelJabatan.add(labelJabatan, BorderLayout.NORTH);

        String[] kolomJabatan = {"ID", "Nama Jabatan"};
        modelJabatan = new DefaultTableModel(kolomJabatan, 0);
        tableJabatan = new JTable(modelJabatan);
        tableJabatan.setRowHeight(25);

        JScrollPane scrollJabatan = new JScrollPane(tableJabatan);
        panelJabatan.add(scrollJabatan, BorderLayout.CENTER);

        // --- Panel bawah: Divisi ---
        JPanel panelDivisi = new JPanel(new BorderLayout(5, 5));
        panelDivisi.setBackground(Color.WHITE);
        JLabel labelDivisi = new JLabel("Data Divisi");
        labelDivisi.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelDivisi.add(labelDivisi, BorderLayout.NORTH);

        String[] kolomDivisi = {"ID", "Nama Divisi"};
        modelDivisi = new DefaultTableModel(kolomDivisi, 0);
        tableDivisi = new JTable(modelDivisi);
        tableDivisi.setRowHeight(25);

        JScrollPane scrollDivisi = new JScrollPane(tableDivisi);
        panelDivisi.add(scrollDivisi, BorderLayout.CENTER);

        // --- Add kedua panel ke main panel ---
        add(panelJabatan);
        add(panelDivisi);

        // Load data dari database
        loadDataJabatan();
        loadDataDivisi();
    }

    private void loadDataJabatan() {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT id, nama_jabatan FROM jabatan";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("nama_jabatan")
                };
                modelJabatan.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal load data Jabatan: " + e.getMessage());
        }
    }

    private void loadDataDivisi() {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT id, nama_divisi FROM divisi";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("nama_divisi")
                };
                modelDivisi.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal load data Divisi: " + e.getMessage());
        }
    }
}
