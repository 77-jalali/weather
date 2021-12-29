package com.diacoipj.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import Json.Clouds;
import Json.Coord;
import Json.WeatherApi;
import Json.Wind;
import Json.main;
import Json.sys;
import Json.weather;

public class MainActivity extends AppCompatActivity {
    private TextView cityName, temp, description, humidity, pressure, windd, sunrise, sunset, updated;
    private ImageView iconView, icon;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = (TextView) findViewById(R.id.cityText);
        iconView = (ImageView) findViewById(R.id.thumbnailIcom);
        temp = (TextView) findViewById(R.id.tempText);
        description = (TextView) findViewById(R.id.cloudText);
        humidity = (TextView) findViewById(R.id.humidText);
        pressure = (TextView) findViewById(R.id.pressureText);
        windd = (TextView) findViewById(R.id.windText);
        sunrise = (TextView) findViewById(R.id.riseText);
        sunset = (TextView) findViewById(R.id.setText);
        updated = (TextView) findViewById(R.id.updateText);
        relativeLayout = (RelativeLayout) findViewById(R.id.back);
        linearLayout = (LinearLayout) findViewById(R.id.ll);
        progressBar = (ProgressBar) findViewById(R.id.pr);
        icon = (ImageView) findViewById(R.id.Icon);

        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://api.openweathermap.org/data/2.5/weather?q=Lahijan,IR&units=metric&appid=e91da184703396781f9ee1253313a0c5&appid=e91da184703396781f9ee1253313a0c5", response -> {
            Log.d("TAG", "onCreate: " + response);


            progressBar.setVisibility(View.GONE);
            icon.setVisibility(View.VISIBLE);
            cityName.setVisibility(View.VISIBLE);
            iconView.setVisibility(View.VISIBLE);
            temp.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
            humidity.setVisibility(View.VISIBLE);
            pressure.setVisibility(View.VISIBLE);
            windd.setVisibility(View.VISIBLE);
            sunrise.setVisibility(View.VISIBLE);
            sunset.setVisibility(View.VISIBLE);
            updated.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);


            try {
                JSONObject jsonObject = new JSONObject(response);
                WeatherApi weatherr = new WeatherApi();
                Coord coord = new Coord();
                coord.setLat(jsonObject.getJSONObject("coord").getDouble("lat"));
                coord.setLon(jsonObject.getJSONObject("coord").getDouble("lon"));
                weatherr.setId(jsonObject.getInt("id"));
                weatherr.setCoord(coord);
                weather weather = new weather();
                weather.setId(jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id"));
                weather.setMain(jsonObject.getJSONArray("weather").getJSONObject(0).getString("main"));
                weather.setDescription(jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
                weather.setIcon(jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon"));
                weatherr.setBase(jsonObject.getString("base"));
                main main = new main();
                main.setTemp(jsonObject.getJSONObject("main").getDouble("temp"));
                main.setFeels_like(jsonObject.getJSONObject("main").getDouble("feels_like"));
                main.setTemp_min(jsonObject.getJSONObject("main").getDouble("temp_min"));
                main.setTemp_max(jsonObject.getJSONObject("main").getDouble("temp_max"));
                main.setPressure(jsonObject.getJSONObject("main").getInt("pressure"));
                main.setHumidity(jsonObject.getJSONObject("main").getInt("humidity"));
                weatherr.setVisibility(jsonObject.getInt("visibility"));
                Wind wind = new Wind();
                wind.setSpeed(jsonObject.getJSONObject("wind").getInt("speed"));
                wind.setDeg(jsonObject.getJSONObject("wind").getInt("deg"));
                Clouds clouds = new Clouds();
                clouds.setAll(jsonObject.getJSONObject("clouds").getInt("all"));
                weatherr.setClouds(clouds);
                weatherr.setWind(wind);
                weatherr.setDt(jsonObject.getLong("dt"));
                sys sys = new sys();
                icon.setBackgroundResource(R.drawable.lah);
                Picasso.get().load("http://openweathermap.org/img/wn/" + jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon") + "@4x.png").into(iconView);


//                sys.setType(jsonObject.getJSONObject("sys").getInt("type"));
//                sys.setId(jsonObject.getJSONObject("sys").getInt("id"));
                sys.setCountry(jsonObject.getJSONObject("sys").getString("country"));
                sys.setSunrise(jsonObject.getJSONObject("sys").getLong("sunrise"));
                sys.setSunset(jsonObject.getJSONObject("sys").getLong("sunset"));
                Locale locale = new Locale("fa", "IR");
                DateFormat df = DateFormat.getTimeInstance(0, locale);
                String sunriseDate = df.format(new Date(sys.getSunrise()));
                String sunsetDate = df.format(new Date(sys.getSunset()));
                String updateD = df.format(new Date(weather.getLastupdate()));
                weatherr.setSys(sys);
                weatherr.setWeather(weather);
                weatherr.setMain(main);
                weatherr.setTimezone(jsonObject.getInt("timezone"));
                weatherr.setName(jsonObject.getString("name"));
                weatherr.setCod(jsonObject.getInt("cod"));
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                String tempFormat = decimalFormat.format(main.getTemp());
                cityName.setText(weatherr.getName() + "," + sys.getCountry());
                temp.setText("" + tempFormat + "°C");
                windd.setText("سرعت باد : " + wind.getSpeed());
                description.setText("وضعیت هوا : " + weather.getDescription());
                updated.setText("آخرین آپدیت : " + updateD);
                if (weather.getDescription().equals("clear sky")) {
                    relativeLayout.setBackgroundResource(R.drawable.clear_sky);
                } else if (weather.getDescription().equals("few clouds")) {
                    relativeLayout.setBackgroundResource(R.drawable.few_clouds);
                } else if (weather.getDescription().equals("scattered clouds")) {
                    relativeLayout.setBackgroundResource(R.drawable.scattered_clouds);
                } else if (weather.getDescription().equals("broken clouds")) {
                    relativeLayout.setBackgroundResource(R.drawable.broken_clouds);
                } else if (weather.getDescription().equals("shower rain")) {
                    relativeLayout.setBackgroundResource(R.drawable.rain);
                } else if (weather.getDescription().equals("rain")) {
                    relativeLayout.setBackgroundResource(R.drawable.rain);
                } else if (weather.getDescription().equals("thunderstorm")) {
                    relativeLayout.setBackgroundResource(R.drawable.thunderstorm);
                } else if (weather.getDescription().equals("snow")) {
                    relativeLayout.setBackgroundResource(R.drawable.snow);
                } else if (weather.getDescription().equals("mist")) {
                    relativeLayout.setBackgroundResource(R.drawable.mist);
                }
                pressure.setText("فشار : " + main.getPressure() + "hPa");
                humidity.setText("رطوبت : " + "%" + main.getHumidity());
                sunrise.setText("طلوع خورشید : " + sunriseDate);
                sunset.setText("غروب خورشید : " + sunsetDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("TAG", error.toString());
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}