import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;

// Names: John Cervantes, Mike Lazzar, Marc Timola, Krzysztof Para
//
// Description of File:
//
// The Server.java file of WordGuessServer includes the information for the Server Class that allows for functionality for
// the Word Guess game.
//

public class Server {
	
	// Variables for the Server maintenance and to control game flow
	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	int clientCount = 0;
	TheServer server;
	private Consumer<Serializable> callback;
	int portNum;
	boolean startGame = false;
	WordGuessInfo gameInfo = new WordGuessInfo();
	String guessWord;
	
	// Parameterized constructor with call only
	Server(Consumer<Serializable> call){
		
		callback = call;
		server = new TheServer();
		server.start();
	}
	
	// Parameterized constructor with call and portNum variable
	Server(Consumer<Serializable> call, int portNum){
	
		callback = call;
		this.portNum = portNum;
		server = new TheServer();
		server.start();
	}
	
	// getClientCount():
	//
	// Returns the client count of the server, return type of int
	//
	public int getClientCount()
	{
		return this.clientCount;
	} // end getClientCount
	
	public class TheServer extends Thread
	{
		// run():
		//
		// The run() method is responsible in connecting creating a server socket with the entered portNum and accept ClientThreads
		// to connect to the server
		//
		public void run() 
		{
			try(ServerSocket mysocket = new ServerSocket(portNum);){
				callback.accept("Server Connection success, waiting on clients to join!");	
			    
				while(true) 
				{	
					ClientThread c = new ClientThread(mysocket.accept(), count);
					callback.accept("Client has connected to server: " + "client #" + count);
					clientCount++; // Increment clientCount by 1
					count++; // Increment count by 1
					clients.add(c); // Add the ClientThread c to arrayList clients
					
					c.start(); // Call the start() method for the ClientThread c
			    } // end while(true)
				
			} //end try(...)
			catch(Exception e) {
				callback.accept("Error: Server socket did not launch");
			} // end catch(...)
			
		} // end run()
		
	} // end TheServer
	
