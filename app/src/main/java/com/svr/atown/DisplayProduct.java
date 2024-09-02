package com.svr.atown;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.svr.atown.adapter.ProductSilderAdapter;
import com.svr.atown.db.DBHelper;
import com.svr.atown.db.Tables;
import com.svr.atown.pojo.OrderedProducts;
import com.svr.atown.pojo.UploadProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DisplayProduct extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    TabLayout tabLayout;
    List<String> backgroud;
    UploadProduct uploadProduct;
    Context context;
    DBHelper dbHelper;
    FragmentActivity activity;
    TextView title,description,discCost,actualCost,discount;
    Button addCart,buyNow;
    LinearLayout quantity;
    CheckBox cod;
    //SelectQuantity selectQuantity;
    TextView qty;
    String kgs;
    float price;
    Button plus,minus;
    static int i=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_product);
        uploadProduct=(UploadProduct) getIntent().getBundleExtra("uploadProductBundle").getSerializable("uploadProduct");
        init();
        setQuantity(i);
        cod.setChecked(true);
    }

    private void init() {
        viewPager=findViewById(R.id.productViewPager);
        tabLayout=findViewById(R.id.productTabLayout);
        backgroud=new ArrayList<>();
        backgroud.add(uploadProduct.getFirstImage());
        backgroud.add(uploadProduct.getSecondImage());
        backgroud.add(uploadProduct.getThirdImage());
        title=findViewById(R.id.showName);
        description=findViewById(R.id.showDescription);
        discCost=findViewById(R.id.finalCost);
        actualCost=findViewById(R.id.actualCost);
        discount=findViewById(R.id.discount);
        addCart=findViewById(R.id.addToCart);
        quantity=findViewById(R.id.quantity);
        buyNow=findViewById(R.id.buyNow);
        qty=findViewById(R.id.user_requirement);
        cod=findViewById(R.id.cashOnDelivery);
        plus=findViewById(R.id.plus);
        minus=findViewById(R.id.minus);
    }

    @Override
    public void onStart() {
        super.onStart();
        addCart.setOnClickListener(this);
        quantity.setOnClickListener(this);
        buyNow.setOnClickListener(this);
        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.setAdapter(new ProductSilderAdapter(DisplayProduct.this,backgroud));
        tabLayout.setupWithViewPager(viewPager,true);
//        Timer timer=new Timer();
//        timer.scheduleAtFixedRate(new SliderTimer(),3000,6000);
        title.setText(uploadProduct.getTitle());
        description.setText(uploadProduct.getDescription());
        int productsActualPrice=Integer.parseInt(uploadProduct.getPrice());
        float discountOnProduct=Integer.parseInt(uploadProduct.getDiscount());
        float percentValue=(discountOnProduct/100)*productsActualPrice;
        price=productsActualPrice-percentValue;
        discCost.setText(String.valueOf(price));
        discCost.setTypeface(discCost.getTypeface(), Typeface.BOLD);
        discCost.setPaintFlags(discCost.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        actualCost.setText(String.valueOf(Float.valueOf(productsActualPrice)));
        discount.setText(uploadProduct.getDiscount()+"% OFF");
        actualCost.setTypeface(actualCost.getTypeface(), Typeface.BOLD_ITALIC);
        actualCost.setPaintFlags(actualCost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        /*ArrayAdapter aa = new ArrayAdapter(context,android.R.layout.simple_spinner_item,kgsList);
        qty.setAdapter(aa);
        qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kgs=kgsList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addToCart:
                addToCart();
                break;
            case R.id.buyNow:
                if(cod.isChecked()) {
                    Toast.makeText(DisplayProduct.this,"You use Cash On Delivery Mode to buy this Product",Toast.LENGTH_LONG).show();

                } else {
                    kgs=qty.getText().toString();
                    OrderedProducts orderedProducts=new OrderedProducts();
                    orderedProducts.setQuantity(String.valueOf(kgs));
                    orderedProducts.setName(uploadProduct.getTitle());
                    orderedProducts.setFinalCost(String.valueOf(price));
                    orderedProducts.setImage(uploadProduct.getFirstImage());
                    /*Intent intent=new Intent(context,Checksum.class);
                    intent.putExtra("orderedProduct",orderedProducts);
                    startActivity(intent);*/
                }
                break;
            case R.id.plus:
                i+=1;
                setQuantity(i);
                break;
            case R.id.minus:
                if(i<=1) {
                    Toast.makeText(DisplayProduct.this, "Not Allow To Decrease", Toast.LENGTH_LONG).show();
                } else {
                    i-=1;
                }
                setQuantity(i);
                break;
        }
    }

//    private class SliderTimer extends TimerTask {
//
//        @Override
//        public void run() {
////            if(activity!=null) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(viewPager.getCurrentItem()<(backgroud.size()-1)) {
//                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
//                        } else {
//                            viewPager.setCurrentItem(0);
//                        }
//                    }
//                });
////            }
//        }
//    }

    private void addToCart() {
//        OrderedProducts orderedProducts=new OrderedProducts();
//        orderedProducts.setQuantity(kgs);
//        orderedProducts.setName(uploadProduct.getTitle());
//        orderedProducts.setFinalCost(String.valueOf(price));
//        orderedProducts.setImage(uploadProduct.getFirstImage());
//        OrderedProducts.save(orderedProducts);
        Log.i("inside : ","addToCart()");
        dbHelper=new DBHelper(DisplayProduct.this);
        ContentValues contentValues=new ContentValues();
        contentValues.put(Tables.cart.name,uploadProduct.getTitle());
        contentValues.put(Tables.cart.image,uploadProduct.getFirstImage());
        contentValues.put(Tables.cart.finalCost,uploadProduct.getPrice());
        contentValues.put(Tables.cart.quantity,qty.getText().toString());
        dbHelper.insertValueIntoTable(Tables.cart.tableName,contentValues);
        dbHelper.close();
//        OrderedProducts.save(orderedProducts);
        Toast.makeText(DisplayProduct.this,"Added to cart",Toast.LENGTH_LONG).show();

    }

    public  void setQuantity(int i) {
        qty.setText(String.valueOf(i));
    }

}