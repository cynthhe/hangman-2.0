/*
 * Hangman2.java
 * Author: Cynthia He, Laurenz Holcik, Evan Howell
 * Submission Date: October 16, 2018
 *
 * Purpose: This program is a word guessing game where the program gets a random word
 * from an enumerated list and the user has to guess letters until they get the word
 * correct, run out of guesses, or solve the word. A unique feature of this program
 * is that it allows the user to specify the number of spaces he/she wants to check.
 *
 * Statement of Academic Honesty:
 *
 * The following code represents my own work. I have neither
 * received nor given inappropriate assistance. I have not copied
 * or modified code from any source other than the course webpage
 * or the course textbook. I recognize that any unauthorized
 * assistance or plagiarism will be handled in accordance with
 * the University of Georgia's Academic Honesty Policy and the
 * policies of this course. I recognize that my work is based
 * on an assignment created by the Department of Computer
 * Science at the University of Georgia. Any publishing
 * or posting of source code for this project is strictly
 * prohibited unless you have written consent from the Department
 * of Computer Science at the University of Georgia.
 */

import java.util.Scanner;

public class Hangman2 {

    public static final boolean testingMode = true;
    public static final int numGuesses = 20;

    public static void main(String[] args) {

        // variables
        boolean play = true, roundOver = false, inputError, guessTrue, win = false;
        int guessesRemaining=0, numSpacesAllowed, roundScore, guess, totalScore = 0;
        String word, spaces, hiddenWord, killWhitespace;
        char letter;

        Scanner keyboard = new Scanner(System.in);

        while(play) {

            // reset and initialize variables and flags
            guess = 0;
            hiddenWord = "";
            roundOver = false;

            // get random word
            word = RandomWord.newWord();

            // show user hidden word, if testing mode is also actual word
            System.out.print("The word is: ");
            for(int i=0;i<word.length();i++) {
                hiddenWord = hiddenWord + "-";
            }
            System.out.println(hiddenWord);

            if(testingMode) {
                System.out.println("The secret word is: " + word);
            }

            // to avoid leftover newlines by keyboard.next methods the input was read in as a string
            // with nextLine and then converted to given data type (if possible)
            System.out.println("Enter the number of spaces allowed");
            numSpacesAllowed = Integer.parseInt(keyboard.nextLine());
            // handling wrong user input
            while(numSpacesAllowed <= 0 || numSpacesAllowed > word.length()) {
                System.out.println("Invalid input. Try again.");
                System.out.println("Enter the number of spaces allowed ");
                numSpacesAllowed = Integer.parseInt(keyboard.nextLine());
            }

            // game loop - loop for each guess; breaks when round is over
            while(!roundOver) {

                // reset variables
                spaces = "";
                guessTrue = false;
                inputError = false;

                // user input
                System.out.print("Please enter the letter you want to guess: ");
                letter = keyboard.nextLine().charAt(0);

                System.out.println("Please enter the spaces you want to check (separated by spaces):");
                for(int i=0;i<numSpacesAllowed;i++) {
                    spaces = spaces + keyboard.next();
                    if(spaces.length()>numSpacesAllowed) break;
                }
                killWhitespace = keyboard.nextLine(); //remove non-evaluated newline, using nextLine not possible because of the loop

                //check input validity
                for(int i=0;i<spaces.length();i++) {
                    if(!Character.isLetter(letter) || !Character.isDigit(spaces.charAt(i)) || Character.getNumericValue(spaces.charAt(i)) >= word.length() || Character.getNumericValue(spaces.charAt(i)) < 0) {
                        System.out.println("Your input is not valid. Try again.");
                        inputError = true;
                        break;
                    }
                }
                if(inputError) {
                    continue;
                }

                //evaluate if input character is in secret word
                for(int i=0;i < spaces.length();i++) {

                    int index = Character.getNumericValue(spaces.charAt(i));
                    if(letter==word.charAt(index)) {
                        hiddenWord = hiddenWord.substring(0,index) + word.charAt(index) + hiddenWord.substring(index+1);
                        guessTrue = true;
                    }
                }
                if(guessTrue) {
                    System.out.println("Your guess is in the word!");
                    System.out.println("The updated word is: " + hiddenWord);
                }
                else if(!guessTrue) {
                    System.out.println("Your letter was not found in the spaces you provided.");
                    guess += 1;
                }

                guessesRemaining = numGuesses - guess;
                System.out.println("Guesses remaining: " + guessesRemaining);

                if(guessesRemaining == 0) {
                    System.out.println("You have failed to guess the word... :(");
                    roundOver = true;
                }

                else if(hiddenWord.equals(word)) {
                    System.out.println("You have guessed the word! Congratulations");
                    roundOver = true;
                }

            }

            // calculate score and print
            roundScore = guessesRemaining*10/numSpacesAllowed;
            totalScore += roundScore;
            System.out.println("Score for this round: " + roundScore);
            System.out.println("Total Score: " + totalScore);

            // prompt user to play game again
            System.out.println("Would you like to play again? Yes(y) or No(n)");
            if(keyboard.nextLine().equals("n")) {
                play = false;
                System.exit(0);
            }
            else if(keyboard.nextLine().equals("y")){
                play = true;
            }

        }
    }
}