package com.example.panjere;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    private ImageView itemImageView;
    private TextView nameTextView, descriptionTextView, priceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        itemImageView = findViewById(R.id.itemDetailImage);
        nameTextView = findViewById(R.id.itemTitle);
        descriptionTextView = findViewById(R.id.itemDescription);
        priceTextView = findViewById(R.id.itemPrice);

        // Get the passed item details
        int itemId = getIntent().getIntExtra("itemId", -1);
        String itemName = getIntent().getStringExtra("itemName");
        String itemDescription = getIntent().getStringExtra("itemDescription");
        float itemPrice = getIntent().getFloatExtra("itemPrice", 0);
        String itemImageBase64 = getIntent().getStringExtra("itemImageBase64");

        // Set the item details to the views
        nameTextView.setText(itemName);
        descriptionTextView.setText(itemDescription);
        priceTextView.setText("Price: $" + String.format("%.2f", itemPrice));

        // Decode Base64 image and set it to ImageView
        if (itemImageBase64 != null && !itemImageBase64.isEmpty()) {
            byte[] decodedString = Base64.decode(itemImageBase64, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            itemImageView.setImageBitmap(decodedBitmap);
        } else {
            itemImageView.setImageResource(R.drawable.ic_placeholder); // Use a placeholder image if no base64 data is available
        }
    }
}
