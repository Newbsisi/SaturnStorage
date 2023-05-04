import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class LoginHasher {
    
    // Hashes the user's login information using SHA-256
    public static String hashLoginInfo(String username, String password) {
        String loginInfo = username + password;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(loginInfo.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String username = "user123";
        String password = "p@ssw0rd";
        String enteredLoginInfo = "user123p@ssw0rd";
        
        // Connect to the MySQL database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "user_auth", "password");
    
            // Retrieve the stored hashed login information for the user
            PreparedStatement stmt = conn.prepareStatement("SELECT password FROM usernames_and_passwords WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            String storedHashedLoginInfo = null;
            if (rs.next()) {
                storedHashedLoginInfo = rs.getString("password");
            }
            rs.close();
            stmt.close();
            conn.close();
    
            // Hash the entered login information and compare it to the stored hash
            String enteredHashedLoginInfo = hashLoginInfo(username, enteredLoginInfo.substring(username.length()));
            System.out.println("Entered hash: " + enteredHashedLoginInfo); // Print out the entered hash
            if (enteredHashedLoginInfo.equals(storedHashedLoginInfo)) {
                System.out.println("Valid login information");
            } else {
                System.out.println("Invalid login information");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
