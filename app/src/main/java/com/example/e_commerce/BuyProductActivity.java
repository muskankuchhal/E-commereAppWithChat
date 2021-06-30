package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Adapter.ProductAdapter;
import com.example.e_commerce.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BuyProductActivity extends AppCompatActivity {

    private String brandName,category;
    private RecyclerView recyclerView;
    private DatabaseReference ProductRef;
    RecyclerView.LayoutManager layoutManager;
    private TextView buy;

    private ArrayList<Product>  productList;
     ProductAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);

        brandName= getIntent().getExtras().get("brand").toString();
        category= getIntent().getExtras().get("category").toString();
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        buy=(TextView)findViewById(R.id.buy);

        buy.setText(brandName.toUpperCase());



         productList = new ArrayList<>();

        recyclerView=(RecyclerView)findViewById(R.id.recycler_menu_1);

        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new ProductAdapter(productList,BuyProductActivity.this,brandName,category);
        recyclerView.setAdapter(adapter);

    }



    @Override
    protected void onStart() {
        super.onStart();
        ProductRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)

            {
                ProductRef.orderByChild("brand").equalTo(brandName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot datas: snapshot.getChildren()) {

                            if(datas.child("category").getValue().toString().equals(category))
                            {
                                String brand = datas.child("brand").getValue().toString();
                                String categoryName = datas.child("category").getValue().toString();
                                String date = datas.child("date").getValue().toString();
                                String description = datas.child("description").getValue().toString();
                                String image = datas.child("image").getValue().toString();
                                String name = datas.child("name").getValue().toString();
                                String pid = datas.child("pid").getValue().toString();
                                String price = datas.child("price").getValue().toString();
                                String time = datas.child("time").getValue().toString();



                                productList.add(new Product(name,description,price,pid,date,time,image,brand,categoryName));
                                adapter.notifyItemInserted(productList.size()-1);
                            }

                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(BuyProductActivity.this, Integer.toString(productList.size()),Toast.LENGTH_LONG).show();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // productList.add(new Product("A","A","A","A","A","A","A","A"));



    }
}
