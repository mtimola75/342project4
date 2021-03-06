import java.util.HashMap;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WordGuessClient extends Application {

	// IP & Port scene elements
	Text portDirections, portLabel, ipLabel;
	TextField portInput, ipInput;
	Button connectButton;
	HBox portBox, ipBox;
	VBox portIPBox;
	BorderPane portPane;
	HashMap<String, Scene> sceneMap;
	EventHandler<ActionEvent> cat1Event, cat2Event, cat3Event;
	PauseTransition pause = new PauseTransition(Duration.seconds(3));

	// Category selection scene elements
	Image _category1, _category2, _category3;
	ImageView category1, category2, category3;
	Text categoryInstructions, category1Label, category2Label, category3Label;
	HBox categorySelect;
	Button cat1, cat2, cat3;

	// Client scene elements
	Text categoryChoiceLabel, categoryChoice, playerGuessLabel;
	Image _hangman;
	ImageView hangman;
	ListView<String> guesses;
	HBox categoryBox, guessBox;
	VBox categoryAndGuessBox;

	Client c;
	
	static final int categoryHeight = 300;
	static final int categoryWidth = 200;
	
	// JOHN'S EDIT
	TextField serverInfo;
	
	boolean portEnter = false;
	boolean ipEnter = false;
	TextField portNum = new TextField();
	TextField ipAddress = new TextField();
	PauseTransition failTran = new PauseTransition(Duration.seconds(2));
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	// feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("(Client) Word Guess!!!");

		// :::start initial port window:::
		this.portDirections = new Text("Input port number & IP Address");
		this.portLabel = new Text("Port number: ");
		this.ipLabel = new Text("IP Address: ");
		this.portInput = new TextField();
		this.ipInput = new TextField();
		this.connectButton = new Button("Connect");
		this.serverInfo = new TextField();
		
		this.portBox = new HBox(8, portLabel, portInput); // port box
		this.ipBox = new HBox(8, ipLabel, ipInput); // ip box
		this.portIPBox = new VBox(10, portDirections, ipLabel, ipAddress, portLabel, portNum, serverInfo); // holds both boxes
		
		// Define the ipAddress EventHandler to handle the event where the TextField is pressed with Enter, update respective fields and enable portNum
		ipAddress.setOnKeyPressed(e-> 
		{
			if (e.getCode().equals(KeyCode.ENTER)) 
			{	
				ipEnter = true; // Set ipEnter to a value of true
				ipAddress.setDisable(true); // Set ipAddress TextField to be disabled using the setDisable(...) method
				portNum.setDisable(false); // Enable portNum using setDisable(...)
			}
		}); // end ipAddress.setOnKeyPressed(...)
		
		// Define the EventHandler for failTran PauseTransition to set the serverInfo background color to white and clear the information after the pause is finished
		failTran.setOnFinished(e->{
			serverInfo.setStyle("-fx-background-color: white;");
			serverInfo.clear();
		}); // end failTran.setOnFinished(...)
		
		// Disable the portNum button by using the setDisable(...) method and passing true as a parameter
		portNum.setDisable(true);
		
		// Define the EventHandler for the portNum TextField for when the Enter key is pressed, the EventHandler is important here as it instantiates the client connection
		// to the server and within it, it handles the GUI updates that taken place in the client
		//
		portNum.setOnKeyPressed(e-> 
		{
			if (e.getCode().equals(KeyCode.ENTER)) 
			{
				portEnter = true;
				
				// Check if the ipEnter and portEnter booleans are both true, if so, enter the block of code to instantiate the Client and attempt to connect to server
				if ((ipEnter == true) && (portEnter == true))
				{
					
					// Declare the instance of Client, assigning it to c and passing the serializable consumer, ipAddress and portNum to the  parameterized constructor
					c = new Client(client->Platform.runLater(()->
					{
						// If the gameStatus held in gameInfo is 1 and the callback string of the client is the corresponding message, the information provided is invalid, set fields and request input from user again
						if (client.toString().contains("Error: Could not connect, invalid ip or port"))
						{
							serverInfo.setText("Error: Could not connect, invalid ip or port");
							serverInfo.setStyle("-fx-background-color: hotpink;");
							failTran.play();
							portNum.clear();
							portNum.setDisable(true);
							ipAddress.setDisable(false);
							ipEnter = false;
							portEnter = false;
						}
						else // Else the client has successfully connected to the server, set the corresponding fields and now the client will listen for and send the gameInfo object to the server
						{
							serverInfo.setStyle("-fx-background-color: lightgreen;"); // Set serverInfo to light green background using setStyle(...)
							serverInfo.setText("Connection successful, continuing to category selection...");
							// Disable portNum and ipAddress when connection is successful using setDisable(...) method
							portNum.setDisable(true);
							ipAddress.setDisable(true);
						}
					}), Integer.parseInt(portNum.getText().toString()), ipAddress.getText().toString());
				
					primaryStage.setScene(createMenuGui()); // Set the primaryStage to the resulting scene of setUpClientMenu() using setScene(...)
					
					// Set ipEnter and portEnter to a value of false
					ipEnter = false;
					portEnter = false;
					
					c.start(); // Start the client connection using the start() method
				}
				
			} // end if(...)
		}); // end portNum.setOnKeyPressed(....)
		
		// launch client
		this.connectButton.setOnAction(e -> {

			String sPortNum = portInput.getText(); // get port num
			String sIPNum = ipInput.getText(); // get ip num
			System.out.println("Port input: " + sPortNum);
			System.out.println("Port input: " + sIPNum);

			if (!sPortNum.isEmpty() && !sIPNum.isEmpty()) // valid ip and port
			{
				primaryStage.setScene(sceneMap.get("client"));
				primaryStage.setTitle("<<CLIENT>>");

				int iPortNum = Integer.valueOf(sPortNum);

				// ADD CODE TO CREATE NEW CLIENT HERE; OLD CODE COMMENTED OUT BC IDK HOW WE
				// SHOULD IMPLEMENT CLIENT THREADS
//				newClient = new Client(iPortNum, data -> {
//					Platform.runLater(()->{
//						listItems.getItems().add(data.toString());
//					});  //end runLater
//				});  //end newClient
//				newClient.start();
			} // end if
			else {
				this.portDirections.setText("<<ERROR>> Input valid port and IP.");
			}

		}); // end connectButton.setOnAction()

//		// create scene for port and ip window
//		portPane = new BorderPane();
//		portPane.setPadding(new Insets(30));
//		portPane.setCenter(portIPBox);
//		portPane.setRight(connectButton);

//		Scene portScene = new Scene(portPane, 400, 170); // INITIAL SCENE; not put in sceneMap since it will start here
		// :::end initial port window:::

		sceneMap = new HashMap<String, Scene>();

		// :::start category selection screen:::
		sceneMap.put("category", createCategoryGui()); // create category selection scene and put into sceneMap

		cat1Event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				/*ADD CODE TO PASS OVER CATEGORY1 DATA HERE*/
				primaryStage.setScene(sceneMap.get("gameplay"));  //get from scene map
			}
		};  //end cat1Event

		cat2Event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				/*ADD CODE TO PASS OVER CATEGORY2 DATA HERE*/
				primaryStage.setScene(sceneMap.get("gameplay"));  //get from scene map
			}
		};  //end cat2Event
		
		cat3Event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				/*ADD CODE TO PASS OVER CATEGORY3 DATA HERE*/
				primaryStage.setScene(sceneMap.get("gameplay"));  //get from scene map
			}
		};  //end cat3Event
		
		// :::end category selection screen::

		// :::start gameplay screen:::
		sceneMap.put("gameplay", createClientGui()); // create gameplay scene and put into sceneMap

		Scene test = createCategoryGui();
		primaryStage.setScene(createMenuGui()); // change to "setScene(portScene)" to start from inputting IP address; if
										// anything else, it is for testing purposes
		primaryStage.show();
	} // end start()

	
	public Scene createMenuGui()
	{
		// create scene for port and ip window
		portPane = new BorderPane();
		Image background = new Image("images/gameroom.png"); // Declare an object of type Image, titleBackground to hold the image based on the value of altLook
		// Declare and instantiate a BackgroundSize object, size, to set up the respective fields of the background
		BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		// Using the BorderPane method, setBackground(...), set the background of pane to the titleBackground
		portPane.setBackground(new Background(new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, size )));
		
		portPane.setPadding(new Insets(30));
		portPane.setCenter(portIPBox);
