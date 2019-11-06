package com.example.android.emandi2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.master.glideimageview.GlideImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Random;

public class AddProductActivity2 extends AppCompatActivity {
    private EditText inputProdName, inputProdPrice, inputProdQuantity, inputProdDescpn, inputProdDate;
    private static final String TAG = "UploadActivity";

    private Button btnChoose, btnUpload,btnback,btnShow;
    private GlideImageView glideImageView;
    private ImageView imageView;

    int random;

    //Firebase
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private Uri filePath;

    private String url,uid,prod,type;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Intent intent=getIntent();
        type=intent.getStringExtra("type");
        prod=intent.getStringExtra("prod");
        //Initialize Views
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView = (ImageView) findViewById(R.id.imgView);

        auth = FirebaseAuth.getInstance();
        FirebaseUser userid=auth.getCurrentUser();
        uid=userid.getUid();


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

    }

    private void ShowImage(){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference ref = storage.getReference();

        url=getUrl(ref);
        Picasso.get()
                .load(url)
                .into(imageView);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            random = new Random().nextInt((10000 - 1000) + 1) + 1000;

            String str = "User/" + FirebaseAuth.getInstance().getUid() + "/" + random;

            FirebaseStorage storage = FirebaseStorage.getInstance();

            final StorageReference ref = storage.getReference();

            // ref = storageReference.child("images/"+ str);
            ref.getRoot().child(str).putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            url=getUrl(ref);
                            progressDialog.dismiss();
                            Toast.makeText(AddProductActivity2.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddProductActivity2.this,DashboardActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddProductActivity2.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }


    public String getUrl(StorageReference ref) {

        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        String str = "User/" + FirebaseAuth.getInstance().getUid() + "/" + random;
        ref.child(str).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                url=uri.toString();
                myRef.child("product2").child(type).child(prod).child(FirebaseAuth.getInstance().getUid()).child("url").setValue(url);
                Toast.makeText(AddProductActivity2.this,url,Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        return url;
    }
}
