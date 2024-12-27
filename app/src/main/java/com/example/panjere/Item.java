package com.example.panjere;

import com.google.gson.annotations.SerializedName;

public class Item {
    private int id;

    private String name;
    private String description;
    private float price;  // Price field

    @SerializedName("image_base64")
    private String imageBase64;  // Use camelCase for consistency

    @SerializedName("user_id")
    private String userId;

    // Default constructor
    public Item() {
    }

    // Parameterized constructor
    public Item(String name, String description, float price, String imageBase64, String userId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageBase64 = imageBase64;
        this.userId = userId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
