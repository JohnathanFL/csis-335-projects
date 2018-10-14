package main;

import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Customer {
  private static int highestID = 0;
  private int custID;
  private String firstName, lastName, address, city, state, zip, cell, email;

  // Each field separated by a pipe since they're pretty rare.
  private static final Pattern parsePattern = Pattern.compile("([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)");

  public Customer(Integer custID, String firstName, String lastName, String address, String city,
                  String state,
                  String zip,
                  String cell, String email) {
    if (custID != null) {
      this.custID = custID;
      if (custID > highestID)
        highestID = custID;
    } else
      this.custID = ++highestID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.cell = cell;
    this.email = email;
  }

  public int getCustID() {
    return custID;
  }

  public void setCustID(int custID) {
    this.custID = custID;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getCell() {
    return cell;
  }

  public void setCell(String cell) {
    this.cell = cell;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  @Override
  public String toString() {
    return "[ID: " + this.custID + "] " + this.firstName + " " + this.lastName;
  }

  public String serialize() {
    return String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s", this.custID, this.firstName, this.lastName, this.address,
        this.city,
        this.state, this.zip, this.cell, this.email);
  }


  public static ArrayList<Customer> parseFile(File inFile) {
    ArrayList<Customer> res = new ArrayList<>();
    try {

      BufferedReader reader = new BufferedReader(new FileReader(inFile));

      String line;
      while ((line = reader.readLine().trim()) != null && !line.isEmpty()) {
        Matcher matcher = parsePattern.matcher(line);
        System.out.println(matcher.matches());
        res.add(new Customer(Integer.parseInt(matcher.group(1)), matcher.group(2), matcher.group(3),
            matcher.group(4), matcher.group(5), matcher.group(6), matcher.group(7),
            matcher.group(8), matcher.group(8)));
      }

    } catch (Exception e) {
      System.out.println("Failed to load " + inFile);
    }

    return res;
  }

  public static void serialize(File outFile, ObservableList<Customer> list) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(outFile, false));
      for (Customer cust : list)
        writer.write(cust.serialize() + '\n');

      writer.close();
    } catch (Exception e) {
    }
  }
}
