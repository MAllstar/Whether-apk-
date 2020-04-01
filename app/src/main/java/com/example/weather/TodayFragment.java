package com.example.weather;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {
    ListView listView;
    Button btn_refresh;
    Handler handler;
    String city;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        listView = view.findViewById(R.id.listView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView textView = getActivity().findViewById(R.id.presentlocation);
        Log.i("123456789",textView.getText().toString());
        city = textView.getText().toString();

        String url = "http://wthrcdn.etouch.cn/weather_mini?city="+city;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        //创建request对象
        Request request = new Request.Builder()
                .url(url)
                .build();
        okhttp3.Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("请求失败","请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String strJson = response.body().string();
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("strJson",strJson);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        btn_refresh = getView().findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .build();

                //获取数据
                TextView textView = getActivity().findViewById(R.id.presentlocation);
                city = textView.getText().toString();
                Log.i("12345678910",textView.getText().toString());
//                city = "上海";
                String url = "http://wthrcdn.etouch.cn/weather_mini?city="+city;
                //创建request对象
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                okhttp3.Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("请求失败","请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try{
                            String strJson = response.body().string();
                            Message msg = new Message();
                            Bundle data = new Bundle();
                            data.putString("strJson",strJson);
                            msg.setData(data);
                            handler.sendMessage(msg);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String strJson = bundle.getString("strJson");

                //解析josn数据
                try{
                    JSONObject jsonObject = new JSONObject(strJson);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = data.getJSONArray("forecast");
                    JSONObject yesterday = data.getJSONObject("yesterday");
                    JSONObject today = jsonArray.getJSONObject(0);
                    JSONObject theSecondDay = jsonArray.getJSONObject(1);
                    JSONObject theThirdDay = jsonArray.getJSONObject(2);
                    JSONObject theFourthDay = jsonArray.getJSONObject(3);
                    JSONObject theFifthDay = jsonArray.getJSONObject(4);



                    //ContentValues以键值对的形式存放数据
                    ContentValues cv = new ContentValues();
                    String yes_weather = yesterday.getString("type");
                    String yes_high = yesterday.getString("high");
                    String yes_low = yesterday.getString("low");
                    String yes_fl = yesterday.getString("fl");
                    String yes_fx = yesterday.getString("fx");
                    String yes_tip = data.getString("ganmao");

                    String date = yesterday.getString("date");
                    String city = data.getString("city");
                    String today_weather = today.getString("type");
                    String today_temperatureTop = today.getString("high");
                    String today_temperatureLow = today.getString("low");
                    String today_wind = today.getString("fengli");
                    String today_windDirection = today.getString("fengxiang");
                    String today_tip = data.getString("ganmao");

                    Log.i("1",today_weather);Log.i("2",today_temperatureTop);Log.i("3",today_temperatureLow);
                    Log.i("4",today_wind);Log.i("5",today_windDirection);Log.i("6",today_tip);

                    //数据库插入昨日信息
                    SQLiteDatabase db = MainActivity.db;
                    //查詢
                    Cursor c = db.query("history ", new String[] {"date"}, "date = ?",new String[] { date },null,null,null);
                    if (c.getCount() == 0){  //未找到数据，说明未插入
                        cv.put("city", city);
                        cv.put("date", date);
                        cv.put("weather", yes_weather);
                        cv.put("top", yes_high);
                        cv.put("low", yes_low);
                        cv.put("fengli", yes_fl);
                        cv.put("fengxiang", yes_fx);
                        cv.put("tip", yes_tip);
                        db.insert("history", null, cv);
                    }
                    else{
                        Log.i("db","数据已存在");
                    }

                    //db.close();
                    //设置适配器并给listView
                    String[] strElement = {"icon", "ttlie", "content"};
                    String[] strTitle = {"城市", "天气", "最高气温", "最低气温", "风力", "风向", "提醒"};
                    String[] strContent = {city,today_weather, today_temperatureTop, today_temperatureLow, today_wind, today_windDirection, today_tip};
                    int[] icnoSet = {
                            R.drawable.ic_launcher_foreground,
                            R.drawable.ic_launcher_foreground,
                            R.drawable.ic_launcher_foreground,
                            R.drawable.ic_launcher_foreground,
                            R.drawable.ic_launcher_foreground,
                            R.drawable.ic_launcher_foreground,
                            R.drawable.ic_launcher_foreground,
                    };
                    List<Map<String,Object>> lists = new ArrayList<>();
                    for(int i = 0; i < strTitle.length; i++){
                        Map<String,Object> list = new HashMap<>();
                        list.put(strElement[0],icnoSet[i]);
                        list.put(strElement[1],strTitle[i]);
                        list.put(strElement[2],strContent[i]);
                        lists.add(list);
                    }
                    SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),lists,R.layout.today_listview,strElement,
                            new int[]{R.id.lv_image,R.id.lv_title,R.id.lv_content});
                    listView.setAdapter(simpleAdapter);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
    }
}
