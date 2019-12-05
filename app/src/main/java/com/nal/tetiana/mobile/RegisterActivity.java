package com.nal.tetiana.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;


@SuppressWarnings("ALL")
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private EditText editTextRegisterEmail;
    private EditText editTextRegisterPassword;
    private EditText editTextRegisterUserName;
    private EditText editRegisterPhone;
    private static final Integer maxPasswordLength = 8;
    private final static Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^380*.{10}");
    private boolean successValidation = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextRegisterEmail = findViewById(R.id.register_email);
        editTextRegisterPassword = findViewById(R.id.register_password);
        editTextRegisterUserName = findViewById(R.id.register_name);
        editRegisterPhone = findViewById(R.id.register_phone);

        auth = FirebaseAuth.getInstance();

        findViewById(R.id.button_register).setOnClickListener(this);
        findViewById(R.id.text_view_login).setOnClickListener(this);
    }

    private void register() {

        final String userName = editTextRegisterUserName.getText().toString();
        final String phoneNumber = editRegisterPhone.getText().toString();
        final String email = editTextRegisterEmail.getText().toString();
        final String password = editTextRegisterPassword.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (allFieldValidation(userName, phoneNumber, email, password)) {
            if (auth.getCurrentUser() != null) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    addUsername(userName);
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            getString(R.string.failure),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }

        }
    }

    private boolean allFieldValidation(final String userName, final String phoneNumber,
                                       final String email, final String password) {
        if (!(userNameFieldvalidation(userName))
                & !(phoneNumberValidationField(phoneNumber))
                & !(emailValidationField(email))
                & !(passwordFieldValidation(password))) {
            return false;
        }
        if (!(userNameFieldvalidation(userName))) {
            return false;
        }
        if (!(phoneNumberValidationField(phoneNumber))) {
            return false;
        }
        if (!(emailValidationField(email))) {
            return false;
        }
        if (!(passwordFieldValidation(password))) {
            return false;
        }

        return successValidation;
    }

    private void addUsername(String userName) {
        FirebaseUser userProfile = auth.getCurrentUser();
        UserProfileChangeRequest userUpdateProfile = new UserProfileChangeRequest
                .Builder().setDisplayName(userName).build();

        userProfile.updateProfile(userUpdateProfile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task1) {
                        if (task1.isSuccessful()) {
                            startNextActivity(WelcomeActivity.class);
                        }
                    }

                });
    }


    private void clearFields() {
        editTextRegisterUserName.getText().clear();
        editRegisterPhone.getText().clear();
        editTextRegisterEmail.getText().clear();
        editTextRegisterPassword.getText().clear();
    }

    private void startNextActivity(Class cls) {
        clearFields();

        Intent intent = new Intent(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private boolean userNameFieldvalidation(String userName) {
        if (userName.isEmpty()) {
            editTextRegisterUserName.setError(getString(R.string.show_messg_required_name));
            editTextRegisterUserName.requestFocus();
            return false;
        }
        return successValidation;
    }

    private boolean phoneNumberValidationField(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            editRegisterPhone.setError(getString(R.string.show_messg_required_phone));
            editRegisterPhone.requestFocus();
            return false;
        } else if (!PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
            editRegisterPhone.setError(getString(R.string.show_messg_wrong_phone));
            editRegisterPhone.requestFocus();
            return false;
        }
        return successValidation;
    }

    private boolean emailValidationField(String email) {
        if (email.isEmpty()) {
            editTextRegisterEmail.setError(getString(R.string.show_messg_required_email));
            editTextRegisterEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextRegisterEmail.setError(getString(R.string.invalid_email));
            editTextRegisterEmail.requestFocus();
            return false;
        }


        return successValidation;
    }

    private boolean passwordFieldValidation(String password) {
        if (password.isEmpty()) {
            editTextRegisterPassword.setError(getString(R.string.required_password));
            editTextRegisterPassword.requestFocus();
            return false;
        } else if (password.length() < maxPasswordLength) {
            editTextRegisterPassword.setError(getString(R.string.weak_password));
            editTextRegisterPassword.requestFocus();
            return false;
        }
        return successValidation;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                register();
                break;
            case R.id.text_view_login:
                startNextActivity(LoginActivity.class);
                break;
        }
    }
}

