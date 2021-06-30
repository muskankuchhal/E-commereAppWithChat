package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Model.Product;
import com.example.e_commerce.Prevalent.Prevalent;
import com.example.e_commerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private TextView pName,pDescription,pPrice;
    private EditText pQuantity;
    private ShapeableImageView pImage;
    private Button addCart;
    private String productID,Url,state="";
    private static final int galleryPicker=1;
    private ImageButton minus,plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID=getIntent().getStringExtra("pid");
        minus=(ImageButton)findViewById(R.id.minus);
        plus=(ImageButton)findViewById(R.id.plus);



        pName=(TextView)findViewById(R.id.p_name_details);
        pDescription=(TextView)findViewById(R.id.p_description_details);
        pPrice=(TextView)findViewById(R.id.p_price_details);

        pQuantity=(EditText)findViewById(R.id.p_quantity_details);
        pQuantity.setText("0");

        pImage =(ShapeableImageView) findViewById(R.id.p_image_details);
        addCart=(Button)findViewById(R.id.add_to_cart);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=Integer.parseInt(pQuantity.getText().toString());
                pQuantity.setText(String.valueOf(i-1));

            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=Integer.parseInt(pQuantity.getText().toString());
                pQuantity.setText(String.valueOf(i+1));

            }
        });


        
        getProductDetails(productID);

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(state.equals("order placed")|| state.equals("order shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this,"order done",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String saveCurrentDate,saveCurrentTime;

                    Calendar calendar =Calendar.getInstance();
                    SimpleDateFormat currentDate= new SimpleDateFormat("dd MMM yyyy");

                    saveCurrentDate= currentDate.format(calendar.getTime());

                    SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");

                    saveCurrentTime= currentTime.format(calendar.getTime());

                    final DatabaseReference cartRef= FirebaseDatabase.getInstance().getReference().child("Cart list");

                    final HashMap<String,Object> cartMap= new HashMap<>();
                    cartMap.put("pid",productID);
                    cartMap.put("pname",pName.getText().toString());
                    cartMap.put("price",pPrice.getText().toString());
                    cartMap.put("date",saveCurrentDate);
                    cartMap.put("time",saveCurrentTime);
                    cartMap.put("quantity",pQuantity.getText().toString());
                    cartMap.put("discount","");
                    cartMap.put("image",Url);




                    cartRef.child("User View").child(Prevalent.currentUser.getPhone()).child("Products").child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                /*cartRef.child("Admin View").child(Prevalent.currentUser.getPhone()).child("Products").child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(ProductDetailsActivity.this,"Product added to cart successfully.",Toast.LENGTH_SHORT).show();

                                            Intent i =new Intent(ProductDetailsActivity.this,homeActivity.class);
                                            startActivity(i);
                                        }
                                    }

                                });
                                 */
                                Toast.makeText(ProductDetailsActivity.this,"Product added to cart successfully.",Toast.LENGTH_SHORT).show();

                                Intent i =new Intent(ProductDetailsActivity.this,homeActivity.class);
                                startActivity(i);

                            }
                            else
                            {
                                String message=task.getException().toString();
                                Toast.makeText(ProductDetailsActivity.this,"error: "+message,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();
    }


    private void getProductDetails(String productID)
    {
        DatabaseReference productRef=FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    Product p=snapshot.getValue(Product.class);

                    pName.setText(p.getName());
                    pDescription.setText(p.getDescription());
                    pPrice.setText("Rs."+p.getPrice());
                    Url=p.getImage();

                    Picasso.get().load(p.getImage()).into(pImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkOrderState()
    {
        DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders list").child(Prevalent.currentUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String shippingState=snapshot.child("state").getValue().toString();
                    String shippingName=snapshot.child("shipment_name").getValue().toString();

                    if(shippingState.equals("shipped"))
                    {
                        state="order shipped";
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        state="order placed";
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
