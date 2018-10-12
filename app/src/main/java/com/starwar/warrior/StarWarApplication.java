package com.starwar.warrior;

import android.app.Application;

import com.starwar.warrior.service.StarWarApiFactory;


public class StarWarApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StarWarApiFactory.getInstance().init(this);
    }

}
