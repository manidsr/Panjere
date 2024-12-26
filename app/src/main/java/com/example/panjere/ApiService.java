package com.example.panjere;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/users/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("/items")
    Call<List<Item>> getAllItems();
}
