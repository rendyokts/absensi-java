package lib;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class Menu extends javax.swing.JFrame {

    public Menu() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(180, 150, 255);
                Color color2 = Color.WHITE;
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        jPanelMenu = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel("Master data");
        jLabel3 = new javax.swing.JLabel("Laporan");
        jPanel2 = new javax.swing.JPanel();
        jPanelLaporan = new javax.swing.JPanel();

        jButton1 = new javax.swing.JButton("Dashboard");
        jButton2 = new javax.swing.JButton("Data Karyawan");
        jButton3 = new javax.swing.JButton("Jabatan & Departemen");
        jButton4 = new javax.swing.JButton("Jadwal Kerja");
        jButton5 = new javax.swing.JButton("Absensi Karyawan");
        jButton6 = new javax.swing.JButton("Pengajuan Cuti & Izin");

        jButton8 = new javax.swing.JButton("Laporan Absensi Harian");
        jButton9 = new javax.swing.JButton("Laporan Absensi Bulanan");
        jButton10 = new javax.swing.JButton("Laporan Karyawan");
        jButton11 = new javax.swing.JButton("Laporan Cuti & Izin");
        jButton12 = new javax.swing.JButton("Laporan Rekapitulasi Kehadiran");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(850, 500));

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/lib/logo.png"));
        Image img = logoIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(img);
        JLabel logoLabel = new JLabel(logoIcon);
        jLabel1 = new JLabel("Zona Kreasi");
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jLabel1.setForeground(new Color(80, 50, 120));

        JPanel logoTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        logoTitlePanel.setBackground(new Color(0, 0, 0, 0));
        logoTitlePanel.add(logoLabel);
        logoTitlePanel.add(jLabel1);

        jLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel3.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jPanelMenu.setLayout(new GridLayout(0, 1, 5, 5));
        jPanelMenu.setBackground(new Color(255, 255, 255));
        jPanelMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jButton1.addActionListener(evt -> showPanel(new DashboardPanel()));
        jButton2.addActionListener(evt -> showPanel(new KaryawanPanel()));
        jButton3.addActionListener(evt -> showPanel(new JabatanDepartemenPanel()));
        jButton4.addActionListener(evt -> showPanel(new JadwalKerjaPanel()));
        jButton5.addActionListener(evt -> showPanel(new AbsensiPanel()));
        jButton6.addActionListener(evt -> showPanel(new CutiIzinPanel()));

        jPanelMenu.add(jButton1);
        jPanelMenu.add(jButton2);
        jPanelMenu.add(jButton3);
        jPanelMenu.add(jButton4);
        jPanelMenu.add(jButton5);
        jPanelMenu.add(jButton6);

        jPanelLaporan.setLayout(new GridLayout(0, 1, 5, 5));
        jPanelLaporan.setBackground(new Color(255, 255, 255));

        jButton8.addActionListener(evt -> showPanel(new LaporanAbsensiHarianPanel()));
        jButton9.addActionListener(evt -> showPanel(new LaporanAbsensiBulananPanel()));
        jButton10.addActionListener(evt -> showPanel(new LaporanKaryawanPanel()));
        jButton11.addActionListener(evt -> showPanel(new LaporanCutiIzinPanel()));
        jButton12.addActionListener(evt -> showPanel(new LaporanRekapitulasiKehadiranPanel()));

        jPanelLaporan.add(jButton8);
        jPanelLaporan.add(jButton9);
        jPanelLaporan.add(jButton10);
        jPanelLaporan.add(jButton11);
        jPanelLaporan.add(jButton12);

        jPanel2.setBackground(Color.WHITE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(logoTitlePanel)
                        .addComponent(jLabel2)
                        .addComponent(jPanelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jPanelLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(20)
                    .addComponent(logoTitlePanel)
                    .addGap(15)
                    .addComponent(jLabel2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(15)
                    .addComponent(jLabel3)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanelLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(20, Short.MAX_VALUE))
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1);
        pack();
    }

    private void showPanel(JPanel panel) {
        jPanel2.removeAll();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(panel, BorderLayout.CENTER);
        jPanel2.revalidate();
        jPanel2.repaint();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Menu().setVisible(true));
    }

    // Panel Dummy
    class DashboardPanel extends JPanel {
        public DashboardPanel() {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            JLabel label = new JLabel("Selamat Datang di Dashboard", SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 18));
            label.setForeground(new Color(80, 60, 130));
            add(label, BorderLayout.CENTER);
        }
    }

    class KaryawanPanel extends JPanel {
        public KaryawanPanel() { add(new JLabel("Ini adalah halaman Data Karyawan")); }
    }
    class JabatanDepartemenPanel extends JPanel {
        public JabatanDepartemenPanel() { add(new JLabel("Ini adalah halaman Jabatan dan Departemen")); }
    }
    class JadwalKerjaPanel extends JPanel {
        public JadwalKerjaPanel() { add(new JLabel("Ini adalah halaman Jadwal Kerja")); }
    }
    class AbsensiPanel extends JPanel {
        public AbsensiPanel() { add(new JLabel("Ini adalah halaman Absensi Karyawan")); }
    }
    class LaporanAbsensiHarianPanel extends JPanel {
        public LaporanAbsensiHarianPanel() { add(new JLabel("Ini adalah Laporan Absensi Harian")); }
    }
    class LaporanAbsensiBulananPanel extends JPanel {
        public LaporanAbsensiBulananPanel() { add(new JLabel("Ini adalah Laporan Absensi Bulanan")); }
    }
    class LaporanKaryawanPanel extends JPanel {
        public LaporanKaryawanPanel() { add(new JLabel("Ini adalah Laporan Karyawan")); }
    }
    class LaporanCutiIzinPanel extends JPanel {
        public LaporanCutiIzinPanel() { add(new JLabel("Ini adalah Laporan Cuti dan Izin")); }
    }
    class LaporanRekapitulasiKehadiranPanel extends JPanel {
        public LaporanRekapitulasiKehadiranPanel() { add(new JLabel("Ini adalah Laporan Rekapitulasi Kehadiran")); }
    }

    class CutiIzinPanel extends JPanel {
        private JTable table;
        private DefaultTableModel tableModel;
        private JTextField searchField;
        private Vector<Vector<Object>> data;

        public CutiIzinPanel() {
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);
            data = new Vector<>();

            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titlePanel.setBackground(Color.WHITE);
            JLabel titleLabel = new JLabel("Pengajuan Cuti & Izin Karyawan");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titlePanel.add(titleLabel);

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topPanel.setBackground(Color.WHITE);
            JButton addButton = new JButton("+");
            addButton.setPreferredSize(new Dimension(40, 25));
            addButton.addActionListener(e -> addDataDialog());

            searchField = new JTextField();
            searchField.setPreferredSize(new Dimension(200, 25));
            searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
                public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            });

            topPanel.add(addButton);
            topPanel.add(Box.createHorizontalStrut(10));
            topPanel.add(searchField);

            String[] columnNames = {"Nama", "Jenis", "Tanggal Mulai", "Tanggal Selesai", "Keterangan"};
            tableModel = new DefaultTableModel(columnNames, 0);
            table = new JTable(tableModel);
            table.setRowHeight(25);

            JScrollPane scrollPane = new JScrollPane(table);

            add(titlePanel, BorderLayout.NORTH);
            add(topPanel, BorderLayout.CENTER);
            add(scrollPane, BorderLayout.SOUTH);
        }

        private void addDataDialog() {
            JTextField namaField = new JTextField();
            JComboBox<String> jenisCombo = new JComboBox<>(new String[]{"Cuti", "Izin (Sakit)", "Izin (WFH)", "Izin (Dinas Luar Kota)"});
            JSpinner startDate = new JSpinner(new SpinnerDateModel());
            startDate.setEditor(new JSpinner.DateEditor(startDate, "dd/MM/yyyy"));
            JSpinner endDate = new JSpinner(new SpinnerDateModel());
            endDate.setEditor(new JSpinner.DateEditor(endDate, "dd/MM/yyyy"));
            JTextField ketField = new JTextField();

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Nama:")); panel.add(namaField);
            panel.add(new JLabel("Jenis:")); panel.add(jenisCombo);
            panel.add(new JLabel("Mulai:")); panel.add(startDate);
            panel.add(new JLabel("Selesai:")); panel.add(endDate);
            panel.add(new JLabel("Keterangan:")); panel.add(ketField);

            if (JOptionPane.showConfirmDialog(null, panel, "Tambah Data", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                Vector<Object> row = new Vector<>();
                row.add(namaField.getText());
                row.add(jenisCombo.getSelectedItem());
                row.add(((JSpinner.DateEditor) startDate.getEditor()).getFormat().format(startDate.getValue()));
                row.add(((JSpinner.DateEditor) endDate.getEditor()).getFormat().format(endDate.getValue()));
                row.add(ketField.getText());

                data.add(row);
                tableModel.addRow(row);
            }
        }

        private void filterTable() {
            String keyword = searchField.getText().toLowerCase();
            tableModel.setRowCount(0);
            for (Vector<Object> row : data) {
                if (row.get(0).toString().toLowerCase().contains(keyword)) {
                    tableModel.addRow(row);
                }
            }
        }
    }

    private javax.swing.JButton jButton1, jButton2, jButton3, jButton4, jButton5, jButton6;
    private javax.swing.JButton jButton8, jButton9, jButton10, jButton11, jButton12;
    private javax.swing.JLabel jLabel1, jLabel2, jLabel3;
    private javax.swing.JPanel jPanel1, jPanel2, jPanelMenu, jPanelLaporan;
}