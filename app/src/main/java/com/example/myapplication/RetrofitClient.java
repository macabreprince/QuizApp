package com.example.myapplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static String BASE_URL = "https://api.anshuwap.com/kbc/api/";
    private static Retrofit retrofit;
    private static ConnectingService service;

    static Retrofit getInstance(){
        if(retrofit==null){
            Retrofit.Builder builder=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            retrofit=builder.build();
        }
        return retrofit;
    }

    static ConnectingService getService(){
        if(service==null){
            service=getInstance().create(ConnectingService.class);
        }
        return service;
    }

}
