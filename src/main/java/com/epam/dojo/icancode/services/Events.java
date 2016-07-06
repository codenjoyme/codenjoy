package com.epam.dojo.icancode.services;

/**
 * Ивенты, которые могут возникать в игре опиши тут. Что есть ивенты? ну убили твоего героя и ты хочешь ему очков начислить штрафных
 * или, быть может, наоборот - он поднял что-то ценное и ты хочешь ему дать бонус. Вот все все ивенты.
 */
public class Events {

    public static Events WIN(int goldCount) {
        return WIN(goldCount, false);
    }

    public static Events WIN(int goldCount, boolean multiple) {
        return new Events(goldCount, multiple);
    }

    public static Events LOOSE() {
        return new Events();
    }

    public enum Type {
        WIN, LOOSE;

    }
    private Type type;

    private int goldCount;
    private boolean multiple;

    public Events(int goldCount, boolean multiple) {
        this.multiple = multiple;
        type = Type.WIN;
        this.goldCount = goldCount;
    }

    public Events() {
        type = Type.LOOSE;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public int getGoldCount() {
        return goldCount;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Events events = (Events) o;

        if (goldCount != events.goldCount) {
            return false;
        }

        return type == events.type;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + goldCount;
        return result;
    }
}
