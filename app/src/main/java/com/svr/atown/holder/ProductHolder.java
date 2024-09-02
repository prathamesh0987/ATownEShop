package com.svr.atown.holder;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.svr.atown.R;

public class ProductHolder extends RecyclerView.ViewHolder {

    private TextView productTitle,productPrice,actualPrice,productDiscount;
    private ImageView productImage;

    public ProductHolder(View itemView) {
        super(itemView);
        productTitle=itemView.findViewById(R.id.prodTitle);
        productPrice=itemView.findViewById(R.id.prodPrice);
        actualPrice=itemView.findViewById(R.id.prodActualPrice);
        productDiscount=itemView.findViewById(R.id.prodDiscount);
        productImage=itemView.findViewById(R.id.prodImage);
    }


    public void setProductTitle(String title) {
        productTitle.setText(title);
    }

    public void setProductPrice(String price) {
        productPrice.setText(price);
        productPrice.setPaintFlags(productPrice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        productPrice.setTypeface(productPrice.getTypeface(), Typeface.BOLD);
    }

    public void setActualPrice(String actPrice) {
        actualPrice.setText(actPrice);
        actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }

    public void setProductDiscount(String productDiscount) {
        this.productDiscount.setText(productDiscount+"% OFF");
    }

    public void setProductImage(final String image,final Context context) {
        Picasso.with(context).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(productImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(context).load(image).into(productImage);
            }
        });
    }
}
