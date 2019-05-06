/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
	
	/* Variables for the secret word and the number of guess left */
	private String secretWord = "";
	private String wordState = "";
	private int guessLeft = N_GUESSES;
	/* The guessed letter, typed in by the user. */
	private char guess;
	
	/* Line objects as strings for Karel's parachute */
	private GLine line1;
	private GLine line2;
	private GLine line3;
	private GLine line4;
	private GLine line5;
	private GLine line6;
	private GLine line7;
	
	/* Image object for Karel and his parachute */
	private GImage karel = new GImage("karel.png");
	private GImage parachute = new GImage("parachute.png");
	
	/* Labels to show the guessed letter */
	private GLabel partiallyGuessed = new GLabel("");
	private GLabel incorrectGuesses = new GLabel("");
	/* Variable to track the wrong letter */
	private String wrongLetter = "";
	
	
	/* An array that will contain secret words for Hangman game */
	private ArrayList<String> wordList = new ArrayList<String>();
	
	
	
	/***********************************************************
	 *                    Methods                              *
	 ***********************************************************/
	
	public void init() {
		add(canvas);
	}
	
	public void run() {
		openHangmanLexicon();
		
		drawBackground();
		drawParachute();
		drawKarel();
		drawString();
		
		secretWord = getRandomWord2();
		println("Welcome to Hangman");
		wordState = displayHint(secretWord);
		
		while(true) {
			partiallyGuessedLabel();
			incorrectGuessesLabel();
		
			if(checkEndGame() == true) {
				break;
			}
			
			println("Your word now looks like this: " + wordState);
			println("You have " + guessLeft + " guesses left.");
			
			guess = getGuess();
			wordState = checkGuessLetter(guess);
			guessLeftTracker();
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
	 * Method: getRandomWord2
	 * ------------------------
	 * This method will return a random word to use in the Hangman game.
	 * It randomly select a word from the ArrayList named wordList.
	 * @return
	 */
	private String getRandomWord2() {
		int maxIndex = wordList.size();
		int index = rg.nextInt(maxIndex - 1);
		String word = wordList.get(index);
		
		return word;
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
			wrongLetter += guess;
			removeString(guessLeft);
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
			flipKarel();
			return true;
		} else if(secretWord.equals(wordState)) {
			println("You win.");
			println("The word was: " + secretWord);
			return true;
		}
		return false;
	}
	
	/**
	 * Method: drawBackground
	 * -----------------------
	 * This method will draw a backgound image to the canvas.
	 */
	private void drawBackground() {
		GImage bg = new GImage("background.jpg");
		bg.setSize(canvas.getWidth(), canvas.getHeight());
		canvas.add(bg, 0, 0);
	}
	
	/**
	 * Method: drawParachute
	 * -----------------------
	 * This method will draw parachute for Karel
	 * in the center of the canvas.
	 */
	private void drawParachute() {
		parachute.setSize(PARACHUTE_WIDTH, PARACHUTE_HEIGHT);
		double x = (canvas.getWidth() - parachute.getWidth()) / 2;
		canvas.add(parachute, x, PARACHUTE_Y);
	}
	
	/**
	 * Method: drawKarel
	 * -------------------
	 * This method will draw Karel in upright position and
	 * place it in the center of the canvas.
	 */
	private void drawKarel() {
		karel.setSize(KAREL_SIZE, KAREL_SIZE);
		double x = (canvas.getWidth() - karel.getWidth()) / 2;
		canvas.add(karel, x, KAREL_Y);
	}
	
	/**
	 * Method: drawString
	 * --------------------
	 * This method will draw seven strings of the Karel's parachute.
	 * Each string will be named line1, line2, line3, line4, line5,
	 * line6, line7. These seven strings indicate the number of guess
	 * left. When the user guess wrong letter, one string will
	 * disappear.
	 */
	private void drawString() {
		double startX = (canvas.getWidth() - parachute.getWidth()) / 2;
		double startY = PARACHUTE_Y + parachute.getHeight();
		double stringDistance = parachute.getWidth() / 6;
		double endX = canvas.getWidth() / 2;
		double endY = KAREL_Y;
		
		line1 = new GLine(startX, startY, endX, endY);
		canvas.add(line1);
		line2 = new GLine(startX + parachute.getWidth(), startY, endX, endY);
		canvas.add(line2);
		line3 = new GLine(startX + stringDistance, startY, endX, endY);
		canvas.add(line3);
		line4 = new GLine(startX + 5 * stringDistance, startY, endX, endY);
		canvas.add(line4);
		line5 = new GLine(startX + 2 * stringDistance, startY, endX, endY);
		canvas.add(line5);
		line6 = new GLine(startX + 4 * stringDistance, startY, endX, endY);
		canvas.add(line6);
		line7 = new GLine(startX + 3 * stringDistance, startY, endX, endY);
		canvas.add(line7);	
	}
	
	/**
	 * Method: removeString
	 * ---------------------
	 * This method will turn one string invisible. When the user guess
	 * wrong letter this method will be called. 
	 * @param guessLeft
	 */
	private void removeString(int guessLeft) {
		switch(guessLeft) {
		case 6: line1.setVisible(false); break;
		case 5: line2.setVisible(false); break;
		case 4: line3.setVisible(false); break;
		case 3: line4.setVisible(false); break;
		case 2: line5.setVisible(false); break;
		case 1: line6.setVisible(false); break;
		case 0: line7.setVisible(false); break;
		}
	}
	
	/**
	 * Method: partiallyGuessedLabel
	 * -------------------------------
	 * This method will show a label of correct letter 
	 * that the user has guessed. The label will be positioned
	 * below Karel.
	 */
	private void partiallyGuessedLabel() {
		partiallyGuessed.setLabel(wordState);
		partiallyGuessed.setFont(PARTIALLY_GUESSED_FONT);
		double x = (canvas.getWidth() - partiallyGuessed.getWidth()) / 2;
		canvas.add(partiallyGuessed, x, PARTIALLY_GUESSED_Y);
	}
	
	/**
	 * Method: incorrectGuessesLabel
	 * ------------------------------
	 * This method will show a label of incorrect letter
	 * that the user has guessed. The label will be positioned
	 * below the partiallyGuessed label.
	 * 
	 */
	private void incorrectGuessesLabel() {
		incorrectGuesses.setLabel(wrongLetter);
		incorrectGuesses.setFont(INCORRECT_GUESSES_FONT);
		double x = (canvas.getWidth() - incorrectGuesses.getWidth()) / 2;
		canvas.add(incorrectGuesses, x, INCORRECT_GUESSES_Y);
	}
	
	/**
	 * Method flipKarel
	 * ------------------
	 * This method will change the karel image to upside down.
	 */
	private void flipKarel() {
		karel.setImage("karelFlipped.png");
		drawKarel();
	}
	
	/**
	 * Method: openHangmanLexicon
	 * This method will open a file "HangmanLexicon.txt" which contain
	 * a library of secret words for the Hangman game. Each word will
	 * be added to the wordList which is an ArrayList declared as
	 * instance variable.
	 */
	private void openHangmanLexicon() {
		try {
			Scanner lexicon = new Scanner(new File("HangmanLexicon.txt"));
			while(lexicon.hasNextLine()) {
				String line = lexicon.nextLine();
				wordList.add(line);
			}
			lexicon.close();
		} catch(IOException e) {
			println("Oops the file didn't open");
		}
		
	}

}
