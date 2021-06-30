package com.example.e_commerce;

import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_commerce.Adapter.MessageAdapter;
import com.example.e_commerce.Adapter.RecentChatAdapter;
import com.example.e_commerce.Model.Message;
import com.example.e_commerce.Model.Users;
import com.example.e_commerce.Prevalent.Prevalent;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */


public class fragmentChatMain extends Fragment {


    public fragmentChatMain() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private EditText friendSearchEditText;
    private FloatingActionButton friendSearchBtn;
    private DatabaseReference friendSearchRef;
    private String friendSearchText,friendRequestKey;
    private ArrayList<Pair<Message,String>> recentChatList;
    RecentChatAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toast.makeText(getActivity(),"Try again later",Toast.LENGTH_SHORT).show();
        View view=inflater.inflate(R.layout.fragment_chat_main, container, false);

        //onStart();
        Log.d("a", "a1");

        recentChatList=new ArrayList<Pair<Message, String>>();
        adapter=new RecentChatAdapter(getContext(),recentChatList);

        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_menu_3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);


        friendSearchEditText=view.findViewById(R.id.search_chat_name);

        friendSearchBtn=view.findViewById(R.id.search_btn3);


        friendSearchRef= FirebaseDatabase.getInstance().getReference();

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

        friendSearchRef.child("Chats").child(Prevalent.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot data: snapshot.getChildren())
                {
                    Message m= data.child("last_message").getValue(Message.class);
                      recentChatList.add(new Pair(m,data.getKey()));

                }

                recentChatList.sort((o1, o2) -> o1.first.getMessageId().compareTo(o2.first.getMessageId()));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
