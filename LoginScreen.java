import java.awt.*;
import java.sql.*;
import java.util.Arrays;
import javax.swing.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import java.awt.event.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;




public class LoginScreen extends JFrame implements ActionListener {
    private final JTextField usernameField; // Declare as final
    JPasswordField passwordField;
    JButton submitButton;

    static String loggedInUsername;


    public LoginScreen() {
        // Set up the GUI components
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String username = usernameField.getText();
                    validateLogin(username);
                }
            }
        });
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);

        JPanel panel = new JPanel();
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(submitButton);

        add(panel);
        setTitle("Login Screen");
        setSize(300, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String username = usernameField.getText();
            validateLogin(username);
    }
}

    public void validateLogin(String username) {
        String password = new String(passwordField.getPassword());
    
        // Validate the username and password
        if (!isValidUsername(username) || !isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, "Invalid username or password!");
            return;
        }
    
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/users", "user_auth", "password");
            String sql = "SELECT salt, hashed_password, isAdmin FROM login_info WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
        
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                byte[] salt = Hex.decodeHex(result.getString("salt"));
                byte[] storedHashedPassword = result.getBytes("hashed_password");
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(salt);
                md.update(password.getBytes(StandardCharsets.UTF_8));
                byte[] computedHashedPassword = md.digest();
                if (Arrays.equals(computedHashedPassword, storedHashedPassword)) {
                    boolean isAdmin = result.getBoolean("isAdmin");
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    loggedInUsername = username;
                    dispose();
        
                    HomePage homePage = new HomePage();
        
                    // If the logged-in user is an admin, show the admin button
                    if (isAdmin) {
                        JLabel usernameLabel = new JLabel("User: " + username);
                        JButton returnButton = new JButton("Deliver back");

                        returnButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                ReturnMethod returnMethod = new ReturnMethod(username);
                                returnMethod.setVisible(true);
                            }
                        });
                        returnButton.setPreferredSize(new Dimension(150, 30));
                        JPanel userPanel = new JPanel(new BorderLayout());
                        userPanel.add(usernameLabel, BorderLayout.WEST);
                        userPanel.add(returnButton, BorderLayout.EAST);
                        homePage.getContentPane().add(userPanel, BorderLayout.SOUTH);
                        homePage.getContentPane().add(userPanel, BorderLayout.NORTH);
                        JButton adminButton = new JButton("Admin Panel");
                        adminButton.setPreferredSize(new Dimension(150, 30));
                        JPanel adminPanel = new JPanel(new BorderLayout());
                        adminPanel.add(adminButton, BorderLayout.EAST);
                        homePage.getContentPane().add(adminPanel, BorderLayout.SOUTH);
        
                        // Add an ActionListener to the Admin Button to open the Admin Panel
                        adminButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (isAdmin) {
                                    AdminPanel adminPanel = new AdminPanel();
                                    adminPanel.setVisible(true);
                                }
                            }
                        });
                    } else {
                    JLabel usernameLabel = new JLabel("User: " + username);
                    JButton returnButton = new JButton("Deliver back");
                    returnButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ReturnMethod returnMethod = new ReturnMethod(username);
                            returnMethod.setVisible(true);
                        }
                    });

                        returnButton.setPreferredSize(new Dimension(150, 30));
                    JPanel userPanel = new JPanel(new BorderLayout());
                    userPanel.add(usernameLabel, BorderLayout.WEST);
                    userPanel.add(returnButton, BorderLayout.EAST);
                    homePage.getContentPane().add(userPanel, BorderLayout.SOUTH);
                    homePage.getContentPane().add(userPanel, BorderLayout.NORTH);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        } catch (SQLException | ClassNotFoundException | NoSuchAlgorithmException | DecoderException e) {
            ((Throwable) e).printStackTrace();
        }
    }


    private boolean isValidUsername(String username) {
        // Check if the username is null or empty
        if (username == null || username.isEmpty()) {
            return false;
        }

        // Check if the username contains only alphanumeric characters
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            return false;
        }

        // Check if the username is at least 6 characters long
        if (username.length() < 6) {
            return false;
        }

        // All validation checks have passed, so the username is valid
        return true;
    }

    private boolean isValidPassword(String password) {
        // Check if the password is null or empty
        if (password == null || password.isEmpty()) {
            return false;
        }

        // Check if the password is at least 8 characters long
        if (password.length() < 8) {
            return false;
        }

        // If the password contains at least one uppercase letter, one lowercase letter, and one digit
        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$")) {
            return false;

        }

        // All validation checks have passed, so the password is valid
        return true;
    }


    public static void main (String[]args){
        new LoginScreen();
    }
    
}