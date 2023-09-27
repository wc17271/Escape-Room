package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppScene;

public class HelpController {

  @FXML private Label helpTimerLabel;

  /** Switches the scene from the help GUI to start GUI and restart timer */
  @FXML
  private void onResumeGame(MouseEvent event) {
    App.setScene(AppScene.ROOM);
  }

  /**
   * Updates the timer label with the current minutes and seconds.
   *
   * @param minutes The number of minutes remaining.
   * @param seconds The number of seconds remaining.
   */
  public void updateTimerLabel(int minutes, int seconds) {
    helpTimerLabel.setText(String.format("%02d:%02d", minutes, seconds));
  }
}
