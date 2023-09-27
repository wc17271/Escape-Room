package nz.ac.auckland.se206.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CountDownTimer;
import nz.ac.auckland.se206.GameMaster;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.NotificationBuilder;
import nz.ac.auckland.se206.SceneManager.AppScene;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.speech.TextToSpeech;
import org.controlsfx.control.Notifications;

/** Controller class for the room view. */
public class RoomController {

  @FXML private Label taskLabel;
  @FXML private Label roomTimerLabel;
  private TextToSpeech textToSpeech;
  private GameMaster gameMaster;
  private ChatMessage chatMessage;
  private int spamCount = 0;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // Initialize game master object:
    gameMaster = new GameMaster();
    gameMaster.chatCompletionRequest();
    gameMaster();
  }

  /**
   * Pauses the game, stopping the timer.
   *
   * @param event Mouse click event.
   */
  @FXML
  private void pauseGame(MouseEvent event) {
    CountDownTimer.pauseTimer();
    App.setScene(AppScene.PAUSE);
  }

  /** Opens the chat window with the game master. */
  @FXML
  private void openChat(MouseEvent event) {
    App.setScene(AppScene.CHAT);
  }

  /**
   * Opens the help window GUI and pauses the time.
   *
   * @param event Mouse click event.
   */
  @FXML
  private void openHelp(MouseEvent event) {
    App.setScene(AppScene.HELP);
  }

  /**
   * Updates the timer label with the current minutes and seconds.
   *
   * @param minutes The number of minutes remaining.
   * @param seconds The number of seconds remaining.
   */
  public void updateTimerLabel(int minutes, int seconds) {
    roomTimerLabel.setText(String.format("%02d:%02d", minutes, seconds));
  }

  /**
   * Updates the task label with a given message.
   *
   * @param message The message to be displayed.
   */
  public void updateTaskLabel(String message) {
    taskLabel.setText("Task: " + message);
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

  /**
   * Allows user to enter a pin to unlock the door
   *
   * @param event Mouse click event.
   */
  @FXML
  private void doorClick(MouseEvent event) {
    // If riddle has not been resolved, prompt user to solve the riddle:
    if (!GameState.isRiddleResolved) {
      showDialog(
          "Info",
          "Door Locked! Seems like we need a pin...",
          "Solve the riddle to recieve a clue!");

      // Take user to chat window to solve riddle:
      App.setScene(AppScene.CHAT);
      return;
    }

    // If riddle has been solved but the item has not been clicked:
    if (GameState.isRiddleResolved && !GameState.itemClicked) {
      showDialog(
          "Info",
          "Door Locked! Seems like we need a pin...",
          "Use your answer from the riddle to recieve the next clue!");
      return;
    }

    // If riddle has been solved and item has been clicked, prompt user to escape
    if (GameState.isRiddleResolved && GameState.itemClicked) {
      // Switch scene to the keypad:
      App.setScene(AppScene.KEYPAD);
      return;
    }
  }

  /**
   * If the lamp riddle is selected and solved, the user can interact with the lamp to see clue.
   *
   * @param event Mouse click event.
   */
  @FXML
  private void lampClick(MouseEvent event) {

    // if the riddle has NOT been solved give help
    if (!GameState.isRiddleResolved) {
      spamCount++;

      if (spamCount == 5) {
        Notifications message2 =
            NotificationBuilder.createNotification(
                "Open the chat to talk to me and get your first clue!", 6);
        message2.show();
      }
      return;
    }

    // If the item has already been clicked, dont let them click again.
    if (GameState.itemClicked) {
      return;
    }

    // if lamp riddle is selected and solved solved:
    if (GameState.isLamp && GameState.isRiddleResolved) {
      GameState.itemClicked = true;

      showDialog(
          "You've found a clue!",
          "TV Remote Found!",
          "You found the TV remote! Turn on the TV for your next clue!");

      updateTaskLabel("Turn on the TV!");

      Notifications message = NotificationBuilder.createNotification(chatMessage.getContent(), 6);
      message.show();
    }
  }

  /**
   * If the rug riddle is selected and solved, the user can interact with the rug to see clue.
   *
   * @param event Mouse click event.
   */
  @FXML
  private void rugClick(MouseEvent event) {

    // if the riddle has NOT been solved give help
    if (!GameState.isRiddleResolved) {
      spamCount++;

      if (spamCount == 5) {
        Notifications message2 =
            NotificationBuilder.createNotification(
                "Open the chat to talk to me and get your first clue!", 6);
        message2.show();
      }
      return;
    }

    // If the item has already been clicked, dont let them click again.
    if (GameState.itemClicked) {
      return;
    }

    // if rug riddle is selected and solved
    if (GameState.isRug && GameState.isRiddleResolved) {
      GameState.itemClicked = true;

      showDialog(
          "You've found a clue!",
          "TV Remote Found!",
          "You found the TV remote! Turn on the TV for your next clue!");

      updateTaskLabel("Turn on the TV!");

      Notifications message = NotificationBuilder.createNotification(chatMessage.getContent(), 6);
      message.show();
    }
  }

  /**
   * If the riddle has been solved, the user can interact with the TV to see clue.
   *
   * @param event Mouse click event.
   */
  @FXML
  private void televisionClick(MouseEvent event) {

    // If the riddle has not been solved, give user help
    if (!GameState.isRiddleResolved) {
      spamCount++;

      if (spamCount == 5) {
        Notifications message2 =
            NotificationBuilder.createNotification(
                "Open the chat to talk to me and get your first clue!", 6);
        message2.show();
      }
      return;
    }

    // if the item has been clicked, then change GUI to show the math problem on the screen
    if (GameState.itemClicked) {
      // Switch GUI to TV screen:
      App.setScene(AppScene.TELEVISION);
      updateTaskLabel("Enter the pin to the door!");
    }
  }

  /**
   * When the clock is clicked, the current local time is spoken.
   *
   * @param event Mouse click event.
   */
  @FXML
  private void clockClick(MouseEvent event) {
    Task<Void> clockSpeakTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            textToSpeech = new TextToSpeech();
            textToSpeech.speak(
                "The current time is: "
                    + new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));

            return null;
          }
        };

    Thread clockSpeakThread = new Thread(clockSpeakTask);
    clockSpeakThread.start();
  }

  /**
   * This method is called when the user clicks on the bookshelf:
   *
   * @param event Mouse click event.
   */
  @FXML
  private void bookClick(MouseEvent event) {
    Notifications message =
        NotificationBuilder.createNotification("Who even reads books these days!", 3);
    message.show();
  }

  /**
   * This method is called when the user clicks on the bookshelf:
   *
   * @param event Mouse click event.
   */
  @FXML
  void windowClick(MouseEvent event) {
    Notifications message =
        NotificationBuilder.createNotification("Ha ha ha... yeah.. good luck.", 3);
    message.show();
  }

  /**
   * This method is called when the user does not talk to the game master to answer the riddle.
   *
   * @param event Mouse click event.
   */
  @FXML
  private void helpUser(MouseEvent event) {

    spamCount++;

    // If the user clicks any part of the screen 6 times, help will be shown:
    if (spamCount == 5) {
      Notifications message2 =
          NotificationBuilder.createNotification(
              "Open the chat to talk to me and get your first clue!", 6);
      message2.show();
    }
  }

  /**
   * This method is called when the user clicks on the rug or lamp. It will call the game master
   * (powered by GPT) to generate a response.
   */
  private void gameMaster() {
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // Get game master response:
            ChatMessage msg =
                new ChatMessage(
                    "user",
                    GptPromptEngineering.chatWithGameMaster(
                        "about finally finding a television remote"));
            chatMessage = gameMaster.runGameMaster(msg);

            return null;
          }
        };

    Thread thread = new Thread(task);
    thread.start();
  }
}
