package com.floopflop;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import javafx.animation.RotateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 *
 *
 * @author Tavo Loaiza
 */
class FloopFlopGame {
	private int SIZE_X;
    private int SIZE_Y;
    public static final String TITLE = "FloopFlop9000";
    public int KEY_INPUT_SPEED = 25;
    private int OBSTACLE_SPEED = 10;
    private int RAM_SPEED = 20;
    private int OBSTACLE_GEN_RATE = 81;
    private static final int RAM_GEN_RATE = 101;
    private static final String BOT_FILENAME = "/FloopFlop9000.png";
    private static final String BACKGROUND_FILENAME = "/background.png";
    private static final String[] OBSTACLE_FILENAMES = {"/gear.png","/cabinet.png","/fridge.png","/laptop.png","/printer.png","/bin.png"};
    private static final String RAM_FILENAME = "/ram.png";
    private static final int NUM_OF_LIVES = 5;
    private Font bigFont = Font.font( "Digital Sans EF", FontWeight.BOLD, 100 );
    private Font smallFont = Font.font( "Digital Sans EF", FontWeight.BOLD, 50 );
    private double botVel;
    private Stage primaryStage;
    private Group root;
    private Scene myScene;
    private ImageView myBot;
    ImageView background1;
    ImageView background2;
    private MediaPlayer mediaPlayer;
    private ArrayList<ImageView> obstacles;
    private ArrayList<Image> obstacleSet;
    private ArrayList<ImageView> ram;
    private Image ramImage;
    private ArrayList<ImageView> toRemove;
    private long score;
    private long startTime;
    private long currentTime;
    private final long DURATION = 130000;
    private int lives;
    private GraphicsContext text;
    private Canvas canvas;
    private boolean endless;
    private boolean winState;
    private boolean isGameOver;
    private boolean cheatsOn;
    private int incr = 10;
    private int lastGen = 0;
    private double scale = 1;
    private FloopFlop app;
    public BooleanProperty gameQuit = new SimpleBooleanProperty(false);
    private boolean mobile;
    public FloopFlopGame(FloopFlop appl) {
		primaryStage = appl.getMyStage();
		app = appl;
		SIZE_X = app.getSizeX();
		SIZE_Y = app.getSizeY();
		if(SIZE_X<800){
			scale = 0.4;
			mobile = true;
			RAM_SPEED = 10;
			OBSTACLE_SPEED = 4;
			KEY_INPUT_SPEED = 15;
			bigFont = Font.font( "Digital Sans EF", FontWeight.BOLD, 35 );
        	smallFont = Font.font( "Digital Sans EF", FontWeight.BOLD, 20 );
		}
		else
			mobile = false;

	}

	/**
     * Returns name of the game.
     */
    public String getTitle () {
        return TITLE;
    }

