package com.example.fattyfingers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrders;
    private OrdersAdapter ordersAdapter;
    private DatabaseHelper myDb;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        recyclerViewOrders = findViewById(R.id.recycler_orders);
        myDb = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER_EMAIL")) {
            userEmail = intent.getStringExtra("USER_EMAIL");

            if (userEmail != null && !userEmail.isEmpty()) {

                loadOrders(userEmail);
            } else {
                Toast.makeText(this, "User email not found. Please log in again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User email not passed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadOrders(String username) {
        List<OrderItem> orders = myDb.getPaidOrders(username);

        if (orders.isEmpty()) {
            Toast.makeText(this, "You have no past orders.", Toast.LENGTH_SHORT).show();
        } else {
            ordersAdapter = new OrdersAdapter(orders);
            recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewOrders.setAdapter(ordersAdapter);
        }
    }
}