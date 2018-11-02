package main;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressInfo {
  private int id;
  private String firstName, lastName, email, phone;

  public AddressInfo(int id, String firstName, String lastName, String email, String phone) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
  }

  public AddressInfo(ResultSet res) throws SQLException {
    this.id = res.getInt(1);
    this.firstName = res.getString(2);
    this.lastName = res.getString(3);
    this.email = res.getString(4);
    this.phone = res.getString(5);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}
