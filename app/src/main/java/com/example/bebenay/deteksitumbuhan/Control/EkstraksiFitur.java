package com.example.bebenay.deteksitumbuhan.Control;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by bebe on 11/25/2015.
 */
public class EkstraksiFitur {
    Bitmap gambar;
    int baris, kolom;
    Vector<Short> konturX = new Vector<>();
    Vector<Short> konturY = new Vector<>();
    Vector<Double> tandatanganKontur = new Vector<>();
    public Vector<Short> convexhullX = new Vector<>();
    public Vector<Short> convexhullY = new Vector<>();
    double sifatBundar;
    public double konveksitas;
    public double soliditas;


    public EkstraksiFitur(Bitmap src, Vector<Short> konturX, Vector<Short> konturY) {
        gambar = Bitmap.createBitmap(src);
        baris = gambar.getHeight();
        kolom = gambar.getWidth();
        this.konturX = konturX;
        this.konturY = konturY;
    }

    public void sifatBundar() {
        short pusatX, pusatY;
        short jumlah;
        double mu, sigma, total;

        Titik pusat = centroid();

        konturX.remove(konturX.size() - 1);
        konturY.remove(konturY.size() - 1);

        jumlah = (short) konturX.size();

        total = 0;
        for (short i = 0; i < jumlah; i++) {
            double pers1 = konturX.get(i) - pusat.x;
            double pers2 = konturY.get(i) - pusat.y;
            total = total + Math.sqrt((pers1 * pers1) + (pers2 * pers2));
        }

        mu = total / jumlah;

        total = 0;
        for (short i = 1; i < jumlah; i++) {
            double pers1 = konturX.get(i) - pusat.x;
            double pers2 = konturY.get(i) - pusat.y;
            double pers3 = Math.sqrt((pers1 * pers1) + (pers2 * pers2)) - mu;
            total = total + (pers3 * pers3);
        }

        sigma = total / jumlah;

        sifatBundar = mu / sigma;
    }

    public void tandatanganKontur() {
        short jumlah = (short) konturX.size();
        Titik pusat = centroid();
        double total = 0;

        for (short i = 0; i < jumlah; i++) {
            double pers1 = konturX.get(i) - pusat.x;
            double pers2 = konturY.get(i) - pusat.y;
            total = Math.sqrt((pers1 * pers1) + (pers2 * pers2));
            tandatanganKontur.add(i, total);
        }
    }

    public void grahamScan() {
        short jumlah = (short) konturX.size();
        short terkecil = 0;
        Vector<Piksel> piksel = new Vector<Piksel>();

        //mencari pivot
        for (short i = 1; i < jumlah; i++) {
            if (konturY.get(i) == konturY.get(terkecil)) {
                if (konturX.get(i) < konturX.get(terkecil)) {
                    terkecil = i;
                }
            } else if (konturY.get(i) < konturY.get(terkecil)) {
                terkecil = i;
            }
        }

        //susun data jarak dan sudut
        short indeks = 0;
        for (short i = 0; i < jumlah; i++) {
            if (i == terkecil)
                continue;

            Piksel p = new Piksel();
            p.x = konturX.get(i);
            p.y = konturY.get(i);
            p.sudut = sudut(new Titik(konturX.get(terkecil), konturY.get(terkecil)), new Titik(konturX.get(i), konturY.get(i)));
            p.jarak = jarak(new Titik(konturX.get(terkecil), konturY.get(terkecil)), new Titik(konturX.get(i), konturY.get(i)));
            piksel.add(indeks, p);
            indeks++;
        }

        short jum_piksel = (short) (indeks - 1);

        //pengurutan menurut sudut dan jarak
        for (short p = 1; p < jum_piksel; p++) {
            Piksel x = piksel.get(p);

            //sisipkan x ke dalam data [1 ... p-1]
            short q = (short) (p - 1);
            boolean ketemu = false;

            while ((q >= 1) && !ketemu) {
                if (x.sudut < piksel.get(p).sudut) {
                    piksel.set(q + 1, piksel.get(q));
                    q--;
                } else {
                    ketemu = true;
                }

                piksel.set(q + 1, x);
            }
        }

        piksel = unik(piksel);
        jum_piksel = (short) piksel.size();

        Vector<Short> hullX = new Vector<>();
        Vector<Short> hullY = new Vector<>();

        short top = 0;
        hullX.add(top, konturX.get(terkecil));
        hullY.add(top, konturY.get(terkecil));

        top++;
        hullX.add(top, piksel.get(0).x);
        hullY.add(top, piksel.get(0).y);

        short i = 1;
        while ( i < jum_piksel) {
            Titik titik = new Titik(piksel.get(i).x, piksel.get(i).y);

            //ambil dua data pertama
            Titik A = new Titik(hullX.get(top),hullY.get(top));
            Titik B = new Titik(hullX.get(top - 1), hullY.get(top - 1));

            if (berputarkeKanan(A,B, titik)) {
                top =(short) (top - 1);
            } else {
                top = (short) (top + 1);
                hullX.add(top,titik.x);
                hullY.add(top, titik.y);
                i++;
            }
        }

        Vector<Short> convexX = new Vector<>();
        Vector<Short> convexY = new Vector<>();

        indeks = 0;

        while (top != 0) {
            convexX.add(indeks, hullX.get(top));
            convexY.add(indeks, hullY.get(top));
            indeks++;
            top--;
        }
        indeks =(short) (indeks - 1);

        for (short j = 1; j <= convexX.size() ; j++) {
            convexhullX.add(j - 1, convexX.get(convexX.size() - j));
            convexhullY.add(j - 1, convexY.get(convexY.size() - j));
        }

    }

