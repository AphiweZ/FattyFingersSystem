package com.example.fattyfingers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private CartItemAdapter cartItemAdapter;
    private TextView txtTotal;
    private Button btnPay;

    private List<OrderItem> cartItems;
    private DatabaseHelper myDb;
    private String userEmail;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recycler_cart_items);
        txtTotal = findViewById(R.id.txt_total_price);
        btnPay = findViewById(R.id.btn_pay);
        myDb = new DatabaseHelper(this);


        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        userEmail = prefs.getString("USER_EMAIL", null);

        if (userEmail == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            txtTotal.setText("Total: R0");
            return;
        }


        cartItems = myDb.getCartItems(userEmail);

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
            txtTotal.setText("Total: R0");
        } else {
            setupRecyclerView();
            calculateTotal();
        }

        btnPay.setOnClickListener(v -> {
            if (cartItems != null && !cartItems.isEmpty()) {

                myDb.insertPaidOrders(userEmail);
                myDb.deleteCartItems(userEmail);

                Toast.makeText(this, "Payment successful! Your order has been placed.", Toast.LENGTH_LONG).show();


                cartItems.clear();
                cartItemAdapter.notifyDataSetChanged();
                txtTotal.setText("Total: R0");
            } else {
                Toast.makeText(this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {

        cartItemAdapter = new CartItemAdapter(this, cartItems);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartItemAdapter);
    }

    private void calculateTotal() {
        int total = 0;
        for (OrderItem item : cartItems) {
            total += item.getPrice();
        }
        txtTotal.setText(String.format("Total: R%d", total));
    }
}