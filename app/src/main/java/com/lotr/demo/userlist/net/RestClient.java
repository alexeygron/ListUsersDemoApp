package com.lotr.demo.userlist.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provide retrofit2 client as singleton
 */
public class RestClient {

    private static volatile Retrofit instance;

    public static Retrofit getInstance() {
        Retrofit localInstance = instance;
        if (localInstance == null) {
            synchronized (RestClient.class) {
                localInstance = instance;
                if (localInstance == null) {

                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Config.URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    instance = localInstance = retrofit;
                }
            }
        }
        return localInstance;
    }
}
