package net.tetris.dom;

public class GlassEvent<T> {
    public enum Type{
        GLASS_OVERFLOW, LINES_REMOVED, FIGURE_DROPPED
    }

    private Type type;
    private T value;

    protected GlassEvent(Type type, T value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public T getValue() {
        return value;
    }
}
