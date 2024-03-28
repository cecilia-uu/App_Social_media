package edu.northeastern.memecho.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import edu.northeastern.memecho.R;
import edu.northeastern.memecho.activities.SignInActivity;
import edu.northeastern.memecho.databinding.FragmentProfileBinding;
import edu.northeastern.memecho.utilities.Constants;
import edu.northeastern.memecho.utilities.PreferenceManager;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private PreferenceManager preferenceManager;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        preferenceManager = new PreferenceManager(getContext());
        loadUserDetails();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        binding.imageSignOut.setOnClickListener(v -> signOut());
    }


    /**
     * Try to sign out.
     */
    private void signOut() {
        showToast("Signing out...");
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);

        if (userId != null && !userId.isEmpty()) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            DocumentReference documentReference =
                    database.collection(Constants.KEY_COLLECTION_USERS).document(
                            preferenceManager.getString(Constants.KEY_USER_ID)
                    );
            HashMap<String, Object> updates = new HashMap<>();
            updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
            documentReference.update(updates)
                    .addOnSuccessListener(unused -> {
                        preferenceManager.clear();
                        startActivity(new Intent(getActivity(), SignInActivity.class));
                    })
                    .addOnFailureListener(e -> showToast("Unable to sign out"));
        } else {
            showToast("User ID is not available. Unable to sign out.");
        }

    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    /**
     * Load user's name and profile image.
     */
    private void loadUserDetails() {
        // set username
        binding.toolbarUsername.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));

        // set profile image
        String encodedImage = preferenceManager.getString(Constants.KEY_IMAGE);
        if (encodedImage != null && !encodedImage.isEmpty()) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            binding.profileImage.setImageBitmap(bitmap);
        } else {
            // Set a default image or handle the case where there is no image.
            binding.profileImage.setImageResource(R.drawable.ic_profile); // Replace with your default image resource.
        }


        // TODO
        // set following num, follower num, post num
        binding.textFollowerNumber.setText(preferenceManager.getString(Constants.KEY_FOLLOWER_NUM));
        binding.textFollowingNumber.setText(preferenceManager.getString(Constants.KEY_FOLLOWING_NUM));
        binding.textFollowerNumber.setText(preferenceManager.getString(Constants.KEY_POST_NUM));

        // TODO
        // set status
        binding.textStatus.setText(preferenceManager.getString(Constants.KEY_STATUS));

    }
}