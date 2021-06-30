package com.example.e_commerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.R;
import com.example.e_commerce.interfaces.ItemClickListener;
import com.google.android.material.imageview.ShapeableImageView;

public class SearchRequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtRequestName;
    public ShapeableImageView RequestimageView;
    public Button acceptbtn,rejectbtn;

    public ItemClickListener listener;

    public SearchRequestViewHolder(@NonNull View itemView) {
        super(itemView);

       RequestimageView =(ShapeableImageView) itemView.findViewById(R.id.add_remove_friend_image_View);
        txtRequestName =(TextView)itemView.findViewById(R.id.add_remove_friend_name);
        acceptbtn=(Button)itemView.findViewById(R.id.accept_btn);
        rejectbtn=(Button)itemView.findViewById(R.id.reject_btn);


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
