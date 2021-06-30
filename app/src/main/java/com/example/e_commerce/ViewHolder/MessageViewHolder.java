package com.example.e_commerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.Model.Message;
import com.example.e_commerce.R;
import com.example.e_commerce.interfaces.ItemClickListener;

public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtMessage,txtTime;
    public ImageView reaction;
    public CardView cardView;


    public ItemClickListener listener;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

        txtMessage=(TextView) itemView.findViewById(R.id.sendchat_message);
        txtTime=(TextView) itemView.findViewById(R.id.sendchat_time);
        cardView=(CardView)itemView.findViewById(R.id.card);
        reaction=(ImageView)itemView.findViewById(R.id.react);

    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener=listener;
    }

    @Override
    public void onClick(View v)
    {
        listener.OnClick(itemView,getAdapterPosition(),false);

    }
}
