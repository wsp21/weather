package com.example.weather;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class thirdActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btn_back;
    private Button btn_search;
    private EditText editText1;
    private DatabaseHelper helper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third);

        bindView();
    }


    private void bindView() {
        btn_back=findViewById(R.id.back);
        btn_search=findViewById(R.id.search);
        editText1=findViewById(R.id.sv_city);

        editText1.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    private void toback() {
        Intent intent= new Intent();
        intent.setClass(this,secondActivity.class);
        startActivity(intent);
    }

    //获取天气信息
    //请求的API，详细参考https://lbs.amap.com/api/webservice/guide/api/weatherinfo/
    private String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=f284fac506721e5b7903bdb011afc2f8";
    //使用OkHttpClient进行网络请求
    private OkHttpClient httpClient = new OkHttpClient();
    //存储解析json字符串得到的天气信息
    private void getWeather(String no) {
        String newUrl = url + "&city=" + no;
        final Request request = new Request.Builder().url(newUrl).get().build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = httpClient.newCall(request).execute();
                    //请求成功
                    if (response.isSuccessful()) {
                        String result = response.body().string();

                        Log.d("服务器返回的结果:", result);

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray infoback = jsonObject.getJSONArray("lives");

                        String weatherstate=infoback.getString(0);
                        Log.d("weatherstate:", weatherstate);

                        JSONObject info = infoback.getJSONObject(0);

                            String province=info.getString("province");
                            Log.d("province:", province);

                            String city=info.getString("city");
                            Log.d("city:", city);

                            String adcode=info.getString("adcode");
                            Log.d("adcode:", adcode);

                            String weather=info.getString("weather");
                            Log.d("weather:", weather);

                            String temperature=info.getString("temperature");
                            Log.d("temperature:", temperature);

                            String winddirection=info.getString("winddirection");
                            Log.d("winddirection:", winddirection);

                            String windpower=info.getString("windpower");
                            Log.d("windpower:", windpower);

                            String humidity=info.getString("humidity");
                            Log.d("humidity:", humidity);

                            String reporttime=info.getString("reporttime");
                            Log.d("reporttime:", reporttime);
                        Intent intent= new Intent();
                        boolean flag=true;
                        intent.putExtra("flag",flag);
                        intent.putExtra("province",province);
                        intent.putExtra("city",city);
                        intent.putExtra("adcode",adcode);
                        intent.putExtra("weather",weather);
                        intent.putExtra("temperature",temperature);
                        intent.putExtra("winddirection",winddirection);
                        intent.putExtra("windpower",windpower);
                        intent.putExtra("humidity",humidity);
                        intent.putExtra("reporttime",reporttime);
                        intent.setClass(thirdActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Log.i("SearchWeather.java", "服务器异常:" + e.toString());
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                toback();
                break;
            case R.id.search:
                //得到输入信息
                String no=editText1.getText().toString();
                //返回信息显示界面
                getWeather(no);

                break;
        }
    }
}
