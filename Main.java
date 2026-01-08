import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/moviemanagement";
    private static final String USER = "postgre";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

class MovieManagement {

    public void addMovie(String title, String director, int year) {
        String sql = "CALL add_movie(?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, title);
            stmt.setString(2, director);
            stmt.setInt(3, year);
            stmt.execute();
            System.out.println("-> Thêm phim thành công!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMovie(int id, String title, String director, int year) {
        String sql = "CALL update_movie(?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, id);
            stmt.setString(2, title);
            stmt.setString(3, director);
            stmt.setInt(4, year);
            stmt.execute();
            System.out.println("-> Cập nhật phim thành công!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMovie(int id) {
        String sql = "CALL delete_movie(?)";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, id);
            stmt.execute();
            System.out.println("-> Xóa phim thành công!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listMovies() {
        String sql = "SELECT * FROM list_movies()"; 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.printf("%-5s | %-30s | %-20s | %-10s\n", "ID", "TIÊU ĐỀ", "ĐẠO DIỄN", "NĂM");
            System.out.println("---------------------------------------------------------------------------");
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("out_id");
                String title = rs.getString("out_title");
                String director = rs.getString("out_director");
                int year = rs.getInt("out_year");
                System.out.printf("%-5d | %-30s | %-20s | %-10d\n", id, title, director, year);
            }

            if (!hasData) {
                System.out.println("(Danh sách trống)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static MovieManagement manager = new MovieManagement();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== QUẢN LÝ PHIM (PostgreSQL) ===");
            System.out.println("1. Liệt kê phim");
            System.out.println("2. Thêm phim mới");
            System.out.println("3. Cập nhật phim");
            System.out.println("4. Xóa phim");
            System.out.println("0. Thoát");
            System.out.print("Chọn chức năng: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập số!");
                continue;
            }

            switch (choice) {
                case 1:
                    manager.listMovies();
                    break;
                case 2:
                    handleAdd();
                    break;
                case 3:
                    handleUpdate();
                    break;
                case 4:
                    handleDelete();
                    break;
                case 0:
                    System.out.println("Thoát chương trình.");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void handleAdd() {
        try {
            System.out.print("Nhập tiêu đề: ");
            String title = scanner.nextLine();
            if (title.isEmpty()) throw new Exception("Tiêu đề không được để trống");

            System.out.print("Nhập đạo diễn: ");
            String director = scanner.nextLine();
            if (director.isEmpty()) throw new Exception("Đạo diễn không được để trống");

            System.out.print("Nhập năm phát hành: ");
            int year = Integer.parseInt(scanner.nextLine());

            manager.addMovie(title, director, year);
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Năm phải là số nguyên!");
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private static void handleUpdate() {
        try {
            manager.listMovies();
            System.out.print("Nhập ID phim cần sửa: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Nhập tiêu đề mới: ");
            String title = scanner.nextLine();
            
            System.out.print("Nhập đạo diễn mới: ");
            String director = scanner.nextLine();
            
            System.out.print("Nhập năm phát hành mới: ");
            int year = Integer.parseInt(scanner.nextLine());

            manager.updateMovie(id, title, director, year);
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: ID hoặc Năm phải là số nguyên!");
        }
    }

    private static void handleDelete() {
        try {
            System.out.print("Nhập ID phim cần xóa: ");
            int id = Integer.parseInt(scanner.nextLine());
            manager.deleteMovie(id);
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: ID phải là số nguyên!");
        }
    }
}
