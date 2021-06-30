package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Admin.AdminCheckOrderActivity;
import com.example.e_commerce.Admin.AdminGetOrderDetailsActivity;
import com.example.e_commerce.Model.AdminOrder;
import com.example.e_commerce.Model.Cart;
import com.example.e_commerce.Prevalent.Prevalent;
import com.example.e_commerce.ViewHolder.AdminOrderViewHolder;
import com.example.e_commerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MyOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView txtMyOrders;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        recyclerView=(RecyclerView)findViewById(R.id.my_order_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        txtMyOrders =(TextView)findViewById(R.id.txt_orders);

        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders list");
    }


    @Override
    protected void onStart() {
        super.onStart();

        //Toast.makeText(CartActivity.this,"open cart1",Toast.LENGTH_SHORT).show();

        FirebaseRecyclerOptions<Cart> options =new FirebaseRecyclerOptions.Builder<Cart>().setQuery(orderRef.child("Shipped orders").child(Prevalent.currentUser.getPhone()).child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter= new FirebaseRecyclerAdapter<Cart,CartViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, final int i, @NonNull final Cart model)
            {
                holder.cartProductName.setText(model.getPname());
                holder.cartQuantity.setText(model.getQuantity());
                Picasso.get().load(model.getimage()).into(holder.cartImageView);
            }
            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,null);
                CartViewHolder holder=new CartViewHolder(view);

                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
