 package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerce.Prevalent.Prevalent;
import com.rey.material.widget.CheckBox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

 public class loginActivity extends AppCompatActivity {

     private Button login;
     private EditText inputPhone,inputPassword;
     private ProgressDialog loadingBar;
     private CheckBox rememberMe;
     private TextView adminLink, notAdminLink,forgotPassword;
     String parentDname="Users";
     private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=(Button)findViewById(R.id.login_btn2);
        inputPhone=(EditText)findViewById(R.id.login_phone_no);
        inputPassword=(EditText)findViewById(R.id.login_password);
        loadingBar = new ProgressDialog(this);
        rememberMe =(CheckBox)findViewById(R.id.remember_me);
        adminLink=(TextView)findViewById(R.id.I_am_admin);
        notAdminLink=(TextView)findViewById(R.id.I_am_not_admin);
        forgotPassword=(TextView)findViewById(R.id.forgot_password);
        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();

            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDname="Admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setText("Login ");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDname="Users";
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(loginActivity.this,ResetPasswordActivity.class);
                i.putExtra("uid",inputPhone.getText().toString());
                i.putExtra("parentDname",parentDname);
                startActivity(i);
            }
        });
    }

     private void loginUser()
     {
         String phone =inputPhone.getText().toString();
         String password =inputPassword.getText().toString();

         if(TextUtils.isEmpty(phone) || TextUtils.isEmpty(password))
         {
             Toast.makeText(this,"Please enter the details. ",Toast.LENGTH_SHORT).show();
         }
         else
         {
             loadingBar.setTitle("Logging in");
             loadingBar.setMessage("Please wait while we check the credentials.");
             loadingBar.setCanceledOnTouchOutside(false);
             loadingBar.show();

             AllowLogin(phone,password);
         }
     }

     private void AllowLogin(final String phone, final String password)
     {
         if(rememberMe.isChecked())
         {
             Paper.book().write(Prevalent.UserPhoneKey,phone);
             Paper.book().write(Prevalent.UserPasswordKey,password);
             Paper.book().write(Prevalent.Dname,parentDname);
         }
         Toast.makeText(loginActivity.this, "came", Toast.LENGTH_SHORT).show();
         final DatabaseReference RootRef;
         RootRef= FirebaseDatabase.getInstance().getReference();


         RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {

                 if(snapshot.child(parentDname).child(phone).exists()) {

                     Users userdata = snapshot.child(parentDname).child(phone).getValue(Users.class);
                     if (userdata.getPhone().equals(phone)) {
                         if (userdata.getPassword().equals(password)) {

                             if (parentDname.equals("Users")) {
                                 Toast.makeText(loginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                 loadingBar.dismiss();
                                 Prevalent.currentUser=userdata;

                                Intent i = new Intent(loginActivity.this, homeActivity.class);
                                 startActivity(i);
                             } else if (parentDname.equals("Admins")) {
                                 Toast.makeText(loginActivity.this, "Admin,Logged in successfully", Toast.LENGTH_SHORT).show();
                                 loadingBar.dismiss();

                                 Intent i = new Intent(loginActivity.this, BrandActivity.class);
                                 i.putExtra("user","admins");
                                 startActivity(i);
                             }


                         } else {
                             Toast.makeText(loginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                             loadingBar.dismiss();
                         }
                     }
                     else
                     {
                         Toast.makeText(loginActivity.this,"This account does not exist. Try with some other phone number.", Toast.LENGTH_SHORT).show();
                         loadingBar.dismiss();
                     }


                 }


             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 throw error.toException();
                 //Log.d("ghg", "Test = " + "5");

             }
         });
     }
 }
