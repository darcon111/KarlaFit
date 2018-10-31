package app.karlafit.com.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

import app.karlafit.com.R;
import app.karlafit.com.config.AppPreferences;
import app.karlafit.com.config.Constants;

public class VideoActivity extends AppCompatActivity {

    private int id;
    private VideoView videoView;
    private ProgressBar loading,progress;
    private ImageView play;
    private TextView tiempo1,tiempo2;


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

        videoView = (VideoView) findViewById(R.id.video);
        play = (ImageView) findViewById(R.id.play);
        loading = (ProgressBar) findViewById(R.id.loading);
        progress = (ProgressBar) findViewById(R.id.progress);
        tiempo1 = (TextView) findViewById(R.id.tiempo1);
        tiempo2 = (TextView) findViewById(R.id.tiempo2);

        progress.getProgressDrawable().setColorFilter(
                getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);

        final Uri video = Uri.parse(SemanaDiaActivity.mListDias.get(id).getVideo());

        try
        {
            videoView.setVideoURI(video);
        }catch (Exception e)
        {

        }




        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (isFinishing())
                    return;

              finish();
            }
        });



        /*videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {

                videoView.start();
                play.setVisibility(View.VISIBLE);
                progress.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);


            }
        });*/

        play.setOnClickListener(new View.OnClickListener() {
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
        });



        new VideoAsync().execute();


    }

    private class VideoAsync extends AsyncTask<Void, Integer, Void>
    {
        int duration = 0;
        int current = 0;
        @Override
        protected Void doInBackground(Void... params) {




            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {

                    videoView.start();
                    play.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);

                    duration = videoView.getDuration();


                    tiempo1.setText("/"+ Constants.formatTime(duration));
                }
            });

            do {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                current = videoView.getCurrentPosition();

                runOnUiThread(new Runnable() {
                    public void run() {
                        tiempo2.setText(Constants.formatTime(current));
                    }
                });



                try {
                    publishProgress((int) (current * 100 / duration));
                    if(progress.getProgress() >= 100){
                        break;
                    }
                } catch (Exception e) {
                }
            } while (progress.getProgress() <= 100);

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            System.out.println(values[0]);
            progress.setProgress(values[0]);
        }
    }



}
