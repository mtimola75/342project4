import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Names: John Cervantes, Mike Lazzar, Marc Timola, Krzysztof Para
//
// Description of File:
//
// The GuessTest.java file for ServerWordGuessSpring2020 contains all of the test cases that are used to test the
// Server class. The test cases include testing all member variables and the evalPlayerGuess() method.
// The client is instantiated with the port value of 5555 and the IP address of "127.0.0.1" corresponding to local host using the parameterized
// constructor of Server and TheServer.
//
// GuessTest: Includes all the test cases relevant to the Server
class GuessTest {

	Server server;
	
	// setUpCode():
	//
	// The setUpCode() method instantiates the Server instance and assigns it to the server variable of type Server
	//
	@BeforeEach
	void setUpCode()
	{
		server = new Server(server-> {}, 5555);
	} // end setUpCode()
	
	// testServerInit():
	//
	// Test if the Server instance is correctly initialized when the instance is created through the use of the parameterized constructor
	//
	@Test
	void testServerInit()
	{
		assertEquals("Server", server.getClass().getName(), "Incorrect initialization of the Server instance, server, the returned name should be Server");
	} // end testServerInit()
	
	// testServerWordGuessInfoInit()
	//
	// Test if the WordGuessInfo member, gameInfo, in the Server instance is correctly instantiated with the correct class type, MorraInfo
	//
	@Test
	void testServerWordGuessInfoInit()
	{
		assertEquals("WordGuessInfo", server.gameInfo.getClass().getName(), "Incorrect result for the WordGuessInfo variable, gameInfo, inside of the Server instance, server");
	} // end testServerWordGuessInfoInit()
	
	// testServerTheServerInit():
	//
	// Test if the TheServer member, server in the Server instance is correctly initialized with the correct class type, TheServer
	//
	@Test
	void testServerTheServerInit()
	{
		assertEquals("Server$TheServer", server.server.getClass().getName(), "Incorrect result: The returned result should be TheServer which is in Server");
	} // end testServerTheServerInit()
	
	// testServerInitCount():
	//
	// Test if the Server instance, server, has the correct initial value of count when the server is instantiated
	//
	@Test
	void testServerInitCount()
	{
		assertEquals(1, server.count, "Incorrect result: The initialized value of count should be 1 when Server is instantiated");
	} // end testServerInitCount()
	
	// testServerInitClientsSize():
	@Test
	void testServerInitClientsSize()
	{
		assertEquals(0, server.clients.size(), "Incorrect result: The initialized ArrayList, clients, should have a size of 0 when created");
	} // end testServerInitClientsSize()
	
	// testServerInitServerField():
	//
	// Test if the Server instance, server's server member is not null. The member is initialized in the parameterized constructor
	//
	@Test
	void testServerInitServerField()
	{
		assertNotNull(server.server, "Incorrect result: As the Server member of type TheServer, server, should be initialized in the parameterized constructor it should not be null when Server instance is created");
	} // end testServerInitServerField()
	
	// testServerInitPortNum():
	//
	// Test if the Server instance, server, has the correct value for portNum, when it is initialized in the parameterized constructor
	//
	@Test
	void testServerInitPortNum()
	{
		assertEquals(5555, server.portNum, "Incorrect result: The value of portNum should be 5555 as it was passed into the parameterized constructor");
	} // end testServerInitPortNum()
	
	// testServerEvalPlayerGuessCorrect():
	//
	// The testServerEvalPlayerGuessCorrect() method tests if the evalPlayerGuess() method of the server class correctly evaluates the entered guess
	// in this case the guess is correct and a result of 1 is returned. The guessWord is updated accordingly
	//
	@Test
	void testServerEvalPlayerGuessCorrect()
	{
		server.guessWord = "TEST";
		server.gameInfo.clientProgressGuess = "____";
		server.gameInfo.playerGuess = "E";
		
		assertEquals(1, server.evalPlayerGuess(), "Incorrect result, as the player guess entered exists in the guessWord the result should be 1");
		assertEquals("T#ST", server.guessWord, "Incorrect result, the E in TEST should be marked with a # character");
	} // end testServerEvalPlayerGuessCorrect()
	
	// testServerEvalPlayerGuessIncorrect():
	//
	// The testServerEvalPlayerGuessIncorrect() method tests if the evalPlayerGuess() method of the server class correctly evaluates the entered guess
	// in this case the guess is incorrect and a result of 2 is returned. The guessWord is not updated
	//
	@Test
	void testServerEvalPlayerGuessIncorrect()
	{
		server.guessWord = "TEST";
		server.gameInfo.clientProgressGuess = "____";
		server.gameInfo.playerGuess = "X";
		
		assertEquals(2, server.evalPlayerGuess(), "Incorrect result, as the player guess entered does not exist in the guessWord the result should be 2");
		assertEquals("TEST", server.guessWord, "Incorrect result, the letters in TEST should not be marked with a # character");
	} // end testServerEvalPlayerGuessIncorrect()
	
	// testServerEvalPlayerGuessMultipleCorrect():
	//
	// The testServerEvalPlayerGuessMultipleCorrect() method tests if the evalPlayerGuess() method of the server class correctly evaluates the entered guess
	// in this case the guess is correct and updates multiple characters and a result of 1 is returned. The guessWord is updated accordingly
	//
	@Test
	void testServerEvalPlayerGuessMultipleCorrect()
	{
		server.guessWord = "TEST";
		server.gameInfo.clientProgressGuess = "____";
		server.gameInfo.playerGuess = "T";
		
		assertEquals(1, server.evalPlayerGuess(), "Incorrect result, as the player guess entered exists in the guessWord the result should be 1");
		assertEquals("#ES#", server.guessWord, "Incorrect result, the T  characters in TEST should be marked with a # character");
	} // end testServerEvalPlayerGuessMultipleCorrect()
	
}
