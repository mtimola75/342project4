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

// Name: John Cervantes
// UIN: 659125820
// NetID: jcerva23
//
// Description of File:
//
// The Server.java file of MorraProject3Client includes the information for the Server Class that allows for functionality for
// the Game of Morra game.
//

public class Server{

	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	
	TheServer server;
	private Consumer<Serializable> callback;
	int portNum;
	boolean startGame = false;
	WordGuessInfo gameInfo = new WordGuessInfo();
	int replacement = 1;
	String guessWord;
	boolean clientOneOnServer = false;
	boolean clientTwoOnServer = false;
	
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
		return clients.size();
	} // end getClientCount
	
	public class TheServer extends Thread
	{
		public void run() 
		{
			try(ServerSocket mysocket = new ServerSocket(portNum);){
				callback.accept("Server Connection success, waiting on clients to join!");	
			    while(true) {
			    	// Check if clientOneOnServer is false, if so, update replacement
			    	if (clientOneOnServer == false)
					{
						replacement = 1;
					}
			    	
					ClientThread c = new ClientThread(mysocket.accept(), count);
					count++; // Increment count
					clients.add(c); // Add c to arrayList
					
					// Check if clientOneOnServer is false, if so, set c name and update other fields
					if (clientOneOnServer == false)
					{
						c.setName("clientOne");
						clientOneOnServer = true;
	//					gameInfo.reconnectNum = 1;
						replacement = 1;
					}
					else if (clientTwoOnServer == false) // Else if clientTwoOnServer is false, set c name and update other fields
					{
						c.setName("clientTwo");
						clientTwoOnServer = true;
	//					gameInfo.reconnectNum = 2;
						replacement = 2;
					}
					
					callback.accept("client has connected to server: " + "client #" + replacement);
					c.start();
					
			    }
			}//end of try
			catch(Exception e) {
				callback.accept("Error: Server socket did not launch");
			}
		}//end of while
	}
	
//		 evalPlayerGuess():
//		
//		 The evalPlayerGuess() method evaluates the play and guess of each player and returns an integer result based on the evaluation.
//		 If both players guess correctly on the opposing play, no points are awarded. Else if both players guess incorrectly, no points are awarded.
//		 If one player guesses correctly and the other player does not, the point is awarded accordingly. (1: p1 wins, 2: p2 wins, 3: no one wins)
//		
		public int evalPlayerGuess() 
		{
			// 1: player correct
			// 2: player incorrect
			// 3: No one wins
			boolean correctGuessTemp = false;
			
			while(true)
			{
				if (this.guessWord.contains(gameInfo.playerGuess))
				{
//					System.out.println("GUESS INDEX: " + this.guessWord.indexOf(gameInfo.playerGuess));
//					gameInfo.clientProgressGuess.charAt(this.guessWord.indexOf(gameInfo.playerGuess))
					StringBuilder editString = new StringBuilder(gameInfo.clientProgressGuess);
					StringBuilder editGuessString = new StringBuilder(this.guessWord);
					
					char[] playerGuessArr = gameInfo.playerGuess.toCharArray();
					
					editString.setCharAt(this.guessWord.indexOf(gameInfo.playerGuess), playerGuessArr[0]);
					editGuessString.setCharAt(this.guessWord.indexOf(gameInfo.playerGuess), '#');
					
					gameInfo.clientProgressGuess = editString.toString();
					this.guessWord = editGuessString.toString();
//					System.out.println("Edited string: " + gameInfo.clientProgressGuess);
					correctGuessTemp = true;
					for (int i = 0; i < editGuessString.length(); i++)
					{
						if (editGuessString.charAt(i) == '#')
						{
							gameInfo.allCorrect = true;
						}
						else
						{
							gameInfo.allCorrect = false;
							break;
						}
					}
				}
				else
				{
//					gameInfo.guessCorrect = false;
					break;
				}
			}
			
			
			if (correctGuessTemp == true)
			{
				if (gameInfo.allCorrect && gameInfo.message.contains("Food"))
				{
					gameInfo.foodAllCorrect = true;
				}
				else if (gameInfo.allCorrect && gameInfo.message.contains("Animal"))
				{
					gameInfo.animalAllCorrect = true;
				}
				else if (gameInfo.allCorrect && gameInfo.message.contains("State"))
				{
					gameInfo.stateAllCorrect = true;
				}
				
				return 1;
			}
			else
			{
				gameInfo.guessesLeft--;
				if (gameInfo.foodList.size() == 0 && gameInfo.guessesLeft == 0)
				{
					gameInfo.foodFail = true;
				}
				else if (gameInfo.animalList.size() == 0 && gameInfo.guessesLeft == 0)
				{
					gameInfo.animalFail = true;
				}
				else if (gameInfo.stateList.size() == 0 && gameInfo.guessesLeft == 0)
				{
					gameInfo.stateFail = true;
				}
				return 2;
			}
			
		} // end evalPlayerGuess()
