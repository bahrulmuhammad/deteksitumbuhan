package com.example.bebenay.deteksitumbuhan.Control;

import java.text.DecimalFormat;
import java.util.Vector;

/**
 * Created by bebe on 12/5/2015.
 */
public class Distance {
    Vector<Double> input = new Vector<>();
    Vector<Double> target = new Vector<>();
    public Double jarak = 0.0;
    double testing;
    double  training;
    DecimalFormat df;

    public Distance(Vector<Double> input, Vector<Double> target) {
        this.input = input;
        this.target = target;
    }

    public Distance(double testing, double training) {
        this.testing = testing;
        this.training = training;
    }

    public void Euclidean() {
        for (short i = 0; i < input.size(); i++) {
            double pers = target.get(i) - input.get(i);
            jarak += pers * pers;
        }
        jarak = Math.sqrt(jarak);
    }
}
