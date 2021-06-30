package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.net.URI;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private TextView closeBtn,updateBtn,changeprofilePic;
    private EditText txtphoneNo,txtname,txtaddress;

    private Uri imageUri;
    private String myUrl="";
    private StorageReference storageProfilePicRef;
    private String checker="";
    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePicRef= FirebaseStorage.getInstance().getReference().child("Profile Images");
        profileImage=(CircleImageView)findViewById(R.id.profile_image_settings);
        closeBtn =(TextView)findViewById(R.id.close_settings);
        updateBtn =(TextView)findViewById(R.id.update_settings);
        changeprofilePic =(TextView)findViewById(R.id.change_profile_image);

        txtphoneNo =(EditText) findViewById(R.id.settings_phone);
        txtname =(EditText) findViewById(R.id.settings_name);
        txtaddress=(EditText) findViewById(R.id.settings_address);
        
        userInfoDisplay(profileImage,txtname,txtphoneNo,txtaddress);


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("Clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });

        changeprofilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                checker="Clicked";
                CropImage.activity(imageUri).setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });

    }

    private void updateOnlyUserInfo()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String,Object> userMap= new HashMap<>();
        userMap.put("name",txtname.getText().toString());
        userMap.put("AlternatePhone",txtphoneNo.getText().toString());
        userMap.put("address",txtaddress.getText().toString());
        userMap.put("image",myUrl);
        ref.child(Prevalent.currentUser.getPhone()).updateChildren(userMap);

        Toast.makeText(SettingsActivity.this,"Profile updated successfully",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SettingsActivity.this,homeActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK&& data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();

            profileImage.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(SettingsActivity.this,"Error,try again.",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }

    }

    private void userInfoSaved()
    {
        if(TextUtils.isEmpty(txtname.getText().toString())|| TextUtils.isEmpty(txtphoneNo.getText().toString())||TextUtils.isEmpty(txtaddress.getText().toString()))
        {
            Toast.makeText(SettingsActivity.this,"Incomplete information.",Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("Clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage()
    {
        final ProgressDialog progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("Update profile");
        progressDialog.setMessage("Please wait while we are updating your account information.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!=null)
        {

            final StorageReference fileRef = storageProfilePicRef.child(Prevalent.currentUser.getPhone()+".jpg");
            uploadTask =fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl= task.getResult();
                        myUrl=downloadUrl.toString();

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String,Object> userMap= new HashMap<>();
                        userMap.put("name",txtname.getText().toString());
                        userMap.put("phoneOrder",txtphoneNo.getText().toString());
                        userMap.put("address",txtaddress.getText().toString());
                        userMap.put("image",myUrl);
                        ref.child(Prevalent.currentUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();

                        Toast.makeText(SettingsActivity.this,"Profile updated successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SettingsActivity.this,homeActivity.class));
                        finish();

                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this,"Error,try again.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(SettingsActivity.this,"Image is not selected.",Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView profileImage, EditText name, EditText phoneNo, EditText address)
    {
        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference().child(Prevalent.currentUser.getPhone());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    if(snapshot.child("image").exists())
                    {
                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String password = snapshot.child("password").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImage);
                        txtname.setText(name);
                        txtaddress.setText(address);
                        txtphoneNo.setText(phone);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
