package com.sign_in.aasu.logicalia;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by Lenovo on 1/27/2017.
 */

public class HistoryData {
    Context context;
    private SharedPreferences.Editor set;
    private SharedPreferences get;
    String de = "N/A";
    public HistoryData(Context context) {
        this.context = context;
        get = context.getSharedPreferences("history",Context.MODE_PRIVATE);
        set = context.getSharedPreferences("history",context.MODE_PRIVATE).edit();
    }
    public void clearAll(){
        set.clear();
        set.putInt("count",0);
        set.commit();
    }

    public void putOne(String s){
        int count = get.getInt("count",0);
        String countStr = Integer.toString(count);
        set.putString(countStr,s);
        count++;
        set.putInt("count",count);
        set.commit();
    }

    public ArrayList<String> getAll(){
        ArrayList<String> history = new ArrayList<>();
        int count = get.getInt("count",0);
        for(int i=0;i<count;i++){
            String countStr = Integer.toString(i);
            history.add(get.getString(countStr,de));
        }
        return history;
    }
}
