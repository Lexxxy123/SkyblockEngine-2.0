package in.godspunky.skyblock.api.protocol;

public class EIDGen {
    private static int lastIssuedEID;

   public static int generateEID() {
        int i = lastIssuedEID;
        lastIssuedEID++;
        return i;
    }

    static {
        lastIssuedEID = 2000000000;
    }
}
