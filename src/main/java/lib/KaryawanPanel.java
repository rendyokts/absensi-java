package lib;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

public class KaryawanPanel extends JPanel {
    private final JTable table;
    private final DefaultTableModel tableModel;

    public KaryawanPanel() {
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("Data Karyawan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titlePanel.add(titleLabel);

        String[] columnNames = {"NRP", "Nama", "Jabatan", "Departemen", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);

        table = new JTable(tableModel);
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);

        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        try (Connection conn = Database.getConnection()) {
            String sql = """
                SELECT 
                    k.nrp,
                    k.nama,
                    j.nama_jabatan AS jabatan,
                    d.nama_divisi AS departemen,
                    CASE k.status WHEN 1 THEN 'Aktif' ELSE 'Non-aktif' END AS status
                FROM karyawan k
                LEFT JOIN jabatan j ON k.jabatan_id = j.id
                LEFT JOIN divisi d ON k.divisi_id = d.id
            """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("nrp"),
                    rs.getString("nama"),
                    rs.getString("jabatan"),
                    rs.getString("departemen"),
                    rs.getString("status")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal load data: " + e.getMessage());
        }
    }
}
