import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.*;
import org.apache.commons.codec.binary.Hex;

public class SaltHashing {
    SecureRandom random = new SecureRandom();

    byte[] salt;
    byte[] hashedPassword;

    MessageDigest md;

    public SaltHashing(String username, String password, boolean isAdmin) {
        salt = new byte[4];
        random.nextBytes(salt);
        System.out.println(salt);

        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        insertUser(username, isAdmin ? 1: 0);
    }

    public void insertUser(String username, int isAdmin) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/users", "user_auth", "password");
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO login_info (username, salt, hashed_password, isAdmin) VALUES (?, ?, ?, ?)"); 
            pstmt.setString(1, username);
            pstmt.setString(2, Hex.encodeHexString(salt));
            pstmt.setBytes(3, hashedPassword);
            pstmt.setInt(4, isAdmin);
            pstmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
