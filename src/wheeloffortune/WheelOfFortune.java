package wheeloffortune;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Phil O'Connell <pxo4@psu.edu>
 */
public class WheelOfFortune {

  // To read from the keyboard
  private static final Scanner _keyboard = new Scanner(System.in);

  // Used to get random values for puzzle and wheel
  private static final Random _random = new Random();

  // True if we want to show all letters
  private static boolean revealLetters = false;

  /*
  * These are the wedges that are part of the wheel.
  * There are 24.  Some values can appear more than once
  */
  private static final List<String> _wedges = Arrays.asList(
      /* 01 */"$5000",
      /* 02 */ "$600",
      /* 03 */ "$500",
      /* 04 */ "$300",
      /* 05 */ "$500",
      /* 06 */ "$800",
      /* 07 */ "$550",
      /* 08 */ "$400",
      /* 09 */ "$300",
      /* 10 */ "$900",
      /* 11 */ "$500",
      /* 12 */ "$300",
      /* 13 */ "$900",
      /* 14 */ "BANKRUPT",
      /* 15 */ "$600",
      /* 16 */ "$400",
      /* 17 */ "$300",
      /* 18 */ "LOSE-A-TURN",
      /* 19 */ "$800",
      /* 20 */ "$350",
      /* 21 */ "$450",
      /* 22 */ "$700",
      /* 23 */ "$300",
      /* 24 */ "$600"
  );

  /*
  * The number of wedges will not change throughout the game
  * We can cache the value so we're not calling .size() over and over
  */
  private static final int _wedgeCount = _wedges.size();

  private static String chooseRandomWedgeValue() {
    // Choose a random index
    int randomWedgeIndex = _random.nextInt(_wedgeCount);

    // Return the corresponding wedge
    return _wedges.get(randomWedgeIndex);
  }

  // The menu choices
  private static final List<String> _menuChoices = Arrays.asList(
      "1. Spin the wheel",
      "2. Buy a vowel",
      "3. Solve the puzzle",
      "4. Quit the game",
      "", // 5
      "", // 6
      "", // 7
      "8. Toggle puzzle reveal",
      "9. Test letter input"
  );

  // The different puzzles to choose from
  private static final List<String> _puzzles = Arrays.asList(
      "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG",
      "PENN STATE ABINGTON",
      "INFORMATION SCIENCES AND TECHNOLOGY"
  );

  /*
  * The number of puzzles will not change throughout the game
  * We can cache the value so we're not calling .size() over and over
  */
  private static final int _puzzlesCount = _puzzles.size();

  /*
  * We will store the guessed letters in a hash map.
  * The "key" will be the character that was guessed
  * The "value" will be true/false
  *
  * Actually, the "value" aspect of this is not relevant.
  * Just the fact that a letter appears in the map as a key, is enough to imply
  * it was guessed.
  */
  private static Map<Character, Boolean> guessedLetters = new HashMap<>();

  private static String maskPuzzle(String puzzle, boolean revealLetters) {
    StringBuilder maskedPuzzle = new StringBuilder();
    for (int i = 0; i < puzzle.length(); i++) {
      char c = puzzle.charAt(i);
      boolean isLetterGuessed = revealLetters || guessedLetters.containsKey(c);
      if (c != ' ' && !isLetterGuessed){
        c = '_';
      }
      maskedPuzzle.append(c + " ");
    }

    return maskedPuzzle.toString();
  }

  private static String chooseRandomPuzzle() {
    // Choose a random puzzle index
    int randomPuzzleIndex = _random.nextInt(_puzzlesCount);

    //Return the corresponding puzzle
    return _puzzles.get(randomPuzzleIndex);
  }

  private static final int _quitChoiceNumber = 4;

  // Determine if the given number choice actually appears on the menu
  private static boolean isValidMenuChoice(int choice) {
    if (choice < 1 || choice > _menuChoices.size()) {
      return false;
    }

    // Subtrace 1 because arrays/lists are zero-based
    int index = choice - 1;
    String menuText = _menuChoices.get(index);

    return !menuText.equals("");
  }

  // Input a letter from the keyboard
  private static char inputLetter() {
    char letter = ' ';
    boolean finished = false;

    while (!finished) {
      System.out.print("Enter a letter: ");

      String line = _keyboard.nextLine();
      if (line.length() != 1) {
        System.out.println("Enter just one letter");
      } else {
        // Convert letter to upper case
        letter = Character.toUpperCase(line.charAt(0));
        if (!Character.isLetter(letter)) {
          System.out.println("That is not a letter");
        } else {
          // Will exit the loop
          finished = true;
        }
      }
    }

    return letter;
  }

  // Display the game menu, and handle the choices made
  private static void gameMenu() {
    // Choice from the menu
    int choice = 0;

    // Line entered from keyboard
    String line = "";

    // True when user wants to quit
    boolean quit = false;

    // Choose one of the puzzles at random
    String puzzle = chooseRandomPuzzle();

    // Repeat the menu until the user chooses to quit
    while (!quit) {
      System.out.println("                      ======================");
      System.out.println("                      =  Wheel Of Fortune  =");
      System.out.println("                      ======================");
      System.out.println("                                            ");

      System.out.println(maskPuzzle(puzzle, revealLetters));
      System.out.println();

      // Loop over the menu choices, and display each one
      for (String menuChoice : _menuChoices) {
        // Skip blank place-holder choices
        if (!menuChoice.equals("")) {
          System.out.println(menuChoice);
        }
      }
      System.out.print("Enter choice: ");
      line = _keyboard.nextLine();
      try {
        // If the input was not an integer, then that error will be caught
        choice = Integer.parseInt(line);
      } catch (NumberFormatException nfe) {
        // Error message, then go to the top of the loop
        System.out.println("Invalid input");
        continue;
      }

      // If not valid, then go back to the top of the loop
      if (!isValidMenuChoice(choice)) {
        System.out.println("Not a menu choice");
        continue;
      }

      System.out.println("You chose: " + _menuChoices.get(choice - 1));
      switch (choice) {
        case _quitChoiceNumber:
          // This will allow us to leave the menu loop
          quit = true;
          break;

        case 1: // Spin the wheel
          System.out.println("You landed on: " + chooseRandomWedgeValue());
          char letter = inputLetter();
          System.out.println("Your letter is: " + letter);
          guessedLetters.put(letter, true);
          break;

        case 8: // Toggle reveal letters
          revealLetters = !revealLetters;
          break;

        case 9: // Test to read in a letter from the keyboard
          System.out.println("Your letter is: " + inputLetter());
          break;
      }
    }
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    gameMenu();
  }

}
