package com.example.simpleecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // 1. Initializing views
        Button btnPlus = findViewById(R.id.btnPlus);
        Button btnMinus = findViewById(R.id.btnMinus);
        TextView txtQuantity = findViewById(R.id.txtQuantity);

        TextView nameTV = findViewById(R.id.detailName);
        TextView priceTV = findViewById(R.id.detailPrice);
        TextView descTV = findViewById(R.id.detailDesc);
        ImageView detailImage = findViewById(R.id.detailImage);
        Button btnAddToCart = findViewById(R.id.btnAddToCart);

        // 2. Quantity counter management
        final int[] quantity = {1};

        btnPlus.setOnClickListener(v -> {
            quantity[0]++;
            txtQuantity.setText(String.valueOf(quantity[0]));
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                txtQuantity.setText(String.valueOf(quantity[0]));
            }
        });

        // 3. Receiving ProductModel object from Intent
        ProductModel selectedProduct = (ProductModel) getIntent().getSerializableExtra("product");

        if (selectedProduct != null) {
            // 4. Setting product details to views
            nameTV.setText(selectedProduct.getName());
            priceTV.setText("৳ " + selectedProduct.getPrice());
            descTV.setText(selectedProduct.getDescription());

            // FIX: Load local drawable image instead of using Glide for URL
            try {
                int imageResId = Integer.parseInt(selectedProduct.getImageUrl());
                detailImage.setImageResource(imageResId);
            } catch (Exception e) {
                // Placeholder image if resource ID is not found
                detailImage.setImageResource(R.drawable.laptop);
            }
        }

        // 5. Add to Cart functionality
        btnAddToCart.setOnClickListener(v -> {
            if (selectedProduct != null) {
                CartManager.getInstance().addToCart(selectedProduct);
                Toast.makeText(this, "Added to Cart!", Toast.LENGTH_SHORT).show();

                // Navigating to Cart screen
                Intent intent = new Intent(DetailsActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }
}