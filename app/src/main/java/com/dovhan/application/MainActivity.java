package com.dovhan.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private Button signOut;
    private TextView welcomeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        initViewElements();

        signOut.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent in = new Intent(MainActivity.this, SignInActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
            }
        });
    }

    private void initViewElements() {
        signOut = findViewById(R.id.sign_out_btn);
        welcomeField = findViewById(R.id.welcome);

        StringBuilder sb = new StringBuilder();
        String username = user.getDisplayName();
        sb.append(getResources().getString(R.string.welcome_text)).append(username).append("!");
        welcomeField.setText(sb.toString());
    }
}