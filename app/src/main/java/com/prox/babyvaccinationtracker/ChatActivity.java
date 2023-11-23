package com.prox.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.adapter.ChatAdapter;
import com.prox.babyvaccinationtracker.model.Conversation;
import com.prox.babyvaccinationtracker.model.Message;
import com.prox.babyvaccinationtracker.response.BotResponse;
import com.prox.babyvaccinationtracker.service.api.ApiService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    String conversation_id;
    RecyclerView recyclerViewChat;
    EditText editTextMessage;
    Button sendButton;

    private ChatAdapter chatAdapter;
    String user_name = "";

    Conversation conversation = new Conversation();
    private HashMap<String, Message> messages = new HashMap<>();
    ImageView imageViewBack;

    TextView textViewTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.sendButton);
        imageViewBack = findViewById(R.id.imageViewBack);
        textViewTitle = findViewById(R.id.textViewTitle);

        Intent intent = getIntent();
        conversation_id = intent.getStringExtra("conversation_id");
        String conversation_name = intent.getStringExtra("center_name");

        textViewTitle.setText(conversation_name);

        chatAdapter = new ChatAdapter(this);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        user_name = sharedPreferences.getString("cus_name", "");

        if(conversation_id != null){
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
                            message.setMessage_id(key);
                            chatAdapter.addMessage(message);
                            Log.i("Messages", "onDataChange: " + message.getMess_content());
                        }
                        chatAdapter.sortMessagesByKeyName();
                        chatAdapter.notifyDataSetChanged();
                        recyclerViewChat.scrollToPosition(chatAdapter.getItemCount() - 1);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
                    Map<String, Boolean> users = conversation.getUsers();
                    if (users != null && users.containsKey("bot")){
                        Log.i("chat", "onClick: " + "bot is in conversation");
                        String bot_message_id = databaseRef.push().getKey();
                        Message bot_message = new Message();
                        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                        Call<BotResponse> call = apiService.ask(message_content);
                        call.enqueue(new Callback<BotResponse>() {
                            @Override
                            public void onResponse(Call<BotResponse> call, Response<BotResponse> response) {
                                if (response.isSuccessful()) {
                                    Log.i("FirebaseManager", "onResponse: call");
                                    BotResponse botResponse = response.body();
                                    if (botResponse != null){
                                        String result = botResponse.getMessage();
                                        bot_message.setMess_content(result);
                                        bot_message.setUser_name("Bot");
                                        messages.put(bot_message_id, bot_message);
                                        databaseRef.child("messages").child(bot_message_id).setValue(bot_message);
                                        chatAdapter.addMessage(bot_message);
                                        recyclerViewChat.scrollToPosition(chatAdapter.getItemCount() - 1);
                                    }
                                } else {
                                    Log.i("FireBaseManager", "Lỗi call API");
                                    bot_message.setMess_content("Hệ thống đang quá tải, hãy thử lại sau nhé ❤️");
                                    bot_message.setUser_name("Bot");
                                    messages.put(bot_message_id, bot_message);
                                    databaseRef.child("messages").child(bot_message_id).setValue(bot_message);
                                    chatAdapter.addMessage(bot_message);
                                    recyclerViewChat.scrollToPosition(chatAdapter.getItemCount() - 1);
                                }
                            }

                            @Override
                            public void onFailure(Call<BotResponse> call, Throwable t) {
                                Log.i("FireBaseManager", "Call API thất bại");
                                bot_message.setMess_content("Hệ thống đang quá tải, hãy thử lại sau nhé ❤️");
                                bot_message.setUser_name("Bot");
                                messages.put(bot_message_id, bot_message);
                                databaseRef.child("messages").child(bot_message_id).setValue(bot_message);
                                chatAdapter.addMessage(bot_message);
                                recyclerViewChat.scrollToPosition(chatAdapter.getItemCount() - 1);
                            }
                        });
                    }
                    editTextMessage.setText("");
                }
            }
        });
    }
}