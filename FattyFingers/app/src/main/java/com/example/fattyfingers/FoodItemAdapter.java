package com.example.fattyfingers;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodHolder> {


    private List<FoodItem> foodList;
    private int selectedFoodItemPos = RecyclerView.NO_POSITION;
    private OnAddToCartClickListener addToCartListener;


    public FoodItemAdapter(List<FoodItem> foodList) {
        this.foodList = foodList;
    }

    public interface OnAddToCartClickListener {
        void onAddToCartClick(FoodItem foodItem);
    }

    public void setAddToCartListener(OnAddToCartClickListener listener) {
        this.addToCartListener = listener;
    }


    public void updateList(List<FoodItem> newList) {
        this.foodList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.food_holder, parent, false);

        return new FoodHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, int position) {

        FoodItem currentItem = foodList.get(position);
        holder.txtPrice.setText(String.format("R%d", currentItem.getPrice()));
        holder.txtName.setText(currentItem.getName());
        holder.imgFood.setImageResource(currentItem.getImage());
        holder.ratingBar.setRating(currentItem.getRating());

        holder.imgAddToCart.setOnClickListener(v -> {
            if (addToCartListener != null) {
                addToCartListener.onAddToCartClick(currentItem);
            }
        });

        if (selectedFoodItemPos == position) {
            holder.cardView.animate().scaleX(1.1f);
            holder.cardView.animate().scaleY(1.1f);
        } else {
            holder.txtName.setTextColor(Color.BLACK);
            holder.txtPrice.setTextColor(Color.BLACK);
            holder.cardView.animate().scaleX(1f);
            holder.cardView.animate().scaleY(1f);
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    class FoodHolder extends RecyclerView.ViewHolder {

        ImageView imgFood, imgAddToCart;
        TextView txtPrice, txtName;
        RatingBar ratingBar;
        CardView cardView;
        LinearLayout ll;

        public FoodHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.food_img);
            txtPrice = itemView.findViewById(R.id.food_price);
            txtName = itemView.findViewById(R.id.food_title);
            ratingBar = itemView.findViewById(R.id.rating);
            ll = itemView.findViewById(R.id.ll_background);
            cardView = itemView.findViewById(R.id.food_card);
            imgAddToCart = itemView.findViewById(R.id.add_cart);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedFoodItemPos = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}