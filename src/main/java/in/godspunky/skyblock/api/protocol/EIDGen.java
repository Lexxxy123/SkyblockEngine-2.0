package in.godspunky.skyblock.api.protocol;

class EIDGen {
    private static int lastIssuedEID;

    static int generateEID() {
        int i = lastIssuedEID;
        lastIssuedEID++;
        return i;
    }

    static {
        lastIssuedEID = 2000000000;
    }
}
