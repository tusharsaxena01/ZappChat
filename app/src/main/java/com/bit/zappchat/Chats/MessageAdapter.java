package com.bit.zappchat.Chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bit.zappchat.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.myViewHolder>{

    Context context;
    List<Message> messages;

    public MessageAdapter(Context context) {
        this.context = context;
        messages = new ArrayList<>();
    }

    public void add(Message message){
        messages.add(message);
        notifyDataSetChanged();
    }

    public void clear(){
        messages.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.msg.setText(message.getMessage());
        if(message.getSenderId().equals(FirebaseAuth.getInstance().getUid())){
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.teal_700));
            holder.msg.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.black));
            holder.msg.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView msg;
        LinearLayout main;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.message);
            main = itemView.findViewById(R.id.mainMessageLayout);
        }
    }
}
