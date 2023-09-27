package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {

  public enum AppScene {
    START,
    ROOM,
    TUTORIAL,
    CHAT,
    PAUSE,
    GAMEOVER,
    TELEVISION,
    GAMEFINISHED,
    HELP,
    STORY,
    KEYPAD
  }

  // Hashmap which stores the states of scenes
  public static HashMap<AppScene, Parent> sceneMap = new HashMap<AppScene, Parent>();

  /**
   * Adds the inputted scene to the hashmap.
   *
   * @param scene The scene to be added (key).
   * @param root The root of the scene to be added (value).
   */
  public static void addScene(AppScene scene, Parent root) {
    sceneMap.put(scene, root);
  }

  /**
   * Retrieves the inputted scene from the hashmap.
   *
   * @param scene The scene to be retrieved.
   * @return The scene associated with the inputted scene.
   */
  public static Parent getScene(AppScene scene) {
    return sceneMap.get(scene);
  }
}
