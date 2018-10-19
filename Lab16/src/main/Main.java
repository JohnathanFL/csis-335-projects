package main;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = new Driver();

        Scanner scanner = new Scanner(System.in);

        Connection conn = DriverManager.getConnection("jdbc:mysql://puff.mnstate.edu:3306/leejo_books?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", "leejo", scanner.nextLine().trim());
        Statement stmt = conn.createStatement();

        ResultSet res = stmt.executeQuery("SELECT * FROM authors");
        ResultSetMetaData meta = res.getMetaData();

        int numCols = meta.getColumnCount();
        System.out.println("Authors Table of Books DB:");
        for(int i = 1; i <= numCols; i++)
            System.out.printf("%-8s\t", meta.getColumnName(i));
        System.out.println();

        while(res.next()) {
            for(int i = 1; i <= numCols; i++)
                System.out.printf("%-8s\t", res.getObject(i));

            System.out.println();
        }
        stmt.close();

        System.out.println("Closing the connection");
        conn.close();
    }
}
