import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

// Names: John Cervantes, Mike Lazzar, Marc Timola, Krzysztof Para
//
// Description of File:
//
// The Client.java file of ClientWordGuessSpring2020 includes the information for the Client Class that allows for functionality for
// the WordGuess game and for connection to the server.
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
			
			// Try to read in the object of type WordGuessInfo
			try {
				
				gameInfo = (WordGuessInfo)in.readObject();
				
				if (gameInfo.gameStatus == 2)
				{
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

}
