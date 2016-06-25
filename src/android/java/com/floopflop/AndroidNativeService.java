package com.floopflop;

import static android.app.Activity.RESULT_OK;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import com.floopflop.NativeService;
import javafx.scene.image.Image;
import 	android.media.MediaPlayer;
import 	android.media.AudioManager;
import javafxports.android.FXActivity;
public class AndroidNativeService implements NativeService {


    public AndroidNativeService() {

    }

	private static final String FILENAME = "/raw/floopflop";
	private MediaPlayer mediaPlayer;

	@Override
	public boolean load() {
		  Uri myUri = Uri.parse("android.resource://"+FXActivity.getInstance().getPackageName()+FILENAME);
          mediaPlayer = new MediaPlayer();
           mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
             try {
              mediaPlayer.setDataSource(FXActivity.getInstance().getApplicationContext(), myUri);
               } catch (IOException e) {
               return false;
           }
              try {
               mediaPlayer.prepare();
           } catch (IOException e) {
              return false;
           }
		return true;
	}

	@Override
	public void play() {
		mediaPlayer.start();
	}

	@Override
	public void stop() {
		mediaPlayer.stop();
	}


}
