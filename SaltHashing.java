import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.*;
import java.lang.Object;

public class SaltHashing<random> {

    String username = "saltuser";
    String password = "Password3?";
    SecureRandom random = new SecureRandom();

    byte[] salt;
    byte[] hashedPassword;

    MessageDigest md;

    public SaltHashing() {
        salt = new byte[16];
        random.nextBytes(salt);

        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void insertUser() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/users", "user_auth", "password");
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO login_info (username, salt, hashed_password) VALUES (?, ?, ?)")) {
            pstmt.setString(1, username);
            pstmt.setString(2, DatatypeConverter.printHexBinary(salt));
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

