package in.godspunky.skyblock.gui;

import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ConfirmWitherRuins extends GUI {
    public ConfirmWitherRuins() {
        super("Travel to the Withering Ruins?", 27);
        this.fill(BLACK_STAINED_GLASS_PANE);
    }

    @Override
    public void onOpen(final GUIOpenEvent e) {
        final Player player = e.getPlayer();
        final User user = User.getUser(player.getUniqueId());
        this.set(new GUIClickableItem() {
            @Override
            public void run(final InventoryClickEvent e) {
                final Player p = (Player) e.getWhoClicked();
                if (SkyBlock.getEconomy().getBalance(p) >= 2000.0) {
                    SkyBlock.getEconomy().withdrawPlayer(player, 2000.0);
                    p.sendMessage(Sputnik.trans("&eYou have travelled to the &cWithering Ruins&e!"));
                    p.teleport(new Location(Bukkit.getWorld("arena"), 234744.5, 158.0, 236558.5, 135.0f, 0.0f));
                } else {
                    p.sendMessage(Sputnik.trans("&cYou cannot afford for this ride!"));
                }
            }

            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getStack(Sputnik.trans("&aTravel to: &cWithering Ruins"), Material.MINECART, (short) 0, 1, Sputnik.trans("&7Following &dArlly &7rails and travel into"), Sputnik.trans("&7a mysterious place under the &eGiants Island"), Sputnik.trans("&cBe Careful! &4Something, is there..."), Sputnik.trans("&7"), Sputnik.trans("&7Cost for a Minecart Ride"), Sputnik.trans("&b2,000 Bits"), Sputnik.trans("&7"), Sputnik.trans((SkyBlock.getEconomy().getBalance(player) >= 2000.0) ? "&eClick to travel" : "&cYou cannot afford this!"));
            }
        });
    }
}
