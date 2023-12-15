package vn.giakhanhvn.skysim;

import vn.giakhanhvn.skysim.util.SkyEncryption;

public class ServerVersion {
    private final String z;
    private final int a;
    private final int b;
    private final int c;
    private final int d;
    private byte[] finalResult;

    public ServerVersion(final String stage, final int a, final int b, final int c, final int d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.z = stage;
        try {
            this.finalResult = new SkyEncryption().encrypt(this.z + "-" + this.a + "." + this.b + "." + this.c + "." + this.d).getBytes();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public String readableString() throws Exception {
        return new SkyEncryption().decrypt(new String(this.finalResult));
    }

    public byte[] getEncryptedByteArray() {
        return this.finalResult;
    }

    public byte[] getDecryptedByteArray() throws Exception {
        return new SkyEncryption().decrypt(new String(this.finalResult)).getBytes();
    }
}
