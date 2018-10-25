package com.projeto.retrofit.domain;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiEndpoint {
    @GET("posts/{id}")
    Call<Retrofitee> obterDados(@Path("id") int userID);

}
