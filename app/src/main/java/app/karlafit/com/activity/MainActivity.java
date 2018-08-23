package app.karlafit.com.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.transition.Transition;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import app.karlafit.com.R;
import app.karlafit.com.adapter.MenuAdapter;
import app.karlafit.com.config.AppPreferences;
import app.karlafit.com.config.Constants;
import app.karlafit.com.holder.Semanas;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private RecyclerView mRecyclerView_main,mSemanasRecyclerView;
    private String[] TITLES = new String[4];
    private int[] ICONS = new int[4];
    private ActionBarDrawerToggle mDrawerToggle;

    private int PROFILE = R.drawable.ic_user;
    private MenuAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout Drawer;
    private String TAG = MainActivity.class.getName();
    private String name="Usuario";

    private AppPreferences appPreferences;

    private SweetAlertDialog pDialog;

    private ImageButton btnMenu;

    private ArrayList<Semanas> mListSemanas;
    private SemanasRecycleAdapter mSemanasAdapter;

    private TextView txtReto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appPreferences = new AppPreferences(MainActivity.this);

        /* toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbaruser);

        TextView title = (TextView) findViewById(R.id.txtTitle);

        title.setText(getString(R.string.app_name));

        setSupportActionBar(toolbar);
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_list_white_24dp));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_list_white_24dp));
        }*/

        /*main menu*/
        mRecyclerView_main = (RecyclerView) findViewById(R.id.RecyclerView_main); // Assigning the RecyclerView Object to the xml View
        mRecyclerView_main.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size


        /* menu main*/
        TITLES[0] = getString(R.string.edit);
        TITLES[1] = getString(R.string.help);
        TITLES[2] = getString(R.string.exit);


        ICONS[0] = R.drawable.ic_edit;
        ICONS[1] = R.drawable.ic_help;
        ICONS[2] = R.drawable.ic_exit;

        menu();

        btnMenu=(ImageButton) findViewById(R.id.btnmenu);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Drawer.openDrawer(Gravity.RIGHT);

            }
        });

        mSemanasRecyclerView = (RecyclerView) findViewById(R.id.semanas_recycler_view);
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

        txtReto=(TextView) findViewById(R.id.txtReto);

        txtReto.setBackgroundColor(getResources().getColor(R.color.colorSecondaryText));



    }

    public void menu()
    {


        mAdapter = new MenuAdapter(TITLES, ICONS, "temp", PROFILE, appPreferences.getImagen(), MainActivity.this);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)

        mRecyclerView_main.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView_main.setLayoutManager(mLayoutManager);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view

        int width = getResources().getDisplayMetrics().widthPixels/2;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mRecyclerView_main.getLayoutParams();
        params.width = width;
        mRecyclerView_main.setLayoutParams(params);

        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, Drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if (item != null && item.getItemId() == android.R.id.home) {
                    if (Drawer.isDrawerOpen(Gravity.RIGHT)) {
                        Drawer.closeDrawer(Gravity.RIGHT);
                    }
                    else {
                        Drawer.openDrawer(Gravity.RIGHT);
                    }
                }
                return false;
            }


        }; // Drawer Toggle Object Made

        Drawer.addDrawerListener(mDrawerToggle);

        mRecyclerView_main.addOnItemTouchListener(new Constants.RecyclerTouchListener(getApplicationContext(), mRecyclerView_main, new Constants.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, final int position) {

                Intent intent;

                switch (position) {
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:
                        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.NORMAL_TYPE);
                        pDialog.setTitleText(getResources().getString(R.string.app_name));
                        pDialog.setContentText(getResources().getString(R.string.msg_exit));
                        pDialog.setConfirmText(getString(R.string.yes));
                        pDialog.setCancelText(getString(R.string.no));
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                //signOut();
                                finish();
                            }
                        });
                        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        });
                        pDialog.show();
                        break;
                    case 4:

                        break;

                    case 5:


                        break;

                    case 6:



                        break;


                    default:

                        break;
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDrawerToggle.onDrawerClosed(mRecyclerView_main);
                        Drawer.closeDrawers();
                    }
                }, 200);


            }
        }));
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

            Glide.with(MainActivity.this).load(R.drawable.ic_bg_semanas).into(new SimpleTarget<GlideDrawable>() {
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
                    animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_back_in);
                } else {
                    animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_forward_in);
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
