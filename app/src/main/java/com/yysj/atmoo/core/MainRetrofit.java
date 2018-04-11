package com.yysj.atmoo.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import java.util.concurrent.TimeUnit;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class MainRetrofit {

    final Api mService;

    final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").serializeNulls().create();

    MainRetrofit() {
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(21, TimeUnit.SECONDS);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint(MainFactory.HOST)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        mService = restAdapter.create(Api.class);
    }

    public Api getService(){
        return mService;
    }
}
