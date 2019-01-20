package com.codenjoy.dojo.services.hash;

import org.apache.commons.lang.StringUtils;

/**
 * Портировано с https://github.com/denxc/ZBase32Encoder/blob/master/ZBase32Encoder/ZBase32Encoder/ZBase32Encoder.cs
 * Читай  https://habrahabr.ru/post/190054/
 * Спасибо автору!
 */
public class ZBase32Encoder {

    private static final String ENCODING_TABLE = "ybndrfg8ejkmcpqxot1uwisza345h769";

    private static final byte[] DECODING_TABLE = new byte[128];

    static {
        for (int i = 0; i < DECODING_TABLE.length; ++i) {
            DECODING_TABLE[i] = Byte.MAX_VALUE;
        }

        for (int i = 0; i < ENCODING_TABLE.length(); ++i) {
            DECODING_TABLE[(byte)ENCODING_TABLE.codePointAt(i)] = (byte)i;
        }
    }

    public static String encode(byte[] data) {
        if (data == null) {
            return null;
        }

        StringBuilder encodedResult = new StringBuilder((int)Math.ceil(data.length * 8.0 / 5.0));

        for (int i = 0; i < data.length; i += 5) {
            int byteCount = Math.min(5, data.length - i);

            long buffer = 0;
            for (int j = 0; j < byteCount; ++j) {
                buffer = (buffer << 8) | data[i + j];
            }

            int bitCount = byteCount * 8;
            while (bitCount > 0) {
                int index = bitCount >= 5
                        ? (int)(buffer >> (bitCount - 5)) & 0x1f
                        : (int)(buffer & (long)(0x1f >> (5 - bitCount))) << (5 - bitCount);

                encodedResult.append(ENCODING_TABLE.charAt(index));
                bitCount -= 5;
            }
        }

        return encodedResult.toString();
    }

    public static byte[] decode(String data) {
        if (StringUtils.isEmpty(data)) {
            return new byte[0];
        }

        byte[] result = new byte[(int) Math.floor(data.length() * 5.0 / 8.0)];
        int bindex = 0;

        int[] index = new int[8];
        for (int i = 0; i < data.length(); ) {
            i = createIndexByOctetAndMovePosition(data, i, index);

            int shortByteCount = 0;
            long buffer = 0;
            for (int j = 0; j < 8 && index[j] != -1; ++j) {
                buffer = (buffer << 5) | (long)(DECODING_TABLE[index[j]] & 0x1f);
                shortByteCount++;
            }

            int bitCount = shortByteCount * 5;
            while (bitCount >= 8) {
                result[bindex++] = (byte)((buffer >> (bitCount - 8)) & 0xff);
                bitCount -= 8;
            }
        }

        return result;
    }

    private static int createIndexByOctetAndMovePosition(String data, int currentPosition, int[] index) {
        int j = 0;
        while (j < 8) {
            if (currentPosition >= data.length()) {
                index[j++] = -1;
                continue;
            }

            if (IgnoredSymbol(data.charAt(currentPosition))) {
                currentPosition++;
                continue;
            }

            index[j] = data.charAt(currentPosition);
            j++;
            currentPosition++;
        }

        return currentPosition;
    }

    private static boolean IgnoredSymbol(char checkedSymbol) {
        return checkedSymbol >= DECODING_TABLE.length || DECODING_TABLE[checkedSymbol] == Byte.MAX_VALUE;
    }
}
