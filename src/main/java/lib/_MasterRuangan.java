package lib;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class _MasterRuangan extends javax.swing.JFrame {
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel model;

    public _MasterRuangan() {
        initComponents();
        connect();
        load_table();
    }

    public void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_penyewaan_gedung", "root", "");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Koneksi Gagal: " + ex.getMessage());
        }
    }

    public void load_table() {
        model = new DefaultTableModel(new String[]{"Kode", "Nama Ruangan", "Lokasi", "Luas", "Kapasitas", "Harga Sewa", "Catatan"}, 0);
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM ruangan");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("kode"),
                    rs.getString("nama_ruangan"),
                    rs.getString("lokasi"),
                    rs.getDouble("luas"),
                    rs.getInt("kapasitas"),
                    rs.getDouble("harga_sewa"),
                    rs.getString("catatan")
                });
            }
            tableRuangan.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Load Data: " + e.getMessage());
        }
    }

    public void clear() {
        txtKode.setText("");
        txtNamaRuangan.setText("");
        txtLokasi.setText("");
        txtLuas.setText("");
        txtKapasitas.setText("");
        txtHargaSewa.setText("");
        txtCatatan.setText("");
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            pst = conn.prepareStatement("INSERT INTO ruangan VALUES (?, ?, ?, ?, ?, ?, ?)");
            pst.setString(1, txtKode.getText());
            pst.setString(2, txtNamaRuangan.getText());
            pst.setString(3, txtLokasi.getText());
            pst.setDouble(4, Double.parseDouble(txtLuas.getText()));
            pst.setInt(5, Integer.parseInt(txtKapasitas.getText()));
            pst.setDouble(6, Double.parseDouble(txtHargaSewa.getText()));
            pst.setString(7, txtCatatan.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan");
            load_table();
            clear();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Simpan Data: " + e.getMessage());
        }
    }

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            pst = conn.prepareStatement("UPDATE ruangan SET nama_ruangan=?, lokasi=?, luas=?, kapasitas=?, harga_sewa=?, catatan=? WHERE kode=?");
            pst.setString(1, txtNamaRuangan.getText());
            pst.setString(2, txtLokasi.getText());
            pst.setDouble(3, Double.parseDouble(txtLuas.getText()));
            pst.setInt(4, Integer.parseInt(txtKapasitas.getText()));
            pst.setDouble(5, Double.parseDouble(txtHargaSewa.getText()));
            pst.setString(6, txtCatatan.getText());
            pst.setString(7, txtKode.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data Berhasil Diedit");
            load_table();
            clear();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Edit Data: " + e.getMessage());
        }
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            pst = conn.prepareStatement("DELETE FROM ruangan WHERE kode=?");
            pst.setString(1, txtKode.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus");
            load_table();
            clear();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Hapus Data: " + e.getMessage());
        }
    }

    private void tableRuanganMouseClicked(java.awt.event.MouseEvent evt) {
        int baris = tableRuangan.getSelectedRow();
        txtKode.setText(model.getValueAt(baris, 0).toString());
        txtNamaRuangan.setText(model.getValueAt(baris, 1).toString());
        txtLokasi.setText(model.getValueAt(baris, 2).toString());
        txtLuas.setText(model.getValueAt(baris, 3).toString());
        txtKapasitas.setText(model.getValueAt(baris, 4).toString());
        txtHargaSewa.setText(model.getValueAt(baris, 5).toString());
        txtCatatan.setText(model.getValueAt(baris, 6).toString());
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        lblIdentitas = new javax.swing.JLabel();
        lblNamaNpm = new javax.swing.JLabel();
        lblKode = new javax.swing.JLabel();
        lblNamaRuangan = new javax.swing.JLabel();
        lblLokasi = new javax.swing.JLabel();
        lblLuas = new javax.swing.JLabel();
        lblKapasitas = new javax.swing.JLabel();
        lblHargaSewa = new javax.swing.JLabel();
        lblCatatan = new javax.swing.JLabel();

        txtKode = new javax.swing.JTextField();
        txtNamaRuangan = new javax.swing.JTextField();
        txtLokasi = new javax.swing.JTextField();
        txtLuas = new javax.swing.JTextField();
        txtKapasitas = new javax.swing.JTextField();
        txtHargaSewa = new javax.swing.JTextField();
        txtCatatan = new javax.swing.JTextArea();

        btnSave = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        tableRuangan = new javax.swing.JTable();
        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Master Ruangan");

        lblIdentitas.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblIdentitas.setText("Master Data Ruangan");

        lblNamaNpm.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblNamaNpm.setText("Rendi Oktavian - 202243502465");

        lblKode.setText("Kode Ruangan:");

        lblNamaRuangan.setText("Nama Ruangan:");

        lblLokasi.setText("Lokasi:");

        lblLuas.setText("Luas (m²):");

        lblKapasitas.setText("Kapasitas:");

        lblHargaSewa.setText("Harga Sewa:");

        lblCatatan.setText("Catatan:");

        btnSave.setText("Simpan");
        btnSave.addActionListener(this::btnSaveActionPerformed);

        btnEdit.setText("Edit");
        btnEdit.addActionListener(this::btnEditActionPerformed);

        btnDelete.setText("Hapus");
        btnDelete.addActionListener(this::btnDeleteActionPerformed);

        tableRuangan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Kode", "Nama Ruangan", "Lokasi", "Luas", "Kapasitas", "Harga Sewa", "Catatan"}
        ));
        tableRuangan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableRuanganMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableRuangan);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblIdentitas)
                    .addComponent(lblKode)
                    .addComponent(lblNamaRuangan)
                    .addComponent(lblLokasi)
                    .addComponent(lblLuas)
                    .addComponent(lblKapasitas)
                    .addComponent(lblHargaSewa)
                    .addComponent(lblCatatan)
                    .addComponent(txtKode)
                    .addComponent(txtNamaRuangan)
                    .addComponent(txtLokasi)
                    .addComponent(txtLuas)
                    .addComponent(txtKapasitas)
                    .addComponent(txtHargaSewa)
                    .addComponent(txtCatatan)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelete)))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNamaNpm, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdentitas)
                    .addComponent(lblNamaNpm))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblKode)
                        .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNamaRuangan)
                        .addComponent(txtNamaRuangan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblLokasi)
                        .addComponent(txtLokasi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblLuas)
                        .addComponent(txtLuas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblKapasitas)
                        .addComponent(txtKapasitas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblHargaSewa)
                        .addComponent(txtHargaSewa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCatatan)
                        .addComponent(txtCatatan, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSave)
                            .addComponent(btnEdit)
                            .addComponent(btnDelete)))
                    .addComponent(jScrollPane1))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new _MasterRuangan().setVisible(true);
        });
    }

    // Komponen GUI
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnDelete;
    private javax.swing.JLabel lblIdentitas;
    private javax.swing.JLabel lblNamaNpm;
    private javax.swing.JLabel lblKode;
    private javax.swing.JLabel lblNamaRuangan;
    private javax.swing.JLabel lblLokasi;
    private javax.swing.JLabel lblLuas;
    private javax.swing.JLabel lblKapasitas;
    private javax.swing.JLabel lblHargaSewa;
    private javax.swing.JLabel lblCatatan;
    private javax.swing.JTable tableRuangan;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtNamaRuangan;
    private javax.swing.JTextField txtLokasi;
    private javax.swing.JTextField txtLuas;
    private javax.swing.JTextField txtKapasitas;
    private javax.swing.JTextField txtHargaSewa;
    private javax.swing.JTextArea txtCatatan;
}
