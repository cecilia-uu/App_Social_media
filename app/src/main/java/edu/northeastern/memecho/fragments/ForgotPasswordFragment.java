package edu.northeastern.memecho.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import edu.northeastern.memecho.activities.SignUpActivity;
import edu.northeastern.memecho.databinding.FragmentForgotPasswordBinding;
import edu.northeastern.memecho.utilities.PreferenceManager;


public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseAuth auth;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        preferenceManager = new PreferenceManager(getContext());
        auth = FirebaseAuth.getInstance();

        setListeners();
        return binding.getRoot();
    }

    /**
     * Listeners for recover button and create new account.
     */
    private void setListeners() {
        // recover button
        binding.buttonRecover.setOnClickListener(v -> {
            // not valid email, return!
            if (isValidEmail() == Boolean.FALSE) {
                return;
            }
            // continue to work
            String email = binding.inputEmail.getText().toString();

            auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // Successfully send email to reset password
                                    if (task.isSuccessful()) {
                                        showToast("Password reset email has sent successfully!");
                                        binding.inputEmail.setText("");
                                    } else {
                                        showToast("Error: " + task.getException().getMessage());
                                    }
                                }
                            });
        });

        // create a new account
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), SignUpActivity.class)));

    }

    /**
     * Helper method for checking whether it's valid email
     */
    private Boolean isValidEmail() {
        if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Please enter Email!");
            return Boolean.FALSE;
        } else if (!Patterns.EMAIL_ADDRESS.
                matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Please enter valid email!");
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    /**
     * Helper method
     * @param message the message you want to send to users.
     */
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}