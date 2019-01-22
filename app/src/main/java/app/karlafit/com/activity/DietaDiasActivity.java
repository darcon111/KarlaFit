package app.karlafit.com.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.util.ArrayList;

import app.karlafit.com.R;
import app.karlafit.com.config.Constants;
import app.karlafit.com.holder.DiasDieta;
import app.karlafit.com.holder.DiasHeader;
import app.karlafit.com.holder.SemanaDietas;
import app.karlafit.com.holder.Semanas;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class DietaDiasActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public static ArrayList<DiasHeader> mListDiasHeader;
    private RecyclerView mDiasHeaderRecyclerView,mDiasRecyclerView;
    private HeaderDiasRecycleAdapter mDiasHeaderAdapter;
    private String semana_select = "";
    private DatabaseReference databaseDiasDieta;
    public static ArrayList<DiasDieta> mListDias;
    private DiasRecycleAdapter mDiasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieta_dias);


        /* toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbaruser);

        TextView title = (TextView) findViewById(R.id.txtTitle);

        title.setText(getString(R.string.dieta));

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow));
        }

        mDiasHeaderRecyclerView = (RecyclerView) findViewById(R.id.DiasHeader);

        mListDiasHeader = new ArrayList<DiasHeader>();


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        int screen = Constants.deterScreenSize(DietaDiasActivity.this);
        if(screen ==0 || screen ==1)
        {
            screen = 1;
        }else
        {
            screen = 2;
        }





        mDiasHeaderRecyclerView.setLayoutManager(layoutManager);
        mDiasHeaderAdapter = new HeaderDiasRecycleAdapter();
        mDiasHeaderRecyclerView.setAdapter(mDiasHeaderAdapter);

        mListDiasHeader.add(new DiasHeader(1,"Lunes"));
        mListDiasHeader.add(new DiasHeader(2,"Martes"));
        mListDiasHeader.add(new DiasHeader(3,"Miércoles"));
        mListDiasHeader.add(new DiasHeader(4,"Jueves"));
        mListDiasHeader.add(new DiasHeader(5,"Viernes"));
        mListDiasHeader.add(new DiasHeader(6,"Sábado"));
        mListDiasHeader.add(new DiasHeader(7,"Domingo"));

        mDiasHeaderAdapter.notifyDataSetChanged();

        semana_select = SemanaDietaActivity.mListSemanas.get( Integer.parseInt(getIntent().getStringExtra("id"))).getId();

        databaseDiasDieta = FirebaseDatabase.getInstance().getReference("diasDietas");
        databaseDiasDieta.keepSynced(true);

        mListDias = new ArrayList<DiasDieta>();

        select();
        buscar(String.valueOf("1"));


        mDiasRecyclerView = (RecyclerView) findViewById(R.id.dias);
        StaggeredGridLayoutManager layoutManager2 = new StaggeredGridLayoutManager(screen, 1);
        mDiasRecyclerView.setLayoutManager(layoutManager2);
        mDiasAdapter = new DiasRecycleAdapter();
        mDiasRecyclerView.setAdapter(mDiasAdapter);

    }

    private void select()
    {
        mDiasHeaderRecyclerView.addOnItemTouchListener(new Constants.RecyclerTouchListener(getApplicationContext(), mDiasHeaderRecyclerView, new Constants.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, final int position) {

                    buscar(String.valueOf(mListDiasHeader.get(position).getId()));

            }
        }));
    }

    private void buscar(final String dia)
    {
        databaseDiasDieta.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                mListDias.clear();

                //iterating through all the nodes
                for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    DiasDieta diasDieta = postSnapshot.getValue(DiasDieta.class);

                    if(diasDieta.getSemana().equals(semana_select) && diasDieta.getDia().equals(dia)) {

                        mListDias.add(diasDieta);
                    }

                }

                mDiasAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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


    /* adapter*/

    public class HeaderDiasRecycleAdapter extends RecyclerView.Adapter<HeaderDiasRecycleHolder> {
        private int lastPosition = -1;

        @Override
        public HeaderDiasRecycleHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dias_header, viewGroup, false);
            //setAnimation(v,i);
            return new HeaderDiasRecycleHolder(v);
        }


        @Override
        public void onBindViewHolder(final HeaderDiasRecycleHolder productHolder, final int i) {

            productHolder.mTitle.setText(mListDiasHeader.get(i).getTitle());



        }


        @Override
        public int getItemCount() {
            return mListDiasHeader.size();
        }


    }

    public class HeaderDiasRecycleHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;


        public HeaderDiasRecycleHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.txtTitle);

        }
    }


    /* adapter*/

    public class DiasRecycleAdapter extends RecyclerView.Adapter<DiasRecycleHolder> {
        private int lastPosition = -1;

        @Override
        public DiasRecycleHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dia_receta, viewGroup, false);
            //setAnimation(v,i);
            return new DiasRecycleHolder(v);
        }


        @Override
        public void onBindViewHolder(final DiasRecycleHolder productHolder, final int i) {

            productHolder.mTitle.setText(mListDias.get(i).getTitle());
            productHolder.mCalorias.setText(mListDias.get(i).getCalorias());

            productHolder.mDes.setText(mListDias.get(i).getDescripcion());


            Glide.with(DietaDiasActivity.this).load(mListDias.get(i).getImagen())
                    .thumbnail(1.0f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(productHolder.imageView);



        }


        @Override
        public int getItemCount() {
            return mListDias.size();
        }




    }

    public class DiasRecycleHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextView mCalorias;
        public JustifiedTextView mDes;
        public ImageView imageView;



        public DiasRecycleHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            mCalorias = (TextView) itemView.findViewById(R.id.txtCalorias);
            mDes = (JustifiedTextView) itemView.findViewById(R.id.txtDes);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

        }
    }




}
