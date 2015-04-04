package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic; /**
 * Created with IntelliJ IDEA.
 * User: Vlasov Alexander
 * Date: 04.09.13
 * Time: 12:35
 * To change this template use File | Settings | File Templates.
 */

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Возвращает массив целых чисел заданной длиной бит с заданным числом установленных бит
 */
public class Sequence6 {

    public static long getAmount(int setted, int whole) {

        long result = 1;
        for (int i = whole; i > Math.max(setted, whole - setted); i--) {
            result *= i;
        }
        result /= factorial(Math.min(setted, whole - setted));
        if (result < 0) result = Long.MAX_VALUE;

        return result;
    }

    public static BigInteger getBigIntegerAmount(int setted, int whole) {
        BigInteger result = new BigInteger("1");
        BigInteger multiplier = new BigInteger(Integer.toString(whole));
        BigInteger minusOne = new BigInteger("-1");
        for (int i = whole; i > Math.max(setted, whole - setted); i--) {
            result = result.multiply(multiplier);
            multiplier = multiplier.add(minusOne);
        }
        result = result.divide(factorialBigInteger(Math.min(setted, whole - setted)));
//        System.out.println(result);
        return result;
    }


    /**
     * Возвращает массив целых чисел заданной длиной бит с заданным числом установленных бит
     *
     * @param setted число установленных бит
     * @param whole  полное число бит
     * @return массив целых чисел
     */
    public static Integer[] getSequensed(int setted, int whole) {
        checkArguments(setted, whole);
        ArrayList arrayList = new ArrayList();
        int length = (int) Math.pow(2, (whole));
        for (int i = 0; i < length; i++) {
            if (Integer.bitCount(i) == setted) {
                arrayList.add(i);
            }
        }
        Integer[] result = new Integer[arrayList.size()];
//        System.out.println("Sequence6 нашел "+result.length+" комбинаций");
        return (Integer[]) arrayList.toArray(result);
    }

    private static void checkArguments(int setted, int whole) {
        if (setted > whole) {
            throw new IllegalArgumentException(
                    "Количество установленных бит превышает общее количество бит ( " + setted + " > " + whole + " )");
        }
        if (whole > Integer.SIZE - 2) {
            throw new IllegalArgumentException("Разрядность числа превышает 30 бит");
        }
    }

    private static long factorial(int n) {
        long result = 1;
        for (int i = 1; i <= n; ++i) {
            result *= i;
        }
        return result;
    }

    private static BigInteger factorialBigInteger(int n) {
        BigInteger result = new BigInteger("1");
        for (int i = 1; i <= n; ++i) {
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

        for (int i = 0; i < a.length; i++) {
            StringBuilder prefix = new StringBuilder();
            String string = Integer.toBinaryString(a[i]);
            for (int j = string.length(); j < bits; j++) prefix.append('0');
            System.out.println(prefix + string);

        }
        System.out.println("Всего " + a.length + " комбинаций");

    }
}

