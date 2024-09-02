package com.svr.atown.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.svr.atown.pojo.OrderHistoryPojo;
import com.svr.atown.pojo.OrderedProducts;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "user_config_db.db";
    private static int DB_VERSION = 1;
    private Context mContext;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_QUERY = "";
        CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS `user` ( " +
                "`name` TEXT DEFAULT 1, " +
                "`mail` TEXT DEFAULT 1, " +
                "`mobile` TEXT DEFAULT 1, " +
                "`house_no` TEXT DEFAULT 1, " +
                "`society` TEXT DEFAULT 1, " +
                "`area` TEXT DEFAULT 1, " +
                "`landmark` TEXT DEFAULT 1, " +
                "`pinCode` TEXT DEFAULT 1, " +
                "`password` TEXT DEFAULT 1, " +
                "`status` TEXT, 'user');";
        db.execSQL(CREATE_TABLE_QUERY);


        CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS `cart` ( " +
                "`name` TEXT DEFAULT 1, " +
                "`image` TEXT DEFAULT 1, " +
                "`finalCost` TEXT DEFAULT 1, " +
                "`quantity` TEXT DEFAULT 1);";
        db.execSQL(CREATE_TABLE_QUERY);

        CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS `order_history` ( " +
                "`order_no` TEXT DEFAULT 1, " +
                "`status` TEXT DEFAULT 1, " +
                "`finalCost` TEXT DEFAULT 1, " +
                "`order_time` TEXT DEFAULT 1);";
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertValueIntoTable(String tableName, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.insert(tableName, null, contentValues);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return true;
    }

    public boolean updateTable(String tableName, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.update(tableName, contentValues, null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return true;
    }

    public Cursor getTableContent(String tableName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        String QUERY = "SELECT * FROM "+tableName+";";
        cursor = db.rawQuery(QUERY, null, null);
        cursor.moveToFirst();
        db.close();
        return cursor;
    }

    public boolean deleteAll(String tableName) {
        boolean flag=false;
        SQLiteDatabase db=this.getWritableDatabase();
        try{
            db.beginTransaction();
            String delete="delete from "+tableName;
            Log.i("Delete String : ",delete);
            db.execSQL(delete);
            flag=true;

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return flag;
    }

    public ArrayList<OrderedProducts> getCartList() {
        ArrayList<OrderedProducts> cartList=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        String QUERY = "SELECT * FROM "+Tables.cart.tableName+";";
        cursor = db.rawQuery(QUERY, null, null);
        if(cursor.moveToFirst()) {
            while (!cursor.isLast()) {
                OrderedProducts orderedProducts=new OrderedProducts();
                String nm=cursor.getString(cursor.getColumnIndex(Tables.cart.name));
                String img=cursor.getString(cursor.getColumnIndex(Tables.cart.image));
                String finalCST=cursor.getString(cursor.getColumnIndex(Tables.cart.finalCost));
                String qty=cursor.getString(cursor.getColumnIndex(Tables.cart.quantity));
                orderedProducts.setName(nm);
                orderedProducts.setFinalCost(finalCST);
                orderedProducts.setImage(img);
                orderedProducts.setQuantity(qty);
                cartList.add(orderedProducts);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return cartList;
    }

    public boolean deleteSpecific(String tableName, String clause) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(tableName, Tables.cart.name + "=?", new String[]{clause}) > 0;
    }


    public ArrayList<OrderHistoryPojo> getOrderHistoryList() {
        ArrayList<OrderHistoryPojo> orderHistoryArrayList=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        String QUERY = "SELECT * FROM "+Tables.orderHistory.tableName+";";
        cursor = db.rawQuery(QUERY, null, null);
        if(cursor.moveToFirst()) {
            while (!cursor.isLast()) {
                OrderHistoryPojo orderHistoryPojo=new OrderHistoryPojo();
                String no=cursor.getString(cursor.getColumnIndex(Tables.orderHistory.order_no));
                String status=cursor.getString(cursor.getColumnIndex(Tables.orderHistory.status));
                String finalCST=cursor.getString(cursor.getColumnIndex(Tables.orderHistory.finalCost));
                String orderTime=cursor.getString(cursor.getColumnIndex(Tables.orderHistory.order_time));
                orderHistoryPojo.setOrderNo(no);
                orderHistoryPojo.setOrderCost(finalCST);
                orderHistoryPojo.setOrderStat(status);
                orderHistoryPojo.setOrderTime(orderTime);
                orderHistoryArrayList.add(orderHistoryPojo);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return orderHistoryArrayList;
    }
}

