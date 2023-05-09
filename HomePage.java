import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


public class HomePage extends JFrame {
    private String username;

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

    // table methods
    private JTable getReadOnlyTable(String category) throws SQLException {
        ResultSet resultSet = getDataFromDatabase(category);
        JTable table = new JTable(ProductTable.buildTableModel(resultSet)) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // set single row selection mode
        table.setRowHeight(20); // set row height
        table.setGridColor(Color.BLACK); // set grid color
        table.setShowGrid(true); // show grid lines

        // create popup menu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem loanMenuItem = new JMenuItem("Loan");
        loanMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(table, "Item loaned!");
            }
        });
        popupMenu.add(loanMenuItem);

        // add mouse listener to table
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        return table;
    }


    public HomePage() {
        super("SaturnStorage");

        JTextField b = new JTextField("Search");
        b.setPreferredSize(new Dimension(250, 30));
        JButton searchButton = new JButton("Search");
        JButton returnButton = new JButton("Return");


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
                    JTable table = getReadOnlyTable("camera");
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

        // repeat for other categories

        lightB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JTable table = getReadOnlyTable("light");
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

        // repeat for other categories

        cablesB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JTable table = getReadOnlyTable("cables");
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

        // repeat for other categories

        propsB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JTable table = getReadOnlyTable("props");
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

        // repeat for other categories

        camerastandsB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JTable table = getReadOnlyTable("stands");
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

        // repeat for other categories

        microphoneB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JTable table = getReadOnlyTable("microphone");
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

        JButton logout = new JButton("Logout");
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutfunction();
            }
        });

        // Layout constraints
        cameraB.setPreferredSize(new Dimension(200, 30));
        lightB.setPreferredSize(new Dimension(200, 30));
        cablesB.setPreferredSize(new Dimension(200, 30));
        propsB.setPreferredSize(new Dimension(200, 30));
        camerastandsB.setPreferredSize(new Dimension(200, 30));
        microphoneB.setPreferredSize(new Dimension(200, 30));



        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.add(logout, BorderLayout.EAST);


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(userPanel, BorderLayout.SOUTH);

        this.add(mainPanel);

        this.setSize(800, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    // Logout logic
    public void logoutfunction() {
        this.setVisible(false);

        LoginScreen loginScreen = new LoginScreen();
        loginScreen.setVisible(true);

        dispose();
    }

    public static void main(String[] args) {
        HomePage homePage = new HomePage();
    }
}