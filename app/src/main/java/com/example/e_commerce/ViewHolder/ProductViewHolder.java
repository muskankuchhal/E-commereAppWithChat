package com.example.e_commerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.R;
import com.example.e_commerce.interfaces.ItemClickListener;
import com.google.android.material.imageview.ShapeableImageView;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductDescription,txtProductPrice;
    public ShapeableImageView imageView;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView =(ShapeableImageView) itemView.findViewById(R.id.p_image);
        txtProductName =(TextView)itemView.findViewById(R.id.p_name);
        txtProductDescription =(TextView)itemView.findViewById(R.id.p_description);
        txtProductPrice =(TextView)itemView.findViewById(R.id.p_price);
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
