package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic;

import java.util.Arrays;

/**
 * Величина с числами различной разрядности.
 * Например есть разрядности 3, 7, 5.
 * Тогда 0+1=1, 4+1=10, 10+1=11, 14+1=20, 64+1=100, 264+1 = OutOfIndexException.
 * Используется только для перебора вариантов от начала до конца путем инкремента величины по принципу итератора
 *
 * @author Alexander Vlasov
 */
public class Sequence7 {
    private final int[] digits;
    private final int[] current;
    private final int last;

    /**
     * Создает величину с числами различной разрядности.
     *
     * @param digits разрядности каждого числа. Каждый разряд - положительное число.
     */
    public Sequence7(int[] digits) {
        checkArgument(digits);
        this.digits = digits;
        current = new int[digits.length];
        last = current.length - 1;
        current[last] = -1;
    }

    private void checkArgument(int[] digits) {
        for (int digit : digits) {
            if (digit <= 0) {
                throw new IllegalArgumentException("Неположительный разряд числа: " + Arrays.toString(digits));
            }
        }
    }

    public boolean hasNext() {
        for (int i = 0; i < digits.length; i++) {
            if (current[i] != digits[i] - 1) return true;
        }
        return false;
    }

    public int[] next() {
        inc(last);
        return current;
    }

    private void inc(int n) {
        if (current[n] == digits[n] - 1) {
            current[n] = 0;
            inc(n - 1);
        } else {
            current[n]++;
        }
    }
}
