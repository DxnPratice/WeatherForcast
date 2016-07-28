package weather.newer.com.weatherforcast;

import android.app.Application;

import com.thinkland.sdk.android.JuheSDKInitializer;

/**
 * Created by windows on 2016/7/28.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();//getApplicationContext
        JuheSDKInitializer.initialize(getApplicationContext());
    }
}
