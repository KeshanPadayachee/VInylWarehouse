package com.varsitycollege.vinyl_warehouse;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.varsitycollege.vinyl_warehouse.Utils.Validation;

public class EmailCreateAccount extends AppCompatActivity {
    //creates firebase auth
    private FirebaseAuth mAuth;
    //declare UI variable components
    EditText et_email, et_password;
    Button btn_sign_up;
    ImageView imgEmailCreate_BackArrow;
    ProgressBar progressCreateAccount;
    TextInputLayout txtLayoutEmailCreateAccount_Password, txtLayoutEmailCreateAccount_EmailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //assigns the UI variables to the  UI components
        setContentView(R.layout.activity_email_create_account);
        //set screen orientation for activity to portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mAuth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.edtLogin_EmailAddress);
        et_password = findViewById(R.id.edtLogin_Password);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        txtLayoutEmailCreateAccount_EmailAddress = findViewById(R.id.txtLayoutEmailCreateAccount_EmailAddress);
        txtLayoutEmailCreateAccount_Password = findViewById(R.id.txtLayoutEmailCreateAccount_Password);
        imgEmailCreate_BackArrow = findViewById(R.id.imgEmailCreate_BackArrow);
        progressCreateAccount = findViewById(R.id.progressCreateAccount);
        //checks if the back button is clicked
        imgEmailCreate_BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goes to the intro page
                Intent goBackToLogin = new Intent(view.getContext(), IntroWelcome.class);
                startActivity(goBackToLogin);
                finish();
            }
        });
        //validation for the email
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
                //checks if the email matches the email requirements and shows if incorrect
                if (Validation.validateEmail(editable.toString())) {
                    txtLayoutEmailCreateAccount_EmailAddress.setError("");
                } else {
                    txtLayoutEmailCreateAccount_EmailAddress.setError("Incorrect");
                }
            }
        });
        //checks if the password is valid
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //checks if the password meets password requirements
                if (editable.toString().length() >= 6) {
                    txtLayoutEmailCreateAccount_Password.setError("");
                } else {
                    txtLayoutEmailCreateAccount_Password.setError("Incorrect");
                }
            }
        });
        //sign up button is clicked
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gets data from UI
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                //checks if the email and password is valid and calls the sign up method
                if (Validation.validateEmail(email)) {
                    if (password != null) {
                        if (password.length() >= 6) {
                            signUp(email, password);
                        } else {
                            Toast.makeText(EmailCreateAccount.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EmailCreateAccount.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(EmailCreateAccount.this, "Incorrect Email Address", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    //method used to create an account with email and password
    private void signUp(String emailAddress, String password) {
        // Showing the progress dialog
        progressCreateAccount.setVisibility(View.VISIBLE);
        // Disabling the sign up button
        btn_sign_up.setEnabled(false);
        //calls method
        mAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(EmailCreateAccount.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Showing the progress dialog
                        progressCreateAccount.setVisibility(View.INVISIBLE);
                        // Enabling the sign up button
                        btn_sign_up.setEnabled(true);
                        //check if user creation was successful
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(EmailCreateAccount.this, "ACCOUNT SUCCESSFULLY CREATED", Toast.LENGTH_LONG).show();
                            //Sign out
                            FirebaseUser user = mAuth.getCurrentUser();
                            mAuth.signOut();
                            // Switching layouts
                            Intent intent = new Intent(EmailCreateAccount.this, EmailSign.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailCreateAccount.this, "Authentication failed. Please try again",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}