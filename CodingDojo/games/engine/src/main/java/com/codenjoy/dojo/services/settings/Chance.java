package com.codenjoy.dojo.services.settings;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.printer.CharElements;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Chance {

    public static final int MAX_PERCENT = 100;
    public static final int RESERVE_FOR_MINUS = 30;

    private Dice dice;
    private CharElements element;
    private int toAxisMinus;

    private Map<CharElements, Parameter> input;
    private List<CharElements> axis;

    public Chance(Map<CharElements, Parameter> input) {
        this.input = input;
        dice = new RandomDice();
        axis = new LinkedList<>();
        toAxisMinus = 0;
    }

    private void checkParameters() {
        int countOfMinus = 0;
        int sum = 0;

        for (Parameter parameter : input.values()) {
            if ((int) parameter.getValue() == -1) {
                countOfMinus++;
            }
            if ((int) parameter.getValue() > 0) {
                sum += (int) parameter.getValue();
            }
        }

        if (sum > MAX_PERCENT) {
            changeParameters(sum, countOfMinus);
        }

        if (sum < MAX_PERCENT) {
            minusToAxis(sum, countOfMinus);
        }
    }

    private void changeParameters(int sum, int countOfMinus) {
        for (Parameter parameter : input.values()) {
            int value = 0;

            if (countOfMinus == 0) {
                value = (int) parameter.getValue() * (MAX_PERCENT) / sum;
            }

            if (countOfMinus > 0 && (int) parameter.getValue() > 0){
                value = (int) parameter.getValue() * (MAX_PERCENT - RESERVE_FOR_MINUS) / sum;
            }

            if (value > 0) {
                parameter.update(value);
            }
        }
        checkParameters();
    }

    private void fillAxis() {
        for (Map.Entry<CharElements, Parameter> entry : input.entrySet()) {
            CharElements element = entry.getKey();
            Parameter parameter = entry.getValue();
            addAxis(element, parameter);
        }
        getAny();
    }

    private void addAxis(CharElements element, Parameter parameter) {
        if ((int) parameter.getValue() > 0) {
            for (int i = 0; i < (int) parameter.getValue(); i++) {
                axis.add(element);
            }
        }

        if ((int) parameter.getValue() == -1) {
            for (int i = 0; i < toAxisMinus; i++) {
                axis.add(element);
            }
        }
    }

    private void minusToAxis(int sum, int countOfMinus) {
        toAxisMinus = (MAX_PERCENT - sum) / 2;

        if (countOfMinus > 1) {
            toAxisMinus = (MAX_PERCENT - sum) / countOfMinus;
        }
    }

    private CharElements getAny() {
        if (!axis.isEmpty()) {
            element = axis.get(dice.next(axis.size()));
        }

        return element;
    }

    public void run() {
        checkParameters();
        fillAxis();
    }

    public List<CharElements> axis() {
        return axis;
    }

    public CharElements get() {
        return element;
    }
}
