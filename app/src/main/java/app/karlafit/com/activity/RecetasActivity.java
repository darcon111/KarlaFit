package app.karlafit.com.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.util.ArrayList;

import app.karlafit.com.R;
import app.karlafit.com.config.Constants;
import app.karlafit.com.holder.Recetas;
import app.karlafit.com.holder.Semanas;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class RecetasActivity extends android.support.v4.app.Fragment {

    private RecyclerView mRecetasRecyclerView;
    public static ArrayList<Recetas> mListRecetas;
    private RecetasRecycleAdapter mRecetasAdapter;
    private DatabaseReference databaseRecetas;
    private ProgressBar progress;
    private SweetAlertDialog pDialog;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_recetas, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        databaseRecetas = FirebaseDatabase.getInstance().getReference("recetas");
        databaseRecetas.keepSynced(true);



        mRecetasRecyclerView = (RecyclerView) view.findViewById(R.id.recetas_recycler_view);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        // Create a grid layout with two columns

        mListRecetas = new ArrayList<Recetas>();


        int screen = Constants.deterScreenSize(getContext());
        if(screen ==0 || screen ==1)
        {
            screen = 2;
        }else
        {
            screen = 4;
        }

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(screen, 1);

        mRecetasRecyclerView.setLayoutManager(layoutManager);
        mRecetasAdapter = new RecetasRecycleAdapter();
        mRecetasRecyclerView.setAdapter(mRecetasAdapter);

        cargaRecetas();



    }


    private void cargaRecetas()
    {
        databaseRecetas.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                mListRecetas.clear();

                //iterating through all the nodes
                for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Recetas receta = postSnapshot.getValue(Recetas.class);

                    mListRecetas.add(receta);

                }

                if (mListRecetas.size() > 0) {

                    progress.setVisibility(View.GONE);
                    mRecetasAdapter.notifyDataSetChanged();
                    mRecetasRecyclerView.setVisibility(View.VISIBLE);


                }else
                {
                    mRecetasRecyclerView.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });
    }


    /* adapter*/

    public class RecetasRecycleAdapter extends RecyclerView.Adapter<RecetasRecycleHolder> {
        private int lastPosition = -1;

        @Override
        public RecetasRecycleHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recetas, viewGroup, false);
            //setAnimation(v,i);
            return new RecetasRecycleHolder(v);
        }


        @Override
        public void onBindViewHolder(final RecetasRecycleHolder productHolder, final int i) {

            productHolder.mTitle.setText(mListRecetas.get(i).getTitulo());
            productHolder.mDescri.setText(mListRecetas.get(i).getDescripcion());




            Glide.with(getActivity()).load(mListRecetas.get(i).getImagen()).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        productHolder.mContenedor.setBackground(resource);
                    }else
                    {
                        productHolder.mContenedor.setBackground(resource);
                    }
                }
            });



            productHolder.mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), RecetaActivity.class);
                        intent.putExtra("id", String.valueOf(i));
                        startActivity(intent);

                }
            });





            /*Glide.with(SemanasActivity.this).load(R.drawable.ic_bg_semanas).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        productHolder.mContenedor.setBackground(resource);
                    }
                }
            });*/

            // setAnimation(productHolder.itemView, i);



        }


        @Override
        public int getItemCount() {
            return mListRecetas.size();
        }

        public void removeItem(int position) {
            mListRecetas.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mListRecetas.size());
            //Signal.get().reset();


        }

        private void setAnimation(View viewToAnimate, int position) {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition) {
                Animation animation;
                if (position % 2 == 0) {
                    animation = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_back_in);
                } else {
                    animation = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_forward_in);
                }

                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }


    }

    public class RecetasRecycleHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public JustifiedTextView mDescri;
        public ImageView mImagen;
        public RelativeLayout mContenedor;
        public Button mBtn;


        public RecetasRecycleHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            mDescri = (JustifiedTextView) itemView.findViewById(R.id.txtDes);
            mImagen = (ImageView) itemView.findViewById(R.id.cocina);
            mContenedor= (RelativeLayout) itemView.findViewById(R.id.contenedor);
            mBtn= (Button) itemView.findViewById(R.id.btn);

        }
    }

}
