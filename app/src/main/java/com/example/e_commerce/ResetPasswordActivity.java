package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_commerce.Model.Users;
import com.example.e_commerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText sq1,sq2,newPassword;
    private Button reset;
    private ProgressDialog loadingBar;
    private String parentDname,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        parentDname=getIntent().getExtras().get("parentDname").toString();
        uid=getIntent().getExtras().get("uid").toString();

        sq1=(EditText)findViewById(R.id.SQ_1);
        sq2=(EditText)findViewById(R.id.SQ_2);
        newPassword=(EditText)findViewById(R.id.new_password);
        loadingBar = new ProgressDialog(this);

        reset=(Button)findViewById(R.id.Reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetUser();

            }
        });

    }

    private void resetUser()
    {
        String sq1Ans=sq1.getText().toString();
        String sq2Ans=sq2.getText().toString();
        String newPass=newPassword.getText().toString();

        if(TextUtils.isEmpty(sq2Ans) || TextUtils.isEmpty(sq1Ans)||TextUtils.isEmpty(newPass))
        {
            Toast.makeText(this,"Please enter the details. ",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Resetting password");
            loadingBar.setMessage("Please wait while we check the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ResetPassword(sq1Ans,sq2Ans,newPass);
        }
    }
    private void ResetPassword(final String sq1Ans, final String sq2Ans,final String newPass)
    {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDname).child(uid).exists())
                {

                   if ( snapshot.child(parentDname).child(uid).child("securitySport").getValue().equals(sq1Ans))
                    {
                        if ( snapshot.child(parentDname).child(uid).child("securityBirth").getValue().equals(sq2Ans))
                        {
                            RootRef.child(parentDname).child(uid).child("password").setValue(newPass);
                            Users userData=snapshot.child(parentDname).child(uid).getValue(Users.class);

                            if (parentDname.equals("Users"))
                            {
                                Prevalent.currentUser= userData;
                            }

                            Toast.makeText(ResetPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent i = new Intent(ResetPasswordActivity.this, loginActivity.class);
                            startActivity(i);


                        }
                        else
                        {
                            Toast.makeText(ResetPasswordActivity.this, "Incorrect security answers", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this,"This account does not exist. Try with some other phone number.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
