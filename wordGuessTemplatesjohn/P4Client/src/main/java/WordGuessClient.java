import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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

	// Guesses
	TextField playerGuessTextField;
	Button submitGuessButton = new Button();
	Set<String> playerGuessesSet = new HashSet<String>(Arrays.asList(""));
	String playerGuess;
	WordGuessInfo gameInfo = new WordGuessInfo();
	Text enterA_ZValue = new Text();
	Client c;

	static final int categoryHeight = 300;
	static final int categoryWidth = 200;

	// JOHN'S EDIT
	TextField serverInfo;

	boolean portEnter = false;
	boolean guessEnter = false;
	boolean ipEnter = false;
	TextField portNum = new TextField();
	TextField ipAddress = new TextField();
	PauseTransition failTran = new PauseTransition(Duration.seconds(2));
	PauseTransition returnToCategories = new PauseTransition(Duration.seconds(1.5));

	boolean cat1Chosen = false;
	boolean cat2Chosen = false;
	boolean cat3Chosen = false;
	boolean cat1Correct = false;
	boolean cat2Correct = false;
	boolean cat3Correct = false;

	Button continueButton = new Button("Continue");
	
	Image guessZero = new Image("images/hang0.png");
	ImageView guessZeroView = new ImageView(guessZero);
	Image guessOne = new Image("images/hang1.png");
	ImageView guessOneView = new ImageView(guessOne);
	Image guessTwo = new Image("images/hang2.png");
	ImageView guessTwoView = new ImageView(guessTwo);
	Image guessThree = new Image("images/hang3.png");
	ImageView guessThreeView = new ImageView(guessThree);
	Image guessFour = new Image("images/hang4.png");
	ImageView guessFourView = new ImageView(guessFour);
	Image guessFive = new Image("images/hang5.png");
	ImageView guessFiveView = new ImageView(guessFive);
	Image guessSix = new Image("images/hang6.png");
	ImageView guessSixView = new ImageView(guessSix);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	// feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("(Client) Word Guess!!!");
		
		guesses = new ListView<String>();
		guesses.setMaxSize(400, 100);
		
		cat1 = new Button("Food");
		cat2 = new Button("Animals");
		cat3 = new Button("States");
		// :::start initial port window:::
		this.portDirections = new Text("Input port number & IP Address");
		this.portDirections.setFont(Font.font(null, FontWeight.BOLD, 20));
		this.portDirections.setFill(Color.AZURE);
		this.portDirections.setStyle("-fx-stroke-width: 1; -fx-stroke: cornflowerblue;");

		this.portLabel = new Text("Port number: ");
		this.portLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
		this.portLabel.setFill(Color.AZURE);
		this.portLabel.setStyle("-fx-stroke-width: 1; -fx-stroke: cornflowerblue;");

		this.ipLabel = new Text("IP Address: ");
		this.ipLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
		this.ipLabel.setFill(Color.AZURE);
		this.ipLabel.setStyle("-fx-stroke-width: 1; -fx-stroke: cornflowerblue;");

		this.portInput = new TextField();
		this.ipInput = new TextField();
		this.connectButton = new Button("Connect");
		this.connectButton.setFont(Font.font(null, FontWeight.BOLD, 20));
		this.connectButton.setStyle("-fx-stroke-width: 1; -fx-stroke: cornflowerblue;");
		
		this.serverInfo = new TextField();

		this.portBox = new HBox(8, portLabel, portInput); // port box
		this.ipBox = new HBox(8, ipLabel, ipInput); // ip box
		this.portIPBox = new VBox(10, portDirections, ipLabel, ipAddress, portLabel, portNum, serverInfo); // holds both
																											// boxes

		// Define the ipAddress EventHandler to handle the event where the TextField is
		// pressed with Enter, update respective fields and enable portNum
		ipAddress.setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				ipEnter = true; // Set ipEnter to a value of true
				ipAddress.setDisable(true); // Set ipAddress TextField to be disabled using the setDisable(...) method
				portNum.setDisable(false); // Enable portNum using setDisable(...)
			}
		}); // end ipAddress.setOnKeyPressed(...)

		// Define the EventHandler for failTran PauseTransition to set the serverInfo
		// background color to white and clear the information after the pause is
		// finished
		failTran.setOnFinished(e -> {
			serverInfo.setStyle("-fx-background-color: white;");
			serverInfo.clear();
		}); // end failTran.setOnFinished(...)

		returnToCategories.setOnFinished(e -> {
			if (cat1Chosen && c.gameInfo.allCorrect) {
				cat1.setDisable(true);
			} else if (cat2Chosen && c.gameInfo.allCorrect) {
				cat2.setDisable(true);
			} else if (cat3Chosen && c.gameInfo.allCorrect) {
				cat3.setDisable(true);
			}
			c.gameInfo.allCorrect = false;
			c.gameInfo.guessesLeft = 6;
			c.gameInfo.guessCorrect = false;
			c.gameInfo.clientProgressGuess = "";
			playerGuessesSet.clear();
			guesses = new ListView<String>();

			primaryStage.setScene(createCategoryGui());

		});
		// Disable the portNum button by using the setDisable(...) method and passing
		// true as a parameter
		portNum.setDisable(true);

		// Define the EventHandler for the portNum TextField for when the Enter key is
		// pressed, the EventHandler is important here as it instantiates the client
		// connection
		// to the server and within it, it handles the GUI updates that taken place in
		// the client
		//
		portNum.setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				portEnter = true;

				// Check if the ipEnter and portEnter booleans are both true, if so, enter the
				// block of code to instantiate the Client and attempt to connect to server
				if ((ipEnter == true) && (portEnter == true)) {

					// Declare the instance of Client, assigning it to c and passing the
					// serializable consumer, ipAddress and portNum to the parameterized constructor
					c = new Client(client -> Platform.runLater(() -> {
						// If the gameStatus held in gameInfo is 1 and the callback string of the client
						// is the corresponding message, the information provided is invalid, set fields
						// and request input from user again
						if (client.toString().contains("Error: Could not connect, invalid ip or port")) {
							serverInfo.setText("Error: Could not connect, invalid ip or port");
							serverInfo.setStyle("-fx-background-color: hotpink;");
							failTran.play();
							portNum.clear();
							portNum.setDisable(true);
							ipAddress.setDisable(false);
							ipEnter = false;
							portEnter = false;
						} else // Else the client has successfully connected to the server, set the
								// corresponding fields and now the client will listen for and send the gameInfo
								// object to the server
						{

							serverInfo.setStyle("-fx-background-color: lightgreen;"); // Set serverInfo to light green
																						// background using
																						// setStyle(...)
							serverInfo.setText("Connection successful, continuing to category selection...");
							// Disable portNum and ipAddress when connection is successful using
							// setDisable(...) method
							portNum.setDisable(true);
							ipAddress.setDisable(true);
							if (c.gameInfo.gameStatus == 2) {
								
//								guesses.getItems().add(client.toString());
								if (c.gameInfo.allCorrect && guessEnter) {
									if (cat1Chosen) {
										cat1Correct = true;
									} else if (cat2Chosen) {
										cat2Correct = true;
									} else if (cat3Chosen) {
										cat3Correct = true;
									}
									guesses.getItems().add("You have correctly guessed the word: "
											+ c.gameInfo.clientProgressGuess + "!");
									guessEnter = false;
								} else if (c.gameInfo.guessCorrect && guessEnter) {
									guesses.getItems().add("Correct guess, keep going! Updated String: "
											+ c.gameInfo.clientProgressGuess);
									guessEnter = false;
								} else if (!c.gameInfo.guessCorrect && guessEnter) {
									guesses.getItems().add("Incorrect guess, try again! Updated String: "
											+ c.gameInfo.clientProgressGuess);
									guessEnter = false;
								}
								
								primaryStage.setScene(createClientGui());
								
								if ((c.gameInfo.guessesLeft == 0) || (c.gameInfo.allCorrect == true)) {
									if ((cat1Correct && cat2Correct && cat3Correct) || (c.gameInfo.foodFail == true)
											|| (c.gameInfo.animalFail == true) || (c.gameInfo.stateFail == true))
									{
										primaryStage.setScene(createContinueScreen());
//										System.out.println("QUIT");
//										Platform.exit(); // REPLACE WITH PAUSE TRANSITION
									} else {
										returnToCategories.play();
									}
								}
							}

						}
					}), Integer.parseInt(portNum.getText().toString()), ipAddress.getText().toString());

//					primaryStage.setScene(createMenuGui()); // Set the primaryStage to the resulting scene of setUpClientMenu() using setScene(...)
					primaryStage.setScene(createCategoryGui()); // Set the primaryStage to the resulting scene
					// of setUpClientMenu() using setScene(...)
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

//		submitGuessButton.setOnAction(e -> {
//
//			playerGuess = playerGuessTextField.getText();
//
//			if ((playerGuessesSet.contains(playerGuess))) {
//				playerGuessTextField.setText("You have already guessed " + playerGuessTextField.getText() + "'");
//
//			} else {
//
//				playerGuess = playerGuessTextField.getText();
//				playerGuessesSet.add(playerGuess);
//				guesses.getItems().add(playerGuessTextField.getText());
//				
//				c.gameInfo.playerGuess = playerGuessTextField.getText().toUpperCase();
//				
//				c.send(c.gameInfo);
////				if (firstword.contains(playerGuess)) {
////					guesses.getItems().add(playerGuessTextField.getText() + " is a correct letter!");
////				}
//
//			}
//
//		});
//		// create scene for port and ip window
//		portPane = new BorderPane();
//		portPane.setPadding(new Insets(30));
//		portPane.setCenter(portIPBox);
//		portPane.setRight(connectButton);

//		Scene portScene = new Scene(portPane, 400, 170); // INITIAL SCENE; not put in sceneMap since it will start here
		// :::end initial port window:::

		continueButton.setOnAction(e->{
			continueButton.setDisable(true);
			cat1Chosen = false;
			cat2Chosen = false;
			cat3Chosen = false;
			
			cat1Correct = false;
			cat2Correct = false;
			cat3Correct = false;
			
			cat1.setDisable(false);
			cat2.setDisable(false);
			cat3.setDisable(false);
			
			c.gameInfo = new WordGuessInfo();
			
			c.gameInfo.gameStatus = 0;
			c.gameInfo.allCorrect = false;
			c.gameInfo.message = "";
			c.send(c.gameInfo);
			returnToCategories.play();
		});
		
		sceneMap = new HashMap<String, Scene>();

		// :::start category selection screen:::
//		sceneMap.put("category", createCategoryGui()); // create category selection scene and put into sceneMap

		cat1Event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				/* ADD CODE TO PASS OVER CATEGORY1 DATA HERE */
				c.gameInfo.message = "Food";
				c.gameInfo.gameStatus = 1;
				cat1Chosen = true;
				cat2Chosen = false;
				cat3Chosen = false;
				categoryChoice = new Text("Food");

				c.send(c.gameInfo);
				primaryStage.setScene(createClientGui()); // get from scene map
			}
		}; // end cat1Event

		cat2Event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				/* ADD CODE TO PASS OVER CATEGORY2 DATA HERE */
				c.gameInfo.message = "Animals";
				c.gameInfo.gameStatus = 1;
				cat1Chosen = false;
				cat2Chosen = true;
				// added this disable because something was bugging out
				// and cat3Chosen = true wasnt disabling the button
				cat2.setDisable(true);
				cat3Chosen = false;
				categoryChoice = new Text("Animals");

				c.send(c.gameInfo);
				primaryStage.setScene(createClientGui()); // get from scene map
			}
		}; // end cat2Event

		cat3Event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				/* ADD CODE TO PASS OVER CATEGORY3 DATA HERE */
				c.gameInfo.message = "States";
				c.gameInfo.gameStatus = 1;
				cat1Chosen = false;
				cat2Chosen = false;
				cat3Chosen = true;
				// added this disable because something was bugging out
				// and cat3Chosen = true wasnt disabling the button
				cat3.setDisable(true);
				categoryChoice = new Text("States");

				c.send(c.gameInfo);
				primaryStage.setScene(createClientGui()); // get from scene map
			}
		}; // end cat3Event

		primaryStage.setScene(createMenuGui()); // change to "setScene(portScene)" to start from inputting IP address;
												// if
		// anything else, it is for testing purposes
		primaryStage.show();
	} // end start()

	public Scene createMenuGui() {
		// create scene for port and ip window
		portPane = new BorderPane();
		Image background = new Image("images/gameroom.png"); // Declare an object of type Image, titleBackground to hold
																// the image based on the value of altLook
		// Declare and instantiate a BackgroundSize object, size, to set up the
		// respective fields of the background
		BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		// Using the BorderPane method, setBackground(...), set the background of pane
		// to the titleBackground
		portPane.setBackground(new Background(new BackgroundImage(background, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, size)));

		portPane.setPadding(new Insets(30));
		portPane.setCenter(portIPBox);
//		portPane.setRight(connectButton);

		return new Scene(portPane, 600, 600);
	}

	public Scene createCategoryGui() {
		BorderPane categoryPane = new BorderPane();
		categoryPane.setPadding(new Insets(30));

		Image background = new Image("images/gameroom.png"); // Declare an object of type Image, titleBackground to hold
																// the image based on the value of altLook
		// Declare and instantiate a BackgroundSize object, size, to set up the
		// respective fields of the background
		BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		// Using the BorderPane method, setBackground(...), set the background of pane
		// to the titleBackground
		categoryPane.setBackground(new Background(new BackgroundImage(background, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, size)));

		categoryInstructions = new Text("Choose a category you would like to play.");
		categoryInstructions.setFont(Font.font(null, FontWeight.BOLD, 20));
		categoryInstructions.setFill(Color.AZURE);
		categoryInstructions.setStyle("-fx-stroke-width: 1; -fx-stroke: cornflowerblue;");
		
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

//		cat1 = new Button("Food");
//		cat1.setGraphic(category1);
		cat1.setOnAction(cat1Event); // goes to EventHandler for category1
//		cat2 = new Button("Animals");
//		cat2.setGraphic(category2);
		cat2.setOnAction(cat2Event); // goes to EventHandler for category2

//		cat3 = new Button("States");
//		cat3.setGraphic(category3);
		cat3.setOnAction(cat3Event); // goes to EventHandler for category3

		categorySelect = new HBox(3, cat1, cat2, cat3); // put pictures into HBox to view

		categoryPane.setCenter(categorySelect);
		categoryPane.setTop(categoryInstructions);

		return new Scene(categoryPane, 600, 600);
	}

	public Scene createClientGui() {
		BorderPane gameplayPane = new BorderPane();

		Image background = new Image("images/gameroom.png"); // Declare an object of type Image, titleBackground to hold the image based on the value of altLook
		
		ImageView hangmanView;
		if (c.gameInfo.guessesLeft == 6)
		{
			hangmanView = new ImageView(guessZero);
		}
		else if (c.gameInfo.guessesLeft == 5)
		{
			hangmanView = new ImageView(guessOne);
		}
		else if (c.gameInfo.guessesLeft == 4)
		{
			hangmanView = new ImageView(guessTwo);
		}
		else if (c.gameInfo.guessesLeft == 3)
		{
			hangmanView = new ImageView(guessThree);
		}
		else if (c.gameInfo.guessesLeft == 2)
		{
			hangmanView = new ImageView(guessFour);
		}
		else if (c.gameInfo.guessesLeft == 1)
		{
			hangmanView = new ImageView(guessFive);
		}
		else
		{
			hangmanView = new ImageView(guessSix);
		}
		// Declare and instantiate a BackgroundSize object, size, to set up the
		// respective fields of the background
		BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		// Using the BorderPane method, setBackground(...), set the background of pane
		// to the titleBackground
		gameplayPane.setBackground(new Background(new BackgroundImage(background, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, size)));

		categoryChoiceLabel = new Text("Your category: ");
		categoryChoiceLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
		categoryChoiceLabel.setFill(Color.AZURE);
		categoryChoiceLabel.setStyle("-fx-stroke-width: 1; -fx-stroke: cornflowerblue;");

		enterA_ZValue = new Text("Enter a character A-Z below!");
		playerGuessTextField = new TextField("");
		submitGuessButton = new Button("Submit Guess");
		enterA_ZValue.setFont(Font.font(null, FontWeight.BOLD, 20));
		enterA_ZValue.setFill(Color.AZURE);
		enterA_ZValue.setStyle("-fx-stroke-width: 1; -fx-stroke: cornflowerblue;");
		
		if (cat1Chosen) {
			categoryChoice = new Text("Food");
		} else if (cat2Chosen) {
			categoryChoice = new Text("Animals");
		} else if (cat3Chosen) {
			categoryChoice = new Text("States");
		} else {
			categoryChoice = new Text("<<CATEGORY HERE>>");
		}

//		String firstword = gameInfo.foodList.get(1);
//		System.out.println(firstword);

		submitGuessButton.setOnAction(e -> {
			playerGuess = playerGuessTextField.getText();

			if ((playerGuessesSet.contains(playerGuess))) {
				playerGuessTextField.setText("You have already guessed " + playerGuessTextField.getText() + "'");

			} else {

				playerGuess = playerGuessTextField.getText();
				playerGuessesSet.add(playerGuess);
				guesses.getItems().add(playerGuessTextField.getText());

				c.gameInfo.playerGuess = playerGuessTextField.getText().toUpperCase();
				c.send(c.gameInfo);
//				if (firstword.contains(playerGuess)) {
//					guesses.getItems().add(playerGuessTextField.getText() + " is a correct letter!");
//				}

			}
			playerGuessTextField.clear();

		});

		playerGuessTextField.setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				guessEnter = true;

				playerGuess = playerGuessTextField.getText();

				if ((playerGuessesSet.contains(playerGuess))) {
					playerGuessTextField.setText("You have already guessed " + playerGuessTextField.getText() + "'");

				} else {

					playerGuess = playerGuessTextField.getText();
					playerGuessesSet.add(playerGuess);
					guesses.getItems().add(playerGuessTextField.getText());

					c.gameInfo.playerGuess = playerGuessTextField.getText().toUpperCase();

					c.send(c.gameInfo);
//				if (firstword.contains(playerGuess)) {
//					guesses.getItems().add(playerGuessTextField.getText() + " is a correct letter!");
//				}

				}
			}
			playerGuessTextField.clear();
