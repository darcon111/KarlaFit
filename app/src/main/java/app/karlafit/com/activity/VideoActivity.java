package app.karlafit.com.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;

import app.karlafit.com.R;
import app.karlafit.com.clases.VideoControllerView;
import app.karlafit.com.config.Constants;

public class VideoActivity extends AppCompatActivity  implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl  {

    private int id;

    //private VideoView videoView;
    private ProgressBar loading;
    //private ImageView play;
    //private TextView tiempo1,tiempo2;
    //private SeekBar progress;
    //private AudioMediaController media;


    SurfaceView videoSurface;
    MediaPlayer player;
    VideoControllerView controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_video);

        if (getIntent().hasExtra("id")) {
            id = Integer.parseInt(getIntent().getStringExtra("id"));
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " );
        }

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);

        player = new MediaPlayer();
        controller = new VideoControllerView(this);

        try {
            player.setAudioStreamType(AudioManager.MODE_NORMAL);
            final Uri video = Uri.parse(SemanaDiaActivity.mListDias.get(id).getVideo());
            player.setDataSource(this, video);
            player.setOnPreparedListener(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



       /* videoView = (VideoView) findViewById(R.id.video);
        play = (ImageView) findViewById(R.id.play);*/
        loading = (ProgressBar) findViewById(R.id.loading);

        /*tiempo1 = (TextView) findViewById(R.id.tiempo1);
        tiempo2 = (TextView) findViewById(R.id.tiempo2);*/

        /*progress = (SeekBar) findViewById(R.id.control);
        media = new AudioMediaController(this);*/


        /*progress.getProgressDrawable().setColorFilter(
                getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);*/





        /*try
        {
            videoView.setVideoURI(video);
        }catch (Exception e)
        {

        }*/




        /*videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (isFinishing())
                    return;

              finish();
            }
        });*/



        /*videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {

                videoView.start();
                play.setVisibility(View.VISIBLE);
                progress.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);


            }
        });*/

        /*play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(videoView.isPlaying())
                {
                    videoView.pause();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        play.setImageDrawable(getDrawable(R.drawable.ic_action));
                    }else
                    {
                        play.setImageDrawable(getResources().getDrawable(R.drawable.ic_action));
                    }
                }else
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        play.setImageDrawable(getDrawable(R.drawable.ic_pause));
                    }else
                    {
                        play.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                    }
                    videoView.start();
                }

            }
        });*/


       /* videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {

                videoView.start();
                videoView.setMediaController(media);
                media.setAnchorView(videoView);
                play.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);

            }
        });*/



        //new VideoAsync().execute();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        loading.setVisibility(View.GONE);
        player.start();

    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        player.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
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
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        player.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(player.isPlaying())
        {
            player.stop();
        }
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                if(player.isPlaying())
                {
                    player.stop();
                }
                finish();
                //------------
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
