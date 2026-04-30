// made with nasim
package com.example.simpleecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<ProductModel> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProductModel product);
        void onDeleteClick(int position);
    }

    public ProductAdapter(Context context, List<ProductModel> productList, OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel product = productList.get(position);
        holder.name.setText(product.getName());
        holder.price.setText("৳ " + product.getPrice());

        // Loading image from drawable image ID
        try {
            int imageResId = Integer.parseInt(product.getImageUrl());
            holder.image.setImageResource(imageResId);
        } catch (Exception e) {
            holder.image.setImageResource(R.drawable.laptop);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(product));
        if (holder.imgDelete != null) {
            holder.imgDelete.setOnClickListener(v -> listener.onDeleteClick(position));
        }
    }

    @Override
    public int getItemCount() { return productList.size(); }

    public void filterList(ArrayList<ProductModel> filteredList) {
        this.productList = filteredList;
        notifyDataSetChanged();
    }
// nasim
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView image, imgDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtProductName);
            price = itemView.findViewById(R.id.txtProductPrice);
            image = itemView.findViewById(R.id.imgProduct);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}