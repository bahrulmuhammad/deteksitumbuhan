package com.example.bebenay.deteksitumbuhan.Control;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bebe on 11/6/2015.
 */
public class GrayScale {

    public Bitmap gambar, hasilGambar;

    private final double GRAY_RED = 0.299, GRAY_GREEN = 0.587, GRAY_BLUE = 0.114;

    public GrayScale(Bitmap gambar) {
        this.gambar = Bitmap.createBitmap(gambar);
    }




    private void Convert() {
        hasilGambar = Bitmap.createBitmap(gambar.getWidth(), gambar.getHeight(), gambar.getConfig());

        int baris = gambar.getHeight();
        int kolom = gambar.getWidth();

        int A, R, G, B, piksel;

        for (short i = 0; i < kolom - 1; i++) {
            for (short j = 0; j < baris - 1; j++) {
                piksel = gambar.getPixel(i, j);

                A = piksel >>> 24;
                R = (piksel >> 16) & 0xff;
                G = (piksel >> 8) & 0xff;
                B = piksel & 0xff;

                R = G = B = (int)(R * GRAY_RED + G * GRAY_GREEN + B * GRAY_BLUE);
                hasilGambar.setPixel(i,j,Color.argb(A, R, G, B));
            }
        }


    }


    public void RGBtoGray() {
        Convert();
    }
}
