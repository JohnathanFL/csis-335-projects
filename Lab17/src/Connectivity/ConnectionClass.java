package Connectivity;

import java.sql.*;

public class ConnectionClass {
  public static Connection conn;
  private static boolean alreadyInit = false;

  public Connection getConnection() {
    final String url = "jdbc:mysql://localhost:3306/leejo_addressbook" + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", username = "leejo", password = "OverlyUnderlyPoweredMS";


    try {
      if(!alreadyInit) // Let's do this as a proper singleton, shall we? Wish java had proper static variables
        conn = DriverManager.getConnection(url, username, password);
      alreadyInit = true;
    } catch (SQLException e) {
      System.out.println(e);
    }

    return conn;
  }
}
