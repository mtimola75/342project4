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

// Names: John Cervantes, Mike Lazzar, Marc Timola, Krzysztof Para
//
// Description of File:
//
// The WordGuessClient.java file includes the main GUI application for the Client which allows the client to play
// the game of Word Guess. The game connects to a server and allows the user to choose a category from Food, Animals, and States.
// Each category has three words and the user must select one category to guess. If the user gets three words incorrect
// in one category, they lose the game and are given the option to play again or quit. Contrastingly, if the user guesses
// a word in all three categories within 6 guesses, then they win and are given the option to play again or quit.
//
// The background image used for the Client program is from the video game: "Club Penguin"
// The image was obtained in: https://clubpenguin.fandom.com/wiki/Book_Room
// The background image was resized to a dimension of 600x600 to fit the scene dimensions
//
// The hang man images were created by Marc Timola and used for the game screen where the client is guessing the word

// WordGuessClient: The WordGuessClient contains the widgets, scenes, and client connection to the server which allows
// the user to play the game of Word Guess
public class WordGuessClient extends Application {

	// IP & Port scene widgets
	Text portDirections, portLabel, ipLabel, welcomeToTheGame;
	TextField portInput, ipInput;
	HBox portBox, ipBox;
	VBox portIPBox;
	BorderPane portPane;
	
	// EventHandlers for the cat1, cat2, and cat3 Buttons for the category screen
	EventHandler<ActionEvent> cat1Event, cat2Event, cat3Event;
	
	// Buttons to allow the user to continue and play again or quit the game
	Button continueButton = new Button("Play Again");
	Button quitButton = new Button("Quit Game");

	// Category selection scene widgets
	Text categoryInstructions, category1Label, category2Label, category3Label;
	HBox categorySelect;
	Button cat1, cat2, cat3;

	// Category scene widgets
	Text categoryChoiceLabel, categoryChoice, playerGuessLabel;
	Image _hangman;
	ImageView hangman;
	ListView<String> guesses;
	HBox categoryBox, guessBox;
	VBox categoryAndGuessBox;

	// Game scene widgets to control the game screen, to hold the player guesses
	TextField playerGuessTextField;
	Set<String> playerGuessesSet = new HashSet<String>(Arrays.asList(""));
	String playerGuess;
	WordGuessInfo gameInfo = new WordGuessInfo();
	Text enterA_ZValue = new Text();
	
	// Client instance c, that is to be instantiated by successful input of IP and Port
	Client c;

	// TextField, serverInfo, to hold the information retrieved from the Server to display on game screen for client
	TextField serverInfo;

	// Boolean variables to control the flow of the program in terms of client connection to server and to keep track if a guess is entered
	boolean portEnter = false;
	boolean guessEnter = false;
	boolean ipEnter = false;
	
	// TextField variables, portNum and ipAddress, to take the user input of port number and IP address
	TextField portNum = new TextField();
	TextField ipAddress = new TextField();
	
	// PauseTransitions to control the flow of the game to allow the client to take in what is happening in the game
	PauseTransition failTran = new PauseTransition(Duration.seconds(2));
	PauseTransition returnToCategories = new PauseTransition(Duration.seconds(1.9));
	PauseTransition goToContinue = new PauseTransition(Duration.seconds(2.25));
	
	// Boolean variables to keep track of the client's category choices and whether the client guessed the categories correctly
	boolean cat1Chosen = false;
	boolean cat2Chosen = false;
	boolean cat3Chosen = false;
	boolean cat1Correct = false;
	boolean cat2Correct = false;
	boolean cat3Correct = false;
	
	// Continue screen widget, to display if the player has won or lost the game
	Text playerLoseOrWin;
	
	// Image objects to hold the Hang man images for all respective remaining guesses to display on the game screen for the client
	Image guessZero = new Image("images/hang0.png",600,600,false,true);
	Image guessOne = new Image("images/hang1.png",600,600,false,true);
	Image guessTwo = new Image("images/hang2.png",600,600,false,true);
	Image guessThree = new Image("images/hang3.png",600,600,false,true);
	Image guessFour = new Image("images/hang4.png",600,600,false,true);
	Image guessFive = new Image("images/hang5.png",600,600,false,true);
	Image guessSix = new Image("images/hang6.png",600,600,false,true);
	
	// Boolean variable to keep track if the client has connected to the server successfully, initially false
	boolean clientConnect = false;
	
