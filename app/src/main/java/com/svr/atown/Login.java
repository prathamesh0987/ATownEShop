package com.svr.atown;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.svr.atown.db.DBHelper;
import com.svr.atown.db.Tables;
import com.svr.atown.firebase.FirebaseData;
import com.svr.atown.pojo.SignupPojo;

import java.util.Iterator;

public class Login extends AppCompatActivity {

    EditText username,password;
    Button login;
    Button signup;
    TextView forgotPass;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    Window window;
    Fragment progressbar;
    FragmentManager fragmentManager;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setTitle(R.string.loginTitle);
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null) {
                    final String user=firebaseAuth.getCurrentUser().getEmail();
                    DatabaseReference userReference= FirebaseData.getUserList();
                    userReference.keepSynced(true);
                    fragmentManager.beginTransaction().replace(R.id.frameLayout,progressbar).commitAllowingStateLoss();
                    window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    userReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> iterator=dataSnapshot.getChildren().iterator();
                            while (iterator.hasNext()) {
                                DataSnapshot data=iterator.next();
                                SignupPojo signup=data.getValue(SignupPojo.class);
                                if(signup.getMail().equals(user)) {
                                    boolean flag=dbHelper.deleteAll(Tables.user.tableName);
//                                    int no=SugarRecord.deleteAll(LoggedUser.class);
                                    if(flag) {
                                        String username=signup.getName();
                                        String mail=signup.getMail();
                                        String mobile=signup.getMobile();
                                        String status=signup.getStatus();
                                        String house_no=signup.getHouse_no();
                                        String society=signup.getSociety();
                                        String area=signup.getArea();
                                        String landmark=signup.getLandmark();
                                        String pincode=signup.getPinCode();
                                        Cursor cursor = dbHelper.getTableContent(Tables.user.tableName);
                                        Log.i("After Delete Count :",String.valueOf(cursor.getCount()));
                                        ContentValues values = new ContentValues();
                                        if(cursor.getCount() == 0){
                                            values.put(Tables.user.name, username);
                                            values.put(Tables.user.mail, mail);
                                            values.put(Tables.user.house_no,house_no);
                                            values.put(Tables.user.society,society);
                                            values.put(Tables.user.area,area);
                                            values.put(Tables.user.landmark,landmark);
                                            values.put(Tables.user.pinCode,pincode);
                                            values.put(Tables.user.mobile,mobile);
                                            values.put(Tables.user.status,status);
                                            dbHelper.insertValueIntoTable(Tables.user.tableName, values);
                                        }
                                        cursor.close();
//                                    LoggedUser loggedUser=new LoggedUser();
//                                    loggedUser.setMail_id(user);
//                                    loggedUser.setStatus("user");
//                                    loggedUser.setUsername(username);
//                                    LoggedUser.save(loggedUser);
                                        fragmentManager.beginTransaction().remove(progressbar).commitAllowingStateLoss();
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        startActivity(new Intent(Login.this,MainActivity.class));
                                        finish();
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        };
    }

    void init() {
        username=findViewById(R.id.username);
        password=findViewById(R.id.pass);
        signup=findViewById(R.id.signup);
        login=findViewById(R.id.login);
        //forgotPass=findViewById(R.id.forgot_password);
        firebaseAuth=FirebaseData.getFirebaseAuth();
        window=getWindow();
        fragmentManager=getSupportFragmentManager();
        progressbar=new Progressbar();
        dbHelper=new DBHelper(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Cursor cursor=dbHelper.getTableContent(Tables.user.tableName);
        if(cursor.moveToFirst()) {
            firebaseAuth.addAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Intent intent=new Intent(getApplicationContext(),Signup.class);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        final Intent loginIntent=new Intent(Login.this,MainActivity.class);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frameLayout,progressbar).commitAllowingStateLoss();
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                final String user=username.getText().toString().trim();
                String pass=password.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            DatabaseReference userReference=FirebaseData.getUserList();
                            userReference.keepSynced(true);
                            userReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //Iterator<DataSnapshot> iterator=dataSnapshot.getChildren().iterator();
                                    /*while (iterator.hasNext()) {
                                        DataSnapshot data=iterator.next();

                                  }*/

                                    for(DataSnapshot ds:dataSnapshot.getChildren()) {
                                        SignupPojo signup=ds.getValue(SignupPojo.class);
                                        if(signup.getMail().equals(user)) {
                                            boolean flag=dbHelper.deleteAll(Tables.user.tableName);
                                            Log.e("Flag : ",String.valueOf(flag));
                                            if(flag) {
                                                String username=signup.getName();
                                                String mail=signup.getMail();
                                                String mobile=signup.getMobile();
                                                String status=signup.getStatus();
                                                String house_no=signup.getHouse_no();
                                                String society=signup.getSociety();
                                                String area=signup.getArea();
                                                String landmark=signup.getLandmark();
                                                String pincode=signup.getPinCode();
                                                Cursor cursor = dbHelper.getTableContent(Tables.user.tableName);
                                                Log.i("AfterDeleteCountonLogin",String.valueOf(cursor.getCount()));
                                                ContentValues values = new ContentValues();
                                                if(cursor.getCount() == 0){
                                                    values.put(Tables.user.name, username);
                                                    values.put(Tables.user.mail, mail);
                                                    values.put(Tables.user.house_no,house_no);
                                                    values.put(Tables.user.society,society);
                                                    values.put(Tables.user.area,area);
                                                    values.put(Tables.user.landmark,landmark);
                                                    values.put(Tables.user.pinCode,pincode);
                                                    values.put(Tables.user.mobile,mobile);
                                                    values.put(Tables.user.status,status);
                                                    dbHelper.insertValueIntoTable(Tables.user.tableName, values);
                                                }
                                                cursor.close();
                                                fragmentManager.beginTransaction().remove(progressbar).commitAllowingStateLoss();
                                                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                startActivity(loginIntent);
                                                finish();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            /*String username=getIntent().getStringExtra("user");*/
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        fragmentManager.beginTransaction().remove(progressbar).commit();
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(),"Login Failed Due To : "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


//        forgotPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String userId=username.getText().toString().trim();
//                if(!TextUtils.isEmpty(userId)) {
//                    FirebaseAuth firebaseAuth=FirebaseData.getFirebaseAuth();
//                    firebaseAuth.sendPasswordResetEmail(userId).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()) {
//                                Snackbar resetLink=Snackbar.make(findViewById(android.R.id.content),Html.fromHtml("<font color=\"#00b33c\">Reset Link Send To Your Mail Id</font>"),Snackbar.LENGTH_LONG);
//                                resetLink.show();
//                            }
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Snackbar resetLink=Snackbar.make(findViewById(android.R.id.content),Html.fromHtml("<font color=\"#ff3333\">You Are Not Register Yet</font>"),Snackbar.LENGTH_LONG);
//                            resetLink.show();
//                        }
//                    });
//                } else {
//                    Snackbar forgotPassword=Snackbar.make(findViewById(android.R.id.content),Html.fromHtml("<font color=\"#ff3333\">Please Enter Your Username First</font>"),Snackbar.LENGTH_LONG);
//                    forgotPassword.show();
//                }
//            }
//        });
    }
}