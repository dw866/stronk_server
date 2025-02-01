package utils;

import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {
    public static void printAllTables(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            // Query the database for table names (you can customize schema, catalog, etc.)
            ResultSet resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            // Loop over all tables
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                System.out.println("\u001B[1;31m\n+--------------------------------+");
                System.out.println(tableName);
                System.out.println("+--------------------------------+\u001B[0m");

                // Print table contents
                printTableContent(connection, tableName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to print the content of a table
    private static void printTableContent(Connection connection, String tableName) {
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName);

            // Get metadata to get column names
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();

            // Print the column headers
            System.out.print("| ");
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rsMetaData.getColumnName(i) + " | ");
            }
            System.out.println();
            System.out.println("+" + "-".repeat(20 * columnCount) + "+");

            // Print the content of the table
            while (rs.next()) {
                System.out.print("| ");
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + " | ");
                }
                System.out.println();
            }
            System.out.println("+" + "-".repeat(30 * columnCount) + "+");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
