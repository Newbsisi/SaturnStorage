import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.*;
import org.apache.commons.codec.binary.Hex;

public class SaltHashing {

    //Random number generator
    SecureRandom random = new SecureRandom();

    byte[] salt;
    byte[] hashedPassword;

    MessageDigest md;

    //Method for hashing, taking username, password and isAdmin as parameters
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
    }

    //Method for inserting user via admin panel
    public boolean insertUser(String username, String password, Boolean isAdmin) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/users", "user_auth", "password");
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO login_info (username, salt, hashed_password, isAdmin) VALUES (?, ?, ?, ?)"); 
            pstmt.setString(1, username);
            pstmt.setString(2, Hex.encodeHexString(salt));
            pstmt.setBytes(3, hashedPassword);
            pstmt.setBoolean(4, isAdmin);
            pstmt.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            return false;
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
