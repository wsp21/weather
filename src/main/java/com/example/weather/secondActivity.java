package com.example.weather;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class secondActivity extends AppCompatActivity implements View.OnClickListener  {
    private String TAG="secondActivity";
    private ImageButton btn_back,btn_addButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        bindView();

        //创建数据库
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        //链接listView
        ListView listView = (ListView)findViewById(R.id.list1);

        //创建存贮容器
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        //循环显示布局信息.
        String sql="select * from "+DatabaseHelper.DB_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext())
        {
            Map<String, String> listem = new HashMap<String, String>();
            String city_=cursor.getString(cursor.getColumnIndex("city_"));
            String temperature_=cursor.getString(cursor.getColumnIndex("temperature_"));
            String weather_=cursor.getString(cursor.getColumnIndex("weather_"));
            listem.put("city_",city_);
            listem.put("temperature_",temperature_);
            listem.put("weather_",weather_);
            list.add(listem);
        }
        MyAdapter adapter =new MyAdapter(this,list);
        listView.setAdapter(adapter);

        //点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position=position+1;
                Log.d(TAG, "点击！"+position+"行");
                change(position);
            }
        });
        db.close();
    }

    private void bindView() {
        btn_back=findViewById(R.id.back);
        btn_addButton=findViewById(R.id.addButton);

        btn_back.setOnClickListener(this);
        btn_addButton.setOnClickListener(this);

    }

    public void toback() {
        Intent intent= new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
    }

    private void add() {
        Intent intent= new Intent();
        intent.setClass(this,thirdActivity.class);
        startActivity(intent);
    }

    public int change(int position) {
        Intent intent= new Intent();
        intent.putExtra("position",position);
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                toback();
                break;
            case R.id.addButton:
                add();
                break;
        }
    }




}
