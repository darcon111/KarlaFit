package app.karlafit.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.karlafit.com.R;
import app.karlafit.com.clases.User;
import app.karlafit.com.config.AppPreferences;
import app.karlafit.com.config.Constants;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 1;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;
    private static String imagen,name;

    private LoginButton loginFacebook;

    private Button btnFacebook;

    private static FirebaseUser user;
    private static String provider;
    private static AppPreferences app;
    private static User Utemp;
    private static DatabaseReference databaseUsers;
    private static List<User> mListUser;
    private ValueEventListener listen;
    private EditText txtEmail,txtPass;

    private SweetAlertDialog pDialog;
    private String imagen_perfil="";

    //data facebook
    private String appname="",applastname="";
    private String appbirthday="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /* *************************************
         *                FACEBOOK             *
         ***************************************/

        FacebookSdk.sdkInitialize(getApplicationContext());

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);

        txtEmail= (EditText) findViewById(R.id.txtemail);
        txtPass= (EditText) findViewById(R.id.txtpass);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mListUser = new ArrayList<User>();
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseUsers.keepSynced(true);
        app = new AppPreferences(getApplicationContext());
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "app.karlafit.com",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        // ...
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out" );

                }
                // ...
            }
        };

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        loginFacebook = (LoginButton) findViewById(R.id.button_facebook_login);
        loginFacebook.setReadPermissions("email", "public_profile");

        btnFacebook = (Button) findViewById(R.id.btnfacebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    loginFacebook.performClick();
                    loginFacebook.setPressed(true);
                    loginFacebook.invalidate();
                    loginFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(final LoginResult loginResult) {
                            Log.d(TAG, "facebook:onSuccess:" + loginResult);

                            // App code
                            GraphRequest request = GraphRequest.newMeRequest(
                                    loginResult.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            Log.v("LoginActivity", response.toString());

                                            // Application code
                                            try {


                                                if (Constants.isHasJson(object, "birthday")) {
                                                    appbirthday = object.getString("birthday"); // 01/31/1980 format
                                                }

                                                if (Constants.isHasJson(object, "name")) {
                                                    String[] name = object.getString("name").split(" ");

                                                    appname = name[0];
                                                    applastname = name[1];
                                                }


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            handleFacebookAccessToken(loginResult.getAccessToken());

                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email,gender,birthday");
                            request.setParameters(parameters);
                            request.executeAsync();
                        }

                        @Override
                        public void onCancel() {
                            Log.d(TAG, "facebook:onCancel");
                            // ...
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Log.d(TAG, "facebook:onError", error);
                            // ...
                        }
                    });
                    loginFacebook.setPressed(false);
                    loginFacebook.invalidate();


            }
        });



        btnLogin= (Button) findViewById(R.id.btnlogin);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    login();

            }
        });

        /* App permissions */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && getApplicationContext().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && getApplicationContext().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_CODE);



        }

        listen= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous artist list
                mListUser.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    User temp = postSnapshot.getValue(User.class);
                    //adding artist to the list

                    mListUser.add(temp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseUsers.addValueEventListener(listen);


    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().signOut();
        mAuth.addAuthStateListener(mAuthListener);



    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void auth(String token)
    {

        pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor(getString(R.string.colorAccent)));
        pDialog.setTitleText(getResources().getString(R.string.auth));
        pDialog.setCancelable(true);
        pDialog.show();


        if(token.equals("")) {
            //authenticate user email


            mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtPass.getText().toString())
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.

                            if(!task.isSuccessful())
                            {
                                pDialog.dismiss();
                                messageFirebase(task);
                                return;
                            }

                            if (task.isSuccessful()) {
                                pDialog.dismiss();

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user.isEmailVerified() == false) {
                                    user.sendEmailVerification();



                                    pDialog= new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                    pDialog.setTitleText(getResources().getString(R.string.app_name));
                                    pDialog.setContentText(getResources().getString(R.string.valide_user));
                                    pDialog.setConfirmText(getResources().getString(R.string.ok));
                                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            clear();
                                        }
                                    });
                                    pDialog.show();

                                    return;

                                }
                                insertUser("");

                                databaseUsers.removeEventListener(listen);

                            }
                        }
                    });
        }else {

            mAuth.signInWithCustomToken(token)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.

                            if(!task.isSuccessful())
                            {
                                pDialog.dismiss();
                                LoginManager.getInstance().logOut();
                                FirebaseAuth.getInstance().signOut();
                                messageFirebase(task);
                                return;
                            }else
                            {
                                pDialog.dismiss();
                                insertUser(imagen_perfil);
                                databaseUsers.removeEventListener(listen);
                            }

                        }
                    });
        }




    }







    public  void insertUser(String imagen)
    {



        user = FirebaseAuth.getInstance().getCurrentUser();

        if(mListUser.size()>0)
        {
            for(int i=0;i<mListUser.size();i++)
            {
                if(mListUser.get(i).getEmail().equals(user.getEmail()))
                {
                    Utemp=mListUser.get(i);
                }
            }
        }


        if(Utemp==null)
        {
            if (user != null) {
                List<String> listProvider =user.getProviders();
                provider=listProvider.get(0);
                // User is signed in
                if(user.getPhotoUrl()!=null) {
                    imagen = user.getPhotoUrl().toString();
                }

                try{
                    name=user.getEmail().toString();
                }catch (Exception e)
                {
                    if(user.getDisplayName()!=null) {
                        name = user.getDisplayName().toString();
                    }
                }


                if(imagen==null){imagen="";}
                if(name==null){name="";}

                String id = databaseUsers.push().getKey();
                User data = new User();
                data.setId(id);
                data.setFirebaseId(user.getUid());
                data.setEmail(user.getEmail());
                if(imagen==null){
                    imagen="";
                }
                data.setUrl_imagen(imagen);
                data.setName(appname);
                data.setLastname(applastname);
                data.setFecha_nac(appbirthday);
                data.setEdad("");
                data.setEstatura("");
                data.setPeso("");
                data.setLat("0");
                data.setLog("0");
                data.setFirebase_code("");
                Date date = new Date();
                data.setMobile("1");
                data.setDate_created(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date));
                data.setProvider(provider);
                data.setPago("N");
                //Saving
                databaseUsers.child(id).setValue(data);
                app.setUserId(id);
                app.setImagen(imagen);


            }
        }else
        {
            app.setUserId(Utemp.getId());
            app.setImagen(imagen);
            if(!imagen.equals("")) {
                databaseUsers.child(Utemp.getId()).child("url_imagen").setValue(imagen);


                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(imagen))
                        .build();

                user.updateProfile(profile)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                }
                            }
                        });

            }


        }

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();
                    Log.e(TAG, "Permission Granted, Now you can access location data.");
                    restartActivity(this);

                } else {
                    Log.e(TAG, "Permission Denied, You cannot access location data.");



                    pDialog= new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitleText(getResources().getString(R.string.app_name));
                    pDialog.setContentText(getResources().getString(R.string.permissions));
                    pDialog.setConfirmText(getResources().getString(R.string.ok));
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            finish();
                        }
                    });
                    pDialog.show();


                }
                break;
        }
    }

    public static void restartActivity(Activity actividad) {
        Intent intent = new Intent();
        intent.setClass(actividad, actividad.getClass());
        actividad.startActivity(intent);
        actividad.finish();
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {

                            imagen_perfil ="https://graph.facebook.com/"+token.getUserId()+"/picture?type=large";
                            databaseUsers.removeEventListener(listen);
                            insertUser(imagen_perfil);
                            //auth(token.getToken().toString());



                        }else
                        {
                            LoginManager.getInstance().logOut();
                            FirebaseAuth.getInstance().signOut();
                            messageFirebase(task);
                            //auth(token.getToken().toString());
                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }





    public void registro(View v)
    {
        Intent intent = new Intent(LoginActivity.this,RegistroActivity.class);
        startActivity(intent);
    }

    public void recuperar(View v)
    {

        pDialog= new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitleText(getResources().getString(R.string.app_name));
        pDialog.setContentText(getResources().getString(R.string.restabler_msg));
        pDialog.setConfirmText(getResources().getString(R.string.ok));
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
                Intent intent = new Intent(LoginActivity.this,RecuperarActivity.class);
                startActivity(intent);
            }
        });
        pDialog.show();

    }

    public void login()
    {
        if(txtEmail.getText().toString().equals("") || !Constants.validateEmail(txtEmail.getText().toString()))
        {
            txtEmail.setError(getString(R.string.error_mail));
            return ;
        }
        if(txtPass.getText().toString().equals(""))
        {
            txtPass.setError(getString(R.string.error_pass));
            return ;
        }



        mAuth.fetchSignInMethodsForEmail(txtEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                if(task.getResult().getSignInMethods().size()>0){
                    if(task.getResult().getSignInMethods().get(0).equals("password"))
                    {
                        auth("");
                    }else if(task.getResult().getSignInMethods().get(0).equals("facebook.com"))
                    {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getResources().getString(R.string.app_name))
                                .setContentText(getResources().getString(R.string.user_facebook))
                                .setConfirmText(getResources().getString(R.string.ok))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();

                                    }
                                })
                                .show();

                    }
                }

               else
                {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(getResources().getString(R.string.app_name))
                            .setContentText(getResources().getString(R.string.error_user_exite))
                            .setConfirmText(getResources().getString(R.string.ok))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                }
                            })
                            .show();
                }
            }
        });




    }

    public void clear()
    {
        txtEmail.setText("");
        txtPass.setText("");

    }

    private void messageFirebase(Task<AuthResult> task)
    {
        if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
        {
            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(getResources().getString(R.string.app_name))
                    .setContentText(getResources().getString(R.string.error_login_pass))
                    .setConfirmText(getResources().getString(R.string.ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            txtPass.setText("");
                        }
                    })
                    .show();



        }else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(getResources().getString(R.string.app_name))
                    .setContentText(getResources().getString(R.string.error_user))
                    .setConfirmText(getResources().getString(R.string.ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            clear();
                        }
                    })
                    .show();
        }else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(getResources().getString(R.string.app_name))
                    .setContentText(getResources().getString(R.string.error_user_exite))
                    .setConfirmText(getResources().getString(R.string.ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            clear();
                        }
                    })
                    .show();
        }




    }


}
