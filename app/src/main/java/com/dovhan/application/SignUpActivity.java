package com.dovhan.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    private static final String emailRegex = ".+@.+\\.com";
    private static final String passwordRegex = ".{8,}";
    private static final String phoneRegex = "\\+\\d{12}";

    private FirebaseAuth mAuth;

    private Button regBtn;
    private TextView changeAuth;

    private EditText emailField;
    private EditText passwordField;
    private EditText nameField;
    private EditText phoneField;

    private TextView errorEmail;
    private TextView errorPass;
    private TextView errorPhone;
    private TextView errorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigh_up);

        mAuth = FirebaseAuth.getInstance();

        initViewElements();

        regBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkRegField()) {
                    createAccount();
                }
            }
        });

        changeAuth.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInActivity();
            }
        });
    }

    private void initViewElements() {
        regBtn = findViewById(R.id.btn_reg);
        changeAuth = findViewById(R.id.change_auth_link);
        changeAuth.setText(getResources().getString(R.string.sign_in));
        regBtn.setText(getResources().getString(R.string.sign_up_btn));

        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        nameField = findViewById(R.id.name_field);
        phoneField = findViewById(R.id.phone_field);

        errorEmail = findViewById(R.id.error_email);
        errorPass = findViewById(R.id.error_pass);
        errorPhone = findViewById(R.id.error_phone);
        errorName = findViewById(R.id.error_name);
    }

    private String getEmail() {
        return emailField.getText().toString();
    }

    private String getPassword() {
        return passwordField.getText().toString();
    }

    private String getName() {
        return nameField.getText().toString();
    }

    private String getPhone() {
        return phoneField.getText().toString();
    }

    private boolean checkRegField() {
        String name = getName();
        String phone = getPhone();
        String email = getEmail();
        String password = getPassword();

        if (email.matches(emailRegex)) {
            errorEmail.setVisibility(View.GONE);
            emailField.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.form_black));
        }
        else {
            errorEmail.setText(R.string.error_email);
            errorEmail.setVisibility(View.VISIBLE);
            emailField.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.form_red));
            return false;
        }
        if (password.matches(passwordRegex)) {
            errorPass.setVisibility(View.GONE);
            passwordField.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.form_black));
        }
        else {
            errorPass.setText(R.string.error_pass);
            errorPass.setVisibility(View.VISIBLE);
            passwordField.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.form_red));
            return false;
        }
        if (name.matches("")) {
            errorName.setText(R.string.error_name);
            errorName.setVisibility(View.VISIBLE);
            nameField.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.form_red));
            return false;
        }
        else {
            errorName.setVisibility(View.GONE);
            nameField.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.form_black));
        }
        if (phone.matches(phoneRegex)){
            errorPhone.setVisibility(View.GONE);
            phoneField.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.form_black));
            return true;
        }
        else {
            errorPhone.setText(R.string.error_phone);
            errorPass.setVisibility(View.VISIBLE);
            phoneField.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.form_red));
            return false;
        }
    }

    private void createAccount() {
        mAuth.createUserWithEmailAndPassword(getEmail(), getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", getResources().getString(R.string.fire_create_user_success));
                            writeNewUser(getName(), getPhone(), mAuth.getCurrentUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", getResources().getString(R.string.fire_create_user_failure), task.getException());
                            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.toast_auth_failure),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void writeNewUser(String name, String phone, FirebaseUser user) {

        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();

        if (user != null) {
            user.updateProfile(userProfileChangeRequest).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("TAG", getResources().getString(R.string.fire_add_profile_name));
                        startMainActivity();
                    }
                    if (task.isCanceled()) {
                        Log.d("TAG", task.getException().getMessage());
                    }
                }
            });
        }

    }

    private void startMainActivity() {
        Intent in = new Intent(SignUpActivity.this, MainActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        finish();
    }

    private void startSignInActivity() {
        Intent in = new Intent(SignUpActivity.this, SignInActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        finish();
    }
}
