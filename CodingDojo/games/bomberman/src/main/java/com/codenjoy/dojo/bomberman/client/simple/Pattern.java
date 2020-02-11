package com.codenjoy.dojo.bomberman.client.simple;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Pattern {
    
    private String pattern;
    private Synonyms synonyms;

    public Pattern(String pattern, Synonyms synonyms) {
        this.pattern = pattern;
        this.synonyms = synonyms;
    }

    public Pattern() {
        pattern = StringUtils.EMPTY;
        synonyms = new Synonyms(); 
    }

    public String pattern() {
        return pattern;
    }

    public Synonyms synonyms() {
        return synonyms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pattern pattern1 = (Pattern) o;
        return pattern.equals(pattern1.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern);
    }

    @Override
    public String toString() {
        return pattern;
    }
}
