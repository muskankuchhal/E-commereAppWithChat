package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Model.Cart;
import com.example.e_commerce.Model.Product;
import com.example.e_commerce.Prevalent.Prevalent;
import com.example.e_commerce.ViewHolder.CartViewHolder;
import com.example.e_commerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button placeOrder,contactSeller;
    private TextView txtTotalPrice,txtMsg1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView=(RecyclerView)findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        placeOrder=(Button)findViewById(R.id.place_order);
        contactSeller=(Button)findViewById(R.id.contact);

        txtTotalPrice =(TextView)findViewById(R.id.total_price);
        txtMsg1 =(TextView)findViewById(R.id.msg1);

        contactSeller.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                CharSequence op[]= new CharSequence[]
                        {
                                "Phone","Whatsapp","Message"
                        };
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("Contact Options");

                builder.setItems(op, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(which==0)
                        {
                            Intent intent =new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:+9350509675"));
                            startActivity(intent);
                        }
                        else if(which==1)
                        {
                            boolean installed= whatsAppInstalled("com.whatsapp");

                            if(installed)
                            {
                                Intent intent =new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("https://wa.me/+91 9350509675"));
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(CartActivity.this,"WhatsApp is not installed. Kindly install WhatsApp.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(which==2)
                        {
                            Intent intent =new Intent(Intent.ACTION_SENDTO);
                            intent.setData(Uri.parse("smsto:+91 9350509675"));
                            startActivity(intent);
                        }
                    }

                });
                builder.show();

            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(CartActivity.this,ConfirmOrderActivity.class);
                startActivity(i);
            }
        });

    }

    private boolean whatsAppInstalled(String url) {

        PackageManager packageManager= getPackageManager();

        boolean installed;
        try{
            packageManager.getPackageInfo(url,PackageManager.GET_ACTIVITIES);
            installed=true;

        } catch (PackageManager.NameNotFoundException e)
        {
            installed=false;
        }
        return installed;
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();

        //Toast.makeText(CartActivity.this,"open cart1",Toast.LENGTH_SHORT).show();

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart list");

        FirebaseRecyclerOptions<Cart> options =new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("User View")
                .child(Prevalent.currentUser.getPhone()).child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter= new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull final Cart model)
            {
                Toast.makeText(CartActivity.this,"open cart",Toast.LENGTH_SHORT).show();
                holder.cartProductName.setText(model.getPname());
                holder.cartQuantity.setText(model.getQuantity());
                Picasso.get().load(model.getimage()).into(holder.cartImageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        CharSequence options[]= new CharSequence[]
                                {
                                        "Edit","Delete"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which==0)
                                {
                                    Intent i= new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    i.putExtra("pid",model.getPid());
                                    startActivity(i);
                                }
                                else if(which==1)
                                {
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentUser.getPhone()).child("Products").child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(CartActivity.this,"item removed successfully.",Toast.LENGTH_SHORT).show();
                                                Intent i= new Intent(CartActivity.this,homeActivity.class);
                                                startActivity(i);
                                            }

                                        }
                                    });
                                }

                            }
                        });
                        builder.show();

                    }
                });




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

    private void checkOrderState()
    {
        DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders list").child("Shipment details").child(Prevalent.currentUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String shippingState=snapshot.child("state").getValue().toString();
                    String shippingName=snapshot.child("shipment_name").getValue().toString();

                    if(shippingState.equals("not shipped"))
                    {
                        txtMsg1.setVisibility(View.VISIBLE);
                        placeOrder.setVisibility(View.GONE);
                        contactSeller.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
