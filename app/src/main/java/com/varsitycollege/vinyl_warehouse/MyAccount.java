package com.varsitycollege.vinyl_warehouse;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.varsitycollege.vinyl_warehouse.DataWorkers.DataDeleteWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseDeleteWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.FirebaseReadWorker;
import com.varsitycollege.vinyl_warehouse.FirebaseWorkers.OnGetDataListener;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;

public class MyAccount extends AppCompatActivity {
    public static final String USER = "User: ";
    private static final String TRUE = "true";
    public static final String CONFIRMATION_DELETION = "Confirmation deletion";
    public static final String DELETE_YOUR_ACCOUNT = "Are you sure you want to delete your account?";
    public static final String EMAIL = "Email: ";
    // Creating variables for the GUI components
    Button btnPrivacyPolicy, btnMyAccount_DeleteAccount;
    TextView txvUserName, txvEmailAddress;
    ProgressBar progressMyAccount;
    ImageView imgBackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Assigning the GUI components to the variables
        setContentView(R.layout.activity_my_account);
        btnPrivacyPolicy = findViewById(R.id.btnMyAccount_PrivacyPolicy);
        btnMyAccount_DeleteAccount = findViewById(R.id.btnMyAccount_DeleteAccount);
        txvEmailAddress = findViewById(R.id.txvMyAccount_EmailAddress);
        txvUserName = findViewById(R.id.txvMyAccount_Username);
        imgBackArrow = findViewById(R.id.imgMyAccount_BackArrow);
        progressMyAccount = findViewById(R.id.progressMyAccount);

        // Getting the current users details
        txvUserName.setText(USER + CurrentUser.currentUserObj.getPersonName());
        txvEmailAddress.setText(EMAIL + CurrentUser.currentUserObj.getPersonEmail());

        // OnClickListener for the Delete Account Button
        btnMyAccount_DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Confirming whether the user wants to delete
                AlertDialog.Builder builder = new AlertDialog.Builder(MyAccount.this);
                builder.setTitle(CONFIRMATION_DELETION);
                builder.setMessage(DELETE_YOUR_ACCOUNT);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Showing the progress dialog
                        progressMyAccount.setVisibility(View.VISIBLE);
                        // Calling the DeleteWorker class
                        DataDeleteWorker dataDeleteWorker = new DataDeleteWorker(CurrentUser.currentUserObj.getPersonId());
                        // Deletes all the users storage data
                        dataDeleteWorker.deleteAll(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                if (o.toString().equals(TRUE)) {
                                    // Switching screens
                                    FirebaseDeleteWorker firebaseDeleteWorker = new FirebaseDeleteWorker(CurrentUser.currentUserObj.getPersonId());
                                    // Deleting all the users realtime data
                                    firebaseDeleteWorker.deleteUser(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {

                                            // Hiding the progress dialog
                                            progressMyAccount.setVisibility(View.INVISIBLE);
                                           // CurrentUser.currentUserObj = null;
                                            Intent intent = new Intent(MyAccount.this, IntroWelcome.class);
                                            startActivity(intent);
                                            finish();
                                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                            firebaseAuth.signOut();

                                        }
                                    });
                                }

                            }
                        });

                    }
                });
                // If the user selects no on alert dialog
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // closing the dialog
                        dialogInterface.dismiss();
                    }
                });
                // Shows the alert dialog
                builder.show();
            }
        });

        // OnClickListener to take the user to the privacy policy page
        btnPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switching layouts
                Intent privacyIntent = new Intent(MyAccount.this, PrivacyPolicy.class);
                startActivity(privacyIntent);
                finish();
            }
        });

        // OnClickListener for the back arrow
        imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switching layouts
                Intent toMainActivity = new Intent(MyAccount.this, MainActivity.class);
                startActivity(toMainActivity);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Checking if user is not found, goes back to intro
        if (CurrentUser.currentUserObj.getPersonId() == null) {
            // Switching layouts
            Intent intent = new Intent(MyAccount.this, IntroWelcome.class);
            startActivity(intent);
            finish();
        }
    }
}