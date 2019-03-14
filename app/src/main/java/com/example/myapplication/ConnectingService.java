package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ConnectingService {

    @GET("categories/categories")
    Call<List<CategoriesReference>> getCategoriesReference();

    @GET("quiz/quiz/catid/{id}")
    Call<QuestionsReference> getQuestionsReference(@Path("id") int id);

    @GET("quiz/en/catid/{id}")
    Call<QuestionsReference> getEnglishQuestionsReference(@Path("id") int id);

    @GET("quiz/hi/catid/{id}")
    Call<QuestionsReference> getHindiQuestionsReference(@Path("id") int id);

    @GET("quiz/en")
    Call<QuestionsReference> getEnglishMiscellaneous();
    @GET("quiz/hi")
    Call<QuestionsReference> getHindiMiscellaneous();


    @GET("news/news")
    Call<List<NoticePOJO>> getNotice();


}
