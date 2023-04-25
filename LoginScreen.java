import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;






public class LoginScreen extends JFrame implements ActionListener {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton submitButton;

    public LoginScreen() {
        // Set up the GUI components
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Validate the username and password
            if (!isValidUsername(username) || !isValidPassword(password)) {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
                return;
            }

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/?user=root", "root", "jtv78kgf");
                String sql = "SELECT * FROM usernames_and_passwords WHERE username = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, password);

                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    HomePage homePage = new HomePage();
                    homePage.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Login was not successful!");
                }
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
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

    public static void main(String[] args) {
        new LoginScreen();
    }
}