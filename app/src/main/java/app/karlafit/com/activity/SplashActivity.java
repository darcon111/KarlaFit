package app.karlafit.com.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import com.crashlytics.android.Crashlytics;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

import app.karlafit.com.R;
import app.karlafit.com.config.AppPreferences;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    private AppPreferences app;
    private VideoView videoView;

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 3000;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        AppEventsLogger.activateApp(getApplication());
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_splash);

        /*TimerTask task = new TimerTask() {
            @Override
            public void run() {

                app = new AppPreferences(getApplicationContext());
                user = FirebaseAuth.getInstance().getCurrentUser();

                if(app.getTour().equals("0"))
                {
                    Intent intent = new Intent(SplashActivity.this, RetoActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    if (user != null) {
                        // User is signed in
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // No user is signed in
                        Intent mainIntent = new Intent().setClass(
                                SplashActivity.this, LoginActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
               }

            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);*/



        videoView = (VideoView) findViewById(R.id.videoView);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pepa);

        videoView.setVideoURI(video);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (isFinishing())
                    return;

                app = new AppPreferences(getApplicationContext());
                user = FirebaseAuth.getInstance().getCurrentUser();

                if(app.getTour().equals("0"))
                {
                    Intent intent = new Intent(SplashActivity.this, RetoActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    if (user != null) {
                        // User is signed in
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // No user is signed in
                        Intent mainIntent = new Intent().setClass(
                                SplashActivity.this, LoginActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }
            }
        });

        videoView.start();




    }
}
