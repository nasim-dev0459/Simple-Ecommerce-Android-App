package com.example.simpleecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore; // ফায়ারস্টোর ইমপোর্ট
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameField, emailField, passField;
    private Button regBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // ফায়ারস্টোর ডিক্লেয়ার করা

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

// made with nasim
        // Connecting Firebase and Views
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameField = findViewById(R.id.regName);
        emailField = findViewById(R.id.regEmail);
        passField = findViewById(R.id.regPassword);
        regBtn = findViewById(R.id.btnRegister);
        TextView loginLink = findViewById(R.id.txtLoginLink);

        regBtn.setOnClickListener(v -> {
            String name = nameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passField.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.length() < 6) {
                Toast.makeText(this, "Fill in all the fields and enter the correct password.", Toast.LENGTH_SHORT).show();
            } else {
                // 1. Creating a user in Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // 2. If user creation is successful, save the information to Firestore
                                saveUserToFirestore(name, email);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error occurred: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        loginLink.setOnClickListener(v -> finish());
    }

    // Method for saving data in Firestore
    private void saveUserToFirestore(String name, String email) {
        String userId = mAuth.getCurrentUser().getUid(); // Get the user's unique ID

        // Creating a data map
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("role", "customer");

        // Sending data to the "Users" collection
        db.collection("Users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Congratulations! Registration and data saving successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Problem saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
// nasim