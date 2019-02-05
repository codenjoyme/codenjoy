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

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;


public class HashTest {

    @Test
    public void testEncodeEmailToIdAndBack() {
        assertHashes("soul",
                    "apofig@gmail.com\n" +
                    "apofig@gmail.comd\n" +
                    "qwertyuiop[]asdfghjkl;'zxvbnm,./!@#$%^&*()_+~`1234567890-=QWERTYUIOP{}ASDFGHJKL:\"||ZXCVBNM<>?\\/\n" +
                    "1\n" +
                    "12\n" +
                    "123\n" +
                    "1234\n" +
                    "12345\n" +
                    "123456\n" +
                    "1234567\n" +
                    "12345678\n" +
                    "123456789\n" +
                    "1234567890\n" +
                    "12345678901\n" +
                    "123456789012\n" +
                    "1234567890123\n" +
                    "12345678901234\n" +
                    "123456789012345\n" +
                    "1234567890123456\n" +
                    "afia7o8g48w74gf4\n" +
                    "I&X#F&I!#C#KXF*OC\n" +
                    "GI&F&IDo7diXCCYkl8\n" +
                    "O&*Do&do&de6o&do7D\n" +
                    "O&*d6IO&^Do7DO7e^o&\n" +
                    "I&io&cO&*c6\n" +
                    "(PG&opD&^IO^5sdio&^dOC\n" +
                    "O78F^&O6Fo&fr6O8\n" +
                    "1\n" +
                    "11\n" +
                    "111\n" +
                    "1111\n" +
                    "11111\n" +
                    "111111\n" +
                    "1111111\n" +
                    "11111111\n" +
                    "111111111\n" +
                    "1111111111\n" +
                    "11111111111\n" +
                    "111111111111\n" +
                    "1111111111111\n" +
                    "11111111111111\n" +
                    "111111111111111\n" +
                    "1111111111111111\n" +
                    "11111111111111111\n" +
                    "111111111111111111\n" +
                    "1111111111111111111\n" +
                    "11111111111111111111\n" +
                    "111111111111111111111\n" +
                    "1111111111111111111111\n" +
                    "11111111111111111111111\n" +
                    "111111111111111111111111\n" +
                    "1111111111111111111111111\n" +
                    "11111111111111111111111111\n" +
                    "111111111111111111111111111\n" +
                    "o87f67D^o&r6FP&*F",

                    "bymya1ygkhffoidtbrnogzn4by\n" +
                    "brniqiykmjmio1yemtaocdyeycmy\n" +
                    "ytqog44zyakrexnhbbgwsfmqyhei1naqkw8bnfo6ncdwq8yiyffyg71fejyd6fe5jfggc81ey3miqwo8yrffrfacktfispbsq3tuk1mcxthuc8earbo8kj3nqf9z4k1hehqw6433r3zzq6bmmjpih5a6\n" +
                    "yh\n" +
                    "kodo\n" +
                    "ktkoq\n" +
                    "kieieba\n" +
                    "ktkoqint\n" +
                    "yikfkb4wkr\n" +
                    "kwdykwkwkodo\n" +
                    "ktkycbkty7ki1\n" +
                    "mfkfninimodokbo\n" +
                    "ktqfebagkfciknef\n" +
                    "kiciniy8ytqycbkwbr\n" +
                    "bfciebntkodoeiefy3qy\n" +
                    "kwnyqsery3kfnbejktkia\n" +
                    "ytkycsejmonykik1y7kiniy\n" +
                    "bfjfkikomodykwkwyhnfebn3\n" +
                    "ktkfqik3y7ey1begkijfnbyrmo\n" +
                    "kayfkwogkhbonzyjbhyoeoobkr\n" +
                    "8ao86fkxxia8wxt8n3hrym1ge3yy\n" +
                    "mtxwym48xetohjk1qiauanmqriqow\n" +
                    "kynraoyqnwoohondkihon7eybfqoh\n" +
                    "yo7wakebmat819m6ehyiri44eb7g4ta\n" +
                    "b7dowod9yp8zhwn8me\n" +
                    "byy1noaqep4zcw548hythpuopy7iataiqamo\n" +
                    "87ei1fnzxrifcfm6m3engr4orc\n" +
                    "yh\n" +
                    "khdo\n" +
                    "k7moq\n" +
                    "k7kfqba\n" +
                    "k7moqwnw\n" +
                    "yjmiqb4oko\n" +
                    "khyyrinzkydo\n" +
                    "k7eyyy1wy7miy\n" +
                    "kbmiewnzkodoryy\n" +
                    "k7kfybayktefqnyn\n" +
                    "k7efeia8ytkyyy1oby\n" +
                    "bbefybnwkhdoqianybky\n" +
                    "khnyqwy8ybmieyoekbmie\n" +
                    "ytmoywyekodori4zy7miewy\n" +
                    "bbmiqi4wkoyyrinoyhnfqb4o\n" +
                    "kbmiyi4oy7kyoyoyk7mieby8ko\n" +
                    "k7moqy1wyhbyoi4oyoyfywnwktmo\n" +
                    "yobfqi4oyjkfyi4wyydieyy8khrfy\n" +
                    "kbkfqi4wybmoqnyykydiewyrkhbyrwy\n" +
                    "yhnfqinzkbkyriyekybfqynwy7eyyi4o\n" +
                    "y7mooinoyjkfywnoytkfqyy8bbmiqy1wyy\n" +
                    "kbkfqinoyeyyeiarkbkyqiaey7kyrwnzbyyy\n" +
                    "kbmiqbayktkyriyrybkyob4okhbfqwnobynyq\n" +
                    "kybfyi4wyhdieb4zkbmiqnnwyybyywnwbynyeia\n" +
                    "kynyqwnzyjmiyiyyytkfqi4wkybyyi4wbydoob4z\n" +
                    "yhnyoynokyyyriyeytkfqi4zkybfei4wk7kfyia8yh\n" +
                    "yhnfqwnoyeyfyinzkodiqinzkteyownzyoryqi4wyeyy\n" +
                    "kr5f1bndyb4t6enze74u61akypxy");

        assertHash("1", "F@#*O2gt", "ya");
        assertHash("", "soul", "");
        assertHash("", "H*OCv2ck", "");

        assertHash(null, "soul", null);
        assertHash(null, "F#*O&GF", null);
    }

    private void assertHashes(String soul, String expectedEmails, String expectedIds) {
        List<String> actualIds = getIds(soul, Arrays.asList(expectedEmails.split("\n")));
        List<String> actualEmails = getEmails(soul, actualIds);

        assertEquals(expectedEmails, String.join("\n", actualEmails));
        assertEquals(expectedIds, String.join("\n", actualIds));
    }

    @Test
    public void test() {
        long time = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < 10_000; i++) {
            assertHash("apofig@gmail.com", "G(^D@F(@&If2d", "bomy6dkaywyygsy8bwboqj1eba");
        }
        System.out.println(Calendar.getInstance().getTimeInMillis() - time);
    }

    private List<String> getIds(String soul, List<String> emails) {
        return emails.stream()
                .map(email -> Hash.getId(email, soul))
                .collect(toList());
    }

    private List<String> getEmails(String soul, List<String> ids) {
        return ids.stream()
                .map(id -> Hash.getEmail(id, soul))
                .collect(toList());
    }

    private void assertHash(String email, String soul, String expectedHash) {
        String id = Hash.getId(email, soul);

        assertEquals(id, expectedHash);

        String decodedEmail = Hash.getEmail(id, soul);

        assertEquals(decodedEmail, email);
    }
}