	// evalPlayerGuess():
	//		
	// The evalPlayerGuess() method of return type int evaluates if the player guess is correct, that is, the guessWord contains the player's guess
	// and replaces the character with '#' to represent that the word was guessed correctly. Given the player's guess, the method
	// also accommodates repeated letters in the string and marks them given correct guess. If the player guess is not contained
	// in guessWord, the guessesLeft variable in gameInfo is decremented by 1 and a value of 2 is returned. The method
	// also updates end-game variables (allCorrect and fail variables) accordingly. Returned values: (1: correct, 2: incorrect)
	//
	public int evalPlayerGuess() 
	{
		boolean correctGuessTemp = false; // Declare and initialize boolean variable, correctGuessTemp to false to keep track if guess is correct
		
		// Check the special case where the client(player) enters the '#' character when a letter is guessed, if so enter block
		if (gameInfo.playerGuess.contains("#"))
		{
			gameInfo.guessesLeft--; // Decrement guessesLeft
			
			// Check if the following conditions are met to update variables indicating a category was failed due to an incorrect guess
			if (gameInfo.foodList.size() == 0 && gameInfo.guessesLeft == 0) // If foodList is empty (0 size) or guessesLeft is 0, enter block 
			{
				gameInfo.foodFail = true; // Set foodFail to true
			}
			else if (gameInfo.animalList.size() == 0 && gameInfo.guessesLeft == 0)// Else if animalList is empty (0 size) or guessesLeft is 0, enter block 
			{
				gameInfo.animalFail = true; // Set animalFail to true
			}
			else if (gameInfo.stateList.size() == 0 && gameInfo.guessesLeft == 0) // Else if stateList is empty (0 size) or guessesLeft is 0, enter block 
			{
				gameInfo.stateFail = true; // Set stateFail to true
			}
			
			return 2; // Return 2 as player guessed incorrectly
		}
		
		// Iterate until the a break statement is read in order to accommodate for a duplicate correct guess (example: TEST with T appearing twice)
		while(true)
		{
			// Check if the server's guessWord contains the gameInfo.playerGuess, that is the player's guess is correct if so enter block
			if (this.guessWord.contains(gameInfo.playerGuess))
			{
				StringBuilder editString = new StringBuilder(gameInfo.clientProgressGuess); // Declare and initialize instance of StringBuiler, editString, to edit the clientProgressGuess string
				StringBuilder editGuessString = new StringBuilder(this.guessWord); // Declare and initialize instance of StringBuilder, editGuessString, to edit the guessWord
				
				char[] playerGuessArr = gameInfo.playerGuess.toCharArray(); // Create array of characters, playerGuessArr, assign it to gameInfo.playerGuess converted to char array
				
				editString.setCharAt(this.guessWord.indexOf(gameInfo.playerGuess), playerGuessArr[0]); // Set the character at the position of the player guess to the 0-th value of playerGuessArr
				editGuessString.setCharAt(this.guessWord.indexOf(gameInfo.playerGuess), '#'); // Set the character at the position of the playerGuess to a '#' character
				
				gameInfo.clientProgressGuess = editString.toString(); // COnvert the editString to a string and assign to clientProgressGuess
				this.guessWord = editGuessString.toString(); // Convert editGuessString to a string and assign to guessWord
				correctGuessTemp = true; // Set correctGuessTemp to true
				
				// Iterate through the editGuessString length and check if all characters in the string are '#' (Marked as correct)
				for (int i = 0; i < editGuessString.length(); i++)
				{
					// Check if the editGuessString character at the i-th position is equal to '#' if so enter block
					if (editGuessString.charAt(i) == '#')
					{
						gameInfo.allCorrect = true; // Set allCorrect to true
					}
					else // Else the character is not equal to '#' enter block
					{
						gameInfo.allCorrect = false; // Set allCorrect to false
						break; // Break out of for loop
					}
				} // end for(...)
			}
			else // Else the character guess of the player is incorrect, enter block
			{
				break; // Break out of the while(true) loop
			}
		} // end while(true)
		
		// Check if correctGuessTemp is true, that is the player guessed right, if so enter block
		if (correctGuessTemp == true)
		{
			// Check if the gameInfo allCorrect field is true and the message field contains Food, if so enter block
			if (gameInfo.allCorrect && gameInfo.message.contains("Food")) 
			{
				gameInfo.foodAllCorrect = true; // Set foodAllCorrect to true
			}
			else if (gameInfo.allCorrect && gameInfo.message.contains("Animal")) // Else if the gameInfo allCorrect field is true and the message field contains Animal, if so enter block
			{
				gameInfo.animalAllCorrect = true; // Set animalAllCorrect to true
			}
			else if (gameInfo.allCorrect && gameInfo.message.contains("State")) // Else if the gameInfo allCorrect field is true and the message field contains State, if so enter block
			{
				gameInfo.stateAllCorrect = true; // Set stateAllCorrect to true
			}
			
			return 1; // Return the value 1 as player guess was correct
		}
		else // Else the correctGuessTemp is false, that is the player guessed wrong, enter block
		{
			gameInfo.guessesLeft--; // Decrement guessesLeft by 1
			
			// Check if the foodList ArrayList is empty and the guessesLeft field is 0, if so enter block
			if (gameInfo.foodList.size() == 0 && gameInfo.guessesLeft == 0)
			{
				gameInfo.foodFail = true; // Set foodFail to true
			}
			else if (gameInfo.animalList.size() == 0 && gameInfo.guessesLeft == 0) // Else if the animalList ArrayList is empty and the guessesLeft field is 0, if so enter block
			{
				gameInfo.animalFail = true; // Set animalFail to true
			}
			else if (gameInfo.stateList.size() == 0 && gameInfo.guessesLeft == 0) // Else if the stateList ArrayList is empty and the guessesLeft field is 0, if so enter block
			{
				gameInfo.stateFail = true; // Set stateFail to true
			}
			
			return 2; // Return 2 as player guess was wrong
		}
		
	} // end evalPlayerGuess()

	// ClientThread: Nested class extending Thread to independently run a separate thread on the server connection to allow client games to run independently of each other
	class ClientThread extends Thread
	{
		
		// Variables that are needed for the ClientThread object to function and input and output information
		Socket connection;
		int count;
		ObjectInputStream in;
		ObjectOutputStream out;
		String guessThreadWord;
		// Parameterized Constructor:
		// Set this.connection to the passed socket and set the count equal to the passed count
		ClientThread(Socket s, int count){
			this.connection = s;
			this.count = count;	
		}
			
