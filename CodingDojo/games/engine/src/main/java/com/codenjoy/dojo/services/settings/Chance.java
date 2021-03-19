package com.codenjoy.dojo.services.settings;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.printer.CharElements;

import java.util.*;

public class Chance<T extends CharElements> {

    public static final int MAX_PERCENT = 100;
    public static final int RESERVE_FOR_MINUS = 30;

    private Dice dice;
    private SettingsReader settings;

    private Map<T, Parameter<Integer>> input;
    private List<T> axis;

    public Chance(Dice dice, SettingsReader settings, Map<T, SettingsReader.Key> params) {
        this.settings = settings;
        this.input = new LinkedHashMap<>();
        this.dice = dice;
        this.axis = new LinkedList<>();
        fill(params);
    }

    private void fill(Map<T, SettingsReader.Key> params) {
        params.entrySet().forEach(entry -> {
            SettingsReader.Key name = entry.getValue();
            T el = entry.getKey();

            Parameter<Integer> param = settings.integerValue(name);
            add(el, param);
            param.onChange(value -> run());
        });
    }

    private int countAuto() {
        List<Parameter> params = new ArrayList<>(input.values());
        return (int) params.stream()
                .filter(param -> (int) param.getValue() == -1)
                .count();
    }

    private int sum() {
        List<Parameter<Integer>> params = new ArrayList<>(input.values());
        return params.stream()
                .mapToInt(param -> param.getValue())
                .filter(param -> param > 0)
                .sum();
    }

    private void checkParams() {
        int sum = sum();
        if (sum > MAX_PERCENT) {
            changeParams(sum, countAuto());
        }
    }

    private void changeParams(int sum, int auto) {
        if (auto == 0) {
            input.forEach((el, param) -> {
                int value = param.getValue();
                if (value <= 0) return;

                value = value * (MAX_PERCENT) / sum;
                if (value <= 0) return;

                update(param, value);
            });
        }

        if (auto > 0) {
            input.forEach((el, param) -> {
                int value = param.getValue();
                if (value <= 0) return;

                value = value * (MAX_PERCENT - RESERVE_FOR_MINUS) / sum;
                if (value <= 0) return;

                update(param, value);
            });
        }
        checkParams();
    }

    private void update(Parameter param, int value) {
        // обновляет параметр без вызова лиснера, который перегенерит все axis
        ((Updatable)param).justSet(value);
    }

    private void fillAxis(int toAxisMinus) {
        input.forEach(((el, param) -> addAxis(el, param, toAxisMinus)));
    }

    private void addAxis(T el, Parameter param, int toAxisMinus) {
        if ((int) param.getValue() > 0) {
            axis.addAll(Collections.nCopies((int) param.getValue(), el));
        }

        if ((int) param.getValue() == -1) {
            axis.addAll(Collections.nCopies(toAxisMinus, el));
        }
    }

    private int minusToAxis() {
        int auto = countAuto();
        if (auto > 1) {
            return  (MAX_PERCENT - sum()) / auto;
        }

        return (MAX_PERCENT - sum()) / 2;
    }

    public T any() {
        if (axis.isEmpty()) {
            return null;
        }

        return axis.get(dice.next(axis.size()));
    }

    public List<T> axis() {
        return axis;
    }

    private void add(T el, Parameter<Integer> param) {
        input.put(el, param);
    }

    public void run() {
        axis.clear();
        checkParams();
        fillAxis(minusToAxis());
    }
}
