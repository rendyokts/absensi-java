package lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PengajuanCuti extends JFrame {

    private JTextField txtNrp;
    private JTextField txtNama;
    private JTextField txtTanggalMulai;
    private JTextField txtTanggalSelesai;
    private JComboBox<String> cbAlasan;
    private JTextArea txtKeterangan;
    private JLabel lblTanggalPengajuan;
    private Timer timer;

    public PengajuanCuti() {
        initComponents();
        tampilkanTanggal();
    }

    private Font loadCustomFont(float size, int style) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/font/BebasNeue-Regular.ttf"));
            return font.deriveFont(style, size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", style, (int) size);
        }
    }

    private void initComponents() {
        setTitle("Form Pengajuan Cuti");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(null);

        // Title Panel
        JPanel panelJudul = new JPanel(null);
        panelJudul.setBounds(0, 5, 800, 80);
        panelJudul.setOpaque(false);

        JLabel lblJudul1 = new JLabel("Pengajuan Cuti", SwingConstants.CENTER);
        lblJudul1.setFont(loadCustomFont(42f, Font.PLAIN));
        lblJudul1.setBounds(0, 0, 800, 40);

        panelJudul.add(lblJudul1);
        mainPanel.add(panelJudul);

        // Input Fields
        JLabel lblNrp = new JLabel("NRP");
        lblNrp.setFont(loadCustomFont(18f, Font.PLAIN));
        lblNrp.setBounds(50, 100, 100, 25);
        mainPanel.add(lblNrp);

        txtNrp = new JTextField();
        txtNrp.setBounds(50, 125, 300, 25);
        txtNrp.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        mainPanel.add(txtNrp);

        JLabel lblNama = new JLabel("Nama");
        lblNama.setFont(loadCustomFont(18f, Font.PLAIN));
        lblNama.setBounds(50, 160, 100, 25);
        mainPanel.add(lblNama);

        txtNama = new JTextField();
        txtNama.setBounds(50, 185, 300, 25);
        txtNama.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        mainPanel.add(txtNama);

        JLabel lblTanggalMulai = new JLabel("Tanggal Mulai (yyyy-MM-dd)");
        lblTanggalMulai.setFont(loadCustomFont(18f, Font.PLAIN));
        lblTanggalMulai.setBounds(50, 220, 300, 25);
        mainPanel.add(lblTanggalMulai);

        txtTanggalMulai = new JTextField();
        txtTanggalMulai.setBounds(50, 245, 300, 25);
        txtTanggalMulai.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        mainPanel.add(txtTanggalMulai);

        JLabel lblTanggalSelesai = new JLabel("Tanggal Selesai (yyyy-MM-dd)");
        lblTanggalSelesai.setFont(loadCustomFont(18f, Font.PLAIN));
        lblTanggalSelesai.setBounds(50, 280, 300, 25);
        mainPanel.add(lblTanggalSelesai);

        txtTanggalSelesai = new JTextField();
        txtTanggalSelesai.setBounds(50, 305, 300, 25);
        txtTanggalSelesai.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        mainPanel.add(txtTanggalSelesai);

        JLabel lblAlasan = new JLabel("Alasan");
        lblAlasan.setFont(loadCustomFont(18f, Font.PLAIN));
        lblAlasan.setBounds(50, 340, 100, 25);
        mainPanel.add(lblAlasan);

        cbAlasan = new JComboBox<>(new String[]{"Izin", "Sakit", "Cuti"});
        cbAlasan.setBounds(50, 365, 200, 30);
        mainPanel.add(cbAlasan);

        JLabel lblKeterangan = new JLabel("Keterangan");
        lblKeterangan.setFont(loadCustomFont(18f, Font.PLAIN));
        lblKeterangan.setBounds(50, 405, 150, 25);
        mainPanel.add(lblKeterangan);

        txtKeterangan = new JTextArea();
        txtKeterangan.setLineWrap(true);
        txtKeterangan.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtKeterangan);
        scrollPane.setBounds(50, 430, 300, 60);
        mainPanel.add(scrollPane);

        lblTanggalPengajuan = new JLabel();
        lblTanggalPengajuan.setFont(loadCustomFont(14f, Font.PLAIN));
        lblTanggalPengajuan.setBounds(50, 500, 300, 25);
        mainPanel.add(lblTanggalPengajuan);

        JButton btnSubmit = new JButton("Ajukan Cuti");
        btnSubmit.setBounds(400, 500, 150, 30);
        btnSubmit.setBackground(new Color(0, 102, 204));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        mainPanel.add(btnSubmit);

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simpanKeDatabase();
            }
        });

        // Logo
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("zki.png"));
        Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(img));
        lblLogo.setBounds(500, 150, 200, 200);
        mainPanel.add(lblLogo);

        setContentPane(mainPanel);
    }

    private void tampilkanTanggal() {
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                lblTanggalPengajuan.setText("Tanggal Pengajuan: " + sdf.format(new Date()));
            }
        });
        timer.start();
    }

    private void simpanKeDatabase() {
        String url = "jdbc:mysql://localhost:3306/absensi"; // Sesuaikan
        String username = "root"; // Sesuaikan
        String password = "";     // Sesuaikan

        try {
            Connection conn = DriverManager.getConnection(url, username, password);

            String sql = "INSERT INTO cuti_izin (nrp, tgl_mulai, tgl_selesai, alasan, status_pengajuan, tgl_pengajuan) VALUES (?, ?, ?, ?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, txtNrp.getText());
            stmt.setString(2, txtTanggalMulai.getText());
            stmt.setString(3, txtTanggalSelesai.getText());
            stmt.setString(4, cbAlasan.getSelectedItem().toString());
            stmt.setString(4, txtKeterangan.getText());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Pengajuan Cuti Berhasil Disimpan!");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal Menyimpan Pengajuan Cuti.");
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Gagal load FlatLaf");
        }

        SwingUtilities.invokeLater(() -> {
            new PengajuanCuti().setVisible(true);
        });
    }

    class GradientPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(242, 242, 242));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
