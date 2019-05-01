/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Hangman extends ConsoleProgram {

	/***********************************************************
	 *              CONSTANTS                                  *
	 ***********************************************************/
	
	/* The number of guesses in one game of Hangman */
	private static final int N_GUESSES = 7;
	/* The width and the height to make the karel image */
	private static final int KAREL_SIZE = 150;
	/* The y-location to display karel */
	private static final int KAREL_Y = 230;
	/* The width and the height to make the parachute image */
	private static final int PARACHUTE_WIDTH = 300;
	private static final int PARACHUTE_HEIGHT = 130;
	/* The y-location to display the parachute */
	private static final int PARACHUTE_Y = 50;
	/* The y-location to display the partially guessed string */
	private static final int PARTIALLY_GUESSED_Y = 430;
	/* The y-location to display the incorrectly guessed letters */
	private static final int INCORRECT_GUESSES_Y = 460;
	/* The fonts of both labels */
	private static final String PARTIALLY_GUESSED_FONT = "Courier-36";
	private static final String INCORRECT_GUESSES_FONT = "Courier-26";
	
	/***********************************************************
	 *              Instance Variables                         *
	 ***********************************************************/
	
	/* An object that can produce pseudo random numbers */
	private RandomGenerator rg = new RandomGenerator();
	
	private GCanvas canvas = new GCanvas();
	
	private String secretWord = "";
	private String wordState = "";
	private char guess;
	private String newWordState = "";
	private int guessLeft = N_GUESSES;
	
	
	/***********************************************************
	 *                    Methods                              *
	 ***********************************************************/
	
	public void run() {
		// shall we?
		secretWord = getRandomWord();
		println("Welcome to Hangman");
		wordState = displayHint(secretWord);
		
		while(true) {
			
			println("Your word now looks like this: " + wordState);
			println("You have " + guessLeft + " guesses left.");
			
			guess = getGuess();
			wordState = checkGuessLetter(guess);
			guessLeftTracker();
			
			if(checkEndGame() == true) {
				break;
			}
		}
		
	}
	
	/**
	 * Method: Get Random Word
	 * -------------------------
	 * This method returns a word to use in the hangman game. It randomly 
	 * selects from among 10 choices.
	 */
	private String getRandomWord() {
		int index = rg.nextInt(10);
		if(index == 0) return "BUOY";
		if(index == 1) return "COMPUTER";
		if(index == 2) return "CONNOISSEUR";
		if(index == 3) return "DEHYDRATE";
		if(index == 4) return "FUZZY";
		if(index == 5) return "HUBBUB";
		if(index == 6) return "KEYHOLE";
		if(index == 7) return "QUAGMIRE";
		if(index == 8) return "SLITHER";
		if(index == 9) return "ZIRCON";
		throw new ErrorException("getWord: Illegal index");
	}
	/**
	 * Method: displayHint
	 * --------------------
	 * This method will hide the secretWord by converting every letter
	 * of the secretWord into "-" and return it as a string. 
	 *
	 * @param secretWord
	 * @return
	 */
	private String displayHint(String secretWord) {
		String result = "";
		for(int i = 0; i < secretWord.length(); i++) {
			result += "-";
		}
		return result;
	}
	
	/**
	 * Method: getGuess
	 * -------------------
	 * This method will read input from the user. The input must be a
	 * single letter. If it is not a single letter, it will display
	 * an error message and prompt the user until he/she input a single
	 * letter.
	 * 
	 * @return
	 */
	private char getGuess() {
		while(true) {
			String strGuess = readLine("Your guess: ");
			guess = strGuess.charAt(0);
			guess = Character.toUpperCase(guess);
			if(!Character.isLetter(guess) || strGuess.length() > 1) {
				println("Invalid input. Please enter single letter!");
			} else if(Character.isLetter(guess)) {
				break;
			}
		}
		return guess;
	}
	
	/**
	 * Method: checkGuessLetter
	 * ----------------------------
	 * This method will check the letter that user has guessed.
	 * If the user guessed a correct letter, it will be displayed.
	 * If the user guessed letter enter correct letter more than
	 * once, nothing happen.
	 * 
	 * @param guess
	 * @return
	 */
	private String checkGuessLetter(char guess) {
		String result = "";
		
		for(int i = 0; i < secretWord.length(); i++) {
			char ch = secretWord.charAt(i);
			if(ch == guess) {
				result += ch;
			} else if(ch == wordState.charAt(i)) {
				result += ch;
			} else if(ch != guess) {
				result += "-";
			}
		}
		return result;
	}
	
	/**
	 * Method: guessLeftTracker
	 * --------------------------
	 * This method will decreased the number of guess left whenever
	 * the user input incorrect letter.
	 */
	private void guessLeftTracker() {
		if(secretWord.indexOf(guess) < 0) {
			guessLeft--;
			println("There are no " + guess + "'s " + "in the word.");
		} else if(secretWord.indexOf(guess) > 0) {
			println("That guess is correct");
		}
	}
	
	/**
	 * Method: checkEndGame
	 * -----------------------
	 * This method will check the end condition of the game.
	 * if number of guess left equals to zero, the game is over, and
	 * it will display "You are completely hung". Then it will return
	 * boolean value of true.
	 * 
	 * If the user has guess all the letter correctly, the game is ended,
	 * and display "You win." Then it will return boolean value of true.
	 * 
	 * If the both of this conditions are not satisfied, it will return
	 * false.
	 */
	private boolean checkEndGame() {
		if(guessLeft == 0) {
			println("You're completely hung.");
			println("The word was: " + secretWord);
			return true;
		} else if(secretWord.equals(wordState)) {
			println("You win.");
			println("The word was: " + secretWord);
			return true;
		}
		return false;
	}

}
