package edu.northeastern.memecho.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import edu.northeastern.memecho.R;
import edu.northeastern.memecho.databinding.ActivityForgotPasswordBinding;
import edu.northeastern.memecho.databinding.ActivitySignInBinding;
import edu.northeastern.memecho.utilities.PreferenceManager;

public class ForgotPasswordActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private FirebaseAuth auth;
    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        setListeners();
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

            binding.progressBar.setVisibility(View.VISIBLE); // show the progress

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

                            binding.progressBar.setVisibility(View.GONE); // progress ends
                        }
                    });
        });

        // create a new account
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));

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
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}