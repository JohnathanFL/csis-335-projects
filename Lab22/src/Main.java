import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
  static final String weatherAPIKey = "b52ddf9b09e3335ad3e72b6d115d9545", ipInfoAPIKey = "552178a708c7ed100084d534730d10e6d68f4f40d91de09d9cc455ffae592b2b";

  private static void getLocInfoBasedOnCurrentIP() {
    try {
      String data = getData("http://api.ipinfodb.com/v3/ip-city/?key=" + ipInfoAPIKey +"&format=json");
      System.out.println(data);

      JSONObject root = (JSONObject)(new JSONParser()).parse(data);

      System.out.println(root.get("countryName"));
      System.out.println(root.get("countryCode"));
      System.out.println(root.get("cityName"));
      System.out.println(root.get("latitude"));
      System.out.println(root.get("longitude"));
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  private static void getWeatherData() {
    try {
      String data = getData("http://api.openweathermap.org/data/2.5/weather?q=Fargo,us&appid=" + weatherAPIKey);
      System.out.println("Weather Data: " + data);

      JSONObject root = (JSONObject) (new JSONParser()).parse(data);
      System.out.println(root.get("name"));
      System.out.println(root.get("id"));
      JSONObject wind = (JSONObject) root.get("wind");
      System.out.println("Wind: " + wind.get("speed"));

      JSONArray weather = (JSONArray) root.get("weather");
      if (weather.size() > 0) {
        JSONObject ob = (JSONObject) weather.get(0);
        System.out.println("Weather Description: " + ob.get("description"));

      }

    } catch (Exception e) {
      System.out.println(e);
    }

  }

  private static String getData(String url) {
    try {
      URL obj = new URL(url);

      HttpURLConnection con = (HttpURLConnection)obj.openConnection();
      System.out.println(con.getResponseCode());
      if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
        BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream())
        );

        return in.lines().collect(Collectors.joining());
      } else
        return "";
    } catch (Exception e) {
      System.out.println(e);
      return "";
    }
  }

  public static void main(String[] args) {
    getWeatherData();
    getLocInfoBasedOnCurrentIP();
  }
}
