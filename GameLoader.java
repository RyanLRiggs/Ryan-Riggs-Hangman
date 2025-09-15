import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Following the @see <a href="https://google.github.io/styleguide/javaguide.html#s7-javadoc">Google
 * Java StyleGuide</a> For help with which tags to use @see <a
 * href="https://www.baeldung.com/tag/javadoc">Baeldung guide to Javadco</a>
 * <br>
 * The goal of the GameLoader is, surprisingly, to load the game.  The game loader provides a menu
 * to select a game and provides the loop to run the game.  The GameLoader requires each game to
 * expose an API as follows:
 * <ol>
 *   <li>play</li>
 *   <li>hasWon</li>
 *   <li>hasLost</li>
 *   <li>exit</li>
 *   <li>getScore</li>
 * </ol>
 */
public class GameLoader {

  /**
   * a global hangman game
   */
  private final Hangman hangman;
  /**
   * A list of games that can be played.  One day this might be a list of enums. For now, we only
   * have one game so a String is fine.
   */
  private final List<String> games = new ArrayList<>();

  /**
   * Used to run the GameLoader
   *
   * @param args needed for main
   */
  public static void main(String[] args) {
    GameLoader loader = new GameLoader();
    loader.gameLoop();
  }

  /**
   * No parameter constructor.  Calls the parameterized constructor with "hangmanWords.txt" Should
   * probably make that a constant.
   */
  public GameLoader() {
    this("hangmanWords.txt");
  }

  /**
   * Initialized a hangman instance. calls {@link Hangman#readFile(String)} calls
   * {@link GameLoader#games}.add and adds the string "Hangman"
   *
   *
   * @param wordsFile a String representing a filename
   */
  public GameLoader(String wordsFile) {
    hangman = new Hangman();
    hangman.readFile(wordsFile);

    games.add("Hangman"); //Hangman Should NOT be a String. Make it an enum.

  }

  /**
   * The main function of GameLoader.  This creates a text menu for Loading Hangman. It also keeps
   * track of the overall score.
   *
   */
  public void gameLoop() {
    System.out.println("Welcome to CST 338 Arcade!");

    Scanner scan = new Scanner(System.in);
    String choice = "";
    while (!choice.equalsIgnoreCase("zz")) {
      int score = 0;
      System.out.println("Current score: " + score);
      System.out.println("You may choose from the following games:");
      for (String game : games) {
        System.out.println("\t" + game);
      }
      System.out.print("Please enter your choice (zz to exit): ");
      choice = scan.nextLine();

      if (choice.equalsIgnoreCase(games.get(0))) {
        score += hangman();
      } else if (choice.equalsIgnoreCase("zz")) {
        System.out.println("Thank you for playing!");
      } else {
        System.out.println("I am not sure what " + choice + " is...");
      }

      System.out.println("Final Score: " + score);
    }
  }

  /**
   * A method that creates a hangman instance and calls the methods from Hangman. It should be
   * fairly evident from reading the code.
   *
   * @return the score earned in hangman.
   */
  public int hangman() {

    int hangmanScore = 0;
    int choice;

    Scanner scan = new Scanner(System.in);

    System.out.println("Welcome to Hangman!");
    System.out.println("Current score: " + hangmanScore);

    if (hangman.getCountWordsRemaining() <= 0) {
      System.out.println("Nothing left to guess!");
    }
    hangman.chooseWord();

    while (!hangman.isGameOver()) {
      hangman.displayGameState();
      System.out.println("Your options are:");
      System.out.println("\t 1: Guess a letter");
      if (hangman.getNumberOfHints() > 0) {
        System.out.println("\t 2: Get a hint");
      }
      System.out.println("\t 3: exit");

      System.out.print("Enter your choice:");

      String input = scan.nextLine();

      try {
        choice = Integer.parseInt(input);

      } catch (NumberFormatException e) {
        System.out.println(input + " is not a valid option.");
        continue;
      }

      switch (choice) {
        case 1: //Case 1 make a guess (these should probably be constants defined in hangman)
          System.out.print("What is your guess: ");
          input = scan.nextLine();
          hangman.makeGuess(input.charAt(0));
          break;

        case 2: // 2 get a hint
          if (hangman.getNumberOfHints() <= 0) {
            System.out.println("No more hints!");
            continue;
          }
          hangman.getHint();
          break;

        case 3: //Exit the game
          hangmanScore += hangman.exit();
          break;
      }

      if (hangman.hasWon()) {
        return hangman.exit();
      }
    }

    return hangmanScore;
  }
}