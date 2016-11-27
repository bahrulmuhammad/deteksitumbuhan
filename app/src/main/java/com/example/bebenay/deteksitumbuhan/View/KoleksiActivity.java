package com.example.bebenay.deteksitumbuhan.View;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bebenay.deteksitumbuhan.Model.DBadapter;
import com.example.bebenay.deteksitumbuhan.R;

/**
 * Created by bebenay on 9/23/2015.
 */
public class KoleksiActivity extends AppCompatActivity {
    DBadapter db;
    String listtumbuhan [];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.koleksi_activity);

        db = new DBadapter(this);
        db.open();
        Cursor c = db.getAllPlant();
        short i = 0;
        listtumbuhan = new String[c.getCount()];
        while (c.moveToNext()) {
            listtumbuhan[i] = c.getString(1);
            i++;
        }
        db.close();

        ListView list = (ListView) findViewById(R.id.tumbuhan);
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listtumbuhan));
    }
}
