import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class ReturnMethod extends JFrame {
    private JTable table;
    private JButton returnButton;

    public ReturnMethod(String username) {
        super("Return Items");

        // Set up the GUI components
        table = new JTable();
        returnButton = new JButton("Return");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnSelectedItem();
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(returnButton, BorderLayout.SOUTH);

        add(panel);
        setTitle("Return Items");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Fetch and display the borrowed items for the user
        displayBorrowedItems(username);
    }


    private void displayBorrowedItems(String username) {
        try {
            // Fetch the borrowed items from the database for the given username
            ResultSet resultSet = getBorrowedItems(username);
            // Create a table model with the result set data
            DefaultTableModel tableModel = ProductTable.buildTableModel(resultSet);
            // Set the table model to the JTable
            table.setModel(tableModel);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving borrowed items: " + e.getMessage());
        }
    }

    private ResultSet getBorrowedItems(String username) throws SQLException {
        Connection connection = getConnection(); // Establish the database connection

        String query = "SELECT * FROM items_on_loan WHERE Username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);

        return statement.executeQuery();
    }

    private void returnSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String itemId = table.getValueAt(selectedRow, 0).toString();
            // Perform the necessary database operations to return the item based on the itemId
            try {
                Connection connection = getConnection();
                PreparedStatement returnStatement = connection.prepareStatement("UPDATE items_on_loan SET Returned = ? WHERE id = ?");
                returnStatement.setBoolean(1, true);
                returnStatement.setString(2, itemId);
                int rowsAffected = returnStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Return successful, remove the row from the table
                    DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                    tableModel.removeRow(selectedRow);

                    // Move the item back to the correct category table
                    moveItemToCategory(itemId);

                    JOptionPane.showMessageDialog(this, "Item returned!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to return the item.");
                }

                returnStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error returning the item: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to return.");
        }
    }

    private void moveItemToCategory(String itemId) {
        try {
            Connection connection = getConnection();

            // Retrieve the original category and item details
            String getCategoryQuery = "SELECT Category, Name, Brand, Type FROM items_on_loan WHERE id = ?";
            PreparedStatement getCategoryStatement = connection.prepareStatement(getCategoryQuery);
            getCategoryStatement.setString(1, itemId);
            ResultSet categoryResult = getCategoryStatement.executeQuery();

            if (categoryResult.next()) {
                String originalCategory = categoryResult.getString("Category");
                String name = categoryResult.getString("Name");
                String brand = categoryResult.getString("Brand");
                String type = categoryResult.getString("Type");

                // Update the item's category in the items_on_loan table
                String updateQuery = "UPDATE items_on_loan SET Category = ?, Returned = ? WHERE id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, originalCategory);
                updateStatement.setBoolean(2, true);
                updateStatement.setString(3, itemId);
                updateStatement.executeUpdate();

                // Insert the item into the original category table
                String insertQuery = "INSERT INTO " + originalCategory + " (id, Name, Brand, Type, Category) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, itemId);
                insertStatement.setString(2, name);
                insertStatement.setString(3, brand);
                insertStatement.setString(4, type);
                insertStatement.setString(5, originalCategory);
                insertStatement.executeUpdate();

                // Delete the item from the items_on_loan table
                String deleteQuery = "DELETE FROM items_on_loan WHERE id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setString(1, itemId);
                deleteStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error moving item to category: " + e.getMessage());
        }
    }



    private Connection getConnection() throws SQLException {
        // Establish a connection to the database
        String url = "jdbc:mysql://127.0.0.1:3306/products";
        String username = "user_auth";
        String password = "password";
        return DriverManager.getConnection(url, username, password);
    }

}
