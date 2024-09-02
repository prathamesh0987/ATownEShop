package com.svr.atown;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.svr.atown.adapter.OrderAdatpter;
import com.svr.atown.db.DBHelper;
import com.svr.atown.db.Tables;
import com.svr.atown.firebase.FirebaseData;
import com.svr.atown.pojo.OrderHistoryPojo;
import com.svr.atown.pojo.OrderedProducts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Cart extends AppCompatActivity {

    RecyclerView orderRecyclerView;
    Button placeOrder;
    ArrayList<OrderedProducts> orderList;
    CheckBox cod;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        dbHelper=new DBHelper(Cart.this);
        orderRecyclerView=findViewById(R.id.orderRecyclerView);
        placeOrder=findViewById(R.id.placeOrder);
//        orderList=getParcelableArrayList("productsList");
        orderList=dbHelper.getCartList();
        cod=findViewById(R.id.cod);
        cod.setChecked(true);

        orderRecyclerView.setHasFixedSize(true);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(Cart.this));
        orderRecyclerView.addItemDecoration(new DividerItemDecoration(Cart.this, LinearLayoutManager.VERTICAL));
        OrderAdatpter orderAdatpter=new OrderAdatpter(Cart.this,orderList);
        orderRecyclerView.setAdapter(orderAdatpter);
        orderAdatpter.notifyDataSetChanged();

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time=new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
                int totalCost=0;
                for(OrderedProducts orderedProducts:orderList) {
                    totalCost+=Integer.parseInt(orderedProducts.getFinalCost());
                }
                String[] splitTime=time.split(" ");
                String dt=splitTime[0].replaceAll("/","_");
                String tm=splitTime[1].replaceAll(":","_");
                String orderNo=dt+tm;

                Cursor cursor=dbHelper.getTableContent(Tables.user.tableName);
                cursor.moveToFirst();
                String mobileno=cursor.getString(cursor.getColumnIndex(Tables.user.mobile));
                String pushTime=new SimpleDateFormat("dd_MM_yyyy_HH_mm").format(new Date());
                FirebaseData.getOrderReference().child(mobileno).child(pushTime).setValue(orderList);

//                if(cod.isChecked()) {
//                    Toast.makeText(Cart.this, "Order PLaced For : "+orderList.size() +" & Transaction Mode is Cash On Delivery", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(Cart.this, "Order PLaced For : "+orderList.size() +" & Transaction Mode is PayTm", Toast.LENGTH_SHORT).show();
//                }

                ContentValues contentValues=new ContentValues();
                contentValues.put(Tables.orderHistory.status,"Pending");
                contentValues.put(Tables.orderHistory.finalCost,String.valueOf(totalCost));
                contentValues.put(Tables.orderHistory.order_time,time);
                contentValues.put(Tables.orderHistory.order_no,orderNo);

                dbHelper.insertValueIntoTable(Tables.orderHistory.tableName,contentValues);

                Toast.makeText(Cart.this,"Order is Placed",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}