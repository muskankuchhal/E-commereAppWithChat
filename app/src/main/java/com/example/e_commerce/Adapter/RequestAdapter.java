package com.example.e_commerce.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.Model.Users;
import com.example.e_commerce.Prevalent.Prevalent;
import com.example.e_commerce.R;
import com.example.e_commerce.ViewHolder.SearchRequestViewHolder;
import com.example.e_commerce.interfaces.ItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<SearchRequestViewHolder>  {

    private ArrayList<Users> requestList;
    private Context context;


    public RequestAdapter(ArrayList<Users> requestList,Context context) {

        this.context=context;
        this.requestList = requestList;
    }




    @NonNull
    @Override
    public SearchRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_add_remove,parent,false);
        return new SearchRequestViewHolder(itemview);
    }


    @Override
    public void onBindViewHolder(@NonNull final SearchRequestViewHolder holder, final int position)
    {

        Log.d("a", "x");

        holder.txtRequestName.setText(requestList.get(position).getName());


        Picasso.get().load(requestList.get(position).getImage()).into(holder.RequestimageView);

        holder.acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentUser.getPhone()).child("friends").child(requestList.get(position).getPhone()).setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(requestList.get(position).getPhone()).child("sent_requests").child(Prevalent.currentUser.getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    final String phone=requestList.get(position).getPhone();


                                    FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentUser.getPhone()).child("friend_requests").child(phone).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("friends").child(Prevalent.currentUser.getPhone()).setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(v.getContext(),"Friend added successfully.",Toast.LENGTH_SHORT).show();
                                                    holder.acceptbtn.setText("Friend");
                                                    holder.acceptbtn.setEnabled(false);
                                                    holder.rejectbtn.setVisibility(View.GONE);
                                                }
                                            });

                                        }
                                    });


                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(v.getContext(),"Try again later1",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        holder.rejectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentUser.getPhone()).child("friend_requests").child(requestList.get(position).getPhone()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(v.getContext(),"Deleted request",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.setIsRecyclable(false);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }


}
