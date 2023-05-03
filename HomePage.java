import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;



public class HomePage extends JFrame {

    //Database connection
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/products";
        String username = "user_auth";
        String password = "password";
        return DriverManager.getConnection(url, username, password);
    }

    //Fetching data from product database
    private ResultSet getDataFromDatabase(String category) throws SQLException {
        String query = "SELECT * FROM " + category;
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
    }

    public HomePage() {
        super("SaturnStorage");


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


        //Calling the different tables from database
        cameraB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ResultSet resultSet = getDataFromDatabase("camera");
                    JTable table = new JTable(ProductTable.buildTableModel(resultSet));
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Cameras");
                    dialog.add(new JScrollPane(table));
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        lightB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ResultSet resultSet = getDataFromDatabase("light");
                    JTable table = new JTable(ProductTable.buildTableModel(resultSet));
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Lights");
                    dialog.add(new JScrollPane(table));
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        cablesB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ResultSet resultSet = getDataFromDatabase("cables");
                    JTable table = new JTable(ProductTable.buildTableModel(resultSet));
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Cables");
                    dialog.add(new JScrollPane(table));
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        propsB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ResultSet resultSet = getDataFromDatabase("props");
                    JTable table = new JTable(ProductTable.buildTableModel(resultSet));
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Props");
                    dialog.add(new JScrollPane(table));
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        camerastandsB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ResultSet resultSet = getDataFromDatabase("stands");
                    JTable table = new JTable(ProductTable.buildTableModel(resultSet));
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Stands");
                    dialog.add(new JScrollPane(table));
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        microphoneB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ResultSet resultSet = getDataFromDatabase("microphone");
                    JTable table = new JTable(ProductTable.buildTableModel(resultSet));
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Microphones");
                    dialog.add(new JScrollPane(table));
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

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

        this.add(mainPanel);

        this.setSize(800, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        HomePage homePage = new HomePage();
    }
}
