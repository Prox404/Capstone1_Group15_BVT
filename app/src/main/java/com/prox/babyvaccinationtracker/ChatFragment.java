package com.prox.babyvaccinationtracker;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.adapter.ConversationAdapter;
import com.prox.babyvaccinationtracker.model.Conversation;
import com.prox.babyvaccinationtracker.model.Message;
import com.prox.babyvaccinationtracker.util.NetworkUtils;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ChatFragment extends Fragment {

    Context context;
    RecyclerView recyclerConversation;

    LinearLayout chatWithBot;
    ConversationAdapter conversationAdapter;
    ArrayList<String> conversation_ids = new ArrayList<>();
    List<Conversation> conversations = new ArrayList<>();
    List<Conversation> conversations_all = new ArrayList<>();
    List<Conversation> conversations_filter = new ArrayList<>();
    EditText editTextSearch;

    ImageButton AddConversation;

    DatabaseReference databaseRef;
    DatabaseReference chatUserRef;
    View loadingLayout;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                            loadingLayout.setVisibility(View.GONE);
                        }
                    });
                }
                loadingLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("chat", "onCancelled: " + error.getMessage());
            }
        });

        Log.i("chat", "onCreateView: " + conversations.size());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container != null ? container.getContext() : null;
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerConversation = view.findViewById(R.id.recyclerConversation);
        chatWithBot = view.findViewById(R.id.chatWithBot);
        AddConversation = view.findViewById(R.id.AddConversation);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        loadingLayout = view.findViewById(R.id.loadingLayout);
        loadingLayout.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerConversation.setLayoutManager(linearLayoutManager);

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE);
        String user_id = sharedPreferences.getString("customer_id", "");
        String user_name = sharedPreferences.getString("cus_name", "");
        String user_address = sharedPreferences.getString("cus_address","");

        databaseRef = FirebaseDatabase.getInstance().getReference("chat");
        chatUserRef = FirebaseDatabase.getInstance().getReference("chat_users").child(user_id);

        conversationAdapter = new ConversationAdapter(conversations);
        recyclerConversation.setAdapter(conversationAdapter);

        addConversation();
//        String path  = "users/" + user_id;
//        Query query = chatUserRef.orderByChild(user_id).equalTo(true);
//        Log.i("chat", "onCreateView: " + query.toString());

        chatWithBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean network_state = NetworkUtils.isNetworkAvailable(context);
                HomeActivity.NETWORK_STATE = network_state;
                Log.i("chat with bot", "onClick:  " + conversations.size());
                if (!network_state){
                    HomeActivity.showNetworkAlertDialog(context);
                    return;
                }
                Boolean isExist = false;
                String conversation_id = "";
                for (Conversation conversation : conversations) {
                    Log.i("chat with bot", "onClick: " + conversation.getConversation_id());
                    Map<String, Boolean> users = conversation.getUsers();
                    if (users.containsKey("bot")) {
                        isExist = true;
                        conversation_id = conversation.getConversation_id();
                        break;
                    }
                }

                if (isExist){
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("conversation_id", conversation_id);
                    Log.i("Exits", "onClick: " + conversation_id);
                    startActivity(intent);
                }else{
                    Map<String, Boolean> users = new HashMap<>();
                    users.put("bot", true);
                    users.put(user_id, true);

                    HashMap<String, Message> messages = new HashMap<>();

                    // push conversation and get conversation_id
                    conversation_id = databaseRef.push().getKey();
                    Log.i("chat", "onCreateView: " + conversation_id);

                    // Check if conversation_id is not null
                    if (conversation_id != null) {
                        // Create a new conversation and set users
                        Conversation conversation = new Conversation();
                        conversation_id = databaseRef.push().getKey();
                        conversation.setUsers(users);

                        // Push the new conversation to the database
                        databaseRef.child(conversation_id).setValue(conversation);
                        chatUserRef.child(conversation_id).setValue(true);

                        // Push a message to the conversation
                        Message message = new Message("Bot", "Chào bạn, tôi có thể giúp gì cho bạn?");
                        String messageKey = databaseRef.child(conversation_id).child("messages").push().getKey();
                        if (messageKey != null) {
                            messages.put(messageKey, message);
                            conversation.setMessages(messages);
                            databaseRef.child(conversation_id).setValue(conversation);
                        }
                    } else {
                        // Handle the case where conversation_id is null
                        Log.e("chat", "Conversation ID is null.");
                    }

                    Log.i("Not Exits", "onClick: " + conversation_id);
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("conversation_id", conversation_id);
                    startActivity(intent);
                }
            }
        });

        AddConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,AddChatConversition.class);
                intent.putExtra("cus_address", user_address);
                startActivity(intent);
            }
        });

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

        //---------------------------------------------------


////
//        Map<String, Boolean> users = new HashMap<>();
//        users.put(user_id, true);
//
//        HashMap<String, Message> messages = new HashMap<>();
//
//        // push conversation and get conversation_id
//        String conversation_id = databaseRef.push().getKey();
//        Log.i("chat", "onCreateView: " + conversation_id);
//
//        // Check if conversation_id is not null
//        if (conversation_id != null) {
//            // Create a new conversation and set users
//            Conversation conversation = new Conversation();
//            conversation_id = databaseRef.push().getKey();
//            conversation.setUsers(users);
//
//            // Push the new conversation to the database
//            databaseRef.child(conversation_id).setValue(conversation);
//            chatUserRef.child(user_id).child(conversation_id).setValue(true);
//
//            // Push a message to the conversation
//            Message message = new Message(user_name, "alooo");
//            String messageKey = databaseRef.child(conversation_id).child("messages").push().getKey();
//            if (messageKey != null) {
//                messages.put(messageKey, message);
//                conversation.setMessages(messages);
//                databaseRef.child(conversation_id).setValue(conversation);
//            }
//        } else {
//            // Handle the case where conversation_id is null
//            Log.e("chat", "Conversation ID is null.");
//        }
        return view;
    }
    private void search(String searchText) {
        conversationAdapter.search(searchText,conversations_all);
    }
}