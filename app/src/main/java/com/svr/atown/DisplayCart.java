//package com.svr.atown;
//
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.Toast;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.svr.atown.adapter.OrderAdatpter;
//
//import java.util.ArrayList;
//
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class DisplayCart extends Fragment {
//
//    RecyclerView orderRecyclerView;
//    Button placeOrder;
//    Context context;
//    ArrayList orderList;
//    CheckBox cod;
//    public DisplayCart() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view=inflater.inflate(R.layout.fragment_display_cart, container, false);
//        orderRecyclerView=view.findViewById(R.id.orderRecyclerView);
//        placeOrder=view.findViewById(R.id.placeOrder);
//        context=view.getContext();
//        orderList=getArguments().getParcelableArrayList("productsList");
//        cod=view.findViewById(R.id.cod);
//        return view;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        orderRecyclerView.setHasFixedSize(true);
//        orderRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//        orderRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
//        OrderAdatpter orderAdatpter=new OrderAdatpter(context,orderList);
//        orderRecyclerView.setAdapter(orderAdatpter);
//        orderAdatpter.notifyDataSetChanged();
//
//        placeOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(cod.isChecked()) {
//                    Toast.makeText(context, "Order PLaced For : "+orderList.size() +" & Transaction Mode is Cash On Delivery", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "Order PLaced For : "+orderList.size() +" & Transaction Mode is PayTm", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//}