	// main():
	//
	// The main(...) method is responsible in launching the client using launch(args)
	//
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	// start():
	//
	// The start(...) method is responsible in creating the GUI application for the Client side of the Word Guess game
	// in doing so, the method uses helper methods to creates Scenes and to transition the client screen
	//
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("(Client) Word Guess!!!"); // Set the primaryStage title to a specific title using setTitle(...)
		
		// Assign the guesses ListView to a new instance of ListView to hold Strings and to set the size of the ListView
		// using setMinSize(...) and setMaxSize(...) with dimensions 500x100
		guesses = new ListView<String>();
		guesses.setMinSize(300, 100);
		guesses.setMaxSize(300, 100);
		
		// Instantiate the Button variables: cat1, cat2, and cat3 to new Buttons with their respective category string passed into the constructor
		cat1 = new Button("Food");
		cat2 = new Button("Animals");
		cat3 = new Button("States");
		
		// Instantiate and initialize the respective Text object to the specific string and set
		// the font, fill, and style using setFont(), setFill(), and setStyle()
		this.welcomeToTheGame = new Text("~ The Game Of Word Guess ~");
		this.welcomeToTheGame.setFont(Font.font(null, FontWeight.BOLD, 35));
		this.welcomeToTheGame.setFill(Color.WHITE);
		this.welcomeToTheGame.setStyle("-fx-stroke-width: 1; -fx-stroke: black;");
		
		// Instantiate and initialize the respective Text object to the specific string and set
		// the font, fill, and style using setFont(), setFill(), and setStyle()
		this.portDirections = new Text("Input port number & IP Address");
		this.portDirections.setFont(Font.font(null, FontWeight.BOLD, 20));
		this.portDirections.setFill(Color.WHITE);
		this.portDirections.setStyle("-fx-stroke-width: 1; -fx-stroke: black;");

		// Instantiate and initialize the respective Text object to the specific string and set
		// the font, fill, and style using setFont(), setFill(), and setStyle()
		this.portLabel = new Text("Port number: ");
		this.portLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
		this.portLabel.setFill(Color.WHITE);
		this.portLabel.setStyle("-fx-stroke-width: 1; -fx-stroke: black;");

		// Instantiate and initialize the respective Text object to the specific string and set
		// the font, fill, and style using setFont(), setFill(), and setStyle()
		this.ipLabel = new Text("IP Address: ");
		this.ipLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
		this.ipLabel.setFill(Color.WHITE);
		this.ipLabel.setStyle("-fx-stroke-width: 1; -fx-stroke: black;");

		// Assign the portInput, serverInfo, and ipInput TextFields to a new instance of TextField
		this.portInput = new TextField();
		this.ipInput = new TextField();
		this.serverInfo = new TextField();
		
		// Set the serverInfo TextField to be disabled and with opacity 100 using respective functions
		serverInfo.setDisable(true);
		serverInfo.setOpacity(100);
		
		this.portBox = new HBox(8, portLabel, portInput); // HBox to hold the widgets pertaining to the Port input
		this.ipBox = new HBox(8, ipLabel, ipInput); // HBox to hold widgets pertaining to the IP Address input
		this.portIPBox = new VBox(10, welcomeToTheGame, portDirections, ipLabel, ipAddress, portLabel, portNum, serverInfo); // The portIPBox holds the GUI elements relevant to the menu screen where the IP and Port Number are entered

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

		
		// Define the EventHandler for returnToCategories PauseTransition
		// to update the fields of c.gameInfo, disable and enable widgets, and to reset widgets to prepare for new round
		// and also set the primaryStage scene to the result of createCategoryGui()
		returnToCategories.setOnFinished(e -> {
			// Check the booleans cat1Chosen, cat2Chosen, and cat3Chosen and check if the user got the category correct to disable the corresponding button
			if (cat1Chosen && c.gameInfo.foodAllCorrect) // Check if the corresponding boolean is true and if the c.gameInfo field corresponding to the category is true if so enter block
			{
				cat1.setDisable(true); // Disable cat1 using setDisable(...)
			} 
			else if (cat2Chosen && c.gameInfo.animalAllCorrect) // Else if the corresponding boolean is true and if the c.gameInfo field corresponding to the category is true if so enter block
			{
				cat2.setDisable(true); // Disable cat2 using setDisable(...)
			} 
			else if (cat3Chosen && c.gameInfo.stateAllCorrect) // Else if the corresponding boolean is true and if the c.gameInfo field corresponding to the category is true if so enter block
			{
				cat3.setDisable(true); // Disable cat3 using setDisable(...)
			}
			
			// Update fields in c.gameInfo to then later send to the server when a new category is chosen
			c.gameInfo.allCorrect = false;
			c.gameInfo.guessesLeft = 6;
			c.gameInfo.guessCorrect = false;
			c.gameInfo.clientProgressGuess = "";
			
			// Reset fields including the playerGuessesSet and the guesses ListView. Also re-enable the continueButton to allow user to continue again to another round
			playerGuessesSet.clear();
			continueButton.setDisable(false);
			guesses = new ListView<String>(); // Set guesses to a new instance of ListView to hold Strings
			
			// Set the guesses minimum and maximum size to 500x100 using respective methods
			guesses.setMinSize(300, 100);
			guesses.setMaxSize(300, 100);
			
			primaryStage.setScene(createCategoryGui()); // Set the primaryStage to the result of createCategoryGui()

		}); // end returnToCategories.setOnFinished(...)
		
