package com.tnt.ethiopianmoviesboxoffice;

import android.app.Application;

public class EthiopianMoviesBoxOfficeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AudienceNetworkInitializeHelper.initialize(this);
    }
}
