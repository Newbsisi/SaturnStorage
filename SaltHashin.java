public class SaltHashin {
    String username = "saltuser";
    String password = "Password3?";
    SecureRandom random = new SecureRandom();
    
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(salt);
    
    byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
    
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/users", "user_auth", "password");
     PreparedStatement pstmt = conn.prepareStatement("INSERT INTO login_info (username, salt, hashed_password) VALUES (?, ?, ?)")) {
    pstmt.setString(1, username);
    pstmt.setBytes(2, salt);
    pstmt.setBytes(3, hashedPassword);
    pstmt.executeUpdate();
}
}
