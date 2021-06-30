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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button createAccount;
    private EditText inputName,inputPhone,inputPassword,inputSecuritySport,inputSecurityBirth,inputFirmname,inputGst,inputMail,inputAddress;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccount=(Button)findViewById(R.id.create_account);
        inputName=(EditText)findViewById(R.id.input_name);
        inputPhone=(EditText)findViewById(R.id.input_phone_no);
        inputPassword=(EditText)findViewById(R.id.input_password);
        inputSecuritySport=(EditText)findViewById(R.id.security_question_1);
        inputSecurityBirth=(EditText)findViewById(R.id.security_question_2);
        inputMail=(EditText)findViewById(R.id.input_email);
        inputAddress=(EditText)findViewById(R.id.input_address);
        inputFirmname=(EditText)findViewById(R.id.input_firm_name);
        inputGst=(EditText)findViewById(R.id.input_gst);
        loadingBar = new ProgressDialog(this);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String name =inputName.getText().toString();
        String phone =inputPhone.getText().toString();
        String password =inputPassword.getText().toString();
        String securitySport=inputSecuritySport.getText().toString();
        String securityBirth=inputSecurityBirth.getText().toString();
        String address =inputAddress.getText().toString();
        String firmName =inputFirmname.getText().toString();
        String gst=inputGst.getText().toString();
        String email=inputMail.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)||TextUtils.isEmpty(address) || TextUtils.isEmpty(firmName))
        {
            Toast.makeText(this,"Please enter the mandatory details. ",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait while we check the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validateCredentials(name,phone,password,securityBirth,securitySport,address,firmName,gst,email);
        }
    }

    private void validateCredentials(final String name, final String phone, final String password,final String securityBirth,final String securitySport,final String address,final  String firmName,final String gst,final  String email)
    {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(!(snapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap= new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("name",name);
                    userdataMap.put("password",password );
                    userdataMap.put("securityBirth",securityBirth );
                    userdataMap.put("securitySport",securitySport );
                    userdataMap.put("address",address );
                    userdataMap.put("firmName",firmName );
                    userdataMap.put("gst",gst );
                    userdataMap.put("email",email );



                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this,"Congratulations.your account has been created successfully.",Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(this,"Congratulations.your account has been created successfully.",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent =new Intent(RegisterActivity.this,loginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterActivity.this,"Network error!Please try again.",Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(this,"Network error!Please try again.",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this,"This"+phone+"already exists.",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"please try again with another phone number.",Toast.LENGTH_SHORT).show();

                    Intent intent =new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