//		portPane.setRight(connectButton);
		
		return new Scene(portPane, 600, 600);
	}
	
	public Scene createCategoryGui() {
		BorderPane categoryPane = new BorderPane();
		categoryPane.setPadding(new Insets(30));

		categoryInstructions = new Text("Choose a category you would like to play.");
		categoryInstructions.setTextAlignment(TextAlignment.CENTER);
		// init pics
//		_category1 = new Image("cat1.jpg");
//		_category2 = new Image("cat2.jpg");
//		_category3 = new Image("cat3.jpg");
//
//		category1 = new ImageView(_category1);
//		category2 = new ImageView(_category2);
//		category3 = new ImageView(_category3);

		// sizing
//		category1.setFitHeight(categoryHeight);
//		category1.setFitWidth(categoryWidth);
//		category1.setPreserveRatio(true);
//
//		category2.setFitHeight(categoryHeight);
//		category2.setFitWidth(categoryWidth);
//		category2.setPreserveRatio(true);
//
//		category3.setFitHeight(categoryHeight);
//		category3.setFitWidth(categoryWidth);
//		category3.setPreserveRatio(true);

		cat1 = new Button("Category 1");
//		cat1.setGraphic(category1);
		cat1.setOnAction(cat1Event);  //goes to EventHandler for category1
		
		cat2 = new Button("Category 2");
//		cat2.setGraphic(category2);
		cat2.setOnAction(cat2Event);  //goes to EventHandler for category2
		
		cat3 = new Button("Category 3");
//		cat3.setGraphic(category3);
		cat3.setOnAction(cat3Event);  //goes to EventHandler for category3

		categorySelect = new HBox(3, cat1, cat2, cat3); // put pictures into HBox to view

		categoryPane.setCenter(categorySelect);
		categoryPane.setTop(categoryInstructions);

		return new Scene(categoryPane, 775, 350);
	}

	public Scene createClientGui() {
		BorderPane gameplayPane = new BorderPane();

		categoryChoiceLabel = new Text("Your category: ");
		categoryChoice = new Text("<<CATEGORY HERE>>"); // can be updated based on the category chosen
		playerGuessLabel = new Text("You guessed the letters: ");
		guesses = new ListView<String>();

		categoryBox = new HBox(3, categoryChoiceLabel, categoryChoice);
		guessBox = new HBox(3, playerGuessLabel, guesses);
		categoryAndGuessBox = new VBox(3, categoryBox, guessBox);

		gameplayPane.setLeft(categoryAndGuessBox);

		return new Scene(gameplayPane, 800, 800);
	}

}