		// run():
		//
		// The run() method is responsible in running the ClientThread object by opening the input and output stream
		// and checking if it is the case that a client is reconnecting and accommodating for it. Also it accommodates for all the gameStatus values
		// that are possible in the game and updates the gameInfo objects accordingly to send back to clients
		//
		public void run()
		{		
			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);	
			} // end try
			catch(Exception e) {
				callback.accept("Streams not open");
			} // end catch(...)

			while(true) 
			{
			    try {
			    	gameInfo = (WordGuessInfo)in.readObject(); // Read in gameInfo object from clients
			    	guessWord = guessThreadWord;
			    	
			    	// Check if the WordGuessInfo object, gameInfo, passed by the client has gameStatus of 4, if so assign the gameInfo clientID and gameStatus to 0
			    	if (gameInfo.gameStatus == 4)
			    	{
			    		gameInfo.clientID = clients.size(); // Set the gameInfo clientID field to the size of the clients ArrayList
			    		gameInfo.gameStatus = 0; // Set the gameInfo gameStatus to 0
			    	}
			    	
			    	// Check if the WordGuessInfo object, gameInfo, is 0, that is the client barely entered the server if so enter block
			    	if (gameInfo.gameStatus == 0)
			    	{
			    		gameInfo.foodList.clear(); // Clear foodList ArrayList
			    		// Add the following items to the ArrayList using add(...)
			    		gameInfo.foodList.add("PIZZA");
			    		gameInfo.foodList.add("NACHOS");
			    		gameInfo.foodList.add("TACOS");
			    		
			    		gameInfo.animalList.clear(); // Clear animalList ArrayList
			    		// Add the following items to the ArrayList using add(...)
			    		gameInfo.animalList.add("PIG");
			    		gameInfo.animalList.add("DOLPHIN");
			    		gameInfo.animalList.add("JAGUAR");
			    		
			    		gameInfo.stateList.clear(); // Clear stateList ArrayList
			    		// Add the following items to the ArrayList using add(...)
			    		gameInfo.stateList.add("ILLINOIS");
			    		gameInfo.stateList.add("TEXAS");
			    		gameInfo.stateList.add("CALIFORNIA");
			    		
			    		gameInfo.gameStatus = 1; // Set gameStatus of gameInfo to a value of 1
			    		callback.accept("Filling array list with items..."); // Accept the following string to the server
			    		clients.get(gameInfo.clientID-1).out.writeObject(gameInfo); // Write the WordGuessInfo object, gameInfo, to the specific clientID held in the gameInfo
			    	}
			    	else if (gameInfo.gameStatus == 1) // Else if the gameInfo field gameStatus is equal to 1, that is, the client is in the category screen enter the block of code
			    	{
			    		// Based on the gameInfo field, message, check what category the specific client has selected using the method contains(...)
			    		if (gameInfo.message.contains("Food")) // Check if the gameInfo field, message, contains "Food" and if so enter block
			    		{
			    			callback.accept("Client " + this.count + " chose Food as category\n" + "The word to guess is: " + gameInfo.foodList.get(0));
			    			guessThreadWord = gameInfo.foodList.get(0); // Get the first item in the gameInfo member foodList using the get() method at the 0-th index
			    			
			    			// Iterate through the length of the first element in the gameInfo foodList ArrayList using the length() method and add a "_" for each iteration to the gameInfo member clientProgressGuess
			    			for (int i = 1; i <= gameInfo.foodList.get(0).length(); i++)
			    			{
			    				gameInfo.clientProgressGuess += "_";
			    			}
			    			
			    			gameInfo.foodList.remove(0); // Remove the first item in the gameInfo member foodList using the remove() method at the 0-th index
			    			gameInfo.gameStatus = 2; // Set gameInfo field gameStatus to 2 
			    			clients.get(gameInfo.clientID-1).out.writeObject(gameInfo); // Using the Output stream, out, write the gameInfo object to the specific client that sent the guess using clients ArrayList and the gameInfo field clientID
			    		}
			    		
			    		else if (gameInfo.message.contains("Animals")) // Else if the gameInfo field, message, contains "Animals" and if so enter block
			    		{
			    			callback.accept("Client " + this.count + " chose Animals as category\n" + "The word to guess is: " + gameInfo.animalList.get(0));
			    			guessThreadWord = gameInfo.animalList.get(0); // Get the first item in the gameInfo member animalList using the get() method at the 0-th index
			    			
			    			// Iterate through the length of the first element in the gameInfo stateList ArrayList using the length() method and add a "_" for each iteration to the gameInfo member clientProgressGuess
			    			for (int i = 1; i <= gameInfo.animalList.get(0).length(); i++)
			    			{
			    				gameInfo.clientProgressGuess += "_";
			    			}
			    			
			    			gameInfo.animalList.remove(0); // Remove the first item in the gameInfo member animalList using the remove() method at the 0-th index
			    			gameInfo.gameStatus = 2; // Set gameInfo field gameStatus to 2 
			    			clients.get(gameInfo.clientID-1).out.writeObject(gameInfo);
			    		}
			    		
			     		else if (gameInfo.message.contains("States")) // Else if the gameInfo field, message, contains "States" and if so enter block
			    		{
			    			callback.accept("Client " + this.count + " chose states as category\n" + "The word to guess is: " + gameInfo.stateList.get(0));
			    			guessThreadWord = gameInfo.stateList.get(0); // Get the first item in the gameInfo member stateList using the get() method at the 0-th index
			    			
			    			// Iterate through the length of the first element in the gameInfo stateList ArrayList using the length() method and add a "_" for each iteration to the gameInfo member clientProgressGuess
			    			for (int i = 1; i <= gameInfo.stateList.get(0).length(); i++)
			    			{
			    				gameInfo.clientProgressGuess += "_"; // Append a "_" to the gameInfo clientProgressGuess member to indicate the empty spaces of the word that is being guessed
			    			} // end for(...)
			    			
			    			gameInfo.stateList.remove(0); // Remove the first item in the gameInfo member stateList using the remove() method at the 0-th index
			    			gameInfo.gameStatus = 2; // Set gameInfo field gameStatus to 2 
			    			clients.get(gameInfo.clientID-1).out.writeObject(gameInfo); // Using the Output stream, out, write the gameInfo object to the specific client that sent the guess using clients ArrayList and the gameInfo field clientID
			    		}
			    		
			    	}
			    	else if (gameInfo.gameStatus == 2) // Else if the gameInfo gameStatus is 2, that is, the player has sent a guess enter block of code
			    	{
			    		callback.accept("Client " + this.count + " has guessed: " + gameInfo.playerGuess); // Accept a string indicating the specific client that sent a guess and display the guess to the Server GUI
			    		// Call the evalPlayerGuess() method and assign the gameInfo field guessCorrect according to the returned result
			    		if (evalPlayerGuess() == 1) // If the evalPlayerGuess() method returned 1, set the gameInfo field guessCorrect to true
			    		{
			    			callback.accept("Client " + this.count + " has sent an correct guess!"); // Accept a string indicating that the specific client has guessed correctly, using callback.accept()
			    			gameInfo.guessCorrect = true; // As the guess was correct, set gameInfo field guessCorrect to true
			    		}
			    		else // Else the evalPlayerGuess() returned 0, set the gameInfo field guessCorrect to false
			    		{
			    			callback.accept("Client " + this.count + " has sent an incorrect guess!"); // Accept a string indicating that the specific client has guessed incorrectly, using callback.accept()
			    			gameInfo.guessCorrect = false; // As the guess was incorrect, set gameInfo field guessCorrect to false
			    		}
			    		
			    		guessThreadWord = guessWord; // Set guessThreadWord to value of guessWord to save the progress of the word being guessed for the specific client
			    		callback.accept("Client " + this.count + " guess progress (# = Guessed): " + guessThreadWord); // Accept a string indicating the specific client's guess progress, outputting the guessThreadWord
			    		clients.get(gameInfo.clientID-1).out.writeObject(gameInfo); // Using the Output stream, out, write the gameInfo object to the specific client that sent the guess using clients ArrayList and the gameInfo field clientID
			    	}
		    	}
			    catch(Exception e) {
		    		callback.accept("Client #" + this.count + " has left the server"); // Accept a string indicating that the specific client has quit and left the server using callback.accept()
			    	Server.this.clientCount--; // Decrement the Server variable clientCount by 1
			    	break; // Break out of the while loop
			    	
			    } // end catch(...)
			    
			} // end while(true)
			
		} // end run()
		
	} // end ClientThread
	
} // end Server