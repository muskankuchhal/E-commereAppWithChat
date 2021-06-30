package com.example.e_commerce;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.e_commerce.Adapter.RequestAdapter;
import com.example.e_commerce.Model.Users;
import com.example.e_commerce.Prevalent.Prevalent;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fragmentFriendAddRemove extends Fragment {
    // TODO: Rename parameter arguments, choose names that match


    public fragmentFriendAddRemove() {
        // Required empty public constructor
    }

    private RecyclerView RequestSearchList;
    private DatabaseReference RequestSearchRef;
    private ArrayList<Users> requestList;
    RequestAdapter adapter ;
    private Map<String,Boolean> temp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toast.makeText(getActivity(),"Try again later",Toast.LENGTH_SHORT).show();
        View view=inflater.inflate(R.layout.fragment_friend_add_remove, container, false);

        //onStart();
        Log.d("a", "a1");

        requestList = new ArrayList<>();
        RequestSearchList=view.findViewById(R.id.add_remove_friend_RL);

        RequestSearchList.hasFixedSize();

        RequestSearchList.setLayoutManager(new LinearLayoutManager(getContext()));
        RequestSearchList.setItemAnimator(new DefaultItemAnimator());
        //adapter=new RequestAdapter(requestList,getContext());
        RequestSearchList.setAdapter(adapter);




        RequestSearchRef= FirebaseDatabase.getInstance().getReference();



        //onStart();


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        temp=new HashMap<>();
        RequestSearchRef.child("Users").child(Prevalent.currentUser.getPhone()).child("friend_requests").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot datas: snapshot.getChildren()) {


                    String phone=datas.getKey().toString();
                    //Toast.makeText(getActivity(),phone,Toast.LENGTH_SHORT).show();
                    temp.put(phone,Boolean.TRUE);
                }


                RequestSearchRef.child("Users").child(Prevalent.currentUser.getPhone()).child("friend_requests").orderByKey().addChildEventListener(new ChildEventListener() {


                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot Snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot Snapshot) {

                        String phone=Snapshot.getKey().toString();
                        temp.remove(phone);

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot Snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        RequestSearchRef.child("Users").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                requestList.clear();
              for(DataSnapshot data:snapshot.getChildren())
              {
                  if(temp.get(data.getKey())==Boolean.TRUE)
                  {
                        Users user= data.getValue(Users.class);
                          requestList.add(user);
//                      adapter.notifyItemInserted(requestList.size()-1);
//                      Log.d("a",data.getKey());
                  }
              }

              adapter=new RequestAdapter(requestList,getContext());
              RequestSearchList.setAdapter(adapter);

               // Toast.makeText(getActivity(), Integer.toString(requestList.size()),Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
