package com.example.e_commerce.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.MainActivity;
import com.example.e_commerce.Model.Product;
import com.example.e_commerce.ProductDetailsActivity;
import com.example.e_commerce.R;
import com.example.e_commerce.ViewHolder.ProductViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private ArrayList<Product> productList;
    private Context context;
    String brandName,type;

    public ProductAdapter(ArrayList<Product> productList,Context context, String brandName, String type) {

        this.context=context;
        this.brandName=brandName;
        this.type=type;
        this.productList = productList;
    }

   /* public class myholder extends RecyclerView.ViewHolder{
        private TextView txtProductName,txtProductDescription,txtProductPrice;
        private ImageView imageView;

        public myholder(final View itemView) {
            super(itemView);

            imageView =(ImageView)itemView.findViewById(R.id.p_image);
            txtProductName =(TextView)itemView.findViewById(R.id.p_name);
            txtProductDescription =(TextView)itemView.findViewById(R.id.p_description);
            txtProductPrice =(TextView)itemView.findViewById(R.id.p_price);
            }
        }*/


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
        return new ProductViewHolder(itemview);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {

        holder.txtProductName.setText(productList.get(position).getName());
        holder.txtProductDescription.setText(productList.get(position).getDescription());
        holder.txtProductPrice.setText("Price = Rs."+ productList.get(position).getPrice());

        Picasso.get().load(productList.get(position).getImage()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("admin"))
                {
                    CharSequence op[]= new CharSequence[]
                            {
                                    "Delete"
                            };
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setItems(op, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            if (which == 0)
                            {
                                FirebaseDatabase.getInstance().getReference().child("Products")
                                        .child(productList.get(position).getPid())
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(context,"Product removed successfully.",Toast.LENGTH_SHORT).show();

                                                    Intent i =new Intent(context, MainActivity.class);
                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    context.startActivity(i);
                                                }

                                            }
                                        });
                            }

                        }
                    });
                    builder.show();


                }
                else {
                    Intent i=new Intent(context, ProductDetailsActivity.class);
                    i.putExtra("pid",productList.get(position).getPid());
                    context.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
