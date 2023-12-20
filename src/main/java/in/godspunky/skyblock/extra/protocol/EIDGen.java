package in.godspunky.skyblock.extra.protocol;

class EIDGen {
    private static int lastIssuedEID;

    static int generateEID() {
        return EIDGen.lastIssuedEID++;
    }

    static {
        EIDGen.lastIssuedEID = 2000000000;
    }
}
