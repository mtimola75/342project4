import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WordGuessServer extends Application {
	
	Server serverConnection;
	Button startServer = new Button("Press to start Server");
	Button startGame = new Button("Start Game");
	Text numUsers = new Text();
	Text title = new Text("Word Guess\n  ~Server~");
	Text serverInfoTitle = new Text("\tWord\n\tGuess\nServer Information");
	Boolean serverLive = false;

	Text portText = new Text("Enter Server Port here:");
	Text serverStatus = new Text("Server Status:");
	TextField portNum;
	TextField ipAddress;
	Boolean ipEnter = false;
	Boolean portEnter = false;
	TextField errorMessage = new TextField();
	PauseTransition errorTran = new PauseTransition(Duration.seconds(1));
	PauseTransition successTran = new PauseTransition(Duration.seconds(1));
	MenuBar optionBar;
	Menu mOne;
	MenuItem optionOne;
	PauseTransition revertColor = new PauseTransition(Duration.seconds(0.25));
	// Variables to display server information in a ListView obtaining strings from an ObservableList
	ListView<String> listItems;
	ObservableList<String> serverItems;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("(server) Playing word guess!!!"); // Using setTitle(...) set the title of the primaryStage to a specific string showing the user that the program refers to the server
		
		// Define the EventHandler for revertColor to set the color of the startServer button to cornflower blue when the
		// pause is finished
		revertColor.setOnFinished(e->startServer.setStyle("-fx-background-color: black"));
		
		startServer.setStyle("-fx-background-color: cornflowerblue;");
		startServer.setTranslateX(235);
		startServer.setTranslateY(30);
		startServer.setDisable(true);
		
		// Set the font, fill, and style of the serverInfoTitle using setFont(), setFill() and setStyle() methods respectively
		serverInfoTitle.setFont(Font.font(null, FontWeight.BOLD, 25));
		serverInfoTitle.setFill(Color.WHITE);
		serverInfoTitle.setStyle("-fx-stroke-width: 1; -fx-stroke: black;");
		
		// Set the font, fill, and style of the title using setFont(), setFill() and setStyle() methods respectively
		title.setFont(Font.font(null, FontWeight.BOLD, 40));
		title.setFill(Color.WHITE);
		title.setStyle("-fx-stroke-width: 2; -fx-stroke: black;");
		title.setTranslateX(200);
		
		// Set the font, fill, and style of the portText using setFont(), setFill() and setStyle() methods respectively
		portText.setFont(Font.font(null, FontWeight.BOLD, 20));
		portText.setFill(Color.WHITE);
		portText.setStyle("-fx-stroke-width: 1; -fx-stroke: black;");
		portText.setTranslateX(200);
		
		// Set the font, fill, and style of the serverStatus using setFont(), setFill() and setStyle() methods respectively
		serverStatus.setFont(Font.font(null, FontWeight.BOLD, 20));
		serverStatus.setFill(Color.WHITE);
		serverStatus.setStyle("-fx-stroke-width: 1; -fx-stroke: black;");
		serverStatus.setTranslateX(235);
		
		// Set the font, fill, and style of the numUsers using setFont(), setFill() and setStyle() methods respectively
		numUsers.setFont(Font.font(null, FontWeight.BOLD, 15));
		numUsers.setFill(Color.WHITE);
		numUsers.setStyle("-fx-stroke-width: 1; -fx-stroke: black;");
		
		// Initialize portNum to a new instance of the class TextField and set the x coordinate and max size of the TextField using setTranslateX(...) and setMaxSize(...)
		portNum = new TextField();
		portNum.setTranslateX(200);
		portNum.setMaxSize(200, 20);
		
		// Set the maximum size, x axis corresponding to the stage, and opacity of the errorMessage TextField using setMaxSize(...), setTranslateX(...), and setOpacity(...)
		errorMessage.setMaxSize(300, 20);
		errorMessage.setTranslateX(150);
		errorMessage.setOpacity(50);
		
		// Define the EventHanlder for successTran PauseTransition to clear and change the color of errorMessage when done and set the primaryStage to the result of createServerGUI()
		successTran.setOnFinished(e->
		{
			errorMessage.clear();
			errorMessage.setStyle("-fx-background-color: white;");
			primaryStage.setScene(createServerGui());
		}); // end successTran.setOnFinished(...)
		
		listItems = new ListView<String>(); // Assign listItems to an instance of the ListView class that holds objects of type String
		serverItems = FXCollections.observableArrayList(); // Assign serverItems to the result of the FXCollections.observableArrayList() method.
		
		optionBar = new MenuBar(); // Initialize optionBar to an instance of MenuBar
		optionBar.setStyle("-fx-background-color: darkgray;"); // Set the color of the optionBar to peru, using the setStyle() method
		mOne = new Menu("Options"); // Initialize mOne to a new instance of the class Menu and name the Menu to "Options"
		optionOne = new MenuItem("Exit"); // Initialize optionOne to a new instance of the class MenuItem and name it "Exit"
		optionOne.setOnAction(e->primaryStage.hide()); // Define the eventHandler for optionOne using a lambda expression to hide the primaryStage when option is pressed

		mOne.getItems().addAll(optionOne); // Using the Menu method, getItems() and addAll(), add all needed MenuItems to mOne 
		optionBar.getMenus().addAll(mOne); // Use the MenuBar method, getMenus() and addAll(), to add mOne to optionBar
		errorMessage.setDisable(true);
		
		// Define the EventHandler for portNum for when the key press of Enter occurs, it disables portNum and enables startServer
		portNum.setOnKeyPressed(e-> {if (e.getCode().equals(KeyCode.ENTER)) {
			
			// Disable and Enable widgets respectively using setDisable(...)
			portNum.setDisable(true); 
			startServer.setDisable(false);
			
		}}); // end portNum.setOnKeyPressed(...)
		
		// Define the EventHandler for startServer, for when the user of the server presses the startServer button. The startServer button is responsible in creating
		// the serverConnection
		this.startServer.setOnAction(e->
		{
			startServer.setStyle("-fx-background-color:skyblue");
			revertColor.play();
			
			serverConnection = new Server(data->
			{
				Platform.runLater(()->
				{
					// Condition to check if the server contains an error message that the socket did not launch for the server, if so, update fields and widgets respectively to allow new input
					if (data.toString().contains("Error: Server socket did not launch"))
					{
						// Update errorMessage by updating the text and background color using setText() and setStyle()
						errorMessage.setText("Error: Server socket did not launch, try again");
						errorMessage.setStyle("-fx-background-color: hotpink");
						
						serverLive = false; // set serverLive to false
						
						portNum.setDisable(false); // Enable the portNum button using the setDisable(...) method
						errorTran.play(); // Play the errorTran PauseTransition using play()
					}
					else if (data.toString().contains("Server Connection success, waiting on clients to join!") && (serverLive != true)) // Else if the server data contains a success message and the server is not yet true, enter block of code
					{
						// Update errorMessage with a new message indicating a successful connection and change the color to light green
						errorMessage.setText("Connection Success, waiting for clients!");
						errorMessage.setStyle("-fx-background-color: lightgreen");
						serverLive = true; // Set serverLive to true
						portNum.setDisable(true); // Disable portNum using setDisable(...)
					}
					
					// Check if serverLive is true, if so, enter block of code
					if (serverLive)
					{
						successTran.play(); // play successTran PauseTransition using play()
						startServer.setDisable(true);
						
						// Update numUsers Text object to the current client count
						numUsers.setText("Users connected: " + serverConnection.getClientCount());
						
						serverItems.add(data.toString());
						listItems.setItems(serverItems);
					}
				});

			}, Integer.parseInt(portNum.getText().toString()));
			
			primaryStage.setScene(createStartUpGui()); // Set the primaryStage scene to the resulting scene of createStartUpGui(), using setScene(...)
											
		}); // end this.startServer.setOnAction(...)
		
		primaryStage.setScene(createStartUpGui()); // Set the primaryStage scene to the resulting scene of createStartUpGui(), using setScene(...)
		primaryStage.show(); // Show the primaryStage using the method show()
	}
	
	// createStartUpGui()
	//
	// The createStartUpGui returns a scene consisting of the start up user interface containing an area to input the port number to start the server connection
	// for local host. The return type of the method is Scene
	//
	public Scene createStartUpGui()
	{
		BorderPane pane = new BorderPane(); // Create an instance of BorderPane to hold the GUI widgets and elements
		Image titleBackground = new Image("images/gameroom.png"); // Declare an object of type Image, titleBackground to hold the image based on the value of altLook
		// Declare and instantiate a BackgroundSize object, size, to set up the respective fields of the background
		BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		
		VBox centerArea = new VBox(5, optionBar, title, portText, portNum, serverStatus, errorMessage, startServer); // Declare and instantiate new VBox instance, centerArea, to hold the GUI elements including widgets
		pane.setCenter(centerArea); // set the center of the pane to centerArea using setCenter()
		
		// Using the BorderPane method, setBackground(...), set the background of pane to the titleBackground
		pane.setBackground(new Background(new BackgroundImage(titleBackground, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, size )));
		return new Scene(pane, 600, 600); // Return new Scene displaying the pane with dimensions 600x600
	} // end createStartUpGui()
	
	// createServerGui():
	//
	// The createServerGui() method returns a scene displaying Server user interface that shows the messages obtained and accepted by the server given client input
	// of gameInfo objects. The return type of the method is Scene.
	//
	public Scene createServerGui() {
		
		BorderPane pane = new BorderPane(); // Create an instance of BorderPane to hold the boxes and GUI elements
		Image titleBackground = new Image("images/gameroom.png"); // Declare an object of type Image, titleBackground to hold the image based on the value of altLook
		// Declare and instantiate a BackgroundSize object, size, to set up the respective fields of the background
		BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		
		// Using the BorderPane method, setBackground(...), set the background of pane to the titleBackground
		pane.setBackground(new Background(new BackgroundImage(titleBackground, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, size )));
		
		listItems.setMinSize(200, 200); // Limit the listItems ListView to 200x200 using setMinSize(...)

		pane.setPadding(new Insets(70)); // Using setPadding set the padding of the pane to a new instance of Insets(70)
		VBox infoBox = new VBox(10, serverInfoTitle, listItems, numUsers); // Declare and instantiate new vBox object, infoBox to hold the GUI elements
		pane.setCenter(infoBox); // Set infoBox to center of pane
	
		return new Scene(pane, 500, 500); // Return new Scene displaying the pane with dimensions 500x500
		
	} // end createServerGui()

}
