package edu.northeastern.memecho.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.memecho.R;
import edu.northeastern.memecho.adapters.HomeAdapter;
import edu.northeastern.memecho.databinding.ActivityHomeBinding;
import edu.northeastern.memecho.databinding.ActivityMainBinding;
import edu.northeastern.memecho.models.HomeModel;
import edu.northeastern.memecho.utilities.Constants;
import edu.northeastern.memecho.utilities.PreferenceManager;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;

    private HomeAdapter adapter;

    private FirebaseUser user;

    private List<HomeModel> list;

    private DocumentReference reference;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        //reference = FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_USERS)
                //.document(Constants.KEY_USER_ID);

        list = new ArrayList<>();
        adapter = new HomeAdapter(list);
        recyclerView.setAdapter(adapter);

        loadDataFromFirestore();

    }

    private void loadDataFromFirestore() {
        // sample data
//        list.add(new HomeModel("Cecilia", "01/11/2024", "","", "123", 12));
//        list.add(new HomeModel("Patrick", "01/12/2024", "","", "124", 11));
//        list.add(new HomeModel("Sofia", "01/14/2024", "","", "125", 10));
//        list.add(new HomeModel("Daniel", "01/16/2024", "","", "126", 19));
        CollectionReference reference = FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_USERS)
                        .document(user.getUid())
                                .collection(Constants.KEY_COLLECTION_POST_IMAGES);
        reference.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Error: ", error.getMessage());
                    return;
                }
            }
        });
        adapter.notifyDataSetChanged();
    }


    private void init(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        recyclerView = view.findViewById(R.id.homeRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
}