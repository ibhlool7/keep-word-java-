package com.iman.keepword.rest;

import com.iman.keepword.model.WordModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApi {
    @POST(value = "api/{user}/{password}")
    Call<WordModel> addWord(@Path("user") String user,
                                   @Path("password") String password,
                                   @Body WordModel wordModel);

}
