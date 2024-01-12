package in.godspunky.skyblock.minion;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.listener.PListener;
import in.godspunky.skyblock.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MinionListener extends PListener {

    @EventHandler
    public void onInteract(PlayerArmorStandManipulateEvent event) {
        if (event.getPlayer() == null || event.getRightClicked() == null) return;
        if (!event.getRightClicked().hasMetadata("minion")) return;
        event.setCancelled(true);
        UUID minionId = UUID.fromString(event.getRightClicked().getMetadata("uuid").get(0).asString());
        User user = User.getUser(event.getPlayer().getUniqueId());
        SkyblockMinion minion = user.minions.stream()
                .filter(minion1 -> minion1.getUuid().equals(minionId)).findFirst().get();
        minion.openInventory(event.getPlayer());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItemInHand();
        if (!itemInHand.hasItemMeta()) return;
        System.out.println(itemInHand.getItemMeta().getDisplayName());
        if (!event.getItemInHand().getItemMeta().getDisplayName().contains("minion")) return;
        System.out.println("found a minion!");
        System.out.println("**** Details ****");
        SItem sItem = SItem.find(itemInHand);
        System.out.println("Display name : " + sItem.getDisplayName());
        System.out.println("Type : " + sItem.getType());
        System.out.println("is Minion : " + sItem.getType().getSMinion() != null);
        System.out.println("**** end ****");
        // todo :  store minion level in item nbt!
        if (sItem.getType().getSMinion() != null){
            new SkyblockMinion(sItem.getType() ,
                    1 , event.getBlock().getLocation().add(0.5, 0, 0.5) ,
                    User.getUser(player.getUniqueId())).spawn();
        }
     }

}
