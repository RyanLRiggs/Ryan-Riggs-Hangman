import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


/**
 *
 *<br>
 * This object represents a game of Hangman.
 * <br>
 * It requires GameLoader.java to run.
 * <br>
 *  @author Drew "Dr.C" Clinkenbeard And Ryan Riggs
 *  @since 3 - Sep - 2025
 */

public class Hangman {

  /**
   * The String used to represent a letter before it has been guessed.
   */
  private static final String PLACEHOLDER = "_";
  /**
   * this is the word the user is trying to guess
   */
  private String secretWord;

  /**<ul>
   *    <li> the placeholder for the word that is being guessed.  This will initially be populated with the
   *    '_' character for each character in secretWord.  As the player guesses correct letters those letters will replace
   *    the placeholder character.</li>
   *    <li>
   *      The String method {@link String#charAt(int)} method in the
   *{@link String} class will be useful {<a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html#charAt(int)">Oracle Documentation</a> }
   *    </li>
   *    <li>
   *      The StringBuilder method {@link StringBuilder#setCharAt(int, char)} method will be <i>very</i> helpful.
   *    </li>
   *   </ul>
   */
  private StringBuilder guessedWord;

  /**
   * Used to keep track of the remaining guesses.  It is set to {@link String#length secretWord.length()} - 1
   * */
  private int remainingGuesses;
  /**
   * Initialized to 0.  Incremented when a correct letter is guessed
   */
  private int score = 0;
  /**
   * This is set to the floor of remainingGuesses divided by 2 when a word is chosen
   */
  private int numberOfHints;
  /**
   * The structure used to keep track of, and display, all the letters that have been guessed.  This tracks the correct AND incorrect guesses.
   *  * Note that this is NOT the same as the StringBuilder guessedWord.
   */
  private List<Character> guessedLetters;
  /**
   * The words that may be selected for secretWord
   */
  private final List<String> allWords = new ArrayList<>();
  /**
   * The words that have been select for secretWord
   */
  private final List<String> guessedWords = new ArrayList<>();
  /**
   * Used as a flag to display additional information
   */
  private boolean debug = false;

  /**
   * The chooseWord method is one of the main the methods in the Hangman assignment.
   * This method uses a Random object to select a word from the list of unguessed words.
   * <br>
   * @return The word that was chosen.
   * <br><br>
   * A loop is used to ensure that the String selected from the List allWords does not exist in the list guessedWords.\
   * <br><br>
   * If the selected word DOES exist in the list guessedWords, a new String is selected.
   *<br><br>
   * Once a String that has not been guessed is selected, it is added to the List guessedWords.
   * <br>
   * The new String is assigned to the field secretWord.
   * <br><br>
   * The field remainingGuesses is set to the length of secretWord -1
   *<br><br>
   * the field numberOfHints is set to the floor of remainingGuesses divided by 2. Use {@link Math#floorDiv} to make this easier.
   *<br><br>
   * The field guessedLetters is initialized to a new ArrayList
   * <br><br>
   * guessedWord is initialized to a new StringBuilder of the same length as the field secretWord.
   * <br><br>
   * The character "_" is appended to guessedWord (I'd suggest the {@link String#repeat(int)} method)
   *<br><br>
   * If debug is true, display the selected word.
   *<br><br>
   *<br><br>
   *
   */
  public String chooseWord() {
    Random rand = new Random();
    String word;
    do {
        word = allWords.get(rand.nextInt(allWords.size()));
    } while (guessedWords.contains(word));

    guessedWords.add(word);
    secretWord = word.toUpperCase();
    remainingGuesses = secretWord.length() - 1;
    numberOfHints = Math.floorDiv(remainingGuesses, 2);
    guessedLetters = new ArrayList<>();
    guessedWord = new StringBuilder(PLACEHOLDER.repeat(secretWord.length()));

    if (debug) System.out.println("Chosen word: " + secretWord);
    return secretWord;
  }

