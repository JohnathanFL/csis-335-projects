package pong;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.util.Duration;
import pong.state.*;

import java.sql.*;
import java.util.*;

public class Controller {
  public static Connection conn;
  public static PreparedStatement addGameWon, addUser, getUserDetails, getIdFromName, tryLogin, getAllPlayed;

  public static boolean call(PreparedStatement stmt, Object... obj) {

    int i = 1;
    for (Object o : obj)
      try {
        stmt.setObject(i++, o);
      } catch (SQLException e) {
        System.out.println(e);
      }

    try {
      System.out.println(stmt);
      return stmt.execute();
    } catch (SQLException e) {
      System.out.println(e);
      return false;
    }
  }

  public static ResultSet query(PreparedStatement stmt, Object... obj) {
    int i = 1;
    for (Object o : obj)
      try {
        stmt.setObject(i++, o);
      } catch (SQLException e) {
        System.out.println(e);
      }

    try {
      System.out.println(stmt);
      return stmt.executeQuery();
    } catch (SQLException e) {
      System.out.println(e);
      return null;
    }
  }

  public Label p1ScoreLbl;
  public Label p2ScoreLbl;
  public Label goalText;
  public Button p1Login, p2Login, viewLeaderboard, backToGameBtn;
  public Canvas gfx;
  public TableView statTable;
  public GridPane gameScene, statBox;
  //Deque<State> stateStack = new ArrayDeque<>();

  // {paddle1: {leftCtrl, rightCtrl}, paddle2: ..., pauseBtn
  public Map<String, Boolean> controls = new HashMap<>(),
  // gives us simple "just pressed" style capabilities
  lastControls = new HashMap<>();


  Integer handleAIInterval = 0;

  public void setupHandlers() {
    gfx.getScene().setOnKeyReleased(key -> setKeyStatesTo(false, key.getCode()));
    gfx.getScene().setOnKeyPressed(key -> {
      setKeyStatesTo(true, key.getCode());
    });
  }

  public static Map<KeyCode, String> mappings;

  public void setKeyStatesTo(boolean bool, KeyCode key) {
    String which;
    switch (key) {
      case LEFT:
        which = "P1Left";
        break;

      case RIGHT:
        which = "P1Right";
        break;

      case A:
        which = "P2Left";
        break;

      case D:
        which = "P2Right";
        break;

      case ESCAPE:
        which = "Pause";
        break;

      default:
        return;
    }

    lastControls.put(which, controls.get(which));
    controls.put(which, bool);

    //System.out.println(this.controls);
  }

  public void drawPaddle(Vec2 pos, Paint color) {
    GraphicsContext ctx = gfx.getGraphicsContext2D();
    ctx.setLineWidth(5);

    ctx.setFill(color);

    Vec2 tmp = new Vec2(pos);
//    double magic = 5.0;
//
//    tmp.mult(1.0/magic);
//    tmp.x = Math.round(tmp.x);
//    tmp.y = Math.round(tmp.y);
//    tmp.mult(magic);


    ctx.fillRect(tmp.x, tmp.y, GameVars.paddleSize.x, GameVars.paddleSize.y);
  }

  public void drawPong(Vec2 pos, double angle, Paint color) {
    GraphicsContext ctx = gfx.getGraphicsContext2D();

    ctx.setLineWidth(5);
    ctx.setFill(color);
    ctx.setStroke(color);

    Vec2 tmp = new Vec2(pos);
//    double magic = 5.0;
//
//    tmp.mult(1.0/magic);
//    tmp.x = Math.round(tmp.x);
//    tmp.y = Math.round(tmp.y);
//    tmp.mult(magic);

    ctx.fillArc(tmp.x, tmp.y, GameVars.pongSize.x, GameVars.pongSize.y, 0, angle, ArcType.ROUND);
  }

  public void draw() {
    GraphicsContext ctx = gfx.getGraphicsContext2D();
    ctx.clearRect(0, 0, GameVars.extents.x, GameVars.extents.y);

    GameVars info = State.state;

    //System.out.println("Drawing to " + p1Pos);

    drawPaddle(info.paddle1Pos, new Color(0, 0, 1, 1));
    drawPaddle(info.paddle2Pos, new Color(1, 0, 0, 1));

    drawPong(info.pongPos, 360, Color.PURPLE);
  }

