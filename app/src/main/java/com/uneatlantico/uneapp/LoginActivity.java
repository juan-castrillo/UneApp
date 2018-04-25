package com.uneatlantico.uneapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.uneatlantico.uneapp.Inicio.InicioActivity;
import com.uneatlantico.uneapp.db.UneAppExecuter;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 9001;
    GoogleApiClient mGoogleApiClient;
    SignInButton signInButton;
    Button signOutButton;
    UneAppExecuter uneAppExecuter = new UneAppExecuter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(checkUsuario())
            startNewActivity();

        else {
            setTheme(R.style.AppTheme);

            setContentView(R.layout.activity_login);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestProfile()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            signInButton = findViewById(R.id.sign_in_button);
            signInButton.setSize(1);
            signInButton.setOnClickListener(this);

            signOutButton = findViewById(R.id.signOutButton);
            signOutButton.setOnClickListener(this);
        }
    }

    private boolean checkUsuario(){
        Boolean check = false;
        try {
            Short usuario = uneAppExecuter.devolverUsuario();
            if (usuario == 1)
                check = true;
        }
        catch (Exception e) {

        }
        return check;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.signOutButton:
                signOut();
                break;
        }
    }

    private void mensaje(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg).setTitle("Advertencia Debug");
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            String[] partesMail;
            try {
                partesMail = acct.getEmail().split("@", -1);
                if(partesMail[1].equals("alumnos.uneatlantico.es")) {
                    insertUsuarioDB(acct);
                    startNewActivity();
                }
                else
                    mensaje("Utilice su correo de la Universidad del Atlantico");
            }
            catch (Exception e){ Log.d("usuarioNoInsertado", e.getMessage());}
        }
    }

    private void insertUsuarioDB(GoogleSignInAccount acct) {
        try {

            String nombre = acct.getDisplayName();

            String email = acct.getEmail();
            String[] idPersona = email.split("@");
            String photoUrl = acct.getPhotoUrl().toString();
            List<String> listaTemp =  Arrays.asList(idPersona[0], nombre, email, photoUrl);

            uneAppExecuter.insertarUsuario(listaTemp);
        }
        catch (Exception e){
            Log.d("noInsertadoLogin", e.getMessage());
        }
    }

    private void startNewActivity(){
        Intent i = new Intent(this, InicioActivity.class);
        //i.putExtra("account", acct);

        //Log.d("jsonaccount" ,acct.toJson());
         //Kill the activity from which you will go to next activity
        startActivity(i);
        finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        mensaje("onConnectionFailed:" + connectionResult);
    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                mensaje("estas fuera");
            }
        });
    }


}
