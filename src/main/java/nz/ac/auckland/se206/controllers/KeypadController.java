package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CountDownTimer;
import nz.ac.auckland.se206.SceneManager.AppScene;

/** This keypad check the code (28) is correct */
public class KeypadController {

  @FXML private Label keypadTimerLabel;

  private int code = 0;

  @FXML
  private void goBack(MouseEvent event) {
    code = 0;
    // Change scene back to room
    App.setScene(AppScene.ROOM);
  }

  @FXML
  private void onReset() {
    code = 0;
  }

  @FXML
  private void onCheck() {
    if (code != 28) {
      // show error message:
      showDialog("Error", "Incorrect code", "The code you entered was incorrect.");

      code = 0;
      return;
    }

    // show success, change scene to game finished
    CountDownTimer.pauseTimer();
    App.setScene(AppScene.GAMEFINISHED);
  }

  @FXML
  private void onClickOne() {
    code += 100;
  }

  @FXML
  private void onClickTwo() {

    // Check to make sure that the first value entered was a two:
    if (code == 8) {
      code += 200;
    }

    code += 20;
  }

  @FXML
  private void onClickThree() {
    code += 300;
  }

  @FXML
  private void onClickFour() {
    code += 400;
  }

  @FXML
  private void onClickFive() {
    code += 500;
  }

  @FXML
  private void onClickSix() {
    code += 600;
  }

  @FXML
  private void onClickSeven() {
    code += 700;
  }

  @FXML
  private void onClickEight() {
    code += 8;
  }

  @FXML
  private void onClickNine() {
    code += 900;
  }

  @FXML
  private void onClickZero() {
    code += 1000000;
  }

  /**
   * Updates the timer label with the current minutes and seconds.
   *
   * @param minutes The number of minutes remaining.
   * @param seconds The number of seconds remaining.
   */
  public void updateTimerLabel(int minutes, int seconds) {
    keypadTimerLabel.setText(String.format("%02d:%02d", minutes, seconds));
  }

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   */
  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
