package com.svr.atown;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.svr.atown.firebase.FirebaseData;
import com.svr.atown.pojo.UploadProduct;

public class UpdateItemDetatils extends AppCompatActivity implements View.OnClickListener {

    ImageButton firstView,secondView,thirdView;
    EditText productTitle,productDescription, productPrice, productDiscount;
    Button uploadData;
    UploadProduct uploadProduct,updateProduct;
    String first,second,third;
    Fragment progressbar;
    Window window;
    FragmentManager fragmentManager;
    View v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item_detatils);
        init();
//        uploadProduct=(UploadProduct) getArguments().getSerializable("product");
        uploadProduct=(UploadProduct) getIntent().getBundleExtra("productDetailsBundle").getSerializable("product");
        firstView.setOnClickListener(this);
        secondView.setOnClickListener(this);
        thirdView.setOnClickListener(this);
        uploadData.setOnClickListener(this);

    }

    private void init() {
        firstView=findViewById(R.id.firstImage);
        secondView=findViewById(R.id.secondImage);
        thirdView=findViewById(R.id.thirdImage);
        productTitle=findViewById(R.id.productTitle);
        productDescription=findViewById(R.id.description);
        productPrice=findViewById(R.id.price);
        productDiscount=findViewById(R.id.discount);
        uploadData=findViewById(R.id.uploadProduct);
        v=findViewById(R.id.uploadLayout);
        progressbar=new Progressbar();
        window=getWindow();
        fragmentManager=getSupportFragmentManager();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstImage:
                displaySnackbar();
                break;
            case R.id.secondImage:
                displaySnackbar();
                break;
            case R.id.thirdImage:
                displaySnackbar();
                break;
            case R.id.uploadProduct:
                uploadProduct();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(uploadProduct!=null) {
            productTitle.setFocusable(false);
            productTitle.setText(uploadProduct.getTitle());
            first=uploadProduct.getFirstImage();
            Picasso.with(UpdateItemDetatils.this).load(first).networkPolicy(NetworkPolicy.OFFLINE).into(firstView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(UpdateItemDetatils.this).load(first).into(firstView);
                }
            });

            second=uploadProduct.getSecondImage();

            Picasso.with(UpdateItemDetatils.this).load(second).networkPolicy(NetworkPolicy.OFFLINE).into(secondView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(UpdateItemDetatils.this).load(second).into(secondView);
                }
            });

            third=uploadProduct.getThirdImage();

            Picasso.with(UpdateItemDetatils.this).load(third).networkPolicy(NetworkPolicy.OFFLINE).into(thirdView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(UpdateItemDetatils.this).load(third).into(thirdView);
                }
            });

            productDescription.setText(uploadProduct.getDescription());
            productPrice.setText(uploadProduct.getPrice());
            productDiscount.setText(uploadProduct.getDiscount());

        }
    }

    private void uploadProduct() {

        fragmentManager.beginTransaction().replace(R.id.updateItemLayout,progressbar).commitAllowingStateLoss();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final String titleField=productTitle.getText().toString().trim();
        final String descField=productDescription.getText().toString().trim();
        final String priceField=productPrice.getText().toString().trim();
        final String discField=productDiscount.getText().toString().trim();
        productTitle.setFocusable(false);
        productDescription.setFocusable(false);
        productPrice.setFocusable(false);
        productDiscount.setFocusable(false);


        updateProduct=new UploadProduct();


        if(!TextUtils.isEmpty(titleField) && !TextUtils.isEmpty(descField) && !TextUtils.isEmpty(priceField) && !TextUtils.isEmpty(discField)) {

            updateProduct.setTitle(uploadProduct.getTitle());
            updateProduct.setDescription(descField);
            updateProduct.setPrice(priceField);
            updateProduct.setDiscount(discField);
            updateProduct.setFirstImage(uploadProduct.getFirstImage());
            updateProduct.setSecondImage(uploadProduct.getSecondImage());

            updateProduct.setThirdImage(uploadProduct.getThirdImage());
            FirebaseData.getProductReference().child(titleField).setValue(updateProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        fragmentManager.beginTransaction().remove(progressbar).commitAllowingStateLoss();
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(UpdateItemDetatils.this,"Product Sucessfully Updated",Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(UpdateItemDetatils.this,MainActivity.class));

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateItemDetatils.this,"Something Went Wrong : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });


        } else {
            Toast.makeText(UpdateItemDetatils.this,"Else Executed",Toast.LENGTH_LONG).show();
        }
    }

    private void displaySnackbar() {
        Snackbar snackbar= Snackbar.make(v,"To Change Image", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Go To Upload", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment uploadFragment=new Upload();
//                fragmentManager.beginTransaction().replace(R.id.replaceFragment,uploadFragment).commitAllowingStateLoss();
                startActivity(new Intent(UpdateItemDetatils.this,UploadItem.class));
            }
        });
        snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
        snackbar.show();
    }
}