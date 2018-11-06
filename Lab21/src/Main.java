import org.json.simple.parser.JSONParser;
import org.json.simple.*;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {


  public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
    writeJSON();
    readJSON();
  }


  public static void readJSON() throws FileNotFoundException, IOException, ParseException {
    // parsing file "JSONExample.json"
    Object obj = new JSONParser().parse(new FileReader("JSONExample.json"));

    // typecasting obj to JSONObject
    JSONObject rootRoot = (JSONObject) obj;

    JSONArray root = (JSONArray)rootRoot.get("root");

    for(Object joObj : root) {
      JSONObject jo = (JSONObject)joObj;
      // getting firstName and lastName
      String firstName = (String) jo.get("firstName");
      String lastName = (String) jo.get("lastName");

      System.out.println(firstName);
      System.out.println(lastName);

      // getting age
      long age = (long) jo.get("age");
      System.out.println(age);

      // getting address
      Map address = ((Map) jo.get("address"));

      // iterating address Map
      Iterator<Map.Entry> itr1 = address.entrySet().iterator();
      while (itr1.hasNext()) {
        Map.Entry pair = itr1.next();
        System.out.println(pair.getKey() + " : " + pair.getValue());
      }

      // getting phoneNumbers
      JSONArray ja = (JSONArray) jo.get("phoneNumbers");

      // iterating phoneNumbers
      Iterator itr2 = ja.iterator();

      while (itr2.hasNext()) {
        itr1 = ((Map) itr2.next()).entrySet().iterator();
        while (itr1.hasNext()) {
          Map.Entry pair = itr1.next();
          System.out.println(pair.getKey() + " : " + pair.getValue());
        }
      }
      System.out.println("\n\n########################\n\n");
    }
  }

  public static void writeJSON() throws FileNotFoundException {
    JSONArray root = new JSONArray();

    for(int i = 0; i < 6; i++) {
      JSONObject jo = new JSONObject();

      // putting data to JSONObject
      jo.put("firstName", "John" + i);
      jo.put("lastName", "Smith");
      jo.put("age", 25);

      // for address data, first create LinkedHashMap
      Map m = new LinkedHashMap(4);
      m.put("streetAddress", "21 2nd Street");
      m.put("city", "New York");
      m.put("state", "NY");
      m.put("postalCode", 10021);

      // putting address to JSONObject
      jo.put("address", m);

      // for phone numbers, first create JSONArray
      JSONArray ja = new JSONArray();

      m = new LinkedHashMap(2);
      m.put("type", "home");
      m.put("number", "212 555-1234");

      // adding map to list
      ja.add(m);

      m = new LinkedHashMap(2);
      m.put("type", "fax");
      m.put("number", "212 555-1234");

      // adding map to list
      ja.add(m);

      // putting phoneNumbers to JSONObject
      jo.put("phoneNumbers", ja);

      root.add(jo);
    }

    // writing JSON to file:"JSONExample.json" in cwd
    PrintWriter pw = new PrintWriter("JSONExample.json");
    pw.write("{ \"root\": " + root.toJSONString() + "}");

    pw.flush();
    pw.close();
  }
}
