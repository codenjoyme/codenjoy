package com.codenjoy.dojo.services.hash;

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