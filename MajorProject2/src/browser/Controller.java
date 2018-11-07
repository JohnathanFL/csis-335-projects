package browser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
  public TabPane root;
  public TabPane browsingPane;
  public Button newTabBtn;
  public Button bookmarkBtn;
  public Tab bookmarkTab, browsingTab;
  public TableView bookmarkTable;
  public TableColumn nameCol;
  public TableColumn timesVisitedCol;
  public TableColumn urlCol;
  public PieChart bkVisitedChart, domVisitedChart, tlDomVisitedChart;

  ObservableList<Bookmark> bookmarks;

  private static Connection conn;
  private PreparedStatement selectAll, selectWhereLessThan, addNew, update;

  public void refreshBookmarks() {
    bookmarks.clear();
    bkVisitedChart.getData().clear();
    domVisitedChart.getData().clear();

    try {
      ResultSet res = selectAll.executeQuery();

      while (res.next())
        bookmarks.add(new Bookmark(res));

//      System.out.println(bkVisitedChart);

      final Pattern domPatt = Pattern.compile("https?:\\/\\/(www\\.)?(.+)(\\.\\w+)(\\/?.*)");
      for(Bookmark bkmark : bookmarks) {
        bkVisitedChart.getData().add(new PieChart.Data(bkmark.name, bkmark.timesVisited));

        Matcher matcher = domPatt.matcher(bkmark.url);
        matcher.matches();

        // Main domain
        {
          String domain = matcher.group(2);
          FilteredList<PieChart.Data> preexData = domVisitedChart.getData().filtered(data -> data.getName().equals(domain));
          if (preexData.size() == 1) {
            preexData.get(0).setPieValue(preexData.get(0).getPieValue() + bkmark.timesVisited);
          } else {
            domVisitedChart.getData().add(new PieChart.Data(domain, bkmark.timesVisited));
          }
        }

        // TLDomain
        {
          String tlDomain = matcher.group(3);
          System.out.println(tlDomain);
          FilteredList<PieChart.Data> preexData = tlDomVisitedChart.getData().filtered(data -> data.getName().equals(tlDomain));
          if (preexData.size() == 1) {
            preexData.get(0).setPieValue(preexData.get(0).getPieValue() + bkmark.timesVisited);
          } else {
            tlDomVisitedChart.getData().add(new PieChart.Data(tlDomain, bkmark.timesVisited));
          }
        }
      }

    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  public void updateAndRefresh() {
    try {
      for(Bookmark bookmark : bookmarks) {
        if (bookmark.isNew) {
          addNew.setString(1, bookmark.name);
          addNew.setString(2, bookmark.url);
          addNew.setInt(3, bookmark.timesVisited);

          addNew.execute();
        } else if (bookmark.changed) {
          update.setInt(1, bookmark.timesVisited);
          update.setString(2, bookmark.name);
          update.execute();
        }
      }
    } catch(SQLException e){
        System.out.println(e);
      }


    refreshBookmarks();
  }

  public void newTab(String url) {
    WebView view = new WebView();
    Tab tab = new Tab("New Tab", view);

    browsingPane.getTabs().add(tab);


    view.getEngine().load(url);
    view.getEngine().titleProperty().addListener((event, oldVal, newVal) -> {
      tab.setText(newVal);
    });

    view.getEngine().titleProperty().addListener((event, oldVval, newVal) -> {
      // Should only ever be one per URL
      try {
        Bookmark bookmark = bookmarks.filtered(bk -> bk.name.equals(newVal)).get(0);
        bookmark.timesVisited++;
        bookmark.changed = true;
        System.out.println(bookmarks);
      } catch (IndexOutOfBoundsException ex) {
        // Didn't have a bookmark for that!
      }
      updateAndRefresh();
    });

    root.getSelectionModel().select(browsingTab);
  }

  public void initialize() {
    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/leejo_bookmarks?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", "leejo", "OverlyUnderlyPoweredMS");
      selectAll = conn.prepareStatement("select name, url, timesVisited from Bookmarks;");
      selectWhereLessThan = conn.prepareStatement("select * from Bookmarks where Bookmarks.timesVisited < ?;");
      addNew = conn.prepareStatement("insert into Bookmarks (name, url, timesVisited) values (?, ?, ?);");
      update = conn.prepareStatement("update Bookmarks set timesVisited = ? where name = ?;");
    } catch (SQLException e) {
      System.out.println(e);
    }

    nameCol.setCellValueFactory(new PropertyValueFactory<Bookmark, String>("name"));
    urlCol.setCellValueFactory(new PropertyValueFactory<Bookmark, String>("url"));
    timesVisitedCol.setCellValueFactory(new PropertyValueFactory<Bookmark, Integer>("timesVisited"));

    bookmarks = FXCollections.observableArrayList();
    bkVisitedChart.setData(FXCollections.observableArrayList());
    domVisitedChart.setData(FXCollections.observableArrayList());
    tlDomVisitedChart.setData(FXCollections.observableArrayList());

    bookmarkTable.setItems(bookmarks);



    refreshBookmarks();



    newTabBtn.setOnAction(e -> newTab("https://google.com"));

    bookmarkBtn.setOnAction(e -> {
      Tab curTab = browsingPane.getSelectionModel().getSelectedItem();
      if(curTab != null) {
        WebView view = (WebView)curTab.getContent();
        String name = view.getEngine().titleProperty().get();

        if(bookmarks.filtered(bk -> bk.name.equals(name)).size() > 0)
          return;


        Bookmark bookmark = new Bookmark(view.getEngine().titleProperty().get(), view.getEngine().locationProperty().get(), 1);
        bookmark.isNew = true;
        bookmarks.add(bookmark);
        updateAndRefresh();
      }
    });

    bookmarkTable.setOnMousePressed(event -> {
      if(event.isPrimaryButtonDown() && event.getClickCount() == 2) {
        newTab(((Bookmark)bookmarkTable.getSelectionModel().getSelectedItem()).url);
      }
    });
  }
}
