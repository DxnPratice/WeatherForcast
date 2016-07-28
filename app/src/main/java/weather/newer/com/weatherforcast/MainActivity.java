package weather.newer.com.weatherforcast;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.github.mikephil.charting.components.XAxis.*;

public class MainActivity extends AppCompatActivity {
    ImageView imageviewbk;
    String cityName;
    TextView cityname;
    TextView temp;
    ImageView view2;
    TextView wind;
    EditText edittext;
    ArrayList<String> dates;//日期
    ArrayList<Integer> temps;//最高温度
    ArrayList<Integer> temps1;//最低温度
    LineChart xLineChart;
    LineChart nLineChart;
    JSONObject Id;
    Context context;
    private StringBuffer tv = new StringBuffer();//用来连接从网络上读取的字符串
    //数据源
    private WeatherTile weatherTile;
    DailyWeather dailyWeather;
    private ArrayList<DailyWeather>dailyWeathers;
    private TodayWeather todayWeather;
    private int[] logoes =
            new int[]{R.drawable.weathy_01, R.drawable.weathy_02, R.drawable.weathy_04,
                    R.drawable.weathy_05, R.drawable.weathy_10, R.drawable.weathy_10,
                    R.drawable.weathy_15, R.drawable.weathy_07, R.drawable.weathy_08,
                    R.drawable.weathy_09, R.drawable.weathy_27, R.drawable.weathy_26,
                    R.drawable.weathy_29, R.drawable.weathy_30, R.drawable.weathy_11,
                    R.drawable.weathy_14, R.drawable.weathy_17, R.drawable.weathy_31,
                    R.drawable.weathy_32, R.drawable.weathy_17, R.drawable.weathy_24,
                    R.drawable.weathy_33, R.drawable.weathy_34, R.drawable.weathy_34,
                    R.drawable.weathy_34, R.drawable.weathy_34, R.drawable.weathy_34,
                    R.drawable.weathy_35, R.drawable.weathy_36, R.drawable.weathy_33,
                    R.drawable.weathy_37};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        imageviewbk = (ImageView) findViewById(R.id.bac_iv);
        cityname = (TextView) findViewById(R.id.cityname_tv);
        temp = (TextView) findViewById(R.id.temp_tv);
        view2 = (ImageView) findViewById(R.id.tile_iv);
        wind = (TextView) findViewById(R.id.weather_tv);
        xLineChart = (LineChart) findViewById(R.id.max_chart);
        nLineChart = (LineChart) findViewById(R.id.min_chart);
        edittext = (EditText) findViewById(R.id.editText);

