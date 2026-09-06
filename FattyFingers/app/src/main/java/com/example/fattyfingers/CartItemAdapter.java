package com.example.fattyfingers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private Context context;
    private List<OrderItem> cartItems;


    public CartItemAdapter(Context context, List<OrderItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        OrderItem currentItem = cartItems.get(position);

        holder.itemName.setText(currentItem.getName());
        holder.itemPrice.setText(String.format("R%d", currentItem.getPrice()));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
    public void updateList(List<OrderItem> newList) {
        this.cartItems = newList;
        notifyDataSetChanged();
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemPrice;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.txt_cart_item_name);
            itemPrice = itemView.findViewById(R.id.txt_cart_item_price);
        }
    }
}