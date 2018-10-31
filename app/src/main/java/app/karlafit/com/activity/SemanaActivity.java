package app.karlafit.com.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import app.karlafit.com.R;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class SemanaActivity extends AppCompatActivity {

    private int id;
    private RelativeLayout semana;
    private Toolbar toolbar;
    private TextView txtHoras,txtTitulo2,txtDescri;
    private ImageView play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_semana);

        if (getIntent().hasExtra("id")) {
            id = Integer.parseInt(getIntent().getStringExtra("id"));
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " );
        }

        /* toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbaruser);

        TextView title = (TextView) findViewById(R.id.txtTitle);

        title.setText(SemanasActivity.mListSemanas.get(id).getTitle());

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow));
        }


        semana = (RelativeLayout) findViewById(R.id.semana);
        txtHoras = (TextView) findViewById(R.id.txtHoras);
        txtTitulo2 = (TextView) findViewById(R.id.txtTitulo2);
        txtDescri = (TextView) findViewById(R.id.txtDescri);
        play = (ImageView) findViewById(R.id.play);

        txtHoras.setText(SemanasActivity.mListSemanas.get(id).getSubtitle());
        txtTitulo2.setText(SemanasActivity.mListSemanas.get(id).getTitle2());
        txtDescri.setText(SemanasActivity.mListSemanas.get(id).getDescripcion());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txtDescri.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }


        Glide.with(SemanaActivity.this).load(SemanasActivity.mListSemanas.get(id).getImagen()).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    semana.setBackground(resource);
                }else
                {
                    semana.setBackground(resource);
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SemanaActivity.this, SemanaDiaActivity.class);
                intent.putExtra("id",String.valueOf(id));
                startActivity(intent);
            }
        });





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                finish();
                //------------
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
