package lib;

import javax.swing.*;
import javax.swing.border.AbstractBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class Menu extends javax.swing.JFrame {
    private User currentUser;
    private String role;

    public Menu(User user) {
        this.currentUser = user;
        this.role = currentUser.getRole();
        initComponents();
        setLocationRelativeTo(null);
        showPanel(new DashboardPanel(Database.getConnection(), currentUser.getNrp(), currentUser.getRole()));
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
        jButtonLogout.addActionListener(_ -> logout());

        jLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel3.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jPanelMenu.setLayout(new GridLayout(0, 1, 5, 5));
        jPanelMenu.setBackground(new Color(255, 255, 255));
        jPanelMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jButton1.addActionListener(_ -> showPanel(
                new DashboardPanel(Database.getConnection(), currentUser.getNrp(), currentUser.getRole())));
        jButton2.addActionListener(_ -> showPanel(new DataKaryawanPanel()));
        jButton3.addActionListener(_ -> showPanel(new JabatanDivisi()));
        jButton4.addActionListener(_ -> {
            boolean isAdmin = "Admin".equalsIgnoreCase(currentUser.getRole());
            showPanel(new JadwalKerjaPanel(isAdmin, currentUser.getNrp()));
        });
        jButton5.addActionListener(_ -> {
            boolean isAdmin = "Admin".equalsIgnoreCase(currentUser.getRole());
            showPanel(new AbsensiPanel(isAdmin, currentUser.getNrp()));
        });
        jButton6.addActionListener(_ -> {
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

        jButton8.addActionListener(_ -> showPanel(new LaporanAbsensiHarianPanel()));
        jButton9.addActionListener(_ -> showPanel(new LaporanAbsensiBulananPanel()));
        jButton10.addActionListener(_ -> showPanel(new LaporanKaryawanPanel()));
        jButton11.addActionListener(_ -> showPanel(new LaporanCutiIzinPanel()));
        jButton12.addActionListener(_ -> showPanel(new LaporanRekapAbsensiPanel()));

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
        private Connection conn;
        private String nrp;
        private String role;
        // Warna tema baru yang lebih modern
        private final Color PRIMARY_COLOR = new Color(50, 87, 168); // Biru yang lebih modern
        private final Color TEXT_DARK = new Color(51, 51, 51); // Teks gelap
        private final Color BACKGROUND = new Color(245, 247, 250); // Background light gray
        private final Color CARD_BG = Color.WHITE; // Kartu putih

        public DashboardPanel(Connection conn, String nrp, String role) {
            this.conn = conn;
            this.nrp = nrp;
            this.role = role;
        
            setLayout(new BorderLayout(0, 20));
            setBackground(BACKGROUND);
            setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
            if ("admin".equalsIgnoreCase(role)) {
                // Jika admin, tampilkan chart dashboard
                JLabel title = new JLabel("Dashboard Admin", SwingConstants.CENTER);
                title.setFont(new Font("Segoe UI", Font.BOLD, 20));
                add(title, BorderLayout.NORTH);
        
                JPanel chartPanel = createAdminChartsPanel();
                add(chartPanel, BorderLayout.CENTER);
        
            } else if ("karyawan".equalsIgnoreCase(role)) {
                // Jika karyawan, tampilkan dashboard karyawan
                JPanel headerPanel = createHeaderPanel();
                JPanel cardsPanel = createCardsPanel();
        
                add(headerPanel, BorderLayout.NORTH);
                add(cardsPanel, BorderLayout.CENTER);
                
            } else {
                // Role tidak dikenali
                removeAll();
                setLayout(new BorderLayout());
                JLabel label = new JLabel("Akses ditolak: Role tidak dikenal", SwingConstants.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 18));
                label.setForeground(Color.RED);
                add(label, BorderLayout.CENTER);
            }
        }
        

        private JPanel createAdminChartsPanel() {
            JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
            panel.setBackground(BACKGROUND);

            panel.add(createChartPanel("Absensi Harian", getDataAbsensiHarian()));
            panel.add(createChartPanel("Pengajuan Cuti", getDataPengajuan("Cuti")));
            panel.add(createChartPanel("Pengajuan Izin", getDataPengajuan("Izin")));

            return panel;
        }

        private Map<String, Integer> getDataAbsensiHarian() {
            Map<String, Integer> data = new LinkedHashMap<>();
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT tgl, COUNT(*) AS total FROM absensi " +
                                "GROUP BY tgl ORDER BY tgl DESC LIMIT 7");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    data.put(rs.getString("tgl"), rs.getInt("total"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return data;
        }

        private Map<String, Integer> getDataPengajuan(String jenis) {
            Map<String, Integer> data = new LinkedHashMap<>();
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT tgl_pengajuan, COUNT(*) AS total FROM cuti_izin " +
                                "WHERE jenis = ? GROUP BY tgl_pengajuan ORDER BY tgl_pengajuan DESC LIMIT 7");
                ps.setString(1, jenis);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    data.put(rs.getString("tgl_pengajuan"), rs.getInt("total"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return data;
        }

        private JPanel createChartPanel(String title, Map<String, Integer> dataMap) {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Map.Entry<String, Integer> entry : dataMap.entrySet()) {
                dataset.addValue(entry.getValue(), title, entry.getKey());
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    title, "Tanggal", "Jumlah", dataset,
                    PlotOrientation.VERTICAL, false, true, false);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(300, 250));
            chartPanel.setBackground(BACKGROUND);

            return chartPanel;
        }

        private JPanel createHeaderPanel() {
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(BACKGROUND);
            headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

            // Judul dengan styling yang lebih baik
            JLabel titleLabel = new JLabel("Dashboard Absensi", SwingConstants.LEFT);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titleLabel.setForeground(PRIMARY_COLOR);

            // Subtitle
            JLabel subtitleLabel = new JLabel("PT Zona Kreatif Indonesia", SwingConstants.LEFT);
            subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            subtitleLabel.setForeground(TEXT_DARK);

            JPanel titlePanel = new JPanel(new GridLayout(2, 1));
            titlePanel.setBackground(BACKGROUND);
            titlePanel.add(titleLabel);
            titlePanel.add(subtitleLabel);

            headerPanel.add(titlePanel, BorderLayout.WEST);

            // Tambahkan tanggal saat ini di sisi kanan
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
            String dateString = dateFormat.format(new Date());
            JLabel dateLabel = new JLabel(dateString);
            dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            dateLabel.setForeground(TEXT_DARK);
            headerPanel.add(dateLabel, BorderLayout.EAST);

            return headerPanel;
        }

        private JPanel createCardsPanel() {
            JPanel cardPanel = new JPanel(new GridLayout(1, 3, 20, 0));
            cardPanel.setBackground(BACKGROUND);

            // Data untuk kartu
            Object[][] cardData = {
                    { "Sisa Cuti", getSisaCuti() + "", "hari", new Color(76, 175, 80) },
                    { "Hari Kerja", getHariKerja() + "", "hari", new Color(33, 150, 243) },
                    { "Jumlah Izin", getJumlahIzin() + "", "izin", new Color(255, 152, 0) }
            };

            // Membuat kartu untuk setiap data
            for (Object[] data : cardData) {
                cardPanel.add(createModernCard(
                        (String) data[0],
                        (String) data[1],
                        (String) data[2],
                        (Color) data[3]));
            }

            return cardPanel;
        }

        private JPanel createModernCard(String title, String value, String unit, Color accentColor) {
            JPanel card = new JPanel();
            card.setLayout(new BorderLayout());
            card.setBackground(CARD_BG);

            // Shadow border effect menggunakan compound border
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    BorderFactory.createCompoundBorder(
                            new ShadowBorder(3, Color.LIGHT_GRAY),
                            BorderFactory.createEmptyBorder(20, 20, 20, 20))));

            // Panel untuk judul dengan strip warna di atas
            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setBackground(CARD_BG);
            titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

            // Strip warna di bagian atas
            JPanel colorStrip = new JPanel();
            colorStrip.setBackground(accentColor);
            colorStrip.setPreferredSize(new Dimension(50, 5));
            titlePanel.add(colorStrip, BorderLayout.NORTH);

            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            titleLabel.setForeground(TEXT_DARK);
            titlePanel.add(titleLabel, BorderLayout.CENTER);

            // Panel untuk nilai
            JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            valuePanel.setBackground(CARD_BG);

            JLabel valueLabel = new JLabel(value);
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
            valueLabel.setForeground(accentColor);

            JLabel unitLabel = new JLabel(" " + unit);
            unitLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            unitLabel.setForeground(TEXT_DARK);

            valuePanel.add(valueLabel);
            valuePanel.add(unitLabel);

            card.add(titlePanel, BorderLayout.NORTH);
            card.add(valuePanel, BorderLayout.CENTER);

            return card;
        }

        // Kelas untuk membuat efek shadow pada kartu
        private class ShadowBorder extends AbstractBorder {
            private final int shadowSize;
            private final Color shadowColor;

            public ShadowBorder(int size, Color color) {
                shadowSize = size;
                shadowColor = color;
            }

            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(shadowColor);
                for (int i = 0; i < shadowSize; i++) {
                    g.drawRect(x + i, y + i, width - 1 - (i * 2), height - 1 - (i * 2));
                }
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
            }
        }

        private int getSisaCuti() {
            int totalDisetujui = 0;
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT SUM(DATEDIFF(tgl_selesai, tgl_mulai) + 1) AS total_cuti " +
                                "FROM cuti_izin WHERE nrp = ? AND jenis = 'Cuti' AND status_pengajuan = 'Disetujui'");
                ps.setString(1, nrp);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getObject("total_cuti") != null) {
                    totalDisetujui = rs.getInt("total_cuti");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 12 - totalDisetujui;
        }

        private int getHariKerja() {
            int totalHariKerja = 0;
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT COUNT(*) AS total_hari FROM absensi " +
                                "WHERE nrp = ? AND status IN ('Hadir', 'Telat')");
                ps.setString(1, nrp);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    totalHariKerja = rs.getInt("total_hari");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return totalHariKerja;
        }

        private int getJumlahIzin() {
            int jumlahIzin = 0;
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT COUNT(*) AS jumlah FROM cuti_izin " +
                                "WHERE nrp = ? AND jenis = 'Izin' AND status_pengajuan = 'Disetujui'");
                ps.setString(1, nrp);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    jumlahIzin = rs.getInt("jumlah");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return jumlahIzin;
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
