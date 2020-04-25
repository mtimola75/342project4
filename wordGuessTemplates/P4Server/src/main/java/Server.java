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
//	MorraInfo gameInfo = new MorraInfo();
	int replacement = 1;
	
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
		// evalPlayerGuess():
		//
		// The evalPlayerGuess() method evaluates the play and guess of each player and returns an integer result based on the evaluation.
		// If both players guess correctly on the opposing play, no points are awarded. Else if both players guess incorrectly, no points are awarded.
		// If one player guesses correctly and the other player does not, the point is awarded accordingly. (1: p1 wins, 2: p2 wins, 3: no one wins)
		//
//		public int evalPlayerGuess() 
//		{
//			// 1: player 1 wins
//			// 2: player 2 wins
//			// 3: No one wins
////			if ((Integer.parseInt(gameInfo.p2Plays) == gameInfo.p1Guess ) && (Integer.parseInt(gameInfo.p1Plays) == gameInfo.p2Guess)) // If the player one and two guessed correctly, return 3 as a tie
////			{
////				return 3; // Return 3 as no one won round
////			}
////			else if ((Integer.parseInt(gameInfo.p2Plays) == gameInfo.p1Guess ) && (Integer.parseInt(gameInfo.p1Plays) != gameInfo.p2Guess)) // Else if player one guessed correctly and wins round
////			{
////				return 1; // Return 1 as player one won round
////			}
////			else if ((Integer.parseInt(gameInfo.p1Plays) == gameInfo.p2Guess ) && (Integer.parseInt(gameInfo.p2Plays) != gameInfo.p1Guess)) // Else if the player two guessed correctly and wins round
////			{
////				return 2; // Return 2 as player two won round
////			}
////			else // No one guessed correctly
////			{
////				return 3; // Return 3 as no one won round
////			}
////			
//			
//		} // end evalPlayerGuess()
//	

		class ClientThread extends Thread{
			
			// Variables that are needed for the ClientThread object to function and input and output information
			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			
			// Parameterized Constructor:
			// Set this.connection to the passed socket and set the count equal to the passed count
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
			}
			
			// updateClients(...):
			//
			// The updateClients(...) method is responsible in updating the clients in the clients arrayList with the
			// gameInfo object of type MorraInfo
			//
//			public void updateClients(MorraInfo data) {
//				for(int i = 0; i < clients.size(); i++) {
//					ClientThread t = clients.get(i);
//					try {
//					 t.out.writeObject(data);
//					}
//					catch(Exception e) {}
//				}
//			} // end updateClients(...)
			
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
//					
//					// Check if the count is greater than 3, if so enter block
//					if (count >= 3)
//					{
//						// Update the gameInfo object to a new message, new gameStatus, and overwrite the count stored to the value of reconnectNum
//						gameInfo.message = "Reconnected";
//						this.count = gameInfo.reconnectNum;
//						gameInfo.gameStatus = 1;
//						
//						//Condition to check if the clients arrayList is of size 2, if so enter block
//						if (clients.size() == 2)
//						{
//							// Output the gameInfo object to the two clients on the server using writeObject()
//							clients.get(1).out.writeObject(gameInfo);
//							clients.get(0).out.writeObject(gameInfo);
//						}
//						else if (clients.size() == 1) // Else check if the clients arrayList is of size 1, if so enter block
//						{
//							clients.get(0).out.writeObject(gameInfo); // Send the gameInfo object to the only client on the server using writeObject()
//						}
//						
//					}
				} // end try
				catch(Exception e) {
					callback.accept("Streams not open");
				}
