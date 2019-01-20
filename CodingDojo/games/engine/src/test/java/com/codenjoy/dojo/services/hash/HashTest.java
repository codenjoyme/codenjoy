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
        assertHash("apofig@gmail.com", "soul", "k3dfwbacm74feznamcgthbk6bh");
        assertHash("apofig@gmail.comd", "soul", "k3dfwbacm74feznamcgthbk6b7ky");
        assertHash("apofig@gmail.com", "G(^D@F(@&If2d", "yemy1byxyf4ygzkzmy8b1y16ba");

        assertHash("qwertyuiop[]asdfghjkl;'zxvbnm,./!@#$%^&*()_+~`1234567890-=QWERTYUIOP{}ASDFGHJKL:\"||ZXCVBNM<>?\\/",
                "soul",
                "e3yiyratefyiwz1jpr6fnfkiytmi6sapmy8tguy7npmy4snkjhqbc7oseiygcro3dreg411qyayfyyadyteygdypy3rfo3jwqy4dk4unx77dn81fqio8k95iff7n49kanjfw4xdcq7t8ek3ebbqowq1q");

        assertHash("1", "soul", "ya");
        assertHash("12", "soul", "yany");
        assertHash("123", "soul", "yanyc");
        assertHash("1234", "soul", "yanycie");
        assertHash("12345", "soul", "yanyciko");
        assertHash("123456", "soul", "yanycikoba");
        assertHash("1234567", "soul", "yanycikobabo");
        assertHash("12345678", "soul", "yanycikobabos");
        assertHash("123456789", "soul", "yanycikobabosny");
        assertHash("1234567890", "soul", "yanycikobabosnyj");

        assertHash("1", "F@#*O2gt", "kh");

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
