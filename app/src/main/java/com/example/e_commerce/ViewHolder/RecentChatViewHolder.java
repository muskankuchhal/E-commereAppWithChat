package com.example.e_commerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.R;
import com.example.e_commerce.interfaces.ItemClickListener;
import com.google.android.material.imageview.ShapeableImageView;

public class RecentChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtRecentChatName,txtRecentChatTime,txtRecentChat;
    public ShapeableImageView RecentChatimageView;

    public ItemClickListener listener;

    public RecentChatViewHolder(@NonNull View itemView) {
        super(itemView);

        txtRecentChatName=(TextView)itemView.findViewById(R.id.recent_friend_name);
        txtRecentChatTime=(TextView)itemView.findViewById(R.id.recent_time);
        txtRecentChat=(TextView)itemView.findViewById(R.id.recent_chat);

        RecentChatimageView=(ShapeableImageView)itemView.findViewById(R.id.recent_friend_image_view);
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
