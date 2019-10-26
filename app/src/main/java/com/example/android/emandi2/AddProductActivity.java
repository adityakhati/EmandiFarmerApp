package com.example.android.emandi2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProductActivity extends AppCompatActivity {
    private EditText inputProdName,inputProdPrice,inputProdQuantity;
    private FirebaseAuth auth;
    private Button btnAddProd;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment1);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();

        inputProdName = (EditText) findViewById(R.id.prod_name);
        inputProdPrice= (EditText) findViewById(R.id.prod_price);
        inputProdQuantity = (EditText) findViewById(R.id.prod_quantity);
        btnAddProd = (Button) findViewById(R.id.btn_add_product);

        btnAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String prod_name = inputProdName.getText().toString();
                final String prod_price = inputProdPrice.getText().toString();
                final String prod_quantity = inputProdQuantity.getText().toString();

                if (!(prod_name.equals("")&&prod_price.equals("")&&prod_quantity.equals(""))){
                    myRef.child("Prod").child(prod_name).child("Manan").child("rs").setValue(prod_price);
                    myRef.child("Prod").child(prod_name).child("Manan").child("kg").setValue(prod_quantity);
                    Toast.makeText(AddProductActivity.this,"Product Added",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AddProductActivity.this,"Fill All Fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}