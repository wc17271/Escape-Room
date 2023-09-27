package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CountDownTimer;
import nz.ac.auckland.se206.GameMaster;
import nz.ac.auckland.se206.NotificationBuilder;
import nz.ac.auckland.se206.SceneManager.AppScene;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import org.controlsfx.control.Notifications;

public class TutorialController {

  private CountDownTimer timer;
  private ChatMessage chatMessage;
  private GameMaster gameMaster;

  public void initialize() throws ApiProxyException {
    // Fetch timer from App.java and store:
    timer = App.getTimer();

    // Initialize game master object:
    gameMaster = new GameMaster();
    gameMaster.chatCompletionRequest();

    // Pass message to AI and fetch response:
    ChatMessage msg =
        new ChatMessage(
            "user",
            GptPromptEngineering.chatWithGameMaster(
                "being stuck in this room and you need help escaping"));
    chatMessage = gameMaster.runGameMaster(msg);
  }

  /**
   * Switches the scene from the tutorial GUI to start GUI and starts timer.
   *
   * @throws IOException
   */
  @FXML
  private void onLaunchGame(MouseEvent event) throws IOException {

    // Start timer:
    timer = App.getTimer();
    timer.beginTimer();
    CountDownTimer.startTimer();

    // Fetch message from AI and show:
    Notifications message = NotificationBuilder.createNotification(chatMessage.getContent(), 7);
    message.show();
    App.setScene(AppScene.ROOM);
  }
}
