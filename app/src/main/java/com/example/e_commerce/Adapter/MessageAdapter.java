package com.example.e_commerce.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.Model.Message;
import com.example.e_commerce.Prevalent.Prevalent;
import com.example.e_commerce.R;
import com.example.e_commerce.ViewHolder.MessageViewHolder;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder>{

    Context context;
    ArrayList<Message> msgs;
    final int item_sent=1;
    final int item_received=2;
    String senderRoom,receiverRoom;

    public MessageAdapter(Context context, ArrayList<Message> msgs,String senderRoom,String receiverRoom)
    {
        this.context=context;
        this.msgs=msgs;
        this.senderRoom=senderRoom;
        this.receiverRoom=receiverRoom;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sendchat_card_view,parent,false);
        return new MessageViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

       Message m=msgs.get(position);
       Log.d("a",m.getMessage());
       holder.txtMessage.setText(m.getMessage());

        holder.txtTime.setText(m.getMessageId().substring(11,15));



        if(Prevalent.currentUser.getPhone().equals(m.getSenderId()))
       {
           RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
           params.leftMargin=500;
           //params.setMarginStart(100);
           holder.itemView.setLayoutParams(params);
           holder.cardView.setBackground(ContextCompat.getDrawable(context,R.drawable.layout_bg_green));

       }
       else
       {
           RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
           params.rightMargin=100;
           holder.itemView.setLayoutParams(params);
           holder.cardView.setBackground(ContextCompat.getDrawable(context,R.drawable.layout_bg_white));
       }

        int reactions[] = new int[]{
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };
        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();



        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {

            holder.reaction.setImageResource(reactions[pos]);
            holder.reaction.setVisibility(View.VISIBLE);

            m.setFeeling(pos);
            //Log.d("x", senderRoom);
            FirebaseDatabase.getInstance().getReference().child("Chats").child(senderRoom).child(receiverRoom).child("messages").child(m.getMessageId()).setValue(m);
            FirebaseDatabase.getInstance().getReference().child("Chats").child(receiverRoom).child(senderRoom).child("messages").child(m.getMessageId()).setValue(m);
            return true; // true is closing popup, false is requesting a new selection
        });

        if(m.getFeeling()>=0)
        {
            holder.reaction.setImageResource(reactions[m.getFeeling()]);
            holder.reaction.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popup.onTouch(v,event);
                return false;
            }
        });




    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }


}



