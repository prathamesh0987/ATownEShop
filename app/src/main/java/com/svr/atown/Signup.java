package com.svr.atown;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.svr.atown.db.DBHelper;
import com.svr.atown.db.Tables;
import com.svr.atown.firebase.FirebaseData;
import com.svr.atown.pojo.SignupPojo;

public class Signup extends AppCompatActivity {

    EditText name,mobile,email,password,confirmPassword,signup_house_no_edit_text,signup_society_edit_text,signup_area_edit_text,
            signup_landmark_edit_text,signup_pinCode_edit_text;
    Button signup;
    Context context;
    FirebaseAuth firebaseAuth;
    DatabaseReference userReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName=name.getText().toString().trim();
                final String mail_id=email.getText().toString().trim();
                final String mobile_no=mobile.getText().toString().trim();
                final String house_no=signup_house_no_edit_text.getText().toString().trim();
                final String society=signup_society_edit_text.getText().toString().trim();
                final String area=signup_area_edit_text.getText().toString().trim();
                final String landmark=signup_landmark_edit_text.getText().toString().trim();
                final String pincode=signup_pinCode_edit_text.getText().toString().trim();
                final String pass=password.getText().toString().trim();
                String confPassword=confirmPassword.getText().toString().trim();

                if(!(TextUtils.isEmpty(userName)) && !(TextUtils.isEmpty(mail_id)) && !(TextUtils.isEmpty(mobile_no))
                && !(TextUtils.isEmpty(house_no)) && !(TextUtils.isEmpty(society)) && !(TextUtils.isEmpty(area))
                        && !(TextUtils.isEmpty(landmark)) && !(TextUtils.isEmpty(pincode)) && !(TextUtils.isEmpty(pass)) && !(TextUtils.isEmpty(confPassword))) {
                    if(pass.equals(confPassword)) {
                        firebaseAuth.createUserWithEmailAndPassword(mail_id,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    final SignupPojo user=new SignupPojo();
                                    user.setHouse_no(house_no);
                                    if(mail_id.equals("durgesh@gmail.com")) {
                                        user.setStatus("admin");
                                    } else {
                                        user.setStatus("user");
                                    }
                                    user.setSociety(society);
                                    user.setArea(area);
                                    user.setLandmark(landmark);
                                    user.setPinCode(pincode);
                                    user.setMobile(mobile_no);
                                    user.setName(userName);
                                    user.setMail(mail_id);
                                    user.setPassword(pass);
                                    userReference.child(mobile_no).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                try {
                                                    DBHelper dbHelper=new DBHelper(getApplicationContext());
                                                    dbHelper.getWritableDatabase();
                                                    Cursor cursor = dbHelper.getTableContent(Tables.user.tableName);
                                                    cursor.moveToFirst();
                                                    ContentValues values = new ContentValues();
                                                    if(cursor.getCount() <= 0){
                                                        values.put(Tables.user.name, userName);
                                                        values.put(Tables.user.mail, mail_id);
                                                        values.put(Tables.user.house_no,house_no);
                                                        values.put(Tables.user.society,society);
                                                        values.put(Tables.user.area,area);
                                                        values.put(Tables.user.landmark,landmark);
                                                        values.put(Tables.user.pinCode,pincode);
                                                        values.put(Tables.user.password,pass);
                                                        values.put(Tables.user.mobile,mobile_no);
                                                        if(mail_id.equals("durgesh@gmail.com")) {
                                                            values.put(Tables.user.status,"admin");
                                                        } else {
                                                            values.put(Tables.user.status,"user");
                                                        }
                                                        dbHelper.insertValueIntoTable(Tables.user.tableName, values);
                                                    }
                                                    cursor.close();
                                                    Toast.makeText(context,"User Created",Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(Signup.this,Login.class));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context,"Failed due to : "+e.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,"Failed due to : "+e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(context,"Password mismatch",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context,"All Fields Are Required",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void init() {
        name=findViewById(R.id.name);
        email=findViewById(R.id.mail);
        mobile=findViewById(R.id.mobile);
        signup_house_no_edit_text=findViewById(R.id.signup_house_no_edit_text);
        signup_society_edit_text=findViewById(R.id.signup_society_edit_text);
        signup_area_edit_text=findViewById(R.id.signup_area_edit_text);
        signup_landmark_edit_text=findViewById(R.id.signup_landmark_edit_text);
        signup_pinCode_edit_text=findViewById(R.id.signup_pinCode_edit_text);
        //address=findViewById(R.id.address);
        password=findViewById(R.id.password);
        confirmPassword=findViewById(R.id.confirmPassword);
        signup=findViewById(R.id.register);
        context=getApplicationContext();
        firebaseAuth= FirebaseData.getFirebaseAuth();
        userReference=FirebaseData.getUserList();
    }
}