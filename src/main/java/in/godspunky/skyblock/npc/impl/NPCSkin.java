package in.godspunky.skyblock.npc.impl;

import lombok.Getter;

public class NPCSkin {
    @Getter
    private final String texture;
    @Getter
    private final String signature;


    public NPCSkin(String texture, String signature) {
        this.texture = texture;
        this.signature = signature;
    }
}
