package com.prox.babyvaccinationtracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.adapter.ChatAdapter;
import com.prox.babyvaccinationtracker.model.Conversation;
import com.prox.babyvaccinationtracker.model.Message;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity_user extends AppCompatActivity {

    String conversation_id;
    RecyclerView recyclerViewChat;
    EditText editTextMessage;
    Button sendButton;

    private ChatAdapter chatAdapter;
    String user_name = "";
    String user_id = "";

    String center_id = "";

    Conversation conversation = new Conversation();
    private HashMap<String, Message> messages = new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.sendButton);

        Intent intent = getIntent();
        conversation_id = intent.getStringExtra("conversation_id");
        Log.i("CHATACTIVITY_USERRRRRRR", conversation_id+"");

        chatAdapter = new ChatAdapter(this);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        user_name = sharedPreferences.getString("cus_name", "");
        user_id = sharedPreferences.getString("customer_id", "");

        if(conversation_id != null){
            Log.i("CONIDDDDDDDDD", conversation_id);
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("chat").child(conversation_id);
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        // dataSnapshot is the "issue" node with all children with id 0
                        conversation = dataSnapshot.getValue(Conversation.class);
                        assert conversation != null;
                        messages = conversation.getMessages();
                        Log.i("chat mess", "onDataChange: " + messages);
                        //convert hashmap to arraylist
                        for (String key : messages.keySet()) {
                            Message message = messages.get(key);
                            chatAdapter.addMessage(message);
                        }
                        chatAdapter.notifyDataSetChanged();
                        recyclerViewChat.scrollToPosition(chatAdapter.getItemCount() - 1);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message_content = editTextMessage.getText().toString();
                if(!message_content.isEmpty()){
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("chat").child(conversation_id);
                    String message_id = databaseRef.push().getKey();
                    Message message = new Message();
                    message.setMess_content(message_content);
                    message.setUser_name(user_name);
                    messages.put(message_id, message);
                    databaseRef.child("messages").child(message_id).setValue(message);
                    chatAdapter.addMessage(message);
                    recyclerViewChat.scrollToPosition(chatAdapter.getItemCount() - 1);
                    editTextMessage.setText("");
                }
            }
        });
    }
}
