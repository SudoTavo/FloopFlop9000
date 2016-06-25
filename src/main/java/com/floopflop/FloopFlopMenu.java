package com.floopflop;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/**
 *
 *
 * @author Tavo Loaiza
 */
class FloopFlopMenu {

	public static final String TITLE = "FloopFlop9000 Menu";
	public static final int KEY_INPUT_SPEED = 5;
	private static final double ROTATE_SPEED = 0.3;
	private int optionNum;
	private Scene myScene;
	private ImageView myBot;
	private ImageView back;
	private Rectangle myHighlight;
	private Font headerFont = Font.font( "Eras Bold ITC", FontWeight.BOLD, 100 );
	private Font optionFont = Font.font( "Digital Sans EF", FontWeight.BOLD, 70 );
	private static final String BACKGROUND_FILENAME = "/menuBackground.png";
	public BooleanProperty optionSelected = new SimpleBooleanProperty(false);
	private Button classic;
	private Button endless;
	private Button quit;
	private Button options;
	private FloopFlop app;
	//   private AudioPlayer music;

	public FloopFlopMenu(FloopFlop FloopFlop) {
		app = FloopFlop;
	}

	/**
	 * Returns name of the game.
	 */

	public int getOptionNum(){
		return optionNum;
	}

	public String getTitle () {
		return TITLE;
	}

	/**
	 * Create the game's scene
	 */
	public Scene init (int width, int height) {
		optionNum = 0;

		Group root = new Group();

		myScene = new Scene(root, width, height, Color.WHITE);

		Image backImage = new Image(FloopFlop.class.getResourceAsStream(BACKGROUND_FILENAME));
		back = new ImageView(backImage);
		back.setY(0);
		root.getChildren().add(back);

		Image image = new Image(FloopFlop.class.getResourceAsStream("/FloopFlop9000XXXL.png"));
		myBot = new ImageView(image);
		myBot.setPreserveRatio(true);
		myBot.setFitWidth(app.getSizeX()/2);

		if(app.getSizeX()<800)
			headerFont = Font.font( "Eras Bold ITC", FontWeight.BOLD, 50 );

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(app.getSizeX()/20);
		grid.setVgap(app.getSizeY()/20);
		grid.setPadding(new Insets(app.getSizeY()/10, app.getSizeX()/10, app.getSizeY()/10, app.getSizeX()/10));

		//      myHighlight = initHighlight();
		Canvas canvas = new Canvas(app.getSizeX(),app.getSizeY());
		root.getChildren().add(canvas);
		//	root.getChildren().add(myHighlight);

		addButtons(grid);
		root.getChildren().add(grid);
		initMenuText(canvas);

		myBot.setX(width / 1.3 - myBot.getBoundsInLocal().getWidth() / 2);
		myBot.setY(height / 2  - myBot.getBoundsInLocal().getHeight() / 2);
		// x and y represent the top left corner, so center i
		// order added to the group is the order in which they are drawn
		root.getChildren().add(myBot);

		// Respond to input
		myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		//   myScene.setOnMouseClicked(value);;

		return myScene;
	}


	private Button buttonFactory(String text, double scale){
		Button button = new Button(text);
		button.setOnAction(e -> handleButtonAction(e));
		button.setScaleX(scale);
		button.setScaleY(scale);

		return button;
	}

	private void handleButtonAction(ActionEvent event){
		if(event.getSource()==classic){
			app.startGame(false);
		}
		if(event.getSource()==endless){
			app.startGame(true);
		}
		if(event.getSource()==quit){
			app.quit();
		}
		if(event.getSource()==options){
			app.startMenu();;
		}
	}


	public void addButtons(GridPane grid){

		double scale = app.getSizeX()/1000;
		if(app.getSizeX()<800)
			scale = 1;

		classic = buttonFactory("Start Classic",scale);
		endless = buttonFactory("Start Endless",scale);
		quit = buttonFactory("Quit",scale);
		options = buttonFactory("Restart",scale);

		grid.add(classic,0,1);
		grid.add(endless,1,1);
		grid.add(quit,0,2);
		grid.add(options,1,2);
	}

	/**
	 * Change properties of shapes to animate them
	 *
	 * Note, there are more sophisticated ways to animate shapes,
	 * but these simple ways work too.
	 */
	public void step (double elapsedTime) {
		// update attributes
		myBot.setRotate(myBot.getRotate() - ROTATE_SPEED);

	}


	// What to do each time a key is pressed
	private void handleKeyInput (KeyCode code) {
		switch (code) {
		case RIGHT:
			myHighlight.setTranslateY(0);
			myHighlight.setTranslateX(450);
			optionNum = 1;
			break;
		case LEFT:
			myHighlight.setTranslateY(0);
			myHighlight.setTranslateX(0);
			optionNum = 0;
			break;
		case UP:
			myHighlight.setTranslateY(0);
			myHighlight.setTranslateX(0);
			optionNum = 0;
			break;
		case DOWN:
			myHighlight.setTranslateY(100);
			myHighlight.setTranslateX(200);
			optionNum = 2;
			break;
		case ENTER:
			optionSelected.set(true);
			break;

		default:
			// do nothing
		}
	}

	private Rectangle initHighlight()
	{
		Rectangle hlight = new Rectangle(40,230,380,100);
		hlight.opacityProperty().set(0.4);
		hlight.setArcHeight(15);
		hlight.setArcWidth(15);
		hlight.setFill(Color.BLUE);
		hlight.setStroke(Color.BLUE);
		return hlight;
	}




	private void initMenuText(Canvas canvas)
	{

		//A button with the specified text caption.


		GraphicsContext ops = canvas.getGraphicsContext2D();

		ops.setFill( Color.RED );
		ops.setStroke( Color.BLACK );
		ops.setLineWidth(2);
		ops.setFont( headerFont );
		ops.fillText( "FloopFlop9000", app.getSizeX()*0.05, app.getSizeY()*0.1 );
		ops.strokeText( "FloopFlop9000", app.getSizeX()*0.05, app.getSizeY()*0.1 );

		/*
	    ops.setLineWidth(2);
	    ops.setFont( optionFont );
	    ops.fillText( "Start Classic", 50, 300 );
	    ops.strokeText( "Start Classic", 50, 300 );
	    ops.fillText( "Start Endless", 500, 300 );
	    ops.strokeText( "Start Endless", 500, 300 );

	    ops.fillText( "Quit game", 250, 400 );
	    ops.strokeText( "Quit game", 250, 400 );
		 */
	}

}
