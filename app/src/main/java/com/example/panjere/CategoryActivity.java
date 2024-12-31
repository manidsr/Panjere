package com.example.panjere;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryActivity extends AppCompatActivity {

    private ListView listView;
    private ApiService apiService;
    private List<Category> categories;
    private TextView categoryButton, addItemButton, homeButton, profileButton;
    private ImageView categoryIcon, addItemIcon, homeIcon, profileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        listView = findViewById(R.id.listView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://avatft.pythonanywhere.com")  // Replace with your actual API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        fetchCategories();

        // Set up the Profile button click listener

        profileButton = findViewById(R.id.Profile);
        profileIcon = findViewById(R.id.imageView4);
        View.OnClickListener profileClickListener = v -> {
            Intent profileIntent = new Intent(CategoryActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        };
        profileButton.setOnClickListener(profileClickListener);
        profileIcon.setOnClickListener(profileClickListener);

        // Set up the Home button click listener
        homeButton = findViewById(R.id.Home);
        homeIcon = findViewById(R.id.imageView3);
        View.OnClickListener homeClickListener = v -> {
            Intent homeIntent = new Intent(CategoryActivity.this, HomeActivity.class);
            startActivity(homeIntent);
        };
        homeButton.setOnClickListener(homeClickListener);
        homeIcon.setOnClickListener(homeClickListener);

        // Set up the Add Post button click listener
        addItemButton = findViewById(R.id.addItem);
        addItemIcon = findViewById(R.id.imageView2);
        View.OnClickListener addItemClickListener = v -> {
            Intent addPostIntent = new Intent(CategoryActivity.this, AddPostActivity.class);
            startActivity(addPostIntent);
        };
        addItemButton.setOnClickListener(addItemClickListener);
        addItemIcon.setOnClickListener(addItemClickListener);

        // Set up the Category button click listener
        categoryButton = findViewById(R.id.category);
        categoryIcon = findViewById(R.id.imageView1);
        View.OnClickListener categoryClickListener = v -> {
            Intent categoryIntent = new Intent(CategoryActivity.this, CategoryActivity.class);
            startActivity(categoryIntent);
        };
        categoryButton.setOnClickListener(categoryClickListener);
        categoryIcon.setOnClickListener(categoryClickListener);
    }

    private void fetchCategories() {
        Call<List<Category>> call = apiService.getCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories = response.body();
                    ArrayAdapter<Category> adapter = new ArrayAdapter<>(CategoryActivity.this,
                            android.R.layout.simple_list_item_1, categories);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        Category selectedCategory = categories.get(position);
                        Intent intent = new Intent(CategoryActivity.this, HomeActivity.class);
                        intent.putExtra("categoryId", selectedCategory.getId());
                        intent.putExtra("categoryName", selectedCategory.getName());
                        startActivity(intent);
                    });
                } else {
                    Toast.makeText(CategoryActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(CategoryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
