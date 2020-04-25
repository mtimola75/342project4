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
import javafx.scene.input.MouseEvent;
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

	static final int categoryHeight = 300;
	static final int categoryWidth = 200;

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

		this.portBox = new HBox(8, portLabel, portInput); // port box
		this.ipBox = new HBox(8, ipLabel, ipInput); // ip box
		this.portIPBox = new VBox(10, portDirections, portBox, ipBox); // holds both boxes

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

		// create scene for port and ip window
		portPane = new BorderPane();
		portPane.setPadding(new Insets(30));
		portPane.setCenter(portIPBox);
		portPane.setRight(connectButton);

		Scene portScene = new Scene(portPane, 400, 170); // INITIAL SCENE; not put in sceneMap since it will start here
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
		primaryStage.setScene(test); // change to "setScene(portScene)" to start from inputting IP address; if
										// anything else, it is for testing purposes
		primaryStage.show();
	} // end start()

	public Scene createCategoryGui() {
		BorderPane categoryPane = new BorderPane();
		categoryPane.setPadding(new Insets(30));

		categoryInstructions = new Text("Choose a category you would like to play.");
		categoryInstructions.setTextAlignment(TextAlignment.CENTER);
		// init pics
		_category1 = new Image("cat1.jpg");
		_category2 = new Image("cat2.jpg");
		_category3 = new Image("cat3.jpg");

		category1 = new ImageView(_category1);
		category2 = new ImageView(_category2);
		category3 = new ImageView(_category3);

		// sizing
		category1.setFitHeight(categoryHeight);
		category1.setFitWidth(categoryWidth);
		category1.setPreserveRatio(true);

		category2.setFitHeight(categoryHeight);
		category2.setFitWidth(categoryWidth);
		category2.setPreserveRatio(true);

		category3.setFitHeight(categoryHeight);
		category3.setFitWidth(categoryWidth);
		category3.setPreserveRatio(true);

		cat1 = new Button("Category 1");
		cat1.setGraphic(category1);
		cat1.setOnAction(cat1Event);  //goes to EventHandler for category1
		
		cat2 = new Button("Category 2");
		cat2.setGraphic(category2);
		cat2.setOnAction(cat2Event);  //goes to EventHandler for category2
		
		cat3 = new Button("Category 3");
		cat3.setGraphic(category3);
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
