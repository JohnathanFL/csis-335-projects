package browser;

import javafx.scene.chart.PieChart;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Bookmark {
  boolean changed = false;
  boolean isNew = false;

  String name, url;
  int timesVisited;

  public PieChart.Data data;

  public Bookmark(String name, String url, int timesVisited) {
    this.name = name;
    this.url = url;
    this.timesVisited = timesVisited;

    this.data = new PieChart.Data(name, timesVisited);
  }

  public Bookmark(ResultSet res) throws SQLException {
    this.name = res.getString(1);
    this.url = res.getString(2);
    this.timesVisited = res.getInt(3);

    this.data = new PieChart.Data(this.name, this.timesVisited);
  }

  public String getName() {
    return name;
  }

  public String getUrl() {
    return url;
  }

  public int getTimesVisited() {
    return timesVisited;
  }

  public void setTimesVisited(int timesVisited) {
    this.timesVisited = timesVisited;
    this.changed = true;
  }

  @Override
  public String toString() {
    return "Bookmark{" +
        "changed=" + changed +
        ", isNew=" + isNew +
        ", name='" + name + '\'' +
        ", url='" + url + '\'' +
        ", timesVisited=" + timesVisited +
        ", data=" + data +
        '}';
  }
}
