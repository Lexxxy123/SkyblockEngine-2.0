package vn.giakhanhvn.skysim.item;

import org.bukkit.entity.Player;

public interface Ability
{
    String getAbilityName();
    
    String getAbilityDescription();
    
    default void onAbilityUse(final Player player, final SItem sItem) {
    }
    
    int getAbilityCooldownTicks();
    
    int getManaCost();
    
    default AbilityActivation getAbilityActivation() {
        return AbilityActivation.RIGHT_CLICK;
    }
    
    default boolean displayUsage() {
        return true;
    }
    
    default boolean requirementsUse(final Player player, final SItem sItem) {
        return false;
    }
    
    default String getAbilityReq() {
        return "";
    }
    
    default boolean displayCooldown() {
        return true;
    }
}
