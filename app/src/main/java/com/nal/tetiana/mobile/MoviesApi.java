package com.nal.tetiana.mobile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MoviesApi {

    @GET("/movies/")
    Call<List<Movies>> getAllMovies();
}
