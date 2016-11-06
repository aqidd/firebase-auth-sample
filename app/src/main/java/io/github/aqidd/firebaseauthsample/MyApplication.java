package io.github.aqidd.firebaseauthsample;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by bukhoriaqid on 11/4/16.
 */

public class MyApplication extends Application
{
    @Override
    public void onCreate ()
    {
        super.onCreate();
        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
