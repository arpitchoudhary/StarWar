package com.starwar.warrior.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.ParameterizedType;
import java.net.ConnectException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BaseFactorySetup<S> {

    private static OkHttpClient okHttpClient;

    private Retrofit retrofit;

    private final String baseUrl;

    private Set<Interceptor> interceptors = new HashSet();

    private S service;

    private Context context;

    protected BaseFactorySetup(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
        Retrofit retrofit = getRetrofit(context, baseUrl);
        ParameterizedType paramType = (ParameterizedType) getClass().getGenericSuperclass();
        Class<S> sClass = (Class<S>) paramType.getActualTypeArguments()[0];
        service = retrofit.create(sClass);
    }


    @NonNull
    private synchronized Retrofit getRetrofit(Context context, String baseUrl) {
        if (retrofit == null) {
            OkHttpClient client = getClient(context);

            Converter.Factory converterFactory = getConverterFactory();
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(converterFactory)
                    .baseUrl(baseUrl)
                    .build();

        }

        return retrofit;
    }

    @NonNull
    private static GsonConverterFactory getConverterFactory() {
        Gson gson = new GsonBuilder()
                .setDateFormat("dd-MM-yyyy 'T' HH:mm:ss Z")
                .setPrettyPrinting()
                .setLenient()
                .create();

        return GsonConverterFactory.create(gson);
    }

    private OkHttpClient getClient(Context context) {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
            okHttpClient = builder.build();
        }

        return okHttpClient;
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    protected S getService() {
        return service;
    }

    protected <T extends Object> Observable<T> prepareObservable(Observable<T> observable) {
        if (!isConnectionAvailable()) {
            return Observable.error(new ConnectException("Connection Not Available"));
        }
        observable = observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    protected <T extends Object> Single<T> prepareSingle(Single<T> single) {
        if (!isConnectionAvailable()) {
            return Single.error(new ConnectException("Connection Not Available"));
        }
        single = single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return single;
    }

    private boolean isConnectionAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
