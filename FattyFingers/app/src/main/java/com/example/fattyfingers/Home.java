package com.example.fattyfingers;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {

    RecyclerView recyclerCategory, recyclerFood;
    FoodcategoryAdapter foodCategoryAdapter;
    private DatabaseHelper myDb;
    private String userEmail;

    private EditText searchEditText;

    private List<FoodItem> allFoodItems = new ArrayList<>();
    private FoodItemAdapter foodItemAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);


        myDb = new DatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        if (userEmail == null) {
            userEmail = prefs.getString("USER_EMAIL", null);
        }

        TextView btnCart = findViewById(R.id.btn_cart);
        TextView btnOrders = findViewById(R.id.btn_orders);
        TextView btnLogout = findViewById(R.id.btn_logout);

        btnCart.setOnClickListener(v -> {
            Intent cartIntent = new Intent(Home.this, CartActivity.class);
            startActivity(cartIntent);
        });

        btnOrders.setOnClickListener(v -> {
            if (userEmail != null && !userEmail.isEmpty()) {
                Intent ordersIntent = new Intent(Home.this, OrdersActivity.class);
                // Add this line to pass the user's email
                ordersIntent.putExtra("USER_EMAIL", userEmail);
                startActivity(ordersIntent);
            } else {
                Toast.makeText(Home.this, "Please log in to view your orders.", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(Home.this, MainActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

        btnLogout.setOnClickListener(v -> {
            Intent logoutIntent = new Intent(Home.this, MainActivity.class);
            startActivity(logoutIntent);
            finish();
            Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
        });

        recyclerCategory = findViewById(R.id.recyclerCategories);
        recyclerFood = findViewById(R.id.recyclerView2);

        populateAllFoodItems();

        foodItemAdapter = new FoodItemAdapter(allFoodItems);
        foodItemAdapter.setAddToCartListener(foodItem -> {
            if (userEmail != null && !userEmail.isEmpty()) {
                // This line adds the item to the database.
                myDb.insertCartItem(userEmail, foodItem.getName(), foodItem.getPrice());
                Toast.makeText(Home.this, foodItem.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Home.this, "Please log in to add items to your cart.", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerFood.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL, false));
        recyclerFood.setAdapter(foodItemAdapter);

        searchEditText = findViewById(R.id.editText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFoodItems(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        setCategory();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void filterFoodItems(String query) {
        List<FoodItem> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase(Locale.getDefault());

        for (FoodItem item : allFoodItems) {
            if (item.getName().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery)) {
                filteredList.add(item);
            }
        }

        foodItemAdapter.updateList(filteredList);
    }

    private void populateAllFoodItems() {
        allFoodItems.clear();

        allFoodItems.add(new FoodItem("Shawarma", 3.5f, 20, R.drawable.shawarma));
        allFoodItems.add(new FoodItem("Shawarma Special", 4.5f, 30, R.drawable.shawarma_special));
        allFoodItems.add(new FoodItem("Shawarma Combo", 4.5f, 25, R.drawable.shawarmawithfries));

        allFoodItems.add(new FoodItem("Small Fish&Chips meal", 5.5f, 15, R.drawable.small_fishandchips));
        allFoodItems.add(new FoodItem("Medium Fish&Chips meal", 5.5f, 20, R.drawable.medium_fishandchips));
        allFoodItems.add(new FoodItem("Large Fish&Chips meal", 5.5f, 25, R.drawable.largefishandchips));

        allFoodItems.add(new FoodItem("Kota meal (Small)", 3.5f, 15, R.drawable.small_kota));
        allFoodItems.add(new FoodItem("Kota meal (Medium)", 5.5f, 25, R.drawable.medium));
        allFoodItems.add(new FoodItem("Kota meal (Large)", 5.5f, 35, R.drawable.large_kota));

        allFoodItems.add(new FoodItem("Sandwich meal (Small)", 3.5f, 15, R.drawable.sandwich_small));
        allFoodItems.add(new FoodItem("Sandwich meal (Medium)", 5.5f, 20, R.drawable.sandwich_medium));
        allFoodItems.add(new FoodItem("Sandwich meal (Large)", 5.5f, 30, R.drawable.sandwich_large));

        allFoodItems.add(new FoodItem("Fries (Small)", 5.5f, 12, R.drawable.small_fries));
        allFoodItems.add(new FoodItem("Fries (Medium)", 5.5f, 20, R.drawable.medium_fries));
        allFoodItems.add(new FoodItem("Fries combo", 5.5f, 25, R.drawable.frieswithrussian));

        allFoodItems.add(new FoodItem("Passion Fruit Juice", 5.5f, 10, R.drawable.passionfruit));
        allFoodItems.add(new FoodItem("GrapeFruit Juice", 5.5f, 12, R.drawable.grapefruit));
        allFoodItems.add(new FoodItem("Fanta Orange cooldrink", 5.5f, 12, R.drawable.fanta_orange));
        allFoodItems.add(new FoodItem("Sprite cooldrink", 5.5f, 12, R.drawable.sprite));
        allFoodItems.add(new FoodItem("Strawberry dirty Sprite", 5.5f, 20, R.drawable.strawberrydirtysprite));
        allFoodItems.add(new FoodItem("Coke cooldrink", 5.5f, 15, R.drawable.coke));
        allFoodItems.add(new FoodItem("Lemon Soda drink", 5.5f, 20, R.drawable.lemonsoda));
    }

    public void setCategory() {
        List<FoodCategory> data = new ArrayList<>();
        data.add(new FoodCategory("Shawarma", R.drawable.line_icon_for_shawarma));
        data.add(new FoodCategory("Fish & Chips", R.drawable.fish_and_chips_free_icons_designed_by_good_ware));
        data.add(new FoodCategory("Kota", R.drawable.pr57ug01));
        data.add(new FoodCategory("Sandwich", R.drawable.download_unique_sandwich_vector_icon_for_free));
        data.add(new FoodCategory("Fries", R.drawable.rohit_s));
        data.add(new FoodCategory("Refreshments", R.drawable.good_drink__soda_clipart__drink__icon_png_and_vector_with_transparent_background_for_free_download));

        foodCategoryAdapter = new FoodcategoryAdapter(data, Home.this, position -> {
            List<FoodItem> categoryItems = new ArrayList<>();
            switch (position) {
                case 0:
                    categoryItems.add(new FoodItem("Shawarma", 3.5f, 20, R.drawable.shawarma));
                    categoryItems.add(new FoodItem("Shawarma Special", 4.5f, 30, R.drawable.shawarma_special));
                    categoryItems.add(new FoodItem("Shawarma Combo", 4.5f, 25, R.drawable.shawarmawithfries));
                    break;
                case 1:
                    categoryItems.add(new FoodItem("Small Fish&Chips meal", 5.5f, 15, R.drawable.small_fishandchips));
                    categoryItems.add(new FoodItem("Medium Fish&Chips meal", 5.5f, 20, R.drawable.medium_fishandchips));
                    categoryItems.add(new FoodItem("Large Fish&Chips meal", 5.5f, 25, R.drawable.largefishandchips));
                    break;
                case 2:
                    categoryItems.add(new FoodItem("Kota meal (Small)", 3.5f, 15, R.drawable.small_kota));
                    categoryItems.add(new FoodItem("Kota meal (Medium)", 5.5f, 25, R.drawable.medium));
                    categoryItems.add(new FoodItem("Kota meal (Large)", 5.5f, 35, R.drawable.large_kota));
                    break;
                case 3:
                    categoryItems.add(new FoodItem("Sandwich meal (Small)", 3.5f, 15, R.drawable.sandwich_small));
                    categoryItems.add(new FoodItem("Sandwich meal (Medium)", 5.5f, 20, R.drawable.sandwich_medium));
                    categoryItems.add(new FoodItem("Sandwich meal (Large)", 5.5f, 30, R.drawable.sandwich_large));
                    break;
                case 4:
                    categoryItems.add(new FoodItem("Fries (Small)", 2.5f, 12, R.drawable.small_fries));
                    categoryItems.add(new FoodItem("Fries (Medium)", 5.5f, 20, R.drawable.medium_fries));
                    categoryItems.add(new FoodItem("Fries combo", 5.5f, 25, R.drawable.frieswithrussian));
                    break;
                case 5:
                    categoryItems.add(new FoodItem("Passion Fruit Juice", 5.5f, 10, R.drawable.passionfruit));
                    categoryItems.add(new FoodItem("GrapeFruit Juice", 5.5f, 12, R.drawable.grapefruit));
                    categoryItems.add(new FoodItem("Fanta Orange cooldrink", 5.5f, 12, R.drawable.fanta_orange));
                    categoryItems.add(new FoodItem("Sprite cooldrink", 5.5f, 12, R.drawable.sprite));
                    categoryItems.add(new FoodItem("Strawberry dirty Sprite", 5.5f, 20, R.drawable.strawberrydirtysprite));
                    categoryItems.add(new FoodItem("Coke cooldrink", 5.5f, 15, R.drawable.coke));
                    categoryItems.add(new FoodItem("Lemon Soda drink", 5.5f, 20, R.drawable.lemonsoda));
                    break;
            }
            foodItemAdapter.updateList(categoryItems);
        });
        recyclerCategory.setLayoutManager(new LinearLayoutManager(Home.this, HORIZONTAL, false));
        recyclerCategory.setAdapter(foodCategoryAdapter);
    }
}