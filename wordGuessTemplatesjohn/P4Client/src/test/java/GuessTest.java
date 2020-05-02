import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GuessTest {

	Client c;
	WordGuessInfo gameInfo = new WordGuessInfo();

	// Test 1
	@Test
	void testGameStatus() {
		WordGuessInfo gameInfo = new WordGuessInfo();
		assertEquals(0, gameInfo.gameStatus, "Game status should be 0 but is " + gameInfo.gameStatus);
	}

	// Test 2
	@Test
	void testClientID() {
		WordGuessInfo gameInfo = new WordGuessInfo();
		assertEquals(1, gameInfo.clientID, "Client ID should be 1 but is " + gameInfo.clientID);
	}

	// Test 3
	@Test
	void testGuessesLeft() {
		WordGuessInfo gameInfo = new WordGuessInfo();
		assertEquals(6, gameInfo.guessesLeft, "Guesses left should be 6 but is " + gameInfo.guessesLeft);
	}

	// Test 4
	@Test
	void testGuessCorrect() {
		WordGuessInfo gameInfo = new WordGuessInfo();
		assertEquals(false, gameInfo.guessCorrect, "Guess correct should be false but is " + gameInfo.guessCorrect);
	}

	// Test 5
	@Test
	void testAllCorrect() {
		WordGuessInfo gameInfo = new WordGuessInfo();
		assertEquals(false, gameInfo.allCorrect, "All correct should be false but is " + gameInfo.allCorrect);
	}

	// Test 6
	@Test
	void testFoodFail() {
		WordGuessInfo gameInfo = new WordGuessInfo();
		assertEquals(false, gameInfo.foodFail, "Food fail should be false but is " + gameInfo.foodFail);
	}

	// Test 7
	@Test
	void testAnimalFail() {
		WordGuessInfo gameInfo = new WordGuessInfo();
		assertEquals(false, gameInfo.animalFail, "Animal fail should be false but is " + gameInfo.animalFail);
	}

	// Test 8
	@Test
	void testStateFail() {
		WordGuessInfo gameInfo = new WordGuessInfo();
		assertEquals(false, gameInfo.stateFail, "State fail should be false but is " + gameInfo.stateFail);
	}

	// Test 9
	@Test
	void testclientProgressGuess() {
		WordGuessInfo gameInfo = new WordGuessInfo();
		assertEquals("", gameInfo.clientProgressGuess,
				"Client progress guess should be blank but is " + gameInfo.clientProgressGuess);
	}

	// Test 9
	@Test
	void testplayerGuess() {
		WordGuessInfo gameInfo = new WordGuessInfo();
		assertEquals("", gameInfo.playerGuess, "Player guess should be blank but is " + gameInfo.playerGuess);
	}

}