  public void gridSet(GridPane grid, Node[][] nodes) {
    int rowNum = 0;
    for (Node[] row : nodes) {
      int colNum = 0;
      for (Node node : row) {
        grid.add(node, colNum, rowNum);
        colNum++;
      }
      rowNum++;
    }
  }

  public void setDisable(boolean[] ar, Button btn) {
    boolean res = ar[0];
    for (boolean bool : ar)
      res = res & bool;
    System.out.println(res);
    btn.setDisable(!res);
  }

  public void createUser() {
    System.out.println("Creating user...");
    TextInputDialog dia = new TextInputDialog();
    dia.getDialogPane().getStylesheets().add(getClass().getResource("Pong.css").toExternalForm());


    TextField nameField = new TextField(), emailField = new TextField(), passwdField = new PasswordField();
    ComboBox<IconSet> iconCombo = new ComboBox<>(FXCollections.observableArrayList(IconSet.Standard, IconSet.Monster, IconSet.Suave, IconSet.Kitten));
    iconCombo.setValue(IconSet.Standard);
    ComboBox<BgSet> bgCombo = new ComboBox<>(FXCollections.observableArrayList(BgSet.None, BgSet.Scenery, BgSet.Radiance));
    bgCombo.setValue(BgSet.None);

    Label
        nameLbl = new Label("Display Name: "),
        emailLbl = new Label("Email: "),
        passwdLbl = new Label("Password: "),
        genLbl = new Label("Avatar Set: "),
        bgLbl = new Label("Background Set: ");
    GridPane pane = new GridPane();
    gridSet(pane, new Node[][]{
        {nameLbl, nameField},
        {emailLbl, emailField},
        {passwdLbl, passwdField},
        {genLbl, iconCombo},
        {bgLbl, bgCombo}
    });

    Button okBtn = (Button) dia.getDialogPane().lookupButton(ButtonType.OK);

    boolean[] isValid = {false, false, false};
    nameField.textProperty().addListener((e, oldVal, newVal) -> {
      isValid[0] = !newVal.isEmpty();
      setDisable(isValid, okBtn);
    });
    emailField.textProperty().addListener((e, oldVal, newVal) -> {
      isValid[1] = !newVal.isEmpty();
      setDisable(isValid, okBtn);
    });
    passwdField.textProperty().addListener((e, oldVal, newVal) -> {
      isValid[2] = !newVal.isEmpty();
      setDisable(isValid, okBtn);
    });

    okBtn.setDisable(true);

    okBtn.setOnAction(e -> {
      call(addUser,
          nameField.getText(),
          emailField.getText(),
          passwdField.getText().hashCode(),
          iconCombo.getValue().val,
          bgCombo.getValue().val
      );
    });

    dia.getDialogPane().setContent(pane);


    dia.show();
  }

  Map<Integer, User> users = new TreeMap<>();
  ObservableList<Played> played = FXCollections.observableArrayList();

  public void logout(int player) {
    if (player == 1)
      State.state.p1 = null;
    else if (player == 2)
      State.state.p2 = GameVars.botUser;
  }

  public void toggleLogin(int player) {
    if (player == 1 && State.state.p1 != null) {
      logout(player);
      State.state.stateStack.push(new DormantState());
    } else if (player == 2 && State.state.p2 != GameVars.botUser) {
      logout(player);
    } else {
      if (login(player) && player == 1) {
        State.state.stateStack.pop();
        State.state.stateStack.push(new PlayState());
      }
    }
  }

