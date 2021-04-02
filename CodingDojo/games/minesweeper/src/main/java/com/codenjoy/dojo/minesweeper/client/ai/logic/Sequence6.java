

package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.math.BigInteger;
import java.util.ArrayList;

public class Sequence6 {
    public Sequence6() {
    }

    public static long getAmount(int setted, int whole) {
        long result = 1L;

        for(int i = whole; i > Math.max(setted, whole - setted); --i) {
            result *= (long)i;
        }

        result /= factorial(Math.min(setted, whole - setted));
        if (result < 0L) {
            result = 9223372036854775807L;
        }

        return result;
    }

    public static BigInteger getBigIntegerAmount(int setted, int whole) {
        BigInteger result = new BigInteger("1");
        BigInteger multiplier = new BigInteger(Integer.toString(whole));
        BigInteger minusOne = new BigInteger("-1");

        for(int i = whole; i > Math.max(setted, whole - setted); --i) {
            result = result.multiply(multiplier);
            multiplier = multiplier.add(minusOne);
        }

        result = result.divide(factorialBigInteger(Math.min(setted, whole - setted)));
        return result;
    }

    public static Integer[] getSequensed(int setted, int whole) {
        checkArguments(setted, whole);
        ArrayList arrayList = new ArrayList();
        int length = (int)Math.pow(2.0D, (double)whole);

        for(int i = 0; i < length; ++i) {
            if (Integer.bitCount(i) == setted) {
                arrayList.add(i);
            }
        }

        Integer[] result = new Integer[arrayList.size()];
        return (Integer[]) arrayList.toArray(result);
    }

    private static void checkArguments(int setted, int whole) {
        if (setted > whole) {
            throw new IllegalArgumentException("Количество установленных бит превышает общее количество бит ( " + setted + " > " + whole + " )");
        } else if (whole > 30) {
            throw new IllegalArgumentException("Разрядность числа превышает 30 бит");
        }
    }

    private static long factorial(int n) {
        long result = 1L;

        for(int i = 1; i <= n; ++i) {
            result *= (long)i;
        }

        return result;
    }

    private static BigInteger factorialBigInteger(int n) {
        BigInteger result = new BigInteger("1");

        for(int i = 1; i <= n; ++i) {
            result = result.multiply(new BigInteger(Integer.toString(i)));
        }

        return result;
    }

    public static void main(String[] args) {
        int setted = 30;
        int whole = 30;
        Sequence6 sequence6 = new Sequence6();
        System.out.println(getAmount(setted, whole));
        Integer[] res = getSequensed(setted, whole);
        sequence6.printMas(res);
    }

    public void printMas(Integer[] a) {
        int bits = Integer.toBinaryString(a[a.length - 1]).length();

        for(int i = 0; i < a.length; ++i) {
            StringBuilder prefix = new StringBuilder();
            String string = Integer.toBinaryString(a[i]);

            for(int j = string.length(); j < bits; ++j) {
                prefix.append('0');
            }

            System.out.println(prefix + string);
        }

        System.out.println("Всего " + a.length + " комбинаций");
    }
}
