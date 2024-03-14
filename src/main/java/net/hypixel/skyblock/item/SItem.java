package net.hypixel.skyblock.item;

import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.item.armor.LeatherArmorStatistics;
import net.hypixel.skyblock.features.potion.PotionColor;
import net.hypixel.skyblock.features.potion.PotionEffect;
import net.hypixel.skyblock.features.potion.PotionEffectType;
import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.features.reforge.ReforgeType;
import net.hypixel.skyblock.util.SerialNBTTagCompound;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bson.Document;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;

import java.util.*;

public class SItem implements Cloneable, ConfigurationSerializable {
    private static final List<String> GLOBAL_NBT_TAGS;
    private static final List<String> GLOBAL_DATA_KEYS;
    private final SMaterial type;
    private final short variant;
    private final ItemStack stack;
    private final ItemLore lore;
    private Rarity rarity;
    private ItemOrigin origin;
    private boolean recombobulated;
    private final NBTTagCompound data;

    protected SItem(SMaterial type, short variant, ItemStack stack, Rarity rarity, ItemOrigin origin, boolean recombobulated, NBTTagCompound data, boolean overwrite) {
        this.type = type;
        this.variant = variant;
        this.stack = stack;
        this.rarity = rarity;
        this.data = data;
        this.lore = new ItemLore(this);
        this.origin = origin;
        this.recombobulated = recombobulated;
        if (overwrite) {
            ItemMeta meta = this.stack.getItemMeta();
            if (!(type.getStatistics() instanceof LoreableMaterialStatistics)) {
                meta.setLore(this.lore.asBukkitLore());
            } else {
                meta.setLore(((LoreableMaterialStatistics) type.getStatistics()).getCustomLore(this));
            }
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);
            meta.spigot().setUnbreakable(true);
            this.stack.setItemMeta(meta);
            this.update();
        }
    }

    public void enchant(boolean enchant) {
        if (enchant) {
            if (this.stack.getItemMeta().hasEnchants()) {
                return;
            }
            ItemMeta meta = this.stack.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            this.stack.setItemMeta(meta);
        } else {
            if (!this.stack.getItemMeta().hasEnchants()) {
                return;
            }
            ItemMeta meta = this.stack.getItemMeta();
            meta.removeEnchant(Enchantment.DURABILITY);
            this.stack.setItemMeta(meta);
        }
    }

    public boolean addEnchantment(EnchantmentType type, int level) {
        if (!this.isEnchantable()) {
            return false;
        }
        List<net.hypixel.skyblock.features.enchantment.Enchantment> enchantments = this.getEnchantments();
        net.hypixel.skyblock.features.enchantment.Enchantment enchantment = new net.hypixel.skyblock.features.enchantment.Enchantment(type, level);
        this.removeEnchantment(type);
        enchantments.add(enchantment);
        if (null != type.getVanilla()) {
            ItemMeta meta = this.stack.getItemMeta();
            meta.addEnchant(type.getVanilla(), level, true);
            this.stack.setItemMeta(meta);
        }
        NBTTagCompound es = this.data.getCompound("enchantments");
        for (net.hypixel.skyblock.features.enchantment.Enchantment e : enchantments) {
            es.setInt(e.getType().getNamespace(), e.getLevel());
        }
        this.data.set("enchantments", es);
        this.update();
        return true;
    }

    public boolean removeEnchantment(EnchantmentType type) {
        if (!this.isEnchantable()) {
            return false;
        }
        List<net.hypixel.skyblock.features.enchantment.Enchantment> enchantments = this.getEnchantments();
        boolean removeIf = enchantments.removeIf(e -> e.getType().equals(type));
        if (null != type.getVanilla()) {
            ItemMeta meta = this.stack.getItemMeta();
            meta.removeEnchant(type.getVanilla());
            this.stack.setItemMeta(meta);
        }
        NBTTagCompound es = new NBTTagCompound();
        for (net.hypixel.skyblock.features.enchantment.Enchantment enchantment : enchantments) {
            es.setInt(enchantment.getType().getNamespace(), enchantment.getLevel());
        }
        this.data.set("enchantments", es);
        this.update();
        return removeIf;
    }

    public Long getPrice() {
        if (!data.hasKey("price")) {
            return null;
        } else {
            return data.getLong("price");
        }
    }

    public Long getItemValue() {
        if (!data.hasKey("itemValue")) {
            return null;
        } else {
            return data.getLong("itemValue");
        }
    }

    public void setItemValue(final Long value) {
        data.setLong("itemValue", value);
        update();
    }

    public void setPrice(final Long value) {
        data.setLong("price", value);
        update();
    }

    public boolean hasEnchantment(EnchantmentType type) {
        if (!this.isEnchantable()) {
            return false;
        }
        List<net.hypixel.skyblock.features.enchantment.Enchantment> enchantments = this.getEnchantments();
        for (net.hypixel.skyblock.features.enchantment.Enchantment enchantment : enchantments) {
            if (enchantment.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public net.hypixel.skyblock.features.enchantment.Enchantment getEnchantment(EnchantmentType type) {
        if (!this.isEnchantable()) {
            return null;
        }
        List<net.hypixel.skyblock.features.enchantment.Enchantment> enchantments = this.getEnchantments();
        for (net.hypixel.skyblock.features.enchantment.Enchantment enchantment : enchantments) {
            if (enchantment.getType() == type) {
                return enchantment;
            }
        }
        return null;
    }

    public List<net.hypixel.skyblock.features.enchantment.Enchantment> getEnchantments() {
        if (!this.isEnchantable()) {
            return null;
        }
        NBTTagCompound es = this.data.hasKey("enchantments") ? this.data.getCompound("enchantments") : new NBTTagCompound();
        List<net.hypixel.skyblock.features.enchantment.Enchantment> enchantments = new ArrayList<net.hypixel.skyblock.features.enchantment.Enchantment>();
        for (String key : es.c()) {
            enchantments.add(new net.hypixel.skyblock.features.enchantment.Enchantment(EnchantmentType.getByNamespace(key), es.getInt(key)));
        }
        return enchantments;
    }

    public boolean addPotionEffect(PotionEffect effect) {
        if (!this.isPotion()) {
            return false;
        }
        List<PotionEffect> effects = this.getPotionEffects();
        this.removePotionEffect(effect.getType());
        effects.add(effect);
        NBTTagCompound es = this.data.getCompound("effects");
        for (PotionEffect e : effects) {
            es.set(e.getType().getNamespace(), e.toCompound());
        }
        this.data.set("effects", es);
        this.update();
        return true;
    }

    public boolean removePotionEffect(PotionEffectType type) {
        if (!this.isPotion()) {
            return false;
        }
        List<PotionEffect> effects = this.getPotionEffects();
        boolean removeIf = effects.removeIf(e -> e.getType().equals(type));
        PotionColor top = SUtil.getTopColor(this);
        this.stack.setDurability((null != top) ? (this.isSplashPotion() ? top.getSplashData() : top.getData()) : 0);
        NBTTagCompound es = this.data.getCompound("effects");
        for (final PotionEffect e : effects) {
            es.set(e.getType().getNamespace(), e.toCompound());
        }
        this.data.set("effects", es);
        this.update();
        return removeIf;
    }

    public PotionEffect getPotionEffect(PotionEffectType type) {
        if (!this.isPotion()) {
            return null;
        }
        List<PotionEffect> effects = this.getPotionEffects();
        for (PotionEffect effect : effects) {
            if (effect.getType() == type) {
                return effect;
            }
        }
        return null;
    }

    public List<PotionEffect> getPotionEffects() {
        if (!this.isPotion()) {
            return null;
        }
        NBTTagCompound es = this.data.hasKey("effects") ? this.data.getCompound("effects") : new NBTTagCompound();
        List<PotionEffect> effects = new ArrayList<PotionEffect>();
        for (String key : es.c()) {
            effects.add(PotionEffect.ofCompound(key, es.getCompound(key)));
        }
        return effects;
    }

    public boolean isEnchantable() {
        return this.type.getGenericInstance() instanceof Enchantable;
    }

    public boolean isReforgable() {
        return this.type.getGenericInstance() instanceof Reforgable;
    }

    public boolean isStarrable() {
        return GenericItemType.WEAPON == this.getType().getStatistics().getType() || GenericItemType.ARMOR == this.getType().getStatistics().getType() || GenericItemType.RANGED_WEAPON == this.getType().getStatistics().getType();
    }

    public boolean isDungeonsItem() {
        return this.getDataBoolean("dungeons_item");
    }

    public boolean isPotion() {
        return SMaterial.WATER_BOTTLE == this.type;
    }

    public int getStar() {
        return this.getDataInt("itemStar");
    }

    public boolean isSplashPotion() {
        return this.isPotion() && this.data.getBoolean("splash");
    }

    public void setAnvilUses(int anvilUses) {
        if (!(this.type.getGenericInstance() instanceof Enchantable)) {
            throw new UnsupportedOperationException("You cannot set the anvil uses on an unenchantable item");
        }
        this.data.setInt("anvil", anvilUses);
        this.update();
    }

    public void setKills(Integer kills) {
        if (!this.type.getStatistics().displayKills()) {
            throw new UnsupportedOperationException("You cannot display kills on this item");
        }
        this.data.setInt("kills", kills);
        this.update();
    }

    public void setHPBs(Integer hpbs) {
        this.data.setInt("hpb", hpbs);
        this.update();
    }

    public int getHPBs(){
        return this.getDataInt("hpb");
    }

    public void setCoinsBid(Integer coins) {
        if (!this.type.getStatistics().displayCoins()) {
            throw new UnsupportedOperationException("You cannot display coins bidded on this item");
        }
        this.data.setInt("coins", coins);
        this.update();
    }

    public void setRarity(Rarity rarity, boolean instanceUpdate) {
        this.rarity = rarity;
        this.update(instanceUpdate);
    }

    public void setRarity(Rarity rarity) {
        this.setRarity(rarity, true);
    }

    public void setAmount(int amount) {
        this.stack.setAmount(amount);
    }

    public void upgradeRarity() {
        this.rarity = this.rarity.upgrade();
        this.update();
    }

    public void downgradeRarity() {
        this.rarity = this.rarity.downgrade();
        this.update();
    }

    public void setReforge(Reforge reforge) {
        if (!(this.type.getGenericInstance() instanceof Reforgable)) {
            throw new UnsupportedOperationException("You cannot set the reforge of an unreforgable item");
        }
        this.data.setString("reforge", ReforgeType.getByClass(reforge.getClass()).name());
        this.update();
    }

    public void setOrigin(ItemOrigin origin) {
        this.origin = origin;
        this.update();
    }

    public void setRecombobulated(boolean recombobulated) {
        this.recombobulated = recombobulated;
        if (recombobulated) {
            this.setRarity(this.type.getStatistics().getRarity().upgrade());
        } else {
            this.setRarity(this.type.getStatistics().getRarity());
        }
        this.update();
    }

    public Reforge getReforge() {
        if (!(this.type.getGenericInstance() instanceof Reforgable)) {
            return null;
        }
        if (!this.data.hasKey("reforge")) {
            return null;
        }
        return ReforgeType.getReforgeType(this.data.getString("reforge")).getReforge();
    }

    public String getFullName() {
        return this.rarity.getColor() + (this.data.hasKey("reforge") ? (ReforgeType.getReforgeType(this.data.getString("reforge")).getReforge().getName() + " ") : "") + this.getType().getDisplayName(this.variant);
    }

    public String getDisplayName() {
        return this.getType().getDisplayName(this.variant);
    }

    public boolean isReforged() {
        return this.data.hasKey("reforge");
    }

    public String getDataString(String key) {
        return this.data.getString(key);
    }

    public void setDungeonsItem(boolean bol) {
        this.setDataBoolean("dungeons_item", bol);
    }

    public int getDataInt(String key) {
        return this.data.getInt(key);
    }

    public long getDataLong(String key) {
        return this.data.getLong(key);
    }

    public boolean getDataBoolean(String key) {
        return this.data.getBoolean(key);
    }

    public NBTTagCompound getDataCompound(String key) {
        return this.data.getCompound(key);
    }

    public void setDataString(String key, String value) {
        this.data.setString(key, value);
        this.update();
    }

    public void setDataInt(String key, int value) {
        this.data.setInt(key, value);
        this.update();
    }

    public void setDataDouble(String key, double value) {
        this.data.setDouble(key, value);
        this.update();
    }

    public void setDataFloat(String key, float value) {
        this.data.setFloat(key, value);
        this.update();
    }

    public void setDataLong(String key, long value) {
        this.data.setLong(key, value);
        this.update();
    }

    public void setDataBoolean(String key, boolean value) {
        this.data.setBoolean(key, value);
        this.update();
    }

    public void setDataCompound(String key, NBTTagCompound value) {
        this.data.set(key, value);
        this.update();
    }

    public void removeData(String key) {
        this.data.remove(key);
        this.update();
    }

    public boolean hasDataFor(String key) {
        return this.data.hasKey(key);
    }

    public void setDisplayName(String name) {
        Reforge reforge = null;
        if (this.data.hasKey("reforge")) {
            reforge = ReforgeType.getReforgeType(this.data.getString("reforge")).getReforge();
        }
        ItemMeta meta = this.stack.getItemMeta();
        if (null == reforge) {
            meta.setDisplayName(this.rarity.getColor() + name + Sputnik.createStarStringFrom(this));
        } else {
            meta.setDisplayName(this.rarity.getColor() + reforge.getName() + " " + name + Sputnik.createStarStringFrom(this));
        }
        this.stack.setItemMeta(meta);
    }

    public void update(boolean instanceUpdate) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(this.stack);
        if (null == nmsStack) {
            return;
        }
        NBTTagCompound compound = (null != nmsStack.getTag()) ? nmsStack.getTag() : new NBTTagCompound();
        compound.remove("type");
        compound.remove("variant");
        compound.remove("enchantments");
        compound.remove("anvil");
        compound.remove("rarity");
        compound.remove("kills");
        compound.remove("coins");
        compound.remove("reforge");
        compound.remove("origin");
        compound.remove("recombobulated");
        for (String key : this.data.c()) {
            compound.remove(key);
            if (!this.data.get(key).isEmpty()) {
                compound.set(key, this.data.get(key));
            }
        }
        compound.remove("amount");
        compound.setString("type", this.type.name());
        if (0 != this.variant) {
            compound.setShort("variant", this.variant);
        }
        if (this.rarity != this.type.getStatistics().getRarity()) {
            compound.setString("rarity", this.rarity.name());
        }
        if (ItemOrigin.UNKNOWN != this.origin) {
            compound.setString("origin", this.origin.name());
        }
        if (this.recombobulated) {
            compound.setBoolean("recombobulated", true);
        }
        if (!this.getType().getStatistics().isStackable() && !compound.hasKey("uuid")) {
            compound.setString("uuid", UUID.randomUUID().toString());
        }
        nmsStack.setTag(compound);
        this.stack.setItemMeta(CraftItemStack.getItemMeta(nmsStack));
        ItemMeta meta = this.stack.getItemMeta();
        MaterialStatistics statistics = this.type.getStatistics();
        Reforge reforge = null;
        if (this.data.hasKey("reforge")) {
            reforge = ReforgeType.getReforgeType(this.data.getString("reforge")).getReforge();
        }
        if (null == reforge) {
            meta.setDisplayName(this.rarity.getColor() + this.type.getDisplayName(this.variant) + Sputnik.createStarStringFrom(this));
        } else {
            meta.setDisplayName(this.rarity.getColor() + reforge.getName() + " " + this.type.getDisplayName(this.variant) + Sputnik.createStarStringFrom(this));
        }
        if (this.isPotion() && 0 < this.getPotionEffects().size()) {
            this.stack.setDurability(this.isSplashPotion() ? SUtil.getTopColor(this).getSplashData() : SUtil.getTopColor(this).getData());
        }
        if (!(statistics instanceof LoreableMaterialStatistics)) {
            meta.setLore(this.lore.asBukkitLore());
        } else {
            meta.setLore(((LoreableMaterialStatistics) statistics).getCustomLore(this));
        }
        this.stack.setItemMeta(meta);
        if (this.type.getGenericInstance() instanceof Enchantable || statistics.isEnchanted()) {
            this.enchant(0 != this.data.getCompound("enchantments").c().size() || statistics.isEnchanted());
        }
        MaterialFunction function = this.type.getFunction();
        if (null != function && instanceUpdate) {
            this.type.getFunction().onInstanceUpdate(this);
        }
    }

    public void update() {
        this.update(true);
    }

    public static SItem of(SMaterial specMaterial, short variant, ItemOrigin origin) {
        ItemStack stack = new ItemStack(specMaterial.getCraftMaterial(), 1, variant);
        MaterialStatistics statistics = specMaterial.getStatistics();
        if (Material.SKULL_ITEM == specMaterial.getCraftMaterial() && statistics instanceof SkullStatistics) {
            stack.setDurability((short) 3);
            SUtil.getSkull(((SkullStatistics) statistics).getURL(), stack, specMaterial);
        }
        if (statistics instanceof LeatherArmorStatistics) {
            SUtil.applyColorToLeatherArmor(stack, Color.fromRGB(((LeatherArmorStatistics) statistics).getColor()));
        }
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(statistics.getRarity().getColor() + specMaterial.getDisplayName(variant));
        stack.setItemMeta(meta);
        return new SItem(specMaterial, variant, stack, statistics.getRarity(), origin, false, (null != specMaterial.getItemData()) ? specMaterial.getItemData().getData() : new NBTTagCompound(), true);
    }

    public static SItem of(SMaterial specMaterial, ItemOrigin origin) {
        return of(specMaterial, specMaterial.getData(), origin);
    }

    public static SItem of(SMaterial specMaterial, short variant) {
        return of(specMaterial, variant, ItemOrigin.UNKNOWN);
    }

    public static SItem of(SMaterial specMaterial) {
        return of(specMaterial, specMaterial.getData());
    }

    public void setStarAmount(int star) {
        if (!this.getDataBoolean("dungeons_item")) {
            return;
        }
        this.setDataInt("itemStar", star);
    }

    public static SItem of(ItemStack stack, ItemOrigin origin) {
        if (null == stack) {
            return null;
        }
        SMaterial material = SMaterial.getSpecEquivalent(stack.getType(), stack.getDurability());
        if (null == material) {
            return null;
        }
        if (null == Item.getById(material.getCraftMaterial().getId())) {
            return null;
        }
        SItem n = of(material, stack.getDurability(), origin);
        n.getStack().setAmount(stack.getAmount());
        return n;
    }

    public static SItem of(ItemStack stack) {
        return of(stack, ItemOrigin.UNKNOWN);
    }

    public static boolean isSpecItem(ItemStack stack) {
        if (null == stack) {
            return false;
        }
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        if (null == nmsStack) {
            return false;
        }
        if (!nmsStack.hasTag()) {
            return false;
        }
        NBTTagCompound compound = nmsStack.getTag();
        return compound.hasKey("type");
    }

    public SItem clone() {
        return new SItem(this.type, this.variant, this.stack.clone(), this.rarity, this.origin, this.recombobulated, this.data, true);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SItem)) {
            return false;
        }
        SItem item = (SItem) o;
        return this.type == item.type && this.variant == item.variant && this.stack.equals(item.stack) && this.rarity == item.rarity && this.origin == item.origin && this.recombobulated == item.recombobulated && this.data.equals(item.data);
    }

    public NBTTagCompound toCompound() {
        NBTTagCompound compound = new NBTTagCompound();
        for (String key : this.data.c()) {
            compound.remove(key);
            compound.set(key, this.data.get(key));
        }
        compound.setString("type", this.type.name());
        if (0 != this.variant) {
            compound.setShort("variant", this.variant);
        }
        compound.setInt("amount", this.stack.getAmount());
        if (this.rarity != this.type.getStatistics().getRarity()) {
            compound.setString("rarity", this.rarity.name());
        }
        if (ItemOrigin.UNKNOWN != this.origin) {
            compound.setString("origin", this.origin.name());
        }
        if (this.recombobulated) {
            compound.setBoolean("recombobulated", true);
        }
        if (!this.getType().getStatistics().isStackable() && !compound.hasKey("uuid")) {
            compound.setString("uuid", UUID.randomUUID().toString());
        }
        return compound;
    }

    public static Document serializeSItem(SItem item) {
        Document itemDocument = new Document("type", item.getType().name())
                .append("variant", item.getVariant())
                .append("amount", item.getStack().getAmount())
                .append("rarity", item.getRarity().name())
                .append("origin", item.getOrigin().name())
                .append("recombobulated", item.isRecombobulated());

        for (String key : item.getData().c()) {
            if (key.equals("display")) {
                continue;
            }

            if (item.getData().get(key) instanceof NBTTagCompound) {
                SerialNBTTagCompound serial = new SerialNBTTagCompound(item.getData().getCompound(key));
                for (Map.Entry<String, Object> entry : serial.serialize().entrySet()) {
                    itemDocument.append(key + "." + entry.getKey(), entry.getValue());
                }
            } else {
                itemDocument.append(key, SUtil.getObjectFromCompound(item.getData(), key));
            }
        }

        return itemDocument;
    }

    public static SItem deserializeSItem(Document document) {
        if (document == null) {
            return null;
        }
        NBTTagCompound data = new NBTTagCompound();
        for (String key : document.keySet()) {
            if (SItem.GLOBAL_DATA_KEYS.contains(key)) {
                continue;
            }
            String[] dir = key.split("\\.");
            if (dir.length >= 2) {
                String lastKey = dir[dir.length - 1];
                NBTTagCompound track = data;
                for (int i = 0; i < dir.length - 1; ++i) {
                    if (!track.hasKey(dir[i])) {
                        track.set(dir[i], new NBTTagCompound());
                    }
                    track = track.getCompound(dir[i]);
                }
                track.set(lastKey, SUtil.getBaseFromObject(document.get(key)));
            } else {
                data.set(key, SUtil.getBaseFromObject(document.get(key)));
            }
        }
        SMaterial material = SMaterial.getMaterial(document.getString("type"));
        if (material == null) {
            return null;
        }
        Short variant = (short) document.getInteger("variant", (int) (short) 0);
        int amount = document.getInteger("amount", 0);
        Rarity rarity = Rarity.getRarity(document.getString("rarity"));
        if (rarity == null) {

            return null;
        }
        ItemOrigin origin = ItemOrigin.valueOf(document.getString("origin"));
        if (origin == null) {
            return null;
        }
        boolean recombobulated = document.getBoolean("recombobulated", false);

        return new SItem(material, variant, new ItemStack(material.getCraftMaterial(), amount, variant), rarity, origin, recombobulated, data, true);
    }




    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", this.type.name());
        map.put("variant", this.variant);
        map.put("amount", this.stack.getAmount());
        map.put("rarity", this.rarity.name());
        map.put("origin", this.origin.name());
        map.put("recombobulated", this.recombobulated);
        for (String k : this.data.c()) {
            if (k.equals("display")) {
                continue;
            }
            if (this.data.get(k) instanceof NBTTagCompound) {
                SerialNBTTagCompound serial = new SerialNBTTagCompound(this.data.getCompound(k));
                for (Map.Entry<String, Object> entry : serial.serialize().entrySet()) {
                    map.put(k + "." + entry.getKey(), entry.getValue());
                }
            } else {
                map.put(k, SUtil.getObjectFromCompound(this.data, k));
            }
        }
        return map;
    }

    public static SItem deserialize(Map<String, Object> map) {
        NBTTagCompound data = new NBTTagCompound();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (GLOBAL_DATA_KEYS.contains(entry.getKey())) {
                continue;
            }
            String key = entry.getKey();
            String[] dir = entry.getKey().split("\\.");
            if (2 <= dir.length) {
                key = dir[dir.length - 1];
                NBTTagCompound track = data;
                for (int i = 0; i < dir.length - 1; ++i) {
                    if (!track.hasKey(dir[i])) {
                        track.set(dir[i], new NBTTagCompound());
                    }
                    track = track.getCompound(dir[i]);
                }
                track.set(key, SUtil.getBaseFromObject(entry.getValue()));
            } else {
                data.set(key, SUtil.getBaseFromObject(entry.getValue()));
            }
        }
        SMaterial material = SMaterial.getMaterial((String) map.get("type"));
        short variant = (short) map.get("variant");
        return new SItem(material, variant, new ItemStack(material.getCraftMaterial(), (int) map.get("amount"), variant), Rarity.getRarity((String) map.get("rarity")), ItemOrigin.valueOf((String) map.get("origin")), (Boolean) map.get("recombobulated"), data, true);
    }


    @Override
    public String toString() {
        return "SItem{type=" + this.type.name() + ", variant=" + this.variant + ", stack=" + this.stack.toString() + ", rarity=" + this.rarity.name() + ", origin=" + this.origin.name() + ", recombobulated=" + this.recombobulated + ", data=" + this.data.toString() + "}";
    }

    public static SItem find(ItemStack stack) {
        if (null == stack) {
            return null;
        }
        if (!isSpecItem(stack)) {
            return null;
        }
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound compound = nmsStack.getTag();
        if (null == compound) {
            return null;
        }
        return of(compound, stack);
    }

    public static SItem of(NBTTagCompound compound) {
        SMaterial type = SMaterial.getMaterial(compound.getString("type"));
        ItemStack stack = new ItemStack(type.getCraftMaterial(), compound.hasKey("amount") ? compound.getInt("amount") : 1, type.getData());
        MaterialStatistics statistics = type.getStatistics();
        if (Material.SKULL_ITEM == type.getCraftMaterial() && statistics instanceof SkullStatistics) {
            stack.setDurability((short) 3);
            SUtil.getSkull(((SkullStatistics) statistics).getURL(), stack, type);
        }
        if (statistics instanceof LeatherArmorStatistics) {
            SUtil.applyColorToLeatherArmor(stack, Color.fromRGB(((LeatherArmorStatistics) statistics).getColor()));
        }
        ItemMeta meta = stack.getItemMeta();
        short variant = (short) (compound.hasKey("variant") ? compound.getShort("variant") : 0);
        meta.setDisplayName(statistics.getRarity().getColor() + type.getDisplayName(variant));
        stack.setItemMeta(meta);
        NBTTagCompound data = new NBTTagCompound();
        for (String key : compound.c()) {
            if (GLOBAL_NBT_TAGS.contains(key)) {
                continue;
            }
            data.set(key, compound.get(key));
        }
        return new SItem(type, variant, stack, compound.hasKey("rarity") ? Rarity.getRarity(compound.getString("rarity")) : statistics.getRarity(), compound.hasKey("origin") ? ItemOrigin.valueOf(compound.getString("origin")) : ItemOrigin.UNKNOWN, compound.getBoolean("recombobulated"), data, true);
    }

    public static SItem convert(ItemStack stack) {
        return SUtil.setSItemAmount(of(SMaterial.getSpecEquivalent(stack.getType(), stack.getDurability())), stack.getAmount());
    }

    private static SItem of(NBTTagCompound compound, ItemStack stack) {
        SMaterial type = SMaterial.getMaterial(compound.getString("type"));
        NBTTagCompound data = new NBTTagCompound();
        for (String key : compound.c()) {
            if (GLOBAL_NBT_TAGS.contains(key)) {
                continue;
            }
            data.set(key, compound.get(key));
        }
        return new SItem(type, compound.hasKey("variant") ? compound.getShort("variant") : 0, stack, compound.hasKey("rarity") ? Rarity.getRarity(compound.getString("rarity")) : type.getStatistics().getRarity(), compound.hasKey("origin") ? ItemOrigin.valueOf(compound.getString("origin")) : ItemOrigin.UNKNOWN, compound.hasKey("recombobulated"), data, false);
    }

    public static boolean isAbleToDoEtherWarpTeleportation(Player player, SItem sitem) {
        boolean haveBlockInRange = false;
        try {
            for (int range = 1; 57 > range; ++range) {
                Location location = player.getTargetBlock((Set) null, range).getLocation();
                if (Material.AIR != location.getBlock().getType()) {
                    haveBlockInRange = true;
                    break;
                }
            }
        } catch (IllegalStateException ex) {
        }
        return haveBlockInRange;
    }

    public static void etherWarpTeleportation(Player player, SItem sitem) {
        if (!Sputnik.tpAbilUsable(player)) {
            return;
        }
        boolean haveBlockInRange = false;
        try {
            for (int range = 1; 57 > range; ++range) {
                Location location = player.getTargetBlock((Set) null, range).getLocation();
                if (Material.AIR != location.getBlock().getType()) {
                    haveBlockInRange = true;
                    break;
                }
            }
        } catch (IllegalStateException ex) {
        }
        if (!haveBlockInRange) {
            return;
        }
        try {
            int f_ = 57;
            for (int range2 = 1; 57 > range2; ++range2) {
                Location location2 = player.getTargetBlock((Set) null, range2).getLocation();
                if (Material.AIR != location2.getBlock().getType()) {
                    f_ = range2;
                    break;
                }
            }
            Location location = player.getTargetBlock((Set) null, f_).getLocation().getBlock().getLocation();
            if (Material.AIR != location.clone().add(0.0, 1.0, 0.0).getBlock().getType() && Material.AIR != location.clone().add(0.0, 2.0, 0.0).getBlock().getType()) {
                location = player.getTargetBlock((Set) null, f_ - 1).getLocation().getBlock().getLocation();
            }
            location.setYaw(player.getLocation().getYaw());
            location.setPitch(player.getLocation().getPitch());
            location.add(0.5, 1.0, 0.5);
            if (1 < f_) {
                Sputnik.teleport(player, location);
            } else {
                Sputnik.teleport(player, player.getLocation());
            }
        } catch (IllegalStateException ex2) {
        }
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 3.0f, 1.0f);
    }

    public SMaterial getType() {
        return this.type;
    }

    public short getVariant() {
        return this.variant;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public ItemLore getLore() {
        return this.lore;
    }

    public Rarity getRarity() {
        return this.rarity;
    }

    public ItemOrigin getOrigin() {
        return this.origin;
    }

    public boolean isRecombobulated() {
        return this.recombobulated;
    }

    public NBTTagCompound getData() {
        return this.data;
    }

    static {
        GLOBAL_NBT_TAGS = Arrays.asList("type", "rarity", "origin", "recombobulated");
        GLOBAL_DATA_KEYS = Arrays.asList("type", "variant", "stack", "rarity", "origin", "recombobulated");
    }
}
