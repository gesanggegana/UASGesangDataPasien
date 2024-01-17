import java.sql.*;
import java.util.Scanner;


public class CRUDPasien {

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/datapasien";
    static final String USER = "root";
    static final String PASSWORD = "";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            createTable(connection);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("1. Tambah Pasien");
                System.out.println("2. Tampilkan Pasien");
                System.out.println("3. Update Pasien");
                System.out.println("4. Hapus Pasien");
                System.out.println("5. Keluar");
                System.out.print("Pilih operasi (1-5): ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        tambahPasien(connection);
                        break;
                    case 2:
                        tampilkanPasien(connection);
                        break;
                    case 3:
                        updatePasien(connection);
                        break;
                    case 4:
                        hapusPasien(connection);
                        break;
                    case 5:
                        System.out.println("Program berakhir.");
                        System.exit(0);
                    default:
                        System.out.println("Pilihan tidak valid. Silakan pilih lagi.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS pasien (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nama VARCHAR(255) NOT NULL," +
                "alamat VARCHAR(255) NOT NULL," +
                "umur INT NOT NULL," +
                "jenis_kelamin VARCHAR(10) NOT NULL," +
                "keluhan TEXT)";
        
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
    }

    private static void tambahPasien(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan nama pasien: ");
        String nama = scanner.nextLine();

        System.out.print("Masukkan alamat pasien: ");
        String alamat = scanner.nextLine();

        System.out.print("Masukkan umur pasien: ");
        int umur = scanner.nextInt();

        System.out.print("Masukkan jenis kelamin pasien: ");
        String jenisKelamin = scanner.next();

        scanner.nextLine(); // Membersihkan buffer

        System.out.print("Masukkan keluhan pasien: ");
        String keluhan = scanner.nextLine();

        String insertSQL = "INSERT INTO pasien (nama, alamat, umur, jenis_kelamin, keluhan) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, nama);
            preparedStatement.setString(2, alamat);
            preparedStatement.setInt(3, umur);
            preparedStatement.setString(4, jenisKelamin);
            preparedStatement.setString(5, keluhan);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Pasien berhasil ditambahkan.");
            } else {
                System.out.println("Gagal menambahkan pasien.");
            }
        }
    }

    private static void tampilkanPasien(Connection connection) throws SQLException {
        String selectSQL = "SELECT * FROM pasien";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nama = resultSet.getString("nama");
                String alamat = resultSet.getString("alamat");
                int umur = resultSet.getInt("umur");
                String jenisKelamin = resultSet.getString("jenis_kelamin");
                String keluhan = resultSet.getString("keluhan");

                System.out.println("ID: " + id);
                System.out.println("Nama: " + nama);
                System.out.println("Alamat: " + alamat);
                System.out.println("Umur: " + umur);
                System.out.println("Jenis Kelamin: " + jenisKelamin);
                System.out.println("Keluhan: " + keluhan);
                System.out.println("-----------------------------");
            }
        }
    }

    private static void updatePasien(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan ID pasien yang akan diupdate: ");
        int idToUpdate = scanner.nextInt();

        scanner.nextLine(); // Membersihkan buffer

        System.out.print("Masukkan keluhan pasien yang baru: ");
        String newKeluhan = scanner.nextLine();

        String updateSQL = "UPDATE pasien SET keluhan = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, newKeluhan);
            preparedStatement.setInt(2, idToUpdate);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data pasien berhasil diupdate.");
            } else {
                System.out.println("Gagal mengupdate data pasien. ID tidak ditemukan.");
            }
        }
    }

    private static void hapusPasien(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan ID pasien yang akan dihapus: ");
        int idToDelete = scanner.nextInt();

        String deleteSQL = "DELETE FROM pasien WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, idToDelete);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data pasien berhasil dihapus.");
            } else {
                System.out.println("Gagal menghapus data pasien. ID tidak ditemukan.");
            }
        }
    }
}
