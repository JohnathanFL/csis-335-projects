package Connectivity;

import java.sql.*;

public class ConnectionClass {
  public static Connection conn;
  private static boolean alreadyInit = false;

  public Connection getConnection() {
    String url = "jdbc:mysql://localhost:3306/leejo_prog6" + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", username = "leejo", password = "OverlyUnderlyPoweredMS";


    try {
      if(!alreadyInit)
        conn = DriverManager.getConnection(url, username, password);
    } catch (SQLException e) {
      System.out.println(e);
    }

    return conn;
  }
}
