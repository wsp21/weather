package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHelper helper;
    private Spinner spinner;
    private ImageButton cityicon;
    private int position=-1;
    private TextView province_,city_,adcode_,weather_,temperature_,winddirection_,windpower_,humidity_,reporttime_;
    private boolean flag1=false;
    private static String data[] = {"","刷新", "关注"};
    public String province,city,adcode,weather,temperature,winddirection,windpower,humidity,reporttime;

    //连接api
    private String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=f284fac506721e5b7903bdb011afc2f8";
    private OkHttpClient httpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //创建数据库
        helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        init();
        Intent intent=getIntent();
        position=intent.getIntExtra("position",-1);
        flag1=intent.getBooleanExtra("flag",false);
        if(flag1){
            setsearch();
        }
        else if (!flag1&&position!=-1){
           searchsql(position);
        }
        else{
            defalut();
        }


        //第二个参数是显示数据使用的布局文件的id
        //第三个参数是显示每个数据使用的布局中的TextView的 id
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, data);
        spinner.setAdapter(adapter);
        //让第一个数据项已经被选中
        spinner.setSelection(0, true);

        //给Spinner添加事件监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //当选中某一个数据项时触发该方法
            /*
             * parent接收的是被选择的数据项所属的 Spinner对象，
             * view参数接收的是显示被选择的数据项的TextView对象
             * position接收的是被选择的数据项在适配器中的位置
             * id被选择的数据项的行号
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println(spinner==parent);//true
                //System.out.println(view);
                //String data = adapter.getItem(position);//从适配器中获取被选择的数据项
                //String data = list.get(position);//从集合中获取被选择的数据项
                String data = (String)spinner.getItemAtPosition(position);//从spinner中获取被选择的数据

                if(data.equals("刷新"))
                {
                    String s=city_.getText().toString();
                    update(s);
                    String weather=weather_.getText().toString();
                    String temperature=temperature_.getText().toString();
                    String winddirection=winddirection_.getText().toString();
                    String windpower=windpower_.getText().toString();
                    String humidity=humidity_.getText().toString();
                    String reporttime=reporttime_.getText().toString();

                    SQLiteDatabase db = helper.getWritableDatabase();
                    String sql="update "+DatabaseHelper.DB_NAME+ " set weather_ = '"+ weather + "',temperature_ = '"+ temperature + "',winddirection_ = '"+ winddirection + "',windpower_ = '"+ windpower +"',humidity_ = '"+ humidity + "',reporttime_ = '"+ reporttime + "' where city_ = '"+s+"';";
                    db.execSQL(sql);
                    db.close();
                    Toast.makeText(MainActivity.this, "更新成功!", Toast.LENGTH_SHORT).show();
                }
                else if(data.equals("关注")){
                    String s=city_.getText().toString();
                    guanzhu(s);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void searchsql(int position) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql="select * from "+DatabaseHelper.DB_NAME+" where _id = "+position;
        Cursor cursor = db.rawQuery(sql,null);
        while(cursor.moveToNext()) {
            String province = cursor.getString(cursor.getColumnIndex("province_"));
            String city = cursor.getString(cursor.getColumnIndex("city_"));
            String adcode = cursor.getString(cursor.getColumnIndex("adcode_"));
            String weather = cursor.getString(cursor.getColumnIndex("weather_"));
            String temperature = cursor.getString(cursor.getColumnIndex("temperature_"));
            String winddirection = cursor.getString(cursor.getColumnIndex("winddirection_"));
            String windpower = cursor.getString(cursor.getColumnIndex("windpower_"));
            String humidity = cursor.getString(cursor.getColumnIndex("humidity_"));
            String reporttime = cursor.getString(cursor.getColumnIndex("reporttime_"));
            province_.setText(province);
            city_.setText(city);
            adcode_.setText(adcode);
            weather_.setText(weather);
            temperature_.setText(temperature);
            winddirection_.setText(winddirection);
            windpower_.setText(windpower);
            humidity_.setText(humidity);
            reporttime_.setText(reporttime);
        }
        cursor.close();
        db.close();
    }

    private void guanzhu(String s) {
        boolean flag=false;
        String sql="select * from "+DatabaseHelper.DB_NAME;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext())
        {
            Map<String, String> listem = new HashMap<String, String>();
            String city_=cursor.getString(cursor.getColumnIndex("city_"));
            if(s.equals(city_))
            {
                Toast.makeText(MainActivity.this, "已经关注!", Toast.LENGTH_SHORT).show();
                flag=true;
            }
        }
        if(!flag){
            insert();
            Toast.makeText(MainActivity.this, "关注成功！", Toast.LENGTH_SHORT).show();
        }
    }

    private void setsearch() {
        Intent intent=getIntent();
        flag1=intent.getBooleanExtra("flag",false);
        if(flag1)
        {
            province=intent.getStringExtra("province");
            city=intent.getStringExtra("city");
            adcode=intent.getStringExtra("adcode");
            weather=intent.getStringExtra("weather");
            temperature=intent.getStringExtra("temperature");
            winddirection=intent.getStringExtra("winddirection");
            windpower=intent.getStringExtra("windpower");
            humidity=intent.getStringExtra("humidity");
            reporttime=intent.getStringExtra("reporttime");

            province_.setText(province);
            city_.setText(city);
            adcode_.setText(adcode);
            weather_.setText(weather);
            temperature_.setText(temperature);
            winddirection_.setText(winddirection);
            windpower_.setText(windpower);
            humidity_.setText(humidity);
            reporttime_.setText(reporttime);
        }
    }

    private void defalut( ) {
        //获取天气信息
        //请求的API，详细参考https://lbs.amap.com/api/webservice/guide/api/weatherinfo/
        //使用OkHttpClient进行网络请求
        String newUrl = url + "&city=朝阳区";
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

                        String weatherstate = infoback.getString(0);
                        Log.d("weatherstate:", weatherstate);

                        JSONObject info = infoback.getJSONObject(0);

                        String province = info.getString("province");
                        Log.d("province:", province);

                        String city = info.getString("city");
                        Log.d("city:", city);

                        String adcode = info.getString("adcode");
                        Log.d("adcode:", adcode);

                        String weather = info.getString("weather");
                        Log.d("weather:", weather);

                        String temperature = info.getString("temperature");
                        Log.d("temperature:", temperature);

                        String winddirection = info.getString("winddirection");
                        Log.d("winddirection:", winddirection);

                        String windpower = info.getString("windpower");
                        Log.d("windpower:", windpower);


                        String humidity = info.getString("humidity");
                        Log.d("humidity:", humidity);


                        String reporttime = info.getString("reporttime");
                        Log.d("reporttime:", reporttime);

                        province_.setText(province);
                        city_.setText(city);
                        adcode_.setText(adcode);
                        weather_.setText(weather);
                        temperature_.setText(temperature);
                        winddirection_.setText(winddirection);
                        windpower_.setText(windpower);
                        humidity_.setText(humidity);
                        reporttime_.setText(reporttime);
                    }
                } catch (Exception e) {
                    Log.i("SearchWeather.java", "服务器异常:" + e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void update(String s) {
        String newUrl = url + "&city=" + s;

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

                            String weatherstate = infoback.getString(0);
                            Log.d("weatherstate:", weatherstate);

                            JSONObject info = infoback.getJSONObject(0);

                            String province = info.getString("province");
                            Log.d("province:", province);

                            String city = info.getString("city");
                            Log.d("city:", city);

                            String adcode = info.getString("adcode");
                            Log.d("adcode:", adcode);

                            String weather = info.getString("weather");
                            Log.d("weather:", weather);

                            String temperature = info.getString("temperature");
                            Log.d("temperature:", temperature);

                            String winddirection = info.getString("winddirection");
                            Log.d("winddirection:", winddirection);

                            String windpower = info.getString("windpower");
                            Log.d("windpower:", windpower);


                            String humidity = info.getString("humidity");
                            Log.d("humidity:", humidity);


                            String reporttime = info.getString("reporttime");
                            Log.d("reporttime:", reporttime);

                            province_.setText(province);
                            city_.setText(city);
                            adcode_.setText(adcode);
                            weather_.setText(weather);
                            temperature_.setText(temperature);
                            winddirection_.setText(winddirection);
                            windpower_.setText(windpower);
                            humidity_.setText(humidity);
                            reporttime_.setText(reporttime);
                        }
                    } catch (Exception e) {
                        Log.i("SearchWeather.java", "服务器异常:" + e.toString());
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    public void insert() {
        String province_sv=province_.getText().toString();
        String city_sv=city_.getText().toString();
        String adcode_sv=adcode_.getText().toString();
        String weather_sv=weather_.getText().toString();
        String temperature_sv=temperature_.getText().toString();
        String winddirection_sv=winddirection_.getText().toString();
        String windpower_sv=windpower_.getText().toString();
        String humidity_sv=humidity_.getText().toString();
        String reporttime_sv=reporttime_.getText().toString();

        //打开写入操作
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql="insert into "+DatabaseHelper.DB_NAME+"(province_,city_,adcode_,weather_,temperature_,winddirection_,windpower_,humidity_,reporttime_) values(?,?,?,?,?,?,?,?,?)";
        db.execSQL(sql,new Object[]{province_sv,city_sv,adcode_sv,weather_sv,temperature_sv,winddirection_sv,windpower_sv,humidity_sv,reporttime_sv});
        db.close();
    }

    private void init() {
        spinner = (Spinner) findViewById(R.id.select);
        cityicon=(ImageButton)findViewById(R.id.cityicon);

        province_=findViewById(R.id.province);
        city_=findViewById(R.id.city_);
        adcode_=findViewById(R.id.adcode_);
        weather_=findViewById(R.id.weather_);
        temperature_=findViewById(R.id.temperature_);
        winddirection_=findViewById(R.id.winddirection_);
        windpower_=findViewById(R.id.windpower_);
        humidity_=findViewById(R.id.humidity_);
        reporttime_=findViewById(R.id.reporttime);

        cityicon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        flag1=false;
        Intent intent= new Intent();
        intent.setClass(this,secondActivity.class);
        startActivity(intent);
    }

}
