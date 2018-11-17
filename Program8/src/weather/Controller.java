package weather;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.stream.Collectors;

public class Controller {

  public GridPane rootPane;
  public TextField cityField;
  public TextField countryField;
  public Button updateBtn;
  public VBox weatherBox;

  private JSONObject getData(String url) {
    try {
      HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();
      //System.out.println(con.getResponseCode());
      if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        return (JSONObject) new JSONParser().parse(in.lines().collect(Collectors.joining()));
      } else {
        throw new RuntimeException("Unable to parse JSON. Error " + con.getResponseCode());

      }
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public void refresh() {

    JSONObject data = getData("http://api.openweathermap.org/data/2.5/forecast?q=" + cityField.getText() + "," + countryField + "&appid=4d45ebfb65ad3710630352dc678c9091");
    JSONArray weatherAr = (JSONArray) data.get("list");
    int curDay = 0;
    for (Node child : weatherBox.getChildren()) {
      GridPane grid = (GridPane) child;
      JSONObject weather = (JSONObject) weatherAr.get(curDay * 8);
      JSONObject main = (JSONObject) weather.get("main");
      JSONObject wind = (JSONObject) weather.get("wind");
      JSONObject weath = (JSONObject) ((JSONArray) weather.get("weather")).get(0);

      String[] fieldsTexts = {
          /*Date*/ new Date(((Long)weather.get("dt")) * 1000).toString().substring(0, 11),
          /*Desc*/ (String) weath.get("description"),
          /*Temp*/ String.format("Temp: %.00f°C", ((Double) main.get("temp") - 273.15)),
          /*Min Temp*/ String.format("Minimum: %.00f°C", ((Double) main.get("temp_min") - 273.15)),
          /*Max Temp*/ String.format("Maximum: %.00f°C", ((Double) main.get("temp_max") - 273.15)),
          /*Humidity*/ String.format("Humidity: %d%%", ((Long) main.get("humidity"))),
          /*Windspeed*/ String.format("Windspeed: %.00f m/s", ((Double) wind.get("speed")))};

      Image iconImg = new Image(getClass().getResource(((String) weath.get("icon")) + ".png").toExternalForm());

      ObservableList<Node> fields = grid.getChildren();
      ImageView icon = (ImageView) fields.get(0);
      icon.setImage(iconImg);

      for (int i = 0; i < fieldsTexts.length; i++)
        ((Label) fields.get(i + 1)).setText(fieldsTexts[i]);

      curDay++;
    }
  }

  public void updateDisable(BooleanProperty isDisabled) {
    if(cityField.getText().isEmpty() || countryField.getText().isEmpty())
      isDisabled.set(true);
    else
      isDisabled.set(false);
  }

  public void initialize() {
    rootPane.getStylesheets().add(getClass().getResource("Weather.css").toExternalForm());

    refresh();
    this.updateBtn.setOnAction(e -> refresh());
    cityField.textProperty().addListener((e, oldVal, newVal) -> updateDisable(this.updateBtn.disableProperty()));
    countryField.textProperty().addListener((e, oldVal, newVal) -> updateDisable(this.updateBtn.disableProperty()));

  }
}
