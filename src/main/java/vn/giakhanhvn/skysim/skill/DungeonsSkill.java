package vn.giakhanhvn.skysim.skill;

import java.util.List;

public interface DungeonsSkill {
    default List<String> getPassive() {
        return null;
    }

    default List<String> getGhost() {
        return null;
    }

    default List<String> getOrb() {
        return null;
    }
}
