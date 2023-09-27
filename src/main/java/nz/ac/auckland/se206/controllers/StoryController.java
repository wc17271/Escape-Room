package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppScene;

public class StoryController {

  @FXML
  private void onContinue(MouseEvent event) {
    // Change scene to show tutorial:
    App.setScene(AppScene.TUTORIAL);
  }
}
