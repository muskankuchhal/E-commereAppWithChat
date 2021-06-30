package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.e_commerce.Admin.AdminCheckOrderActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.paperdb.Paper;

public class BrandActivity extends AppCompatActivity {

    private String type="";
    private Button checkNewOrder,logout,edit;
    private ImageView mm,skyflex,gvolt,others;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);


        type=getIntent().getExtras().get("user").toString();


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);


        mm=(ImageView)findViewById(R.id.mm);
        skyflex=(ImageView)findViewById(R.id.skyflex);
        gvolt=(ImageView)findViewById(R.id.gvolt);
        others=(ImageView)findViewById(R.id.other);

        if(type.equals("buyer"))
        {
            bottomNavigationView.setVisibility(View.GONE);
        }
        else {
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.edit:
                        {
                            Intent i = new Intent(BrandActivity.this, homeActivity.class);
                            i.putExtra("admin", "admin");
                            startActivity(i);
                            break;
                        }
                        case R.id.logout:
                        {
                            Paper.book().destroy();
                            Intent i = new Intent(BrandActivity.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                            break;
                        }
                        case R.id.check_new_order:
                        {
                            Intent i = new Intent(BrandActivity.this, AdminCheckOrderActivity.class);
                            startActivity(i);
                        }
                    }
                    return true;
                }
            });
        }

        mm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("buyer"))
                {
                    Intent i=new Intent(BrandActivity.this, CategoryActivity.class);
                    i.putExtra("brand","mm");
                    i.putExtra("user","buyer");
                    startActivity(i);
                }
                else{
                    Intent i=new Intent(BrandActivity.this,  CategoryActivity.class);
                    i.putExtra("brand","mm");
                    i.putExtra("user","admin");
                    startActivity(i);
                }

            }
        });

        skyflex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("buyer"))
                {
                    Intent i=new Intent(BrandActivity.this,  CategoryActivity.class);
                    i.putExtra("brand","skyflex");
                    i.putExtra("user","buyer");
                    startActivity(i);
                }
                else{
                    Intent i=new Intent(BrandActivity.this,  CategoryActivity.class);
                    i.putExtra("brand","skyflex");
                    i.putExtra("user","admin");
                    startActivity(i);
                }

            }
        });

        gvolt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("buyer"))
                {
                    Intent i=new Intent(BrandActivity.this,  CategoryActivity.class);
                    i.putExtra("brand","gvolt");
                    i.putExtra("user","buyer");
                    startActivity(i);
                }
                else{
                    Intent i=new Intent(BrandActivity.this,  CategoryActivity.class);
                    i.putExtra("brand","gvolt");
                    i.putExtra("user","admin");
                    startActivity(i);
                }

            }
        });

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("buyer"))
                {
                    Intent i=new Intent(BrandActivity.this,  CategoryActivity.class);
                    i.putExtra("brand","others");
                    i.putExtra("user","buyer");
                    startActivity(i);
                }
                else{
                    Intent i=new Intent(BrandActivity.this,  CategoryActivity.class);
                    i.putExtra("brand","others");
                    i.putExtra("user","admin");
                    startActivity(i);
                }

            }
        });

    }
}
