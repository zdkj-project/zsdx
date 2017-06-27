package com.zd.core.util;

public class DigitalConversionUtil {

    public static int parseUnsignedInt(String number) {
        return Integer.parseUnsignedInt(number);
    }

    public static long parseSignedLong(String number) {
        Long long1 = Long.parseLong(number);
        return (long1 & 0x0FFFFFFFFl);
    }

    /*
     * Convert byte[] to hex
     * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     * 
     * @param src byte[] data
     * 
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     * 
     * @param hexString
     *            the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     * 
     * @param c
     *            char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    //将指定byte数组以16进制的形式打印到控制台  
    public static void printHexString(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase());
        }

    }

    public static byte CreatePackageCRC(byte[] Buff, int Len) {
        byte ucCRC = 0;
        int i, j;
        for (j = 0; j < Len; j++) {
            ucCRC ^= Buff[j];
            for (i = 0; i < 8; i++) {
                if ((ucCRC & 0x01) != 0) {
                    ucCRC >>= 1;
                    ucCRC ^= (byte) 0x8c;
                } else {
                    ucCRC >>= 1;
                }
            }
        }
        return ucCRC;
    }
}
