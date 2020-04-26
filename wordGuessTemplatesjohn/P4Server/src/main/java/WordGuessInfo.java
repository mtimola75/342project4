import java.io.Serializable;
import java.util.ArrayList;

public class WordGuessInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5285659121272667822L;
	// Game status:
	// 0: Category Choice
	// 1: In-game
	
	int gameStatus = 0;
	
	int clientID = 1;
	int guessesLeft = 6;
	boolean guessCorrect = false;
	String message;
	
	// Keeps track of current client string progress (Displays empty portions)
	String clientProgressGuess;
	
	ArrayList<String> foodList = new ArrayList<String>();
	ArrayList<String> animalList = new ArrayList<String>();
	ArrayList<String> stateList = new ArrayList<String>();
}