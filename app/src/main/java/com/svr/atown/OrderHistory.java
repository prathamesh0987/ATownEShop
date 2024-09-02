package com.svr.atown;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svr.atown.adapter.OrderHistoryAdapter;
import com.svr.atown.db.DBHelper;
import com.svr.atown.pojo.OrderHistoryPojo;
import com.svr.atown.pojo.OrderedProducts;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    DBHelper dbHelper;
    ArrayList<OrderHistoryPojo> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        recyclerView=findViewById(R.id.orderHistoryRecyclerView);
        init();
        if(arrayList.size()>0) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistory.this));
            recyclerView.addItemDecoration(new DividerItemDecoration(OrderHistory.this, LinearLayoutManager.VERTICAL));
            OrderHistoryAdapter orderHistoryAdapter=new OrderHistoryAdapter(arrayList);
            recyclerView.setAdapter(orderHistoryAdapter);
            orderHistoryAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(OrderHistory.this,"No order placed yet",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void init() {
        dbHelper=new DBHelper(OrderHistory.this);
        arrayList=dbHelper.getOrderHistoryList();
    }

}