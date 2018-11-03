package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    static DatabaseMetaData metaData = null;
    static Connection conn = null;

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/leejo_books?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", username = "leejo", passwd = "OverlyUnderlyPoweredMS";
        try {
            conn = DriverManager.getConnection(url, username, passwd);
            System.out.println("Database connected");
            metaData = conn.getMetaData();

            printMetaData();
        } catch (SQLException ex) {
            System.out.println("Failed to connect to database, " + ex.toString());
        } finally {
            if (conn != null)
                try {
                    conn.close();
                } catch(SQLException i){}
        }
    }

    public static void printMetaData() throws SQLException {
        System.out.println("*** General Metadata ***");
        System.out.println("DB Product: " + metaData.getDatabaseProductName());
        System.out.println("DB Product Version: " + metaData.getDatabaseProductVersion());
        System.out.println("Driver + Driver Version: " + metaData.getDriverName() + " " + metaData.getDriverMajorVersion());
        System.out.println("Logged User: " + metaData.getUserName());
        System.out.println("\n**** Printing Metadata -- TABLES ****");

        ArrayList<String> tableList = new ArrayList<>();
        ResultSet rs = metaData.getTables(null, null, "%", null);

        while(rs.next())
            tableList.add(rs.getString("TABLE_NAME"));

        for(String s : tableList)
            System.out.println(s);

        System.out.println("\n****Printing Metadata -- Columns for Tables ****");
        for(String tblName : tableList) {
            rs = metaData.getColumns(null, null, tblName, null);
            String colName;
            System.out.println("\nTable: " + tblName.toUpperCase());
            while(rs.next()) {
                colName = rs.getString("COLUMN_NAME") + " " + rs.getString("TYPE_NAME") + " " + rs.getString("COLUMN_SIZE");
                System.out.println(colName);
            }
        }
    }
}
