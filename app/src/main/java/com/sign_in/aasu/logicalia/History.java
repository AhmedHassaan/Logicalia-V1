package com.sign_in.aasu.logicalia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class History extends AppCompatActivity {
    ArrayList<String> history;
    ArrayAdapter<String> adapter;
    ListView list;
    HistoryData historyData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        list = (ListView)findViewById(R.id.list);
        list.setClickable(false);
        historyData = new HistoryData(this);
        history = new ArrayList<>();
        history = historyData.getAll();
        Collections.reverse(history);
        adapter = new ArrayAdapter<String>(this,R.layout.oneitem,R.id.one,history);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.history_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.clear){
            clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clear(){
        historyData.clearAll();
        history.clear();
        adapter.notifyDataSetChanged();
    }
}
