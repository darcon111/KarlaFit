package app.karlafit.com.activity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import app.karlafit.com.R;
import app.karlafit.com.config.Constants;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class RecetaActivity extends AppCompatActivity {

    private RelativeLayout receta;
    private Toolbar toolbar;
    private TextView txtTitle,txtSub;
    private JustifiedTextView txtDescri,txtIngre,txtPre;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_receta);


        if (getIntent().hasExtra("id")) {
            id = Integer.parseInt(getIntent().getStringExtra("id"));
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " );
        }

        /* toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbaruser);

        TextView title = (TextView) findViewById(R.id.txtTitle);

        title.setText(getString(R.string.receta));

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow));
        }


        receta = (RelativeLayout) findViewById(R.id.receta);
        txtTitle = (TextView) findViewById(R.id.txtTitulo);
        txtSub = (TextView) findViewById(R.id.txtSub);
        txtDescri = (JustifiedTextView) findViewById(R.id.txtDescri);
        txtIngre = (JustifiedTextView) findViewById(R.id.txtIngre);
        txtPre = (JustifiedTextView) findViewById(R.id.txtPrepa);

        txtTitle.setText(RecetasActivity.mListRecetas.get(id).getTitulo());
        txtSub.setText(RecetasActivity.mListRecetas.get(id).getCalorias()+"/"+RecetasActivity.mListRecetas.get(id).getTiempo());
        txtDescri.setText(RecetasActivity.mListRecetas.get(id).getDescripcion());
        txtIngre.setText(RecetasActivity.mListRecetas.get(id).getIngredientes());
        txtPre.setText(RecetasActivity.mListRecetas.get(id).getPreparacion());




        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txtPre.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            txtDescri.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            txtIngre.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }else
        {*/
            //Constants.justify(txtDescri);
            //Constants.justify(txtIngre);
            //Constants.justify(txtPre);
        /*}*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txtDescri.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }


        Glide.with(RecetaActivity.this).load(RecetasActivity.mListRecetas.get(id).getImagen()).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    receta.setBackground(resource);
                }else
                {
                    receta.setBackground(resource);
                }
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
