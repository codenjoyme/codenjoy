package com.codenjoy.dojo.services.settings;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.printer.CharElements;

import java.util.*;

public class Chance<T extends CharElements> {

    public static final int MAX_PERCENT = 100;
    public static final int RESERVE_FOR_MINUS = 30;

    private Dice dice;

    private Map<T, Parameter> input;
    private List<T> axis;

    public Chance() {
        this.input = new LinkedHashMap<>();
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
    }

    private void addAxis(T element, Parameter parameter, int toAxisMinus) {
        if ((int) parameter.getValue() > 0) {
            List<T> elements = new ArrayList<>(Collections.nCopies((int) parameter.getValue(), element));
            axis.addAll(elements);
        }

        if ((int) parameter.getValue() == -1) {
            List<T> elements = new ArrayList<>(Collections.nCopies(toAxisMinus, element));
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

    public T getAny() {
        if (!axis.isEmpty()) {
            return axis.get(dice.next(axis.size()));
        } else {
            return null;
        }
    }

    public List<T> axis() {
        return axis;
    }

    public void put(T element, Parameter<Integer> parameter) {
        input.put(element, parameter);
    }

    public void run() {
        checkParameters();
        fillAxis(minusToAxis());
    }
}
