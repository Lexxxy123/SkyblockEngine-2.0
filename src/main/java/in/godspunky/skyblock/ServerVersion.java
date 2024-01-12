package in.godspunky.skyblock;

import in.godspunky.skyblock.util.SkyEncryption;

public class ServerVersion {
    private byte[] finalResult;

    public ServerVersion(final String stage, final int a, final int b, final int c, final int d) {
        try {
            this.finalResult = new SkyEncryption().encrypt(stage + "-" + a + "." + b + "." + c + "." + d).getBytes();
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
