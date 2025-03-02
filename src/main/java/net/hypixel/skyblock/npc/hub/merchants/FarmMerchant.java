/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.npc.hub.merchants;

import net.hypixel.skyblock.features.merchant.FarmMerchantGUI;
import net.hypixel.skyblock.npc.impl.NPCParameters;
import net.hypixel.skyblock.npc.impl.NPCSkin;
import net.hypixel.skyblock.npc.impl.SkyBlockNPC;
import org.bukkit.entity.Player;

public class FarmMerchant
extends SkyBlockNPC {
    public FarmMerchant() {
        super(new NPCParameters(){

            @Override
            public String id() {
                return "FARM_MERCHANT";
            }

            @Override
            public String name() {
                return "&fFarm Merchant";
            }

            @Override
            public String[] messages() {
                return new String[]{"You can buy and sell harvested crops with me!", "Wheat, carrots, potatoes, and melon are my specialties!", "Click me again to open the Farmer Shop!"};
            }

            @Override
            public NPCSkin skin() {
                return new NPCSkin("ewogICJ0aW1lc3RhbXAiIDogMTU4OTE5MDgwODI5MiwKICAicHJvZmlsZUlkIiA6ICI3NzI3ZDM1NjY5Zjk0MTUxODAyM2Q2MmM2ODE3NTkxOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJsaWJyYXJ5ZnJlYWsiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTNmNGRjM2U2NWI2NzkyNjAzM2IwYWY4M2E4ZGFlNmM2MzhkMzc3MDc3ODlmM2E2MWQ3OGI2ZmRiZGY5MjliZiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9", "Eq4+sXhgFY9iICwFYzPF5sF6fMJJGM4Wp3ECGYNJ6e5O1jhSMGJcQnpRyF7a770pNniz56gTlH6C6AT+aYIEUVjscBBAaAWIxNxR17mUmp0Ggy/6aMKzv8SPO9e5s7G6uMznOYdxEUiSUvSQ4qfM5mC1BAHcv5CnP68i8rGGFcGgEz7rkcHyiQpUkxJzJ1ACbbLmr8BfIINIBYgQVUXYLzZuT/XlutyGDkEy+DSB8IzgsLFlS0jSzVTPJZ3Ev45REsswUugZ8+LLampxHp+L1yehDetlAXM0h20m2mjxV+RpFC0XuPwi6xlhadSjBRCnr9o0Yf2NFBz1sXbAWr7d9xeVABw7X2FzcDCdcmWCO1aL60oagzzuxOYmnKxY9WnlZFpbLyJdq1UbQq+8ec9YsP+EPPrqPPA4L3pPlW23pOR3IFA2jU7/hZoTlPnPZoaMJhV+P+eMqhxSVFANE1Ls02Wzv3/vgv9zJQoCSEJht7i8XzmQ8j7iXrMpftCRl+S+09CCOG8vs/6T2ePmvRk458zz1Oi9XvXQmWl6gMfCnT8btWXdOD7T4f2npxzwVFsUBMKOEBFQGSCCx5lu8fcdGFM0gX1yz9zvoR3SAYcizvk+d6uH8W5EU6GPpYrz5RQ4a5tOilpxRBJkABfxF7lFmTHfRBojuc0QVBr69lmJrsk=");
            }

            @Override
            public String world() {
                return "world";
            }

            @Override
            public double x() {
                return 16.3445;
            }

            @Override
            public double y() {
                return 70.0;
            }

            @Override
            public double z() {
                return -71.9955;
            }

            @Override
            public float yaw() {
                return 97.8751f;
            }

            @Override
            public float pitch() {
                return 0.0f;
            }

            @Override
            public void onInteract(Player player, SkyBlockNPC npc) {
                FarmMerchantGUI gui = new FarmMerchantGUI();
                gui.open(player);
            }
        });
    }
}

