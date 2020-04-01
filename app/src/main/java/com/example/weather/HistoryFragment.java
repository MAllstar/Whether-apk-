package com.example.weather;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View historyLayout = inflater.inflate(R.layout.fragment_history, container, false);
        TextView tv = historyLayout.findViewById(R.id.His_city);

        tv.setText("杭州");
        return historyLayout;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView HistoryList1 = view.findViewById(R.id.HistoryList1);
        ListView HistoryList2 = view.findViewById(R.id.HistoryList2);
        ListView HistoryList3 = view.findViewById(R.id.HistoryList3);


        //设置适配器并给listView
        String[] strElement = {"icon", "title", "content"};
        String[] strTitle = {"日期", "天气", "最高气温", "最低气温", "风力", "风向"};
        String[][] strContent = new String[3][6];
        int i = 0;



        //数据库对象
        SQLiteDatabase db = MainActivity.db;
        //查詢
        Cursor c = db.rawQuery("SELECT * FROM history ", null);
        while(c.moveToNext()){   //按行遍历

            strContent[i][0] = c.getString(c.getColumnIndex("date"));
            strContent[i][1] = c.getString(c.getColumnIndex("weather"));
            strContent[i][2] = c.getString(c.getColumnIndex("top"));
            strContent[i][3] = c.getString(c.getColumnIndex("low"));
            strContent[i][4] = c.getString(c.getColumnIndex("fengli"));
            strContent[i][5]  = c.getString(c.getColumnIndex("fengxiang"));
            i++;

            Log.i("db", "历史读取成功");
        }
        //Log.i("db",strContent[1][2]);
        int[] icnoSet = {
                R.drawable.ic_listview_weather,
                R.drawable.ic_listview_weather,
                R.drawable.ic_listview_weather,
                R.drawable.ic_listview_weather,
                R.drawable.ic_listview_weather,
                R.drawable.ic_listview_weather,
        };
        //1
        List<Map<String,Object>> lists1 = new ArrayList<>();
        for(int j = 0; j < strTitle.length; j++){
            Map<String,Object> list = new HashMap<>();
            list.put(strElement[0],icnoSet[j]);
            list.put(strElement[1],strTitle[j]);
            list.put(strElement[2],strContent[0][j]);
            lists1.add(list);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),lists1,R.layout.today_listview,strElement,
                new int[]{R.id.lv_image,R.id.lv_title,R.id.lv_content});
        HistoryList1.setAdapter(simpleAdapter);
        //2
        List<Map<String,Object>> lists2 = new ArrayList<>();
        for(int j = 0; j < strTitle.length; j++){
            Map<String,Object> list = new HashMap<>();
            list.put(strElement[0],icnoSet[j]);
            list.put(strElement[1],strTitle[j]);
            list.put(strElement[2],strContent[1][j]);
            lists2.add(list);
        }
        simpleAdapter = new SimpleAdapter(getActivity(),lists2,R.layout.today_listview,strElement,
                new int[]{R.id.lv_image,R.id.lv_title,R.id.lv_content});
        HistoryList2.setAdapter(simpleAdapter);
        //3
        List<Map<String,Object>> lists3 = new ArrayList<>();
        for(int j = 0; j < strTitle.length; j++){
            Map<String,Object> list = new HashMap<>();
            list.put(strElement[0],icnoSet[j]);
            list.put(strElement[1],strTitle[j]);
            list.put(strElement[2],strContent[2][j]);
            lists3.add(list);
        }
        simpleAdapter = new SimpleAdapter(getActivity(),lists3,R.layout.today_listview,strElement,
                new int[]{R.id.lv_image,R.id.lv_title,R.id.lv_content});
        HistoryList3.setAdapter(simpleAdapter);


    }
}
