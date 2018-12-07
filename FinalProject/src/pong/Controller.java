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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.util.Duration;
import pong.state.*;

import java.sql.*;
import java.time.Instant;
import java.util.Deque;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Controller {
  static final String loginText = "Log in", logoutText = "Logout";
  public static Connection conn;
  public static PreparedStatement addGameWon, addUser, getUserDetails, getIdFromName, tryLogin, getAllPlayed, getLeaderboard;
  public static Map<KeyCode, String> mappings;
  public static TableView staticStatTable;
  static ObservableList<Leaderboard> leaderboard = FXCollections.observableArrayList();
  public Label p1NameLbl;
  public Label p2NameLbl;
  public TableColumn statIdCol;
  public TableColumn statUserCol;
  public TableColumn statScoreCol;
  public ImageView p1Avatar, p2Avatar;
  public Label p1ScoreLbl;
  public Label p2ScoreLbl;
  public Label goalText;
  public Button p1Login, p2Login, viewLeaderboard, backToGameBtn;
  public Canvas gfx;
  public TableView statTable;
  //Deque<State> stateStack = new ArrayDeque<>();

  // {paddle1: {leftCtrl, rightCtrl}, paddle2: ..., pauseBtn
  // gives us simple "just pressed" style capabilities
  public GridPane gameScene, statBox;
  Integer handleAIInterval = 0;
  State prevState = null;

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

  public static void refresh() {
    try {
      ResultSet allPlayed = query(getLeaderboard);
      leaderboard.clear();

      int i = 1;
      while (allPlayed.next())
        leaderboard.add(new Leaderboard(i++, allPlayed));


      staticStatTable.setItems(leaderboard);
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  public void setupHandlers() {
    gfx.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> setKeyStatesTo(true, event.getCode()));
    gfx.getScene().addEventFilter(KeyEvent.KEY_RELEASED, event -> setKeyStatesTo(false, event.getCode()));
  }

  public void setKeyStatesTo(boolean bool, KeyCode key) {
    //System.out.println("Got key " + key);

    String which;
    if (!mappings.containsKey(key))
      return;
    which = mappings.get(key);
    State.state.prevControls.put(which, State.state.controls.get(which));
    State.state.controls.put(which, bool);

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

  public void drawPong(Vec2 pos, double angle, Color color) {
    GraphicsContext ctx = gfx.getGraphicsContext2D();

    ctx.setLineWidth(5);
    ctx.setStroke(color);

    Vec2 tmp = new Vec2(pos);
//    double magic = 5.0;
//
//    tmp.mult(1.0/magic);
//    tmp.x = Math.round(tmp.x);
//    tmp.y = Math.round(tmp.y);
//    tmp.mult(magic);

    if (State.state.pongVeloc.length() >= 6.0) {
      Color tmpColor = color.invert().invert();
      Vec2 dir = State.state.pongVeloc.clone();
      Vec2 adderDir = dir.clone();
      adderDir.normalize();
      adderDir.mult(1.0 / 5.0);
      for (int i = 0; i < 10; i++) {
        tmpColor = tmpColor.deriveColor(0.0, 1.0, 0.95, 0.95);
        ctx.setFill(tmpColor);
        ctx.fillArc(tmp.x - dir.x, tmp.y - dir.y, GameVars.pongSize.x, GameVars.pongSize.y, 0, angle, ArcType.ROUND);
        dir.add(adderDir);
      }
    }

    ctx.setFill(color);
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
    dia.setHeaderText("REGISTER");
    dia.setGraphic(null);

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

  public void logout(int player) {
    if (player == 1) {
      State.state.p1 = null;
      p1Login.setText(loginText);
      p1NameLbl.setText("PLAYER 1");
      updateAvatar(1, GameVars.defaultP1);
    } else if (player == 2) {
      State.state.p2 = GameVars.botUser;
      p2Login.setText(loginText);
      p2NameLbl.setText("PLAYER 2");
      updateAvatar(2, GameVars.botUser);
    }
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
        State.state.stateStack.push(new WaitState(5));
      }
    }
  }

  private boolean login(int player) {

    Deque<State> stateStack = State.state.stateStack;
    boolean wePaused = false;
    if (!(stateStack.getFirst() instanceof PauseState) && !(stateStack.getFirst() instanceof DormantState)) {
      stateStack.push(new PauseState());
      wePaused = true;
    }

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

    if (ret[0]) {
      stateStack.push(new PlayState());
      if (player == 1) {
        p1Login.setText(logoutText);
        p1NameLbl.setText(State.state.p1.name);
        updateAvatar(1, State.state.p1);
      } else {
        p2Login.setText(logoutText);
        p2NameLbl.setText(State.state.p2.name);
        updateAvatar(2, State.state.p2);
      }
    }

    if (wePaused)
      stateStack.pop();

    return ret[0];
  }

  public void updateAvatar(int num, User profile) {
    String reqString = "https://robohash.org/" + profile.email + "?set=set" + profile.icon.val + "&bgset=bg" + profile.background.val + "&size=200x200";
    System.out.println(reqString);
    if (num == 1)
      p1Avatar.setImage(new Image(reqString));
    else
      p2Avatar.setImage(new Image(reqString));
  }

  public void initialize() {
    staticStatTable = statTable;
    System.out.println("Initializing...");
    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/leejo_pong?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", "leejo", "OverlyUnderlyPoweredMS");
      addGameWon = conn.prepareStatement("insert into Played (p1ID, p2ID, p1Score, p2Score, winner) values (?, ?, ?, ?, ?);");
      addUser = conn.prepareStatement("insert into User (displayName, email, passwd, genSet, bgSet) values (lcase(?), lcase(?), ?, ?, ?);");
      getUserDetails = conn.prepareStatement("select id, displayName, email, genSet, bgSet from User where User.id = ?;");
      getIdFromName = conn.prepareStatement("select id from User where User.displayName like lcase(?);");
      tryLogin = conn.prepareStatement("select id from User where User.displayName like lcase(?) and User.passwd like ?;");
      getAllPlayed = conn.prepareStatement("select * from Leaderboard;");
      getLeaderboard = conn.prepareStatement("select displayName, winCount from Leaderboard;");
    } catch (SQLException e) {
      System.out.println(e);
    }

    updateAvatar(1, GameVars.defaultP1);
    updateAvatar(2, GameVars.botUser);

    statIdCol.setCellValueFactory(new PropertyValueFactory<Leaderboard, Integer>("rank"));
    statUserCol.setCellValueFactory(new PropertyValueFactory<Leaderboard, String>("username"));
    statScoreCol.setCellValueFactory(new PropertyValueFactory<Leaderboard, Integer>("wins"));

    refresh();

    Object[][] newMappings = {
        {KeyCode.A, "P2Left"},
        {KeyCode.D, "P2Right"},
        {KeyCode.RIGHT, "P1Right"},
        {KeyCode.LEFT, "P1Left"},
        {KeyCode.ESCAPE, "Pause"},
    };

    mappings = new TreeMap<>();
    for (Object[] ar : newMappings)
      mappings.put((KeyCode) ar[0], (String) ar[1]);

    p2NameLbl.setText(State.state.p2.name);

    State.state.init(p1ScoreLbl, p2ScoreLbl, goalText);
    State.state.stateStack.push(new DormantState()); // Do nothing until a toggleLogin
    GameVars info = State.state;

    // If these aren't pre-initialized, we'll get nullptr exceptions galore.
    String[] usedMappings = {"P1Left", "P2Left", "P1Right", "P2Right", "Pause"};
    for (String str : usedMappings)
      info.controls.put(str, false);

    System.out.println("Creating update loop...");
    Timeline tick = TimelineBuilder
        .create()
        .keyFrames(
            new KeyFrame(
                new Duration(16.667), // 60 FPS = 16.67ms/frame
                ev -> {
                  if (State.state.p2 == GameVars.botUser)
                    handleAI();

                  State curState = info.stateStack.getFirst();
                  if (curState != prevState) { // We changed states, so we should handle that
                    if (prevState != null)
                      prevState.leave();
                    curState.enter();
                  }
                  FlowControl retCode = curState.handle();
                  if (retCode == FlowControl.LeaveState)
                    info.stateStack.pop();
                  else if (retCode == FlowControl.TransitionTo) {
                    curState.leave();
                    info.stateStack.pop();
                    info.stateStack.push(info.nextState);
                    curState = info.nextState;
                    curState.enter();
                    info.nextState = null;
                  }

                  draw();

                  if (prevState != null && prevState != curState)
                    System.out.println("Went from " + prevState + " to " + curState);

                  prevState = curState;

                  //System.out.println(info.stateStack);
                }
            )
        )
        .cycleCount(Timeline.INDEFINITE)
        .build();

    tick.play();

    p1Login.setText(loginText);
    p2Login.setText(loginText);
    p1Login.setOnAction(e -> {
      toggleLogin(1);
    });
    p2Login.setOnAction(e -> {
      toggleLogin(2);
    });

    viewLeaderboard.setOnAction(e -> {statBox.toFront();
    if(State.state.stateStack.getFirst().getClass() != PauseState.class)
      State.state.stateStack.push(new PauseState());
    });
    backToGameBtn.setOnAction(e -> {gameScene.toFront(); State.state.stateStack.pop();});


    System.out.println("Done initializing");
  }

  private void handleAI() {
    Random gen = new Random(Instant.now().getEpochSecond());

    // Only handle every x ticks
    if (handleAIInterval++ < (gen.nextDouble() * 15.0))
      return;
    else
      handleAIInterval = 0;

    GameVars state = State.state;

    // Only going off of the middle of each
    if ((state.pongPos.x + GameVars.pongSize.x / 2) < (state.paddle2Pos.x + GameVars.paddleSize.x / 2)) {
      state.controls.put("P2Left", true);
      state.controls.put("P2Right", false);
    } else {
      state.controls.put("P2Left", false);
      state.controls.put("P2Right", true);
    }
  }


  public enum Dir {
    Left, Right
  }
}
