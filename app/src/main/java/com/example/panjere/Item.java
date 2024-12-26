package com.example.panjere;

public class Item {
    private int id;
    private String name;
    private String description;
    private float price;
    private String image_base64;
    private String user_id;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public String getImageBase64() { return image_base64; }
    public void setImageBase64(String image_base64) { this.image_base64 = image_base64; }

    public String getUserId() { return user_id; }
    public void setUserId(String user_id) { this.user_id = user_id; }
}
