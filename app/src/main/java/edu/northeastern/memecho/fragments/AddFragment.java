package edu.northeastern.memecho.fragments;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.memecho.adapters.GalleryAdapter;
import edu.northeastern.memecho.databinding.FragmentAddBinding;
import edu.northeastern.memecho.models.GalleryImages;
import edu.northeastern.memecho.utilities.Constants;
import edu.northeastern.memecho.utilities.PreferenceManager;



public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private PreferenceManager preferenceManager;
    private GalleryAdapter adapter;
    private List<GalleryImages> list;
    private Uri imageUri;
    private FirebaseUser user;
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
        user = FirebaseAuth.getInstance().getCurrentUser();

        binding.recyclerView.setAdapter(adapter);

        clickListeners();

        return binding.getRoot();
    }

    private void clickListeners() {
        adapter.SendImage(new GalleryAdapter.SendImage() {
            @Override
            public void onSend(Uri picUri) {
                imageUri = picUri;



                startCrop();
            }
        });

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference storageReference =  storage.getReference()
                        .child(Constants.KEY_STORAGE_POST_IMAGES + System.currentTimeMillis());
                storageReference.putFile(imageUri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            uploadData(uri.toString());
                                        }
                                    });
                                }
                            }
                        });
            }
        });
    }
    // crop image
    private final ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(
            new CropImageContract(),
            new ActivityResultCallback<CropImageView.CropResult>() {
                @Override
                public void onActivityResult(CropImageView.CropResult o) {
                    if (o.isSuccessful()) {
                        Uri uriContent = o.getUriContent();

                        Glide.with(getContext())
                                .load(uriContent)
                                .into(binding.imageView);

                        // make image view become visible
                        binding.imageView.setVisibility(View.VISIBLE);
                        binding.nextBtn.setVisibility(View.VISIBLE);
                    } else {
                        Throwable error = o.getError();
                    }
                }
            }
    );

    private void startCrop() {
        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.guidelines = CropImageView.Guidelines.ON;
        CropImageContractOptions options = new CropImageContractOptions(Uri.EMPTY, new CropImageOptions());
        cropImage.launch(options);
    }

    private void uploadData(String imageUrl) {
        CollectionReference reference = FirebaseFirestore.getInstance()
                .collection(Constants.KEY_COLLECTION_USERS)
                .document(user.getUid()).collection(Constants.KEY_COLLECTION_POST_IMAGES);

        String description = binding.descriptionET.getText().toString();
        String id = reference.document().getId();

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.KEY_ID, id);
        map.put(Constants.KEY_DESCRIPTION, description);
        map.put(Constants.KEY_IMAGE_URL, imageUrl);
        map.put(Constants.KEY_TIMESTAMP, FieldValue.serverTimestamp());

        map.put(Constants.KEY_USERNAME, user.getDisplayName());
        map.put(Constants.KEY_LIKE_COUNT, 0);
        map.put(Constants.KEY_PROFILE_IMAGE, String.valueOf(user.getPhotoUrl()));

        reference.document(id).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println();
                        } else {
                            Toast.makeText(getContext(), "Error: " + task.getException()
                                            .getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dexter.withContext(getContext())
                        .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    File file = new File(Environment.getExternalStorageDirectory().toString()
                                    + "/Download");

                                    if (file.exists()) {
                                        File[] files = file.listFiles();
                                        assert files != null;
                                        for (File file1 : files) {
                                            if (file1.getAbsolutePath().endsWith(".jpg") ||
                                            file1.getAbsolutePath().endsWith(".png")) {
                                                list.add(new GalleryImages(Uri.fromFile(file1)));
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                            }
                        }).check();

            }
        });
    }

}