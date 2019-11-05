package Products;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.emandi2.DashboardActivity;
import com.example.android.emandi2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDescriptionActivity extends AppCompatActivity {

    TextView textViewRs, textViewName, textViewSold,textViewDes, textViewUnits;
    ImageView imageViewProd;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth auth;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_description);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("farmer");
        final String prod = intent.getStringExtra("prod");

        textViewRs = findViewById(R.id.tv_prod_rs);
        textViewName = findViewById(R.id.tv_prod_name);
        textViewSold = findViewById(R.id.tv_units_sold);
        imageViewProd = findViewById(R.id.img_product);
        textViewUnits = findViewById(R.id.tv_prod_units);
        textViewDes=findViewById(R.id.tv_prod_descp);

        textViewName.setText(prod.toUpperCase());

        auth = FirebaseAuth.getInstance();


        findViewById(R.id.tv_arrow_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDescriptionActivity.this, DashboardActivity.class));
            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String rs = dataSnapshot.child("Prod").child(prod).child(name).child("rs").getValue().toString();
                String url = dataSnapshot.child("Prod").child(prod).child(name).child("url").getValue().toString();
                String units = dataSnapshot.child("Prod").child(prod).child(name).child("kg").getValue().toString();
                String desp = dataSnapshot.child("Prod").child(prod).child(name).child("description").getValue().toString();
                textViewDes.setText(desp);
                if (dataSnapshot.child("Product").child(prod).child(name).child("sold").exists()){
                    textViewSold.setText(dataSnapshot.child("Product").child(prod).child(name).child("kg").getValue().toString()+"Kg");
                }
                else {
                    textViewSold.setText("0 Kg");
                }

                Picasso.get().load(url).into(imageViewProd);
                textViewRs.setText("Rs. " + rs);
                textViewUnits.setText(units+" Kg");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
