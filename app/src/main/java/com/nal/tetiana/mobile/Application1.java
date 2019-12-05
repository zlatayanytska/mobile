package com.nal.tetiana.mobile;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.nal.tetiana.mobile.MoviesApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Application1 extends android.app.Application {

    private MoviesApi movieService;
    private FirebaseAuth auth;

    public void onCreate()
    {
        super.onCreate();
        auth = FirebaseAuth.getInstance();
        movieService = createMovieApiService();
    }

    public FirebaseAuth getAuth(){
        return auth;
    }

    public MoviesApi getMovieService(){
        return movieService;
    }

    public MoviesApi createMovieApiService(){
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-central1-lab1a-8fab9.cloudfunctions.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(MoviesApi.class);
    }
}
