package com.example.android.emandi2;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class AddProductActivity extends AppCompatActivity {

    private EditText inputProdPrice, inputProdQuantity, inputProdDescpn, inputProdDate;

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    ImageView mCaptureButton;
    ImageView mImageView;
    Uri image_uri;

    int random;

    ImageView img_upload_btn, img_profile_pic;
    String url, uid,type,prod;

    Bitmap photo;
    private static final int CAMERA_REQ = 1;

    final Calendar myCalendar = Calendar.getInstance();

    private FirebaseAuth auth;
    private Button btnAddProd;
    String product_status;
    private RadioGroup inputStatus;
    private RadioButton inputStatusActive, inputStatusInactive;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment1);

        Intent intent=getIntent();
        type=intent.getStringExtra("type");
        prod=intent.getStringExtra("prod");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();

        inputStatus = (RadioGroup) findViewById(R.id.prod_status);
        inputProdPrice = (EditText) findViewById(R.id.prod_price);
        inputProdQuantity = (EditText) findViewById(R.id.prod_quantity);
        inputProdDescpn = (EditText) findViewById(R.id.prod_description);
        btnAddProd = (Button) findViewById(R.id.btn_add_product);

        mImageView = findViewById(R.id.camera_open);
        mCaptureButton = findViewById(R.id.camera_open);

        inputProdDate = findViewById(R.id.prodDate);
        inputStatusActive = (RadioButton) findViewById(R.id.status_active);
        inputStatusInactive = (RadioButton) findViewById(R.id.status_inactive);


        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQ);

            }
        });


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

        inputProdDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddProductActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                return false;
            }
        });

/*        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        openCamera();
                    }
                } else {
                    openCamera();
                }
            }
        });*/
        inputStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.status_active) {
                    inputProdDate.setVisibility(View.GONE);
                    inputProdDate.setText("");
                } else if (checkedId == R.id.status_inactive) {
                    inputProdDate.setVisibility(View.VISIBLE);
                }
            }
        });

        btnAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String uid = FirebaseAuth.getInstance().getUid();
                final String prod_price = inputProdPrice.getText().toString();
                final String prod_quantity = inputProdQuantity.getText().toString();
                final String prod_descpn = inputProdDescpn.getText().toString();
                final String prod_date = inputProdDate.getText().toString();

                if ( !prod_price.equals(uid) && !prod_quantity.equals("") && !prod_descpn.equals("") && inputStatus.getCheckedRadioButtonId() == R.id.status_active) {
                    myRef.child("Users").child(uid).child("Prod").child(prod).setValue(type);
                    myRef.child("product2").child(type).child(prod).child(uid).child("rs").setValue(prod_price);
                    myRef.child("product2").child(type).child(prod).child(uid).child("kg").setValue(prod_quantity);
                    myRef.child("product2").child(type).child(prod).child(uid).child("description").setValue(prod_descpn);
                    Toast.makeText(AddProductActivity.this, "Product Added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddProductActivity.this, AddProductActivity2.class);
                    intent.putExtra("type",type);
                    intent.putExtra("prod",prod);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddProductActivity.this, "Fill All Fields", Toast.LENGTH_SHORT).show();
                }
                if (inputStatus.getCheckedRadioButtonId() == R.id.status_inactive && !prod_date.equals("") && !prod.equals("") && !prod_price.equals(uid) && !prod_quantity.equals("") && !prod_descpn.equals("")) {
                    myRef.child(uid).child("Prod").child(prod).setValue("1");
                    myRef.child("product2").child(type).child(prod).child(uid).child("rs").setValue(prod_price);
                    myRef.child("product2").child(type).child(prod).child(uid).child("kg").setValue(prod_quantity);
                    myRef.child("product2").child(type).child(prod).child(uid).child("description").setValue(prod_descpn);
                    myRef.child("product2").child(type).child(prod).child(uid).child("Active Date").setValue(prod_date);
                    Toast.makeText(AddProductActivity.this, "Product Added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddProductActivity.this, AddProductActivity2.class);
                    intent.putExtra("type",type);
                    intent.putExtra("prod",prod);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddProductActivity.this, "Fill All Fields", Toast.LENGTH_SHORT).show();
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
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(AddProductActivity.this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Pictures");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(camera, IMAGE_CAPTURE_CODE);
    }


    public void submit() {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] b = stream.toByteArray();

        random = new Random().nextInt((10000 - 1000) + 1) + 1000;
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("User/" + FirebaseAuth.getInstance().getUid() + "/" + random);
        //StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userID);
        storageReference.putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddProductActivity.this, "uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProductActivity.this, "failed", Toast.LENGTH_LONG).show();


            }
        });

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQ && resultCode == RESULT_OK) {

            photo = (Bitmap) data.getExtras().get("data");
            submit();
        }
    }
}