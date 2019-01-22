package app.karlafit.com.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import app.karlafit.com.R;
import app.karlafit.com.clases.User;
import app.karlafit.com.config.Constants;
import app.karlafit.com.holder.Dias;
import app.karlafit.com.holder.Semanas;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class SemanaDiaActivity extends AppCompatActivity {

    private int id;
    private Toolbar toolbar;
    private TextView txtHoras,txtTitulo2,txtDescri;
    private ImageView play,img;
    private RecyclerView mSemanasDiasRecyclerView;
    public static ArrayList<Dias> mListDias;
    private SemanasDiasRecycleAdapter mSemanasDiasAdapter;
    private DatabaseReference databaseDias;
    private String TAG = SemanaDiaActivity.class.getName();
    private TextView title;
    private int selectVideo;
    private SweetAlertDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_semana_dia);

        if (getIntent().hasExtra("id")) {
            id = Integer.parseInt(getIntent().getStringExtra("id"));
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " );
        }

        /* toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbaruser);

        title = (TextView) findViewById(R.id.txtTitle);

        title.setText(SemanasActivity.mListSemanas.get(id).getTitle());

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow));
        }*/

        txtHoras = (TextView) findViewById(R.id.txtHoras);
        txtTitulo2 = (TextView) findViewById(R.id.txtTitulo2);
        txtDescri = (TextView) findViewById(R.id.txtDescri);
        play = (ImageView) findViewById(R.id.play);
        img = (ImageView) findViewById(R.id.img);

        play.setVisibility(View.GONE);

        txtHoras.setText(SemanasActivity.mListSemanas.get(id).getSubtitle());
        txtTitulo2.setText(SemanasActivity.mListSemanas.get(id).getTitle2());
        txtDescri.setText(SemanasActivity.mListSemanas.get(id).getDescripcion());

        Constants.justify(txtDescri);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txtDescri.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);

        }

        databaseDias = FirebaseDatabase.getInstance().getReference("dias");
        databaseDias.keepSynced(true);



        mSemanasDiasRecyclerView = (RecyclerView) findViewById(R.id.listadias);
        mListDias = new ArrayList<Dias>();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, 1);

        mSemanasDiasRecyclerView.setLayoutManager(layoutManager);
        mSemanasDiasAdapter = new SemanasDiasRecycleAdapter();
        mSemanasDiasRecyclerView.setAdapter(mSemanasDiasAdapter);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Constants.getNetworkClass(SemanaDiaActivity.this).equals("-")) {

                    Intent intent = new Intent(SemanaDiaActivity.this, VideoActivity.class);
                    intent.putExtra("id", String.valueOf(selectVideo));
                    startActivity(intent);
                }else
                {
                    pDialog = new SweetAlertDialog(SemanaDiaActivity.this, SweetAlertDialog.WARNING_TYPE);
                    pDialog.setTitleText(getResources().getString(R.string.app_name));
                    pDialog.setContentText(getResources().getString(R.string.error_internet));
                    pDialog.setConfirmText(getString(R.string.yes));
                    pDialog.setCancelText(getString(R.string.no));
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                        }
                    });
                    pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.cancel();
                        }
                    });
                    pDialog.show();
                }

            }
        });


        Glide.with(SemanaDiaActivity.this).load(SemanasActivity.mListSemanas.get(id).getImagen())
                .thumbnail(1.0f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);

        cargaDias();

    }



    private void cargaDias()
    {

        com.google.firebase.database.Query userquery = databaseDias
                .orderByChild("semana_id").equalTo(SemanasActivity.mListSemanas.get(id).getId());

        userquery.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Dias dias = postSnapshot.getValue(Dias.class);

                    mListDias.add(dias);



                }

                if (mListDias.size() > 0) {

                    mSemanasDiasAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

    }



    /* adapter*/

    public class SemanasDiasRecycleAdapter extends RecyclerView.Adapter<SemanasDiasRecycleHolder> {
        private int lastPosition = -1;

        @Override
        public SemanasDiasRecycleHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_semanas_dias, viewGroup, false);
            return new SemanasDiasRecycleHolder(v);
        }


        @Override
        public void onBindViewHolder(final SemanasDiasRecycleHolder productHolder, final int i) {

            productHolder.mTxtDia.setText(mListDias.get(i).getTitle());
            productHolder.mSubTitle.setText(mListDias.get(i).getTiempo()+" "+ "segundos");
            productHolder.mTitle.setText(mListDias.get(i).getSubtitle());


            productHolder.mEvents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Glide.with(SemanaDiaActivity.this).load(mListDias.get(i).getImagen())
                            .thumbnail(1.0f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(img);

                    title.setText(mListDias.get(i).getTitle());

                    txtHoras.setText(mListDias.get(i).getTiempo());
                    txtTitulo2.setText(mListDias.get(i).getSubtitle());
                    txtDescri.setText(mListDias.get(i).getDescripcion());

                    selectVideo = i;

                    play.setVisibility(View.VISIBLE);

                }
            });


        }


        @Override
        public int getItemCount() {
            return mListDias.size();
        }

        public void removeItem(int position) {
            mListDias.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mListDias.size());
            //Signal.get().reset();


        }

    }

    public class SemanasDiasRecycleHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextView mSubTitle;
        public TextView mTxtDia;
        private LinearLayout mEvents;


        public SemanasDiasRecycleHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.txtTitulo2);
            mSubTitle = (TextView) itemView.findViewById(R.id.txtHoras);
            mTxtDia = (TextView) itemView.findViewById(R.id.txtDia);
            mEvents = (LinearLayout) itemView.findViewById(R.id.events);
        }
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
