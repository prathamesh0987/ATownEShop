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
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.svr.atown.firebase.FirebaseData;
import com.svr.atown.pojo.Advertisement;
import com.svr.atown.pojo.UploadProduct;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AdvertisementBanner extends AppCompatActivity implements View.OnClickListener {


    private final int GALLERY_REQUEST=1;
    private static Uri firstImage=null,secondImage=null,thirdImage=null,fourthImage=null,fifthImage=null,
            firstImageUri=null,secondImageUri=null,thirdImageUri=null,fourthImageUri=null, fifthImageUri=null;
    static String requestFrom=null;
    ImageButton firstAdv, secondAdv, thirdAdv, fourthAdv, fifthAdv;
    Button banner;
    Fragment progressbar;
    FragmentManager fragmentManager;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_banner);
        progressbar=new Progressbar();
        fragmentManager=getSupportFragmentManager();
        window=getWindow();
        firstAdv=findViewById(R.id.firstAdv);
        secondAdv=findViewById(R.id.secondAdv);
        thirdAdv=findViewById(R.id.thirdAdv);
        fourthAdv=findViewById(R.id.fourthAdv);
        fifthAdv=findViewById(R.id.fifthAdv);
        banner=findViewById(R.id.banner);

        firstAdv.setOnClickListener(this);
        secondAdv.setOnClickListener(this);
        thirdAdv.setOnClickListener(this);
        fourthAdv.setOnClickListener(this);
        fifthAdv.setOnClickListener(this);
        banner.setOnClickListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();


    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.firstAdv:
                Intent firstIntent=new Intent(Intent.ACTION_GET_CONTENT);
                firstIntent.setType("image/*");
                requestFrom="first";
                startActivityForResult(firstIntent,GALLERY_REQUEST);
                break;
            case R.id.secondAdv:
                Intent secondIntent=new Intent(Intent.ACTION_GET_CONTENT);
                secondIntent.setType("image/*");
                requestFrom="second";
                startActivityForResult(secondIntent,GALLERY_REQUEST);
                break;
            case R.id.thirdAdv:
                Intent thirdIntent=new Intent(Intent.ACTION_GET_CONTENT);
                thirdIntent.setType("image/*");
                requestFrom="third";
                startActivityForResult(thirdIntent,GALLERY_REQUEST);
                break;
            case R.id.fourthAdv:
                Intent fourthIntent=new Intent(Intent.ACTION_GET_CONTENT);
                fourthIntent.setType("image/*");
                requestFrom="fourth";
                startActivityForResult(fourthIntent,GALLERY_REQUEST);
                break;
            case R.id.fifthAdv:
                Intent fifthIntent=new Intent(Intent.ACTION_GET_CONTENT);
                fifthIntent.setType("image/*");
                requestFrom="fifth";
                startActivityForResult(fifthIntent,GALLERY_REQUEST);
                break;
            case R.id.banner:
                uploadProduct();
                break;
        }

    }

    private void uploadProduct() {
        final Advertisement advertisement=new Advertisement();
        fragmentManager.beginTransaction().replace(R.id.uploadLayout,progressbar).commitAllowingStateLoss();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(firstImage!=null && secondImage!=null && thirdImage!=null) {
            final StorageReference filePath=FirebaseData.getAdvStorage();
            //window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            filePath.child("first").putFile(firstImageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AdvertisementBanner.this,"First Image Successfully Uploaded",Toast.LENGTH_LONG).show();
                    filePath.child("first").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> firstTask) {
                            if(firstTask.isSuccessful()) {
                                advertisement.setFirstAdv(firstTask.getResult().toString());
                                filePath.child("second").putFile(secondImageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(AdvertisementBanner.this,"Second Image Successfully Uploaded",Toast.LENGTH_LONG).show();
                                        filePath.child("second").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> secondTask) {
                                                if(secondTask.isSuccessful()) {
                                                    advertisement.setSecondAdv(secondTask.getResult().toString());
                                                    filePath.child("third").putFile(thirdImageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                                                        }
                                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            Toast.makeText(AdvertisementBanner.this,"Third Image Successfully Uploaded",Toast.LENGTH_LONG).show();
                                                            filePath.child("third").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Uri> thirdTask) {
                                                                    if(thirdTask.isSuccessful()) {
                                                                        advertisement.setThirdAdv(thirdTask.getResult().toString());
                                                                        filePath.child("fourth").putFile(fourthImageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                                            @Override
                                                                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                                                                            }
                                                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                Toast.makeText(AdvertisementBanner.this,"Fourth Image Successfully Uploaded",Toast.LENGTH_LONG).show();
                                                                                filePath.child("fourth").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Uri> fourthTask) {
                                                                                        if(fourthTask.isSuccessful()) {
                                                                                            advertisement.setFourthAdv(fourthTask.getResult().toString());
                                                                                            filePath.child("fifth").putFile(fifthImageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                                                                @Override
                                                                                                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                                                                                                }
                                                                                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                @Override
                                                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                                    Toast.makeText(AdvertisementBanner.this,"Fifth Image Successfully Uploaded",Toast.LENGTH_LONG).show();
                                                                                                    filePath.child("fifth").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Uri> fifthTask) {
                                                                                                            if(fifthTask.isSuccessful()) {
                                                                                                                advertisement.setFifthAdv(fifthTask.getResult().toString());
                                                                                                                FirebaseData.getAdvertisementReference().setValue(advertisement).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                                        if (task.isSuccessful()) {
                                                                                                                            fragmentManager.beginTransaction().remove(progressbar).commitAllowingStateLoss();
                                                                                                                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                                                                                            //new StartActivity().execute();
                                                                                                                            Toast.makeText(AdvertisementBanner.this, "Advertise Successfully uploaded", Toast.LENGTH_LONG).show();
                                                                                                                            finish();
//                                                                                                                          startActivity(new Intent(context,MainActivity.class));
                                                                                                                        }
                                                                                                                    }
                                                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                                                    @Override
                                                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                                                        Toast.makeText(AdvertisementBanner.this, "Something Went Wrong : " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                                                                                    }
                                                                                                                });
                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            });


        } else {
            fragmentManager.beginTransaction().remove(progressbar).commitAllowingStateLoss();
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(AdvertisementBanner.this,"Else Executed",Toast.LENGTH_LONG).show();
        }
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
                            .setAspectRatio(3,2)
                            .start(this);
                }
                if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result=CropImage.getActivityResult(data);
                    if(resultCode==RESULT_OK) {
                        firstImageUri=result.getUri();
                        firstAdv.setImageURI(firstImageUri);
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
                            .setAspectRatio(3,2)
                            .start(this);
                }

                if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result=CropImage.getActivityResult(data);
                    if(resultCode==RESULT_OK) {
                        secondImageUri=result.getUri();
                        secondAdv.setImageURI(secondImageUri);
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
                            .setAspectRatio(3,2)
                            .start(this);
                }

                if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result=CropImage.getActivityResult(data);
                    if(resultCode==RESULT_OK) {
                        thirdImageUri=result.getUri();
                        thirdAdv.setImageURI(thirdImageUri);
                    } else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error=result.getError();
                        Log.e("Error",error.getMessage());
                    }
                }
                break;
            case "fourth":
                if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK) {
                    fourthImage=data.getData();
                    CropImage.activity(fourthImage)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(3,2)
                            .start(this);
                }

                if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result=CropImage.getActivityResult(data);
                    if(resultCode==RESULT_OK) {
                        fourthImageUri=result.getUri();
                        fourthAdv.setImageURI(fourthImageUri);
                    } else if(resultCode== CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error=result.getError();
                        Log.e("Error",error.getMessage());
                    }
                }
                break;
            case "fifth":
                if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK) {
                    fifthImage=data.getData();
                    CropImage.activity(fifthImage)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(3,2)
                            .start(this);
                }

                if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result=CropImage.getActivityResult(data);
                    if(resultCode==RESULT_OK) {
                        fifthImageUri=result.getUri();
                        fifthAdv.setImageURI(fifthImageUri);
                    } else if(resultCode== CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error=result.getError();
                        Log.e("Error",error.getMessage());
                    }
                }
                break;
        }
    }
}