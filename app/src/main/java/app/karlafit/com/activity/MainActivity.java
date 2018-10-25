package app.karlafit.com.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
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
import android.util.Log;
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
import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import app.karlafit.com.R;
import app.karlafit.com.adapter.MenuAdapter;
import app.karlafit.com.clases.User;
import app.karlafit.com.config.AppPreferences;
import app.karlafit.com.config.Constants;
import app.karlafit.com.holder.Semanas;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private RecyclerView mRecyclerView_main;
    private String[] TITLES = new String[4];
    private int[] ICONS = new int[4];
    private ActionBarDrawerToggle mDrawerToggle;
    private static FirebaseUser user;
    private int PROFILE = R.drawable.ic_user;
    private MenuAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout Drawer;
    private String TAG = MainActivity.class.getName();
    private String name="Usuario";
    private DatabaseReference databaseUsers;
    private AppPreferences appPreferences;
    private User Utemp;
    private SweetAlertDialog pDialog;
    private String provider;
    private String imagen;

    private ImageButton btnMenu;

    private TextView txtReto;

    private ArrayList<Semanas> mListSemanas= new ArrayList<Semanas>();

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



        btnMenu=(ImageButton) findViewById(R.id.btnmenu);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Drawer.openDrawer(Gravity.RIGHT);

            }
        });

//Initializing the tablayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition()==0)
                {
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.contenedor, new SemanasActivity(), "SOMETAG").
                            commit();
                }else if(tab.getPosition()==1)
                {
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.contenedor, new DietaActivity(), "SOMETAG").
                            commit();
                }else
                {
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.contenedor, new RecetasActivity(), "SOMETAG").
                            commit();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        if (savedInstanceState == null) {
            // Let's first dynamically add a fragment into a frame container
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.contenedor, new SemanasActivity(), "SOMETAG").
                    commit();

            // Now later we can lookup the fragment by tag
            //DemoFragment fragmentDemo = (DemoFragment)
                //    getSupportFragmentManager().findFragmentByTag("SOMETAG");
        }


        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseUsers.keepSynced(true);





        user = FirebaseAuth.getInstance().getCurrentUser();

        com.google.firebase.database.Query userquery = databaseUsers
                .orderByChild("email").equalTo(user.getEmail());

        userquery.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Utemp = postSnapshot.getValue(User.class);
                    

                    if (user != null) {
                        List<String> listProvider = user.getProviders();
                        provider = listProvider.get(0);

                        if(Utemp.getUrl_imagen()!=null){

                            if(!Utemp.getUrl_imagen().equals(""))
                            {
                                imagen=Utemp.getUrl_imagen().toString();
                            }else
                            {
                                if (user.getPhotoUrl() != null) {
                                    imagen = user.getPhotoUrl().toString();
                                }
                            }

                        }else {
                            // User is signed in
                            if (user.getPhotoUrl() != null) {
                                imagen = user.getPhotoUrl().toString();
                            }
                        }



                        if (Utemp == null) {
                            try {
                                name = user.getEmail().toString();
                            } catch (Exception e) {
                                if (user.getDisplayName() != null) {
                                    name = user.getDisplayName().toString();
                                }
                            }
                        } else {
                            if (!Utemp.getName().equals("")) {
                                name = Utemp.getName() + " " + Utemp.getLastname();
                            }
                        }


                        if (imagen == null) {
                            imagen = "";
                        }
                        if (name == null) {
                            name = "";
                        }
                    }




                }
                menu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });


    }

    public void menu()
    {


        mAdapter = new MenuAdapter(TITLES, ICONS, name, PROFILE, imagen, MainActivity.this);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)

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
                                signOut();
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




    private void select(int i)
    {
        switch (i)
        {
            case 1:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.contenedor, new SemanasActivity(), "SOMETAG").
                        commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.contenedor, new DietaActivity(), "SOMETAG").
                        commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.contenedor, new RecetasActivity(), "SOMETAG").
                        commit();
                break;
        }

    }

    //sign out method
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
    }



}
