package com.codenjoy.dojo.services.settings;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.printer.CharElements;

import java.util.*;

public class Chance {

    public static final int MAX_PERCENT = 100;
    public static final int RESERVE_FOR_MINUS = 30;

    private Dice dice;
    private CharElements element;

    private Map<CharElements, Parameter> input;
    private List<CharElements> axis;

    public Chance() {
        element = null;
        input = new LinkedHashMap<>();
        dice = new RandomDice();
        axis = new LinkedList<>();
    }

    private int ofMinus() {
        List<Parameter> parameters = new ArrayList<>(input.values());
        return (int) parameters.stream()
                .filter(parameter -> (int) parameter.getValue() == -1)
                .count();
    }

    private int sum() {
        List<Parameter> parameters = new ArrayList<>(input.values());
        return parameters.stream()
                .mapToInt(parameter -> (int) parameter.getValue())
                .filter(parameter -> parameter > 0)
                .sum();
    }

    private void checkParameters() {
        int sum = sum();

        if (sum > MAX_PERCENT) {
            changeParameters(sum, ofMinus());
        }
    }

    private void changeParameters(int sum, int countOfMinus) {
        if (countOfMinus == 0) {
            input.forEach((elements, parameter) -> parameter.update((int) parameter.getValue() * (MAX_PERCENT) / sum));
        }

        if (countOfMinus > 0) {
            input.forEach((elements, parameter) -> {
                int value = 0;

                if ((int) parameter.getValue() > 0) {
                    value = (int) parameter.getValue() * (MAX_PERCENT - RESERVE_FOR_MINUS) / sum;
                }

                if (value > 0) {
                    parameter.update(value);
                }
            });
        }
        checkParameters();
    }

    private void fillAxis(int toAxisMinus) {
        input.forEach(((element, parameter) -> addAxis(element, parameter, toAxisMinus)));
        getAny();
    }

    private void addAxis(CharElements element, Parameter parameter, int toAxisMinus) {
        if ((int) parameter.getValue() > 0) {
            List<CharElements> elements = new ArrayList<>(Collections.nCopies((int) parameter.getValue(), element));
            axis.addAll(elements);
        }

        if ((int) parameter.getValue() == -1) {
            List<CharElements> elements = new ArrayList<>(Collections.nCopies(toAxisMinus, element));
            axis.addAll(elements);
        }
    }

    private int minusToAxis() {
        int countOfMinus = ofMinus();
        int toAxisMinus = (MAX_PERCENT - sum()) / 2;

        if (countOfMinus > 1) {
            toAxisMinus = (MAX_PERCENT - sum()) / countOfMinus;
        }

        return toAxisMinus;
    }

    private CharElements getAny() {
        if (!axis.isEmpty()) {
            element = axis.get(dice.next(axis.size()));
        } else {
            element = null;
        }

        return element;
    }

    public List<CharElements> axis() {
        return axis;
    }

    public CharElements get() {
        return element;
    }

    public void put(CharElements element, Parameter<Integer> parameter) {
        input.put(element, parameter);
    }

    public void run() {
        checkParameters();
        fillAxis(minusToAxis());
    }
}
