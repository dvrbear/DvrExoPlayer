package com.dvrbear.dvrexoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;


public class MainActivity extends Activity {

    private InputMethodManager keyboardManager;

    private EMVideoView emVideoView;

    private Uri videoUrl1 = Uri.parse("https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8");
    private Uri videoUrl2 = Uri.parse("https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4");
    private Uri videoUrl3;

    private Button testBut1, testBut2, testBut3, playBut, pauseBut;
    private EditText editText;
    private TextView infoText;

    private String errorMessage = "ERROR!";
    private String loadingMessage = "LOADING...";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);

        testBut1 = (Button) findViewById(R.id.but_play_1);
        testBut2 = (Button) findViewById(R.id.but_play_2);
        testBut3 = (Button) findViewById(R.id.but_play_3);
        playBut = (Button) findViewById(R.id.but_play);
        pauseBut = (Button) findViewById(R.id.but_pause);

        editText = (EditText) findViewById(R.id.text_url_3);
        infoText = (TextView) findViewById(R.id.text_info);

        emVideoView = (EMVideoView) findViewById(R.id.video_view);

        keyboardManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboardManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

        setListeners();
        onFirstState();
    }



    //// SET LISTENERS

    private void setListeners(){

        testBut1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVideo(videoUrl1);
            }
        });

        testBut2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVideo(videoUrl2);
            }
        });

        testBut3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseUrl3();
            }
        });

        playBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlayPressed();
            }
        });

        pauseBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPausePressed();
            }
        });

        emVideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                onPlayState();
                emVideoView.start();
            }
        });

        emVideoView.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError() {
                onErrorState();
                return false;
            }
        });
    }

    //// VIDEO CONTROL

    private void setVideo(Uri url){
        onLoadingState();
        emVideoView.setVideoURI(url);
    }

    private void parseUrl3(){
        keyboardManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        if(!editText.getText().toString().equals("")){
            videoUrl3 = Uri.parse(editText.getText().toString());
            setVideo(videoUrl3);
        }
    }

    private void onPlayPressed(){
        emVideoView.start();
        onPlayState();
    }

    private void onPausePressed(){
        emVideoView.pause();
        onPauseState();
    }

    //// SCREEN STATES

    private void onFirstState(){
        hidePlayPauseButtons();
        infoText.setVisibility(View.INVISIBLE);
        emVideoView.setVisibility(View.INVISIBLE);
    }

    private void onPlayState(){
        infoText.setVisibility(View.INVISIBLE);
        playBut.setVisibility(View.INVISIBLE);
        pauseBut.setVisibility(View.VISIBLE);
        emVideoView.setVisibility(View.VISIBLE);
    }

    private void onPauseState(){
        infoText.setVisibility(View.INVISIBLE);
        playBut.setVisibility(View.VISIBLE);
        pauseBut.setVisibility(View.INVISIBLE);
    }

    private void onLoadingState(){
        hidePlayPauseButtons();
        infoText.setVisibility(View.VISIBLE);
        infoText.setText(loadingMessage);
    }

    private void onErrorState(){
        hidePlayPauseButtons();
        infoText.setVisibility(View.VISIBLE);
        infoText.setText(errorMessage);
    }

    private void hidePlayPauseButtons(){
        emVideoView.setVisibility(View.INVISIBLE);
        playBut.setVisibility(View.INVISIBLE);
        pauseBut.setVisibility(View.INVISIBLE);
    }

    ////

    @Override
    protected void onDestroy() {
        emVideoView.release();
        super.onDestroy();
    }
}
