package lib;

import javax.swing.*;
import javax.swing.border.AbstractBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.net.URL;
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
            Session.setCurrentUser("");
            this.dispose();
            new Login().setVisible(true);
        }
    }

    private void initComponents() {
        // Set look and feel modern
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jPanel1 = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(100, 120, 220);
                Color color2 = new Color(245, 245, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        jPanel2 = new javax.swing.JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background warna putih
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                // Shadow effect
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        jPanel2.setOpaque(false);
        jPanel2.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        jPanelMenu = new javax.swing.JPanel();
        jPanelLaporan = new javax.swing.JPanel();

        jLabel1 = new javax.swing.JLabel("Zona Kreatif");
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 22));
        jLabel1.setForeground(new Color(255, 255, 255));

        jLabel2 = new javax.swing.JLabel("Master Data");
        jLabel2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jLabel2.setForeground(new Color(255, 255, 255));

        jLabel3 = new javax.swing.JLabel("Laporan");
        jLabel3.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jLabel3.setForeground(new Color(255, 255, 255));

        JLabel logoLabel = new JLabel();
        try {
            URL logoUrl = getClass().getResource("/images/logo.png");
            if (logoUrl != null) {
                ImageIcon logoIcon = new ImageIcon(logoUrl);
                Image img = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                logoIcon = new ImageIcon(img);
                logoLabel.setIcon(logoIcon);
            } else {
                JLabel iconText = new JLabel("ZK");
                iconText.setFont(new Font("Segoe UI", Font.BOLD, 18));
                iconText.setForeground(Color.WHITE);
                iconText.setBackground(new Color(60, 80, 170));
                iconText.setOpaque(true);
                iconText.setHorizontalAlignment(SwingConstants.CENTER);
                iconText.setPreferredSize(new Dimension(40, 40));
                logoLabel = iconText;
            }
        } catch (Exception ex) {
            System.err.println("Failed to load logo: " + ex.getMessage());
            JLabel iconText = new JLabel("ZK");
            iconText.setFont(new Font("Segoe UI", Font.BOLD, 18));
            iconText.setForeground(Color.WHITE);
            iconText.setBackground(new Color(60, 80, 170));
            iconText.setOpaque(true);
            iconText.setHorizontalAlignment(SwingConstants.CENTER);
            iconText.setPreferredSize(new Dimension(40, 40));
            logoLabel = iconText;
        }

        JPanel logoTitlePanel = new JPanel();
        logoTitlePanel.setLayout(new BoxLayout(logoTitlePanel, BoxLayout.X_AXIS));
        logoTitlePanel.setBackground(new Color(0, 0, 0, 40));
        logoTitlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        logoTitlePanel.add(logoLabel);
        logoTitlePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        logoTitlePanel.add(jLabel1);

        setupMenuButtons();
        setupLaporanButtons();

        jButtonLogout = createStyledButton("Logout", getSafeIcon("/images/logout.png"));
        jButtonLogout.setBackground(new Color(220, 80, 80));
        jButtonLogout.setForeground(Color.WHITE);
        jButtonLogout.addActionListener(evt -> logout());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1280, 768));
        setTitle("Zona Kreatif - Sistem Manajemen Karyawan");

        configureLayout(logoTitlePanel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jPanel1, BorderLayout.CENTER);

        showPanel(new DashboardPanel(Database.getConnection(), currentUser.getNrp(), currentUser.getRole()));

        pack();
        setLocationRelativeTo(null);
    }

    private JButton createStyledButton(String text, ImageIcon icon) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (icon != null) {
            button.setIcon(icon);
            button.setIconTextGap(10);
        }

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 90, 180));
                button.setContentAreaFilled(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setContentAreaFilled(false);
            }
        });

        return button;
    }

    private ImageIcon getSafeIcon(String path) {
        try {
            URL iconUrl = getClass().getResource(path);
            if (iconUrl != null) {
                return new ImageIcon(iconUrl);
            }
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + path);
        }
        return null;
    }

    private void setupMenuButtons() {
        jPanelMenu.setLayout(new BoxLayout(jPanelMenu, BoxLayout.Y_AXIS));
        jPanelMenu.setOpaque(false);
        jPanelMenu.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        boolean isAdmin = "admin".equalsIgnoreCase(currentUser.getRole());

        jButton1 = createStyledButton("Dashboard", getSafeIcon("/images/dashboard.png"));
        jButton2 = createStyledButton("Data Karyawan", getSafeIcon("/images/employee.png"));
        jButton3 = createStyledButton("Jabatan & Divisi", getSafeIcon("/images/organization.png"));
        jButton4 = createStyledButton("Jadwal Kerja", getSafeIcon("/images/schedule.png"));
        jButton5 = createStyledButton("Absensi Karyawan", getSafeIcon("/images/attendance.png"));
        jButton6 = createStyledButton("Pengajuan Cuti & Izin", getSafeIcon("/images/leave.png"));

        jButton1.addActionListener(evt -> showPanel(
                new DashboardPanel(Database.getConnection(), currentUser.getNrp(), currentUser.getRole())));

        jButton4.addActionListener(evt -> {
            showPanel(new JadwalKerjaPanel(isAdmin, currentUser.getNrp()));
        });
        jButton5.addActionListener(evt -> {
            showPanel(new AbsensiPanel(isAdmin, currentUser.getNrp()));
        });
        jButton6.addActionListener(evt -> {
            showPanel(new CutiIzinPanel(isAdmin, currentUser.getNrp()));
        });

        if (isAdmin) {
            jButton2.addActionListener(evt -> showPanel(new DataKaryawanPanel()));
            jButton3.addActionListener(evt -> showPanel(new JabatanDivisi()));
            jButton4.addActionListener(evt -> showPanel(new JadwalKerjaPanel(true, currentUser.getNrp())));
        }

        addButtonToPanel(jPanelMenu, jButton1);
        addButtonToPanel(jPanelMenu, jButton4);
        addButtonToPanel(jPanelMenu, jButton6);
        addButtonToPanel(jPanelMenu, jButton5);

        if (isAdmin) {
            addButtonToPanel(jPanelMenu, jButton3);
            addButtonToPanel(jPanelMenu, jButton2);
        }
    }

    private void setupLaporanButtons() {
        jPanelLaporan.setLayout(new BoxLayout(jPanelLaporan, BoxLayout.Y_AXIS));
        jPanelLaporan.setOpaque(false);
        jPanelLaporan.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        boolean isAdmin = "admin".equalsIgnoreCase(currentUser.getRole());

        jButton8 = createStyledButton("Laporan Absensi Harian", getSafeIcon("/images/report-daily.png"));
        jButton9 = createStyledButton("Laporan Absensi Bulanan", getSafeIcon("/images/report-monthly.png"));
        jButton10 = createStyledButton("Laporan Karyawan", getSafeIcon("/images/report-employee.png"));
        jButton11 = createStyledButton("Laporan Cuti & Izin", getSafeIcon("/images/report-leave.png"));
        jButton12 = createStyledButton("Rekapitulasi Kehadiran", getSafeIcon("/images/report-summary.png"));

        jButton8.addActionListener(evt -> showPanel(new LaporanAbsensiHarianPanel()));
        jButton9.addActionListener(evt -> showPanel(new LaporanAbsensiBulananPanel()));
        jButton10.addActionListener(evt -> showPanel(new LaporanKaryawanPanel()));
        jButton11.addActionListener(evt -> showPanel(new LaporanCutiIzinPanel()));
        jButton12.addActionListener(evt -> showPanel(new LaporanRekapAbsensiPanel()));

        addButtonToPanel(jPanelLaporan, jButton8);
        addButtonToPanel(jPanelLaporan, jButton9);
        addButtonToPanel(jPanelLaporan, jButton10);
        addButtonToPanel(jPanelLaporan, jButton11);
        addButtonToPanel(jPanelLaporan, jButton12);

        jPanelLaporan.setVisible(isAdmin);
        jLabel3.setVisible(isAdmin);
    }

    private void addButtonToPanel(JPanel panel, JButton button) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(button, BorderLayout.CENTER);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        panel.add(buttonPanel);
    }

    private void configureLayout(JPanel logoTitlePanel) {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(30, 50, 100, 180));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));

        sidebarPanel.add(logoTitlePanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel masterDataHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        masterDataHeaderPanel.setOpaque(false);
        masterDataHeaderPanel.add(jLabel2);
        sidebarPanel.add(masterDataHeaderPanel);
        sidebarPanel.add(jPanelMenu);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel laporanHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        laporanHeaderPanel.setOpaque(false);
        laporanHeaderPanel.add(jLabel3);
        sidebarPanel.add(laporanHeaderPanel);
        sidebarPanel.add(jPanelLaporan);

        sidebarPanel.add(Box.createVerticalGlue());

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoutPanel.setOpaque(false);
        logoutPanel.add(jButtonLogout);
        sidebarPanel.add(logoutPanel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(sidebarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
                                .addGap(15)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(sidebarPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(15)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(15)));
    }

    private void showPanel(JPanel panel) {
        jPanel2.removeAll();
        jPanel2.add(panel, BorderLayout.CENTER);
        jPanel2.revalidate();
        jPanel2.repaint();
    }

    public static void main(String args[]) {
        String role = "karyawan";
        User user = new User(1, "NRP001", "admin", "Admin User",
                "admin@example.com", "123456789", role);
        java.awt.EventQueue.invokeLater(() -> new Menu(user).setVisible(true));
    }

    class DashboardPanel extends JPanel {
        private Connection conn;
        private String nrp;
        private String role;
        private final Color PRIMARY_COLOR = new Color(50, 87, 168);
        private final Color TEXT_DARK = new Color(51, 51, 51);
        private final Color BACKGROUND = new Color(245, 247, 250);
        private final Color CARD_BG = Color.WHITE;

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
                JPanel headerPanel = createHeaderPanel();
                JPanel cardsPanel = createCardsPanel();

                add(headerPanel, BorderLayout.NORTH);
                add(cardsPanel, BorderLayout.CENTER);

            } else {
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

            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    BorderFactory.createCompoundBorder(
                            new ShadowBorder(3, Color.LIGHT_GRAY),
                            BorderFactory.createEmptyBorder(20, 20, 20, 20))));

            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setBackground(CARD_BG);
            titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

            JPanel colorStrip = new JPanel();
            colorStrip.setBackground(accentColor);
            colorStrip.setPreferredSize(new Dimension(50, 5));
            titlePanel.add(colorStrip, BorderLayout.NORTH);

            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            titleLabel.setForeground(TEXT_DARK);
            titlePanel.add(titleLabel, BorderLayout.CENTER);

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
