package Products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.emandi2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishListFragment extends Fragment {

    private RecyclerView recyclerView;
    private WishListAdapter adapter;
    private List<WishList> results;
    String uid;

    DatabaseReference dbArtists;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_wishlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        dbArtists = FirebaseDatabase.getInstance().getReference();
        results = new ArrayList<WishList>();
        adapter = new WishListAdapter(getActivity(), results);
        recyclerView.setAdapter(adapter);

        uid=FirebaseAuth.getInstance().getUid();

        Query query = FirebaseDatabase.getInstance().getReference("Users/"+uid+"/Prod");
        query.addListenerForSingleValueEvent(valueEventListener);
        
        return view;
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            results.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot != null) {
                        final String keyvalue=snapshot.getKey();
                        results.add((new WishList(keyvalue,uid)));
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };

}

