package com.example.e_commerce.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.e_commerce.BrandActivity;
import com.example.e_commerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {

    private String brandName,description,name,price, saveCurrentDate,saveCurrentTime,productRandomKey,downloadImageUrl,categoryName;
    private ImageView addImage;
    private EditText productName,productDescription,productPrice;
    private Button addProduct;
    private static final int galleryPicker=1;
    private Uri imageUri;
    private StorageReference productImagesRef;
    private DatabaseReference productsRef;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

       brandName= getIntent().getExtras().get("brand").toString();
       categoryName= getIntent().getExtras().get("category").toString();

        Toast.makeText(AdminActivity.this,brandName,Toast.LENGTH_SHORT).show();

        productImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images").child(brandName);

        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        addImage=(ImageView)findViewById(R.id.add_image);
        productName=(EditText)findViewById(R.id.product_name);
        productDescription=(EditText)findViewById(R.id.product_description);
        productPrice=(EditText)findViewById(R.id.product_price);
        addProduct=(Button)findViewById(R.id.add_product);
        loadingBar = new ProgressDialog(this);

        productPrice.setText("nil");

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                openGallery();
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });


    }
    private void openGallery() {
        Intent i= new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i,galleryPicker);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==galleryPicker && resultCode==RESULT_OK && data!=null)
        {
            imageUri=data.getData();
            addImage.setImageURI(imageUri);
        }
    }
    private void validateProductData() {

        name=productName.getText().toString();
        description=productDescription.getText().toString();
        price=productPrice.getText().toString();


       if(imageUri==null || TextUtils.isEmpty(name)||TextUtils.isEmpty(description)|| TextUtils.isEmpty(price))
        {
            Toast.makeText(AdminActivity.this,"Details are missing.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            storeProductInformation();
            //Toast.makeText(AdminActivity.this,"Details are full.",Toast.LENGTH_SHORT).show();
        }
    }

    private void storeProductInformation() {

        loadingBar.setTitle("Adding new product");
        loadingBar.setMessage("Please wait while we are adding the new product .");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar =Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("dd MMM yyyy");

        saveCurrentDate= currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");

        saveCurrentTime= currentTime.format(calendar.getTime());

        productRandomKey=saveCurrentDate+saveCurrentTime;

        final StorageReference filepath=productImagesRef.child(imageUri.getLastPathSegment()+productRandomKey+".jpg");
        final UploadTask uploadTask= filepath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(AdminActivity.this,"error: "+message,Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminActivity.this,"Image uploaded successfully.",Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl=task.getResult().toString();
                            Toast.makeText(AdminActivity.this,"product URL received.",Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();
                        }

                    }
                });
            }
        });
        
    }

    private void saveProductInfoToDatabase() {
        HashMap<String,Object> productMap= new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("name",name);
        productMap.put("description",description);
        productMap.put("price",price);
        productMap.put("image",downloadImageUrl);
        productMap.put("brand",brandName);
        productMap.put("category",categoryName);

        productsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    loadingBar.dismiss();
                    Toast.makeText(AdminActivity.this,"Product added successfully.",Toast.LENGTH_SHORT).show();

                    Intent i =new Intent(AdminActivity.this, BrandActivity.class);
                    startActivity(i);
                }
                else
                {
                    loadingBar.dismiss();
                    String message=task.getException().toString();
                    Toast.makeText(AdminActivity.this,"error: "+message,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
