package com.codenjoy.dojo.kata.model.levels.algorithms;

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


import org.junit.Test;

public class FactorialAlgorithmTest {

    @Test
    public void shouldWork() {
        String[] expected = new String[]{"1", "1", "2", "6", "24", "120", "720", "5040", "40320", "362880", "3628800", "39916800",
                "479001600", "6227020800", "87178291200", "1307674368000", "20922789888000", "355687428096000",
                "6402373705728000", "121645100408832000", "2432902008176640000", "51090942171709440000",
                "1124000727777607680000", "25852016738884976640000", "620448401733239439360000", "15511210043330985984000000",
                "403291461126605635584000000", "10888869450418352160768000000", "304888344611713860501504000000",
                "8841761993739701954543616000000", "265252859812191058636308480000000", "8222838654177922817725562880000000",
                "263130836933693530167218012160000000", "8683317618811886495518194401280000000",
                "295232799039604140847618609643520000000", "10333147966386144929666651337523200000000"};

        Assertions.assertAlgorithm(expected, new FactorialAlgorithm());
    }


}
