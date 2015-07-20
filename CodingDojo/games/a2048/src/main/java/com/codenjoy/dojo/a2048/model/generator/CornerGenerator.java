package com.codenjoy.dojo.a2048.model.generator;

import com.codenjoy.dojo.a2048.model.*;
import com.codenjoy.dojo.a2048.model.Number;

public class CornerGenerator implements Generator {

    @Override
    public void generate(Numbers numbers) {
        int last = numbers.size() - 1;
        checkAndSet(numbers, 0, 0);
        checkAndSet(numbers, 0, last);
        checkAndSet(numbers, last, 0);
        checkAndSet(numbers, last, last);
    }

    private void checkAndSet(Numbers numbers, int x, int y) {
        if (!numbers.isBusy(x, y)) {
            numbers.add(new Number(2, x, y));
        }
    }
}
