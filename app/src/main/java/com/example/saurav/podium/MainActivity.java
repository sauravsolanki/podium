package com.example.saurav.podium;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;

    private static final String TAG = MainActivity.class.getName();
    public static String USER_NAME = "ANONYMOUS";

    public static final int RC_SIGN_IN = 1;

    private String sharedPrefFile = "com.example.saurav.podium";
    private SharedPreferences mPreference;
    private boolean isProfiled = false;
    private int FORM_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is sign in
//                    Toast.makeText(MapsActivity.this, "You are signed in buddy", Toast.LENGTH_SHORT).show();
                    USER_NAME = user.getUid();
                    Log.i(TAG, "Current User ID on onAuthStateChanged: " + USER_NAME);
                } else {
                    //user is signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.PhoneBuilder().build()))
//                                    .setTheme(R.style.FullscreenTheme)
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };
        mPreference = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        if (savedInstanceState == null) {
            isProfiled = mPreference.getBoolean("isFormFilled", false);
            if (!isProfiled) {
                //prompt user to fill the form
                showToFillProfileForm();
            } else {
                //form is filled
            }

        } else {
            //form is filled here .
        }
    }

    private void showToFillProfileForm() {
        //Show Form to get filled and stored in firebase database
        Intent intent = new Intent(this, ProfileForm.class);
        startActivityForResult(intent, FORM_ACTIVITY_REQUEST_CODE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FORM_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            //form is filled
            isProfiled = true;
        } else if (requestCode == FORM_ACTIVITY_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            //submit button is not clicked
            Toast.makeText(this, "Form not filled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.logout) {
            AuthUI.getInstance().signOut(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
        SharedPreferences.Editor preferenceEditor = mPreference.edit();
        preferenceEditor.putBoolean("isFormFilled", isProfiled);
        preferenceEditor.apply();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }

}
