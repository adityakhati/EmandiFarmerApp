package Products;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.emandi2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ArtistViewHolder> {

    private Context context;
    private List<WishList> results;
    //private EditText ed_search;
    private String rs,type;
    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;


    public WishListAdapter(Context mCtx, List<WishList> results) {
        context = mCtx;
        this.results = results;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_wish_list, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArtistViewHolder holder, final int position) {
        final WishList result = results.get(position);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final String prod = result.key;
        final String name = result.child;
        Log.d("key", name);

        holder.textViewName.setText(prod);
        //holder.textViewFarmerName.setText(name);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                type = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getUid()).child("Prod").child(prod).getValue().toString();
                Log.d("type", type);
                String rs = dataSnapshot.child("product2").child(type).child(prod).child(name).child("rs").getValue().toString();
                String url = dataSnapshot.child("product2").child(type).child(prod).child(name).child("url").getValue().toString();
                Picasso.get().load(url).into(holder.imageViewProd);
                holder.textViewRupees.setText("Rs. "+rs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = mFirebaseDatabase.getReference();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        type = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getUid()).child("Prod").child(prod).getValue().toString();
                        Intent intent = new Intent(context, ProductDescriptionActivity.class);
                        intent.putExtra("type", type);
                        intent.putExtra("farmer", name);
                        intent.putExtra("prod", prod);
                        context.startActivity(intent);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });


                //readData(prod,name);
            }
        });
    }


    private void readData( final String name, final String prod) {

        myRef = mFirebaseDatabase.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                type = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getUid()).child("Prod").child(prod).getValue().toString();
                Intent intent = new Intent(context, ProductDescriptionActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("farmer", name);
                intent.putExtra("prod", prod);
                context.startActivity(intent);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    class ArtistViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewRupees;
        //textViewFarmerName;
        LinearLayout parentlayout;
        ImageView imageViewProd;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            //textViewFarmerName=itemView.findViewById(R.id.tv_units_sold);
            textViewName = itemView.findViewById(R.id.tv_prod_name);
            parentlayout = itemView.findViewById(R.id.ll_parent_layout);
            textViewRupees = itemView.findViewById(R.id.tv_prod_rs);
            imageViewProd = itemView.findViewById(R.id.img_product);
        }
    }
}


