/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.http;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class ResponseDecrypter {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;
    private final SecretKey secretKey;

    public ResponseDecrypter(String strSecret) {
        byte[] decodedSecret = Base64.getDecoder().decode(strSecret);
        this.secretKey = new SecretKeySpec(decodedSecret, 0, decodedSecret.length, "AES");
    }

    public String decrypt(byte[] bytes) throws GeneralSecurityException {
        ByteBuffer bb2 = ByteBuffer.wrap(bytes);
        byte[] iv = new byte[12];
        bb2.get(iv);
        byte[] cipherText = new byte[bb2.remaining()];
        bb2.get(cipherText);
        return ResponseDecrypter.decrypt(cipherText, this.secretKey, iv);
    }

    private static String decrypt(byte[] bytes, SecretKey secret, byte[] iv) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        cipher.init(2, (Key)secret, new GCMParameterSpec(128, iv));
        byte[] plainText = cipher.doFinal(bytes);
        return new String(plainText, UTF_8);
    }
}

