package com.example.e_commerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.R;
import com.example.e_commerce.interfaces.ItemClickListener;
import com.google.android.material.imageview.ShapeableImageView;

public class SearchFriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtFriendName;
    public ShapeableImageView FriendimageView;
    public Button addbtn;

    public ItemClickListener listener;

    public SearchFriendViewHolder(@NonNull View itemView) {
        super(itemView);

        FriendimageView =(ShapeableImageView) itemView.findViewById(R.id.add_friend_image_view);
        txtFriendName =(TextView)itemView.findViewById(R.id.add_friend_name);
        addbtn=(Button)itemView.findViewById(R.id.add_btn);
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
