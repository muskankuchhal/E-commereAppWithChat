package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.e_commerce.Adapter.MessageAdapter;
import com.example.e_commerce.Model.Message;
import com.example.e_commerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatMessageActivity extends AppCompatActivity {

    MessageAdapter adapter;
    ArrayList<Message> msgs;
    ImageButton sendbtn;
    EditText Message;
    FirebaseDatabase database;
    String senderRoom, receiverRoom;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);

        String name=getIntent().getStringExtra("name");
        receiverRoom=getIntent().getStringExtra("phone");

        Toast.makeText(this,name,Toast.LENGTH_SHORT).show();

        senderRoom= Prevalent.currentUser.getPhone();

        msgs=new ArrayList<>();
        adapter=new MessageAdapter(this,msgs,senderRoom,receiverRoom);

        recyclerView=(RecyclerView)findViewById(R.id.chat_friend_RL);
        LinearLayoutManager mLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        //recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);

//        getSupportActionBar().setTitle(name);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Message=(EditText)findViewById(R.id.Edit_text);
        sendbtn=(ImageButton)findViewById(R.id.send_message_btn);

        database =FirebaseDatabase.getInstance();

        database.getReference().child("Chats").child(senderRoom).child(receiverRoom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                msgs.clear();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    Message m=data.getValue(Message.class);
                    m.setMessageId(data.getKey());
                    msgs.add(m);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgText=Message.getText().toString();
                Message.setText("");

                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                String Messagekey = dateFormat.format(date);


                final Message message=new Message(Messagekey,msgText,Prevalent.currentUser.getPhone());
                //Toast.makeText(ChatMessageActivity.this,String.valueOf(date.getTime()),Toast.LENGTH_SHORT).show();

                database.getReference().child("Chats").
                        child(senderRoom).child(receiverRoom).child("messages").child(Messagekey).setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        database.getReference().child("Chats").
                                child(receiverRoom).child(senderRoom).child("messages").child(Messagekey).setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                database.getReference().child("Chats").
                                        child(receiverRoom).child(senderRoom).child("last_message").setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        database.getReference().child("Chats").
                                                child(senderRoom).child(receiverRoom).child("last_message").setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        });

                                    }
                                });

                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
