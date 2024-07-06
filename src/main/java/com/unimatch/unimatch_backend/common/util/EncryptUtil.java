package com.unimatch.unimatch_backend.common.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class EncryptUtil {
    private static final String PASSWORD_CRYPT_KEY = "unimatch";

    private final static String DES = "DES";


    public final static String decrypt(String src) {
        try {
            return new String(decrypt(hex2byte(src.getBytes()), PASSWORD_CRYPT_KEY.getBytes()));
        } catch (Exception e) {
        }
        return null;
    }

    public final static String encrypt(String src) {
        try {
            return byte2hex(encrypt(src.getBytes(), PASSWORD_CRYPT_KEY.getBytes()));
        } catch (Exception e) {
        }
        return null;
    }

    // Encrypt using DES ALGO
    private static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        //DES Algo request a trustable random number
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(src);
    }

    private final static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        //Trustable random number
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        // DES key > SecretKey
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Use Cipher to decrypt
        Cipher cipher = Cipher.getInstance(DES);
        // Use secretkey to initialize Cipher
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        // Decrypt
        return cipher.doFinal(src);
    }

    private final static byte[] hex2byte(byte[] b) {
        int standard = 2;
        if ((b.length % standard) != 0) {
            throw new IllegalArgumentException("Hex to Byte error! Length is not even");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += standard) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    private final static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs.append("0").append(stmp);
            } else {
                hs = hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

}
