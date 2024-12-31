package com.example.panjere;

public class Category {
    private int id;
    private String name;

    // Default constructor
    public Category() {
    }

    // Parameterized constructor
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Override toString method for Spinner display
    @Override
    public String toString() {
        return name;
    }
}
