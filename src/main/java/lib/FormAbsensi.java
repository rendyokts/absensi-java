package lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormAbsensi extends JFrame {

    private JTextField txtId;
    private JTextField txtNama;
    private JLabel lblWaktu;
    private JComboBox<String> cbStatus;
    private Timer timer;

    public FormAbsensi() {
        initComponents();
        URL testUrl = getClass().getClassLoader().getResource("images/zona-kreatif.png");
            System.out.println("Gambar ditemukan di: " + testUrl);
            tampilkanWaktu();
    }

    private void initComponents() {
        setTitle("Form Absensi");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(null);

        JLabel lblTitle = new JLabel("Halo, Jangan Lupa Untuk Mengisi Presensi Kehadiran Yaa");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(30, 20, 600, 30);
        mainPanel.add(lblTitle);

        JLabel lblId = new JLabel("ID");
        lblId.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblId.setForeground(Color.WHITE);
        lblId.setBounds(50, 80, 100, 25);
        mainPanel.add(lblId);

        txtId = new JTextField();
        txtId.setBounds(50, 105, 250, 25);
        mainPanel.add(txtId);

        JButton btnCari = new JButton("Cari");
        btnCari.setBounds(310, 105, 70, 25);
        mainPanel.add(btnCari);

        JLabel lblNama = new JLabel("Nama Pegawai");
        lblNama.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNama.setForeground(Color.WHITE);
        lblNama.setBounds(50, 140, 150, 25);
        mainPanel.add(lblNama);

        txtNama = new JTextField();
        txtNama.setBounds(50, 165, 330, 25);
        txtNama.setEditable(false);
        mainPanel.add(txtNama);

        JLabel lblTanggal = new JLabel("Tanggal");
        lblTanggal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTanggal.setForeground(Color.WHITE);
        lblTanggal.setBounds(50, 200, 100, 25);
        mainPanel.add(lblTanggal);

        lblWaktu = new JLabel();
        lblWaktu.setForeground(Color.WHITE);
        lblWaktu.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblWaktu.setBounds(50, 225, 300, 25);
        mainPanel.add(lblWaktu);

        cbStatus = new JComboBox<>(new String[]{"Hadir", "Izin", "Sakit"});
        cbStatus.setBounds(50, 270, 150, 30);
        mainPanel.add(cbStatus);

        JButton btnAbsen = new JButton("ABSEN");
        btnAbsen.setBounds(220, 270, 100, 30);
        mainPanel.add(btnAbsen);

        // Logo Image
        ImageIcon logo = new ImageIcon(getClass().getClassLoader().getResource("ok.png"));   
        JLabel lblLogo = new JLabel(logo);
        lblLogo.setBounds(500, 100, 200, 200);
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
        System.out.println("Program dimulai...");
        FormAbsensi form = new FormAbsensi();
            form.setVisible(true);
    }

    // Custom JPanel with Gradient
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            Color color1 = new Color(136, 96, 255);
            Color color2 = new Color(0, 212, 255);
            GradientPaint gp = new GradientPaint(0, 0, color1, width, height, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }
}