//			guessEnter = false;
		});
		hangmanView.setFitHeight(100);
		hangmanView.setFitWidth(100);
//		categoryChoice = new Text("<<CATEGORY HERE>>"); // can be updated based on the category chosen
		playerGuessLabel = new Text("You guessed the letters: ");
//		guesses = new ListView<String>();
		playerGuessLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
		playerGuessLabel.setFill(Color.AZURE);
		playerGuessLabel.setStyle("-fx-stroke-width: 1; -fx-stroke: cornflowerblue;");
		
		categoryBox = new HBox(3, categoryChoiceLabel, categoryChoice, hangmanView);
		guessBox = new HBox(3, playerGuessLabel, guesses);
		categoryAndGuessBox = new VBox(3, categoryBox, guessBox, enterA_ZValue, playerGuessTextField,
				submitGuessButton);

		gameplayPane.setLeft(categoryAndGuessBox);

		return new Scene(gameplayPane, 600, 600);
	}
	
	public Scene createContinueScreen()
	{
		BorderPane continuePane = new BorderPane();
		Image background = new Image("images/gameroom.png"); // Declare an object of type Image, titleBackground to hold the image based on the value of altLook
		
		// Declare and instantiate a BackgroundSize object, size, to set up the
		// respective fields of the background
		BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		
		// Using the BorderPane method, setBackground(...), set the background of pane
		// to the titleBackground
		continuePane.setBackground(new Background(new BackgroundImage(background, BackgroundRepeat.REPEAT,
		BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, size)));
		
		VBox decisionBox = new VBox(continueButton);
		
		continuePane.setCenter(decisionBox);
		
		return new Scene(continuePane, 600, 600);
	}

}