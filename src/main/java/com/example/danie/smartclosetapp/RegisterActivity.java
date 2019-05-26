package com.example.danie.smartclosetapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText loginEmailText;
    private EditText loginPasswordText;
    private EditText loginPasswordTextConfirm;
    private Button loginButton;
    private Button newAccountButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        loginEmailText = findViewById(R.id.loginEmailNew);
        loginPasswordText =  findViewById(R.id.loginPasswordNew);
        loginPasswordTextConfirm =  findViewById(R.id.confirmPassword);
        newAccountButton = findViewById(R.id.haveAccountBtn);
        loginButton = findViewById(R.id.loginAccountNewBtn);
        progressBar =  findViewById(R.id.progressBarRegister);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmail = loginEmailText.getText().toString();
                String loginPassword = loginPasswordText.getText().toString();
                String loginPasswordConfirm = loginPasswordTextConfirm.getText().toString();

                if (!(TextUtils.isEmpty(loginEmail) && TextUtils.isEmpty(loginPassword)
                        && TextUtils.isEmpty(loginPasswordConfirm))) {
                    if (loginPassword.equals(loginPasswordConfirm)){
                        progressBar.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(loginEmail, loginPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        moveToMain();

                                    } else {
                                        String e = task.getException().getMessage();
                                        Toast.makeText(RegisterActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                                    }

                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });

                }

                else {
                        Toast.makeText(RegisterActivity.this,
                                "confirm password not equals to password", Toast.LENGTH_SHORT).show();
                    }
            }

            }
        });

        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            moveToMain();


        }

    }

    private void moveToMain(){
        Intent mainIntent = new Intent(RegisterActivity.this , MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

}
