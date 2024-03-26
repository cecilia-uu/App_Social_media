package edu.northeastern.memecho.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TableLayout;

import edu.northeastern.memecho.R;
import edu.northeastern.memecho.databinding.ActivityHomeBinding;
import edu.northeastern.memecho.fragments.AddFragment;
import edu.northeastern.memecho.fragments.HomeFragment;
import edu.northeastern.memecho.fragments.NotificationFragment;
import edu.northeastern.memecho.fragments.ProfileFragment;
import edu.northeastern.memecho.fragments.SearchFragment;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        setListeners();


    }

    private void setListeners() {
        replaceFragment(new HomeFragment());
        // Listeners for navigation buttons
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (id == R.id.search) {
                replaceFragment(new SearchFragment());
            } else if (id == R.id.add) {
                replaceFragment(new AddFragment());
            } else if (id == R.id.message) {
                replaceFragment(new NotificationFragment());
            } else if (id == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
}