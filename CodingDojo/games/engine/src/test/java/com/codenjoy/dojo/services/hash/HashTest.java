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

    @Test
    public void getCode() {
        assertEquals("332508025959427091", Hash.getCode("some@email.com", "password"));
        assertEquals("1692647133079004397", Hash.getCode("some@email.com", "password1"));
        assertEquals("4328714851439546419", Hash.getCode("some1@email.com", "password"));
        assertEquals("9202308006379358423", Hash.getCode("some2@email.com", "password"));
        assertEquals("9190844035320923121", Hash.getCode("some@email.com", "password2"));
        assertEquals("5604620294581909543", Hash.getCode("some2@email.com", "password2"));
        assertEquals("8875365410164739627", Hash.getCode("other@qweasd.com", "&DF^(3f#"));
        assertEquals("2629080835288317399", Hash.getCode("", ""));
        assertEquals("3967685628367253962", Hash.getCode("a", ""));
        assertEquals("2534205565314907788", Hash.getCode("aa", ""));
        assertEquals("2185515729311252789", Hash.getCode("aaa", ""));
        assertEquals("1823907032787024471", Hash.getCode("aaaa", ""));
        assertEquals("6422843662087450229", Hash.getCode("aaaaa", ""));
        assertEquals("8031143483660354678", Hash.getCode("aaaaaa", ""));
        assertEquals("7327808622569386488", Hash.getCode("aaaaaaa", ""));
        assertEquals("4868999876524578793", Hash.getCode("aaaaaaaa", ""));
        assertEquals("7435395250271427528", Hash.getCode("aaaaaaaaa", ""));
        assertEquals("7329568606724989118", Hash.getCode("aaaaaaaaaa", ""));
        assertEquals("2629080835288317399", Hash.getCode("", ""));
        assertEquals("1822563622007870532", Hash.getCode("", "a"));
        assertEquals("9081627849986635866", Hash.getCode("", "aa"));
        assertEquals("9188476625050457977", Hash.getCode("", "aaa"));
        assertEquals("7910242258643120595", Hash.getCode("", "aaaa"));
        assertEquals("5891487770313027016", Hash.getCode("", "aaaaa"));
        assertEquals("6517161914849431196", Hash.getCode("", "aaaaaa"));
        assertEquals("6550003572887281106", Hash.getCode("", "aaaaaaa"));
        assertEquals("6438391547563772104", Hash.getCode("", "aaaaaaaa"));
        assertEquals("2573975121814597326", Hash.getCode("", "aaaaaaaaa"));
        assertEquals("6234584207232655936", Hash.getCode("", "aaaaaaaaaa"));
    }
}
