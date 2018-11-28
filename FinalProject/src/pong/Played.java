package pong;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Played {
  int id, p1Id, p2Id, p1Score, p2Score;
  Date datePlayed;
  int winner;

  public Played(int id, int p1Id, int p2Id, int p1Score, int p2Score, Date datePlayed, int winner) {
    this.id = id;
    this.p1Id = p1Id;
    this.p2Id = p2Id;
    this.p1Score = p1Score;
    this.p2Score = p2Score;
    this.datePlayed = datePlayed;
    this.winner = winner;
  }

  public Played(ResultSet res) {
    try {
      if (res.isBeforeFirst())
        res.next();
      this.id = res.getInt("id");
      this.p1Id = res.getInt("p1Id");
      this.p2Id = res.getInt("p2Id");
      this.p1Score = res.getInt("p1Score");
      this.p2Score = res.getInt("p2Score");
      this.datePlayed = res.getDate("datePlayed");
      this.winner = res.getInt("winner");
    } catch (SQLException e) {
      System.out.println("Failed to create a Played object: " + e);
    }
  }
}
