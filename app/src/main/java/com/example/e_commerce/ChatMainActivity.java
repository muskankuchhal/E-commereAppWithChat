package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.rey.material.widget.FloatingActionButton;

public class ChatMainActivity extends AppCompatActivity {


    private RecyclerView friendSearchList;
    private EditText friendSearchEditText;
    private FloatingActionButton friendSearchBtn;
    private DatabaseReference friendSearchRef;
    private String friendSearchText,friendRequestKey;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        fragmentChatMain chatmessage=new fragmentChatMain();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_chat_main1,chatmessage).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_chat);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.chat:
                    {
                        fragmentChatMain chatmessage=new fragmentChatMain();
                        FragmentManager fragmentManager=getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragment_chat_main1,chatmessage).commit();
                        break;
                    }
                    case R.id.friend_list:
                    {
                        if (savedInstanceState == null) {
                           fragmentSearchFriend fragment = new fragmentSearchFriend();
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_chat_main1, fragment)
                                    .commit();
                        }

                        break;
                    }
                    case R.id.add_friend:
                    {
                        //Toast.makeText(ChatMainActivity.this,"chatmain",Toast.LENGTH_SHORT).show();
                        if (savedInstanceState == null) {
                            fragmentFriendAddRemove fragment = new fragmentFriendAddRemove();
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_chat_main1, fragment)
                                    .commit();
                        }
//
                        break;
                    }
                }
                return true;
            }
        });
    }
}
