

package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.Arrays;

public class Sequence7 {
    private final int[] digits;
    private final int[] current;
    private final int last;

    public Sequence7(int[] digits) {
        this.checkArgument(digits);
        this.digits = digits;
        this.current = new int[digits.length];
        this.last = this.current.length - 1;
        this.current[this.last] = -1;
    }

    private void checkArgument(int[] digits) {
        int[] arr$ = digits;
        int len$ = digits.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            int digit = arr$[i$];
            if (digit <= 0) {
                throw new IllegalArgumentException("Неположительный разряд числа: " + Arrays.toString(digits));
            }
        }

    }

    public boolean hasNext() {
        for(int i = 0; i < this.digits.length; ++i) {
            if (this.current[i] != this.digits[i] - 1) {
                return true;
            }
        }

        return false;
    }

    public int[] next() {
        this.inc(this.last);
        return this.current;
    }

    private void inc(int n) {
        if (this.current[n] == this.digits[n] - 1) {
            this.current[n] = 0;
            this.inc(n - 1);
        } else {
            int var10002 = this.current[n]++;
        }

    }
}
