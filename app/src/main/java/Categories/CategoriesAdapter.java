package Categories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.emandi2.AddProductActivity;
import com.example.android.emandi2.DashboardActivity;
import com.example.android.emandi2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ArtistViewHolder> {

    private Context context;
    private List<Categories> results;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private int flag;
    private String type;


    public CategoriesAdapter(Context mCtx, List<Categories> results, int flag,String type) {
        context = mCtx;
        this.results = results;
        this.flag=flag;
        this.type=type;
    }


    @NonNull
    @Override
    public CategoriesAdapter.ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_categories, parent, false);
        return new CategoriesAdapter.ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriesAdapter.ArtistViewHolder holder, final int position) {
        final Categories result = results.get(position);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final String key = result.key;
        holder.textViewName.setText(key);
        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==0){
                    Bundle bundle=new Bundle();
                    bundle.putString("edttext", key);
                    CategoriesFragment fragobj = new CategoriesFragment();
                    fragobj.setArguments(bundle);
                    FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.fragment_container,
                            fragobj).commit();
                }
                else {
                    Intent intent =new Intent(context, AddProductActivity.class);
                    intent.putExtra("prod", key);
                    intent.putExtra("type", type);
                    context.startActivity(intent);

                }
                Log.d("Text View",key);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    class ArtistViewHolder extends RecyclerView.ViewHolder {


        TextView textViewName;
        RelativeLayout parentlayout;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_name);
            parentlayout = itemView.findViewById(R.id.ll_parent_layout);
        }
    }
}

