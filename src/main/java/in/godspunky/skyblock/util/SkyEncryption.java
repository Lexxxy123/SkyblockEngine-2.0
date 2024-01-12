package in.godspunky.skyblock.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

public class SkyEncryption {
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private static final String UNICODE_FORMAT = "UTF8";
    private final Cipher cipher;
    byte[] arrayBytes;
    SecretKey key;

    public SkyEncryption() throws Exception {
        String myEncryptionKey = "ThisIsSpartaThisIsSparta";
        String myEncryptionScheme = "DESede";
        this.arrayBytes = myEncryptionKey.getBytes(StandardCharsets.UTF_8);
        KeySpec ks = new DESedeKeySpec(this.arrayBytes);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        this.cipher = Cipher.getInstance(myEncryptionScheme);
        this.key = skf.generateSecret(ks);
    }

    public String encrypt(final String unencryptedString) {
        String encryptedString = null;
        try {
            this.cipher.init(1, this.key);
            final byte[] plainText = unencryptedString.getBytes(StandardCharsets.UTF_8);
            final byte[] encryptedText = this.cipher.doFinal(plainText);
            encryptedString = new String(Base64.getEncoder().encode(encryptedText));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    public String decrypt(final String encryptedString) {
        String decryptedText = null;
        try {
            this.cipher.init(2, this.key);
            final byte[] encryptedText = Base64.getDecoder().decode(encryptedString);
            final byte[] plainText = this.cipher.doFinal(encryptedText);
            decryptedText = new String(plainText);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }
}
