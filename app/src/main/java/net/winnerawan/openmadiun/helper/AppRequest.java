package net.winnerawan.openmadiun.helper;

import net.winnerawan.openmadiun.config.AppConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by winnerawan on 2/9/17.
 */

public class AppRequest {

    public Retrofit Guide() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
