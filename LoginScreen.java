import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

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
            try {
                List<String> allowedIPs = Arrays.asList("192.168.0.239", "192.168.56.1");

                String clientIP = InetAddress.getLocalHost().getHostAddress();

                if (!allowedIPs.contains(clientIP)) {
                    JOptionPane.showMessageDialog(this, "Access denied. Your IP address is not allowed.");
                    return;
                }

                Class.forName("com.mysql.cj.jdbc.Driver");
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/users", "user_auth", "password");
                String sql = "SELECT * FROM usernames_and_passwords WHERE username = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, password);

                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Login was not successful!");
                }
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}
