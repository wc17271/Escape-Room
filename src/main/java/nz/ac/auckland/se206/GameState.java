package nz.ac.auckland.se206;

/** Represents the state of the game. */
public class GameState {

  // Indicates which riddle has been selected:
  public static boolean isLamp = false;
  public static boolean isRug = false;

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved =
      false; // this is set to true when the GPT thing sees correct

  // check if item as told by the riddle has been clicked - this will allow the TV to be accessed
  public static boolean itemClicked = false;
}
