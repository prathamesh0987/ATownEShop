package com.svr.atown;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.svr.atown.firebase.FirebaseData;
import com.svr.atown.holder.ProductHolder;
import com.svr.atown.listener.RecyclerTouchListener;
import com.svr.atown.pojo.UploadProduct;

import java.util.ArrayList;

public class UpdateItem extends AppCompatActivity {

    RecyclerView updateRecyclerView;
    DatabaseReference productReference;
    ArrayList<UploadProduct> uploadProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);
        init();
        updateRecyclerView.setHasFixedSize(true);
        updateRecyclerView.setLayoutManager(new GridLayoutManager(UpdateItem.this,3));
    }

    private void init() {
        updateRecyclerView=findViewById(R.id.updateRecyclerView);
        productReference= FirebaseData.getProductReference();
        uploadProduct=new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseRecyclerOptions<UploadProduct> firebaseRecyclerOptions=new FirebaseRecyclerOptions.
                Builder<UploadProduct>()
                .setQuery(productReference,UploadProduct.class)
                .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<UploadProduct, ProductHolder>(firebaseRecyclerOptions) {


            @NonNull
            @Override
            public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.product_row, parent, false);
                return new ProductHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull UploadProduct model) {
                holder.setProductTitle(model.getTitle());
                holder.setProductImage(model.getFirstImage(),UpdateItem.this);
                int productsActualPrice=Integer.valueOf(model.getPrice());
                float discountOnProduct=Integer.valueOf(model.getDiscount());
                float percentValue=(discountOnProduct/100)*productsActualPrice;
                float price=productsActualPrice-percentValue;
                holder.setActualPrice(String.valueOf(Float.valueOf(model.getPrice())));
                holder.setProductPrice(String.valueOf(price));
                holder.setProductDiscount(model.getDiscount());
                uploadProduct.add(model);
            }
        };

        updateRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

        updateRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(UpdateItem.this,
                updateRecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if(uploadProduct!=null) {
                            Fragment fragment=new UpdateDetails();
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("product", uploadProduct.get(position));
                            Intent intent=new Intent(UpdateItem.this,UpdateItemDetatils.class);
                            intent.putExtra("productDetailsBundle",bundle);
                            startActivity(intent);
//                            fragment.setArguments(bundle);
//                            getSupportFragmentManager().beginTransaction().replace(R.id.replaceFragment,fragment).commitAllowingStateLoss();
                        }
                    }

                    @Override
                    public void onHold(View view, int position) {

                    }
                }));
    }
}