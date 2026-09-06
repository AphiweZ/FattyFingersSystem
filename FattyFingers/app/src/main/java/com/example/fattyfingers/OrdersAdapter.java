package com.example.fattyfingers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderHolder> {

    private List<OrderItem> orders;

    public OrdersAdapter(List<OrderItem> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        OrderItem order = orders.get(position);
        holder.txtOrderName.setText(order.getName());
        holder.txtOrderPrice.setText(String.format("R%d", order.getPrice()));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderHolder extends RecyclerView.ViewHolder {
        TextView txtOrderName;
        TextView txtOrderPrice;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderName = itemView.findViewById(R.id.txt_order_item_name);
            txtOrderPrice = itemView.findViewById(R.id.txt_order_item_price);
        }
    }
}