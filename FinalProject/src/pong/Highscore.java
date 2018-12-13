/**
 * Johnathan Lee
 * CSIS 335
 * Final Project
 * Due 12/12/18
 *
 * A classic game of pong, with a slight scoring tweak.
 */
package pong;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A record from the Leaderboard view.
 */
public class Highscore {
  int rank;
  String username;
  int wins;

  public Highscore(int rank, ResultSet set) {
    try {
      this.rank = rank;
      this.username = set.getString("displayName");
      this.wins = set.getInt("winCount");
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  public Highscore(int rank, String username, int wins) {
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
