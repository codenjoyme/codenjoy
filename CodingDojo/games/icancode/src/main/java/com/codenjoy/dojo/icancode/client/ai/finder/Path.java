package com.codenjoy.dojo.icancode.client.ai.finder;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mikhail_Udalyi on 10.10.2016.
 */
public class Path extends LinkedList {

    public Path() {
        super();
    }

    public Path(List list) {
        super(list);
    }

    public int[] getArray() {
        int[] ret = new int[super.size()];
        int i = 0;
        for (Iterator iter = super.iterator(); iter.hasNext(); i++) {
            ret[i] = ((Integer) iter.next()).intValue();
        }
        return ret;
    }

    public void add(int direction) {
        super.add(new Integer(direction));
    }

    public String toString() {
        return super.toString();
    }

    public static void main(String[] args) {
        Path p = new Path();
        for (int i = 0; i < 10; i++) {
            p.add(i);
        }
        System.out.println(p);
    }
}
