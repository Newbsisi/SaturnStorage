import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminPanel extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox adminCheckbox;
    private JTextArea userListTextArea;

    public AdminPanel() {
        // Set up the GUI components for adding a new user
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        JButton submitButton = new JButton("Add User");
        submitButton.addActionListener(this);
    
        adminCheckbox = new JCheckBox("Admin");
        adminCheckbox.setHorizontalAlignment(SwingConstants.LEFT);
    
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        userListTextArea = new JTextArea(20, 40);
        JScrollPane scrollPane = new JScrollPane(userListTextArea);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 400, 0, 10);
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 0, 100);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 400, 10, 10);
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 100);
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 400, 10, 10);
        panel.add(adminCheckbox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 25);
        panel.add(submitButton, gbc);

        JButton listUsersButton = new JButton("List Users");
        listUsersButton.addActionListener(this);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 20, 10);
        panel.add(listUsersButton, gbc);

        add(panel);
        setTitle("Admin Panel");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("List Users")) {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost/users", "user_auth", "password");
        
                // Execute the query to retrieve all users
                stmt = conn.prepareStatement("SELECT username, isAdmin FROM login_info");
                ResultSet rs = stmt.executeQuery();
        
                // Clear the JTextArea
                userListTextArea.setText("");
        
                // Append each user to the JTextArea
                while (rs.next()) {
                    String username = rs.getString("username");
                    boolean isAdmin = rs.getBoolean("isAdmin");
                    int userType = isAdmin ? 1 : 0;
                    userListTextArea.append(username + " (" + userType + ")\n");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } finally {
                // Close the resources
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        

        if (e.getActionCommand().equals("Add User")) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            boolean isAdmin = adminCheckbox.isSelected();
    
            // Validate the username and password
            if (!isValidUsername(username) || !isValidPassword(password)) {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
                return;
            }
    
            SaltHashing saltHashing = new SaltHashing(username, password, isAdmin);
            boolean isUserAdded = saltHashing.insertUser(username, password, isAdmin);
    
            if (isUserAdded) {
                JOptionPane.showMessageDialog(this, "User added successfully!");
                usernameField.setText("");
                passwordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add user, username already exists.");
            }
        }
    }
    

    // Validation methods for username and password
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

        // All validation checks have
        return true;
    }
}

