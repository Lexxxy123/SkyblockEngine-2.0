package vn.giakhanhvn.skysim.gui;

import java.util.HashMap;

import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import vn.giakhanhvn.skysim.user.PlayerUtils;
import vn.giakhanhvn.skysim.user.PlayerStatistics;
import vn.giakhanhvn.skysim.user.User;
import org.bukkit.entity.Player;

import java.util.Map;

public class CollectionBoss extends GUI {
    public static final Map<Player, Boolean> ableToJoin;

    public CollectionBoss() {
        super("Boss Collections", 36);
    }

    @Override
    public void onOpen(final GUIOpenEvent e) {
        this.fill(CollectionBoss.BLACK_STAINED_GLASS_PANE);
        final Player player = e.getPlayer();
        final User user = User.getUser(player.getUniqueId());
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        this.set(GUIClickableItem.getCloseItem(31));
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                GUIType.CATACOMBS_BOSS.getGUI().open((Player) e.getWhoClicked());
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.0f);
            }

            @Override
            public int getSlot() {
                return 30;
            }

            @Override
            public ItemStack getItem() {
                Bukkit.getServer();
                return SUtil.getStack(ChatColor.GREEN + "Back", Material.ARROW, (short) 0, 1);
            }
        });
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
            }

            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public ItemStack getItem() {
                String rw = "&bAble to use &cGolden Sadan Trophy&b!";
                String a = "";
                String b = "";
                if (user.getBCollection() < 100L) {
                    a = SUtil.createLineProgressBar(25, ChatColor.DARK_GREEN, (double) user.getBCollection(), 100.0);
                } else if (user.getBCollection() >= 100L) {
                    a = SUtil.createLineProgressBar(25, ChatColor.DARK_GREEN, (double) user.getBCollection(), 1000.0);
                } else {
                    a = Sputnik.trans("&c&lMAXED!");
                }
                if (user.getBCollection() >= 100L && user.getBCollection() < 1000L) {
                    rw = "&aAble to use &cDiamond Sadan Trophy&a!";
                } else if (user.getBCollection() >= 1000L) {
                    rw = "&6&lMAXED OUT! NICE";
                }
                if (user.getBCollection() > user.getBRun6()) {
                    b = "&7Completion Rate: &cBruh, what the, how??";
                } else if (user.getBRun6() > 0L) {
                    b = "&7Completion Rate: &a" + Math.round(user.getBCollection() / (double) user.getBRun6() * 100.0) + "%";
                } else {
                    b = "&7Completion Rate: &cHaven't played.";
                }
                ItemStack itemstack = null;
                itemstack = SUtil.getSkullURLStack(ChatColor.RED + "Sadan", "fa06cb0c471c1c9bc169af270cd466ea701946776056e472ecdaeb49f0f4a4dc", 1, Sputnik.trans("&7View all your Sadan Collection"), Sputnik.trans("&7Progress and rewards!"), "   ", Sputnik.trans("&7Total Boss Killed: &a" + SUtil.commaify(user.getBCollection())), Sputnik.trans("&7Total Runs: &e" + SUtil.commaify(user.getBRun6())), Sputnik.trans(b), "   ", Sputnik.trans("&7Progress to next level"), Sputnik.trans(a), "   ", Sputnik.trans("&7Rewards"), Sputnik.trans(rw), "   ", Sputnik.trans("&eClick to refresh"));
                return itemstack;
            }
        });
    }

    static {
        ableToJoin = new HashMap<Player, Boolean>();
    }
}
