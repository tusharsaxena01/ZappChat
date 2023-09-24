package com.bit.zappchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.bit.zappchat.databinding.ActivityNavigationBinding;
import com.bit.zappchat.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavigationActivity extends AppCompatActivity {
    ActivityNavigationBinding binding;
    DatabaseReference databaseReference;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userAdapter = new UserAdapter(this);
        binding.recycler.setAdapter(userAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAdapter.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String uid = dataSnapshot.getKey();
                    if(!uid.equals(FirebaseAuth.getInstance().getUid())){
                        User user = dataSnapshot.getValue(User.class);
                        userAdapter.add(user);
                    }
                }
                setProgress(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setProgress(false);
            }
        });

    }

    private void setProgress(boolean isProgress) {
        if(isProgress){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            binding.pbLoading.setVisibility(View.VISIBLE);
        }else{
            binding.pbLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(NavigationActivity.this, WelcomeActivity.class));
            finish();
            return true;
        }
        return false;
    }
}