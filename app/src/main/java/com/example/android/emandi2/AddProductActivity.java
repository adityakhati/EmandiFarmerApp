package com.example.android.emandi2;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddProductActivity extends AppCompatActivity {

    private EditText inputProdName,inputProdPrice,inputProdQuantity,inputProdDescpn,inputProdDate;

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    ImageView mCaptureButton;
    ImageView mImageView;
    Uri image_uri;

    final Calendar myCalendar = Calendar.getInstance();

    private FirebaseAuth auth;
    private Button btnAddProd;
    String product_status;
    private RadioGroup inputStatus;
    private RadioButton inputStatusActive,inputStatusInactive;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment1);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();

        inputStatus=(RadioGroup)findViewById(R.id.prod_status);
        inputProdName = (EditText) findViewById(R.id.prod_name);
        inputProdPrice= (EditText) findViewById(R.id.prod_price);
        inputProdQuantity = (EditText) findViewById(R.id.prod_quantity);
        inputProdDescpn = (EditText) findViewById(R.id.prod_description);
        btnAddProd = (Button) findViewById(R.id.btn_add_product);

        mImageView = findViewById(R.id.camera_open);
        mCaptureButton = findViewById(R.id.camera_open);

        inputProdDate=findViewById(R.id.prodDate);
        inputStatusActive=(RadioButton) findViewById(R.id.status_active);
        inputStatusInactive=(RadioButton) findViewById(R.id.status_inactive);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        inputProdDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddProductActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,PERMISSION_CODE);
                    }
                    else
                    {
                        openCamera();
                    }
                }
                else {
                    openCamera();
                }
            }
        });

        inputStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId==R.id.status_active)
                {
                    inputProdDate.setVisibility(View.GONE);
                    inputProdDate.setText("");
                }
                else if(checkedId==R.id.status_inactive)
                {
                    inputProdDate.setVisibility(View.VISIBLE);

                }
            }
        });

        btnAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String prod_name = inputProdName.getText().toString();
                final String prod_price = inputProdPrice.getText().toString();
                final String prod_quantity = inputProdQuantity.getText().toString();
                final String prod_descpn=inputProdDescpn.getText().toString();
                final String prod_date=inputProdDate.getText().toString();


                if (!prod_name.equals("")&& !prod_price.equals("")&& !prod_quantity.equals("")&& !prod_descpn.equals("")&& !prod_date.equals("")){
                    myRef.child("Prod").child(prod_name).child("Manan").child("rs").setValue(prod_price);
                    myRef.child("Prod").child(prod_name).child("Manan").child("kg").setValue(prod_quantity);
                    myRef.child("Prod").child(prod_name).child("Manan").child("description").setValue(prod_descpn);
                    myRef.child("Prod").child(prod_name).child("Manan").child("Active Date").setValue(prod_date);
                    Toast.makeText(AddProductActivity.this,"Product Added",Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(AddProductActivity.this,DashboardActivity.class);
                    inputProdName.setText("");
                    inputProdPrice.setText("");
                    inputProdQuantity.setText("");
                    inputProdDescpn.setText("");
                    inputProdDate.setText("");
                }
                else {
                    Toast.makeText(AddProductActivity.this,"Fill All Fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        inputProdDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    openCamera();
                }
                else
                {
                    Toast.makeText(AddProductActivity.this,"Permission denied.",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Pictures");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(camera,IMAGE_CAPTURE_CODE);
    }
}