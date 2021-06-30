package com.example.e_commerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Model.Cart;
import com.example.e_commerce.R;
import com.example.e_commerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminGetOrderDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView txtOrderDetails;
    private DatabaseReference orderListRef;
    private String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_get_order_details);

        recyclerView=(RecyclerView)findViewById(R.id.order_details_list);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtOrderDetails =(TextView)findViewById(R.id.order_details);

        userID= getIntent().getStringExtra("uid");

        //Toast.makeText(AdminGetOrderDetailsActivity.this,userID,Toast.LENGTH_SHORT).show();

        orderListRef =FirebaseDatabase.getInstance().getReference().child("Orders list").child("Product details").child(userID).child("Products");
        //Toast.makeText(AdminGetOrderDetailsActivity.this, "open cart1", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart()
    {
        super.onStart();


        FirebaseRecyclerOptions<Cart> options =new FirebaseRecyclerOptions.Builder<Cart>().setQuery(orderListRef,Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter= new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull final Cart model)
            {
                holder.cartProductName.setText(model.getPname());
                holder.cartQuantity.setText(model.getQuantity());
                Picasso.get().load(model.getimage()).into(holder.cartImageView);

                Toast.makeText(AdminGetOrderDetailsActivity.this,model.getPname()+"...",Toast.LENGTH_SHORT).show();
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
