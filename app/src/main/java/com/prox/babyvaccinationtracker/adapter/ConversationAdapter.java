package com.prox.babyvaccinationtracker.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.babyvaccinationtracker.ChatActivity;
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Conversation;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {
    private List<Conversation> conversations;

    public ConversationAdapter(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_item, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation conversation = conversations.get(position);
//        Log.i("conversation", "onBindViewHolder:  " + conversation.toString());
        // Hiển thị thông tin cuộc trò chuyện trên ViewHolder ở đây.
        // Ví dụ:
        Log.i("adapter", "onBindViewHolder: " + conversation.getLastMessage().getMess_content());
         holder.userNameTextView.setText(conversation.getLastMessage().getUser_name());
         holder.lastMessageTextView.setText(conversation.getLastMessage().getMess_content());

         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(v.getContext(), ChatActivity.class);
                 intent.putExtra("conversation_id", conversation.getConversation_id());
                 v.getContext().startActivity(intent);
             }
         });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    static class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        TextView lastMessageTextView;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
        }
    }
}

