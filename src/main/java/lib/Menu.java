package lib;

import javax.swing.*;
import java.awt.*;

public class Menu extends javax.swing.JFrame {
    private User currentUser;
    private String role;

    public Menu(User user) {
        this.currentUser = user;
        this.role = currentUser.getRole();
        initComponents();
        setLocationRelativeTo(null);
        showPanel(new DashboardPanel());
        configureMenuBasedOnRole();

        jLabel3.setVisible("admin".equals(role));
        jLabel2.setVisible("admin".equals(role));
    }

    private void configureMenuBasedOnRole() {
        if ("karyawan".equals(role)) {
            jPanelMenu.remove(jButton2);
            jPanelMenu.remove(jButton3);
            jPanelLaporan.remove(jButton8);
            jPanelLaporan.remove(jButton9);
            jPanelLaporan.remove(jButton10);
            jPanelLaporan.remove(jButton11);
            jPanelLaporan.remove(jButton12);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin logout?", "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose(); // Tutup jendela menu
            new Login().setVisible(true); // Kembali ke form login
        }
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
        jPanel2 = new javax.swing.JPanel(new BorderLayout()); // <- Penting: supaya bisa ganti-ganti panel
        jPanelLaporan = new javax.swing.JPanel();

        jButton1 = new javax.swing.JButton("Dashboard");
        jButton2 = new javax.swing.JButton("Data Karyawan");
        jButton3 = new javax.swing.JButton("Jabatan & Divisi");
        jButton4 = new javax.swing.JButton("Jadwal Kerja");
        jButton5 = new javax.swing.JButton("Absensi Karyawan");
        jButton6 = new javax.swing.JButton("Pengajuan Cuti & Izin");

        jButton8 = new javax.swing.JButton("Laporan Absensi Harian");
        jButton9 = new javax.swing.JButton("Laporan Absensi Bulanan");
        jButton10 = new javax.swing.JButton("Laporan Karyawan");
        jButton11 = new javax.swing.JButton("Laporan Cuti & Izin");
        jButton12 = new javax.swing.JButton("Laporan Rekapitulasi Kehadiran");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1200, 700));

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/logo.png"));
        Image img = logoIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(img);
        JLabel logoLabel = new JLabel(logoIcon);
        jLabel1 = new JLabel("Zona Kreatif");
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jLabel1.setForeground(new Color(80, 50, 120));

        JPanel logoTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        logoTitlePanel.setBackground(new Color(0, 0, 0, 0));
        logoTitlePanel.add(logoLabel);
        logoTitlePanel.add(jLabel1);

        jButtonLogout = new javax.swing.JButton("Logout");
        jButtonLogout.addActionListener(evt -> logout());

        jLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel3.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jPanelMenu.setLayout(new GridLayout(0, 1, 5, 5));
        jPanelMenu.setBackground(new Color(255, 255, 255));
        jPanelMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jButton1.addActionListener(evt -> showPanel(new DashboardPanel()));
        jButton2.addActionListener(evt -> showPanel(new DataKaryawanPanel()));
        jButton3.addActionListener(evt -> showPanel(new JabatanDivisi()));
        jButton4.addActionListener(evt -> {
            boolean isAdmin = "Admin".equalsIgnoreCase(currentUser.getRole());
            showPanel(new JadwalKerjaPanel(isAdmin, currentUser.getNrp()));
        });
        jButton5.addActionListener(evt -> {
            boolean isAdmin = "Admin".equalsIgnoreCase(currentUser.getRole());
            showPanel(new AbsensiPanel(isAdmin, currentUser.getNrp()));
        });
        jButton6.addActionListener(evt -> {
            boolean isAdmin = "Admin".equalsIgnoreCase(currentUser.getRole());
            showPanel(new CutiIzinPanel(isAdmin, currentUser.getNrp()));
        });
        jPanelMenu.add(jButton1);
        jPanelMenu.add(jButton2);
        jPanelMenu.add(jButton3);
        jPanelMenu.add(jButton4);
        jPanelMenu.add(jButton5);
        jPanelMenu.add(jButton6);
        jPanelMenu.add(jButtonLogout);

        jPanelLaporan.setLayout(new GridLayout(0, 1, 5, 5));
        jPanelLaporan.setBackground(new Color(255, 255, 255));

        jButton8.addActionListener(evt -> showPanel(new LaporanAbsensiHarianPanel()));
        jButton9.addActionListener(evt -> showPanel(new LaporanAbsensiBulananPanel()));
        jButton10.addActionListener(evt -> showPanel(new LaporanKaryawanPanel()));
        jButton11.addActionListener(evt -> showPanel(new LaporanCutiIzinPanel()));
        jButton12.addActionListener(evt -> showPanel(new LaporanRekapAbsensiPanel()));

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
                                        .addComponent(jPanelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3)
                                        .addComponent(jPanelLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 220,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                                .addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20)
                                .addComponent(logoTitlePanel)
                                .addGap(15)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelMenu, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelLaporan, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(20, Short.MAX_VALUE))
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jPanel1, BorderLayout.CENTER);
        pack();
    }

    private void showPanel(JPanel panel) {
        jPanel2.removeAll();
        jPanel2.add(panel, BorderLayout.CENTER);
        jPanel2.revalidate();
        jPanel2.repaint();
    }

    public static void main(String args[]) {
        // Misalnya peran pengguna diambil dari login
        String role = "karyawan"; // Gantilah sesuai dengan role pengguna yang login
        User user = new User(1, "NRP001", "admin", "Admin User",
                "admin@example.com", "123456789", role);
        java.awt.EventQueue.invokeLater(() -> new Menu(user).setVisible(true));
    }

    // Panel Dummy
    class DashboardPanel extends JPanel {
        public DashboardPanel() {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            JLabel label = new JLabel("Absensi PT Zona Kreatif Indonesia", SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 18));
            label.setForeground(new Color(80, 60, 130));
            add(label, BorderLayout.CENTER);
        }
    }

    // Deklarasi komponen GUI
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelMenu;
    javax.swing.JLabel jLabel2;
    javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelLaporan;
    private javax.swing.JButton jButton1, jButton2, jButton3, jButton4, jButton5, jButton6, jButton8, jButton9,
            jButton10, jButton11, jButton12, jButtonLogout;
    private javax.swing.JLabel jLabel1;
}
