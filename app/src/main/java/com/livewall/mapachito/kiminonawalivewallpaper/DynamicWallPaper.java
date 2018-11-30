package com.livewall.mapachito.kiminonawalivewallpaper;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Created by lili.zheng on 2017/5/15.
 * Adapted to a play/pause state by DT3264 on 2018/11/24
 */

public class DynamicWallPaper extends WallpaperService{
    @Override
    public Engine onCreateEngine() {
        return new MyEngine();
    }

    //The lifecycle of the liveWallpaper, the changes in the surface state and the handle of touch events
    public class MyEngine extends Engine{
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            gestureListener = new GestureDetector(getApplicationContext(), new GestureListener());
            setTouchEventsEnabled(true);
        }

        boolean isActive = false;
        private GestureDetector gestureListener;
        private MediaPlayer mediaPlayer = null ;

        private class GestureListener extends GestureDetector.SimpleOnGestureListener {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                turnOnOffLiveWall();
                return super.onDoubleTap(e);
            }
        }

        private void playWall(){
            isActive=true;
            mediaPlayer.start();
        }

        private void pauseWallpapper(){
            isActive=false;
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }

        private void turnOnOffLiveWall() {
            if (!isActive) {
                playWall();
            } else {
                pauseWallpapper();
            }
        }

        @Override
        public SurfaceHolder getSurfaceHolder() {
            return super.getSurfaceHolder();
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            gestureListener.onTouchEvent(event);
        }

        @Override
        public void setTouchEventsEnabled(boolean enabled) {
            super.setTouchEventsEnabled(enabled);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            initMediaPlayer(holder);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            //Either is visible or not, it will start paused since the only time it moves is when is double pressed
            pauseWallpapper();
        }

        private void initMediaPlayer(SurfaceHolder holder){
            mediaPlayer = new MediaPlayer();
            String yourName = getBaseContext().getSharedPreferences("Default", MODE_PRIVATE).getString("Name", "Taki");
            String vidName="";
            if(yourName.equals("Taki")) vidName = "left.mp4";
            else if(yourName.equals("Mitsuhara")) vidName = "right.mp4";
            try {
                AssetManager assetMg = getApplicationContext().getAssets();
                AssetFileDescriptor fileDescriptor = assetMg.openFd(vidName);
                mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                mediaPlayer.setSurface(holder.getSurface());
                mediaPlayer.prepare();
                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(0, 0);
                mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            if (null!=mediaPlayer){
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }


}
