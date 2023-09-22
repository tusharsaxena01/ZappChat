package com.bit.zappchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.bit.zappchat.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    DatabaseReference databaseReferenceSender;
    DatabaseReference databaseReferenceReceiver;
    String senderRoom, receiverRoom;
    String receiverId;
    MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        receiverId = getIntent().getStringExtra("id");

        senderRoom = FirebaseAuth.getInstance().getUid()+receiverId;
        receiverRoom = receiverId+FirebaseAuth.getInstance().getUid();
        databaseReferenceSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        databaseReferenceReceiver = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);
        messageAdapter = new MessageAdapter(this);
        binding.recycler.setAdapter(messageAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        databaseReferenceSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageAdapter.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    messageAdapter.add(message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.messageEd.getText().toString();
                if(message.trim().length() > 0){
                    sendMessage(message);
                    binding.messageEd.setText("");
                }
            }
        });

    }

    private void sendMessage(String message) {
        String messageId = UUID.randomUUID().toString();
        Message messageModel = new Message(message, FirebaseAuth.getInstance().getUid(), message);
        messageAdapter.add(messageModel);
        databaseReferenceSender
                .child(messageId)
                .setValue(messageModel);
        databaseReferenceReceiver
                .child(messageId)
                .setValue(messageModel);

    }
}