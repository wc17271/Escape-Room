package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppScene;

public class StartScreenController {
  /** Switches the scene from the start GUI to tutorial GUI. */
  @FXML
  private void onLaunchGame() {
    // Change scene to show back story:
    App.setScene(AppScene.STORY);
  }

  /** Quits the game. */
  @FXML
  private void onLeaveGame() {
    System.exit(0);
  }
}
