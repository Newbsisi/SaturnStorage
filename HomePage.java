import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
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

    // table methods
    private JTable getReadOnlyTable(String category) throws SQLException {
        ResultSet resultSet = getDataFromDatabase(category);
        JTable table = new JTable(ProductTable.buildTableModel(resultSet)) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(20);
        table.setGridColor(Color.BLACK);
        table.setShowGrid(true);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem loanMenuItem = new JMenuItem("Loan");
        loanMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Loan menu item clicked"); // Debug statement
                String username = LoginScreen.loggedInUsername;
                if (username != null){
                    loanItem(table, category, username);
                }
            }
        });
        popupMenu.add(loanMenuItem);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                        System.out.println("Right-clicked on row: " + row); // Debug statement
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        return table;
    }

    private void loanItem(JTable table, String category, String username) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow >= 0) {
            try {
                String itemId = table.getValueAt(selectedRow, 0).toString();
                System.out.println("Selected item ID: " + itemId); // Debug statement

                if (itemId == null || itemId.isEmpty()) {
                    System.out.println("Invalid item ID"); // Debug statement
                    return;
                }

                if (username == null || username.isEmpty()) {
                    System.out.println("Invalid username"); // Debug statement
                    return;
                }

                Connection connection = getConnection();
                System.out.println("Database connection established"); // Debug statement

                String sourceTable = category;
                String destinationTable = "items_on_loan";

                String selectQuery = "SELECT * FROM " + sourceTable + " WHERE id = ?";
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                selectStatement.setString(1, itemId);
                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    // Get the data from the source table
                    String name = resultSet.getString("Name");
                    String brand = resultSet.getString("Brand");
                    String type = resultSet.getString("Type");
                    String itemCategory = resultSet.getString("Category");

                    // Insert the data into the destination table
                    String insertQuery = "INSERT INTO " + destinationTable + " (id, Username, Name, Brand, Type, Category) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setString(1, itemId);
                    insertStatement.setString(2, username);
                    insertStatement.setString(3, name);
                    insertStatement.setString(4, brand);
                    insertStatement.setString(5, type);
                    insertStatement.setString(6, itemCategory);
                    int rowsAffected = insertStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Remove the loaned item from the source table
                        String deleteQuery = "DELETE FROM " + sourceTable + " WHERE id = ?";
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                        deleteStatement.setString(1, itemId);
                        deleteStatement.executeUpdate();

                        // Remove the row from the table
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.removeRow(selectedRow);

                        JOptionPane.showMessageDialog(table, "Item loaned!");
                    } else {
                        System.out.println("Failed to insert into destination table"); // Debug statement
                    }
                } else {
                    System.out.println("No matching item found in source table"); // Debug statement
                }

                // Close the result set, statements, and connection
                resultSet.close();
                selectStatement.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println("SQL Exception occurred: " + ex.getMessage()); // Debug statement
            }
        } else {
            System.out.println("No row selected"); // Debug statement
        }
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
                    JTable table = getReadOnlyTable("Camera");
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
                    JTable table = getReadOnlyTable("Light");
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
                    JTable table = getReadOnlyTable("Cables");
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
                    JTable table = getReadOnlyTable("Props");
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
                    JTable table = getReadOnlyTable("Stands");
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
                    JTable table = getReadOnlyTable("Microphone");
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