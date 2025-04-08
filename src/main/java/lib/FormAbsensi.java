package lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormAbsensi extends JFrame {

    private JTextField txtId;
    private JTextField txtNama;
    private JLabel lblWaktu;
    private JComboBox<String> cbStatus;
    private Timer timer;

    public FormAbsensi() {
        initComponents();     // Buat UI
        tampilkanWaktu();     // Jam real-time
    }
    
    private Font loadCustomFont(float size, int style) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/font/BebasNeue-Regular.ttf"));
            return font.deriveFont(style, size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", style, (int) size); // fallback
        }
    }
    
    

    private void initComponents() {
        setTitle("Form Absensi");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(null);
        
    
        //==========================//

        JPanel panelJudul = new JPanel(null);
        panelJudul.setBounds(0, 5, 800, 80);
        panelJudul.setOpaque(false); // Biar transparan

        JLabel lblJudul1 = new JLabel("Halo,", SwingConstants.CENTER);
        lblJudul1.setFont(loadCustomFont(42f, Font.PLAIN));
        lblJudul1.setBounds(0, 0, 800, 35);

        JLabel lblJudul2 = new JLabel("Jangan Lupa Isi Presensi", SwingConstants.CENTER);
        lblJudul2.setFont(loadCustomFont(32f, Font.PLAIN));
        lblJudul2.setBounds(0, 35, 800, 35);

        panelJudul.add(lblJudul1);
        panelJudul.add(lblJudul2);
        mainPanel.add(panelJudul);  

        JLabel lblId = new JLabel("ID");
        lblId.setFont(loadCustomFont(18f, Font.PLAIN));
        lblId.setForeground(Color.BLACK);
        lblId.setBounds(50, 80, 100, 25);
        mainPanel.add(lblId);
        
        txtId = new JTextField();
        txtId.setBounds(50, 105, 250, 25);
        txtId.setOpaque(false); // Biar background-nya transparan
        txtId.setBackground(new Color(0, 0, 0, 0)); // Tambahan biar lebih aman
        txtId.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK)); // Border bawah aja
        txtId.setForeground(Color.BLACK); // Teks putih
        txtId.setCaretColor(Color.BLACK); // Kursor putih juga
        mainPanel.add(txtId);


        JButton btnCari = new JButton("Cari");
        btnCari.setBounds(310, 105, 70, 25);
        btnCari.setFocusPainted(false);
        btnCari.setContentAreaFilled(true);
        btnCari.setBorderPainted(false);
        btnCari.setBackground(new Color(220, 220, 220)); // Warna abu muda
        btnCari.setForeground(Color.BLACK);
        btnCari.putClientProperty("JButton.buttonType", "plain");
        mainPanel.add(btnCari);

        JLabel lblNama = new JLabel("Nama Pegawai");
        lblNama.setFont(loadCustomFont(18f, Font.PLAIN));
        lblNama.setForeground(Color.BLACK);
        lblNama.setBounds(50, 140, 150, 25);
        mainPanel.add(lblNama);

        txtNama = new JTextField();
        txtNama.setBounds(50, 165, 330, 25);
        txtNama.setOpaque(false);
        txtNama.setBackground(new Color(0, 0, 0, 0));
        txtNama.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        txtNama.setForeground(Color.BLACK);
        txtNama.setCaretColor(Color.BLACK);
        mainPanel.add(txtNama);

        JLabel lblTanggal = new JLabel("Tanggal");
        lblTanggal.setFont(loadCustomFont(18f, Font.PLAIN));
        lblTanggal.setForeground(Color.BLACK);
        lblTanggal.setBounds(50, 200, 100, 25);
        mainPanel.add(lblTanggal);

        lblWaktu = new JLabel();
        lblWaktu.setForeground(Color.BLACK);
        lblWaktu.setFont(loadCustomFont(14f, Font.PLAIN));
        lblWaktu.setBounds(50, 225, 300, 25);
        mainPanel.add(lblWaktu);

        cbStatus = new JComboBox<>(new String[]{"Hadir", "Izin", "Sakit"});
        cbStatus.setBounds(50, 270, 150, 30);
        mainPanel.add(cbStatus);

        JButton btnAbsen = new JButton("ABSEN");
        btnAbsen.setBounds(220, 270, 100, 30);
        btnAbsen.setFocusPainted(false);
        btnAbsen.setContentAreaFilled(true);
        btnAbsen.setBorderPainted(false);
        btnAbsen.setBackground(new Color(0, 102, 204)); // Biru flat
        btnAbsen.setForeground(Color.WHITE);
        btnAbsen.putClientProperty("JButton.buttonType", "plain");
        mainPanel.add(btnAbsen);

        // Logo Image
        ImageIcon originalIcon = new ImageIcon(getClass().getClassLoader().getResource("zki.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH); // sesuaikan ukuran
        ImageIcon resizedIcon = new ImageIcon(scaledImage);

        JLabel lblLogo = new JLabel(resizedIcon);
        lblLogo.setBounds(500, 100, 200, 200); // posisinya tetap bisa kamu atur
        mainPanel.add(lblLogo);



        setContentPane(mainPanel);
    }

    private void tampilkanWaktu() {
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pattern = "dd/MM/yyyy HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                lblWaktu.setText(sdf.format(new Date()));
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Gagal memuat FlatLaf");
        }

        SwingUtilities.invokeLater(() -> {
            System.out.println("Program dimulai...");
            FormAbsensi form = new FormAbsensi();
            form.setVisible(true);
        });
    }

    // Custom JPanel with solid background
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            Color solidColor = new Color(242, 242, 242); // warna solid
            g2d.setColor(solidColor);
            g2d.fillRect(0, 0, width, height);
        }
    }
}