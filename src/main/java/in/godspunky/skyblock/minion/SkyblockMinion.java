package in.godspunky.skyblock.minion;

import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.item.SMinion;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.ItemBuilder;
import in.godspunky.skyblock.util.SUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SkyblockMinion {
    private SMinion sMinion;
    private String name;
    private int level;
    private Location location;
    private List<ItemStack> inventory;
    private ArmorStand armorStand;
    private ItemStack ItemInHand;
    private ArrayList<ItemStack> drops;
    private int actionDelay;
    private User owner;
    private Color leatherArmorColor;

    public SkyblockMinion(SMaterial sMaterial , int level , Location location , User owner) {
        this.sMinion = sMaterial.getSMinion();
        if (sMinion == null) return;
        this.name = sMinion.getDisplayName();
        this.ItemInHand = sMinion.getHand(level);
        this.location = location;
        this.level = level;
        this.inventory = new ArrayList<>();
        this.armorStand = null;
        this.ItemInHand = new ItemStack(Material.WOOD_PICKAXE);
        this.drops = sMinion.calculateDrops(level);
        this.actionDelay = sMinion.getActionDelay(level);
        this.owner = owner;
        this.leatherArmorColor = Color.fromRGB(72, 71, 59);

    }

    public void spawn() {
        this.armorStand = location.getWorld().spawn(location , ArmorStand.class);
        this.armorStand.setCustomName(name + " " + level);
        this.armorStand.setCustomNameVisible(true);
        this.armorStand.setSmall(true);
        this.armorStand.setGravity(false);
        this.armorStand.setVisible(true);
        this.armorStand.setArms(true);
        this.armorStand.setBasePlate(false);
        this.armorStand.setCanPickupItems(false);

        this.armorStand.setHelmet(new ItemStack(Material.SKULL_ITEM));
        this.armorStand.setChestplate(SUtil.colorLeatherArmor(new ItemBuilder("", Material.LEATHER_CHESTPLATE, 1).toItemStack(), this.leatherArmorColor));
        this.armorStand.setLeggings(SUtil.colorLeatherArmor(new ItemBuilder("", Material.LEATHER_LEGGINGS, 1).toItemStack(), this.leatherArmorColor));
        this.armorStand.setBoots(SUtil.colorLeatherArmor(new ItemBuilder("", Material.LEATHER_BOOTS, 1).toItemStack(), this.leatherArmorColor));
        this.armorStand.setItemInHand(ItemInHand);

    }

}