//				
//				
//				while(true) 
//				{
//				    try {
//				    	gameInfo = (MorraInfo)in.readObject(); // Read in gameInfo object from clients
//				    	
//				    	gameInfo.amountPlayers = clients.size(); // Update amountPlayers field
//				    	
//				    	if (gameInfo.p1Quit || gameInfo.p2Quit) // If either players have quit, update fields and send to both clients
//				    	{
//				    		gameInfo.message = "Waiting for a total of 2 clients to join server (Current: 1)...";
//				    		this.updateClients(gameInfo);
//				    	}
//				    	
//				    	// Check if gameStatus is 1, enter block
//				    	if (gameInfo.gameStatus == 1)
//				    	{
//				    		if ((clients.size() == 1)) // If client size is 1, update message
//					    	{
//				    			callback.accept("Waiting for a total of 2 clients to join server (Current: 1)...");
//					    		gameInfo.message = "Waiting for a total of 2 clients to join server (Current: 1)...";
//					    	}
//					    	else if ((clients.size() == 2)) // Else if client size is 2, update message and update gameStatus
//					    	{
//				    			callback.accept("Game is ready to begin... game is starting");
//					    		gameInfo.message = "Game is ready to begin... game is starting";
//					    		gameInfo.gameStatus = 2;
//					    	}
//					    	else // Else client size is not 2 or 1, update respectively
//					    	{
//					    		gameInfo.message = "Game is ready to begin... game is starting";
//					    		gameInfo.gameStatus = 2;
//					    	}
//				    		
//				    		this.updateClients(gameInfo);
//				    	}
//				    	else if (gameInfo.gameStatus == 2) // Else if gameStatus is 2, enter block
//			    		{
//				    		// Check if both players are ready by checking boolean values stored inside of gameInfo MorraInfo object
//				    		if (gameInfo.p1Ready && gameInfo.p2Ready)
//				    		{
//				    			// Check if the evalPlayerGuess() function returns 1 based on both player plays and guesses, if so, player one won
//				    			if (evalPlayerGuess() == 1)
//				    			{
//				    				gameInfo.p1Points++; // Increment p1Points inside gameInfo to reflect that player one won
//				    			}
//				    			else if (evalPlayerGuess() == 2) // Else if the evalPlayerGuess() function returns 2 based on both player plays and guesses, player two won
//				    			{
//				    				gameInfo.p2Points++; // Increment p2Points inside gameInfo to reflect that player two won
//				    			}
//				    			
//				    			String gameResult;
//				    			
//				    			// Check if any player has not won, that is p1Points or p2Points < 2
//				    			if ((gameInfo.p1Points < 2) && (gameInfo.p2Points < 2))
//				    			{
//				    				if (evalPlayerGuess() == 2)
//					    			{
//					    				gameResult = "Player Two wins round!";
//					    			}
//					    			else if (evalPlayerGuess() == 1)
//					    			{
//					    				gameResult = "Player One wins round!";
//					    			}
//					    			else
//					    			{
//					    				gameResult = "No one won round!";
//					    			}
//				    			}
//				    			else // Else a player has won, update gameResult
//				    			{
//				    				if (gameInfo.p1Points >= 2)
//				    				{
//				    					gameResult = "Player One wins the game!";
//				    				}
//				    				else
//				    				{
//				    					gameResult = "Player Two wins the game!";
//				    				}
//				    			}
//				    			
//				    			gameInfo.message = "Player Two Played: " + gameInfo.p2Plays + "\nPlayer Two Guess: " + gameInfo.p2Guess + "\n" + gameResult;
//				    			clients.get(0).out.writeObject(gameInfo);
//				    			gameInfo.message = "Player One Played: " + gameInfo.p1Plays + "\nPlayer One Guess: " + gameInfo.p1Guess + "\n" + gameResult;
//			    				clients.get(1).out.writeObject(gameInfo);
//			    				callback.accept("Player One Played: " + gameInfo.p1Plays + "\nPlayer One Guess: " + gameInfo.p1Guess + "\n" + "Player Two Played: " + gameInfo.p2Plays + "\nPlayer Two Guess: " + gameInfo.p2Guess + "\n" + gameResult + "\nPlayer One Points: " + gameInfo.p1Points + "\nPlayer Two Points: " + gameInfo.p2Points);
//
//			    				continue;
//				    		}
//				    		else // Else both players are not ready, update info respectively
//				    		{
//				    			if (gameInfo.p1Ready) // If p1 is ready, enter block
//				    			{
//				    				gameInfo.message = "Player 1 is ready... waiting on Player 2";
//				    				callback.accept("Player 1 is ready... waiting on Player 2");
//				    			}
//				    			else if (gameInfo.p2Ready) // Else if p2 is ready enter block
//				    			{
//				    				gameInfo.message = "Player 2 is ready... waiting on Player 1";
//				    				callback.accept("Player 2 is ready... waiting on Player 1");
//				    			}
//				    		}
//				    		this.updateClients(gameInfo);
//			    		}
//				    	else if (gameInfo.gameStatus == 3) // Else if gameStatus is 3, enter block
//				    	{
//				    		gameInfo.gameStatus = 2;
//				    		gameInfo.message = " ";
//				    		this.updateClients(gameInfo);
//				    	}
//				    	else if (gameInfo.gameStatus == 4) // Else if gameStatus is 4, enter block
//				    	{
//				    		callback.accept("Client has transitioned to Continue screen");
//				    		
//				    		// Check if both p1 and p2 are continuing if so, update fields and send gameInfo to both clients
//				    		if (gameInfo.p1Continue && gameInfo.p2Continue)
//				    		{
//				    			callback.accept("Both clients have chosen to continue");
//				    			gameInfo.message = "New game is starting...";
//				    			this.updateClients(gameInfo);
//				    		}
//				    		else if (gameInfo.p1Quit)  // Else if p1Quit is true, update fields and send
//				    		{
//				    			callback.accept("Player 1 has left the game...\nWaiting for another client");
//				    			gameInfo.message = "Player 1 has left the game...\nWaiting for another client";
//				    			clients.get(0).out.writeObject(gameInfo);
//				    		}
//				    		else if (gameInfo.p2Quit)  // Else if p2Quit is true, update fields and send
//				    		{
//				    			callback.accept("Player 2 has left the game...\nWaiting for another client");
//				    			gameInfo.message = "Player 2 has left the game...\nWaiting for another client";
//				    			clients.get(0).out.writeObject(gameInfo);
//				    		}
//				    		else if (gameInfo.p1Continue) // Else if p1Continue is true, update fields and send
//				    		{
//				    			callback.accept("Player 1 wants to play again!");
//				    			gameInfo.message = "Player 1 wants to play again... awaiting response";
//				    			clients.get(1).out.writeObject(gameInfo);
//				    		}
//				    		else if (gameInfo.p2Continue) // Else if p2Continue is true, update fields and send
//				    		{
//				    			callback.accept("Player 2 wants to play again!");
//				    			gameInfo.message = "Player 2 wants to play again... awaiting response";
//				    			clients.get(0).out.writeObject(gameInfo);
//				    		}
//				    		else // Else no field is true, so request information from the client
//				    		{
//				    			gameInfo.message = "Select to Continue or Quit!";
//				    			this.updateClients(gameInfo);
//				    			callback.accept("Waiting for Client's choice to Continue or Quit");
//				    		}
//				    	}
//			    	}
//				    catch(Exception e) {
//				    	
//				    	// If both players have left the server, set respective fields
//				    	if (Server.this.clients.size() == 1)
//				    	{
//				    		callback.accept("Both players have left the server, server empty");
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
//				    	break;
//				    }
//				}
				
			}//end of run

			
		}//end of client thread
}

