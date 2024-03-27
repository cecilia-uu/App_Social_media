package edu.northeastern.memecho.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.northeastern.memecho.R;
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
        return binding.getRoot();
    }

    /**
     * Load user's name and profile image.
     */
    private void loadUserDetails() {
        // set username
        binding.toolbarUsername.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        // set profile image
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.profileImage.setImageBitmap(bitmap);
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