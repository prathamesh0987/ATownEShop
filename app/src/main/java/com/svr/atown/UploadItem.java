package com.svr.atown;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.svr.atown.firebase.FirebaseData;
import com.svr.atown.pojo.UploadProduct;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class UploadItem extends AppCompatActivity implements View.OnClickListener {

    ImageButton firstView,secondView,thirdView;
    EditText productTitle,productDescription, productPrice, productDiscount;
    Button uploadData;
//    Spinner units;
    private final int GALLERY_REQUEST=1;
    private static Uri firstImage=null,secondImage=null,thirdImage=null,firstImageUri=null,secondImageUri=null,thirdImageUri=null;
    static String requestFrom=null;
    //String firstImageURI,secondImageURI,thirdImageURI;
    Fragment progressbar;
    Window window;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);
        init();
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
        progressbar=new Progressbar();
        window=getWindow();
//        units=findViewById(R.id.units);
        fragmentManager=getSupportFragmentManager();

        //register listener
        firstView.setOnClickListener(this);
        secondView.setOnClickListener(this);
        thirdView.setOnClickListener(this);
        uploadData.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestFrom) {
            case "first":
                if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK) {
                    firstImage=data.getData();
                    CropImage.activity(firstImage)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(this);
                }
                if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result=CropImage.getActivityResult(data);
                    if(resultCode==RESULT_OK) {
                        firstImageUri=result.getUri();
                        firstView.setImageURI(firstImageUri);
                    } else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error=result.getError();
                        Log.e("Error",error.getMessage());
                    }
                }
                break;
            case "second":
                if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK) {
                    secondImage=data.getData();
                    CropImage.activity(secondImage)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(this);
                }

                if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result=CropImage.getActivityResult(data);
                    if(resultCode==RESULT_OK) {
                        secondImageUri=result.getUri();
                        secondView.setImageURI(secondImageUri);
                    } else if(resultCode== CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error=result.getError();
                        Log.e("Error",error.getMessage());
                    }
                }
                break;
            case "third":
                if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK) {
                    thirdImage=data.getData();
                    CropImage.activity(thirdImage)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(this);
                }

                if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result=CropImage.getActivityResult(data);
                    if(resultCode==RESULT_OK) {
                        thirdImageUri=result.getUri();
                        thirdView.setImageURI(thirdImageUri);
                    } else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error=result.getError();
                        Log.e("Error",error.getMessage());
                    }
                }
                break;
        }
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.firstImage:
                Intent firstIntent=new Intent(Intent.ACTION_GET_CONTENT);
                firstIntent.setType("image/*");
                requestFrom="first";
                startActivityForResult(firstIntent,GALLERY_REQUEST);
                break;
            case R.id.secondImage:
                Intent secondIntent=new Intent(Intent.ACTION_GET_CONTENT);
                secondIntent.setType("image/*");
                requestFrom="second";
                startActivityForResult(secondIntent,GALLERY_REQUEST);

                break;
            case R.id.thirdImage:
                Intent thirdIntent=new Intent(Intent.ACTION_GET_CONTENT);
                thirdIntent.setType("image/*");
                requestFrom="third";
                startActivityForResult(thirdIntent,GALLERY_REQUEST);
                break;
            case R.id.uploadProduct:
                uploadProduct();
                break;
        }

    }

    private void uploadProduct() {
        final String titleField=productTitle.getText().toString().trim();
        final String descField=productDescription.getText().toString().trim();
        final String priceField=productPrice.getText().toString().trim();
        final String discField=productDiscount.getText().toString().trim();
        productTitle.setFocusable(false);
        productDescription.setFocusable(false);
        productPrice.setFocusable(false);
        productDiscount.setFocusable(false);
        final UploadProduct uploadProduct=new UploadProduct();
        fragmentManager.beginTransaction().replace(R.id.uploadLayout,progressbar).commitAllowingStateLoss();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(!TextUtils.isEmpty(titleField) && !TextUtils.isEmpty(descField) && !TextUtils.isEmpty(priceField) && !TextUtils.isEmpty(discField)
                && firstImage!=null && secondImage!=null && thirdImage!=null) {
            final StorageReference filePath= FirebaseData.getStorage().child("product").child(titleField);
            //window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            filePath.child("first").putFile(firstImageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //getFragmentManager(). beginTransaction().replace(R.id.uploadLayout,progressbar).commitNowAllowingStateLoss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    //getFragmentManager().beginTransaction().remove(progressbar).commitNowAllowingStateLoss();
                    Toast.makeText(UploadItem.this,"First Image Successfully Uploaded",Toast.LENGTH_LONG).show();
                    filePath.child("first").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> firstTask) {
                            if(firstTask.isSuccessful()) {
                                uploadProduct.setFirstImage(firstTask.getResult().toString());
                                filePath.child("second").putFile(secondImageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(UploadItem.this,"Second Image Successfully Uploaded",Toast.LENGTH_LONG).show();
                                        filePath.child("second").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> secondTask) {
                                                uploadProduct.setSecondImage(secondTask.getResult().toString());
                                                filePath.child("third").putFile(thirdImageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                                    }
                                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        Toast.makeText(UploadItem.this,"Third Image Successfully Uploaded",Toast.LENGTH_LONG).show();
                                                        filePath.child("third").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Uri> thirdTask) {
                                                                if(thirdTask.isSuccessful()) {
                                                                    uploadProduct.setThirdImage(thirdTask.getResult().toString());
                                                                    uploadProduct.setTitle(titleField);
                                                                    uploadProduct.setDescription(descField);
                                                                    uploadProduct.setPrice(priceField);
                                                                    uploadProduct.setDiscount(discField);
                                                                    FirebaseData.getProductReference().child(titleField).setValue(uploadProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()) {
                                                                                fragmentManager.beginTransaction().remove(progressbar).commitAllowingStateLoss();
                                                                                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                                                //new StartActivity().execute();
                                                                                Toast.makeText(UploadItem.this,"Product Sucessfully Uploaded",Toast.LENGTH_LONG).show();
                                                                                finish();
//                                                                                startActivity(new Intent(context,MainActivity.class));
                                                                            }
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(UploadItem.this,"Something Went Wrong : "+e.getMessage(),Toast.LENGTH_LONG).show();
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
//                    hideFrontPage();
//                 getFragmentManager().beginTransaction().replace(R.id.uploadLayout,new Event()).commit();
                }
            });
        } else {
            fragmentManager.beginTransaction().remove(progressbar).commitAllowingStateLoss();
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(UploadItem.this,"Else Executed",Toast.LENGTH_LONG).show();
        }

    }

}