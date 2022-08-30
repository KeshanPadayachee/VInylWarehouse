package com.varsitycollege.vinyl_warehouse;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
//Imports for the Google Sign in API
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
//imports for static and user obj classes
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;
import com.varsitycollege.vinyl_warehouse.UserDetails.UserDetails;

public class IntroWelcome extends AppCompatActivity {
    //Code Attribution
    //Link: https://www.youtube.com/watch?v=SXlidHy-Tb8&t=1s
    //Author: Firebase
    //End
    public static final String DEFAULT_IMAGE = "https://firebasestorage.googleapis.com/v0/b/farmcentral-v1.appspot.com/o/icon.png?alt=media&token=71d25606-a2ea-489e-923c-29a2d519318f";
    public static final String SERVER_CLIENT_ID = "1013453271071-3l9v8qk7g7pn4cip3agjlto9frviu2tv.apps.googleusercontent.com";
    public static final String ONE_TAP_UI = "Couldn't start One Tap UI: ";
    public static final String COOL_DOWN = "Cool down come back in 5 min.";
    public static final String LOGIN_SUCCESSFUL = "LOGIN SUCCESSFUL";
    public static final String LOGIN_FAILED_PLEASE_TRY_AGAIN = "LOGIN FAILED. PLEASE TRY AGAIN";
    //Declaring variables for Google Sign in
    private SignInClient oneTapClient;
    private FirebaseAuth mAuth;
    MaterialButton imgIntroWelcome_GoogleSignIn;
    ProgressBar progressIntroWelcome;
    TextView txvIntroWelcome_Sign, txvIntroWelcome_CreateAccount;
    private static final int REQ_ONE_TAP = 2;


    private BeginSignInRequest signInRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int[] countSign = {4};
        setContentView(R.layout.activity_intro_welcome);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        progressIntroWelcome = findViewById(R.id.progressIntroWelcome);
        txvIntroWelcome_Sign = findViewById(R.id.txvIntroWelcome_Sign);
        txvIntroWelcome_CreateAccount = findViewById(R.id.txvIntroWelcome_CreateAccount);
        //connect GUI components to variables
        imgIntroWelcome_GoogleSignIn = findViewById(R.id.imgIntroWelcome_GoogleSignIn);
        //Constructor for Google sign in
        mAuth = FirebaseAuth.getInstance();
        //get client
        oneTapClient = Identity.getSignInClient(this);

        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(SERVER_CLIENT_ID)
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        // Handling click
        txvIntroWelcome_Sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Showing the progress dialog
                progressIntroWelcome.setVisibility(View.VISIBLE);
                // Disabling the sign in button
                imgIntroWelcome_GoogleSignIn.setEnabled(false);
                Intent intent = new Intent(view.getContext(), EmailSign.class);
                startActivity(intent);
                finish();
            }
        });

        // Handling click
        txvIntroWelcome_CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Showing the progress dialog
                progressIntroWelcome.setVisibility(View.VISIBLE);
                // Disabling the sign in button
                imgIntroWelcome_GoogleSignIn.setEnabled(false);
                Intent intent = new Intent(view.getContext(), EmailCreateAccount.class);
                startActivity(intent);
                finish();
            }
        });
        //Sign in button
        imgIntroWelcome_GoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check how many times the user tries to sign-in
                if (countSign[0] > 1) {
                    //Show sign accounts
                    signIn();
                    countSign[0]--;
                } else {
                    Toast.makeText(IntroWelcome.this, COOL_DOWN, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void signIn() {
        //creates an intent which pops up the google sign in page.

        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
            @Override
            public void onSuccess(BeginSignInResult beginSignInResult) {
                // Showing the progress dialog
                progressIntroWelcome.setVisibility(View.VISIBLE);
                try {
                    loginResultHandler.launch(new IntentSenderRequest.Builder(beginSignInResult.getPendingIntent().getIntentSender()).build());

                } catch (Exception e) {
                    // Hiding the progress dialog
                    progressIntroWelcome.setVisibility(View.INVISIBLE);
                    Log.e(TAG, ONE_TAP_UI + e.getLocalizedMessage());
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Hiding the progress dialog
                progressIntroWelcome.setVisibility(View.INVISIBLE);
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d(TAG, "sigInw" + e.getLocalizedMessage());
            }
        });

    }

    // Getting the google account details
    private ActivityResultLauncher<IntentSenderRequest> loginResultHandler = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
        // handle intent result here
        if (result.getResultCode() == RESULT_OK) {
            SignInCredential credential = null;
            try {
                credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                String idToken = credential.getGoogleIdToken();

            } catch (ApiException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "SigIn: ");
        }
    });

    //method for activity completion
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //checks if the sign in activity is complete and successful
        try {
            String googleCredential = oneTapClient.getSignInCredentialFromIntent(data).getGoogleIdToken();
            AuthCredential credential = GoogleAuthProvider.getCredential(googleCredential, null);
            //here we are checking the Authentication Credential and checking the task is successful or not and display the message
            //based on that.
            mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Hiding the progress dialog
                        progressIntroWelcome.setVisibility(View.INVISIBLE);
                        Toast.makeText(IntroWelcome.this, LOGIN_SUCCESSFUL, Toast.LENGTH_LONG).show();
                        //get signed in user
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        //update details and go to main activity
                        UpdateUI(firebaseUser);
                    } else {
                        // Hiding the progress dialog
                        progressIntroWelcome.setVisibility(View.INVISIBLE);
                        Toast.makeText(IntroWelcome.this, LOGIN_FAILED_PLEASE_TRY_AGAIN, Toast.LENGTH_LONG).show();
                    }
                }
            });

        } catch (ApiException e) {
            // Hiding the progress dialog
            progressIntroWelcome.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        //Get login user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //if null then no user loged in
        //else login the user and go to main activity
        if (currentUser != null) {
            UpdateUI(currentUser);
        }

    }

    private void UpdateUI(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            //userID
            String id = firebaseUser.getUid();
            //Username
            String personName = firebaseUser.getDisplayName();
            //Email address
            String personEmail = firebaseUser.getEmail();
            //profile picture
            Uri personPhoto = firebaseUser.getPhotoUrl();
            //check if user has profile picture
            if (personPhoto == null) {
                personPhoto = Uri.parse(DEFAULT_IMAGE);
            }
            //Update user details
            UserDetails user = new UserDetails(personName, personEmail, id, personPhoto.toString());
            //Store user basic details in runtime
            CurrentUser.currentUserObj = user;
            Toast.makeText(IntroWelcome.this, LOGIN_SUCCESSFUL, Toast.LENGTH_LONG).show();
                //Go to main activity
                Intent i = new Intent(IntroWelcome.this, MainActivity.class);
                startActivity(i);
                finish();

        }
    }


}

