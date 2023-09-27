package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Random;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppScene;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** Controller class for the chat view. */
public class ChatController {
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;
  @FXML private Label chatTimerLabel;
  @FXML private ProgressBar progressBar;

  private ChatCompletionRequest chatCompletionRequest;

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    // Randomly select either lamp or rug as the word to guess:
    String wordToGuess;
    Random random = new Random();
    int randomInt = random.nextInt(20); // Picks a random number between 0 and 20

    if (randomInt <= 10) {
      wordToGuess = "lamp"; // If number is less than or equal to 10, word to guess is lamp
      GameState.isLamp = true;
    } else {
      wordToGuess = "rug";
      GameState.isRug = true;
    }

    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(1.3).setTopP(0.5).setMaxTokens(100);
    runGpt(new ChatMessage("user", GptPromptEngineering.getRiddleWithGivenWord(wordToGuess)));
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    chatTextArea.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      appendChatMessage(result.getChatMessage());
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      // TODO handle exception appropriately
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    String message = inputText.getText();

    // If the user has not entered a message, do nothing
    if (message.trim().isEmpty()) {
      return;
    }

    // If the user has entered a message:
    inputText.clear();
    progressBar.setProgress(0);
    sendButton.setDisable(true);

    // Add the user's message to the chat text area
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);

    // Create a new thread to run the GPT model
    Task<Void> gameMasterTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {

            updateProgress(0.5, 1);
            ChatMessage lastMsg = runGpt(msg);
            updateProgress(1, 1);
            // Reset the bar, indicating that the AI has finished responding
            updateProgress(0, 1);

            Platform.runLater(
                () -> {
                  // Check if the user has guessed the riddle:
                  if (lastMsg.getRole().equals("assistant")
                      && lastMsg.getContent().startsWith("Correct")) {
                    GameState.isRiddleResolved = true;

                    App.getRoomController().updateTaskLabel("Click on the riddle's answer!");
                  }
                });

            return null;
          }
        };

    // Update the progress bar as the GPT model runs
    progressBar.progressProperty().bind(gameMasterTask.progressProperty());

    // Unbind:
    gameMasterTask.setOnSucceeded(
        e -> {
          sendButton.setDisable(false);
          progressBar.progressProperty().unbind();
        });

    gameMasterTask.setOnFailed(
        e -> {
          sendButton.setDisable(false);
        });

    // Start the thread
    Thread gameMasterThread = new Thread(gameMasterTask);
    gameMasterThread.start();
  }

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    App.setScene(AppScene.ROOM);
  }

  /**
   * Updates the timer label with the current minutes and seconds.
   *
   * @param minutes The number of minutes remaining.
   * @param seconds The number of seconds remaining.
   */
  public void updateTimerLabel(int minutes, int seconds) {
    chatTimerLabel.setText(String.format("%02d:%02d", minutes, seconds));
  }
}
