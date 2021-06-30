package com.example.e_commerce.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.ChatMessageActivity;
import com.example.e_commerce.Model.Message;
import com.example.e_commerce.Model.Users;
import com.example.e_commerce.R;
import com.example.e_commerce.ViewHolder.RecentChatViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatViewHolder> {

    Context context;
    ArrayList<Pair<Message,String>> recentChatList;

    public RecentChatAdapter(Context context, ArrayList<Pair<Message, String>> recentChatList) {
        this.context = context;
        this.recentChatList = recentChatList;
    }

    @NonNull
    @Override
    public RecentChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_recent_chat,parent,false);
        return new RecentChatViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecentChatViewHolder holder, int position) {

        String phone=recentChatList.get(position).second;
        Message m=recentChatList.get(position).first;

        FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("phone").equalTo(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot data:snapshot.getChildren())
                {
                    if(data.getKey().equals(phone))
                    {
                        holder.txtRecentChatName.setText(data.child("name").getValue().toString());
                        //Log.d("x",data.child("image").getValue().toString() );
                        Picasso.get().load(data.child("image").getValue().toString()).into(holder.RecentChatimageView);


                        holder.txtRecentChat.setText(m.getMessage());

                        holder.txtRecentChatTime.setText(m.getMessageId().substring(11,15));

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i=new Intent(context, ChatMessageActivity.class);
                                i.putExtra("phone",data.child("phone").getValue().toString());
                                i.putExtra("name",data.child("name").getValue().toString());
                                context.startActivity(i);
                            }
                        });
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    @Override
    public int getItemCount() {
        return recentChatList.size();
    }
}
