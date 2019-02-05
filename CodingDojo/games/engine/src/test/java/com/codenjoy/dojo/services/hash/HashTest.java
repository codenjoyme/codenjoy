package com.codenjoy.dojo.services.hash;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import static org.junit.Assert.assertEquals;


public class HashTest {

    @Test
    public void testEncodeEmailToIdAndBack() {
        assertHash("apofig@gmail.com", "soul", "khko1yacyauyrnyybhrwsyojby");
        assertHash("apofig@gmail.comd", "soul", "khko1yacyauyrnyybhrwsyojbyyo");
        assertHash("apofig@gmail.com", "G(^D@F(@&If2d", "kwko4waxke1frsy8morbsiojmy");

        assertHash("qwertyuiop[]asdfghjkl;'zxvbnm,./!@#$%^&*()_+~`1234567890-=QWERTYUIOP{}ASDFGHJKL:\"||ZXCVBNM<>?\\/",
                "soul",
                "ehjygfatdyjoanot8whyeronycby1dyqbfprn8a7nhnysnnpjbfreekfefyd6onxjird1uo5yfmiqi1ikpjirsk9kirfap31ry3urxbofywuk8ohrh5nnj3bfwz1wk19ehqtwx37reanqk3cmjpiwxkj");

        assertHash("1", "soul", "yh");
        assertHash("12", "soul", "y7mo");
        assertHash("123", "soul", "y7mik");
        assertHash("1234", "soul", "y7mikwe");
        assertHash("12345", "soul", "y7mikwko");
        assertHash("123456", "soul", "y7mikwkokh");
        assertHash("1234567", "soul", "y7mikwkok7eo");
        assertHash("12345678", "soul", "y7mikwkok7ei4");
        assertHash("123456789", "soul", "y7mikwkok7ei4zy");
        assertHash("1234567890", "soul", "y7mikwkok7ei4znt");

        assertHash("1", "F@#*O2gt", "by");

        assertHash("", "soul", "");
        assertHash("", "H*OCv2ck", "");

        assertHash(null, "soul", null);
        assertHash(null, "F#*O&GF", null);
    }

    private void assertHash(String email, String soul, String expectedHash) {
        String id = Hash.getId(email, soul);

        assertEquals(id, expectedHash);

        String decodedEmail = Hash.getEmail(id, soul);

        assertEquals(decodedEmail, email);
    }
}
