import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GuessTest {

	Client c;
	
	// setUpClient():
	//
	// Assign the variable c, to a new Client instance passing the port number of 5555 and IP address of "127.0.0.1" to the
	// parameterized constructor
	//
	@BeforeEach
	void setUpCode()
	{
		c = new Client(client->{
		}, 5555, "127.0.0.1");
	} // end setUpCode()
	
	// testInitClientInstance():
	//
	// Test if client is initialized to correct object type, that is, the name of the class is Client and so it should be the string returned by getClass().getName()
	//
	@Test
	void testInitClientInstance()
	{
		assertEquals("Client", c.getClass().getName(), "Incorrect initialization of the client, c. The returned result should be Client");
	} // end testInitClientInstance()
	
	// testInitWordGuessInfoClient()
	//
	// Test if the WordGuessInfoClient variable of client, gameInfo, was initialized correctly with the client, c in the setUpCode() method
	//
	@Test
	void testInitWordGuessInfoClient()
	{
		assertEquals("WordGuessInfo", c.gameInfo.getClass().getName(), "Incorrect initialization of the WordGuessInfo variable in client c, should be of type WordGuessInfo");
	} // end testInitWordGuessInfoClient()
	
	// testClientOutStream():
	//
	// Test if the output stream of the Client instance, c, is currently null as the client has not opened up the specific stream
	//
	@Test
	void testClientOutStream()
	{
		assertNull(c.out, "The output stream of the client should be null as the socket has not be established with the server");
	} // end testClientOutStream()
	
	// testClientInStream():
	//
	// Test if the input stream of the Client instance, c, is currently null as the client has not opened up the specific stream
	//
	@Test
	void testClientInStream()
	{
		assertNull(c.in, "The input stream of the client should be null as the socket has not be established with the server");
	} // end testClientInStream()
	
	// testClientIPAddress():
	//
	// Test if the client, c, is initialized correctly to have the right IP address passed via the parameterized constructor
	//
	@Test
	void testClientIPAddress()
	{
		assertEquals("127.0.0.1", c.ipAddress.toString(), "Incorrect IP address stored inside Client instance, c");
	} // end testClientIPAddress()
	
	// testClientSocket():
	//
	// Test if the Client instance, c, is initialized correctly to have the Socket member socketClient set to null when created
	//
	@Test
	void testClientSocket()
	{
		assertNull(c.socketClient, "Incorrect result, the socketClient of the Client c, should have the value of null as it has not be initialized");
	} // end testClientSocket()
	
	// testClientGameInfoInit()
	//
	// Test if the Client instance, c, is initialized correctly in that the gameInfo member of type MorraInfo is not null as the object was instantiated
	//
	@Test
	void testClientGameInfoInit()
	{
		assertNotNull(c.gameInfo, "Incorrect result, the gameInfo member of the Client c, should not be null as it is instantiated and assigned when the Client is created");
	} // end testClientGameInfoInit()
	
	// testClientThreadAlive()
	//
	// Test if the Client instance, c, returns a boolean result of false if the Thread method isAlive() is used as the thread has not been started
	//
	@Test
	void testClientThreadAlive()
	{
		assertFalse(c.isAlive(), "Incorrect result, the Client c should return a boolean value of false when the Thread Class method isAlive() is called as thread has not be started");
	} // end testClientThreadAlive()
	
	// testClientPortNum():
	//
	// Test if the client, c, is initialized correctly with the passed port number via the parameterized constructor
	//
	@Test
	void testClientPortNum()
	{
		assertEquals(5555, c.portNum, "Incorrect port number stored inside Client instance, c");
	} // end testClientPortNum()
	
	// testGameStatus():
	//
	// Test if the specific gameInfo variable of the Client instance, c, is correctly initialized by asserting if the value held
	// is the one that is expected
	//
	@Test
	void testGameStatus() {
		assertEquals(0, c.gameInfo.gameStatus, "Game status should be 0 but is " + c.gameInfo.gameStatus);
	}

	// testClientID():
	//
	// Test if the specific gameInfo variable of the Client instance, c, is correctly initialized by asserting if the value held
	// is the one that is expected
	//
	@Test
	void testClientID() {
		assertEquals(1, c.gameInfo.clientID, "Client ID should be 1 but is " + c.gameInfo.clientID);
	}

	// testGuessesLeft():
	//
	// Test if the specific gameInfo variable of the Client instance, c, is correctly initialized by asserting if the value held
	// is the one that is expected
	//
	@Test
	void testGuessesLeft() {
		assertEquals(6, c.gameInfo.guessesLeft, "Guesses left should be 6 but is " + c.gameInfo.guessesLeft);
	} // end testGuessesLeft()

	// testGuessCorrect():
	//
	// Test if the specific gameInfo variable of the Client instance, c, is correctly initialized by asserting if the value held
	// is the one that is expected
	//
	@Test
	void testGuessCorrect() {
		assertEquals(false, c.gameInfo.guessCorrect, "Guess correct should be false but is " + c.gameInfo.guessCorrect);
	} // end testGuessCorrect()

	// testAllCorrect():
	//
	// Test if the specific gameInfo variable of the Client instance, c, is correctly initialized by asserting if the value held
	// is the one that is expected
	//
	@Test
	void testAllCorrect() {
		assertEquals(false, c.gameInfo.allCorrect, "All correct should be false but is " + c.gameInfo.allCorrect);
	} // end testAllCorrect()

	// testFoodFail():
	//
	// Test if the specific gameInfo variable of the Client instance, c, is correctly initialized by asserting if the value held
	// is the one that is expected
	//
	@Test
	void testFoodFail() {
		assertEquals(false, c.gameInfo.foodFail, "Food fail should be false but is " + c.gameInfo.foodFail);
	} // end testFoodFail()

	// testAnimalFail():
	//
	// Test if the specific gameInfo variable of the Client instance, c, is correctly initialized by asserting if the value held
	// is the one that is expected
	//
	@Test
	void testAnimalFail() {
		assertEquals(false, c.gameInfo.animalFail, "Animal fail should be false but is " + c.gameInfo.animalFail);
	} // end testAnimalFail()

	// testStateFail():
	//
	// Test if the specific gameInfo variable of the Client instance, c, is correctly initialized by asserting if the value held
	// is the one that is expected
	//
	@Test
	void testStateFail() {
		assertEquals(false, c.gameInfo.stateFail, "State fail should be false but is " + c.gameInfo.stateFail);
	} // end testStateFail()

	// testClientProgressGuess():
	//
	// Test if the specific gameInfo variable of the Client instance, c, is correctly initialized by asserting if the value held
	// is the one that is expected
	//
	@Test
	void testClientProgressGuess() {
		assertEquals("", c.gameInfo.clientProgressGuess,
				"Client progress guess should be blank but is " + c.gameInfo.clientProgressGuess);
	} // end testClientProgressGuess()

	// testPlayerGuess():
	//
	// Test if the specific gameInfo variable of the Client instance, c, is correctly initialized by asserting if the value held
	// is the one that is expected
	//
	@Test
	void testPlayerGuess() {
		assertEquals("", c.gameInfo.playerGuess, "Player guess should be blank but is " + c.gameInfo.playerGuess);
	} // end testPlayerGuess()

}