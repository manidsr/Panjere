package com.example.panjere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;


public class ProfileActivity extends AppCompatActivity {

    private TextView categoryButton, addItemButton, homeButton, profileButton;
    private ImageView categoryIcon, addItemIcon, homeIcon, profileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        // Retrieve the user ID from Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);
        String username = sharedPreferences.getString("username", null);

        TextView userTitle = findViewById(R.id.userTitle);
        userTitle.setText(username);

        // Set up the My Posts button click listener
        Button myPostsButton = findViewById(R.id.myPosts);
        myPostsButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            intent.putExtra("userPosts", true); // Indicator for user-specific posts
            startActivity(intent);
        });

        // Set up the Logout button click listener
        Button logout = findViewById(R.id.Logout);
        logout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Set up the Profile button click listener

        profileButton = findViewById(R.id.Profile);
        profileIcon = findViewById(R.id.imageView4);
        View.OnClickListener profileClickListener = v -> {
            Intent profileIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        };
        profileButton.setOnClickListener(profileClickListener);
        profileIcon.setOnClickListener(profileClickListener);

        // Set up the Home button click listener
        homeButton = findViewById(R.id.Home);
        homeIcon = findViewById(R.id.imageView3);
        View.OnClickListener homeClickListener = v -> {
            Intent homeIntent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(homeIntent);
        };
        homeButton.setOnClickListener(homeClickListener);
        homeIcon.setOnClickListener(homeClickListener);

        // Set up the Add Post button click listener
        addItemButton = findViewById(R.id.addItem);
        addItemIcon = findViewById(R.id.imageView2);
        View.OnClickListener addItemClickListener = v -> {
            Intent addPostIntent = new Intent(ProfileActivity.this, AddPostActivity.class);
            startActivity(addPostIntent);
        };
        addItemButton.setOnClickListener(addItemClickListener);
        addItemIcon.setOnClickListener(addItemClickListener);

        // Set up the Category button click listener
        categoryButton = findViewById(R.id.category);
        categoryIcon = findViewById(R.id.imageView1);
        View.OnClickListener categoryClickListener = v -> {
            Intent categoryIntent = new Intent(ProfileActivity.this, CategoryActivity.class);
            startActivity(categoryIntent);
        };
        categoryButton.setOnClickListener(categoryClickListener);
        categoryIcon.setOnClickListener(categoryClickListener);
    }
}

