package pong;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.util.Duration;
import pong.state.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Controller {
  public static Connection conn;
  public static PreparedStatement addGameWon, addUser, getUserDetails, getIdFromName, tryLogin;

  public static boolean call(PreparedStatement stmt, Object ... obj) {
    int i = 1;
    for(Object o : obj)
      try {
        stmt.setObject(i++, o);
      } catch (SQLException e) {
        System.out.println(e);
      }

      try {
        return stmt.execute();
      } catch (SQLException e) {
        System.out.println(e);
        return false;
      }
  }

  public static ResultSet query(PreparedStatement stmt, Object ... obj) {
    int i = 1;
    for(Object o : obj)
      try {
        stmt.setObject(i++, o);
      } catch (SQLException e) {
        System.out.println(e);
      }

    try {
      return stmt.executeQuery();
    } catch (SQLException e) {
      System.out.println(e);
      return null;
    }
  }


  public AnchorPane gameScene;
  public Label p1ScoreLbl;
  public Label p2ScoreLbl;
  public Label goalText;
  public Button p1Login, p2Login;
  public Canvas gfx;

  boolean p2IsBot = false;

  //Deque<State> stateStack = new ArrayDeque<>();

  // {paddle1: {leftCtrl, rightCtrl}, paddle2: ..., pauseBtn
  public Map<String, Boolean> controls = new HashMap<>(),
  // gives us simple "just pressed" style capabilities
        lastControls = new HashMap<>();


  Integer handleAIInterval = 0;

  public void setupHandlers() {
    gameScene.getScene().setOnKeyReleased(key -> setKeyStatesTo(false, key.getCode()));
    gameScene.getScene().setOnKeyPressed(key -> {
      setKeyStatesTo(true, key.getCode());
    });
  }

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

    ctx.fillRect(pos.x, pos.y, StateInfo.paddleSize.x, StateInfo.paddleSize.y);
  }

  public void drawPong(Vec2 pos, double angle, Paint color) {
    GraphicsContext ctx = gfx.getGraphicsContext2D();

    ctx.setLineWidth(5);
    ctx.setFill(color);
    ctx.setStroke(color);

    ctx.fillArc(pos.x, pos.y, StateInfo.pongSize.x, StateInfo.pongSize.y, 0, angle, ArcType.ROUND);
  }

  public void draw() {
    GraphicsContext ctx = gfx.getGraphicsContext2D();
    ctx.clearRect(0,0, StateInfo.extents.x, StateInfo.extents.y);

    StateInfo info = State.state;

    //System.out.println("Drawing to " + p1Pos);

    drawPaddle(info.paddle1Pos, Color.BLUE);
    drawPaddle(info.paddle2Pos, Color.RED);

    drawPong(info.pongPos, 360, Color.PURPLE);
  }

  public void createUser() {
    
  }

  public boolean login(int player) {
    TextInputDialog dia = new TextInputDialog();

    TextField userField = new TextField();
    PasswordField passField = new PasswordField();

    userField.setPromptText("username");
    passField.setPromptText("password");

    HBox cont = new HBox(userField, passField);

    dia.getDialogPane().setContent(cont);

    Button btn = (Button)dia.getDialogPane().lookupButton(ButtonType.OK);

    boolean[] ret = {false}; // Evil hackery to return from a lambda
    btn.setOnAction(e -> {
      ResultSet res = query(tryLogin, userField.getText(), passField.getText().hashCode());

      try {
        if (res.next()) {
          int id = res.getInt("id");
          if(player == 1)
            State.state.p1ID = id;
          else
            State.state.p2ID = id;

          ret[0] = true;
        } else
          ret[0] = false;

      } catch (SQLException ex) {
        System.out.println(ex);
      }
    });

    dia.showAndWait();

    return ret[0];
  }

  public void initialize() {
    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/leejo_pong?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", "leejo", "OverlyUnderlyPoweredMS");
      addGameWon = conn.prepareStatement("insert into Played (p1ID, p2ID, p1Score, p2Score, winner) values (?, ?, ?, ?, ?);");
      addUser = conn.prepareStatement("insert into User (displayName, email, passwd, genSet, bgSet) values (?, ?, ?, ?, ?);");
      getUserDetails = conn.prepareStatement("select * from User where User.id = ?;");
      getIdFromName = conn.prepareStatement("select id from User where User.displayName like ?;");
      tryLogin = conn.prepareStatement("select id from User where User.displayName like ? and User.passwd like ?;");
    } catch (SQLException e) {
      System.out.println(e);
    }

    State.state.init(controls, p1ScoreLbl, p2ScoreLbl, goalText);
    State.state.stateStack.push(new PlayState()); // Do nothing until a login
    StateInfo info = State.state;

    // If these aren't pre-initialized, we'll get nullptr exceptions galore.
    String[] usedMappings = {"P1Left", "P2Left", "P1Right", "P2Right", "Pause"};
    for(String str : usedMappings)
      controls.put(str, false);

    Timeline tick = TimelineBuilder
            .create()
            .keyFrames(
                    new KeyFrame(
                            new Duration(16.667), // 60 FPS
                            ev -> {
                              if(p2IsBot)
                                handleAI();

                              State curState = info.stateStack.getFirst();
                              if(curState.handle() == FlowControl.LeaveState)
                                info.stateStack.pop();

                              draw();

                              //System.out.println(info.stateStack);
                            }
                    )
            )
            .cycleCount(Timeline.INDEFINITE)
            .build();

    tick.play();

    p1Login.setOnAction(e -> {
      System.out.println(login(1));
    });


  }

  private void handleAI() {
    // Only handle every 100 ticks
    if (handleAIInterval++ < 10)
      return;
    else
      handleAIInterval = 0;

    StateInfo state = State.state;

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
