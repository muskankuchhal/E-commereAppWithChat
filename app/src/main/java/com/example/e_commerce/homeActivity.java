package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.Model.Product;
import com.example.e_commerce.Prevalent.Prevalent;
import com.example.e_commerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class homeActivity extends AppCompatActivity {

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    FloatingActionButton fab,searchBtn;
    private DatabaseReference ProductRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private EditText searchProductName;
    private String searchInput,type="";
    TextView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

       Intent i=getIntent();
        Bundle bundle=i.getExtras();
        if(bundle!=null){
            type=getIntent().getExtras().get("admin").toString();
        }


        ProductRef= FirebaseDatabase.getInstance().getReference().child("Products");

        Paper.init(this);

       Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        searchBtn=(FloatingActionButton) findViewById(R.id.search_btn);
        searchProductName=(EditText)findViewById(R.id.search_product_name);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                searchInput=searchProductName.getText().toString();
                onStart();
            }
        });


        navigationView=(NavigationView)findViewById(R.id.nav_menu);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);

        //fab =(FloatingActionButton)findViewById(R.id.fab);

        toggle =new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Toast.makeText(homeActivity.this,"open",Toast.LENGTH_SHORT).show();

        View headerView= navigationView.getHeaderView(0);
        TextView userNametextView =headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView =headerView.findViewById(R.id.profile_image);

        recyclerView=(RecyclerView)findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        if(!type.equals("admin")){
            userNametextView.setText(Prevalent.currentUser.getName());
            Picasso.get().load(Prevalent.currentUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    int id=menuItem.getItemId();
                    if(id==R.id.nav_cart)
                    {
                        if(!type.equals("admin")) {
                            Intent i = new Intent(homeActivity.this, CartActivity.class);
                            startActivity(i);
                        }
                    }
                    else if(id==R.id.nav_orders)
                    {
                        if(!type.equals("admin"))
                        {
                            Intent i = new Intent(homeActivity.this, MyOrdersActivity.class);
                            startActivity(i);
                        }

                    }
                    else if(id==R.id.nav_categories)
                    {
                        if(!type.equals("admin"))
                        {
                            Intent i = new Intent(homeActivity.this, BrandActivity.class);
                            i.putExtra("user","buyer");
                            startActivity(i);
                        }
                    }
                    else if(id==R.id.nav_settings)
                    {
                        if(!type.equals("admin")) {
                            Intent i = new Intent(homeActivity.this, SettingsActivity.class);
                            startActivity(i);
                        }
                    }
                    else if(id==R.id.nav_logout)
                    {
                        if(!type.equals("admin")) {
                            Paper.book().destroy();

                            Intent i = new Intent(homeActivity.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    }
                    else if(id==R.id.nav_chat)
                    {
                        if(!type.equals("admin")) {
                            Paper.book().destroy();

                            Intent i = new Intent(homeActivity.this, ChatMainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);

                    return true;
                }
            });

        }
        else {
            navigationView.setVisibility(View.GONE);
        }

       FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        if(type.equals("admin")) {
            fab.setVisibility(View.GONE);
        }
       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               if(!type.equals("admin")) {
                   Intent i = new Intent(homeActivity.this, CartActivity.class);
                   startActivity(i);
               }
           }
       });

    }

    @Override
    protected void onStart() {
        super.onStart();

        ProductRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)

            {
                FirebaseRecyclerOptions<Product> options=new FirebaseRecyclerOptions.Builder<Product>().setQuery(ProductRef.orderByChild("name").startAt(searchInput),Product.class).build();

                FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter= new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Product model)
                    {
                        Toast.makeText(homeActivity.this,"yes",Toast.LENGTH_LONG).show();
                        holder.txtProductName.setText(model.getName());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = Rs."+model.getPrice());

                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(type.equals("admin"))
                                {
                                    CharSequence op[]= new CharSequence[]
                                            {
                                                    "Delete"
                                            };
                                    AlertDialog.Builder builder = new AlertDialog.Builder(homeActivity.this);

                                    builder.setItems(op, new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            if (which == 0)
                                            {
                                                FirebaseDatabase.getInstance().getReference().child("Products")
                                                        .child(model.getPid())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    Toast.makeText(homeActivity.this,"Product removed successfully.",Toast.LENGTH_SHORT).show();

                                                                    Intent i =new Intent(homeActivity.this,MainActivity.class);
                                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivity(i);
                                                                    finish();
                                                                }

                                                            }
                                                        });
                                            }

                                        }
                                    });
                                    builder.show();


                                }
                                else {
                                    Intent i=new Intent(homeActivity.this,ProductDetailsActivity.class);
                                    i.putExtra("pid",model.getPid());
                                    startActivity(i);
                                }
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,null);
                        ProductViewHolder holder=new ProductViewHolder(view);

                        return holder;
                    }
                };
                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
