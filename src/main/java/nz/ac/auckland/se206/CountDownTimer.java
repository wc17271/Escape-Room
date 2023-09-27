package nz.ac.auckland.se206;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import nz.ac.auckland.se206.SceneManager.AppScene;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.controllers.HelpController;
import nz.ac.auckland.se206.controllers.KeypadController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.TelevisionController;

public class CountDownTimer {

  private static boolean start = false;

  /** Method which pauses the timer. */
  public static void pauseTimer() {
    start = false;
  }

  /** Method which starts the timer. */
  public static void startTimer() {
    start = true;
  }

  private RoomController roomController;
  private ChatController chatController;
  private TelevisionController televisionController;
  private HelpController helpController;
  private KeypadController keypadController;
  private Timer timer;

  private int minutes = 2;
  private int seconds = 0;

  // Constructor
  public CountDownTimer(
      RoomController roomController,
      ChatController chatController,
      TelevisionController televisionController,
      HelpController helpController,
      KeypadController keypadController) {
    this.roomController = roomController;
    this.chatController = chatController;
    this.televisionController = televisionController;
    this.helpController = helpController;
    this.keypadController = keypadController;
  }

  /**
   * Method which controls the flow of the two minute timer. This method allows us to update the
   * timer label in BOTH the room and the chat view.
   *
   * <p>Inspired by https://www.youtube.com/watch?v=zJZ-ogqin2o&t=140s&ab_channel=Randomcode
   */
  public void beginTimer() {
    timer = new Timer();

    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            if (start) {
              if (minutes == 0 && seconds == 0) {
                timer.cancel();

                // Switch to game over screen:
                App.setScene(AppScene.GAMEOVER);
              } else {
                if (seconds == 0) {
                  minutes--;
                  seconds = 59;
                } else {
                  seconds--;
                }
                Platform.runLater(
                    () -> {
                      // Update labels in room, chat and television view:
                      roomController.updateTimerLabel(minutes, seconds);
                      chatController.updateTimerLabel(minutes, seconds);
                      televisionController.updateTimerLabel(minutes, seconds);
                      helpController.updateTimerLabel(minutes, seconds);
                      keypadController.updateTimerLabel(minutes, seconds);
                    });
              }
            }
          }
        },
        0,
        1000);
  }
}
