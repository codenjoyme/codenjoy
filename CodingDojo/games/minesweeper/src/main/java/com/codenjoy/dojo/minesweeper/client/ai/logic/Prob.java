

package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Prob {

    public static double not(double a) {
        if (a <= 1.0D && a >= 0.0D) {
            return 1.0D - a;
        } else {
            throw new IllegalArgumentException("Argument must be 0...1, not " + a);
        }
    }

    public static double sum(double a, double b) {
        if (a <= 1.0D && a >= 0.0D) {
            if (b <= 1.0D && b >= 0.0D) {
                return 1.0D - not(a) * not(b);
            } else {
                throw new IllegalArgumentException("Argument must be 0...1, not " + b);
            }
        } else {
            throw new IllegalArgumentException("Argument must be 0...1, not " + a);
        }
    }

    public static void correct(List<Double> list, double multiply) {
        double sum = 0.0D;

        Double elem;
        for(Iterator i$ = list.iterator(); i$.hasNext(); sum += elem) {
            elem = (Double)i$.next();
        }

        for(int i = 0; i < list.size(); ++i) {
            double res = list.get(i) * multiply / sum;
            list.set(i, res > 1.0D ? res : res);
        }

    }

    public static void main(String[] args) {
        List<Double> list4 = new ArrayList();
        List<Double> list2 = new ArrayList();
        double A = 1.0D;
        double B = 5.0D;
        double z = 7.0D;
        Double d1 = A / z;
        Double d2 = A / z;
        Double d3 = A / z;
        Double d8 = B / z;
        Double d9 = B / z;
        Double d10 = B / z;
        Double d4 = sum(d1, d8);
        Double d5 = sum(d1, d8);
        Double d6 = sum(d1, d8);
        Double d7 = sum(d1, d8);
        list4.add(d1);
        list4.add(d2);
        list4.add(d3);
        list4.add(d4);
        list4.add(d5);
        list4.add(d6);
        list4.add(d7);
        list2.add(d8);
        list2.add(d9);
        list2.add(d10);
        list2.add(d4);
        list2.add(d5);
        list2.add(d6);
        list2.add(d7);
        System.out.println(A + ":" + list4);
        System.out.println(B + ":" + list2);
        double a = 4.0D;
        double b = 1.0D * (A - B) - 13.0D;
        double c = A - 2.0D * (A - B);
        double D = b * b - 4.0D * a * c;
        double d81 = (-b - Math.sqrt(D)) / 2.0D / a;
        double d82 = (-b + Math.sqrt(D)) / 2.0D / a;
        double d41 = (B - 3.0D * d81) / 4.0D;
        double d42 = (B - 3.0D * d82) / 4.0D;
        double d11 = (A - 4.0D * d41) / 3.0D;
        double d12 = (A - 4.0D * d42) / 3.0D;
        System.out.println(d11 + " " + d41 + " " + d81);
        System.out.println(d12 + " " + d42 + " " + d82);

        for(int i = 0; i < 200; ++i) {
            correct(list4, A);
            list2.set(3, list4.get(3));
            list2.set(4, list4.get(4));
            list2.set(5, list4.get(5));
            list2.set(6, list4.get(6));
            correct(list2, B);
            list4.set(3, list2.get(3));
            list4.set(4, list2.get(4));
            list4.set(5, list2.get(5));
            list4.set(6, list2.get(6));
            System.out.print(A + ":" + list4);
            double res = 0.0D;

            Iterator i$;
            Double d;
            for(i$ = list4.iterator(); i$.hasNext(); res += d) {
                d = (Double)i$.next();
            }

            System.out.println(res);
            System.out.print(B + ":" + list2);
            res = 0.0D;

            for(i$ = list2.iterator(); i$.hasNext(); res += d) {
                d = (Double)i$.next();
            }

            System.out.println(res);
            System.out.println();
        }

    }
}