		// Define the EventHandler for the goToContinue PauseTransition to set the primaryStage scene to the result of
		// createContinueScreen() when finished
		goToContinue.setOnFinished(e->{
			primaryStage.setScene(createContinueScreen());
		}); // end goToContinue.setOnFinished(...)
		
		// Disable the portNum button by using the setDisable(...) method and passing
		// true as a parameter
		portNum.setDisable(true);
		
		// Define the EventHandler for the portNum TextField for when the Enter key is
		// pressed, the EventHandler is important here as it instantiates the client
		// connection to the server and within it, it handles the GUI updates that taken place in the client
		//
		portNum.setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				portEnter = true;

				// Check if the ipEnter and portEnter booleans are both true, if so, enter the
				// block of code to instantiate the Client and attempt to connect to server
				if ((ipEnter == true) && (portEnter == true)) {

					// Declare the instance of Client, assigning it to c and passing the
					// Serializable consumer, ipAddress and portNum to the parameterized constructor
					c = new Client(client -> Platform.runLater(() -> {
						
						// If the callback string of the client is the corresponding message, the information provided is invalid, set fields
						// and request input from user again
						if (client.toString().contains("Error: Could not connect, invalid ip or port")) 
						{
							// Set the text and style of serverInfo to display the error encountered using setText(...) and setStyle(...)
							serverInfo.setText("Error: Could not connect, invalid ip or port");
							serverInfo.setStyle("-fx-background-color: hotpink;");
							
							failTran.play(); // Play the failTran PauseTransition
							
							// Clear the respective TextFields portNum and ipAddress and reset ipEnter and portEnter booleans to allow re-input from user
							portNum.clear();
							portNum.setDisable(true);
							ipAddress.clear();
							ipAddress.setDisable(false);
							ipEnter = false;
							portEnter = false;
						} 
						else // Else the client has successfully connected to the server, set the corresponding fields and now the client will listen for and send the gameInfo object to the server
						{
							clientConnect = true; // As connection was successful, set clientConnect to true
							primaryStage.setScene(createCategoryGui()); // Set the scene of the primaryStage to the result of createCategoryGui()
							serverInfo.setStyle("-fx-background-color: lightgreen;"); // Set serverInfo to light green background using setStyle(...)
							serverInfo.setText("Connection successful, continuing to category selection..."); // Set the text of serverInfo to a specific success message using setText(...)
							
							// Disable portNum and ipAddress when connection is successful using
							// setDisable(...) method
							portNum.setDisable(true);
							ipAddress.setDisable(true);
							
							// With the server communication, the client will receive the game object c.gameInfo. Using the c.gameInfo
							// check if the gameStatus is a specific value and update the GUI accordingly in response to the gameStatus and other field values
							if (c.gameInfo.gameStatus == 2) // Check if the c.gameInfo gameStatus field is equal to 2, if so enter block
							{
								// Check if the c.gameInfo field allCorrect and the boolean guessEnter are true, if so enter block
								if (c.gameInfo.allCorrect && guessEnter)
								{
									// Check what category the client has chosen and assign the corresponding boolean to true as they got the category correct
									if (cat1Chosen) // If the category one was chosen that is cat1Chosen is true, enter the block
									{
										cat1Correct = true; // Set the corresponding cat1Correct boolean to true
									} 
									else if (cat2Chosen) // Else if the category two was chosen that is cat2Chosen is true, enter the block
									{
										cat2Correct = true; // Set the corresponding cat2Correct boolean to true
									} 
									else if (cat3Chosen) // Else if the category three was chosen that is cat3Chosen is true, enter the block
									{
										cat3Correct = true; // Set the corresponding cat3Correct boolean to true
									}
									
									guesses.getItems().add("You have correctly guessed the word: "
											+ c.gameInfo.clientProgressGuess + "!"); // Update the ListView guesses accordingly using the getItems() method and the add() method
									guessEnter = false; // Reset the boolean value of guessEnter to false
								} 
								else if (c.gameInfo.guessCorrect && guessEnter) // Else if the c.gameInfo field guessCorrect is true and guessEnter is true, enter block
								{
									guesses.getItems().add("Correct guess, keep going! Updated String: "
											+ c.gameInfo.clientProgressGuess); // Update the ListView guesses accordingly using the getItems() method and the add() method
									guessEnter = false; // Reset the boolean value of guessEnter to false
								} 
								else if (!c.gameInfo.guessCorrect && guessEnter) // Else if the c.gameInfo field guessCorrect is false and guessEnter is true, enter block
								{
									guesses.getItems().add("Incorrect guess, try again! Updated String: "
											+ c.gameInfo.clientProgressGuess); // Update the ListView guesses accordingly using the getItems() method and the add() method
									guessEnter = false; // Reset the boolean value of guessEnter to false
								}
								
								primaryStage.setScene(createGameGui()); // Set the primaryStage scene to the resulting scene of createGameGui() using setScene()
								
								// Check the endgame conditions for the client's game, that is, check if the guessesLeft is equal to 0 or if c.gameInfo allCorrect is true
								if ((c.gameInfo.guessesLeft == 0) || (c.gameInfo.allCorrect == true)) // If the c.gameInfo fields guessesLeft and allCorrect meet the condition of being 0 or true (respectively) enter block
								{
									if ((cat1Correct && cat2Correct && cat3Correct) || (c.gameInfo.foodFail == true)
											|| (c.gameInfo.animalFail == true) || (c.gameInfo.stateFail == true)) // If the client booleans cat1Correct, cat2Correct, and cat3Correct are true, enter block or if any category has been failed (that is the fail boolean is true) enter block
									{
										playerGuessTextField.setDisable(true);
										goToContinue.play(); // Play the goToContinue PauseTransition using play()
									} 
									else  // Else no end conditions have been met yet, enter the block of code
									{
										playerGuessTextField.setDisable(true); // Re-enable the playerGuessTextField using setDisable(true)
										returnToCategories.play(); // Play the returnToCategories PauseTransition using play()
									}
								}
								
							} // end outer if()	
						}
						
					}), Integer.parseInt(portNum.getText().toString()), ipAddress.getText().toString());

					// Check if the clientConnect boolean is true, if so, enter the block to update the primaryStage to display the category GUI
					if (clientConnect)
					{
						primaryStage.setScene(createCategoryGui()); // Set the primaryStage to the resulting scene of createCategoryGui() using setScene(...)
					}
					
					// Set ipEnter and portEnter to a value of false
					ipEnter = false;
					portEnter = false;

					c.start(); // Start the client connection using the start() method
				}

			} // end if(...)
		}); // end portNum.setOnKeyPressed(....)

		// Define the EventHandler for continueButton to adjust boolean values, reset the gameInfo fields to enable the user to start another game
		continueButton.setOnAction(e->{
			
			continueButton.setDisable(true); // Disable the continueButton to disallow repeated press using setDisable()
			
			// Set booleans regarding the category chosen and correct categories to false
			cat1Chosen = false;
			cat2Chosen = false;
			cat3Chosen = false;
			cat1Correct = false;
			cat2Correct = false;
			cat3Correct = false;
			
			// Re-enable buttons cat1, cat2, and cat3 using setDisable(...)
			cat1.setDisable(false);
			cat2.setDisable(false);
			cat3.setDisable(false);
			
			// Re-enable the playerGuessTextField using setDisable(...)
			playerGuessTextField.setDisable(false);
			
			// Reset fields in the c.gameInfo game object to initial values to allow the beginning of a new game
			c.gameInfo.gameStatus = 0;
			c.gameInfo.allCorrect = false;
			c.gameInfo.foodAllCorrect = false;
			c.gameInfo.animalAllCorrect = false;
			c.gameInfo.stateAllCorrect = false;
			c.gameInfo.message = "";
			
			c.send(c.gameInfo); // Send the c.gameInfo object to the Server using send(...)
			returnToCategories.play(); // Play the returnToCategories PauseTransition using play()
			
		}); // end continueButton.setOnAction(...)

		// cat1Event:
		//
		// Define the EventHandler for cat1Event to update c.gameInfo fields and update fields including booleans and Text, and send the c.gameInfo object to the Server and update primaryStage
		// to the result of createGameGui()
		//
		cat1Event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				// Set the fields of c.gameInfo respective to the category choice (Food) and reassign the values of cat1Chosen, cat2Chosen, and cat3Chosen
				c.gameInfo.message = "Food";
				c.gameInfo.gameStatus = 1;
				cat1Chosen = true;
				cat2Chosen = false;
				cat3Chosen = false;
				
				categoryChoice = new Text("Food"); // Set categoryChoice to a new instance of Text with the string Food passed

				c.send(c.gameInfo); // Send the c.gameInfo object with updated values to the Server using send()
				
				primaryStage.setScene(createGameGui()); // Set the primaryStage scene to the result of createGameGui() using setScene()
			}
		}; // end cat1Event
		
		// cat2Event:
		//
		// Define the EventHandler for cat2Event to update c.gameInfo fields and update fields including booleans and Text, and send the c.gameInfo object to the Server and update primaryStage
		// to the result of createGameGui()
		//
		cat2Event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				// Set the fields of c.gameInfo respective to the category choice (Animals) and reassign the values of cat1Chosen, cat2Chosen, and cat3Chosen
				c.gameInfo.message = "Animals";
				c.gameInfo.gameStatus = 1;
				cat1Chosen = false;
				cat2Chosen = true;
				cat3Chosen = false;
				
				categoryChoice = new Text("Animals"); // Set categoryChoice to a new instance of Text with the string Animals passed

				c.send(c.gameInfo); // Send the c.gameInfo object with updated values to the Server using send()
				
				primaryStage.setScene(createGameGui()); // Set the primaryStage scene to the result of createGameGui() using setScene()
			}
		}; // end cat2Event

		// cat3Event:
		//
		// Define the EventHandler for cat3Event to update c.gameInfo fields and update fields including booleans and Text, and send the c.gameInfo object to the Server and update primaryStage
		// to the result of createGameGui()
		//
		cat3Event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				
				// Set the fields of c.gameInfo respective to the category choice (States) and reassign the values of cat1Chosen, cat2Chosen, and cat3Chosen
				c.gameInfo.message = "States";
				c.gameInfo.gameStatus = 1;
				cat1Chosen = false;
				cat2Chosen = false;
				cat3Chosen = true;
				
				categoryChoice = new Text("States"); // Set categoryChoice to a new instance of Text with the string States passed

				c.send(c.gameInfo); // Send the c.gameInfo object with updated values to the Server using send()
				primaryStage.setScene(createGameGui()); // Set the primaryStage scene to the result of createGameGui() using setScene()
			}
		}; // end cat3Event

		// Define the EventHandler for quitButton to close the primaryStage and exit from the program using primaryStage.close() 
		// and System.exit(0) when the button is pressed
		quitButton.setOnAction(e->{
			primaryStage.close(); // Close primaryStage using close()
			System.exit(0); // Exit from the System using exit(0)
		}); // end quitButton.setOnAction(...)
		
		primaryStage.setScene(createMenuGui()); // Set the scene of primaryStage to the result of createMenuGui() using setScene(...)
		primaryStage.show(); // Show the primaryStage using the show() method
		
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

		return new Scene(portPane, 600, 600);
	}
	
	// createCategoryGui():
	//
	// The createCategoryGui() method is of return type Scene and creates the Scene for the category screen (Where the user is choosing the category) in the client game
	// that displays the respective background and widgets for the user to interact with. The returned Scene displays the main BorderPane
	// with dimensions 600x600
	//
	public Scene createCategoryGui() {
		BorderPane categoryPane = new BorderPane(); // Instantiate a new instance of BorderPane and assign it to categoryPane to hold the widgets of the GUI scene
		
		categoryPane.setPadding(new Insets(30)); // Set the padding of categoryPane using setPadding(...)

		Image background = new Image("images/gameroom.png"); // Declare an object of type Image, titleBackground to hold the image based on the value of altLook
		
		// Declare and instantiate a BackgroundSize object, size, to set up the
		// respective fields of the background
		BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		
		// Using the BorderPane method, setBackground(...), set the background of pane to the titleBackground
		categoryPane.setBackground(new Background(new BackgroundImage(background, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, size)));

		// Initialize and style the Text object, categoryInstructions using the methods setFont(...), setFill(...), and setStyle(...)
		categoryInstructions = new Text("Choose a category you would like to play:");
		categoryInstructions.setFont(Font.font(null, FontWeight.BOLD, 20));
		categoryInstructions.setFill(Color.WHITE);
		categoryInstructions.setStyle("-fx-stroke-width: 1; -fx-stroke: black;");
		
		categoryInstructions.setTextAlignment(TextAlignment.CENTER); // Set the alignment of the categoryInstructors to the center using setTextAlignment()

		cat1.setOnAction(cat1Event); // Assign the cat1 button to the EventHandler cat1Event when it is clicked (setOnAction)
		
		cat2.setOnAction(cat2Event); // Assign the cat2 button to the EventHandler cat2Event when it is clicked (setOnAction)
		
		cat3.setOnAction(cat3Event); // Assign the cat3 button to the EventHandler cat3Event when it is clicked (setOnAction)

		// Assign categorySelect to a new instance of HBox to hold the widgets (the buttons for each category) 
		categorySelect = new HBox(5, cat1, cat2, cat3);

		categoryPane.setCenter(categorySelect); // Set the categorySelect HBox to the center of categoryPane using setCenter(...)
		categoryPane.setTop(categoryInstructions); // Set the categoryInstructions to the top of categoryPane using setTop(...)

		return new Scene(categoryPane, 600, 600); // Return a new instance of Scene displaying categoryPane with dimensions 600x600
	} // end createCategoryGui()
	
	// createGameGui():
	//
	// The createGameGui() method is of return type Scene and creates the Scene for the game screen (Where the user is inputting guesses) in the client game
	// that displays the respective background and widgets for the user to interact with. The returned Scene displays the main BorderPane
	// with dimensions 600x600
	//
	public Scene createGameGui() {
		BorderPane gameplayPane = new BorderPane();

		Image background = new Image("images/gameroom.png"); // Declare an object of type Image, titleBackground to hold the image based on the value of altLook
		
		ImageView hangmanView; // ImageView variable, hangmanView to hold the respective image for the amount of guesses remaining to display the hangman progress
		
		// Check the c.gameInfo member guessesLeft and update the hangmanView variable to a new instance of ImageView based on the guessesLeft
		//
		if (c.gameInfo.guessesLeft == 6) // If the guessesLeft member is 6, then set the hangmanView accordingly
		{
			hangmanView = new ImageView(guessZero); // Set hangmanView to a new instance of ImageView passing the corresponding Image
		}
		else if (c.gameInfo.guessesLeft == 5) // Else if the guessesLeft member is 5, then set the hangmanView accordingly
		{
			hangmanView = new ImageView(guessOne); // Set hangmanView to a new instance of ImageView passing the corresponding Image
		}
		else if (c.gameInfo.guessesLeft == 4) // Else if the guessesLeft member is 4, then set the hangmanView accordingly
		{
			hangmanView = new ImageView(guessTwo); // Set hangmanView to a new instance of ImageView passing the corresponding Image
		}
		else if (c.gameInfo.guessesLeft == 3) // Else if the guessesLeft member is 3, then set the hangmanView accordingly
		{
			hangmanView = new ImageView(guessThree); // Set hangmanView to a new instance of ImageView passing the corresponding Image
		}
		else if (c.gameInfo.guessesLeft == 2) // Else if the guessesLeft member is 2, then set the hangmanView accordingly
		{
			hangmanView = new ImageView(guessFour); // Set hangmanView to a new instance of ImageView passing the corresponding Image
		}
		else if (c.gameInfo.guessesLeft == 1) // Else if the guessesLeft member is 1, then set the hangmanView accordingly
		{
			hangmanView = new ImageView(guessFive); // Set hangmanView to a new instance of ImageView passing the corresponding Image
		}
		else // Else the guessesLeft member is 0, set the hangmanView accordingly
		{
			hangmanView = new ImageView(guessSix); // Set hangmanView to a new instance of ImageView passing the corresponding Image
		}
		
		// Declare and instantiate a BackgroundSize object, size, to set up the respective fields of the background
		BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		
		// Using the BorderPane method, setBackground(...), set the background of pane to the background variable
		gameplayPane.setBackground(new Background(new BackgroundImage(background, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, size)));

		// Assign the Text variable to a specific new instance of Text and set the font, fill and style using setFont(...), setFill(...), and setStyle(...)
		categoryChoiceLabel = new Text("Your category: ");
		categoryChoiceLabel.setFont(Font.font(null, FontWeight.BOLD, 19));
		categoryChoiceLabel.setFill(Color.WHITE);
		categoryChoiceLabel.setStyle("-fx-stroke-width: 1; -fx-stroke: black;");
		
		// Assign the Text variable to a specific new instance of Text and set the font, fill and style using setFont(...), setFill(...), and setStyle(...)
		enterA_ZValue = new Text("Press enter to submit a character guess A-Z below:");
		enterA_ZValue.setFont(Font.font(null, FontWeight.BOLD, 19));
		enterA_ZValue.setFill(Color.WHITE);
		enterA_ZValue.setStyle("-fx-stroke-width: 1; -fx-stroke: black;");
		
		// Instantiate a new instance of TextField and assign it to playerGuessTextField
		playerGuessTextField = new TextField("");
		playerGuessTextField.setMinSize(200, 30);
		playerGuessTextField.setMaxSize(200, 30);
		
		// Check what category the client has chosen, based on the category, categoryChoice is assigned to a specific Text object
		if (cat1Chosen) // If the cat1Chosen boolean is true, enter block
		{
			categoryChoice = new Text("Food"); // Assign categoryChoice to a new instance of Text with the specific category passed
		} else if (cat2Chosen) // Else if the cat2Chosen boolean is true, enter block
		{
			categoryChoice = new Text("Animals"); // Assign categoryChoice to a new instance of Text with the specific category passed
		} else if (cat3Chosen) // Else if the cat3Chosen boolean is true, enter block
		{
			categoryChoice = new Text("States"); // Assign categoryChoice to a new instance of Text with the specific category passed
		} 
		else  // Else no category boolean is true, enter block (Precaution condition) There should always be a category chosen
		{
			categoryChoice = new Text("<<CATEGORY HERE>>"); // Assign categoryChoice to a new instance of Text with a filler message passed
		}
		
		// Style the categoryChoice Text using setFont(...), setFill(...), and setStyle(...)
		categoryChoice.setFont(Font.font(null, FontWeight.BOLD, 19));
		categoryChoice.setFill(Color.WHITE);
		categoryChoice.setStyle("-fx-stroke-width: 1; -fx-stroke: black;");
		
		// playerGuessTextField:
		//
		// Define the EventHandler for playerGuessTextfield to handle if the user presses ENTER to submit the guess entered and send the
		// updated gameInfo object to the server
		//
		playerGuessTextField.setOnKeyPressed(e -> {
			// Get the user action using getCode() and Check if the user KeyCode is equal to ENTER, if so enter block
			if (e.getCode().equals(KeyCode.ENTER)) 
			{	
				guessEnter = true; // Set guessEnter to true

				playerGuess = playerGuessTextField.getText(); // Store the playerGuessTextField text into playerGuess using getText()

				// Check if the playerGuess has already been sent once before using the Set contains() method, if so, enter block
				if ((playerGuessesSet.contains(playerGuess))) {
					guesses.getItems().add("You have already guessed: " + playerGuessTextField.getText()); // Update the listView guesses with a message to the user
				} 
				else // Else the playerGuess is unique to the round and the guess is new, enter block
				{
					playerGuess = playerGuessTextField.getText(); // Set playerGuess to the text held in playerGuessTextField using getText
					playerGuessesSet.add(playerGuess); // Add the playerGuess to playerGuessesSet using the add() method
					guesses.getItems().add("You guessed: " + playerGuessTextField.getText()); // Update the guesses ListView using getItems() and add(...) to show the guess

					c.gameInfo.playerGuess = playerGuessTextField.getText().toUpperCase(); // Set c.gameInfo.playerGuess to the text held in playerGuessTextField using getText() and make it upper case using toUpperCase()

					c.send(c.gameInfo); // Send c.gameInfo to the server using the send() method
				}
			} // end if(...)
			
			playerGuessTextField.clear(); // Clear the playerGuessTextField using clear()
		}); // end playerGuessTextField.setOnKeyPressed(...)
		
		// Set the dimensions of the ImageView hangmanView using setFitHeight() and setFitWidth()
		hangmanView.setFitHeight(250);
		hangmanView.setFitWidth(250);
		
		// Initialize playerGuessLabel to a specific string and set the font, fill, and style using setFont(...), setFill(...) and setStyle(...)
		playerGuessLabel = new Text("Game Information: "); 
		playerGuessLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
		playerGuessLabel.setFill(Color.WHITE);
		playerGuessLabel.setStyle("-fx-stroke-width: 1; -fx-stroke: black;");
		
		// Initialize the HBox objects, categoryBox and guessBox to hold specific widgets with a spacing of 3
		categoryBox = new HBox(3, categoryChoiceLabel, categoryChoice, hangmanView);
		guessBox = new HBox(3, playerGuessLabel, guesses);
		
		// Text to show the player how many guesses they have left until they lose that round.
		// If player is correct then it would not decrement otherwise it will go down until the player hits -1 and force end the game.
		// This is just to make the player not lose count and be able to plan accordingly in the game.
		//
		// Initialize playerGuessesLeft and set the text, font, fill, and style using setText(...), setFont(...), setFill(...), and setStyle(...)
		Text playerGuessesLeft = new Text();
		playerGuessesLeft.setText("Guesses Left: " + c.gameInfo.guessesLeft);
		playerGuessesLeft.setFont(Font.font(null, FontWeight.BOLD, 22));
		playerGuessesLeft.setFill(Color.WHITE);
		playerGuessesLeft.setStyle("-fx-stroke-width: 1; -fx-stroke: black;");

		categoryAndGuessBox = new VBox(3, categoryBox, guessBox, enterA_ZValue, playerGuessTextField, playerGuessesLeft); // Initialize the categoryAndGuessBox to hold the widgets to display on the BorderPane gameplayPane

		gameplayPane.setLeft(categoryAndGuessBox); // Set the left of the gamePlane to categoryAndGuessBox using setLect(...)

		return new Scene(gameplayPane, 600, 600); // Return an instance of Scene displaying gameplayPane with dimensions 600x600
	} // end createGameGui()
	
	// createContinueScreen():
	//
	// The createContinueScreen() method is of return type Scene and creates the Scene for the continue screen in the client game
	// that displays the respective background and widgets for the user to interact with. The returned Scene displays the main BorderPane
	// with dimensions 600x600
	//
	public Scene createContinueScreen()
	{
		BorderPane continuePane = new BorderPane(); // Instantiate a new instance of BorderPane, continuePane to hold the GUI elements of the scene
		Image background = new Image("images/gameroom.png"); // Declare an object of type Image, titleBackground to hold the image based on the value of altLook
		
		// Declare and instantiate a BackgroundSize object, size, to set up the respective fields of the background
		BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		
		// Using the BorderPane method, setBackground(...), set the background of pane to background
		continuePane.setBackground(new Background(new BackgroundImage(background, BackgroundRepeat.REPEAT,
		BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, size)));
		//Text to tell the player if they won or not using if else statements
		playerLoseOrWin = new Text();
		playerLoseOrWin.setFont(Font.font(null, FontWeight.BOLD, 30));
		playerLoseOrWin.setFill(Color.WHITE);
		playerLoseOrWin.setStyle("-fx-stroke-width: 1; -fx-stroke: black;");
		
		// Check if the gameInfo booleans, stateFail, animalFail, or foodFail are true if so the player lost, enter block
		if(c.gameInfo.stateFail == true || c.gameInfo.animalFail == true || c.gameInfo.foodFail == true) 
		{
			playerLoseOrWin.setText("You Lose!\nChoose to Play Again or Quit"); // Set the playerLoseOrWin text to a lose message
		}
		else // Else the player has won, enter block to display the appropriate message
		{
			playerLoseOrWin.setText("You Win!\nChoose to Play Again or Quit"); // Set the playerLoseOrWin text to a win message
		}
		
		VBox decisionBox = new VBox(5, playerLoseOrWin, continueButton, quitButton); // Instantiate VBox, decisionBox, to hold the respective GUI widgets
		
		continuePane.setCenter(decisionBox); // Set the decisionBox to the center of continuePane using setCenter()
		
		return new Scene(continuePane, 600, 600); // Return a new instance of Scene displaying continuePane and with dimensions 600x600
	}

}