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

public class SignInActivity extends AppCompatActivity {

    private static final String emailRegex = ".+@.+\\.com";
    private static final String passwordRegex = ".{8,}";

    private FirebaseAuth mAuth;

    private Button loginBtn;
    private TextView changeAuth;

    private EditText emailField;
    private EditText passwordField;

    private TextView errorEmail;
    private TextView errorPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        initViewElements();

        loginBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkLoginFields()) {
                    loginUser();
                }
            }
        });

        changeAuth.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignUpActivity();
            }
        });
    }

    private void initViewElements() {
        loginBtn = findViewById(R.id.btn_login);
        changeAuth = findViewById(R.id.change_auth_link);
        changeAuth.setText(getResources().getString(R.string.sign_up));
        loginBtn.setText(getResources().getString(R.string.sign_in_btn));

        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);

        errorEmail = findViewById(R.id.error_email);
        errorPass = findViewById(R.id.error_pass);
    }

    private String getEmail() {
        return emailField.getText().toString();
    }

    private String getPassword() {
        return passwordField.getText().toString();
    }

    private boolean checkLoginFields() {
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
            return true;
        }
        else {
            errorPass.setText(R.string.error_pass);
            errorPass.setVisibility(View.VISIBLE);
            passwordField.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.form_red));
            return false;
        }
    }

    private void loginUser() {
        mAuth.signInWithEmailAndPassword(getEmail(), getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", getResources().getString(R.string.fire_login_success));
                            FirebaseUser user = mAuth.getCurrentUser();
                            startMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", getResources().getString(R.string.fire_login_failure), task.getException());
                            Toast.makeText(SignInActivity.this, getResources().getString(R.string.toast_auth_failure),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void startMainActivity() {
        Intent in = new Intent(SignInActivity.this, MainActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        finish();
    }

    private void startSignUpActivity() {
        Intent in = new Intent(SignInActivity.this, SignUpActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        finish();
    }
}
