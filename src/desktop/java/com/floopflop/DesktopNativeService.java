package com.floopflop;

import java.net.URL;

import com.floopflop.NativeService;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class DesktopNativeService implements NativeService {


    public DesktopNativeService() {

    }

    private static final String FILENAME = "FloopFlop.mp3";
	private MediaPlayer mediaPlayer;

	@Override
	public boolean load() {
		try{
		 final URL resource = getClass().getResource("/"+FILENAME);
		final Media media = new Media(resource.toString());
		 mediaPlayer = new MediaPlayer(media);
		}
		catch (Exception e) {
			return false;
		}

		return true;
	}

	@Override
	public void play() {
		mediaPlayer.play();
	}

	@Override
	public void stop() {
		mediaPlayer.stop();
	}
}
