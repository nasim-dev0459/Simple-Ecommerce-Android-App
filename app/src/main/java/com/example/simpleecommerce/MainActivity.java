// Made with Nasim
package com.example.simpleecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProductAdapter adapter;
    private ArrayList<ProductModel> productList;
    private ViewPager2 viewPagerSlider;
    private Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing views
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        SearchView searchView = findViewById(R.id.searchView);
        viewPagerSlider = findViewById(R.id.viewPagerSlider);
        SwitchCompat themeSwitch = findViewById(R.id.themeSwitch);

        // Dark mode preference management
        SharedPreferences sharedPreferences = getSharedPreferences("ThemeSave", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean isNightMode = sharedPreferences.getBoolean("nightMode", false);

        if (isNightMode) {
            if (themeSwitch != null) themeSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        // Theme switcher listener
        if (themeSwitch != null) {
            themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("nightMode", true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("nightMode", false);
                }
                editor.apply();
            });
        }

        setupImageSlider();

        // RecyclerView setup
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();

        // Adding 3 products with local drawable IDs stored as String
        productList.add(new ProductModel("HP Pavilion Laptop", "65000", String.valueOf(R.drawable.laptop), "Core i5, 8GB RAM, 512GB SSD."));
        productList.add(new ProductModel("Samsung Galaxy S23", "95000", String.valueOf(R.drawable.mobile), "8GB RAM, 256GB ROM, 50MP Camera."));
        productList.add(new ProductModel("Logitech Mouse", "1500", String.valueOf(R.drawable.mouse), "Wireless Optical Mouse, 10m range."));

        // Adapter setup with click listeners
        adapter = new ProductAdapter(this, productList, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProductModel product) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {}
        });

        recyclerView.setAdapter(adapter);

        // Search feature implementation
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) { return false; }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filter(newText);
                    return true;
                }
            });
        }
    }

    // Setup for the image slider
    private void setupImageSlider() {
        List<Integer> sliderImages = new ArrayList<>();
        sliderImages.add(R.drawable.laptop);
        sliderImages.add(R.drawable.mobile);
        sliderImages.add(R.drawable.mouse);

        SliderAdapter sliderAdapter = new SliderAdapter(sliderImages);
        viewPagerSlider.setAdapter(sliderAdapter);

        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                if (viewPagerSlider != null && !sliderImages.isEmpty()) {
                    int nextItem = (viewPagerSlider.getCurrentItem() + 1) % sliderImages.size();
                    viewPagerSlider.setCurrentItem(nextItem);
                    sliderHandler.postDelayed(this, 3000);
                }
            }
        };
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    // Filter logic for search
    private void filter(String text) {
        ArrayList<ProductModel> filteredList = new ArrayList<>();
        for (ProductModel item : productList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (adapter != null) {
            adapter.filterList(filteredList);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sliderRunnable != null) {
            sliderHandler.postDelayed(sliderRunnable, 3000);
        }
    }
}