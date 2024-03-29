package edu.northeastern.memecho.fragments;

import static edu.northeastern.memecho.utilities.ImageContent.loadSavedImages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.northeastern.memecho.R;
import edu.northeastern.memecho.adapters.GalleryAdapter;
import edu.northeastern.memecho.databinding.FragmentAddBinding;
import edu.northeastern.memecho.databinding.FragmentProfileBinding;
import edu.northeastern.memecho.models.GalleryImages;
import edu.northeastern.memecho.utilities.PreferenceManager;


public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private PreferenceManager preferenceManager;
    private GalleryAdapter adapter;
    private List<GalleryImages> list;
    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false);
        preferenceManager = new PreferenceManager(getContext());

        // recycler view setting
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();
        adapter = new GalleryAdapter(list);

        binding.recyclerView.setAdapter(adapter);


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadSavedImages(getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
                adapter.notifyDataSetChanged();
            }
        });
    }
}