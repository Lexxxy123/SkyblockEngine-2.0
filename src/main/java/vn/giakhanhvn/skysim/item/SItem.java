package vn.giakhanhvn.skysim.item;

import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
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
import vn.giakhanhvn.skysim.enchantment.EnchantmentType;
import vn.giakhanhvn.skysim.item.armor.LeatherArmorStatistics;
import vn.giakhanhvn.skysim.potion.PotionColor;
import vn.giakhanhvn.skysim.potion.PotionEffect;
import vn.giakhanhvn.skysim.potion.PotionEffectType;
import vn.giakhanhvn.skysim.reforge.Reforge;
import vn.giakhanhvn.skysim.reforge.ReforgeType;
import vn.giakhanhvn.skysim.util.SUtil;
import vn.giakhanhvn.skysim.util.SerialNBTTagCompound;
import vn.giakhanhvn.skysim.util.Sputnik;

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

    protected SItem(final SMaterial type, final short variant, final ItemStack stack, final Rarity rarity, final ItemOrigin origin, final boolean recombobulated, final NBTTagCompound data, final boolean overwrite) {
        this.type = type;
        this.variant = variant;
        this.stack = stack;
        this.rarity = rarity;
        this.data = data;
        this.lore = new ItemLore(this);
        this.origin = origin;
        this.recombobulated = recombobulated;
        if (overwrite) {
            final ItemMeta meta = this.stack.getItemMeta();
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

    public void enchant(final boolean enchant) {
        if (enchant) {
            if (this.stack.getItemMeta().hasEnchants()) {
                return;
            }
            final ItemMeta meta = this.stack.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            this.stack.setItemMeta(meta);
        } else {
            if (!this.stack.getItemMeta().hasEnchants()) {
                return;
            }
            final ItemMeta meta = this.stack.getItemMeta();
            meta.removeEnchant(Enchantment.DURABILITY);
            this.stack.setItemMeta(meta);
        }
    }

    public boolean addEnchantment(final EnchantmentType type, final int level) {
        if (!this.isEnchantable()) {
            return false;
        }
        final List<vn.giakhanhvn.skysim.enchantment.Enchantment> enchantments = this.getEnchantments();
        final vn.giakhanhvn.skysim.enchantment.Enchantment enchantment = new vn.giakhanhvn.skysim.enchantment.Enchantment(type, level);
        this.removeEnchantment(type);
        enchantments.add(enchantment);
        if (type.getVanilla() != null) {
            final ItemMeta meta = this.stack.getItemMeta();
            meta.addEnchant(type.getVanilla(), level, true);
            this.stack.setItemMeta(meta);
        }
        final NBTTagCompound es = this.data.getCompound("enchantments");
        for (final vn.giakhanhvn.skysim.enchantment.Enchantment e : enchantments) {
            es.setInt(e.getType().getNamespace(), e.getLevel());
        }
        this.data.set("enchantments", es);
        this.update();
        return true;
    }

    public boolean removeEnchantment(final EnchantmentType type) {
        if (!this.isEnchantable()) {
            return false;
        }
        final List<vn.giakhanhvn.skysim.enchantment.Enchantment> enchantments = this.getEnchantments();
        final boolean removeIf = enchantments.removeIf(e -> e.getType().equals(type));
        if (type.getVanilla() != null) {
            final ItemMeta meta = this.stack.getItemMeta();
            meta.removeEnchant(type.getVanilla());
            this.stack.setItemMeta(meta);
        }
        final NBTTagCompound es = new NBTTagCompound();
        for (final vn.giakhanhvn.skysim.enchantment.Enchantment enchantment : enchantments) {
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

    public void setItemValue(Long value) {
        data.setLong("itemValue", value);
        update();
    }

    public void setPrice(Long value) {
        data.setLong("price", value);
        update();
    }

    public boolean hasEnchantment(final EnchantmentType type) {
        if (!this.isEnchantable()) {
            return false;
        }
        final List<vn.giakhanhvn.skysim.enchantment.Enchantment> enchantments = this.getEnchantments();
        for (final vn.giakhanhvn.skysim.enchantment.Enchantment enchantment : enchantments) {
            if (enchantment.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public vn.giakhanhvn.skysim.enchantment.Enchantment getEnchantment(final EnchantmentType type) {
        if (!this.isEnchantable()) {
            return null;
        }
        final List<vn.giakhanhvn.skysim.enchantment.Enchantment> enchantments = this.getEnchantments();
        for (final vn.giakhanhvn.skysim.enchantment.Enchantment enchantment : enchantments) {
            if (enchantment.getType() == type) {
                return enchantment;
            }
        }
        return null;
    }

    public List<vn.giakhanhvn.skysim.enchantment.Enchantment> getEnchantments() {
        if (!this.isEnchantable()) {
            return null;
        }
        final NBTTagCompound es = this.data.hasKey("enchantments") ? this.data.getCompound("enchantments") : new NBTTagCompound();
        final List<vn.giakhanhvn.skysim.enchantment.Enchantment> enchantments = new ArrayList<vn.giakhanhvn.skysim.enchantment.Enchantment>();
        for (final String key : es.c()) {
            enchantments.add(new vn.giakhanhvn.skysim.enchantment.Enchantment(EnchantmentType.getByNamespace(key), es.getInt(key)));
        }
        return enchantments;
    }

    public boolean addPotionEffect(final PotionEffect effect) {
        if (!this.isPotion()) {
            return false;
        }
        final List<PotionEffect> effects = this.getPotionEffects();
        this.removePotionEffect(effect.getType());
        effects.add(effect);
        final NBTTagCompound es = this.data.getCompound("effects");
        for (final PotionEffect e : effects) {
            es.set(e.getType().getNamespace(), e.toCompound());
        }
        this.data.set("effects", es);
        this.update();
        return true;
    }

    public boolean removePotionEffect(final PotionEffectType type) {
        if (!this.isPotion()) {
            return false;
        }
        final List<PotionEffect> effects = this.getPotionEffects();
        final boolean removeIf = effects.removeIf(e -> e.getType().equals(type));
        final PotionColor top = SUtil.getTopColor(this);
        this.stack.setDurability((top != null) ? (this.isSplashPotion() ? top.getSplashData() : top.getData()) : 0);
        final NBTTagCompound es = this.data.getCompound("effects");
        for (PotionEffect e : effects) {
            es.set(e.getType().getNamespace(), e.toCompound());
        }
        this.data.set("effects", es);
        this.update();
        return removeIf;
    }

    public PotionEffect getPotionEffect(final PotionEffectType type) {
        if (!this.isPotion()) {
            return null;
        }
        final List<PotionEffect> effects = this.getPotionEffects();
        for (final PotionEffect effect : effects) {
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
        final NBTTagCompound es = this.data.hasKey("effects") ? this.data.getCompound("effects") : new NBTTagCompound();
        final List<PotionEffect> effects = new ArrayList<PotionEffect>();
        for (final String key : es.c()) {
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
        return this.getType().getStatistics().getType() == GenericItemType.WEAPON || this.getType().getStatistics().getType() == GenericItemType.ARMOR || this.getType().getStatistics().getType() == GenericItemType.RANGED_WEAPON;
    }

    public boolean isDungeonsItem() {
        return this.getDataBoolean("dungeons_item");
    }

    public boolean isPotion() {
        return this.type == SMaterial.WATER_BOTTLE;
    }

    public int getStar() {
        return this.getDataInt("itemStar");
    }

    public boolean isSplashPotion() {
        return this.isPotion() && this.data.getBoolean("splash");
    }

    public void setAnvilUses(final int anvilUses) {
        if (!(this.type.getGenericInstance() instanceof Enchantable)) {
            throw new UnsupportedOperationException("You cannot set the anvil uses on an unenchantable item");
        }
        this.data.setInt("anvil", anvilUses);
        this.update();
    }

    public void setKills(final Integer kills) {
        if (!this.type.getStatistics().displayKills()) {
            throw new UnsupportedOperationException("You cannot display kills on this item");
        }
        this.data.setInt("kills", kills);
        this.update();
    }

    public void setHPBs(final Integer hpbs) {
        this.data.setInt("hpb", hpbs);
        this.update();
    }

    public void setCoinsBid(final Integer coins) {
        if (!this.type.getStatistics().displayCoins()) {
            throw new UnsupportedOperationException("You cannot display coins bidded on this item");
        }
        this.data.setInt("coins", coins);
        this.update();
    }

    public void setRarity(final Rarity rarity, final boolean instanceUpdate) {
        this.rarity = rarity;
        this.update(instanceUpdate);
    }

    public void setRarity(final Rarity rarity) {
        this.setRarity(rarity, true);
    }

    public void setAmount(final int amount) {
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

    public void setReforge(final Reforge reforge) {
        if (!(this.type.getGenericInstance() instanceof Reforgable)) {
            throw new UnsupportedOperationException("You cannot set the reforge of an unreforgable item");
        }
        this.data.setString("reforge", ReforgeType.getByClass(reforge.getClass()).name());
        this.update();
    }

    public void setOrigin(final ItemOrigin origin) {
        this.origin = origin;
        this.update();
    }

    public void setRecombobulated(final boolean recombobulated) {
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

    public String getDataString(final String key) {
        return this.data.getString(key);
    }

    public void setDungeonsItem(final boolean bol) {
        this.setDataBoolean("dungeons_item", bol);
    }

    public int getDataInt(final String key) {
        return this.data.getInt(key);
    }

    public long getDataLong(final String key) {
        return this.data.getLong(key);
    }

    public boolean getDataBoolean(final String key) {
        return this.data.getBoolean(key);
    }

    public NBTTagCompound getDataCompound(final String key) {
        return this.data.getCompound(key);
    }

    public void setDataString(final String key, final String value) {
        this.data.setString(key, value);
        this.update();
    }

    public void setDataInt(final String key, final int value) {
        this.data.setInt(key, value);
        this.update();
    }

    public void setDataDouble(final String key, final double value) {
        this.data.setDouble(key, value);
        this.update();
    }

    public void setDataFloat(final String key, final float value) {
        this.data.setFloat(key, value);
        this.update();
    }

    public void setDataLong(final String key, final long value) {
        this.data.setLong(key, value);
        this.update();
    }

    public void setDataBoolean(final String key, final boolean value) {
        this.data.setBoolean(key, value);
        this.update();
    }

    public void setDataCompound(final String key, final NBTTagCompound value) {
        this.data.set(key, value);
        this.update();
    }

    public void removeData(final String key) {
        this.data.remove(key);
        this.update();
    }

    public boolean hasDataFor(final String key) {
        return this.data.hasKey(key);
    }

    public void setDisplayName(final String name) {
        Reforge reforge = null;
        if (this.data.hasKey("reforge")) {
            reforge = ReforgeType.getReforgeType(this.data.getString("reforge")).getReforge();
        }
        final ItemMeta meta = this.stack.getItemMeta();
        if (reforge == null) {
            meta.setDisplayName(this.rarity.getColor() + name + Sputnik.createStarStringFrom(this));
        } else {
            meta.setDisplayName(this.rarity.getColor() + reforge.getName() + " " + name + Sputnik.createStarStringFrom(this));
        }
        this.stack.setItemMeta(meta);
    }

    public void update(final boolean instanceUpdate) {
        final net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(this.stack);
        if (nmsStack == null) {
            return;
        }
        final NBTTagCompound compound = (nmsStack.getTag() != null) ? nmsStack.getTag() : new NBTTagCompound();
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
        for (final String key : this.data.c()) {
            compound.remove(key);
            if (!this.data.get(key).isEmpty()) {
                compound.set(key, this.data.get(key));
            }
        }
        compound.remove("amount");
        compound.setString("type", this.type.name());
        if (this.variant != 0) {
            compound.setShort("variant", this.variant);
        }
        if (this.rarity != this.type.getStatistics().getRarity()) {
            compound.setString("rarity", this.rarity.name());
        }
        if (this.origin != ItemOrigin.UNKNOWN) {
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
        final ItemMeta meta = this.stack.getItemMeta();
        final MaterialStatistics statistics = this.type.getStatistics();
        Reforge reforge = null;
        if (this.data.hasKey("reforge")) {
            reforge = ReforgeType.getReforgeType(this.data.getString("reforge")).getReforge();
        }
        if (reforge == null) {
            meta.setDisplayName(this.rarity.getColor() + this.type.getDisplayName(this.variant) + Sputnik.createStarStringFrom(this));
        } else {
            meta.setDisplayName(this.rarity.getColor() + reforge.getName() + " " + this.type.getDisplayName(this.variant) + Sputnik.createStarStringFrom(this));
        }
        if (this.isPotion() && this.getPotionEffects().size() > 0) {
            this.stack.setDurability(this.isSplashPotion() ? SUtil.getTopColor(this).getSplashData() : SUtil.getTopColor(this).getData());
        }
        if (!(statistics instanceof LoreableMaterialStatistics)) {
            meta.setLore(this.lore.asBukkitLore());
        } else {
            meta.setLore(((LoreableMaterialStatistics) statistics).getCustomLore(this));
        }
        this.stack.setItemMeta(meta);
        if (this.type.getGenericInstance() instanceof Enchantable || statistics.isEnchanted()) {
            this.enchant(this.data.getCompound("enchantments").c().size() != 0 || statistics.isEnchanted());
        }
        final MaterialFunction function = this.type.getFunction();
        if (function != null && instanceUpdate) {
            this.type.getFunction().onInstanceUpdate(this);
        }
    }

    public void update() {
        this.update(true);
    }

    public static SItem of(final SMaterial specMaterial, final short variant, final ItemOrigin origin) {
        final ItemStack stack = new ItemStack(specMaterial.getCraftMaterial(), 1, variant);
        final MaterialStatistics statistics = specMaterial.getStatistics();
        if (specMaterial.getCraftMaterial() == Material.SKULL_ITEM && statistics instanceof SkullStatistics) {
            stack.setDurability((short) 3);
            SUtil.getSkull(((SkullStatistics) statistics).getURL(), stack, specMaterial);
        }
        if (statistics instanceof LeatherArmorStatistics) {
            SUtil.applyColorToLeatherArmor(stack, Color.fromRGB(((LeatherArmorStatistics) statistics).getColor()));
        }
        final ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(statistics.getRarity().getColor() + specMaterial.getDisplayName(variant));
        stack.setItemMeta(meta);
        return new SItem(specMaterial, variant, stack, statistics.getRarity(), origin, false, (specMaterial.getItemData() != null) ? specMaterial.getItemData().getData() : new NBTTagCompound(), true);
    }

    public static SItem of(final SMaterial specMaterial, final ItemOrigin origin) {
        return of(specMaterial, specMaterial.getData(), origin);
    }

    public static SItem of(final SMaterial specMaterial, final short variant) {
        return of(specMaterial, variant, ItemOrigin.UNKNOWN);
    }

    public static SItem of(final SMaterial specMaterial) {
        return of(specMaterial, specMaterial.getData());
    }

    public void setStarAmount(final int star) {
        if (!this.getDataBoolean("dungeons_item")) {
            return;
        }
        this.setDataInt("itemStar", star);
    }

    public static SItem of(final ItemStack stack, final ItemOrigin origin) {
        if (stack == null) {
            return null;
        }
        final SMaterial material = SMaterial.getSpecEquivalent(stack.getType(), stack.getDurability());
        if (material == null) {
            return null;
        }
        if (Item.getById(material.getCraftMaterial().getId()) == null) {
            return null;
        }
        final SItem n = of(material, stack.getDurability(), origin);
        n.getStack().setAmount(stack.getAmount());
        return n;
    }

    public static SItem of(final ItemStack stack) {
        return of(stack, ItemOrigin.UNKNOWN);
    }

    public static boolean isSpecItem(final ItemStack stack) {
        if (stack == null) {
            return false;
        }
        final net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        if (nmsStack == null) {
            return false;
        }
        if (!nmsStack.hasTag()) {
            return false;
        }
        final NBTTagCompound compound = nmsStack.getTag();
        return compound.hasKey("type");
    }

    public SItem clone() {
        return new SItem(this.type, this.variant, this.stack.clone(), this.rarity, this.origin, this.recombobulated, this.data, true);
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof SItem)) {
            return false;
        }
        final SItem item = (SItem) o;
        return this.type == item.type && this.variant == item.variant && this.stack.equals(item.stack) && this.rarity == item.rarity && this.origin == item.origin && this.recombobulated == item.recombobulated && this.data.equals(item.data);
    }

    public NBTTagCompound toCompound() {
        final NBTTagCompound compound = new NBTTagCompound();
        for (final String key : this.data.c()) {
            compound.remove(key);
            compound.set(key, this.data.get(key));
        }
        compound.setString("type", this.type.name());
        if (this.variant != 0) {
            compound.setShort("variant", this.variant);
        }
        compound.setInt("amount", this.stack.getAmount());
        if (this.rarity != this.type.getStatistics().getRarity()) {
            compound.setString("rarity", this.rarity.name());
        }
        if (this.origin != ItemOrigin.UNKNOWN) {
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

    public Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", this.type.name());
        map.put("variant", this.variant);
        map.put("amount", this.stack.getAmount());
        map.put("rarity", this.rarity.name());
        map.put("origin", this.origin.name());
        map.put("recombobulated", this.recombobulated);
        for (final String k : this.data.c()) {
            if (k.equals("display")) {
                continue;
            }
            if (this.data.get(k) instanceof NBTTagCompound) {
                final SerialNBTTagCompound serial = new SerialNBTTagCompound(this.data.getCompound(k));
                for (final Map.Entry<String, Object> entry : serial.serialize().entrySet()) {
                    map.put(k + "." + entry.getKey(), entry.getValue());
                }
            } else {
                map.put(k, SUtil.getObjectFromCompound(this.data, k));
            }
        }
        return map;
    }

    public static SItem deserialize(final Map<String, Object> map) {
        final NBTTagCompound data = new NBTTagCompound();
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            if (SItem.GLOBAL_DATA_KEYS.contains(entry.getKey())) {
                continue;
            }
            String key = entry.getKey();
            final String[] dir = entry.getKey().split("\\.");
            if (dir.length >= 2) {
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
        final SMaterial material = SMaterial.getMaterial((String) map.get("type"));
        final short variant = (short) map.get("variant");
        return new SItem(material, variant, new ItemStack(material.getCraftMaterial(), (int) map.get("amount"), variant), Rarity.getRarity((String) map.get("rarity")), ItemOrigin.valueOf((String) map.get("origin")), (Boolean) map.get("recombobulated"), data, true);
    }

    @Override
    public String toString() {
        return "SItem{type=" + this.type.name() + ", variant=" + this.variant + ", stack=" + this.stack.toString() + ", rarity=" + this.rarity.name() + ", origin=" + this.origin.name() + ", recombobulated=" + this.recombobulated + ", data=" + this.data.toString() + "}";
    }

    public static SItem find(final ItemStack stack) {
        if (stack == null) {
            return null;
        }
        if (!isSpecItem(stack)) {
            return null;
        }
        final net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        final NBTTagCompound compound = nmsStack.getTag();
        if (compound == null) {
            return null;
        }
        return of(compound, stack);
    }

    public static SItem of(final NBTTagCompound compound) {
        final SMaterial type = SMaterial.getMaterial(compound.getString("type"));
        final ItemStack stack = new ItemStack(type.getCraftMaterial(), compound.hasKey("amount") ? compound.getInt("amount") : 1, type.getData());
        final MaterialStatistics statistics = type.getStatistics();
        if (type.getCraftMaterial() == Material.SKULL_ITEM && statistics instanceof SkullStatistics) {
            stack.setDurability((short) 3);
            SUtil.getSkull(((SkullStatistics) statistics).getURL(), stack, type);
        }
        if (statistics instanceof LeatherArmorStatistics) {
            SUtil.applyColorToLeatherArmor(stack, Color.fromRGB(((LeatherArmorStatistics) statistics).getColor()));
        }
        final ItemMeta meta = stack.getItemMeta();
        final short variant = (short) (compound.hasKey("variant") ? compound.getShort("variant") : 0);
        meta.setDisplayName(statistics.getRarity().getColor() + type.getDisplayName(variant));
        stack.setItemMeta(meta);
        final NBTTagCompound data = new NBTTagCompound();
        for (final String key : compound.c()) {
            if (SItem.GLOBAL_NBT_TAGS.contains(key)) {
                continue;
            }
            data.set(key, compound.get(key));
        }
        return new SItem(type, variant, stack, compound.hasKey("rarity") ? Rarity.getRarity(compound.getString("rarity")) : statistics.getRarity(), compound.hasKey("origin") ? ItemOrigin.valueOf(compound.getString("origin")) : ItemOrigin.UNKNOWN, compound.getBoolean("recombobulated"), data, true);
    }

    public static SItem convert(final ItemStack stack) {
        return SUtil.setSItemAmount(of(SMaterial.getSpecEquivalent(stack.getType(), stack.getDurability())), stack.getAmount());
    }

    private static SItem of(final NBTTagCompound compound, final ItemStack stack) {
        final SMaterial type = SMaterial.getMaterial(compound.getString("type"));
        final NBTTagCompound data = new NBTTagCompound();
        for (final String key : compound.c()) {
            if (SItem.GLOBAL_NBT_TAGS.contains(key)) {
                continue;
            }
            data.set(key, compound.get(key));
        }
        return new SItem(type, compound.hasKey("variant") ? compound.getShort("variant") : 0, stack, compound.hasKey("rarity") ? Rarity.getRarity(compound.getString("rarity")) : type.getStatistics().getRarity(), compound.hasKey("origin") ? ItemOrigin.valueOf(compound.getString("origin")) : ItemOrigin.UNKNOWN, compound.hasKey("recombobulated"), data, false);
    }

    public static boolean isAbleToDoEtherWarpTeleportation(final Player player, final SItem sitem) {
        boolean haveBlockInRange = false;
        try {
            for (int range = 1; range < 57; ++range) {
                final Location location = player.getTargetBlock((Set) null, range).getLocation();
                if (location.getBlock().getType() != Material.AIR) {
                    haveBlockInRange = true;
                    break;
                }
            }
        } catch (final IllegalStateException ex) {
        }
        return haveBlockInRange;
    }

    public static void etherWarpTeleportation(final Player player, final SItem sitem) {
        if (!Sputnik.tpAbilUsable(player)) {
            return;
        }
        boolean haveBlockInRange = false;
        try {
            for (int range = 1; range < 57; ++range) {
                final Location location = player.getTargetBlock((Set) null, range).getLocation();
                if (location.getBlock().getType() != Material.AIR) {
                    haveBlockInRange = true;
                    break;
                }
            }
        } catch (final IllegalStateException ex) {
        }
        if (!haveBlockInRange) {
            return;
        }
        try {
            int f_ = 57;
            for (int range2 = 1; range2 < 57; ++range2) {
                final Location location2 = player.getTargetBlock((Set) null, range2).getLocation();
                if (location2.getBlock().getType() != Material.AIR) {
                    f_ = range2;
                    break;
                }
            }
            Location location = player.getTargetBlock((Set) null, f_).getLocation().getBlock().getLocation();
            if (location.clone().add(0.0, 1.0, 0.0).getBlock().getType() != Material.AIR && location.clone().add(0.0, 2.0, 0.0).getBlock().getType() != Material.AIR) {
                location = player.getTargetBlock((Set) null, f_ - 1).getLocation().getBlock().getLocation();
            }
            location.setYaw(player.getLocation().getYaw());
            location.setPitch(player.getLocation().getPitch());
            location.add(0.5, 1.0, 0.5);
            if (f_ > 1) {
                Sputnik.teleport(player, location);
            } else {
                Sputnik.teleport(player, player.getLocation());
            }
        } catch (final IllegalStateException ex2) {
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