    public void konveksitas() {
        Vector<Short> X = convexhullX;
        Vector<Short> Y = convexhullY;
        X.add(X.size(), X.firstElement());
        Y.add(Y.size(), Y.firstElement());

        short perimeterObjek =(short) konturX.size();

        int perimeterKonveks = 0;
        for (short i = 1; i < X.size(); i++) {
            double pers1 = X.get(i) - X.get(i - 1);
            double pers2 = Y.get(i) - Y.get(i - 1);
            perimeterKonveks =(int) (perimeterKonveks + (Math.sqrt((pers1 * pers1) + (pers2 * pers2))));
        }

        konveksitas = (double) perimeterKonveks / perimeterObjek;
    }

    public void soliditas() {
        int luasObjek = 0;
        for (short x = 0; x < gambar.getWidth(); x++) {
            for (short y = 0; y < gambar.getHeight(); y++) {
                if (gambar.getPixel(x, y) == 0xffffffff) {
                    luasObjek =  (luasObjek + 1);
                }
            }
        }

        double luasKonveks = 0;
        int sigmaA = 0;
        int sigmaB = 0;

        Vector<Short> X = convexhullX;
        Vector<Short> Y = convexhullY;
        X.add(X.size(), X.firstElement());
        Y.add(Y.size(), Y.firstElement());

        for (short i = 1; i < X.size(); i++) {
            sigmaA =  (sigmaA + (Y.get(i) * X.get(i - 1)));
            sigmaB =  (sigmaB + (X.get(i) * Y.get(i - 1)));
        }

        int delta =  (sigmaA - sigmaB);
        luasKonveks = (double) (Math.abs(delta / 2.0));

        soliditas =(double) luasObjek / luasKonveks;
    }


    double sudut(Titik titik1, Titik titik2) {
        double sudut = 0;
        double deltaY = (double) (titik1.y - titik2.y);
        double deltaX = (double) (titik1.x - titik2.x);

        if (deltaX == 0) {
            deltaX = 0.00000001;
        }

        sudut = Math.atan(deltaY / deltaX);
        if (sudut == 0) {
            sudut = sudut + Math.PI;
        }

        return sudut;
    }

    short jarak(Titik titik1, Titik titik2) {
        short jarak = 0;
        short pers1 = (short) (titik1.x - titik2.x);
        short pers2 = (short) (titik1.y - titik2.y);
        return jarak = (short) ((pers1 * pers1) + (pers2 * pers2));
    }

    boolean berputarkeKanan(Titik titik1, Titik titik2, Titik titik3) {
        boolean status = false;
        return status = ((titik2.x - titik1.x) * (titik3.y - titik1.y)
                - (titik3.x - titik1.x) * (titik2.y - titik1.y)) > 0;
    }

    Vector<Piksel> unik(Vector<Piksel> piksel) {
        short jumlah = (short) piksel.size();
        Vector<Piksel> pikselUnik = new Vector<>();
        double sudut = -1;
        short jarak = 0;

        //tandai jarak -1 untuk titik tidak terpakai
        for (short i = 0; i < jumlah; i++) {
            if (sudut != piksel.get(i).sudut) {
                sudut = piksel.get(i).sudut;
                jarak = piksel.get(i).jarak;
            } else if (jarak < piksel.get(i).jarak) {
                piksel.get(i).jarak = -1;
            }
        }

        short indeks = 0;

        for (short i = 0; i < jumlah; i++) {
            if (piksel.get(i).jarak != -1) {
                pikselUnik.add(indeks, piksel.get(i));
                indeks++;
            }
        }

        return pikselUnik;
    }

    Titik centroid() {
        Titik pusat;
        int pusatX = 0;
        int pusatY = 0;
        int luas = 0;
        for (short i = 0; i < kolom; i++) {
            for (int j = 0; j < baris; j++) {
                if (gambar.getPixel(i,j) == 0xffffffff) {
                    luas =  (luas + 1);
                    pusatX =  (pusatX + i);
                    pusatY =  (pusatY + j);
                }
            }
        }

        pusatX =  (pusatX / luas);
        pusatY =  (pusatY / luas);

        return pusat = new Titik((short) pusatX,(short) pusatY);

    }


    class Titik {
        short x, y;

        Titik(short x, short y) {
            this.x = x;
            this.y = y;
        }

    }

    class Piksel {
        short x, y, jarak;
        double sudut;
    }
}
