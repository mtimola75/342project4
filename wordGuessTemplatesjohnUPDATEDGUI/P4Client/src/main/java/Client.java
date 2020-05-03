import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

// Name: John Cervantes
// UIN: 659125820
// NetID: jcerva23
//
// Description of File:
//
// The Client.java file of MorraProject3Client inlcudes the information for the Client Class that allows for functionality for
// the Game of Morra game.
//

public class Client extends Thread{

	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	static boolean  connectStatus = false;
	int portNum;
	String ipAddress;
	
	private int playerNum = 0;
	private Consumer<Serializable> callback;
	WordGuessInfo gameInfo = new WordGuessInfo();
	
	// Parameterized Constructor with Consumer<Serializable> call
	Client(Consumer<Serializable> call){
	
		callback = call;
		portNum = 5555;
		playerNum = 0;
	}
	
	// Parameterized Constructor with Consumer, portNum, and ipAddress
	Client(Consumer<Serializable> call, int portNum, String ipAddress){
		
		callback = call;
		this.portNum = portNum;
		playerNum = 0;
		this.ipAddress = ipAddress;
	}
	
	// run():
	//
	// Run the clientThread by trying to connect to the socket of the server, open input and output streams. Then listen for when the server sends a MorraInfo object
	// to accept and update information on the GUI by accepting the message field of gameInfo
	//
	public void run() {
		
		// Attempt to connect to the server via usage of a socket using the ipAddress and portNum input by user
		try 
		{
			socketClient= new Socket(ipAddress,portNum);
		    out = new ObjectOutputStream(socketClient.getOutputStream());
		    in = new ObjectInputStream(socketClient.getInputStream());
		    socketClient.setTcpNoDelay(true);
		    gameInfo.gameStatus = 4;
	    	out.writeObject(gameInfo); // Output the gameInfo object created to the server to notify of connection
	    	callback.accept("Success");
		}
		catch(Exception e) {
			connectStatus = false;
			callback.accept("Error: Could not connect, invalid ip or port"); // If connection was a failure, accept an error message
		}
		
		// Infinite loop to listen for MorraInfo objects from the server and update Client GUI using the messages stored in gameInfo
		while(true) {
			
			// Try to read in the object of type MorraInfo
			try {
				
				gameInfo = (WordGuessInfo)in.readObject();
				
				if (gameInfo.gameStatus == 0)
				{
					//
				}
				else if (gameInfo.gameStatus == 2)
				{
//					System.out.println("Info received: " + gameInfo.clientProgressGuess);
					callback.accept(gameInfo);
				}
				
			}
			catch(Exception e) {
			}
		}
    } // end run(...)
	
	// Send():
	//
	// Send MorraInfo object to the server via output stream, the parameter is of type MorraInfo
	//
	public void send(WordGuessInfo data) {
		try {	
				out.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // end send()
	
	// getPlayerNum():
	//
	// Return the player number associated with the client, return type of int
	public int getPlayerNum()
	{
		return playerNum;
	} // end getPlayerNum()
	
	// setPlayerNum(...):
	//
	// Set the playerNum of the calling client to the passed playerNum of type integer. Void return type of function
	//
	public void setPlayerNum(int playerNum)
	{
		this.playerNum = playerNum;
	} // end setPlayerNum(...)


}
