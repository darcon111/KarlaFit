package app.karlafit.com.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import app.karlafit.com.R;
import app.karlafit.com.config.AppPreferences;

public class RetoActivity extends AppCompatActivity {

    private AppPreferences app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_reto);

        app = new AppPreferences(getApplicationContext());
    }

    public void reto(View v){

        app.setTour("1");

        Intent  intent = new Intent(RetoActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
