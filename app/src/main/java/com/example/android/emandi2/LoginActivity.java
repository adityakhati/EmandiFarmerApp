package com.example.android.emandi2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private Button btnSignUp, btnLogin;
    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        //Get FireBase auth instance
        auth = FirebaseAuth.getInstance();

        inputEmail = (TextInputEditText) findViewById(R.id.username_login);
        inputPassword = (TextInputEditText) findViewById(R.id.password_login);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);

        // Validating Login Credits with Firebase
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                try {

                    if (password.length() > 0 && email.length() > 0) {
                        PD.show();
                        // Authenticate user
                        auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                            switch (errorCode) {
                                                case "ERROR_INVALID_CUSTOM_TOKEN":
                                                    Toast.makeText(LoginActivity.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                                    break;

                                                case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                                    Toast.makeText(LoginActivity.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                                    break;

                                                case "ERROR_INVALID_CREDENTIAL":
                                                    Toast.makeText(LoginActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                                    break;

                                                case "ERROR_INVALID_EMAIL":
                                                    Toast.makeText(LoginActivity.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                                    inputEmail.setError("The email address is badly formatted.");
                                                    inputEmail.requestFocus();
                                                    break;

                                                case "ERROR_WRONG_PASSWORD":
                                                    Toast.makeText(LoginActivity.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                                    inputPassword.setError("password is incorrect ");
                                                    inputPassword.requestFocus();
                                                    inputPassword.setText("");
                                                    break;

                                                case "ERROR_USER_MISMATCH":
                                                    Toast.makeText(LoginActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                                    break;

                                                case "ERROR_REQUIRES_RECENT_LOGIN":
                                                    Toast.makeText(LoginActivity.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                                    break;

                                                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                                    Toast.makeText(LoginActivity.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                                    break;

                                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                                    Toast.makeText(LoginActivity.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                                    inputEmail.setError("The email address is already in use by another account.");
                                                    inputEmail.requestFocus();
                                                    break;

                                                case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                                    Toast.makeText(LoginActivity.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                                    break;

                                                case "ERROR_USER_DISABLED":
                                                    Toast.makeText(LoginActivity.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                                    break;

                                                case "ERROR_USER_TOKEN_EXPIRED":
                                                    Toast.makeText(LoginActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                                    break;

                                                case "ERROR_USER_NOT_FOUND":
                                                    Toast.makeText(LoginActivity.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                                    break;

                                                case "ERROR_INVALID_USER_TOKEN":
                                                    Toast.makeText(LoginActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                                    break;

                                                case "ERROR_OPERATION_NOT_ALLOWED":
                                                    Toast.makeText(LoginActivity.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                                    break;

                                                case "ERROR_WEAK_PASSWORD":
                                                    Toast.makeText(LoginActivity.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                                                    inputPassword.setError("The password is invalid it must 6 characters at least");
                                                    inputPassword.requestFocus();
                                                    break;

                                            }
                                            inputEmail.setText("");
                                            inputPassword.setText("");

                                        } else {
                                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        PD.dismiss();
                                    }
                                });
                    } else {
                        Toast.makeText(
                                LoginActivity.this,
                                "Fill All Fields",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Link to Registration Screen

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Link to Forget Password Screen
            /*findViewById(R.id.btn_forgot_pass).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 0));
                }
            });*/

    }
}
