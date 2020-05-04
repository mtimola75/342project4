import java.io.Serializable;
import java.util.ArrayList;

// Names: John Cervantes, Mike Lazzar, Marc Timola, Krzysztof Para
//
// Description of File:
//
// The file WordGuessInfo.java contains the WordGuessInfo class that implements Serializable
// and its purpose in the program is to be the only way of communication between the client and server
//
// WordGuessInfo: Public class implements the interface Serializable and is the only way of communication
// of client and server in the program
public class WordGuessInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5285659121272667822L;
	
	// Game status:
	// 0: Client Connection Success after Client ID Assignment
	// 1: Category Choice
	// 2: In game
	// 4: Client ID Assignment
	int gameStatus = 0;
	
	// Client ID integer variable to keep track of each client independently and know which client sent the WordGuessInfo object
	int clientID = 1;
	
	// Integer variable to keep track of number of guesses left
	int guessesLeft = 6;
	
	// Boolean values to control the game flow and monitor client progress
	boolean guessCorrect = false;
	boolean allCorrect = false;
	boolean foodFail = false;
	boolean animalFail = false;
	boolean stateFail = false;
	boolean foodAllCorrect = false;
	boolean animalAllCorrect = false;
	boolean stateAllCorrect = false;
	
	String message; // String variable to keep track of category choice of client
	
	String playerGuess; // String variable to keep track of client guess
	
	// Keeps track of current client string progress (Displays empty portions)
	String clientProgressGuess = "";
	
	// ArrayLists to keep track of the words available for each client
	ArrayList<String> foodList = new ArrayList<String>();
	ArrayList<String> animalList = new ArrayList<String>();
	ArrayList<String> stateList = new ArrayList<String>();
} // end WordGuessInfo
