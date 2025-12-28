package pl.incognito;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class IncognitoPlugin extends JavaPlugin implements Listener {

    private final Set<UUID> hidden = new HashSet<>();

    @Override
    public void onEnable() {
        getCommand("incognito").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;


        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Incognito");

        boolean state = hidden.contains(p.getUniqueId());

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(state
                ? ChatColor.GREEN + "Skin: UKRYTY"
                : ChatColor.RED + "Skin: WIDOCZNY");
        item.setItemMeta(meta);

        inv.setItem(4, item);
        p.openInventory(inv);
        return true;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        if (!e.getView().getTitle().contains("Incognito")) return;

        e.setCancelled(true);

        if (e.getSlot() == 4) {
            UUID id = p.getUniqueId();
            if (hidden.contains(id)) {
                hidden.remove(id);
                p.sendMessage("Skin widoczny");
            } else {
                hidden.add(id);
                p.sendMessage("Skin ukryty");
            }
            p.closeInventory();
        }
    }
}
