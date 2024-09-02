package com.svr.atown.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.svr.atown.R;
import com.svr.atown.pojo.OrderHistoryPojo;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryHolder> {

    ArrayList<OrderHistoryPojo> arrayList;

    public OrderHistoryAdapter(ArrayList<OrderHistoryPojo> arrayList) {
        this.arrayList=arrayList;
    }

    public class OrderHistoryHolder extends RecyclerView.ViewHolder {

        TextView orderNumber,orderFinalPrice,orderStatus,orderDate;

        public OrderHistoryHolder(@NonNull View itemView) {
            super(itemView);
            orderNumber=itemView.findViewById(R.id.orderNumber);
            orderFinalPrice=itemView.findViewById(R.id.orderFinalPrice);
            orderStatus=itemView.findViewById(R.id.orderStatus);
            orderDate=itemView.findViewById(R.id.orderDate);
        }
    }

    @NonNull
    @Override
    public OrderHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row,parent,false);
        return new OrderHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryHolder holder, int position) {
        OrderHistoryPojo orderHistoryPojo=arrayList.get(position);
        holder.orderDate.setText(orderHistoryPojo.getOrderTime());
        holder.orderFinalPrice.setText(orderHistoryPojo.getOrderCost());
        holder.orderStatus.setText(orderHistoryPojo.getOrderStat());
        holder.orderNumber.setText(orderHistoryPojo.getOrderNo());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
