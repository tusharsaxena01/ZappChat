package com.bit.zappchat.WelcomeScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bit.zappchat.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    String name, email, password;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference dbReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();
        setContentView(binding.getRoot());
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.pbLoading.setVisibility(View.VISIBLE);
                name = binding.etName.getText().toString().trim();
                email = binding.etEmail.getText().toString();
                password = binding.etPassword.getText().toString();
                Log.e("check", ""+validate(name, email, password));
                if(validate(name, email, password)){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> authResultTask) {
                                    Log.e("check",authResultTask.getResult().toString());
                                    if(authResultTask.isSuccessful()) {
                                        User newUser = new User(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), name, email, password);
                                        dbReference.child("users")
                                                .child(newUser.uid)
                                                .setValue(newUser)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                binding.pbLoading.setVisibility(View.GONE);
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(RegisterActivity.this, "Task Completed", Toast.LENGTH_SHORT).show();
                                                                }else{
                                                                    Toast.makeText(RegisterActivity.this, "Task not Completed", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish();
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validate(String name, String email, String password) {
        if(name.length() == 0 || email.length() == 0 || password.length() == 0){
            return false;
        }
        return password.length() >= 8;
    }
}