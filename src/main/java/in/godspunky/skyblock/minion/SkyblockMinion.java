package in.godspunky.skyblock.minion;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.item.SMinion;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.ItemBuilder;
import in.godspunky.skyblock.util.PaginationList;
import in.godspunky.skyblock.util.SUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @Getter
    private UUID uuid;


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
        this.uuid = UUID.randomUUID();
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

        this.armorStand.setHelmet(SUtil.getSkull("null" , SMaterial.SKULL_ITEM));
        this.armorStand.setChestplate(SUtil.colorLeatherArmor(new ItemBuilder("", Material.LEATHER_CHESTPLATE, 1).toItemStack(), this.leatherArmorColor));
        this.armorStand.setLeggings(SUtil.colorLeatherArmor(new ItemBuilder("", Material.LEATHER_LEGGINGS, 1).toItemStack(), this.leatherArmorColor));
        this.armorStand.setBoots(SUtil.colorLeatherArmor(new ItemBuilder("", Material.LEATHER_BOOTS, 1).toItemStack(), this.leatherArmorColor));
        this.armorStand.setItemInHand(ItemInHand);
        this.armorStand.setMetadata("uuid" , new FixedMetadataValue(Skyblock.getPlugin(), uuid.toString()));
        this.armorStand.setMetadata("owner" , new FixedMetadataValue(Skyblock.getPlugin(), owner.getUuid()));
        this.armorStand.setMetadata("minion", new FixedMetadataValue(Skyblock.getPlugin(), true));
        owner.minions.add(this);
        new BukkitRunnable(){

            @Override
            public void run() {
                if (armorStand == null || armorStand.isDead()) {
                    cancel();
                    return;
                }
                    System.out.println("[DEBUG] : generating resource!");
                    collect();
                }

        }.runTaskTimerAsynchronously(Skyblock.getPlugin() , 1 , actionDelay * 20L);

    }
    public void collect() {
        ArrayList<ItemStack> drops = sMinion.calculateDrops(this.level);

        Inventory inventory = Bukkit.createInventory(null, 54);

        this.inventory.forEach((stack) -> { if (stack != null) inventory.addItem(stack); });

        for (ItemStack drop : drops) {
            inventory.addItem(drop);
        }

        List<ItemStack> newInventory = new ArrayList<>();
        for (int i = 0; i < sMinion.getMaxStorage(level); ++i) {
            if (inventory.getItem(i) != null) newInventory.add(inventory.getItem(i));
        }

        this.inventory = newInventory;
    }
    public void openInventory(Player player){
        Inventory minionInventory = Bukkit.createInventory(null , 54);
        for (ItemStack itemStack : inventory){
            if (itemStack == null) continue;
            minionInventory.addItem(itemStack);
        }

        player.openInventory(minionInventory);
    }
}