  /**
   * Creates a File object using the parameter fileToLoad.  Creates a Scanner object.
   * <br>
   * @param fileToLoad a String representing the filename of the file we wish to load.
   * @return boolean true if the operation completes successfully; otherwise return false.
   * <br>
   * The Scanner is initialized in a try/catch to the file object.
   * <br>
   * Prints "There was an Issue creating or reading " + {fileToLoad} if there is an exception and
   * returns false.
   * <br>
   * While there are more tokens in the Scanner, add the next line from the File to the allWords field.
   * <br>
   * If debug is true, print 'adding: + {word}' where 'word' is the String being added to allWords.
   *
   *
   */
  public boolean readFile(String fileToLoad){
    try (Scanner sc = new Scanner(new File(fileToLoad))) {
        while (sc.hasNextLine()) {
            String word = sc.nextLine().trim();
            allWords.add(word);
            if (debug) System.out.println("adding" + word);
        }
        return true;
    } catch (FileNotFoundException e) {
        System.out.println("read file not implemented" + fileToLoad);
        return false;
    }
  }

  /**
   * No parameter constructor.
   * Calls the parameterized constructor with a parameter of {@code false }
   */
  public Hangman(){
    this(false);
  }

  /**
   * All this does is set the value of debug.
   * @param debug if this is set to {@code true} then Strings will be displayed.  See individual method documentation for details.
   */
  public Hangman(boolean debug) {
    this.debug = debug;
    guessedLetters = new ArrayList<>();
  }

  /**
   * Prints out the internal state of the game. Specifically, it creates a String with
   * {@link Hangman#guessedWord}, {@link Hangman#remainingGuesses}, {@link Hangman#numberOfHints},
   * and {@link Hangman#guessedLetters} and prints that.
   * See the sample output for an example.
   * @return A String representation of the game state.  Makes it testable.
   */
  public String displayGameState() {
    return "Guessed Word: " + guessedWord + "\n"
            + "Remaining Guesses: " + remainingGuesses + "\n"
            + "Remaining hints: " + numberOfHints + "\n"
            + "Guessed Letters: " + guessedLetters;
  }

  /**
   *
   * Used to exit Hangman. Sets {@link Hangman#remainingGuesses} to 0, prints
   * "Thanks for playing" and returns.
   *
   * @return if the result of {@link Hangman#hasWon()} is {@code true} '
   * return {@link Hangman#getScore()} otherwise return 0
   */
  public int exit(){
    remainingGuesses = 0;
    System.out.println("Thanks for playing");
    return hasWon() ? getScore() : 0;
  }

  /**
   * If {@link Hangman#numberOfHints} is less than or equal to 0, print "No more hints!" and
   * return numberOfHints.
   *<br>
   *If there are hints remaining, iterate through all the characters in {@link Hangman#secretWord}
   * until a character is found that is NOT contained in {@link Hangman#guessedLetters}
   * call the {@link Hangman#makeGuess(char)} method with the character.
   * <br>
   * Then decrement numberOfHints.
   * <br>
   * Exit the loop.
   *
   * @return the number of hints remaining.
   *
   */
  public int getHint(){
    if (numberOfHints <= 0) {
        System.out.println("No more hints!");
        return numberOfHints;
    };
    for (int i = 0; i < secretWord.length(); i++) {
        char c = secretWord.charAt(i);
        if (!guessedLetters.contains(c)) {
            makeGuess(c);
            numberOfHints--;
            break;
        }
    }

    return numberOfHints;
  }

  /**
   * Used to check the number of words left to guess.
   * @return the difference of {@code allWords.size()} and {@code guessedWords().size()}
   */
  public int getCountWordsRemaining(){
   return allWords.size() - guessedWords.size();
  }

  /**
   *  This can be done in one line... :)
   * @return {@code true} if there are no instances of {@link Hangman#PLACEHOLDER} in
   * {@link Hangman#guessedWord} otherwise {@code false}
   */
  public boolean hasWon() {
    return !guessedWord.toString().contains(PLACEHOLDER);
  }

