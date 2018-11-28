/**
 * Author: Johnathan Lee
 * CSIS 335
 *
 * Program 8 Due 11/26/18
 * Displays a simple 5 day forecast from the OpenWeathermap.org API
 */

package weather;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;Odens
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

  /**
   * Parses data from a web API into a JSONObject
   * @param url The URL to grab from
   * @return A JSONObject if parse was successful, null otherwise.
   */
  private JSONObject getData(String url) {
    try {
      HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();

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

  /**
   * Refreshes all displayed data, pulling from the API using the textfields for parameters
   */
  public void refresh() {

    JSONObject data = getData("http://api.openweathermap.org/data/2.5/forecast?q=" + cityField.getText() + "," + countryField + "&appid=4d45ebfb65ad3710630352dc678c9091");
    if(data == null) {
      System.out.println("Unable to get weather data!");
      System.exit(-1);
    }
    JSONArray weatherAr = (JSONArray) data.get("list");
    int curDay = 0;

    // For each day
    for (Node child : weatherBox.getChildren()) {
      GridPane grid = (GridPane) child;
      JSONObject weather = (JSONObject) weatherAr.get(curDay * 8);
      JSONObject main = (JSONObject) weather.get("main");
      JSONObject wind = (JSONObject) weather.get("wind");
      JSONObject weath = (JSONObject) ((JSONArray) weather.get("weather")).get(0);

      // We must do the (Number).xxValue() stuff because if they happen to send it without trailing zeroes, it seems
      // that the parser immediately assumes it's a long, and even if it has a .00, we can't get it automatically as a long
      //
      // In short, data may be long or double. We make sure it's always what we need.
      String[] fieldsTexts = {
          /*Date*/ new Date(((Long)weather.get("dt")) * 1000).toString().substring(0, 11),
          /*Desc*/ (String) weath.get("description"),
          /*Temp*/ String.format("Temp: %.00f°C", (((Number) main.get("temp")).doubleValue() - 273.15)),
          /*Min Temp*/ String.format("Minimum: %.00f°C", (((Number) main.get("temp_min")).doubleValue() - 273.15)),
          /*Max Temp*/ String.format("Maximum: %.00f°C", (((Number)main.get("temp_max")).doubleValue() - 273.15)),
          /*Humidity*/ String.format("Humidity: %d%%", ((Number)main.get("humidity")).longValue()),
          /*Windspeed*/ String.format("Windspeed: %.00f m/s", ((Number)wind.get("speed")).doubleValue())
      };

      String url = "http://openweathermap.org/img/w/" + weath.get("icon").toString().replace('n', 'd') + ".png";
      System.out.println(url);
      Image iconImg = new Image(url);

      ObservableList<Node> fields = grid.getChildren();
      ImageView icon = (ImageView) fields.get(0);
      icon.setImage(iconImg);

      // For each type of data
      for (int i = 0; i < fieldsTexts.length; i++)
        // i + 1 to compensate for that fact that the first field is the icon.
        ((Label) fields.get(i + 1)).setText(fieldsTexts[i]);

      curDay++;
    }
  }

  /**
   * Checks whether both text fields have something in them and updates the provided property accordingly
   * @param isDisabled Property to update. True if invalid textfields, false if valid
   */
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
