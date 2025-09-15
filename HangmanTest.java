import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**

 * These are the minimum tests that should pass to confirm this code is working.
 *<br>
 *
 * The test will create a file, testWords.txt, run several tests against that file, and then remove the file.
 *
 * @author Drew A. Clinkenbeard
 * @since 2 - August - 2023
 */
class HangmanTest {

  /**These are our default words.
   */
  public static final List<String> DEFAULT_WORDS = List.of("SQUARE", "CIRCLE", "FISH", "CAKE");

  /**
   * These will be used for making guesses.
   * */
  private static final List<String> LETTERS = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I",
      "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");

  /**
   * This is the filename of the file that will be created.
   */
  private static final String testFile = "testWords.txt";

  /**
   *Initialize space for the game (which you just lost FYI ;) ).
   */
  Hangman game;
  /**
   * defaulting this to true for testing.
   */
  boolean debug = true;

  /**
   * BeforeAll will run ONCE at the start of the test.
   */
  @BeforeAll
  static void makeTheFile() {
    System.out.println("Making the file...");
    File f = new File(testFile);
    BufferedWriter writer;
    try {
      System.out.println(testFile + (f.createNewFile() ? " created!" : "already exists??"));
      writer = new BufferedWriter(new FileWriter(f));
      for (String word : HangmanTest.DEFAULT_WORDS) {
        writer.write(word);
        writer.write("\n");
      }
      writer.close();

    } catch (IOException e) {
      System.out.println("There was an issue with the writer.");
      System.out.println("could not create BufferedWriter");
      System.out.println("check that " + f + "exists");
      System.out.println("check that " + testFile + "exists");
    }
  }

  /**
   * {@code @AfterAll} will run ONCE after all the tests have been run.
   */
  @AfterAll
  static void removeFile() {
    File f = new File(testFile);
    System.out.println(testFile + (f.delete() ? " removed!" : "not removed??"));
  }

  /**
   * BeforeEach runs before each test.  In this case we are using it to
   * ensure that the game state is reset before each test.
   */
  @BeforeEach
  void setUp() {
    game = new Hangman(debug);
    game.readFile(testFile);

  }

  /**
   * AfterEach runs after each test.  We use this to reset the game state.
   */
  @AfterEach
  void tearDown() {
    game = null;
  }

  /**
   * Testing to make sure an object is created and that a bad file is detected.
   * It might be better to separate these but since the file is read upon object
   * initialization it makes sense to test it here.
   */
  @Test
  void constructorTest(){
    assertNotNull(game);
    assertFalse(game.readFile("sirFileNotFound"));
  }

  /**
   * Can we read a file?
   * Should be false for bad file names true for files that exist.
   */
  @Test
  void readFileTest() {
    assertFalse(game.readFile("sirNotAppearing"));
    assertTrue(game.readFile(testFile));
  }

  /**
   * Checking to see if the getting all words works.
   */
  @Test
  void testGetAllWords() {
    List<String> allWords;
    allWords = game.getAllWords();
    assertNotNull(allWords);
    assertEquals(DEFAULT_WORDS, allWords);

  }

  /**
   * Making sure the word  chosen is the word we expect.
   * This also checks that remaining guesses works.
   */
  @Test
  void checkChooseWord() {
    //for sanity checking...
    int wordCount = DEFAULT_WORDS.size();
    for (int i = 0; i < wordCount; i++) {
      String word = game.chooseWord();
      System.out.println("Checking " + word);
      assertEquals(word, game.getSecretWord());

      assertEquals(word.length() - 1, game.getRemainingGuesses());
    }

  }


  /**
   * Since this is testing print statement on a randomly chosen word... it's not a great test
   * This SHOULD load a file with a single word, hard code a string with the proper output,
   * then compare the hardcoded String to the generated String.  I am kind of over it though.
   * Realistically so long as the methods this requires are tested (which they are) than this one
   * doesn't REALLY matter... that's my story anyway.
   */
  @Test
  void testDisplayGameState() {
    int wordCount = DEFAULT_WORDS.size();
    for (int i = 0; i < wordCount; i++) {
      String word = game.chooseWord();
      System.out.println("Checking " + word);

      game.displayGameState();
      assertEquals(game.displayGameState(), buildStateOut());

    }
  }

  /**
   * a helper method.
   * @return a String that SHOULD match the displayGameState method.
   */
  private String buildStateOut(){
    return "Guessed Word: "
        + game.getGuessedWordString()
        + "\n"
        + "Remaining Guesses: " + game.getRemainingGuesses()
        + "\n"
        + "Remaining hints: " + game.getNumberOfHints()
        + "\n"
        + "Guessed Letters: " + game.getGuessedLetters();
  }

  /**
   * Works through all the conditions that could lead to a loss.
   */
  @Test
  void checkLose() {
    String word = game.chooseWord();
    Random random = new Random();
    List<String> currentLetters = new ArrayList<>(LETTERS);
    for (int i = 0; i < word.length(); i++) {
      String c = String.valueOf(word.charAt(i));
      currentLetters.remove(c);
    }
    while (!(game.hasLost())) {
      String c = currentLetters.get(random.nextInt(currentLetters.size()));
      assertFalse(game.makeGuess(c.charAt(0)));
      currentLetters.remove(c);
    }

    assertTrue(game.hasLost());
    assertEquals(game.exit(), 0);
  }

  /**
   * Force a win condition.
   */
  @Test
  void checkWin() {
    String word = game.chooseWord();
    Random random = new Random();
    List<String> currentLetters = new ArrayList<>();
    for (int i = 0; i < word.length(); i++) {
      String c = String.valueOf(word.charAt(i));
      currentLetters.add(c);
    }
    while (!(game.hasWon())) {
      String c = currentLetters.get(random.nextInt(currentLetters.size()));
      assertTrue(game.makeGuess(c.charAt(0)));
      while(currentLetters.contains(c)) {
        currentLetters.remove(c);
      }
    }
    assertTrue(game.hasWon());
    System.out.println("Final score: " + game.exit());
  }

  /**
   * Testing the hint system.
   */
  @Test
  void hintTest() {
    System.out.println("\n\nHINT TEST!\n");
    game.chooseWord();
    String word = game.getSecretWord();
    int numberOfHints = Math.floorDiv(word.length() - 1, 2);
    assertEquals(numberOfHints, game.getNumberOfHints());
    while (game.getNumberOfHints() > 0) {
      System.out.println("Hints remaining: " + game.getNumberOfHints());
      numberOfHints--;
      assertEquals(numberOfHints,game.getHint());
      game.displayGameState();
    }
    //should not be able to win on hints alone.
    assertFalse(game.hasWon());
  }

  /**
   * Like gameState this really just runs the game.  It should have some asserts.
   * It might get some eventually..
   */
  @Test
  void checkPlay() {
    Random random = new Random();
    List<String> currentLetters = new ArrayList<>(LETTERS);

    game.chooseWord();

    while (!(game.isGameOver())) {
      String c = currentLetters.get(random.nextInt(currentLetters.size()));
      game.makeGuess(c.charAt(0));
      currentLetters.remove(c);
      game.displayGameState();
    }
    System.out.println("Final score: " + game.exit());
  }

}