package pong;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Leaderboard {
  int rank;
  String username;
  int wins;

  public Leaderboard(int rank, ResultSet set) {
    try {
      this.rank = rank;
      this.username = set.getString("displayName");
      this.wins = set.getInt("winCount");
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  public Leaderboard(int rank, String username, int wins) {
    this.rank = rank;
    this.username = username;
    this.wins = wins;
  }

  public int getRank() {
    return rank;
  }

  public String getUsername() {
    return username;
  }

  public int getWins() {
    return wins;
  }
}
