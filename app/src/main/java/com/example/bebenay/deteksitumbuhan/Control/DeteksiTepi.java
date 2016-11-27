package com.example.bebenay.deteksitumbuhan.Control;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.List;
import java.util.Vector;

/**
 * Created by bebe on 11/24/2015.
 */
public class DeteksiTepi {
    public Bitmap gambar, hasilGambar;
    int baris, kolom;
    short dx = 0, dy = 0;
    public Vector<Short> konturX  = new Vector<>();
    public Vector<Short> konturY = new Vector<>();

    public DeteksiTepi(Bitmap src) {
        gambar = Bitmap.createBitmap(src);
        baris = src.getHeight();
        kolom = src.getWidth();
        hasilGambar = Bitmap.createBitmap(kolom, baris, gambar.getConfig());
    }

    public void delapanKetetanggaan() {
        int piksel0, piksel1, piksel2, piksel3, piksel4, piksel5, piksel6, piksel7;
        for (short i = 2; i < baris - 1; i++) {
            for (short j = 2; j < kolom - 1; j++) {
                piksel0 = gambar.getPixel(j + 1, i);
                piksel1 = gambar.getPixel(j + 1, i - 1);
                piksel2 = gambar.getPixel(j, i - 1);
                piksel3 = gambar.getPixel(j - 1, i - 1);
                piksel4 = gambar.getPixel(j - 1, i);
                piksel5 = gambar.getPixel(j - 1, i + 1);
                piksel6 = gambar.getPixel(j, i + 1);
                piksel7 = gambar.getPixel(j + 1, i + 1);
                int sigma = piksel0 + piksel1 + piksel2 + piksel3 + piksel4 + piksel5 + piksel6 + piksel7;
                if (sigma == -8) {
                    hasilGambar.setPixel(i, j, 0xff000000);
                } else {
                    hasilGambar.setPixel(i, j, gambar.getPixel(j, i));
                }
            }
        }
    }

    public void inboundTracing() {
        boolean selesai = false;
        short b0x = 0, b0y = 0, b1y = 0, b1x = 0, c1 = 0, bx = 0, by = 0;
        for (short y = 1; y < baris; y++) {
            for (short x = 1; x < kolom; x++) {
                if (gambar.getPixel(x, y) == 0xffffffff) {
                    b0y = y;
                    b0x = x;

                    selesai = true;
                    break;
                }
            }

            if (selesai) {
                break;
            }
        }

        short c0 = 4;

        for (short i = 1; i <= 8; i++) {
            deltaPiksel(c0);
            if (gambar.getPixel(b0x + dx, b0y + dy) == 0xffffffff) {
                b1y = (short) (b0y + dy);
                b1x = (short) (b0x + dx);
                c1 = sebelum(c0);
                break;
            } else {
                c0 = berikut(c0);
            }
        }

        konturX.add(0, b0x);
        konturY.add(0, b0y);
        konturX.add(1, b1x);
        konturY.add(1, b1y);

        short n = 1;

        bx = b1x;
        by = b1y;
        short c = c1;

        while (true) {
            for (short p = 1; p <= 8; p++) {
                deltaPiksel(c);
                if (gambar.getPixel(bx + dx, by + dy) == 0xffffffff) {
                    by = (short) (by + dy);
                    bx = (short) (bx + dx);

                    c = sebelum(c);

                    n = (short) (n + 1);

                    konturX.add(n,bx);
                    konturY.add(n,by);
                    break;
                } else {
                    c = berikut(c);
                }
            }

            if ((by == b0y) && (bx == b0x)) {
                break;
            }
        }

        for (short y = 0 ; y < baris; y++) {
            for (short x = 0; x < kolom; x++) {
                hasilGambar.setPixel(x, y, 0xff000000);
            }
        }

        for (short i = 0; i < konturX.size(); i++) {
            hasilGambar.setPixel(konturX.get(i),konturY.get(i), 0xffffffff);
        }
    }

    private short berikut(short x) {
        short b;
        if (x == 0) {
            b = 7;
        } else {
            b = (short) (x - 1);
        }

        return b;
    }

    private short sebelum(short x) {
        short s;
        if (x == 7) {
            s = 0;
        } else {
            s = (short) (x + 1);
        }

        if (s < 2) {
            s = 2;
        } else if (s < 4) {
            s = 4;
        } else if (s < 6) {
            s = 6;
        } else {
            s = 0;
        }

        return s;
    }

    private void deltaPiksel(short id) {

        if (id == 0) {
            dx = 1;
            dy = 0;
        } else if (id == 1) {
            dx = 1;
            dy = -1;
        } else if (id == 2) {
            dx = 0;
            dy = -1;
        } else if (id == 3) {
            dx = -1;
            dy = -1;
        } else if (id == 4) {
            dx = -1;
            dy = 0;
        } else if (id == 5) {
            dx = -1;
            dy = 1;
        } else if (id == 6) {
            dx = 0;
            dy = 1;
        } else if (id == 7) {
            dx = 1;
            dy = 1;
        }
    }
}
