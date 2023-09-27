package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppScene;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.controllers.HelpController;
import nz.ac.auckland.se206.controllers.KeypadController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.TelevisionController;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  private static Scene currentScene;

  // Used to store a reference which can be passed:
  private static RoomController roomController;
  private static ChatController chatController;
  private static TelevisionController televisionController;
  private static HelpController helpController;
  private static KeypadController keypadController;
  private static CountDownTimer timer;
  private static Stage stageTest;

  public static void main(final String[] args) {
    launch();
  }

  /**
   * Returns a stage reference:
   *
   * @return Stage reference.
   */
  public static Stage getStage() {
    return stageTest;
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of FXML files to load without the .fxml extension.
   * @returnv The node associated to the input file.
   * @throws IOException
   */
  private static FXMLLoader loadLoader(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    return fxmlLoader;
  }

  /**
   * Used to set scenes that DO require the state the be saved.
   *
   * @param scene The scene to be set.
   */
  public static void setScene(AppScene scene) {
    currentScene.setRoot(SceneManager.getScene(scene));
  }

  /**
   * Used to fetch the timer object from other classes that need access to it.
   *
   * @return Timer object which have the references to controllers passed in.
   */
  public static CountDownTimer getTimer() {
    return timer;
  }

  /**
   * Returns the room controller.
   *
   * @return
   */
  public static RoomController getRoomController() {
    return roomController;
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Start" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    // Add scenes to hashmap.
    SceneManager.addScene(AppScene.START, loadLoader("start").load());
    SceneManager.addScene(AppScene.TUTORIAL, loadLoader("tutorial").load());
    SceneManager.addScene(AppScene.PAUSE, loadLoader("pause").load());
    SceneManager.addScene(AppScene.GAMEOVER, loadLoader("gameover").load());
    SceneManager.addScene(AppScene.GAMEFINISHED, loadLoader("gamefinished").load());
    SceneManager.addScene(AppScene.STORY, loadLoader("story").load());

    // // Store references to the room, chat, help and television controllers:
    FXMLLoader help = loadLoader("help");
    SceneManager.addScene(AppScene.HELP, help.load());
    helpController = help.getController();

    FXMLLoader room = loadLoader("room");
    SceneManager.addScene(AppScene.ROOM, room.load());
    roomController = room.getController();

    FXMLLoader chat = loadLoader("chat");
    SceneManager.addScene(AppScene.CHAT, chat.load());
    chatController = chat.getController();

    FXMLLoader television = loadLoader("television");
    SceneManager.addScene(AppScene.TELEVISION, television.load());
    televisionController = television.getController();

    FXMLLoader keypad = loadLoader("keypad");
    SceneManager.addScene(AppScene.KEYPAD, keypad.load());
    keypadController = keypad.getController();

    // Create timer object and pass in controllers:
    timer =
        new CountDownTimer(
            roomController, chatController, televisionController, helpController, keypadController);

    // Store stage reference:
    stageTest = stage;

    // Fetch start scene from hashmap and set scene:
    currentScene = new Scene(loadLoader("start").load(), 600, 500);
    stage.setScene(currentScene);
    stage.show();

    stage.setOnCloseRequest(e -> System.exit(0));
  }
}
