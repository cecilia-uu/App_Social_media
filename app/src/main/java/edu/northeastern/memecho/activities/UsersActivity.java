package edu.northeastern.memecho.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.memecho.adapters.UserAdapter;
import edu.northeastern.memecho.databinding.ActivityUsersBinding;
import edu.northeastern.memecho.models.User;
import edu.northeastern.memecho.utilities.Constants;
import edu.northeastern.memecho.utilities.PreferenceManager;

public class UsersActivity extends AppCompatActivity {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();
    }

    /**
     * Back arrow button
     */
    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> myOnBackPressed());
    }

    private void myOnBackPressed() {
        getOnBackPressedDispatcher().onBackPressed();
    }

    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserId.equals(queryDocumentSnapshot.getId()))
                            {
                                continue;
                            }
                            User user = new User(queryDocumentSnapshot.getString(Constants.KEY_NAME),
                                    queryDocumentSnapshot.getString(Constants.KEY_IMAGE),
                                    queryDocumentSnapshot.getString(Constants.KEY_EMAIL),
                                    queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN));
                            users.add(user);
                        }
                        if (users.size() > 0) {
                            UserAdapter userAdapter = new UserAdapter(users);
                            binding.userRecyclerView.setAdapter(userAdapter);
                            binding.userRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            showErrorMessage();
                        }
                    } else {
                        showErrorMessage();
                    }
                });
    }

    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s", "No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

}