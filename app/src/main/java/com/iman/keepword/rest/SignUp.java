package com.iman.keepword.rest;

import com.iman.keepword.model.Log;
import com.iman.keepword.model.User;

import java.util.List;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface SignUp {

    @POST("signup")
    Call<User> getUser (@Body User user);

    @POST("enter")
    Call<Log> enter(@Body Log log);

    @POST("signup")
    Call<User> signUp(@Body User user);

    @POST("login")
    Call<User> login(@Body User user);
}
