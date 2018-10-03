package sample;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Controller {
  @FXML
  WebView webGoogle, webDuck;


  public void initialize() {
    webGoogle.getEngine().load("https://google.com");
    webDuck.getEngine().load("https://duckduckgo.com");
  }
}
