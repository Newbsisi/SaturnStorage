import java.sql.*;

    public class DataBaseTest {
        public static void main(String[] args) {
            try {
                // Load the MySQL JDBC driver
                Class.forName("com.mysql.jdbc.Driver");

                // Establish a connection to the database
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/mydatabase", "myuser", "mypassword");

                // Execute a query on the database
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("jdbc:mysql://localhost:3306/?user=root");

                // Process the query results
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    System.out.println(id + " - " + name);
                }

                // Test if the connection is valid
                if (connection.isValid(5)) { // 5 seconds timeout
                    System.out.println("Connection is valid");
                } else {
                    System.out.println("Connection is invalid");
                }

                // Close the database resources
                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
