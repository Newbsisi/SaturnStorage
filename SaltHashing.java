import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.*;
import java.lang.Object;
import org.apache.commons.codec.binary.Hex;

public class SaltHashing<random> {

    String username = "salthashing";
    String password = "1234Abcd!";
    SecureRandom random = new SecureRandom();

    byte[] salt;
    byte[] hashedPassword;

    MessageDigest md;

    public SaltHashing() {
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
    }

    public void insertUser() {
        try {
        Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/users", "user_auth", "password");
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO login_info (username, salt, hashed_password) VALUES (?, ?, ?)"); 
            pstmt.setString(1, username);
            pstmt.setString(2, Hex.encodeHexString(salt));
            pstmt.setBytes(3, hashedPassword);
            pstmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SaltHashing saltHashing = new SaltHashing();
        saltHashing.insertUser();
    }
}