  private boolean login(int player) {

    Deque<State> stateStack = State.state.stateStack;
    if (!PauseState.class.isInstance(stateStack.getFirst()))
      stateStack.push(new PauseState());

    TextInputDialog dia = new TextInputDialog();
    dia.getDialogPane().getStylesheets().add(getClass().getResource("Pong.css").toExternalForm());
    dia.setGraphic(null);
    dia.setHeaderText("LOGIN");

    TextField userField = new TextField(), passField = new PasswordField();
    Label userLbl = new Label("Username: "), passLbl = new Label("Password: ");

    userField.setPromptText("username");
    passField.setPromptText("password");

    Button regBtn = new Button("Register");
    regBtn.setMaxWidth(Double.MAX_VALUE);
    regBtn.setMaxHeight(Double.MAX_VALUE);

    GridPane pane = new GridPane();
    pane.setVgap(10.0);
    gridSet(pane, new Node[][]{
        {userLbl, userField},
        {passLbl, passField},
    });

    pane.add(regBtn, 0, 2, 2, 1);

    dia.getDialogPane().setContent(pane);

    Button btn = (Button) dia.getDialogPane().lookupButton(ButtonType.OK);

    boolean[] ret = {false}; // Evil hackery to return from a lambda
    btn.setOnAction(e -> {
      ResultSet res = query(tryLogin, userField.getText(), passField.getText().hashCode());

      try {
        if (res.next()) {
          int id = res.getInt("id");
          if (player == 1)
            State.state.p1 = new User(query(getUserDetails, id));
          else
            State.state.p2 = new User(query(getUserDetails, id));

          System.out.println("Player 1 is: " + State.state.p1);
          System.out.println("Player 2 is: " + State.state.p2);

          ret[0] = true;
        } else
          ret[0] = false;

      } catch (SQLException ex) {
        System.out.println(ex);
      }
    });

    regBtn.setOnAction(e -> {
      createUser();
    });

    dia.showAndWait();

    if (ret[0])
      stateStack.push(new PlayState());

    return ret[0];
  }

  State prevState = null;

  public void refresh() {
    ResultSet allPlayed = query(getAllPlayed);
    played.clear();
    try {
      while (allPlayed.next())
        played.add(new Played(allPlayed));
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  public void initialize() {
    System.out.println("Initializing...");
    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/leejo_pong?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", "leejo", "OverlyUnderlyPoweredMS");
      addGameWon = conn.prepareStatement("insert into Played (p1ID, p2ID, p1Score, p2Score, winner) values (?, ?, ?, ?, ?);");
      addUser = conn.prepareStatement("insert into User (displayName, email, passwd, genSet, bgSet) values (?, ?, ?, ?, ?);");
      getUserDetails = conn.prepareStatement("select id, displayName, email, genSet, bgSet from User where User.id = ?;");
      getIdFromName = conn.prepareStatement("select id from User where User.displayName like ?;");
      tryLogin = conn.prepareStatement("select id from User where User.displayName like ? and User.passwd like ?;");
      getAllPlayed = conn.prepareStatement("select * from Played;");
    } catch (SQLException e) {
      System.out.println(e);
    }

    refresh();

    State.state.init(controls, p1ScoreLbl, p2ScoreLbl, goalText);
    State.state.stateStack.push(new DormantState()); // Do nothing until a toggleLogin
    GameVars info = State.state;

    // If these aren't pre-initialized, we'll get nullptr exceptions galore.
    String[] usedMappings = {"P1Left", "P2Left", "P1Right", "P2Right", "Pause"};
    for (String str : usedMappings)
      controls.put(str, false);

    System.out.println("Creating update loop...");
    Timeline tick = TimelineBuilder
        .create()
        .keyFrames(
            new KeyFrame(
                new Duration(16.667), // 60 FPS
                ev -> {
                  if (State.state.p2 == GameVars.botUser)
                    handleAI();

                  State curState = info.stateStack.getFirst();
                  if (curState != prevState) { // We changed states, so we should handle that
                    if(prevState != null)
                      prevState.leave();
                    curState.enter();
                  }

                  if (curState.handle() == FlowControl.LeaveState)
                    info.stateStack.pop();

                  draw();

                  prevState = curState;

                  //System.out.println(info.stateStack);
                }
            )
        )
        .cycleCount(Timeline.INDEFINITE)
        .build();

    tick.play();

    p1Login.setOnAction(e -> {
      toggleLogin(1);
    });
    p2Login.setOnAction(e -> {
      toggleLogin(2);
    });

    viewLeaderboard.setOnAction(e -> statBox.toFront());
    backToGameBtn.setOnAction(e -> gameScene.toFront());


    System.out.println("Done initializing");
  }


  private void handleAI() {
    // Only handle every 100 ticks
    if (handleAIInterval++ < 10)
      return;
    else
      handleAIInterval = 0;

    GameVars state = State.state;

    if (state.pongPos.x < state.paddle2Pos.x) {
      controls.put("P2Left", true);
      controls.put("P2Right", false);
    } else {
      controls.put("P2Left", false);
      controls.put("P2Right", true);
    }
  }


  public enum Dir {
    Left, Right
  }
}
