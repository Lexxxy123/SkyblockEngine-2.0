package in.godspunky.skyblock.user;

public class SaveInfo {
    private final String uuid;
    private boolean isSoft;

    public SaveInfo(String uuid, boolean isSoft) {
        this.uuid = uuid;
        this.isSoft = isSoft;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean isSoft() {
        return isSoft;
    }

    public void setSoft(boolean isSoft) {
        this.isSoft = isSoft;
    }
}
