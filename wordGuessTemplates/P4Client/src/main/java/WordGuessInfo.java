import java.io.Serializable;

public class WordGuessInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5285659121272667822L;
	
	int clientID = 1;
	int guessesLeft = 6;
	boolean guessCorrect = false;
	String message;
}
