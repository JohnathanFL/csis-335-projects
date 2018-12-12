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

public class User {
  public int id;
  public String name, email;
  public IconSet icon;
  public BgSet background;

  public User(int id, String name, String email, IconSet icon, BgSet background) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.icon = icon;
    this.background = background;
  }

  public User(ResultSet res) {
    try {
      if (res.isBeforeFirst())
        res.next();
      this.id = res.getInt("id");
      this.name = res.getString("displayName");
      this.email = res.getString("email");
      this.icon = IconSet.from(res.getInt("genSet"));
      this.background = BgSet.from(res.getInt("bgSet"));

    } catch (SQLException e) {
      System.out.println("Failed to construct a user object!");
    }
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", icon=" + icon +
        ", background=" + background +
        '}';
  }
}
