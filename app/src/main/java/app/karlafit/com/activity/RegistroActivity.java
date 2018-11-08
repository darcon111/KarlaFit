package app.karlafit.com.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.karlafit.com.R;
import app.karlafit.com.clases.ImagenCircular.CircleImageView;
import app.karlafit.com.clases.User;
import app.karlafit.com.config.AppPreferences;
import app.karlafit.com.config.Constants;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistroActivity extends AppCompatActivity implements
        IPickResult {

    private Toolbar toolbar;
    private EditText txtEstatura,txtPeso,txtEdad,txtEmail,txtPass,txtNombre,txtApellido;
    private Button btnRegistrar,btnSubir;
    private CheckBox terminos;
    private SweetAlertDialog pDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;
    private static FirebaseUser user;
    private static String provider;
    private static AppPreferences app;
    private static User Utemp;
    private static DatabaseReference databaseUsers;
    private static List<User> mListUser;
    private static final String TAG = RegistroActivity.class.getSimpleName();
    private ValueEventListener listen;
    private static String imagen=null,name;
    private String appbirthday="";
    private FirebaseAuth mAuth;
    private CircleImageView imgPerfil;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private File outPutFile = null;
    private String mCurrentPhotoPath;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_registro);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        /* toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbaruser);

        TextView title = (TextView) findViewById(R.id.txtTitle);

        title.setText(getString(R.string.registro));

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow));
        }

        btnRegistrar=(Button) findViewById(R.id.btnRegistrar);
        btnSubir=(Button) findViewById(R.id.btnSubir);
        terminos=(CheckBox) findViewById(R.id.ckTer);
        txtEstatura=(EditText) findViewById(R.id.txtEstatura);
        txtPeso=(EditText) findViewById(R.id.txtPeso);
        txtEdad=(EditText) findViewById(R.id.txtEdad);
        txtEmail=(EditText) findViewById(R.id.txtEmail);
        txtPass=(EditText) findViewById(R.id.txtPass);
        txtNombre=(EditText) findViewById(R.id.txtNombre);
        txtApellido=(EditText) findViewById(R.id.txtApellido);
        imgPerfil=(CircleImageView) findViewById(R.id.imgPerfil);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mListUser = new ArrayList<User>();
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseUsers.keepSynced(true);
        app = new AppPreferences(getApplicationContext());


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



        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(terminos.isChecked()) {

                    String mensaje = "";
                    if (txtNombre.getText().toString().equals("")) {
                        mensaje += getResources().getString(R.string.error_name)+"\n";
                    }
                    if (txtApellido.getText().toString().equals("")) {
                        mensaje += getResources().getString(R.string.error_apellido)+"\n";
                    }
                    if (txtEmail.getText().toString().equals("")) {
                        mensaje += getResources().getString(R.string.error_email)+"\n";
                    }
                    if (txtPass.getText().toString().equals("")) {
                        mensaje += getResources().getString(R.string.error_pass)+"\n";
                    }
                    if (!mensaje.equals("")) {
                        pDialog = new SweetAlertDialog(RegistroActivity.this, SweetAlertDialog.ERROR_TYPE);
                        pDialog.setTitleText(getResources().getString(R.string.app_name));
                        pDialog.setContentText(mensaje);
                        pDialog.setConfirmText(getResources().getString(R.string.ok));
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        });
                        pDialog.show();
                        return;
                    }else {
                        createUser();
                    }
                }else{
                    pDialog = new SweetAlertDialog(RegistroActivity.this, SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitleText(getResources().getString(R.string.app_name));
                    pDialog.setContentText(getResources().getString(R.string.aceptar_terminos));
                    pDialog.setConfirmText(getResources().getString(R.string.ok));
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    });
                    pDialog.show();
                }




            }
        });

        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });




    }

    private void createUser()
    {
        mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.


                        if (!task.isSuccessful()) {
                            messageFirebase(task);
                        }
                        else
                        {
                            /* correo verificacion */
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()==false) {
                                user.sendEmailVerification();


                                pDialog= new SweetAlertDialog(RegistroActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.setTitleText(getResources().getString(R.string.app_name));
                                pDialog.setContentText(getString(R.string.user_create));
                                pDialog.setConfirmText(getResources().getString(R.string.ok));
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        finish();
                                    }
                                });
                                pDialog.show();

                                insertUser();
                                databaseUsers.removeEventListener(listen);

                            }else
                            {
                                pDialog= new SweetAlertDialog(RegistroActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.setTitleText(getResources().getString(R.string.app_name));
                                pDialog.setContentText(getString(R.string.user_create));
                                pDialog.setConfirmText(getResources().getString(R.string.ok));
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        finish();
                                    }
                                });
                                pDialog.show();

                                insertUser();
                                databaseUsers.removeEventListener(listen);
                            }
                        }

                        // ...
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


    private void messageFirebase(Task<AuthResult> task)
    {
        if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
        {
            new SweetAlertDialog(RegistroActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(getResources().getString(R.string.app_name))
                    .setContentText(getResources().getString(R.string.error_login_pass))
                    .setConfirmText(getResources().getString(R.string.ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                        }
                    })
                    .show();



        }else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
            new SweetAlertDialog(RegistroActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(getResources().getString(R.string.app_name))
                    .setContentText(getResources().getString(R.string.error_user))
                    .setConfirmText(getResources().getString(R.string.ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                        }
                    })
                    .show();
        }else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
            new SweetAlertDialog(RegistroActivity.this, SweetAlertDialog.ERROR_TYPE)
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

    public  void insertUser()
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
                data.setName(txtNombre.getText().toString());
                data.setLastname(txtApellido.getText().toString());
                data.setFecha_nac(appbirthday);
                data.setEdad(txtEdad.getText().toString());
                data.setEstatura(txtEstatura.getText().toString());
                data.setPeso(txtPeso.getText().toString());
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




    }


    public void showFileChooser() {

        PickImageDialog.build(new PickSetup()
                .setTitle(getResources().getString(R.string.image))
                .setTitleColor(getResources().getColor(R.color.colorPrimaryText))
                .setCameraButtonText(getResources().getString(R.string.camera))
                .setGalleryButtonText(getResources().getString(R.string.sd))
                .setButtonTextColor(getResources().getColor(R.color.colorPrimaryText))
                .setBackgroundColor(getResources().getColor(R.color.colorIcons))
                .setCancelText(getResources().getString(R.string.cancelar))
                .setCancelTextColor(getResources().getColor(R.color.colorPrimaryText))
                .setGalleryIcon(R.drawable.ic_perm_media_black_24dp)
                .setCameraIcon(R.drawable.ic_photo_camera_black_24dp)

        ).show(getSupportFragmentManager());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == PICK_FROM_FILE) && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            //bitmap = ProcessImage.compressImage(filePath, getApplicationContext(), null);
            //Getting the Bitmap from Gallery
            performCrop(filePath);

        }
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {

            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(RegistroActivity.this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            performCrop(uri);
                        }
                    });


        }

        if(requestCode==CROP_FROM_CAMERA) {
            try {
                if(outPutFile.exists()){

                    InputStream ims = new FileInputStream(outPutFile);
                    bitmap= BitmapFactory.decodeStream(ims);

                    imagen = Constants.getStringImage(bitmap);
                    imgPerfil.setImageBitmap(bitmap);

                }
                else {
                    Toast.makeText(getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    private void performCrop(Uri uri) {

        int x=dpToPx(280);
        int y=dpToPx(280);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", x);
        intent.putExtra("outputY", y);
        //intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        //intent.putExtra("return-data", true);
        //Create output file here
        try {
            /*mImageCaptureUri = FileProvider.getUriForFile(AddPlatoActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    createImageFile());*/
            outPutFile =createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }



        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));
        startActivityForResult(intent, CROP_FROM_CAMERA);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            r.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),  r.getBitmap(), "temp", null);
            performCrop(Uri.parse(path));

        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
