package com.example.fattyfingers;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class FoodcategoryAdapter extends RecyclerView.Adapter<FoodcategoryAdapter.CategoryHolder> {

    List<FoodCategory> data;
    Context context;
    private int selectedPosition = 0;
    private onCategoryClickedListener listener;

    public FoodcategoryAdapter(List<FoodCategory> data, Context context, onCategoryClickedListener listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.category_view, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.img.setImageResource(data.get(position).getImage());
        holder.txtName.setText(data.get(position).getName());


        if (position == selectedPosition) {
            holder.txtName.setTextColor(context.getColor(R.color.blue));
            holder.img.setColorFilter(ContextCompat.getColor(context, R.color.blue), PorterDuff.Mode.SRC_IN);
            holder.cardCategory.setOutlineAmbientShadowColor(context.getColor(R.color.blue));
            holder.cardCategory.setOutlineSpotShadowColor(context.getColor(R.color.blue));
            holder.cardCategory.setStrokeWidth(2);
        } else {
            holder.cardCategory.setOutlineAmbientShadowColor(context.getColor(R.color.grey));
            holder.cardCategory.setOutlineSpotShadowColor(context.getColor(R.color.grey));
            holder.cardCategory.setStrokeWidth(0);
            holder.img.setColorFilter(ContextCompat.getColor(context, R.color.grey), PorterDuff.Mode.SRC_IN);
            holder.txtName.setTextColor(context.getColor(R.color.grey));
        }


        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            if (listener != null && selectedPosition != previousPosition) {
                listener.onCategoryClick(selectedPosition);
                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public int getSelectedPosition() {
        return selectedPosition;
    }

    class CategoryHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView img;
        MaterialCardView cardCategory;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.shwama);
            img = itemView.findViewById(R.id.imgShwama);
            cardCategory = itemView.findViewById(R.id.cardView);
        }
    }

    public interface onCategoryClickedListener {
        void onCategoryClick(int position);
    }
}