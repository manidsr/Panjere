package com.example.panjere;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.DELETE;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public interface ApiService {

    @POST("/users/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("/items")
    Call<List<Item>> getAllItems();

    @GET("/items/user/{userId}")
    Call<List<Item>> getUserItems(@Path("userId") String userId);

    @GET("/items/search")
    Call<List<Item>> searchItems(@Query("keyword") String keyword);
    @HTTP(method = "DELETE", path = "/items/{item_id}/delete", hasBody = true)
    Call<Void> deleteItem(@Path("item_id") int itemId, @Body JsonObject userIdBody);

    @Multipart
    @POST("/items")
    Call<Void> uploadItem(
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("userId") RequestBody userId,
            @Part MultipartBody.Part image);
}