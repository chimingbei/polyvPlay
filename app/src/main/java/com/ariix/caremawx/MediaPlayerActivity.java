package com.ariix.caremawx;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;

import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity implements
        MediaController.MediaPlayerControl, MediaPlayer.OnBufferingUpdateListener, SurfaceHolder.Callback {

    private MediaPlayer mediaPlayer;
    private MediaController controller;
    private int bufferPercentage = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player_layout);
        mediaPlayer = new MediaPlayer();
        controller = new MediaController(this);
        controller.setAnchorView(findViewById(R.id.root_ll));
        initSurfaceView();
    }

    private void initSurfaceView() {
        SurfaceView videoSuf = (SurfaceView) findViewById(R.id.controll_surfaceView);
        videoSuf.setZOrderOnTop(false);
        videoSuf.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        videoSuf.getHolder().addCallback(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            String path = "http://mpv.videocc.net/ca6dfdb0a0/e/ca6dfdb0a0a54a1071d6546a2799ebee_2.mp4";
            mediaPlayer.setDataSource(path);
            mediaPlayer.setOnBufferingUpdateListener(this);
            //mediaPlayer.prepare();

            controller.setMediaPlayer(this);
            controller.setEnabled(true);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mediaPlayer){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return super.onTouchEvent(event);
    }

    //MediaPlayer
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    //MediaPlayerControl
    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        bufferPercentage = i;
    }

    @Override
    public void start() {
        if (null != mediaPlayer){
            mediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (null != mediaPlayer){
            mediaPlayer.pause();
        }
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayer.isPlaying()){
            return true;
        }
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return bufferPercentage;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    //SurfaceHolder.callback
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mediaPlayer.setDisplay(surfaceHolder);
        mediaPlayer.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

}
