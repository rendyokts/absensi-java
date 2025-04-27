package lib.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import lib.Database;

public class ApprovalCuti extends javax.swing.JFrame {

    public ApprovalCuti() {
        initComponents();
        tampilkanData();
    }

    private void tampilkanData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("NRP");
        model.addColumn("Jenis");
        model.addColumn("Tgl Mulai");
        model.addColumn("Tgl Selesai");
        model.addColumn("Alasan");
        model.addColumn("Status");
        model.addColumn("Tgl Pengajuan");

        try {
            String sql = "SELECT * FROM cuti_izin";
            Connection conn = Database.getConnection();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);

            while (res.next()) {
                model.addRow(new Object[] {
                        res.getInt("id"),
                        res.getString("nrp"),
                        res.getString("jenis"),
                        res.getDate("tgl_mulai"),
                        res.getDate("tgl_selesai"),
                        res.getString("alasan"),
                        res.getString("status_pengajuan"),
                        res.getDate("tgl_pengajuan")
                });
            }
            jTableCuti.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal Menampilkan Data: " + e.getMessage());
        }
    }

    private void setujuiPengajuan() {
        int selectedRow = jTableCuti.getSelectedRow();
        if (selectedRow >= 0) {
            String id = jTableCuti.getValueAt(selectedRow, 0).toString();
            try {
                String sql = "UPDATE cuti_izin SET status_pengajuan='Disetujui' WHERE id=?";
                Connection conn = Database.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, id);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Pengajuan disetujui!");
                tampilkanData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal Menyetujui: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu!");
        }
    }

    private void tolakPengajuan() {
        int selectedRow = jTableCuti.getSelectedRow();
        if (selectedRow >= 0) {
            String id = jTableCuti.getValueAt(selectedRow, 0).toString();
            try {
                String sql = "UPDATE cuti_izin SET status_pengajuan='Ditolak' WHERE id=?";
                Connection conn = Database.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, id);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Pengajuan ditolak!");
                tampilkanData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal Menolak: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu!");
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setTitle("Approval Cuti dan Izin");
        setSize(800, 450);

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCuti = new javax.swing.JTable();
        btnSetujui = new javax.swing.JButton();
        btnTolak = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        jTableCuti.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "ID", "NRP", "Jenis", "Tgl Mulai", "Tgl Selesai", "Alasan", "Status", "Tgl Pengajuan"
                }));
        jScrollPane1.setViewportView(jTableCuti);

        btnSetujui.setText("Setujui");
        btnSetujui.setBackground(java.awt.Color.GREEN); // warna hijau
        btnSetujui.setForeground(java.awt.Color.WHITE); // teks putih
        btnSetujui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setujuiPengajuan();
            }
        });

        btnTolak.setText("Tolak");
        btnTolak.setBackground(java.awt.Color.RED);
        btnTolak.setForeground(java.awt.Color.white);
        btnTolak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tolakPengajuan();
            }
        });

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tampilkanData();
            }
        });

        // Layout
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(btnSetujui)
                                .addGap(30, 30, 30)
                                .addComponent(btnTolak)
                                .addGap(30, 30, 30)
                                .addComponent(btnRefresh)
                                .addContainerGap(50, Short.MAX_VALUE)));

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnSetujui)
                                        .addComponent(btnTolak)
                                        .addComponent(btnRefresh))
                                .addGap(0, 30, Short.MAX_VALUE)));

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new ApprovalCuti().setVisible(true);
        });
    }

    private javax.swing.JButton btnSetujui;
    private javax.swing.JButton btnTolak;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableCuti;

}
