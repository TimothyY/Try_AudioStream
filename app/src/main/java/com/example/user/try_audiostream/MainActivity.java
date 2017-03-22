package com.example.user.try_audiostream;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener {

    public final static String SONG_URL="http://a1727.phobos.apple.com/us/r30/Music2/v4/d5/ed/17/d5ed17be-831b-2ea9-08f4-c103c3926435/mzaf_7429026204462795502.plus.aac.p.m4a";

    Button btnPlay,btnPause;
    MediaPlayer mediaPlayer;
    boolean isAudioPrepared;
    int audioLastPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioLastPosition = 0;
        isAudioPrepared = false;

        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnPause.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mediaPlayer==null&&isAudioPrepared==false) { //just released from Activity onStop. need to re-initialize.
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            try {
                mediaPlayer.setDataSource(SONG_URL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPlay:
                if(mediaPlayer!=null&&isAudioPrepared==true){ //initialized onCreate
                    try {mediaPlayer.start();} catch (Exception e) {e.printStackTrace();}
                    mediaPlayer.seekTo(audioLastPosition);
                }
                break;
            case R.id.btnPause:
                try {mediaPlayer.pause(); audioLastPosition = mediaPlayer.getCurrentPosition();}
                catch (Exception e) {e.printStackTrace();}
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer!=null){
            audioLastPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isAudioPrepared = false;
            //because we want to be able to continue from last position, we don't set the audio last position to 0
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isAudioPrepared = true;
    }
}
