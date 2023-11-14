package com.vaccinecenter.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vaccinecenter.babyvaccinationtracker.Adapter.ConversationAdapter;
import com.vaccinecenter.babyvaccinationtracker.model.Conversation;

import java.util.ArrayList;
import java.util.List;

public class ConversationActivity extends AppCompatActivity {

    RecyclerView recyclerConversation;
    ConversationAdapter conversationAdapter;
    ArrayList<String> conversation_ids = new ArrayList<>();
    List<Conversation> conversations = new ArrayList<>();
    List<Conversation> conversations_all = new ArrayList<>();
    List<Conversation> conversations_filter = new ArrayList<>();
    EditText editTextSearch;
    ImageButton AddConversation;
    DatabaseReference databaseRef;
    DatabaseReference chatUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        recyclerConversation = findViewById(R.id.recyclerConversation);
        AddConversation = findViewById(R.id.AddConversation);
        editTextSearch = findViewById(R.id.editTextSearch);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerConversation.setLayoutManager(linearLayoutManager);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String user_id = sharedPreferences.getString("center_id", "");
        String user_name = sharedPreferences.getString("center_name", "");
        String user_address = sharedPreferences.getString("center_address", "");

        Log.i("chat", "onCreate: " + user_id + " " + user_name);

        databaseRef = FirebaseDatabase.getInstance().getReference("chat");
        chatUserRef = FirebaseDatabase.getInstance().getReference("chat_users").child(user_id);

        conversationAdapter = new ConversationAdapter(conversations);
        recyclerConversation.setAdapter(conversationAdapter);

        addConversation();

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String name = editable.toString();
                search(name);
            }
        });

        AddConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConversationActivity.this,AddChatConversation.class);
                intent.putExtra("center_address",user_address);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        addConversation();
        conversationAdapter = new ConversationAdapter(conversations);
        recyclerConversation.setAdapter(conversationAdapter);

    }

    private void addConversation(){
        conversations.clear();
        conversation_ids.clear();
        chatUserRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.i("chat", "onDataChange: " + snapshot.toString());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.i("chat", "onDataChange: " + dataSnapshot.toString());
                    String conversation_id = dataSnapshot.getKey();
                    databaseRef.child(conversation_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.i("chat", "onDataChange: " + snapshot.toString());
                            Conversation conversation = snapshot.getValue(Conversation.class);
                            conversation.setConversation_id(conversation_id);
                            if(!conversation_ids.contains(conversation_id)){
                                conversation_ids.add(conversation_id);
                                conversations.add(conversation);
                            }
                            conversations_all = new ArrayList<>(conversations);
                            Log.i("chat_conversation_all", "onCreateView: " + conversations_all.size()+" "+conversations.size());
                            conversationAdapter.notifyDataSetChanged();
//                            Log.i("chat", "onDataChange: " + conversations.size());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("chat", "onCancelled: " + error.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("chat", "onCancelled: " + error.getMessage());
            }
        });

        Log.i("chat", "onCreateView: " + conversations.size());
    }

    private void search(String searchText) {
        conversationAdapter.search(searchText,conversations_all);
    }
}