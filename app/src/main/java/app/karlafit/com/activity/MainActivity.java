package app.karlafit.com.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import app.karlafit.com.R;
import app.karlafit.com.adapter.MenuAdapter;
import app.karlafit.com.config.AppPreferences;
import app.karlafit.com.config.Constants;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private RecyclerView mRecyclerView_main;
    private String[] TITLES = new String[8];
    private int[] ICONS = new int[8];
    private ActionBarDrawerToggle mDrawerToggle;

    private int PROFILE = R.drawable.ic_user;
    //private RecyclerView.Adapter mAdapter;
    private MenuAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout Drawer;
    private String TAG = MainActivity.class.getName();
    private String name="Usuario";

    private AppPreferences appPreferences;

    private SweetAlertDialog pDialog;

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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_list_white_24dp));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_list_white_24dp));
        }

        /*main menu*/
        mRecyclerView_main = (RecyclerView) findViewById(R.id.RecyclerView_main); // Assigning the RecyclerView Object to the xml View
        mRecyclerView_main.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size


        /* menu main*/
        TITLES[0] = getString(R.string.app_name);
        TITLES[1] = getString(R.string.app_name);
        TITLES[2] = getString(R.string.app_name);
        TITLES[3] = getString(R.string.app_name);
        TITLES[4] = getString(R.string.app_name);
        TITLES[5] = getString(R.string.app_name);

        ICONS[0] = R.drawable.ic_user;
        ICONS[1] = R.drawable.ic_user;
        ICONS[2] = R.drawable.ic_user;
        ICONS[3] = R.drawable.ic_user;
        ICONS[4] = R.drawable.ic_user;
        ICONS[5] = R.drawable.ic_user;

        menu();
    }

    public void menu()
    {


        mAdapter = new MenuAdapter(TITLES, ICONS, appPreferences.getUser(), PROFILE, appPreferences.getImagen(), MainActivity.this);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)

        mRecyclerView_main.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView_main.setLayoutManager(mLayoutManager);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
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

                        break;
                    case 4:

                        break;

                    case 5:


                        break;

                    case 6:

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


}
