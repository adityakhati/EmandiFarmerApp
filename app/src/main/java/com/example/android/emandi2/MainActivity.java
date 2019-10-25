package com.example.android.emandi2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LogOut
        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth=FirebaseAuth.getInstance();
                auth.signOut();

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();

                // this listener will be called when there is change in firebase user session
                FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            startActivity(new Intent(MainActivity.this, OpeningActivity.class));
                            finish();
                        }
                    }
                };
            }
        });
    }

    @Override
    protected void onResume() {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, OpeningActivity.class));
            finish();
        }
        super.onResume();
    }
}
