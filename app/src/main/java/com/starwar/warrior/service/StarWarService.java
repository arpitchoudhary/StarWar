package com.starwar.warrior.service;

import android.support.annotation.WorkerThread;

import com.starwar.warrior.dashboard.modal.StarWarCharacter;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface StarWarService {

    @WorkerThread
    @GET("people/")
    Observable<StarWarCharacter> getCharacter(@Query("page") int pageNumber);
}
