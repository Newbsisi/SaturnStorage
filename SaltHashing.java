import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;

public class SaltHashing {
    String username = "saltuser";
    String password = "Password3?";
    SecureRandom random = new SecureRandom();
    
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(salt);
    
    byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
    
    public void insertUser() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/users", "user_auth", "password");
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO login_info (username, salt, hashed_password) VALUES (?, ?, ?)")) {
            pstmt.setString(1, username);
            pstmt.setBytes(2, salt);
            pstmt.setBytes(3, hashedPassword);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SaltHashing saltHashing = new SaltHashing();
        saltHashing.insertUser();
    }
}
