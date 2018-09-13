package app.karlafit.com.activity;

import android.os.Build;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import app.karlafit.com.R;
import app.karlafit.com.holder.Semanas;

public class SemanasActivity extends Fragment {


    private RecyclerView mSemanasRecyclerView;
    private ArrayList<Semanas> mListSemanas;
    private SemanasRecycleAdapter mSemanasAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_semanas, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        mSemanasRecyclerView = (RecyclerView) view.findViewById(R.id.semanas_recycler_view);
        // Create a grid layout with two columns

        mListSemanas = new ArrayList<Semanas>();

        mListSemanas.add(new Semanas("1","semana 1","semana 1 sub","2 libras"));
        mListSemanas.add(new Semanas("2","semana 2","semana 2 sub","3 libras"));
        mListSemanas.add(new Semanas("3","semana 3","semana 3 sub","4 libras"));
        mListSemanas.add(new Semanas("4","semana 4","semana 4 sub","5 libras"));
        mListSemanas.add(new Semanas("5","semana 5","semana 5 sub","6 libras"));
        mListSemanas.add(new Semanas("6","semana 6","semana 6 sub","7 libras"));
        mListSemanas.add(new Semanas("7","semana 6","semana 7 sub","8 libras"));

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, 1);

        mSemanasRecyclerView.setLayoutManager(layoutManager);
        mSemanasAdapter = new SemanasRecycleAdapter();
        mSemanasRecyclerView.setAdapter(mSemanasAdapter);

    }


    /* adapter*/

    public class SemanasRecycleAdapter extends RecyclerView.Adapter<SemanasRecycleHolder> {
        private int lastPosition = -1;

        @Override
        public SemanasRecycleHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_semana, viewGroup, false);
            setAnimation(v,i);
            return new SemanasRecycleHolder(v);
        }


        @Override
        public void onBindViewHolder(final SemanasRecycleHolder productHolder, final int i) {

            productHolder.mTitle.setText(mListSemanas.get(i).getTitle());
            productHolder.mSubTitle.setText(mListSemanas.get(i).getSubtitle());
            productHolder.mLibras.setText(mListSemanas.get(i).getLibras());


            /*Glide.with(MainActivity.this).load(mListSemanas.get(i).getLibras()).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        productHolder.mContenedor.setBackground(resource);
                    }
                }
            });*/

            Glide.with(SemanasActivity.this).load(R.drawable.ic_bg_semanas).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        productHolder.mContenedor.setBackground(resource);
                    }
                }
            });

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
        public TextView mLibras;
        public LinearLayout mContenedor;


        public SemanasRecycleHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            mSubTitle = (TextView) itemView.findViewById(R.id.txtSubtitle);
            mLibras = (TextView) itemView.findViewById(R.id.txtLibras);
            mContenedor = (LinearLayout) itemView.findViewById(R.id.contenedor);
        }
    }

}
