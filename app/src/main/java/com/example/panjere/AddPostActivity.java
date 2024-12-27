package com.example.panjere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddPostActivity extends AppCompatActivity {

    private EditText itemName, itemDescription, itemPrice;
    private ImageView itemImageView;
    private File imageFile;
    private ApiService apiService;
    private int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        itemPrice = findViewById(R.id.itemPrice);
        itemImageView = findViewById(R.id.itemImageView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://avatft.pythonanywhere.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        Button uploadImageButton = findViewById(R.id.uploadImageButton);
        uploadImageButton.setOnClickListener(v -> selectImage());

        Button submitItemButton = findViewById(R.id.submitItemButton);
        submitItemButton.setOnClickListener(v -> submitItem());

        // Retrieve the data passed from ItemDetailActivity
        Intent intent = getIntent();
        itemId = intent.getIntExtra("itemId", -1);
        String itemNameStr = intent.getStringExtra("itemName");
        String itemDescriptionStr = intent.getStringExtra("itemDescription");
        float itemPriceValue = intent.getFloatExtra("itemPrice", 0);
        String itemImageBase64 = intent.getStringExtra("itemImageBase64");

        // Populate the fields with the received data
        if (itemNameStr != null) itemName.setText(itemNameStr);
        if (itemDescriptionStr != null) itemDescription.setText(itemDescriptionStr);
        if (itemPriceValue != 0) itemPrice.setText(String.valueOf(itemPriceValue));

        // Decode and set the image
        if (itemImageBase64 != null && !itemImageBase64.isEmpty()) {
            byte[] decodedString = Base64.decode(itemImageBase64, Base64.DEFAULT);
            android.graphics.Bitmap decodedBitmap = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            itemImageView.setImageBitmap(decodedBitmap);
        }
    }

    private void selectImage() {
        // Intent to select an image from the device
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            try {
                Uri imageUri = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(imageUri);

                imageFile = new File(getCacheDir(), "uploaded_image.jpg");
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                itemImageView.setImageURI(imageUri);
                Log.d("AddPostActivity", "Image File Path: " + imageFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void submitItem() {
        String name = itemName.getText().toString().trim();
        String description = itemDescription.getText().toString().trim();
        String price = itemPrice.getText().toString().trim();
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        if (name.isEmpty() || description.isEmpty() || price.isEmpty() || userId == null || userId.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if it's an update or a new submission
        if (itemId != -1) {
            // Update existing item
            RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
            RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), price);
            RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), userId);
            MultipartBody.Part imagePart = null;
            if (imageFile != null) {
                RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageBody);
            }

            Call<Void> call;
            if (imagePart != null) {
                call = apiService.updateItemWithImage(itemId, nameBody, descriptionBody, priceBody, userIdBody, imagePart);
            } else {
                call = apiService.updateItem(itemId, nameBody, descriptionBody, priceBody, userIdBody);
            }

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddPostActivity.this, "Item updated successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddPostActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        try {
                            Log.e("AddPostActivity", "Failed to update item: " + response.errorBody().string());
                            Toast.makeText(AddPostActivity.this, "Failed to update item: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("AddPostActivity", "Error: " + t.getMessage(), t);
                    Toast.makeText(AddPostActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // New item submission
            RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
            RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), price);
            RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), userId);
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageBody);

            Call<Void> call = apiService.uploadItem(nameBody, descriptionBody, priceBody, userIdBody, imagePart);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddPostActivity.this, "Item created successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddPostActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        try {
                            Log.e("AddPostActivity", "Failed to create item: " + response.errorBody().string());
                            Toast.makeText(AddPostActivity.this, "Failed to create item: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("AddPostActivity", "Error: " + t.getMessage(), t);
                    Toast.makeText(AddPostActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
