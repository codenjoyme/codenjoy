package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic;

/**
 * Created with IntelliJ IDEA.
 * User: Vlasov Alexander
 * Date: 13.09.13
 * Time: 8:41
 * To change this template use File | Settings | File Templates.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Probability Theory
 */
public class Prob {
    public static double not(double a) {
        if (a > 1 || a < 0) throw new IllegalArgumentException("Argument must be 0...1, not " + a);
        return 1 - a;
    }

    /**
     * Вероятность наступления “хотя бы одного события” (т.е. суммы нескольких событий )
     *
     * @param a
     * @param b
     * @return
     */
    public static double sum(double a, double b) {
        if (a > 1 || a < 0) throw new IllegalArgumentException("Argument must be 0...1, not " + a);
        if (b > 1 || b < 0) throw new IllegalArgumentException("Argument must be 0...1, not " + b);
        return 1 - not(a) * not(b);
    }

    public static double sum(List<Double> doubles) {
        double res = 1;
        for (Double d : doubles) {
            if (d > 1 || d < 0) throw new IllegalArgumentException("Argument must be 0...1, not " + d);
            res *= not(d);
        }
        return 1 - res;
    }

    public static double mul(double a, double b) {
        return a * b;
    }

    public static void correct(List<Double> list, double multiply) {
        double sum = 0;
        for (Double elem : list) sum += elem;
        for (int i = 0; i < list.size(); i++) {
            double res = list.get(i) * multiply / sum;
            list.set(i, res > 1 ? res : res);
        }
    }

    public static long combinationNK(int n, int k) {
        double res = 1;
        int n_k = n - k;
        boolean tick;
        for (int i = n; i > k; i--) {
            if (n_k > 1) res = res * i / (n_k--);
            else res = res * i;
        }
        return (long) res;
    }

    public static long factorial(int n) {
        long res = n;
        while (n > 1)
            res = res * (--n);
        return res;

    }

    public static void main(String[] args) {
        List<Double> list4 = new ArrayList<Double>();
        List<Double> list2 = new ArrayList<Double>();
        double A = 1;
        double B = 5;
        double z = 7;

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
        double a = 4;
        double b = 4 / 3 * (A - B) - 13;
        double c = A - 7 / 3 * (A - B);
        double D = b * b - 4 * a * c;
        double d81 = (-b - Math.sqrt(D)) / 2 / a;
        double d82 = (-b + Math.sqrt(D)) / 2 / a;
        double d41 = (B - 3 * d81) / 4;
        double d42 = (B - 3 * d82) / 4;
        double d11 = (A - 4 * d41) / 3;
        double d12 = (A - 4 * d42) / 3;
        System.out.println(d11 + " " + d41 + " " + d81);
        System.out.println(d12 + " " + d42 + " " + d82);
        for (int i = 0; i < 200; i++) {
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
            double res = 0;
            for (Double d : list4) res += d;
            System.out.println(res);
            System.out.print(B + ":" + list2);
            res = 0;
            for (Double d : list2) res += d;
            System.out.println(res);
            System.out.println();
        }
    }
}
