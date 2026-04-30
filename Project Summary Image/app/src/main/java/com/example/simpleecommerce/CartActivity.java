// made eith nasim
package com.example.simpleecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// Make sure ProductModel is imported here.
import com.example.simpleecommerce.ProductModel;

public class CartActivity extends AppCompatActivity {

    private TextView txtTotalAmount;
    private RecyclerView cartRecyclerView;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        Button btnGoToCheckout = findViewById(R.id.btnGoToCheckout);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ProductModel is used to eliminate type conflicts in the table.
        adapter = new ProductAdapter(this, CartManager.cartList, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProductModel product) {
                // To see details
            }

            @Override
            public void onDeleteClick(int position) {
                if (position >= 0 && position < CartManager.cartList.size()) {
                    CartManager.cartList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, CartManager.cartList.size());
                    updateTotalPrice();
                }
            }
        });

        cartRecyclerView.setAdapter(adapter);
        updateTotalPrice();

        btnGoToCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            startActivity(intent);
        });
    }

    private void updateTotalPrice() {
        double total = 0;
        // Don't make a mistake and write Product p here, write ProductModel p
        if (CartManager.cartList != null) {
            for (ProductModel p : CartManager.cartList) {
                try {
                    String priceStr = p.getPrice().replace("৳", "").replace(",", "").trim();
                    total += Double.parseDouble(priceStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        txtTotalAmount.setText("Total Amount: ৳ " + String.format("%.0f", total));
    }
}