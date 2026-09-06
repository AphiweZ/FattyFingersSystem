package com.example.fattyfingers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase.db";

    private static final int DATABASE_VERSION = 3;


    private static final String TABLE_NAME_USERS = "users";
    private static final String COL_ID = "ID";
    private static final String COL_USERNAME = "USERNAME";
    private static final String COL_PASSWORD = "PASSWORD";


    private static final String TABLE_NAME_CART = "cart";
    private static final String COL_CART_ID = "CART_ID";
    private static final String COL_CART_USER = "CART_USER";
    private static final String COL_CART_ITEM = "CART_ITEM";
    private static final String COL_CART_PRICE = "CART_PRICE";


    private static final String TABLE_NAME_ORDERS = "orders";
    private static final String COL_ORDER_ID = "ORDER_ID";
    private static final String COL_ORDER_USER = "ORDER_USER";
    private static final String COL_ORDER_ITEM = "ORDER_ITEM";
    private static final String COL_ORDER_PRICE = "ORDER_PRICE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_NAME_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USERNAME + " TEXT," +
                COL_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_NAME_CART + "(" +
                COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_CART_USER + " TEXT," +
                COL_CART_ITEM + " TEXT," +
                COL_CART_PRICE + " INTEGER" + ")";
        db.execSQL(CREATE_CART_TABLE);


        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_NAME_ORDERS + "(" +
                COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_ORDER_USER + " TEXT," +
                COL_ORDER_ITEM + " TEXT," +
                COL_ORDER_PRICE + " INTEGER" + ")";
        db.execSQL(CREATE_ORDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop all old tables to recreate them with the new structure
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ORDERS);
        onCreate(db);
    }

    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_NAME_USERS, null, contentValues);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + COL_USERNAME + " =? AND " + COL_PASSWORD + " =?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    public void insertCartItem(String username, String itemName, int price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CART_USER, username);
        contentValues.put(COL_CART_ITEM, itemName);
        contentValues.put(COL_CART_PRICE, price);
        db.insert(TABLE_NAME_CART, null, contentValues);
    }


    public List<OrderItem> getCartItems(String username) {
        List<OrderItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_CART + " WHERE " + COL_CART_USER + " =?", new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String itemName = cursor.getString(cursor.getColumnIndex(COL_CART_ITEM));
                @SuppressLint("Range") int price = cursor.getInt(cursor.getColumnIndex(COL_CART_PRICE));
                cartItems.add(new OrderItem(itemName, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems;
    }


    public int deleteCartItems(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_CART, COL_CART_USER + " =?", new String[]{username});
    }


    public List<OrderItem> getPaidOrders(String username) {
        List<OrderItem> paidOrders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_ORDERS + " WHERE " + COL_ORDER_USER + " =?", new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String itemName = cursor.getString(cursor.getColumnIndex(COL_ORDER_ITEM));
                @SuppressLint("Range") int price = cursor.getInt(cursor.getColumnIndex(COL_ORDER_PRICE));
                paidOrders.add(new OrderItem(itemName, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return paidOrders;
    }


    public void insertPaidOrders(String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<OrderItem> cartItems = getCartItems(username);


        for (OrderItem item : cartItems) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_ORDER_USER, username);
            contentValues.put(COL_ORDER_ITEM, item.getName());
            contentValues.put(COL_ORDER_PRICE, item.getPrice());
            db.insert(TABLE_NAME_ORDERS, null, contentValues);
        }
    }
}