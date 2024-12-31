package com.example.panjere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private ApiService apiService;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://avatft.pythonanywhere.com")  // Replace with your actual API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        Intent intent = getIntent();
        boolean isUserPosts = intent.getBooleanExtra("userPosts", false);
        int categoryId = intent.getIntExtra("categoryId", -1);

        if (isUserPosts) {
            // Retrieve the user ID from Shared Preferences
            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
            String userId = sharedPreferences.getString("userId", null);
            fetchUserItems(userId);
        } else if (categoryId != -1) {
            fetchItemsByCategory(categoryId);
        } else {
            fetchAllItems();
        }

        // Set up the Profile button click listener
        Button profileButton = findViewById(R.id.Profile);
        profileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        });

        // Set up the Home button click listener
        Button homeButton = findViewById(R.id.Home);
        homeButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(homeIntent);
        });

        // Set up the Add Post button click listener
        Button addPost = findViewById(R.id.addItem);
        addPost.setOnClickListener(v -> {
            Intent addPostIntent = new Intent(HomeActivity.this, AddPostActivity.class);
            startActivity(addPostIntent);
        });

        // Set up the Add Post button click listener
        Button category = findViewById(R.id.category);
        category.setOnClickListener(v -> {
            Intent CategoryIntent = new Intent(HomeActivity.this, CategoryActivity.class);
            startActivity(CategoryIntent);
        });

        // Set up the search functionality
        searchText = findViewById(R.id.searchText);
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            String keyword = searchText.getText().toString().trim();
            if (!keyword.isEmpty()) {
                searchItems(keyword);
            } else {
                Toast.makeText(HomeActivity.this, "Please enter a search keyword", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchItemsByCategory(int categoryId) {
        Call<List<Item>> call = apiService.searchItemsByCategory(categoryId);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemAdapter = new ItemAdapter(HomeActivity.this, response.body());
                    recyclerView.setAdapter(itemAdapter);
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to load items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchAllItems() {
        Call<List<Item>> call = apiService.getAllItems();
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemAdapter = new ItemAdapter(HomeActivity.this, response.body());
                    recyclerView.setAdapter(itemAdapter);
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to load items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserItems(String userId) {
        Call<List<Item>> call = apiService.getUserItems(userId);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemAdapter = new ItemAdapter(HomeActivity.this, response.body());
                    recyclerView.setAdapter(itemAdapter);
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to load items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchItems(String keyword) {
        Call<List<Item>> call = apiService.searchItems(keyword);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemAdapter = new ItemAdapter(HomeActivity.this, response.body());
                    recyclerView.setAdapter(itemAdapter);
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to load search results", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
