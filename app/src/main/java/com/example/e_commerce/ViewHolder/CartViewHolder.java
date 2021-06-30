package com.example.e_commerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.R;
import com.example.e_commerce.interfaces.ItemClickListener;
import com.google.android.material.imageview.ShapeableImageView;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView cartProductName,cartQuantity,cartPrice;
    public ShapeableImageView cartImageView;
    private ItemClickListener itemClicklistener;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        cartImageView =(ShapeableImageView) itemView.findViewById(R.id.cart_image_view);
        cartProductName =(TextView)itemView.findViewById(R.id.cart_product_name);
        cartQuantity =(TextView)itemView.findViewById(R.id.cart_quantity);
        cartPrice =(TextView)itemView.findViewById(R.id.cart_product_price);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.itemClicklistener=listener;
    }

    @Override
    public void onClick(View v)
    {
        itemClicklistener.OnClick(itemView,getAdapterPosition(),false);

    }
}
