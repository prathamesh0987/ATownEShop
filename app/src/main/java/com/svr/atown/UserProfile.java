package com.svr.atown;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.svr.atown.db.DBHelper;
import com.svr.atown.db.Tables;

public class UserProfile extends AppCompatActivity {

    EditText profile_name_edit_text,profile_house_no_edit_text,profile_society_edit_text,profile_area_edit_text,
            profile_landmark_edit_text,profile_pinCode_edit_text,profile_email_edit_text,profile_contact_no_edit_text,
            profile_password_edit_text;
    Button saveProfile;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        init();
        initializeFields();
    }



    private void init() {
        profile_name_edit_text=findViewById(R.id.profile_name_edit_text);
        profile_house_no_edit_text=findViewById(R.id.profile_house_no_edit_text);
        profile_society_edit_text=findViewById(R.id.profile_society_edit_text);
        profile_area_edit_text=findViewById(R.id.profile_area_edit_text);
        profile_landmark_edit_text=findViewById(R.id.profile_landmark_edit_text);
        profile_pinCode_edit_text=findViewById(R.id.profile_pinCode_edit_text);
        profile_email_edit_text=findViewById(R.id.profile_email_edit_text);
        profile_contact_no_edit_text=findViewById(R.id.profile_contact_no_edit_text);
        profile_password_edit_text=findViewById(R.id.profile_password_edit_text);
        saveProfile=findViewById(R.id.saveProfile);
        dbHelper=new DBHelper(UserProfile.this);
    }

    private void initializeFields() {
        Cursor cursor=dbHelper.getTableContent(Tables.user.tableName);
        cursor.moveToFirst();
        profile_area_edit_text.setText(cursor.getString(cursor.getColumnIndex(Tables.user.area)));
        profile_contact_no_edit_text.setText(cursor.getString(cursor.getColumnIndex(Tables.user.mobile)));
        profile_email_edit_text.setText(cursor.getString(cursor.getColumnIndex(Tables.user.mail)));
        profile_house_no_edit_text.setText(cursor.getString(cursor.getColumnIndex(Tables.user.house_no)));
        profile_landmark_edit_text.setText(cursor.getString(cursor.getColumnIndex(Tables.user.landmark)));
        profile_name_edit_text.setText(cursor.getString(cursor.getColumnIndex(Tables.user.name)));
        profile_society_edit_text.setText(cursor.getString(cursor.getColumnIndex(Tables.user.society)));
        profile_pinCode_edit_text.setText(cursor.getString(cursor.getColumnIndex(Tables.user.pinCode)));
        profile_password_edit_text.setText(cursor.getString(cursor.getColumnIndex(Tables.user.password)));
    }
}