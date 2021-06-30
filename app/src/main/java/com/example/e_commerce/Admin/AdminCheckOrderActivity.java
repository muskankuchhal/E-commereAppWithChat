
package com.example.e_commerce.Admin;

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

import com.example.e_commerce.ConfirmOrderActivity;
import com.example.e_commerce.Model.AdminOrder;
import com.example.e_commerce.R;
import com.example.e_commerce.ViewHolder.AdminOrderViewHolder;
import com.example.e_commerce.homeActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminCheckOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView txtNewOrders;
    private DatabaseReference adminListRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_order);

        recyclerView=(RecyclerView)findViewById(R.id.admin_order_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        txtNewOrders =(TextView)findViewById(R.id.new_orders);

        Toast.makeText(AdminCheckOrderActivity.this,"no",Toast.LENGTH_LONG).show();

        adminListRef= FirebaseDatabase.getInstance().getReference().child("Orders list");

    }


    @Override
    protected void onStart() {
        super.onStart();

        //Toast.makeText(CartActivity.this,"open cart1",Toast.LENGTH_SHORT).show();

        FirebaseRecyclerOptions<AdminOrder> options =new FirebaseRecyclerOptions.Builder<AdminOrder>().setQuery(adminListRef.child("Shipment details"),AdminOrder.class).build();

        FirebaseRecyclerAdapter<AdminOrder, AdminOrderViewHolder> adapter= new FirebaseRecyclerAdapter<AdminOrder,AdminOrderViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, final int i, @NonNull final AdminOrder model)
            {
                final String uid=getRef(i).getKey();

                holder.adminShipmentName.setText(model.getShipment_name());
                holder.adminShipmentPhone.setText(model.getShipment_phone());
                holder.adminShipmentAddressCity.setText(model.getShipment_address());
                holder.adminShipmentDateTime.setText(model.getDate()+" "+model.getTime());

                holder.getDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(AdminCheckOrderActivity.this, AdminGetOrderDetailsActivity.class);
                        //Toast.makeText(AdminCheckOrderActivity.this,"get ordrs",Toast.LENGTH_SHORT).show();
                        i.putExtra("uid",uid);
                        startActivity(i);
                    }
                });
                holder.shipOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String uid=getRef(i).getKey();
                        adminListRef.child("Shipment details").child(uid).removeValue();

                        adminListRef.child("Product details").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            // Now "DataSnapshot" holds the key and the value at the "fromPath".
                            // Let's move it to the "toPath". This operation duplicates the
                            // key/value pair at the "fromPath" to the "toPath".
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                adminListRef.child("Shipped orders").child(dataSnapshot.getKey())
                                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                if (databaseError == null) {

                                                    // In order to complete the move, we are going to erase
                                                    // the original copy by assigning null as its value.
                                                    adminListRef.child("Product details").child(uid).setValue(null);
                                                }

                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });
            }
            @NonNull
            @Override
            public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_item,null);
                AdminOrderViewHolder holder=new AdminOrderViewHolder(view);

                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}
