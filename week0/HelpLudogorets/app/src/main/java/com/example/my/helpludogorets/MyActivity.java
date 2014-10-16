package com.example.my.helpludogorets;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;


public class MyActivity extends Activity implements View.OnClickListener {

    private final int PLAY_IMAGE_ID = 1;
    private final String VIDEO_PATH = "/storage/emulated/0/Movies/Ronaldo_Dive_Moti.mp4";
    private final int THREE_SECONDS_IN_MILLISECONDS = 3000;

    VideoView mVideo;
    ImageButton mPrevious;
    ImageButton mPlay;
    ImageButton mNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mVideo = (VideoView) findViewById(R.id.video);
        mVideo.setVideoPath(VIDEO_PATH);


        mPrevious = (ImageButton) findViewById(R.id.prev);
        mPrevious.setOnClickListener(this);

        mPlay = (ImageButton) findViewById(R.id.play);
        mPlay.setOnClickListener(this);
        mPlay.setImageResource(R.drawable.play);

        mNext = (ImageButton) findViewById(R.id.next);
        mNext.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideo.start();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch(viewId)  {
            case R.id.prev:
                handlePrev();
                break;
            case R.id.play:
                handlePlay();
                break;
            case R.id.next:
                handleNext();
                break;
            default:
                break;
        }
    }

    private void handleNext() {
        mVideo.getCurrentPosition();
        mVideo.seekTo(mVideo.getCurrentPosition() + THREE_SECONDS_IN_MILLISECONDS);
    }

    private void handlePlay() {
        if(mVideo.isPlaying())  {
            mVideo.pause();
            mPlay.setImageResource(R.drawable.play);
        } else {
            mVideo.start();
            mPlay.setImageResource(R.drawable.pause);
        }
    }

    private void handlePrev() {
        mVideo.getCurrentPosition();
        mVideo.seekTo(mVideo.getCurrentPosition() - THREE_SECONDS_IN_MILLISECONDS);
    }
}
