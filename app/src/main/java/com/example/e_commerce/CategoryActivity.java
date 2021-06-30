package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.e_commerce.Admin.AdminActivity;

public class CategoryActivity extends AppCompatActivity {

    private ImageView machines,abrasives,spares,handtools;
    private String brand,userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        machines=(ImageView)findViewById(R.id.machines);
        abrasives=(ImageView)findViewById(R.id.abrasives);
        spares=(ImageView)findViewById(R.id.spares);
        handtools=(ImageView)findViewById(R.id.handtools);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            brand = bundle.getString("brand");
            userType=bundle.getString("user");

        }

        machines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userType.equals("buyer"))
                {
                    Intent i=new Intent(CategoryActivity.this,BuyProductActivity.class);
                    i.putExtra("type","buyer");
                    i.putExtra("brand",brand);
                    i.putExtra("category","machines");
                    startActivity(i);
                }
                else
                {
                    Intent i=new Intent(CategoryActivity.this, AdminActivity.class);
                    i.putExtra("type","admin");
                    i.putExtra("brand",brand);
                    i.putExtra("category","machines");
                    startActivity(i);
                }

            }
        });

        abrasives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userType.equals("buyer"))
                {
                    Intent i=new Intent(CategoryActivity.this,BuyProductActivity.class);
                    i.putExtra("brand",brand);
                    i.putExtra("category","abrasives");
                    startActivity(i);
                }
                else
                {
                    Intent i=new Intent(CategoryActivity.this, AdminActivity.class);
                    i.putExtra("brand",brand);
                    i.putExtra("category","abrasives");
                    startActivity(i);
                }
            }
        });

        spares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userType.equals("buyer"))
                {
                    Intent i=new Intent(CategoryActivity.this,BuyProductActivity.class);
                    i.putExtra("brand",brand);
                    i.putExtra("category","spares");
                    startActivity(i);
                }
                else
                {
                    Intent i=new Intent(CategoryActivity.this, AdminActivity.class);
                    i.putExtra("brand",brand);
                    i.putExtra("category","spares");
                    startActivity(i);
                }

            }
        });

        handtools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userType.equals("buyer"))
                {
                    Intent i=new Intent(CategoryActivity.this,BuyProductActivity.class);
                    i.putExtra("brand",brand);
                    i.putExtra("category","handtools");
                    startActivity(i);
                }
                else
                {
                    Intent i=new Intent(CategoryActivity.this, AdminActivity.class);
                    i.putExtra("brand",brand);
                    i.putExtra("category","handtools");
                    startActivity(i);
                }

            }
        });
    }
}
