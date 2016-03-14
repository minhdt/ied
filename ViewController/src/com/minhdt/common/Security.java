package com.minhdt.common;

import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class Security {
    public Security() {
    }
    
    /**
     *
     * @param txt, text in plain format
     * @param hashType MD5 OR SHA1
     * @return hash in hashType
     */
    public static String getHash(String txt, String hashType) {
        try {
            java.security.MessageDigest md =
                java.security.MessageDigest.getInstance(hashType);
            byte[] array = md.digest(txt.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) |
                                              0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            //error action
            e.printStackTrace();
        }
        return null;
    }

    public static String md5(String txt) {
        return getHash(txt, "MD5");
    }

    public static String sha1(String txt) {
        return getHash(txt, "SHA1");
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char)('0' + halfbyte));
                else
                    buf.append((char)('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException,
                                                  UnsupportedEncodingException {
        byte[] t = text.getBytes("UTF-8");

        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(t, 0, t.length);
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    /**
     *
     * @param text
     * @param charset
     * Description
     * US-ASCII Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the Unicode character set
     * ISO-8859-1   ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1
     * UTF-8 Eight-bit UCS Transformation Format
     * UTF-16BE Sixteen-bit UCS Transformation Format, big-endian byte order
     * UTF-16LE Sixteen-bit UCS Transformation Format, little-endian byte order
     * UTF-16 Sixteen-bit UCS Transformation Format, byte order identified by an optional byte-order mark
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String SHA1(String text,
                              String charset) throws NoSuchAlgorithmException,
                                                     UnsupportedEncodingException {
        byte[] t = text.getBytes(charset);

        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(t, 0, t.length);
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public static String encrypt(String plaintext) throws NoSuchAlgorithmException,
                                                          UnsupportedEncodingException {
        MessageDigest md = null; //step 2
        md = MessageDigest.getInstance("SHA");

        md.update(plaintext.getBytes("UTF-8"));

        byte raw[] = md.digest(); //step 4
        String hash = (new BASE64Encoder()).encode(raw); //step 5
        return hash; //step 6
    }

    public static String convertUTF8(String str) {
        StringBuffer ostr = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if ((ch >= 0x0020) &&
                (ch <= 0x007e)) // Does the char need to be converted to unicode?
            {
                ostr.append(ch); // No.
            } else // Yes.
            {
                ostr.append("\\u"); // standard unicode format.
                String hex =
                    Integer.toHexString(str.charAt(i) & 0xFFFF); // Get hex value of the char.
                for (int j = 0; j < 4 - hex.length();
                     j++) // Prepend zeros because unicode requires 4 digits
                    ostr.append("0");
                ostr.append(hex.toLowerCase()); // standard unicode format.
                //ostr.append(hex.toLowerCase(Locale.ENGLISH));
            }
        }

        return (new String(ostr));
    }
}
