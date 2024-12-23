package com.example.panjere;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/users/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
}
