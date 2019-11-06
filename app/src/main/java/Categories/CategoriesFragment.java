package Categories;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

public class CategoriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoriesAdapter adapter;
    private EditText ed_search;
    private List<Categories> results;
    private DatabaseReference myRef;
    private int flag;
    String type;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_categories, container, false);

        final String strtext = getArguments().getString("edttext");

        if(strtext.equals("Activity"))
            flag=0;
        else
            flag = 1;

        recyclerView = view.findViewById(R.id.recyclerView);
        ed_search=view.findViewById(R.id.edt_search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        myRef = FirebaseDatabase.getInstance().getReference();
        results = new ArrayList<Categories>();
        adapter = new CategoriesAdapter(getActivity(), results,flag,strtext);
        recyclerView.setAdapter(adapter);

        final String uid = FirebaseAuth.getInstance().getUid();

        if(flag==0){
            Query query = FirebaseDatabase.getInstance().getReference("product2");
            query.addListenerForSingleValueEvent(valueEventListener1);

        }
        else{
            Query query = FirebaseDatabase.getInstance().getReference("product2/"+strtext);
            query.addListenerForSingleValueEvent(valueEventListener1);
        }

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(flag==0){
                    Query query = FirebaseDatabase.getInstance().getReference("product2")
                            .orderByKey()
                            .startAt(ed_search.getText().toString())
                            .endAt(ed_search.getText().toString() + "\uf8ff");
                    query.addListenerForSingleValueEvent(valueEventListener1);

                }
                else{
                    Query query = FirebaseDatabase.getInstance().getReference("product2/"+strtext)
                            .orderByKey()
                            .startAt(ed_search.getText().toString())
                            .endAt(ed_search.getText().toString() + "\uf8ff");
                    query.addListenerForSingleValueEvent(valueEventListener1);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            results.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot != null) {
                        results.add(new Categories(snapshot.getValue().toString()));
                        Log.d("Categories",snapshot.getValue().toString());
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    private ValueEventListener valueEventListener1 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            results.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot != null) {
                        results.add(new Categories(snapshot.getKey()));
                        Log.d("Categories",snapshot.getKey());
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
