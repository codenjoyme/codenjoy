package com.codenjoy.dojo.bomberman.model.perks;

import java.util.Objects;

public class PerkSettings {
    public int value;
    public int timeout;

    public PerkSettings(int value, int timeout) {
        this.value = value;
        this.timeout = timeout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerkSettings that = (PerkSettings) o;
        return value == that.value &&
                timeout == that.timeout;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, timeout);
    }
}
