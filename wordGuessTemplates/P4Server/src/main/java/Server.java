import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server {
    public
        int counter = 1;
        ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
        ArrayList<Integer> topScores = new ArrayList<Integer>();
        TheServer server;
        int port;
        int left = 1;
        private Consumer<Serializable> callback;
        GameInfo Master;

	Server(Consumer<Serializable> call, int p){
		callback = call;
		server = new TheServer();
		server.start();
		port = p;
        topScores.add(-1);
		Master = new GameInfo();
	}

	public class TheServer extends Thread{

		public void run() {
			try(ServerSocket mysocket = new ServerSocket(port);){
				System.out.println("Server is waiting for a client!");

				while(true) {
					ClientThread c = new ClientThread(mysocket.accept(), counter);
					callback.accept("new client connected: " + "client #" + counter);
					clients.add(c);
                    topScores.add(0);
					c.start();
					counter++;
				}
			}//end of try
			catch(Exception e) {
				callback.accept("Server socket did not launch");
			}
		}//end of while
	}
    class FindNextMove extends Thread {
        private String[] init_board;
        private ArrayList<Node> movesList;
        MinMax sendIn_InitState;
        volatile int change = -1;

        @Override
        public void run() {
           try {
               while (true) {
                   if(change != -1)
                      makeMove();
               }
           } catch (InterruptedException e) { }
        }
        public synchronized int makeMove() throws InterruptedException{
            init_board = Master.board;
            sendIn_InitState = new MinMax(init_board);
            movesList = sendIn_InitState.findMoves();
            change = -1;

            if(movesList.isEmpty())
                return -1;

            if(Master.difficulty == 1)
                return getMovesEasy();
            else if(Master.difficulty == 2)
                return getMovesMedium();
            else
                return getMovesExpert();
        }


        synchronized int getMovesExpert(){
            ArrayList<Integer> moves = new ArrayList<Integer>();

            for (Node temp : movesList) {
                //add winning moves to array list
                if (temp.getMinMax() == 10)
                    moves.add(temp.getMovedTo());
            }
            //check if there were any winning moves
            if(moves.isEmpty()){
                for (Node temp : movesList) {
                    //add tie moves to array list
                    if (temp.getMinMax() == 0)
                        moves.add(temp.getMovedTo());
                }
            }
            //check if there were any winning/tie moves
            if(moves.isEmpty()){
                for (Node temp : movesList) {
                    //add losing moves to array list
                    if (temp.getMinMax() == -10)
                        moves.add(temp.getMovedTo());
                }
            }
            int range = moves.size();
            int rand = (int)(Math.random()* range);
            return moves.get(rand);
        }

        synchronized int getMovesMedium(){
            ArrayList<Integer> moves = new ArrayList<Integer>();

            for (Node temp : movesList) {
                //add winning and tie moves to array list
                if (temp.getMinMax() == 10 || temp.getMinMax() == -10)
                    moves.add(temp.getMovedTo());
            }
            int range = moves.size();
            int rand = (int)(Math.random()* range);
            return moves.get(rand);
        }

        synchronized int getMovesEasy(){
            ArrayList<Integer> moves = new ArrayList<Integer>();

            for (Node temp : movesList) {
                //add winning, losing, and tie moves to array list
                if (temp.getMinMax() == 10 || temp.getMinMax() == 0 || temp.getMinMax() == -10)
                    moves.add(temp.getMovedTo());
            }
            int range = moves.size();
            int rand = (int)(Math.random()* range);
            return moves.get(rand);
        }
    }//end FindNextMove class

	class ClientThread extends Thread{
        GameInfo theGame = new GameInfo();
		Socket connection;
		int count;
		ObjectInputStream in;
		ObjectOutputStream out;

		ClientThread(Socket s, int count){
			this.connection = s;
			this.count = counter;
		}

		public void updateClients(String message) {
            for (ClientThread client : clients) {
                theGame.message = message;
                ClientThread t = client;
                try {
                    t.out.writeObject(theGame);
                    t.out.reset();
                } catch (Exception e) { }
            }
		}

        private void sendScores(GameInfo g){
            for (ClientThread client : clients) {
                ClientThread t = client;
                try {
                    t.out.writeObject(g);
                    t.out.reset();
                } catch (Exception e) { }
            }
        }

        private void individualMessage(int store, String s) {
            theGame.message = s;
            for(int i = 0; i < clients.size(); i++) {
                ClientThread t = clients.get(i);
                if(t.count == store) {
                    try {
                        t.out.writeObject(theGame);
                        t.out.reset();
                    } catch (Exception e) { }
                }
            }
        }

		public void run(){

			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
                GameInfo g = new GameInfo();
                g.message = "scores";
                for(int i = 1; i < topScores.size(); i++){
                    if(topScores.get(i) > g.p1s){
                        g.p1s = topScores.get(i);
                        g.p1 = i;
                    }
                    else if(topScores.get(i) > g.p2s){
                        g.p2s = topScores.get(i);
                        g.p2 = i;
                    }
                    else if(topScores.get(i) > g.p3s){
                        g.p3s = topScores.get(i);
                        g.p3 = i;
                    }
                }
                sendScores(g);
			}
			catch(Exception e) {
				System.out.println("Streams not open");
			}

			updateClients("new client on server: client #"+count);
			individualMessage(count, "you are "+count);

			while(true) {
                try {
                    FindNextMove finder = new FindNextMove();
                    finder.start();
                    theGame = (GameInfo) in.readObject();
                    if(theGame.message.equals("go")) {
                        Master = theGame;
                        synchronized (finder) {
                            finder.change = count;
                            individualMessage(count, "your turn " + finder.makeMove());
                        }
                    }
                    else if(theGame.message.equals("again")) {
                        callback.accept("client " + count + " is playing again");
                    }
                    else if(theGame.message.equals("choose difficulty")) {
                        if(theGame.difficulty == 1)
                            callback.accept("client " + count + " wants to play easy mode");
                        else if(theGame.difficulty == 2)
                            callback.accept("client " + count + " wants to play medium mode");
                        else if(theGame.difficulty == 3)
                            callback.accept("client " + count + " wants to play expert mode");
                    }
                    else if(theGame.message.equals("client won")){
                        callback.accept("client " + count + " won a game");
                        theGame.message = "";
                        GameInfo g = new GameInfo();
                        g.message = "scores";
                        topScores.set(count, topScores.get(count) +1);
                        for(int i = 1; i < topScores.size(); i++){
                            if(topScores.get(i) > g.p1s){
                                if(g.p1s > 0 && g.p1 != i){
                                    g.p3s = g.p2s;
                                    g.p3 = g.p2;
                                    g.p2s = g.p1s;
                                    g.p2 = g.p1;
                                }
                                g.p1s = topScores.get(i);
                                g.p1 = i;
                            }
                            else if(topScores.get(i) > g.p2s){
                                if(g.p2s > 0 && g.p2 != i){
                                    g.p3s = g.p2s;
                                    g.p3 = g.p2;
                                }
                                g.p2s = topScores.get(i);
                                g.p2 = i;
                            }
                            else if(topScores.get(i) > g.p3s){
                                g.p3s = topScores.get(i);
                                g.p3 = i;
                            }
                        }
                        sendScores(g);
                        individualMessage(count, "");
                    }
                    else if(theGame.message.equals("server won")){
                        callback.accept("client " + count + " lost a game");
                        theGame.message = "";
                        individualMessage(count, "");
                    }
                    else if(theGame.message.equals("tie")){
                        callback.accept("client " + count + " tied a game");
                        theGame.message = "";
                        individualMessage(count, "");
                    }
                }
                catch(Exception e) {
                    if(theGame.message.equals("bye")) {
                        callback.accept("client " + count + " quit, closing down");
                        left++;
                        clients.remove(this);
                        break;
                    }
                }
			}
		}//end of run
	}//end of client thread
}