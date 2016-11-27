package com.example.bebenay.deteksitumbuhan.Control;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by bebe on 11/8/2015.
 */
public class Treshold {
    public Bitmap hasil;
    int baris, kolom, mean;

    public Treshold(Bitmap src) {
        hasil = Bitmap.createBitmap(src);
        baris = src.getHeight();
        kolom = src.getWidth();
    }



    private int GetMean() {
        int jumlahPiksel = 0;
        int piksel = 0, sample =0;
        for (short i = 0; i < kolom - 1; i++) {
            for (short j = 0; j < baris - 1; j++) {
                piksel = hasil.getPixel(i,j);
                sample = (piksel >> 8) & 0xff;
                jumlahPiksel += sample;
            }
        }

        mean = jumlahPiksel / (kolom * baris);
        return mean;
    }

    private void Convert() {
        int piksel = 0, sample = 0, ambang = 250;
        for (short i = 0; i < kolom - 1; i++) {
            for (short j = 0; j < baris - 1; j++) {
                piksel = hasil.getPixel(i,j);
                sample = (piksel >> 8) & 0xff;
                if (sample <=  mean) {
                    hasil.setPixel(i, j, 0xffffffff);
                } else {
                    hasil.setPixel(i, j, 0xff000000);
                }
            }
        }
    }

    public void GrayToBinary() {
        GetMean();
        Convert();
    }
}
