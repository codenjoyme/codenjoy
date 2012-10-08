package net.tetris.dom;

public class GlassEvent<T> {

    public enum Type{
        GLASS_OVERFLOW, LINES_REMOVED, FIGURE_DROPPED, TOTAL_LINES_REMOVED
    }

    private Type type;
    private T value;

    public GlassEvent(Type type, T value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GlassEvent that = (GlassEvent) o;

        if (type != that.type) return false;

        if (value == null && that.value != null) return false;
        if (type.equals(Type.TOTAL_LINES_REMOVED)) {
            if ((Integer)value > (Integer)that.value) return false;
        } else {
            if (!value.equals(that.value)) return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