//	

		class ClientThread extends Thread{
			
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
			
			//			
			//	updateClients(...):
			//			
			//	The updateClients(...) method is responsible in updating the clients in the clients arrayList with the
			//	gameInfo object of type WordGuessInfo
			//
			public void updateClients(WordGuessInfo data) {
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
						t.out.writeObject(data);
					}
					catch(Exception e) {}
				}
			} // end updateClients(...)
			
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
				    	
				    	if (gameInfo.gameStatus == 4)
				    	{
				    		gameInfo.clientID = clients.size();
				    		gameInfo.gameStatus = 0;
//				    		clients.get(this.count-1).out.writeObject(gameInfo);
				    	}
				    	
				    	if (gameInfo.gameStatus == 0)
				    	{
				    		gameInfo.foodList.clear();
				    		gameInfo.foodList.add("PIZZA");
				    		gameInfo.foodList.add("NACHOS");
				    		gameInfo.foodList.add("TACOS");
				    		
				    		gameInfo.animalList.clear();
				    		gameInfo.animalList.add("PIG");
				    		gameInfo.animalList.add("DOLPHIN");
				    		gameInfo.animalList.add("JAGUAR");
				    		
				    		gameInfo.stateList.clear();
				    		gameInfo.stateList.add("ILLINOIS");
				    		gameInfo.stateList.add("TEXAS");
				    		gameInfo.stateList.add("CALIFORNIA");
				    		
				    		gameInfo.gameStatus = 1;
				    		callback.accept("Filling array list with items...");
				    		clients.get(gameInfo.clientID-1).out.writeObject(gameInfo);
//				    		this.updateClients(gameInfo);
				    	}
				    	else if (gameInfo.gameStatus == 1) // Else if the gameInfo field gameStatus is equal to 1, that is, the client is in the category screen enter the block of code
				    	{
				    		// Based on the gameInfo field, message, check what category the specific client has selected using the method contains(...)
				    		if (gameInfo.message.contains("Food")) // Check if the gameInfo field, message, contains "Food" and if so enter block
				    		{
				    			callback.accept("Client " + this.count + " chose Food as category\n" + "The word to guess is: " + gameInfo.foodList.get(0));
//				    			guessWord = gameInfo.foodList.get(0);
				    			guessThreadWord = gameInfo.foodList.get(0);
				    			
				    			// Iterate through the length of the first element in the gameInfo stateList ArrayList using the length() method and add a "_" for each iteration to the gameInfo member clientProgressGuess
				    			for (int i = 1; i <= gameInfo.foodList.get(0).length(); i++)
				    			{
				    				gameInfo.clientProgressGuess += "_";
				    			}
				    			
				    			gameInfo.foodList.remove(0); // Remove the first item in the gameInfo member foodList using the remove() method at the 0-th index
				    			gameInfo.gameStatus = 2; // Set gameInfo field gameStatus to 2 
//				    			clients.get(this.count-1).out.writeObject(gameInfo);
				    			clients.get(gameInfo.clientID-1).out.writeObject(gameInfo); // Using the Output stream, out, write the gameInfo object to the specific client that sent the guess using clients ArrayList and the gameInfo field clientID
//				    			this.out.writeObject(gameInfo);
//				    			this.updateClients(gameInfo);
				    		}
				    		
				    		else if (gameInfo.message.contains("Animals")) // Else if the gameInfo field, message, contains "Animals" and if so enter block
				    		{
				    			callback.accept("Client " + this.count + " chose Animals as category\n" + "The word to guess is: " + gameInfo.animalList.get(0));
//				    			guessWord = gameInfo.animalList.get(0);
				    			guessThreadWord = gameInfo.animalList.get(0); // Get the first item in the gameInfo member stateList using the remove() method at the 0-th index
				    			
				    			// Iterate through the length of the first element in the gameInfo stateList ArrayList using the length() method and add a "_" for each iteration to the gameInfo member clientProgressGuess
				    			for (int i = 1; i <= gameInfo.animalList.get(0).length(); i++)
				    			{
				    				gameInfo.clientProgressGuess += "_";
				    			}
				    			
				    			gameInfo.animalList.remove(0); // Remove the first item in the gameInfo member animalList using the remove() method at the 0-th index
				    			gameInfo.gameStatus = 2; // Set gameInfo field gameStatus to 2 
//				    			clients.get(this.count-1).out.writeObject(gameInfo);
				    			clients.get(gameInfo.clientID-1).out.writeObject(gameInfo);
//				    			this.out.writeObject(gameInfo);
//				    			this.updateClients(gameInfo);
				    		}
				    		
				     		else if (gameInfo.message.contains("States")) // Else if the gameInfo field, message, contains "States" and if so enter block
				    		{
				    			callback.accept("Client " + this.count + " chose states as category\n" + "The word to guess is: " + gameInfo.stateList.get(0));
//				    			guessWord = gameInfo.stateList.get(0);
				    			guessThreadWord = gameInfo.stateList.get(0);
				    			
				    			// Iterate through the length of the first element in the gameInfo stateList ArrayList using the length() method and add a "_" for each iteration to the gameInfo member clientProgressGuess
				    			for (int i = 1; i <= gameInfo.stateList.get(0).length(); i++)
				    			{
				    				gameInfo.clientProgressGuess += "_"; // Append a "_" to the gameInfo clientProgressGuess member to indicate the empty spaces of the word that is being guessed
				    			} // end for(...)
				    			
				    			gameInfo.stateList.remove(0); // Remove the first item in the gameInfo member stateList using the remove() method at the 0-th index
				    			gameInfo.gameStatus = 2; // Set gameInfo field gameStatus to 2 
//				    			clients.get(this.count-1).out.writeObject(gameInfo);
				    			clients.get(gameInfo.clientID-1).out.writeObject(gameInfo); // Using the Output stream, out, write the gameInfo object to the specific client that sent the guess using clients ArrayList and the gameInfo field clientID
//				    			this.out.writeObject(gameInfo);
//				    			this.updateClients(gameInfo);
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
//				    		this.out.writeObject(gameInfo);
//				    		clients.get(this.count-1).out.writeObject(gameInfo);
//				    		this.updateClients(gameInfo);
				    	}
			    	}
				    catch(Exception e) {
//				    	
//				    	// If both players have left the server, set respective fields
//				    	if (Server.this.clients.size() == 1)
//				    	{
			    		callback.accept("Client #" + this.count + " has left the server"); // Accept a string indicating that the specific client has quit and left the server using callback.accept()
//				    		clientOneOnServer = false;
//				    		clientTwoOnServer = false;
//				    		gameInfo.reconnectNum = 1;
//				    		Server.this.replacement = 1;
//				    		gameInfo.p1Quit = true;
//				    		gameInfo.p2Quit = true;
//				    		callback.accept(gameInfo);
//				    	}
//				    	
//				    	// If the second client is leaving the server, set fields respectively
//				    	if (this.count == 2)
//				    	{
//				    		callback.accept("Player #2 has left the server");
//				    		clientTwoOnServer = false;
//				    		gameInfo.reconnectNum = 2;
//				    		gameInfo.p2Quit = true;
//				    		Server.this.replacement = 2;
//				    		callback.accept(gameInfo);
//				    	}
//				    	else if (this.count == 1)// Else the first client, clientOne left the server, set fields respectively
//				    	{
//				    		callback.accept("Player #1 has left the server");
//				    		clientOneOnServer = false;
//				    		gameInfo.reconnectNum = 1;
//				    		gameInfo.p1Quit = true;
//				    		Server.this.replacement = 1;
//				    		callback.accept(gameInfo);
//				    	}
//				    	
//				    	this.count--; // Decrement count
//
//				    	Server.this.clients.remove(this); // Remove the client out of the arrayList
//				    	
				    	break;
				    }
				}
				
			}//end of run

			
		}//end of client thread
}