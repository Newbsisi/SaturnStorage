import javax.swing.*;
import java.awt.event.*;

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
            // Retrieve the entered username and password
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Check the credentials against a stored database
            if (username.equals("admin") && password.equals("password")) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                // Proceed to the main program
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect username or password.");
                // Allow the user to try again
                usernameField.setText("");
                passwordField.setText("");
            }
        }
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}