package com.example.task5_1_itube;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    // UI elements for user input
    private EditText fullNameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button createAccButton; // Button to create a new account
    private UserDatabaseHelper dbHelper; // Database helper for user data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup); // Set the layout for the signup activity

        // Bind UI elements to variables
        fullNameEditText = findViewById(R.id.FullNameEditText); // Field for full name
        usernameEditText = findViewById(R.id.UsernameEditText); // Field for username
        passwordEditText = findViewById(R.id.passwordEditText); // Field for password
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText); // Field for confirming password
        createAccButton = findViewById(R.id.createAccButton); // Button to create account

        // Create an instance of the database helper
        dbHelper = new UserDatabaseHelper(this);

        // Set a click listener for the "Create Account" button
        createAccButton.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString(); // Get the full name input
            String username = usernameEditText.getText().toString(); // Get the username input
            String password = passwordEditText.getText().toString(); // Get the password input
            String confirmPassword = confirmPasswordEditText.getText().toString(); // Get the confirm password input

            // Basic validation logic
            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                return; // If any field is empty, show a toast and return
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return; // If passwords do not match, show a toast and return
            }

            // Attempt to add the new user to the database
            if (dbHelper.addUser(username, password)) {
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                // Redirect to the login screen
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent); // Start the login activity
                finish(); // Close the current activity

            } else {
                // If the username already exists, show a toast
                Toast.makeText(this, "Username already exists.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
