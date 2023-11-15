package com.prox.babyvaccinationtracker.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Message> messagesList;
    String user_name = "";
    private static final int USER_MESSAGE = 1;
    private static final int OTHER_MESSAGE = 2;

    public ChatAdapter(Context context) {
        this.messagesList = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE);
        user_name = sharedPreferences.getString("cus_name", "");
    }

    public void addMessage(Message message) {
        if (message != null) {
            messagesList.add(message);
            messagesList.sort((o1, o2) -> {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                try {
                    return simpleDateFormat.parse(o1.getMess_created_at()).compareTo(simpleDateFormat.parse(o2.getMess_created_at()));
                } catch (Exception e) {
                    Log.e("ChatAdapter", e.getMessage());
                    return 0;
                }
            });
            notifyDataSetChanged(); // Cập nhật giao diện khi có tin nhắn mới
        }
    }

    public void sortMessagesByKeyName(){
        Collections.sort(messagesList, Comparator.comparing(Message::getMessage_id));
    }

    public void clearMessages() {
        messagesList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messagesList.get(position);
        if (message != null) {
            if (message.getUser_name() != null && message.getUser_name().equals(user_name)) {
                return USER_MESSAGE;
            } else {
                return OTHER_MESSAGE;
            }
        }
        return OTHER_MESSAGE; // Giả sử là OTHER_MESSAGE nếu không thể xác định view type.
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == USER_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_message, parent, false);
            return new ChatViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other_message, parent, false);
            return new OtherChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messagesList.get(position);
        if (message != null) {
            if (holder instanceof ChatViewHolder) {
                ((ChatViewHolder) holder).bind(message);
            } else if (holder instanceof OtherChatViewHolder) {
                ((OtherChatViewHolder) holder).bind(message);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public void setMessages(ArrayList<Message> messagesList) {
        this.messagesList = messagesList;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView senderTextView;
        private TextView messageTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }

        public void bind(Message message) {
            if (message != null) {
                senderTextView.setText(message.getUser_name());
                messageTextView.setText(message.getMess_content());
            }
        }
    }

    public class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView senderTextView;
        private TextView messageTextView;

        public OtherChatViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }

        public void bind(Message message) {
            if (message != null) {
                senderTextView.setText(message.getUser_name());
                messageTextView.setText(message.getMess_content());
            }
        }
    }
}
