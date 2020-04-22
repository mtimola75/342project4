import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread{
	Socket socketClient;
	GameInfo theGame = new GameInfo();
	int port;
	String IP;
	ObjectOutputStream out;
	ObjectInputStream in;
	int score;
	int p1, p2, p3;
	int p1s, p2s, p3s;
	int clientnum;

	private Consumer<Serializable> callback;

	Client(Consumer<Serializable> call, int p, String ip){
		callback = call;
		port = p;
		IP = ip;
		score = 0;
		p1 = -1;
		p2 = -1;
		p3 = -1;
		p1s = 0;
		p2s = 0;
		p3s = 0;
	}

	public void run() {

		try {
			socketClient= new Socket(IP,port);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}

		while(true) {
			try {
				GameInfo game = (GameInfo) in.readObject();
				if (!game.message.equals("scores")) {
					theGame = game;
					if (!theGame.message.equals("")) {
						callback.accept(theGame.message);
						theGame.message = "";
						sendGame();
					}
				}
				else{
					p1 = game.p1;
					p1s = game.p1s;
					p2 = game.p2;
					p2s = game.p2s;
					p3 = game.p3;
					p3s = game.p3s;
					callback.accept(game.message);
				}
			}
			catch(Exception e) {}
		}
	}

	public void sendGame() {

		try {
			out.writeObject(theGame);
			out.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}