import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This might be overkill, but I want there to be tests for EVERYTHING.  This is as much modeling
 * good behaviour as anything.
 * This class exists to run Hangman.  Also, as a source for some hints...
 */
class GameLoaderTest {

  /**
   * Create memory space for our GameLoader object and a global variable.  It is initialized elsewhere.
   */
  GameLoader loader;
  /**
   * Our main test file.  It is created and deleted by this class.
   */
  private static final String testFile = "testWords.txt";
  /**
   * Another test file with a single word.
   */
  private static final String singleWord = "oneWord.txt";
  /**
   * The single word we are testing.
   */
  private static final String oneWord = "FISH";
  /**
   * This is tricky.  We are saving Standard.in, so we can replace it and fake keyboard input.  Neat!
   *
   */
  private InputStream originalSystemIn;
  /**
   * This is the input Stream that will fake keyboard input.
   */
  private ByteArrayInputStream testInput;

  /**
   * {@code @BeforeAll} runs before everything else.  THis is where the test files are created.
   */
  @BeforeAll
  static void makeTheFile(){
    System.out.println("Making the file...");
    File f = new File(testFile);
    File oneF = new File(singleWord);
    BufferedWriter writer;
    try{
      System.out.println( testFile + (f.createNewFile() ? " created!" : "already exists??" ));
      writer = new BufferedWriter(new FileWriter(f));
      String  testWords = "SQUARE+\n";
      testWords += "CIRCLE+\n";
      testWords += "FISH+\n";
      testWords += "CAKE+";

      writer.write(testWords);
      writer.close();
      writer = new BufferedWriter(new FileWriter(oneF));
      writer.write(oneWord);
      writer.close();
    } catch (IOException e) {
      System.out.println("There was an issue with the writer.");
      System.out.println("could not create BufferedWriter");
      System.out.println("check that " + f + "exists");
      System.out.println("check that " + testFile + "exists");
    }
  }

  /**
   * Runs after everything else.  Deletes the test files.
   */
  @AfterAll
  static void removeFile() {
    File f = new File(testFile);
    System.out.println(testFile + (f.delete() ? " removed!" : "not removed??"));
    File oneF = new File(singleWord);
    System.out.println(singleWord + (oneF.delete() ? " removed!" : "not removed??"));
  }

  /**
   * Runs before each test.  This is where we preserve System.in.
   */
  @BeforeEach
  void setUp() {
    originalSystemIn = System.in;
  }
  /**
   * Runs after each test.  This is where we restore System.in.
   * We could probably only do this ONCE but still.
   */
  @AfterEach
  void tearDown() {
    System.setIn(originalSystemIn);
  }

  /**
   * Just what it says on the tin.  Create a file and make sure it can be Scanned.
   */
  @Test
  void fileCreated(){
    File file = new File(testFile);
    Scanner scan = null;

    try{
      scan = new Scanner(file);
    }catch (FileNotFoundException e){
      System.out.println("issue creating/reading " + file);
    }

    assertNotNull(scan);

    while(scan.hasNext()){
      System.out.println(scan.nextLine());
    }

  }

  /**
   * I feel really proud of this.
   * Use a {@link ByteArrayInputStream} to fake user input.
   * Should open a game and exit.
   */
  @Test
  void gameLoaderTest(){
    String input = "1\ns\n2\n3\nzz\n\n";
    testInput = new ByteArrayInputStream(input.getBytes());
    loader = new GameLoader();
    System.setIn(testInput);
    assertEquals(0,loader.hangman());
  }

  /**
   * same as {@link GameLoaderTest#gameLoaderTest()} but testing win condition.
   */
  @Test
  void gameWinTest(){

    StringBuilder builder = new StringBuilder();
    for(char c : oneWord.toCharArray()){
      builder.append("1");
      builder.append("\n");
      builder.append(c);
      builder.append("\n");
    }

    testInput = new ByteArrayInputStream(builder.toString().getBytes());
    loader = new GameLoader(singleWord);

    System.setIn(testInput);

    assertEquals(oneWord.length(),loader.hangman());

  }

}