package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    //public static String JDBC_URL = "jdbc:sqlite:C:\\Users\\Aditya Pratap\\Desktop\\ElectronicBookManagementSystem\\src\\ElectronicBookDatabase.db";
    private static final String JDBC_URL = "jdbc:sqlite::resource:ElectronicBookDatabase.db";

    /***
     * Establishes connection to database
     * @return
     */
    public static Connection getDbConnection() {
        Connection conn = null;
        try {
            // db parameters
            conn = DriverManager.getConnection(JDBC_URL);
            //Database Connection

        } catch (SQLException e) {
            e.printStackTrace();
        }



        return conn;
    }
}



