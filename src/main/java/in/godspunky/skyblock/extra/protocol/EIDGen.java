package in.godspunky.skyblock.extra.protocol;

class EIDGen {
    private static int lastIssuedEID;

    static {
        EIDGen.lastIssuedEID = 2000000000;
    }

    static int generateEID() {
        return EIDGen.lastIssuedEID++;
    }
}
