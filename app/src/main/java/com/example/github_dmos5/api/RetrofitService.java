package com.example.github_dmos5.api;

import com.example.github_dmos5.model.Repositorio;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {
    @GET("/users/{name}/repos")
    Call<List<Repositorio>> getDados(@Path("name") String name);


}
