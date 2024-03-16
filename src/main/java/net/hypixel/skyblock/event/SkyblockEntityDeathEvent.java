package net.hypixel.skyblock.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.user.User;

@Getter
@AllArgsConstructor
public class SkyblockEntityDeathEvent extends SkyblockEvent {

    private SEntity entity;
    private User killer;

}