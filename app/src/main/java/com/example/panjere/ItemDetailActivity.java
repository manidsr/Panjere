package com.example.panjere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemDetailActivity extends AppCompatActivity {

    private static final String TAG = "ItemDetailActivity";

    private ImageView itemImageView;
    private TextView nameTextView, descriptionTextView, priceTextView;
    private Button deleteButton, editButton;
    private ApiService apiService;
    private TextView categoryButton, addItemButton, homeButton, profileButton;
    private ImageView categoryIcon, addItemIcon, homeIcon, profileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        itemImageView = findViewById(R.id.itemDetailImage);
        nameTextView = findViewById(R.id.itemTitle);
        descriptionTextView = findViewById(R.id.itemDescription);
        priceTextView = findViewById(R.id.itemPrice);
        deleteButton = findViewById(R.id.deletebtn);
        editButton = findViewById(R.id.editbtn);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://avatft.pythonanywhere.com")  // Replace with your actual API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);


        // Get the passed item details
        int itemId = getIntent().getIntExtra("itemId", -1);
        String itemName = getIntent().getStringExtra("itemName");
        String itemDescription = getIntent().getStringExtra("itemDescription");
        float itemPrice = getIntent().getFloatExtra("itemPrice", 0);
        String itemImageBase64 = getIntent().getStringExtra("itemImageBase64");
        String itemUserId = getIntent().getStringExtra("itemUserId");
        String categoryName = getIntent().getStringExtra("categoryName");
        String categoryId = getIntent().getStringExtra("categoryId");  // Get category name


        // Set the item details to the views
        nameTextView.setText(itemName);
        descriptionTextView.setText(itemDescription);
        priceTextView.setText("$" + String.format("%.2f", itemPrice));

        // Decode Base64 image and set it to ImageView
        if (itemImageBase64 != null && !itemImageBase64.isEmpty()) {
            byte[] decodedString = Base64.decode(itemImageBase64, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            itemImageView.setImageBitmap(decodedBitmap);
        } else {
            itemImageView.setImageResource(R.drawable.ic_placeholder); // Use a placeholder image if no base64 data is available
        }

        // Retrieve the user ID from Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        // Show or hide the delete and edit buttons based on item ownership
        if (itemUserId != null && itemUserId.equals(userId)) {
            deleteButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);

            deleteButton.setOnClickListener(v -> {
                Log.d(TAG, "Delete button clicked");

                // Create a JsonObject for the request body
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("userId", userId);

                Call<Void> call = apiService.deleteItem(itemId, jsonObject);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "Item deleted successfully");
                            Toast.makeText(ItemDetailActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ItemDetailActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Log.e(TAG, "Failed to delete item, response code: " + response.code());
                            Toast.makeText(ItemDetailActivity.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG, "Error: " + t.getMessage());
                        Toast.makeText(ItemDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });

            editButton.setOnClickListener(v -> {
                Intent intent = new Intent(ItemDetailActivity.this, AddPostActivity.class);
                intent.putExtra("itemId", itemId);
                intent.putExtra("itemName", itemName);
                intent.putExtra("itemDescription", itemDescription);
                intent.putExtra("itemPrice", itemPrice);
                intent.putExtra("itemImageBase64", itemImageBase64);
                intent.putExtra("categoryName", categoryName);
                intent.putExtra("categoryId", categoryId);

                startActivity(intent);
            });

        } else {
            deleteButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
        }

        // Set up the Profile button click listener

        profileButton = findViewById(R.id.Profile);
        profileIcon = findViewById(R.id.imageView4);
        View.OnClickListener profileClickListener = v -> {
            Intent profileIntent = new Intent(ItemDetailActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        };
        profileButton.setOnClickListener(profileClickListener);
        profileIcon.setOnClickListener(profileClickListener);

        // Set up the Home button click listener
        homeButton = findViewById(R.id.Home);
        homeIcon = findViewById(R.id.imageView3);
        View.OnClickListener homeClickListener = v -> {
            Intent homeIntent = new Intent(ItemDetailActivity.this, HomeActivity.class);
            startActivity(homeIntent);
        };
        homeButton.setOnClickListener(homeClickListener);
        homeIcon.setOnClickListener(homeClickListener);

        // Set up the Add Post button click listener
        addItemButton = findViewById(R.id.addItem);
        addItemIcon = findViewById(R.id.imageView2);
        View.OnClickListener addItemClickListener = v -> {
            Intent addPostIntent = new Intent(ItemDetailActivity.this, AddPostActivity.class);
            startActivity(addPostIntent);
        };
        addItemButton.setOnClickListener(addItemClickListener);
        addItemIcon.setOnClickListener(addItemClickListener);

        // Set up the Category button click listener
        categoryButton = findViewById(R.id.category);
        categoryIcon = findViewById(R.id.imageView1);
        View.OnClickListener categoryClickListener = v -> {
            Intent categoryIntent = new Intent(ItemDetailActivity.this, CategoryActivity.class);
            startActivity(categoryIntent);
        };
        categoryButton.setOnClickListener(categoryClickListener);
        categoryIcon.setOnClickListener(categoryClickListener);

    }
}
