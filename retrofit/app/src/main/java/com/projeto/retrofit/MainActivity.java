package com.projeto.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projeto.retrofit.domain.ApiEndpoint;
import com.projeto.retrofit.domain.Retrofitee;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView mTextView = (TextView) findViewById(R.id.bodi);
        final TextView mTextViewTitle = (TextView) findViewById(R.id.idtitle);

        ///////////////
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com").addConverterFactory(GsonConverterFactory.create(gson)).build();
        ApiEndpoint apiService = retrofit.create(ApiEndpoint.class);
        Call<Retrofitee> call = apiService.obterDados(1);
        call.enqueue(new Callback<Retrofitee>() {
            //chamada assíncrona
                         public void onResponse(Call<Retrofitee> call, Response<Retrofitee> response) {
                            // int statusCode = response.code();
                             Retrofitee user = response.body();
                         // mUserIdTextView=findViewById(R.id.bodi).s(com.projeto.retrofit.domain.Retrofitee.getBody().toString());


                             Log.i("TESTESSSSS","OIIIIII------------------------- "+ user.getBody());
                             mTextView.setText(user.getBody());
                             mTextViewTitle.setText(user.getTitle());
                             //Log.i("teste", "Cidade do usuário: " + user.getAddress().getCity())
                         }

                    @Override
                    public void onFailure(Call<Retrofitee> call, Throwable t) {
                        Log.i("teste",t.toString());

                    }







        //////////////
    });
}
}
