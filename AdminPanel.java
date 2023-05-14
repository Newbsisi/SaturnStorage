import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminPanel extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox adminCheckbox;

    public AdminPanel() {
        // Set up the GUI components for adding a new user
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        JButton submitButton = new JButton("Add User");
        submitButton.addActionListener(this);
    
        adminCheckbox = new JCheckBox("Admin");
    
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
    
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 0, 10);
        panel.add(usernameLabel, gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 0, 10);
        panel.add(usernameField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(passwordLabel, gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 10);
        panel.add(passwordField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(adminCheckbox, gbc);
    
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 10, 10);
        panel.add(submitButton, gbc);
    
        add(panel);
        setTitle("Admin Panel - Add User");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

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
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add user, duplicate username.");
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

