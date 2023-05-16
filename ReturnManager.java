import java.sql.*;

public class ReturnManager {

    public void returnItemsToInventory(String category) {
        try {
            Connection connection = getConnection();
            String sourceTable = "items_on_loan";
            String destinationTable = category;

            // Retrieve the loaned items from the "items_on_loan" table
            String selectQuery = "SELECT * FROM " + sourceTable;
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = selectStatement.executeQuery();

            // Iterate over the loaned items and insert them back into the original table
            while (resultSet.next()) {
                String itemId = resultSet.getString("id");
                String username = resultSet.getString("Username");
                String name = resultSet.getString("Name");
                String brand = resultSet.getString("Brand");
                String type = resultSet.getString("Type");

                // Insert the item back into the original table
                String insertQuery = "INSERT INTO " + destinationTable + " (id, Name, Brand, Type) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, itemId);
                insertStatement.setString(2, name);
                insertStatement.setString(3, brand);
                insertStatement.setString(4, type);
                insertStatement.executeUpdate();
            }

            // Delete the loaned items from the "items_on_loan" table
            String deleteQuery = "DELETE FROM " + sourceTable;
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.executeUpdate();

            System.out.println("Items returned to inventory successfully");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("SQL Exception occurred: " + ex.getMessage()); // Debug statement
        }
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/products";
        String username = "user_auth";
        String password = "password";
        return DriverManager.getConnection(url, username, password);
    }
}