    /**
     * Create the game's scene
     */
    public Scene init (int width, int height) {
    	root = new Group();
        myScene = new Scene(root, width, height, Color.valueOf("222022") );

        initBackground();
        initVar();
        initBot();
        initObstacle();
        initRam();
        initText();
        // Respond to input
        myScene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));
        myScene.setOnKeyReleased(e -> handleKeyRelease(e.getCode()));
        myScene.setOnMousePressed(e -> handleMousePress(e.getSceneY()));
        myScene.setOnMouseReleased(e -> handleMouseRelease(e.getSceneY()));

        app.playMusic();

        return myScene;

    }
    private void initVar(){
    	endless = false;
        winState = false;
        isGameOver = false;
        toRemove = new ArrayList<ImageView>();
        score = 0;
        startTime = System.currentTimeMillis();;
    }
    public void setEndless(boolean isEndless){
    	endless = isEndless;
    }
    public void step (double elapsedTime) {
    	if(!isGameOver && !winState){
	    	setBotPosition();
	    	setObjectPosition(obstacles,OBSTACLE_SPEED);
	    	updateBackground(OBSTACLE_SPEED);
	    	if(!endless){
	    		setObjectPosition(ram,RAM_SPEED);
	    	}
	    	checkCollisions();
	    	checkTime();
    	}
    	else{
			gameOver();
		}

    }
    private void checkTime(){
	    	currentTime = System.currentTimeMillis();
	    			lastGen++;
	    	if(endless){
	    		score = ( currentTime - startTime)/100;
	    		updateText();
	    	}

	    	if(timeLeft()>=3000){
		    	if( (currentTime%OBSTACLE_GEN_RATE == 0) && lastGen>5){
		    		addObstacle();
		    		lastGen=0;
		    	}

		    	if( (!endless) && currentTime%RAM_GEN_RATE == 0)
		    		addRam();
	    	}
			if(timeLeft()<=0){
				winState = true;
				isGameOver = true;
			}

    }

    private void initText(){
    	canvas = new Canvas(SIZE_X,SIZE_Y);
    	root.getChildren().add(canvas);
    	lives = NUM_OF_LIVES;
    	text = canvas.getGraphicsContext2D();
    	text.setFill( Color.RED );
	    text.setStroke( Color.BLACK );
	    text.setLineWidth(2);
	    text.setFont( smallFont );
    	updateText();

    }
    private void updateText(){

    	int yPad = SIZE_Y/20;

    	text.clearRect(0, 0, SIZE_X, SIZE_Y);
         String livesText = ("Lives: " + lives);
         text.fillText( livesText,  SIZE_X/3, SIZE_Y/20 );
         text.strokeText( livesText, SIZE_X/3, SIZE_Y/20 );
         String scoreText = ("Score: " + score);;
         text.fillText( scoreText, SIZE_X*.66, SIZE_Y/20 );
         text.strokeText( scoreText, SIZE_X*.66, SIZE_Y/20 );

    }
    private void gameOver(){



    	toRemove.addAll(obstacles);
    	toRemove.addAll(ram);
    	removeObjects();

    	String gameOverText;
    	String gameOverMessage;
    	if(winState){
    		gameOverText = " You Won!";
    		gameOverMessage = "Finally, floop is free \nto flop freely!";
    	}
    	else{
    		gameOverText = "Game Over";
    		gameOverMessage = "You flooped when\nyou should have flopped";
    		app.stopMusic();
    	}
    	text.setFont( bigFont );
    	text.fillText( gameOverText, SIZE_X/5,  SIZE_Y/3);
    	text.setFont( smallFont );
    	text.fillText( gameOverMessage, SIZE_X/5,  SIZE_Y/2);
    }

    private void setBotPosition(){
        myBot.setY(myBot.getY()+botVel*KEY_INPUT_SPEED);
        if( myBot.getY() > SIZE_Y - myBot.getBoundsInLocal().getHeight()  )
        	myBot.setY(SIZE_Y - myBot.getBoundsInLocal().getHeight());
        if( myBot.getY() < incr  )
       	 	myBot.setY(incr);
    }

    private void setObjectPosition(List<ImageView> objects, int speed){
    	for (ImageView obj: objects) {
    	   	 obj.setX( obj.getX() - speed);

    	  //Check if out of visibility;
    	   	if(hasObjectLeftScreen(obj)){
    			toRemove.add(obj);
    		}
    	}
    	removeObjects();
    	//Remove out of bounds objects
    }

    private void updateBackground(int speed){
    	background1.setX( background1.getX() - speed);

      	 if(background2.getX()== (app.getSizeX()-10240)){
      		background1.setX(app.getSizeX());
      	 }

      	background2.setX( background2.getX() - speed);

      	if(background1.getX()==0){
      		background2.setX(10240);
      	}
    }

    private boolean hasObjectLeftScreen(ImageView object){
    	return ( (object.getX() + object.getBoundsInLocal().getWidth() ) < 0);
    }

    private void removeObjects(){
    	root.getChildren().removeAll(toRemove);
    	obstacles.removeAll(toRemove);
    	ram.removeAll(toRemove);
    	toRemove.clear();
    }
    private void checkCollisions(){
    	for(ImageView obstacle: obstacles){
    		if (obstacle.getBoundsInParent().intersects(myBot.getBoundsInParent())){
    			lives--;
    			updateText();
    			if(lives<=0){
    				isGameOver = true;
    				rotateObject(myBot,Integer.MAX_VALUE);
    			}

    			rotateObject(myBot,1);
    			toRemove.add(obstacle);
    		}
    	}
    if(!endless)	{
    	for(ImageView ramStick: ram){
    		if (ramStick.getBoundsInParent().intersects(myBot.getBoundsInParent())){
    			score+=100;
    			updateText();
    			toRemove.add(ramStick);
    		}
    	}
    }
    	removeObjects();

    }

    private void initBackground(){
        Image backImage = new Image(FloopFlop.class.getResourceAsStream(BACKGROUND_FILENAME));
        background1 = new ImageView(backImage);
        background2 = new ImageView(backImage);

        background1.setY(0);
        background1.setX(0);

        background1.toBack();
        background2.toBack();

        root.getChildren().add(background1);
        root.getChildren().add(background2);
        background2.setY(0);
        background2.setX(10240);
    }

    private void initBot(){
        botVel = 0;
        Image botImage = new Image(FloopFlop.class.getResourceAsStream(BOT_FILENAME));
        myBot = new ImageView(botImage);
        myBot.setPreserveRatio(true);
        myBot.setScaleX(scale);
        myBot.setScaleY(scale);
        myBot.setX(SIZE_X / 10 - myBot.getBoundsInLocal().getWidth() / 2);
        myBot.setY(SIZE_Y / 2  - myBot.getBoundsInLocal().getHeight() / 2);
        root.getChildren().add(myBot);
    }
    private void initObstacle(){
    	obstacles = new ArrayList<ImageView>();
    	obstacleSet = new ArrayList<Image>();

    	for(String filename: OBSTACLE_FILENAMES){
    		Image obstacle = new Image(FloopFlop.class.getResourceAsStream(filename));
    		obstacleSet.add(obstacle);
    	}
    }
    private void initRam(){
    	ram = new ArrayList<ImageView>();
    	ramImage = new Image(FloopFlop.class.getResourceAsStream(RAM_FILENAME));

    }
    private void addObstacle(){
    	ImageView newObstacle = getObstacle();
    	newObstacle.setPreserveRatio(true);
    	newObstacle.setScaleX(scale);
    	newObstacle.setScaleY(scale);
    	placeOffScreen(newObstacle);
        obstacles.add(newObstacle);
        root.getChildren().add(newObstacle);
    }
    private void placeOffScreen(ImageView sprite){
    	sprite.setX(SIZE_X + 100);
    	double yMax = SIZE_Y - sprite.getBoundsInLocal().getHeight();
    	sprite.setY((int) (yMax*Math.random()) );
    }
    private void addRam(){
    	ImageView newRam = getRam();
        newRam.setPreserveRatio(true);
        newRam.setScaleX(scale);
        newRam.setScaleY(scale);
    	placeOffScreen(newRam);
        ram.add(newRam);
        root.getChildren().add(newRam);
    }
    private ImageView getObstacle(){
    	//TODO: Generate some obstacles more often than others
    	Image obstacleImage = obstacleSet.get( (int) (Math.random()*(obstacleSet.size()) ) );
    	ImageView obs = new ImageView(obstacleImage);
    	obs.setPreserveRatio(true);
    	obs.setScaleX(scale);
    	return new ImageView(obstacleImage);
    }
    private ImageView getRam(){
    	return new ImageView(ramImage);
    }
    private void rotateObject(ImageView myObj, int rotateCount){
	    RotateTransition rt = new RotateTransition(Duration.millis(1000), myObj);
	    rt.setByAngle(-360);
	    rt.setCycleCount(rotateCount);
	    myObj.setRotate(0);
	    rt.play();
    }
    private long timeLeft(){
    	if(endless)
    		return Integer.MAX_VALUE;
    	else
    		return DURATION -  (currentTime - startTime);
    }
    // What to do each time a key is pressed

    private void handleMousePress(double pos) {
    	if(pos<SIZE_Y/2){
    		handleKeyPress(KeyCode.UP);
    	}
    	else{
    		handleKeyPress(KeyCode.DOWN);

    	}
    }
    private void handleMouseRelease(double pos) {
    	if(pos<SIZE_Y/2){
    		handleKeyRelease(KeyCode.UP);
    	}
    	else{
    		handleKeyRelease(KeyCode.DOWN);
    		handleKeyRelease(KeyCode.ENTER);

    	}

    }

    private void handleKeyPress (KeyCode code) {
        switch (code) {
            case RIGHT:
                break;
            case LEFT:
                break;
            case UP:
            	if( myBot.getY()> myBot.getBoundsInLocal().getHeight() )
            		botVel = -1;
            	else
            		botVel = 0;
                break;
            case DOWN:
            	 if( (myBot.getY() < SIZE_Y - myBot.getBoundsInLocal().getHeight())  )
            		 botVel = 1;
            	 else
            		 botVel = 0;
                break;
            case C:
            	cheatsOn = true;
            	break;
            case L: // Set lives to 1000
            	if(cheatsOn){
            		lives = 1000;
            	}
            	break;
            case W: //Automatically win
            	if(cheatsOn){
               		winState = true;
               		isGameOver = true;
            	}
            	break;
            case S: //Turn off the sound
            	if(cheatsOn){
               		mediaPlayer.dispose();
            	}
           	break;
            default:
                // do nothing
        }
    }
    private void handleKeyRelease (KeyCode code) {
        switch (code) {
            case RIGHT:
                break;
            case LEFT:
                break;
            case UP:
            	botVel = 0;
                break;
            case DOWN:
            	botVel = 0;
                break;
            case ENTER:
            	if(isGameOver){
            	//	mediaPlayer.dispose();
            		app.quit();
            	//	gameQuit.set(true);
            	}

            	 break;
            default:
                // do nothing
        }
    }
    public void setStage(Stage s){
    	primaryStage = s;
    }

}