        cityName="黄冈";
        getWeatherData(this, cityName);
       // initView(cityName);
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String m=s.toString();
                cityName=m;
                getWeatherData(MainActivity.this,cityName);



            }
        });




    }

    //获取网络数据
    /*
    *从网络中根据城市名称获取天气数据
     */
    public void getWeatherData(Context context, String cityName) {
        Toast.makeText(MainActivity.this,"cityName"+cityName,Toast.LENGTH_LONG).show();
        try {
            Log.d("today", "请求网络数据的城市：" + cityName);
            Parameters params = new Parameters();
            params.add("cityname", URLEncoder.encode(cityName, "UTF-8"));
            params.add("dtype", "json");
            params.add("format", 2);
            //params.add("key", "d60f77069274e113d1b1674a6ba9d9f4");
            JuheData.executeWithAPI(context,
                    39,
                    "http://v.juhe.cn/weather/index",
                    JuheData.GET, params, new Datecallback());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //回调接口
    public class Datecallback implements DataCallBack {
        /**
         * 请求成功时调用的方法
         *
         * @param i 为http状态码
         * @param s 为请求返回数据.
         */
        @Override
        public void onSuccess(int i, String s) {
            context = null;
            tv.append(s);
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期式
            String str = dateFormat.format(now);

            //数据返回时进行解析
            weatherTile = null;
            dailyWeathers = null;
            todayWeather = null;
            try {Log.d("data",tv.toString());
                JSONObject root = new JSONObject(tv.toString());
                /**
                 * 此处解析JSON数据
                 */
                //WeatherInfo天气数据
                JSONObject resultEntity = root.getJSONObject("result");
                //WeatherTile实时天气SK
                JSONObject sk = resultEntity.getJSONObject("sk");
                //TodayWeahter今日天气today
                JSONObject today = resultEntity.getJSONObject("today");
                JSONObject wid=today.getJSONObject("weather_id");
                //DailyWeather未来天气future
                JSONArray futureArray = resultEntity.getJSONArray("future");
                //测试数据
                //实时天气

                weatherTile =
                        new WeatherTile(
                                sk.getString("wind_strength"),
                                sk.getString("time"),
                                sk.getString("humidity"),
                                sk.getString("wind_direction"),
                                sk.getString("temp"));
                Log.d("Tile",weatherTile.toString());
                //今日天气
                todayWeather =
                        new TodayWeather(today.getString("wind"),
                                today.getString("uv_index"),
                                today.getString("travel_index"),
                                today.getString("temperature"),
                                today.getString("city"),
                                today.getString("comfort_index"),
                                today.getString("date_y"),
                                today.getString("wash_index"),
                                today.getString("exercise_index"),
                                today.getString("weather"),
                                today.getString("drying_index"),
                                new Weather_idEntity(wid.getString("fa"),wid.getString("fb")),
                                today.getString("dressing_advice"),
                                today.getString("week"),
                                today.getString("dressing_index"));
                Log.d("today",todayWeather.toString());
                //未来天气
                dailyWeathers = new ArrayList<>();
                for (int j = 0; j < futureArray.length(); j++) {
                    JSONObject obj = (JSONObject) futureArray.get(j);
                     dailyWeather =
                            new DailyWeather(obj.getString("wind"),
                                    obj.getString("weather"),
                                    obj.getString("date"),
                                    obj.getString("week"),
                                    obj.getString("temperature"));
                    dailyWeathers.add(dailyWeather);

                }
                        initView();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onFailure(int i, String s, Throwable throwable) {

        }
    }



    //获取实时天气的当前温度
    public String getTemperture(WeatherTile titl) {
        return titl.getTemp() + "°";

    }

    //获取天气图标的Id
    public int getWeatherLogo(String id) {
        int index = Integer.parseInt(id);
        if (index > 31) {
            index = 33;
        }
        return logoes[index];
    }


    //获取实时天气的风力和风向
    public String getWind(WeatherTile titl) {
        return titl.getWind_strength() + " " + titl.getWind_direction();
    }

    // 获取最高温度

    public ArrayList<Integer> getMaxTemp(ArrayList<DailyWeather> dailyWeathers) {
        ArrayList<Integer> maxTemps=new ArrayList<>();
        for(int i=0;i<dailyWeathers.size();i++) {
            String temp_str=dailyWeathers.get(i).getTemperature();
            int index1=temp_str.indexOf("~");
            int index2=temp_str.lastIndexOf("℃");
            int temp=Integer.parseInt(temp_str.substring(index1+1,index2));
             maxTemps.add(temp);
        }
        return maxTemps;
    }
   // 获取最低温度
    public ArrayList<Integer> getMinTemp(ArrayList<DailyWeather> dailyWeathers) {
        ArrayList<Integer> minTemps=new ArrayList<>();
        int temp;
        for(int i=0;i<dailyWeathers.size();i++) {
            String temp_str=dailyWeathers.get(i).getTemperature();
            int index=temp_str.indexOf("~");
            temp=Integer.parseInt(temp_str.substring(0,index-1));
            minTemps.add(temp);

        }
        return minTemps;
    }
    //获取日期
    public ArrayList<String> getDates(ArrayList<DailyWeather> dailyWeathers) {
        ArrayList<String> dates=new ArrayList<>();
        String week;
        for(int i=0;i<dailyWeathers.size();i++) {
            week=dailyWeathers.get(i).getWeek();
            dates.add(week);
        }
        return dates;
    }


    //给组件初始化数据

    public void initView() {

        imageviewbk.setImageResource(R.drawable.bk1);//给背景图片赋值
        cityname.setText(cityName);//给第一个赋值
        temp.setText(getTemperture(weatherTile));//给温度赋值
        view2.setImageResource(getWeatherLogo(todayWeather.getWeather_id().getFa()));//给图标赋值
        wind.setText(getWind(weatherTile));//给风力和风向赋值
        dates=getDates(dailyWeathers);
        temps=getMaxTemp(dailyWeathers);
        temps1=getMinTemp(dailyWeathers);
        new TempUI(dates,temps,temps1,xLineChart,nLineChart);






    }



}
