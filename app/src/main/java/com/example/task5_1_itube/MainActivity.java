package com.example.task5_1_itube;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button signupButton;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // This ensures proper display in full-screen mode
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bind UI elements to variables
        usernameInput = findViewById(R.id.usernameInput); // Input field for username
        passwordInput = findViewById(R.id.passwordInput); // Input field for password
        loginButton = findViewById(R.id.loginButton);     // Login button
        signupButton = findViewById(R.id.signupButton);   // Signup button

        // Initialize the database helper
        dbHelper = new UserDatabaseHelper(this);

        // Set an onClickListener for the login button
        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString(); // Get the entered username
            String password = passwordInput.getText().toString(); // Get the entered password

            // Validate username and password against the database
            if (dbHelper.validateUser(username, password)) {
                Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                // Navigate to the MenuActivity
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                finish(); // Close the current activity after login

            } else {
                Toast.makeText(MainActivity.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set an onClickListener for the signup button
        signupButton.setOnClickListener(v -> {
            // Start the SignUpActivity when the signup button is clicked
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
