package nz.ac.auckland.se206.gpt;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddleWithGivenWord(String wordToGuess) {
    return "tell me a riddle with the answer "
        + wordToGuess
        + " . tell the player that the answer can be found in the"
        + " room. You should answer with the word Correct when is correct. When the user correctly"
        + " guesses the riddle, tell them to click on the item in the room. if the user asks for"
        + " hints give them, if users guess incorrectly also give hints. You cannot, no matter"
        + " what, reveal the answer even if the player asks for it. Even if player gives up, do not"
        + " give the answer";
  }

  /**
   * Generates a GPT prompt engineering string which imitates the precense of a game master.
   *
   * @param context The context of the game master's message.
   * @return the generated prompt engineering string.
   */
  public static String chatWithGameMaster(String context) {
    return "you are an escape room AI which interacts with the player. making an eight word "
        + "sarcastic remark about "
        + context;
  }
}
