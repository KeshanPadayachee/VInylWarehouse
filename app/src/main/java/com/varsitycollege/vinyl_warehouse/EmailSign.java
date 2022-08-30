package com.varsitycollege.vinyl_warehouse;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.varsitycollege.vinyl_warehouse.UserDetails.CurrentUser;
import com.varsitycollege.vinyl_warehouse.UserDetails.UserDetails;
import com.varsitycollege.vinyl_warehouse.Utils.Validation;

public class EmailSign extends AppCompatActivity {
    public static final String LOGIN_SUCCESSFUL = "LOGIN SUCCESSFUL";
    public static final String INCORRECT = "Incorrect";
    public static final String SIGN_IN_WITH_EMAIL_SUCCESS = "signInWithEmail:success";
    public static final String AUTHENTICATION_FAILED_PLEASE_TRY_AGAIN = "Authentication failed. Please try again.";
    public static final String SIGN_IN_WITH_EMAIL_FAILURE = "signInWithEmail:failure";
    private FirebaseAuth mAuth;
    public static final String DEFAULT_IMAGE = "https://firebasestorage.googleapis.com/v0/b/farmcentral-v1.appspot.com/o/icon.png?alt=media&token=71d25606-a2ea-489e-923c-29a2d519318f";
    EditText et_email, et_password;
    Button btn_login;
    ImageView imgEmailSign_BackArrow;
    TextInputLayout txtLayoutEmailSign_Password, txtLayoutEmailSign_EmailAddress;
    ProgressBar progressEmailSignIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mAuth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.edtLogin_EmailAddress);
        et_password = findViewById(R.id.edtLogin_Password);
        txtLayoutEmailSign_Password = findViewById(R.id.txtLayoutEmailSign_Password);
        txtLayoutEmailSign_EmailAddress = findViewById(R.id.txtLayoutEmailSign_EmailAddress);
        progressEmailSignIN = findViewById(R.id.progressEmailSignIN);
        imgEmailSign_BackArrow = findViewById(R.id.imgEmailSign_BackArrow);
        btn_login = findViewById(R.id.btn_login);

        imgEmailSign_BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBackToLogin = new Intent(view.getContext(), IntroWelcome.class);
                startActivity(goBackToLogin);
                finish();
            }
        });
        //validates the email address
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //method unused
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //method unused
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Validation.validateEmail(editable.toString())) {
                    txtLayoutEmailSign_EmailAddress.setError("");
                } else {
                    txtLayoutEmailSign_EmailAddress.setError(INCORRECT);
                }
            }
        });
        //validates the password
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //method unused
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() >= 6) {
                    txtLayoutEmailSign_Password.setError("");
                } else {
                    txtLayoutEmailSign_Password.setError(INCORRECT);
                }
            }
        });
        //login button clicked and validates all fields before processing
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                if (Validation.validateEmail(email)) {
                    if (password!=null){
                        if (password.toString().length() >= 6) {
                            SignIn(email, password);
                        } else {
                            Toast.makeText(EmailSign.this, INCORRECT  + "Password", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(EmailSign.this, INCORRECT + "Password", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(EmailSign.this, INCORRECT + "Email Address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void UpdateUI(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            //userID
            String id = firebaseUser.getUid();
            //Username

            String personName = firebaseUser.getEmail();
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
            Toast.makeText(EmailSign.this, LOGIN_SUCCESSFUL, Toast.LENGTH_LONG).show();


            //Go to main activity
            Intent i = new Intent(EmailSign.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void SignIn(String emailAddress, String password) {
        // Showing the progress dialog
        progressEmailSignIN.setVisibility(View.VISIBLE);
        // Disabling the login button
        btn_login.setEnabled(false);
        mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(EmailSign.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Hiding the progress dialog
                        progressEmailSignIN.setVisibility(View.INVISIBLE);
                        // Enabling the log in button
                        btn_login.setEnabled(true);
                        if (task.isSuccessful()) {
                            //Show user tasks are done working in the background
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, SIGN_IN_WITH_EMAIL_SUCCESS);
                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUI(user);
                        } else {

                            Toast.makeText(EmailSign.this, AUTHENTICATION_FAILED_PLEASE_TRY_AGAIN,
                                    Toast.LENGTH_SHORT).show();

                            Log.w(TAG, SIGN_IN_WITH_EMAIL_FAILURE, task.getException());
                        }
                    }
                });
    }
}