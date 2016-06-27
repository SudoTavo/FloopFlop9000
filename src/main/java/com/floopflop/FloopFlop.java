package com.floopflop;

import com.gluonhq.charm.glisten.application.MobileApplication;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.util.Duration;

public class FloopFlop extends Application {

	public static final int FRAMES_PER_SECOND = 60;
	private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

	private FloopFlopGame myGame;
	private FloopFlopMenu myMenu;
	private Rectangle2D visualBounds;
	private int SizeX;
	private int SizeY;
	private NativeService nativeService;
	Stage myStage;

	@Override
	public void start (Stage s) {

		nativeService = PlatformFactory.getPlatform().getNativeService();

		visualBounds = Screen.getPrimary().getVisualBounds();

		myMenu = new FloopFlopMenu(this);
		setSizeX((int) visualBounds.getWidth());
		setSizeY(SizeY = (int) visualBounds.getHeight());

		s.getIcons().add(new Image(FloopFlop.class.getResourceAsStream("/icon.png")));

		//myGame = new ExampleGame();
		myStage = s;
		startMenu();
	}

	public void startMenu(){
		myStage.hide();
		myStage.setTitle(myMenu.getTitle());
		Scene scene = myMenu.init(SizeX, SizeY);

		myStage.setScene(scene);
		myStage.setFullScreen(true);
		myStage.show();

		// sets the game's loop
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
				e -> myMenu.step(SECOND_DELAY));
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();

		menuListener(myMenu.optionSelected, animation, myStage);
		// Scene scene = new Scene(root, visualBounds.getWidth(), visualBounds.getHeight());
	}

	public void startGame(boolean endless) {

		myStage.hide();

		myGame = new FloopFlopGame(this);
		myStage.setTitle(myGame.getTitle());

		Scene scene = myGame.init(SizeX, SizeY);
		myGame.setEndless(endless);
		myStage.setScene(scene);
		myStage.show();

		// sets the game's loop
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
				e -> myGame.step(SECOND_DELAY));
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();

		quitListener(myGame.gameQuit, animation,  myStage);
	}

	public void quit(){
		myStage.close();
	}
	public Stage getMyStage() {
		return myStage;
	}

	public void setMyStage(Stage myStage) {
		this.myStage = myStage;
	}

	public static void main (String[] args) {
		launch(args);
	}

	public void menuListener(BooleanProperty flag,Timeline animation, Stage s){
		flag.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				animation.stop();
				if(myMenu.getOptionNum()==0){
					s.close();
					startGame(false);
				}
				if(myMenu.getOptionNum()==1){
					s.close();
					startGame(true);
				}
				if(myMenu.getOptionNum()==2){
					s.close();
				}

			}

		});
	}
	public void quitListener(BooleanProperty flag,Timeline animation, Stage s){
		flag.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				s.close();
			}

		});
	}



	public void playMusic(){
		if(nativeService.load()){
			nativeService.play();
		}

	}
	public void stopMusic(){
		nativeService.stop();
	}

	public int getSizeX() {
		return SizeX;
	}

	public void setSizeX(int sizeX) {
		SizeX = sizeX;
	}

	public int getSizeY() {
		return SizeY;
	}

	public void setSizeY(int sizeY) {
		SizeY = sizeY;
	}
}
