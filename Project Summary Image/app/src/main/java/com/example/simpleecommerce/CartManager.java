package com.example.simpleecommerce;

import java.util.ArrayList;

public class CartManager {
    // Here you need to use ProductModel instead of Product.
    public static ArrayList<ProductModel> cartList = new ArrayList<>();
    private static CartManager instance;

    // Singleton pattern fixed
    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // ProductModel is used here too.
    public void addToCart(ProductModel product) {
        cartList.add(product);
    }
}