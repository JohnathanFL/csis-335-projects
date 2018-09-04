/*
  Author: Johnathan Lee
  Due: Sep 7, 2018

  DiceGame:
  Simulates rolling 2 dice until a match is found between them. When the game is won, the user is notified how many
  rolls it took, and asked if they would like to play again.
 */

import java.util.Scanner;

public class DiceGame {
  public static void main(String[] args) {
    do {
      int rollCount = 1;
      while (!tossDice(6))
        rollCount++;

      System.out.printf("The game was won in %d games!\n", rollCount);
    } while (shouldContinue());
  }

  /**
   * Tosses 2 dice, and checks to see if they matched.
   *
   * @param sides How many sides does each die have?
   * @return Whether the dice were a match.
   */
  private static boolean tossDice(int sides) {
    int die1 = tossDie(sides), die2 = tossDie(sides);
    System.out.printf("Rolled %d and %d\n", die1, die2);

    return die1 == die2;
  }

  /**
   * Asks the user if we should continue.
   *
   * @return Whether the user has said we should continue.
   */
  private static boolean shouldContinue() {
    System.out.println("Would you like to continue playing?");
    System.out.print("Yes/No? >>");

    Scanner in = new Scanner(System.in);
    String input = in.next();

    //System.out.printf("Received as input: %s\n", input); // DEBUG

    return input.equals("Yes");
  }

  /**
   * Tosses a die and returns the result.
   *
   * @param sides How many sides does the die have?
   * @return A randomly generated number from 1..sides, inclusive.
   */
  private static int tossDie(int sides) {
    // (0...1) * (sides) + 1 = 1...(sides)
    return (int) (Math.random() * (double) sides + 1.0);
  }
}
