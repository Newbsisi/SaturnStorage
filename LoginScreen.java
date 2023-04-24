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
            try {
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
                    request.getSession().setAttribute("loggedIn", true);
                    response.sendRedirect("Homepage");
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

    public static void main(String[] args) {
        new LoginScreen();
    }
}

public class Homepage {
    public static void main(String[] args) {
        if (request.getSession().getAttribute("loggedIn") == null) {
            response.sendRedirect("LoginScreen");
            return;
        }
        JFrame a = new JFrame("SaturnStorage");
        JTextField b = new JTextField("Search");
        b.setPreferredSize(new Dimension(250,30));
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IsValidSearchString.InputValidator.validateInput(b);
            }
        });

        b.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent searching) {
                if (searching.getKeyCode() == KeyEvent.VK_ENTER) {
                    IsValidSearchString.InputValidator.validateInput(b);
                }
            }
        });



        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(b);
        searchPanel.add(searchButton);

        JButton cameraB = new JButton("Camera");
        JButton lightB = new JButton("Light");
        JButton cablesB = new JButton("Cables");
        JButton propsB = new JButton("Props");
        JButton camerastandsB = new JButton("Stands");
        JButton microphoneB = new JButton("Microphone");

        JPanel buttonPanel = new JPanel(new GridLayout(6, 1));
        buttonPanel.add(cameraB);
        buttonPanel.add(lightB);
        buttonPanel.add(cablesB);
        buttonPanel.add(propsB);
        buttonPanel.add(camerastandsB);
        buttonPanel.add(microphoneB);

        cameraB.setPreferredSize(new Dimension(200, 30));
        lightB.setPreferredSize(new Dimension(200, 30));
        cablesB.setPreferredSize(new Dimension(200, 30));
        propsB.setPreferredSize(new Dimension(200, 30));
        camerastandsB.setPreferredSize(new Dimension(200, 30));
        microphoneB.setPreferredSize(new Dimension(200, 30));

        JLabel usernameLabel = new JLabel("Username");
        JButton returnButton = new JButton("Return");

        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.add(usernameLabel, BorderLayout.WEST);
        userPanel.add(returnButton, BorderLayout.EAST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(userPanel, BorderLayout.SOUTH);

        a.add(mainPanel);

        a.setSize(800, 500);
        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.setVisible(true);
    }
}