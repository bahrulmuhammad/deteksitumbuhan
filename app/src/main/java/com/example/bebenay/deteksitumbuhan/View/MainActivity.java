package com.example.bebenay.deteksitumbuhan.View;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.bebenay.deteksitumbuhan.Model.DBadapter;
import com.example.bebenay.deteksitumbuhan.R;


public class MainActivity extends AppCompatActivity {
    DBadapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBadapter(this);
        tambahTanaman();

        Button tombolKoleksi = (Button) findViewById(R.id.tombolKoleksi);
        Button tombolDeteksi = (Button) findViewById(R.id.tombolDeteksi);
        Button tombolKeluar = (Button) findViewById(R.id.tombolKeluar);

        tombolKoleksi.setOnClickListener(onKoleksi);
        tombolDeteksi.setOnClickListener(deteksi);
        tombolKeluar.setOnClickListener(onKeluar);
    }

    private View.OnClickListener onKoleksi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            i.setAction("com.example.KoleksiActivity");
            startActivity(i);
        }
    };

    private View.OnClickListener deteksi = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            i.setAction("com.example.KameraActivity");
            startActivity(i);
        }
    };

    private View.OnClickListener onKeluar = new View.OnClickListener(){
      public void onClick(View v) {
            System.exit(0);
      }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tambahTanaman() {
        db.open();

        db.insertPlant("kamboja", "1.0585", "0.9999");
        db.insertPlant("cocor bebek", "1.0094", "0.9827");
        db.insertPlant("euphorbia", "1.0232", "0.9968");
        db.insertPlant("philo eceng", "1.0217", "0.9914");
        db.insertPlant("zamia kulkas", "1.0553", "0.9827");
        db.close();
    }
}
