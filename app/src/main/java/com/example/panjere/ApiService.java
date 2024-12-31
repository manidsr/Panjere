package com.example.panjere;

import com.google.gson.JsonObject;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.DELETE;

public interface ApiService {

    @POST("/users/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("/items")
    Call<List<Item>> getAllItems();

    @GET("/items/user/{userId}")
    Call<List<Item>> getUserItems(@Path("userId") String userId);

    @GET("/items/search")
    Call<List<Item>> searchItems(@Query("keyword") String keyword);

    @GET("/items/search")
    Call<List<Item>> searchItemsByCategory(@Query("category_id") int categoryId);

    @HTTP(method = "DELETE", path = "/items/{item_id}/delete", hasBody = true)
    Call<Void> deleteItem(@Path("item_id") int itemId, @Body JsonObject userIdBody);

    @Multipart
    @POST("/items")
    Call<Void> uploadItem(
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("userId") RequestBody userId,
            @Part("categoryId") RequestBody categoryId,  // Add categoryId part
            @Part MultipartBody.Part image);

    @Multipart
    @PUT("/items/{item_id}")
    Call<Void> updateItemWithImage(
            @Path("item_id") int itemId,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("userId") RequestBody userId,
            @Part("categoryId") RequestBody categoryId,  // Add categoryId part
            @Part MultipartBody.Part image);

    @Multipart
    @PUT("/items/{item_id}")
    Call<Void> updateItem(
            @Path("item_id") int itemId,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("userId") RequestBody userId,
            @Part("categoryId") RequestBody categoryId);  // Add categoryId part

    @GET("/categories")
    Call<List<Category>> getCategories();
}
