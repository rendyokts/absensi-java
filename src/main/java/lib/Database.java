package lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/absensi";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public static Connection getConnection() {
        try {
            // Memuat driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Membuka koneksi database
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Koneksi ke database berhasil!");
            return conn; // Kembalikan koneksi jika berhasil
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Koneksi ke database gagal: " + e.getMessage());
        }
        return null; // Jika gagal, kembalikan null
    }

    public static void main(String[] args) {
        // Tes koneksi
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.close(); // Tutup koneksi setelah digunakan
                System.out.println("Koneksi ditutup.");
            } catch (SQLException e) {
                System.err.println("Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }
}
