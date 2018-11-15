package app.karlafit.com.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import app.karlafit.com.R;
import app.karlafit.com.config.Constants;
import app.karlafit.com.holder.SemanaDietas;
import app.karlafit.com.holder.Semanas;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SemanaDietaActivity extends Fragment {

    private RecyclerView mSemanasRecyclerView;
    public static ArrayList<SemanaDietas> mListSemanas;
    private SemanasRecycleAdapter mSemanasAdapter;
    private DatabaseReference databaseSemanas;
    private ProgressBar progress;
    private SweetAlertDialog pDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_semana_dieta, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        databaseSemanas = FirebaseDatabase.getInstance().getReference("semanas");
        databaseSemanas.keepSynced(true);



        mSemanasRecyclerView = (RecyclerView) view.findViewById(R.id.semanas_recycler_view);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        // Create a grid layout with two columns

        mListSemanas = new ArrayList<SemanaDietas>();

        int screen = Constants.deterScreenSize(getContext());
        if(screen ==0 || screen ==1)
        {
            screen = 1;
        }else
        {
            screen = 2;
        }

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(screen, 1);

        mSemanasRecyclerView.setLayoutManager(layoutManager);
        mSemanasAdapter = new SemanasRecycleAdapter();
        mSemanasRecyclerView.setAdapter(mSemanasAdapter);

        cargaSemanas();


    }

    private void cargaSemanas()
    {
        databaseSemanas.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                mListSemanas.clear();

                //iterating through all the nodes
                for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    SemanaDietas semana = postSnapshot.getValue(SemanaDietas.class);

                    mListSemanas.add(semana);

                }

                if (mListSemanas.size() > 0) {

                    progress.setVisibility(View.GONE);
                    mSemanasAdapter.notifyDataSetChanged();
                    mSemanasRecyclerView.setVisibility(View.VISIBLE);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });
    }






    /* adapter*/

    public class SemanasRecycleAdapter extends RecyclerView.Adapter<SemanasActivity.SemanasRecycleHolder> {
        private int lastPosition = -1;

        @Override
        public SemanasActivity.SemanasRecycleHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_semana, viewGroup, false);
            //setAnimation(v,i);
            return new SemanasRecycleHolder(v);
        }


        @Override
        public void onBindViewHolder(final SemanasActivity.SemanasRecycleHolder productHolder, final int i) {

            productHolder.mTitle.setText(mListSemanas.get(i).getTitle());
            productHolder.mSubTitle.setText(mListSemanas.get(i).getSubtitle());


            Glide.with(getActivity()).load(mListSemanas.get(i).getImagen()).into(new SimpleTarget<GlideDrawable>() {
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


            productHolder.mContenedor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(i>=3 && MainActivity.Utemp.getPago().equals("N")) {

                        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                        pDialog.setTitleText(getResources().getString(R.string.app_name));
                        pDialog.setContentText(getResources().getString(R.string.msg_pago));
                        pDialog.setConfirmText(getString(R.string.yes));
                        pDialog.setCancelText(getString(R.string.no));
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();


                                Intent intent = new Intent(getContext(), PayPal.class);
                                intent.putExtra("costo", Constants.costo);
                                startActivity(intent);

                            }
                        });
                        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        });
                        pDialog.show();
                    }else
                    {

                        Intent intent = new Intent(getActivity(), SemanaActivity.class);
                        intent.putExtra("id", String.valueOf(i));
                        startActivity(intent);
                    }




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
            return mListSemanas.size();
        }

        public void removeItem(int position) {
            mListSemanas.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mListSemanas.size());
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

    public class SemanasRecycleHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextView mSubTitle;
        public LinearLayout mContenedor;


        public SemanasRecycleHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            mSubTitle = (TextView) itemView.findViewById(R.id.txtSubtitle);
            mContenedor = (LinearLayout) itemView.findViewById(R.id.contenedor);
        }
    }



}
