package com.example.bebenay.deteksitumbuhan.View;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bebenay.deteksitumbuhan.Control.Distance;
import com.example.bebenay.deteksitumbuhan.Control.Fitur;
import com.example.bebenay.deteksitumbuhan.Model.DBadapter;
import com.example.bebenay.deteksitumbuhan.R;

import java.text.DecimalFormat;
import java.util.Vector;

public class HasilActivity extends AppCompatActivity {
    DBadapter db;

    Vector<String> jenisTanaman = new Vector<>();
    Fitur inputFitur = new Fitur();
    Vector<Fitur> fitur = new Vector<>();
    double [] jarak;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);

        db = new DBadapter(this);

        db.open();
        Cursor c = db.getAllPlant();

        while (c.moveToNext()) {
            Fitur fi = new Fitur();
            fi.setKonveksitas(Double.parseDouble(c.getString(2)));
            fi.setSoliditas(Double.parseDouble(c.getString(3)));
            jenisTanaman.addElement(c.getString(1));
            fitur.addElement(fi);
        }
        db.close();

        jarak = new double[fitur.size()];

        inputFitur.setKonveksitas(Double.parseDouble(this.getIntent().getStringExtra("konveksitas")));
        inputFitur.setSoliditas(Double.parseDouble(this.getIntent().getStringExtra("soliditas")));

        proses();

        ListView listHasil = (ListView) findViewById(R.id.listJenis);
        listHasil.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, jenisTanaman));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hasil, menu);
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

    void proses() {
        for (short i = 0; i < fitur.size(); i++) {
            Vector<Double> target = new Vector<>();
            Vector<Double> input = new Vector<>();
            target.add(0, fitur.get(i).getKonveksitas());
            target.add(1, fitur.get(i).getSoliditas());
            input.add(0, inputFitur.getKonveksitas());
            input.add(1, inputFitur.getSoliditas());
            Distance dn = new Distance(input, target);
            dn.Euclidean();
            jarak[i] = dn.jarak;
        }

         for (short j = 0; j < jarak.length - 1; j++) {
            for (short k = 1; k < jarak.length; k++) {
                if (jarak[k] < jarak[j]) {
                    double temp = jarak[k];
                    jarak[k] = jarak[j];
                    jarak[j] = temp;

                    String temp0;
                    temp0 = jenisTanaman.get(k);
                    jenisTanaman.set(k, jenisTanaman.get(j));
                    jenisTanaman.set(j, temp0);
                }
            }
        }

    }

    public void ambilTanaman() {
        db.open();
        db.getAllPlant();
    }
}
