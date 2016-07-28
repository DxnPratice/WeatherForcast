package weather.newer.com.weatherforcast;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by windows on 2016/7/28.
 */
public class Result
    implements Serializable

    {

        private WeatherTile tilt;
        private TodayWeather today;
        private ArrayList<DailyWeather> dailyList;



    public void setTilt(WeatherTile tilt) {
        this.tilt = tilt;
    }

    public void setToday(TodayWeather today) {
        this.today = today;
    }

    public void setDaily(ArrayList<DailyWeather> dailyList) {
        this.dailyList = dailyList;
    }



    public WeatherTile getTilt() {
        return tilt;
    }

    public TodayWeather getToday() {
        return today;
    }

    public ArrayList<DailyWeather> getDaily() {
        return dailyList;
    }
}
