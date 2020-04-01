package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    MenuItem menuItem;
    BottomNavigationView bottomNavigationView;
    public static SQLiteDatabase db;
//    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button btnoutLogin = findViewById(R.id.btoutlogin);
        btnoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        db = openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);

        //db.execSQL("DROP TABLE IF EXISTS history");
        //创建history表
        //db.execSQL("CREATE TABLE history (city VARCHAR , date VARCHAR , weather VARCHAR, top VARCHAR, low  VARCHAR, fengli VARCHAR, fengxiang VARCHAR, tip VARCHAR, PRIMARY KEY(city, date) )");
        /*
        History history = new History();
        history.city = "杭州";
        history.date = "17日星期二";
        history.weather = "小雨";
        history.top = "高温19℃";
        history.low = "低温8℃";
        history.fengli = "<![CDATA[<3级]]>";
        history.fengxiang = "南风";
        history.tip = "天凉，昼夜温差较大，较易发生感冒，请适当增减衣服，体质较弱的朋友请注意适当防护。";
        //ContentValues以键值对的形式存放数据
        ContentValues cv = new ContentValues();
        cv.put("city", history.city);
        cv.put("date", history.date);
        cv.put("weather", history.weather);
        cv.put("top", history.top);
        cv.put("low", history.low);
        cv.put("fengli", history.fengli);
        cv.put("fengxiang", history.fengxiang);
        cv.put("tip", history.tip);
        db.insert("history", null, cv);
        */


        //查詢
        Cursor c = db.rawQuery("SELECT * FROM history ", null);
        while(c.moveToNext()){
            String date = c.getString(c.getColumnIndex("date"));
            Log.i("db", date);
        }

//        //fragment之间数据传递
//        MapFragment mapFragment = new MapFragment();
//        TodayFragment todayFragment = new TodayFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_map,mapFragment,"mapFragment").commit();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_today,todayFragment,"todayFragment").commit();

        //定义
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.map:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.weather:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.history:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.settings:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.map);//设置默认在地图界面
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private Fragment[] mFragments = new Fragment[]{new MapFragment(), new WeatherFragment(), new HistoryFragment(),new SettingsFragment()};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
