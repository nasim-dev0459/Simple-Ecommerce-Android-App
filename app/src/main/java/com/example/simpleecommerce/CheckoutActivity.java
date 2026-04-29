package com.example.simpleecommerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        EditText etName = findViewById(R.id.etName);
        EditText etAddress = findViewById(R.id.etAddress);
        Button btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        btnConfirmOrder.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String address = etAddress.getText().toString();

            if (name.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please provide your name and address.", Toast.LENGTH_SHORT).show();
            } else {
                showSuccessDialog(name);
            }
        });
    }

    // This dialog will appear if the order is successful.
    private void showSuccessDialog(String userName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Order was successful!");
        builder.setMessage("thank you " + userName + "!\nWe have received your order. You will be contacted shortly.");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setCancelable(false); // Don't close when clicking outside

        builder.setPositiveButton("all right", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //1. Empty the cart
                CartManager.cartList.clear();

                // 2. Return to the home page
                Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}