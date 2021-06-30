package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_commerce.Model.Users;
import com.example.e_commerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrderActivity extends AppCompatActivity {

    private EditText shipmentName,shipmentPhone,shipmentAddress,shipmentCity;
    private Button confirmOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        shipmentName=((EditText)findViewById(R.id.shipment_name));
        shipmentPhone=((EditText)findViewById(R.id.shipment_phone));
        shipmentAddress=((EditText)findViewById(R.id.shipment_address));
        shipmentCity=((EditText)findViewById(R.id.shipment_city));


        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userdata = snapshot.child(Prevalent.currentUser.getPhone()).getValue(Users.class);

                if(snapshot.child(Prevalent.currentUser.getPhone()).exists())
                {
                    shipmentName.setText(userdata.getName());
                    shipmentPhone.setText(userdata.getPhone());
                    shipmentAddress.setText(userdata.getAddress());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        confirmOrder =(Button)findViewById(R.id.confirm_order);

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(shipmentName.getText().toString())||TextUtils.isEmpty(shipmentPhone.getText().toString())||TextUtils.isEmpty(shipmentAddress.getText().toString())/*||TextUtils.isEmpty(shipmentCity.getText().toString())*/)
                {
                    Toast.makeText(ConfirmOrderActivity.this,"Please fill the details.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String saveCurrentDate,saveCurrentTime;

                    Calendar calendar =Calendar.getInstance();
                    SimpleDateFormat currentDate= new SimpleDateFormat("dd MMM yyyy");

                    saveCurrentDate= currentDate.format(calendar.getTime());

                    SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");

                    saveCurrentTime= currentTime.format(calendar.getTime());

                    final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference().child("Orders list");
                    final DatabaseReference cartRef= FirebaseDatabase.getInstance().getReference().child("Cart list").child("User View")
                        .child(Prevalent.currentUser.getPhone());

                    final HashMap<String,Object> OrderMap= new HashMap<>();
                    OrderMap.put("shipment_name",shipmentName.getText().toString());
                    OrderMap.put("shipment_address",shipmentAddress.getText().toString());
                    OrderMap.put("shipment_phone",shipmentPhone.getText().toString());
                    //OrderMap.put("shipment_city",shipmentCity.getText().toString());
                    OrderMap.put("date",saveCurrentDate);
                    OrderMap.put("time",saveCurrentTime);
                    OrderMap.put("state","not shipped");


                    orderRef.child("Shipment details").child(Prevalent.currentUser.getPhone()).updateChildren(OrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {   /// changes
                            if(task.isSuccessful())
                            {
                                cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    // Now "DataSnapshot" holds the key and the value at the "fromPath".
                                    // Let's move it to the "toPath". This operation duplicates the
                                    // key/value pair at the "fromPath" to the "toPath".
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        orderRef.child("Product details").child(dataSnapshot.getKey())
                                                .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                        if (databaseError == null) {

                                                            // In order to complete the move, we are going to erase
                                                            // the original copy by assigning null as its value.
                                                            cartRef.setValue(null);
                                                            Toast.makeText(ConfirmOrderActivity.this,"Order placed successfully.",Toast.LENGTH_SHORT).show();

                                                            Intent i =new Intent(ConfirmOrderActivity.this,homeActivity.class);
                                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(i);
                                                            finish();
                                                        }

                                                    }
                                                });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            else
                            {
                                String message=task.getException().toString();
                                Toast.makeText(ConfirmOrderActivity.this,"error: "+message,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