  /**
   * won or lost?
   * @return {@code true} if {@link Hangman#remainingGuesses} == 0. Otherwise {@code false}
   */
  public boolean hasLost() {
    return remainingGuesses == 0;
  }

  /**
   * returns if the game is over (JavaDoc is PICKY).
   * @return {@code true} if the results either {@link Hangman#hasWon()} or {@link Hangman#hasLost()} is true. Otherwise {@code false}
   */
  public boolean isGameOver() {
    return hasWon() || hasLost();
  }

  /**
   * returns the score
   * @return {@link Hangman#remainingGuesses} + {@link Hangman#score}
   */
  public int getScore(){
    return remainingGuesses + score;
  }

  /**
   * This is the heavy lift for Hangman.
   * <br>
   * First the parameter {@code letter} is converted to uppercase.
   * <br>
   * Use {@link Character#toUpperCase(char)}
   * <br>
   * If {@link Hangman#guessedLetters} contains {@code letter} print the message:
   * "{@code letter } has already been guessed" and return {@code false}
   *
   * if the {@code letter } has NOT been guessed print the message:
   * "You chose: {@code letter } "
   * add {@code letter } tp {@link Hangman#guessedLetters}
   *
   * Declare a boolean to track if {@code letter } is found or not.
   * <br>
   * Iterate through the values in {@link Hangman#secretWord} checking for {@code letter }.
   * If {@code letter } is found, increment {@link Hangman#score} and set the corresponding location in {@link Hangman#guessedWord} to {@code letter }.
   * You will need to use {@link StringBuilder#setCharAt(int, char)}
   *
   * if {@code letter } was NOT found decrement {@link Hangman#remainingGuesses} print
   * {@code letter } was not present!
   * Return false.
   * <br>
   * if {@code letter } WAS found print
   * {@code letter } was present!
   * Return true.
   *
   * @param letter the character to guess.
   * @return true if the character exists in {@link Hangman#secretWord} and has not been guessed
   */
  public boolean makeGuess(char letter) {
    letter = Character.toUpperCase(letter);

    if (guessedLetters.contains(letter)) {
        System.out.println(letter + " has already been guessed");
        return false;
    }

    System.out.println("You chose: " + letter);
    guessedLetters.add(letter);

    boolean found = false;
    for (int i = 0; i < secretWord.length(); i++) {
        if (secretWord.charAt(i) == letter) {
            guessedWord.setCharAt(i, letter);
            score++;
            found = true;
        }
    }

    if (found) {
        System.out.println(letter + " was present!");
        return true;
    } else {
        remainingGuesses--;
        System.out.println(letter + " was not present!");
        return false;
    }
  }

  /*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
  ____ _  _ ____ ____ _   _ ___ _  _ _ _  _ ____    ___  ____ _    ____ _ _ _
  |___ |  | |___ |__/  \_/   |  |__| | |\ | | __    |__] |___ |    |  | | | |
  |___  \/  |___ |  \   |    |  |  | | | \| |__]    |__] |___ |___ |__| |_|_|

                          _ ____    ____ _ _  _ ____
                          | [__     |___ | |\ | |___
                          | ___]    |    | | \| |___
   =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

  /**
   * standard getter
   * @return secretWord
   */
  public String getSecretWord() {
    return secretWord;
  }

  /**
   * standard getter
   * @return allWords
   */
  public List<String> getAllWords() {
    return allWords;
  }

  /**
   * standard getter
   * @return guessedLetters.toString()
   */
  public String getGuessedLetters(){
    return guessedLetters.toString();
  }

  /**
   * standard getter
   * @return remainingGuesses
   */
  public int getRemainingGuesses() {
    return remainingGuesses;
  }

  /**
   * standard getter
   * @return numberOfHints
   */
  public int getNumberOfHints() {
    return numberOfHints;
  }

  /**
   * standard getter
   * @return guessedWord.toString()
   */
  public String getGuessedWordString() {
    return guessedWord.toString();
  }
}
