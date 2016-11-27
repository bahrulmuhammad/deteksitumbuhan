package com.example.bebenay.deteksitumbuhan.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bebenay.deteksitumbuhan.Control.DeteksiTepi;
import com.example.bebenay.deteksitumbuhan.Control.EkstraksiFitur;
import com.example.bebenay.deteksitumbuhan.Control.GrayScale;
import com.example.bebenay.deteksitumbuhan.Control.Treshold;
import com.example.bebenay.deteksitumbuhan.R;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by bebe on 12/3/2015.
 */
public class IdentifikasiActivity extends AppCompatActivity {
    static  final int TAKE_PICTURE = 1, IMAGE_PICK = 79;
    private  String imagePath;
    ImageView gambar;
    Button btnIdentifikasi;
    Bitmap bmpImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identifikasi_activity);
        imagePath = this.getIntent().getStringExtra("image_path");


        btnIdentifikasi = (Button) findViewById(R.id.btnIdentifikasi);
        gambar = (ImageView) findViewById(R.id.gambar);
        tampilkanGambar();

        btnIdentifikasi.setOnClickListener(onIdentifikasi);
    }



    private View.OnClickListener onIdentifikasi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            proses();
        }
    };

    public boolean onCreateOptionMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }





    public void tampilkanGambar() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream is = new FileInputStream(imagePath);
            BitmapFactory.decodeStream(is, null, options);
            is.close();
            is  = new FileInputStream(imagePath);
            options.inSampleSize = Math.max(options.outWidth / 460, options.outHeight / 288);
            Bitmap bmp = BitmapFactory.decodeStream(is, null, options);
            is.close();
            bmpImage = Bitmap.createBitmap(bmp);
            gambar.setImageBitmap(bmp);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    public void proses() {
        GrayScale gs = new GrayScale(bmpImage);
        gs.RGBtoGray();
        Treshold tr = new Treshold(gs.hasilGambar);
        tr.GrayToBinary();
        DeteksiTepi dt = new DeteksiTepi(tr.hasil);
        dt.inboundTracing();
        EkstraksiFitur ef = new EkstraksiFitur(tr.hasil, dt.konturX, dt.konturY);
        ef.grahamScan();
        ef.konveksitas();
        ef.soliditas();
        Toast.makeText(this, String.valueOf("konveksitas = "+ef.konveksitas), Toast.LENGTH_LONG).show();
        Toast.makeText(this, String.valueOf("soliditas = "+ef.soliditas), Toast.LENGTH_LONG).show();
        bmpImage = Bitmap.createBitmap(dt.hasilGambar);
        Toast.makeText(this, String.valueOf("convex hull : "+ef.convexhullX.size()),Toast.LENGTH_SHORT).show();

        Intent i = new Intent("com.example.HasilActivity");
        i.putExtra("konveksitas",String.valueOf(ef.konveksitas));
        i.putExtra("soliditas", String.valueOf(ef.soliditas));
        startActivity(i);

        gambar.setImageBitmap(bmpImage);
    }
}
