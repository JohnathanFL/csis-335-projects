import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Product {
  private int id, inStock;
  private String name;
  private BigDecimal unitPrice;

  public Product(int id, String name, BigDecimal unitPrice, int inStock) {
    this.id = id;
    this.inStock = inStock;
    this.name = name;
    this.unitPrice = unitPrice;
  }

  final static private Pattern prodPatt = Pattern.compile("^(\\d+)\\|(.+)\\|(\\d+\\.\\d+)\\|(\\d+)$");

  static ArrayList<Product> loadFile(String path) throws FileNotFoundException, IOException {
    ArrayList<Product> res = new ArrayList<>();

    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line;
    while((line = reader.readLine()) != null) {
      Matcher extracter = prodPatt.matcher(line);
      res.add(new Product(Integer.parseInt(extracter.group(0)),
              extracter.group(1),
              new BigDecimal(extracter.group(2)),
              Integer.parseInt(extracter.group(3))));
    }
  }
}
