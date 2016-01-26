package com.example.user.gourmetpitt.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class BackGroundMusicService extends Service {

    private final IBinder musicBinder = new MyMusicBind();
    MediaPlayer mplayer;
    private final String URL = "/sdcard/runy/takeabow.mp3";
    private int length = 0;
    private boolean isPlay = false;

    public BackGroundMusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return musicBinder;

    }

    public class MyMusicBind extends Binder {
        public BackGroundMusicService getService(){
            return BackGroundMusicService.this;

        }

    }

    public void onCreate(){
        super.onCreate();

        try {

            playAudio(URL);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mplayer.start();
        isPlay=true;
        return Service.START_STICKY;

    }



    private void playAudio(String url)throws Exception
    {
        killMediaPlayer();
        mplayer = new MediaPlayer();
        mplayer.setDataSource(url);

        mplayer.setLooping(true);
        mplayer.setVolume(100,100);

        mplayer.prepare();
    }

    private void killMediaPlayer()
    {
        if(mplayer!=null)
        {
            try
            {
                mplayer.release();
                isPlay=false;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    //control the mplayer
    public void pauseMusic(){

        if(mplayer.isPlaying()) {
            isPlay=false;
            length = mplayer.getCurrentPosition();
            mplayer.pause();

        }
    }

    public void resumeMusic(){

        if(mplayer.isPlaying()==false){
            mplayer.seekTo(length);
            mplayer.start();
            isPlay=true;
        }

    }

    public void response(){
        if(isPlay){
            pauseMusic();
        }else{
            resumeMusic();
        }
    }


    public void stopMusic(){

        if(mplayer!=null) {
            mplayer.stop();
            mplayer.release();
            isPlay = false;

        }

        mplayer=null;

    }

    public void onPause(){
        pauseMusic();
    }

    public void onResume(){
        resumeMusic();
    }

    public void onDestroy(){
        super.onDestroy();

        if(mplayer!=null) {
            mplayer.stop();
            mplayer.release();
            isPlay=false;

        }

        mplayer=null;

    }
}
