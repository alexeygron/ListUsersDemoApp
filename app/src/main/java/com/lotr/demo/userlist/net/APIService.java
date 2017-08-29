package com.lotr.demo.userlist.net;

import com.lotr.demo.userlist.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

/**
 * Description requests for api to retrofit2
 */
public interface APIService {

    @GET("/users.json")
    Call<List<User>> getUsers();

    @POST("/users.json")
    Call<ResponseBody> createUser(@Body User user);

    @PATCH("/users/1.json")
    Call<ResponseBody> updateUser(@Body User user);
}
