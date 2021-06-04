package com.codenjoy.dojo.collapse.client;

import com.codenjoy.dojo.collapse.model.Elements;
import com.codenjoy.dojo.services.PointImpl;

public class ElementPoint extends PointImpl {
    private final Elements element;

    public ElementPoint(int x, int y, Elements element) {
        super();
        this.x = x;
        this.y = y;
        this.element = element;
    }
    
    public Elements getElement() {
        return element;
    }
    
    @Override
    public String toString() {
        return String.format("[%s,%s,%s]", x, y, element);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ElementPoint other = (ElementPoint) obj;
        if (element != other.element)
            return false;
        return true;
    }

}
