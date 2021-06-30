package com.example.e_commerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_commerce.Admin.AdminActivity;
import com.example.e_commerce.Model.Product;
import com.example.e_commerce.Model.Users;
import com.example.e_commerce.Prevalent.Prevalent;
import com.example.e_commerce.ViewHolder.ProductViewHolder;
import com.example.e_commerce.ViewHolder.SearchFriendViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


public class fragmentSearchFriend extends Fragment {


    public fragmentSearchFriend() {
        // Required empty public constructor
    }

    private RecyclerView friendSearchList;
    private EditText friendSearchEditText;
    private FloatingActionButton friendSearchBtn;
    private DatabaseReference friendSearchRef;
    private String friendSearchText,friendRequestKey;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toast.makeText(getActivity(),"Try again later",Toast.LENGTH_SHORT).show();
        View view=inflater.inflate(R.layout.fragment_search_friend, container, false);

        //onStart();
        Log.d("a", "a1");

        friendSearchList=view.findViewById(R.id.recycler_menu_2);

        friendSearchList.hasFixedSize();

        friendSearchList.setLayoutManager(new LinearLayoutManager(getContext()));


        friendSearchEditText=view.findViewById(R.id.search_chat_name);

        friendSearchBtn=view.findViewById(R.id.search_btn2);


        friendSearchRef=FirebaseDatabase.getInstance().getReference();

        friendSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               friendSearchText=friendSearchEditText.getText().toString();
                onStart();
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        friendSearchRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot)

            {
                FirebaseRecyclerOptions<Users> options=new FirebaseRecyclerOptions.Builder<Users>().setQuery(friendSearchRef.child("Users").orderByChild("name").startAt(friendSearchText),Users.class).build();

                final FirebaseRecyclerAdapter<Users, SearchFriendViewHolder> adapter= new FirebaseRecyclerAdapter<Users, SearchFriendViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull final SearchFriendViewHolder holder, int i, @NonNull final Users model)
                    {
                       // Toast.makeText(homeActivity.this,"yes",Toast.LENGTH_LONG).show();
                        if(!Prevalent.currentUser.getName().equals(model.getName()))
                        {
                            Log.d("a", "c");
                            holder.txtFriendName.setText(model.getName());

                            Picasso.get().load(model.getImage()).into(holder.FriendimageView);


                            if(snapshot.child("Users").child(Prevalent.currentUser.getPhone()).child("friends").child(model.getPhone()).exists())
                            {
                                holder.addbtn.setText("Friend");
                                holder.addbtn.setEnabled(false);

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i=new Intent(getContext(),ChatMessageActivity.class);
                                        i.putExtra("phone",model.getPhone());
                                        i.putExtra("name",model.getName());
                                        startActivity(i);
                                    }
                                });
                            }
                            else if((snapshot.child("Users").child(Prevalent.currentUser.getPhone()).child("sent_requests").child(model.getName()).exists()))
                            {
                                holder.addbtn.setText("Request sent");
                                holder.addbtn.setEnabled(false);
                            }
                            else
                            {
                                holder.addbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Log.d("d", "d");

                                        holder.addbtn.setText("Request sent");
                                        holder.addbtn.setEnabled(false);

                                        String saveCurrentDate,saveCurrentTime;

                                        Calendar calendar =Calendar.getInstance();
                                        SimpleDateFormat currentDate= new SimpleDateFormat("dd MMM yyyy");

                                        saveCurrentDate= currentDate.format(calendar.getTime());

                                        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");

                                        saveCurrentTime= currentTime.format(calendar.getTime());

                                        friendRequestKey=saveCurrentDate+saveCurrentTime;



                                        friendSearchRef.child("Users").child(model.getPhone()).child("friend_requests").child(Prevalent.currentUser.getPhone() ).setValue(friendRequestKey).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful())
                                                {
                                                    friendSearchRef.child("Users").child(Prevalent.currentUser.getPhone()).child("sent_requests").child(model.getName()).setValue("1");
                                                    Toast.makeText(getActivity(),"Request sent successfully.",Toast.LENGTH_SHORT).show();
                                                    holder.addbtn.setText("Request sent");
                                                    holder.addbtn.setEnabled(false);
                                                }
                                                else
                                                {
                                                    Toast.makeText(getActivity(),"Try again later",Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });





                                    }
                                });
                            }

                        }
                        else
                        {
                            holder.itemView.setVisibility(View.GONE);
                            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                        }



                    }

                    @NonNull
                    @Override
                    public SearchFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_friend_search_list,null);
                       SearchFriendViewHolder holder=new SearchFriendViewHolder(view);

                        return holder;
                    }
                };
                friendSearchList.setAdapter(adapter);
                adapter.startListening();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
