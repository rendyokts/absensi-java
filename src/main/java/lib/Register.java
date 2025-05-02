package lib;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Form Registrasi Pengguna
 * Author: THINKPAD
 */
public class Register extends JFrame {

    // Konstruktor
    public Register() {
        initComponents();
        setLocationRelativeTo(null); // Menempatkan form di tengah layar
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Inisialisasi komponen GUI
    private void initComponents() {
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        passwordLabel = new JLabel();
        usernameLabel = new JLabel();
        emailLabel = new JLabel();
        nameLabel = new JLabel();
        teleponLabel = new JLabel();
        nameTextField = new JTextField();
        confirmPasswordLabel = new JLabel();
        usernameTextField = new JTextField();
        emailTextField = new JTextField();
        teleponTextField = new JTextField();
        passwordTextField = new JPasswordField();
        confirmPasswordTextField = new JPasswordField();
        registerButton = new JButton();
        jLabel3 = new JLabel();
        loginButtonRedirect = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setIcon(new ImageIcon(getClass().getResource("/images/zona-kreatif.png")));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24));
        jLabel2.setText("Register");

        nameLabel.setText("Name");
        usernameLabel.setText("Username");
        emailLabel.setText("Email");
        teleponLabel.setText("Telepon");
        passwordLabel.setText("Password");
        confirmPasswordLabel.setText("Confirm Password");

        teleponTextField.addActionListener(evt -> teleponTextFieldActionPerformed(evt));
        passwordTextField.addActionListener(evt -> passwordTextFieldActionPerformed(evt));

        registerButton.setBackground(new java.awt.Color(0, 0, 153));
        registerButton.setForeground(new java.awt.Color(255, 255, 255));
        registerButton.setText("Register");
        registerButton.addActionListener(evt -> registerButtonActionPerformed(evt));

        jLabel3.setText("Already have an account?");

        loginButtonRedirect.setBackground(new java.awt.Color(204, 0, 0));
        loginButtonRedirect.setForeground(new java.awt.Color(255, 255, 255));
        loginButtonRedirect.setText("Login");
        loginButtonRedirect.addActionListener(evt -> loginButtonRedirectActionPerformed(evt));

        // Layout menggunakan GroupLayout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel2)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(nameLabel)
                                                        .addComponent(usernameLabel)
                                                        .addComponent(emailLabel)
                                                        .addComponent(teleponLabel)
                                                        .addComponent(passwordLabel)
                                                        .addComponent(confirmPasswordLabel)
                                                        .addComponent(jLabel3))
                                                .addGap(20, 20, 20)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(nameTextField)
                                                        .addComponent(usernameTextField)
                                                        .addComponent(emailTextField)
                                                        .addComponent(teleponTextField)
                                                        .addComponent(passwordTextField)
                                                        .addComponent(confirmPasswordTextField)
                                                        .addComponent(loginButtonRedirect, GroupLayout.DEFAULT_SIZE,
                                                                160, Short.MAX_VALUE)))
                                        .addComponent(registerButton, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(nameLabel)
                                        .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(usernameLabel)
                                        .addComponent(usernameTextField, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(emailLabel)
                                        .addComponent(emailTextField, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(teleponLabel)
                                        .addComponent(teleponTextField, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(passwordLabel)
                                        .addComponent(passwordTextField, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(confirmPasswordLabel)
                                        .addComponent(confirmPasswordTextField, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addComponent(registerButton)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(loginButtonRedirect))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jLabel1));

        pack();
    }

    // Validasi pendaftaran dasar
    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String name = nameTextField.getText().trim();
        String username = usernameTextField.getText().trim();
        String email = emailTextField.getText().trim();
        String phone = teleponTextField.getText().trim();
        String password = passwordTextField.getText().trim();
        String confirmPassword = confirmPasswordTextField.getText().trim();

        // Basic validation
        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()
                || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = Database.getConnection();
            if (conn != null) {
                // Check if username already exists
                String checkSql = "SELECT id FROM users WHERE username = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Username already taken, please try another.", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    conn.close();
                    return;
                }

                // Save data into the database
                String insertSql = "INSERT INTO users (username, name, email, no_hp, password) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(insertSql);
                pst.setString(1, username);
                pst.setString(2, name);
                pst.setString(3, email);
                pst.setString(4, phone);
                pst.setString(5, hashPassword(password));

                int inserted = pst.executeUpdate();
                if (inserted > 0) {
                    JOptionPane.showMessageDialog(this, "Registration successful!");
                    // Reset fields
                    nameTextField.setText("");
                    usernameTextField.setText("");
                    emailTextField.setText("");
                    teleponTextField.setText("");
                    passwordTextField.setText("");
                    confirmPasswordTextField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }

                conn.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void teleponTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        // Bisa ditambahkan validasi angka di sini jika diperlukan
    }

    private void passwordTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        // Kosongkan jika tidak diperlukan
    }

    private void loginButtonRedirectActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Redirecting to Login...");
        // Tambahkan logika redirect ke login form
        this.dispose(); // menutup form register
        new Login().setVisible(true); // jika ada form Login
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new Register().setVisible(true));
    }

    // Deklarasi variabel GUI
    private JLabel confirmPasswordLabel, jLabel1, jLabel2, jLabel3, nameLabel, passwordLabel, teleponLabel,
            usernameLabel, emailLabel;
    private JTextField confirmPasswordTextField, nameTextField, passwordTextField, teleponTextField, usernameTextField,
            emailTextField;
    private JButton loginButtonRedirect, registerButton;
}
