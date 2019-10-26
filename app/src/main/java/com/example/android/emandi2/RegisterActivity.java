package com.example.android.emandi2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputEmail,inputPassword,inputRePassword,inputName,inputLocation;
    private FirebaseAuth auth;
    private Button btnSignUp, btnLogin;
    private ProgressDialog PD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        //Get FireBase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }

        inputEmail = (EditText) findViewById(R.id.username_signup);
        inputPassword = (EditText) findViewById(R.id.password_signup);
        inputRePassword = (EditText) findViewById(R.id.cpassword_signup);
        inputName=(EditText)findViewById(R.id.name_signup);
        inputLocation=(EditText) findViewById(R.id.location_signup);
        btnSignUp = (Button) findViewById(R.id.btn_signup);


        // Validating Credits entered by User with Firebase
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = inputEmail.getText().toString();
                final String re_pass = inputRePassword.getText().toString();
                final String password = inputPassword.getText().toString();
                final String name= inputName.getText().toString();
                final String location= inputLocation.getText().toString();

                if (!email.equals("") && !re_pass.equals("")&& !password.equals("")&& !name.equals("")&& !location.equals("")) {


                    if (password.equals(re_pass)) {

                        try {
                            // Email ID must be valid
                            // Password strength is minimum 6 characters by default in firebase registration
                            // Minimum Password length throws Error as 'WEAK PASSWORD'
                            if (password.length() > 0 && email.length() > 0) {
                                PD.show();
                                //authenticate user
                                auth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (!task.isSuccessful()) {
                                                    try {
                                                        throw task.getException();
                                                    } catch (FirebaseAuthWeakPasswordException e) {
                                                        inputPassword.setError(getString(R.string.error_weak_password));
                                                        inputPassword.requestFocus();
                                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                                        inputEmail.setError(getString(R.string.error_invalid_email));
                                                        inputEmail.requestFocus();
                                                    } catch (FirebaseAuthUserCollisionException e) {
                                                        inputEmail.setError(getString(R.string.error_user_exists));
                                                        inputEmail.requestFocus();
                                                    } catch (Exception e) {
                                                        //Log.e(TAG, e.getMessage());
                                                    }
                                                } else {
                                                    new AddtodatabaseRegisterActivity(email,name,location);
                                                    Intent intent = new Intent(RegisterActivity.this, SplashScreenActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                PD.dismiss();
                                            }
                                        });
                            } else {
                                Toast.makeText(
                                        RegisterActivity.this,
                                        "Fill All Fields",
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        inputRePassword.setError("Password does not match");
                        inputRePassword.setText("");
                        inputPassword.setText("");
                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Fill All Fields",Toast.LENGTH_SHORT).show();
                }
            }

        });


        // Link to Login Screen
        /*btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

    }
}
