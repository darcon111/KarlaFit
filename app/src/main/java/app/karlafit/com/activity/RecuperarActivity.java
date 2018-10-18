package app.karlafit.com.activity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import app.karlafit.com.R;
import app.karlafit.com.config.Constants;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class RecuperarActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private Button btnenviar;
    private EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_recuperar);


        /* toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbaruser);

        TextView title = (TextView) findViewById(R.id.txtTitle);
        btnenviar=(Button) findViewById(R.id.btnenviar);
        txtEmail=(EditText) findViewById(R.id.txtEmail);

        title.setText(getString(R.string.recuperar));

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow));
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

            btnenviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Constants.validateEmail(txtEmail.getText().toString()))
                    {


                        mAuth.fetchSignInMethodsForEmail(txtEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                if (task.getResult().getSignInMethods().size() > 0) {
                                    if (task.getResult().getSignInMethods().get(0).equals("password")) {
                                        mAuth.sendPasswordResetEmail(txtEmail.getText().toString());


                                        new SweetAlertDialog(RecuperarActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText(getResources().getString(R.string.app_name))
                                                .setContentText("Por favor revise su email!!!")
                                                .setConfirmText(getResources().getString(R.string.ok))
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                            finish();
                                                    }
                                                })
                                                .show();

                                    }else
                                    {
                                        new SweetAlertDialog(RecuperarActivity.this, SweetAlertDialog.ERROR_TYPE)
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
                                } else {
                                    new SweetAlertDialog(RecuperarActivity.this, SweetAlertDialog.ERROR_TYPE)
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





                    }else
                    {
                        new SweetAlertDialog(RecuperarActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(getResources().getString(R.string.app_name))
                                .setContentText(getResources().getString(R.string.error_email))
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
