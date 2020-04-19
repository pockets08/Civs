package org.redcastlemedia.multitallented.civs.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.redcastlemedia.multitallented.civs.Civs;
import org.redcastlemedia.multitallented.civs.localization.LocaleManager;
import org.redcastlemedia.multitallented.civs.nations.Nation;
import org.redcastlemedia.multitallented.civs.nations.NationManager;
import org.redcastlemedia.multitallented.civs.towns.Town;
import org.redcastlemedia.multitallented.civs.towns.TownManager;
import org.redcastlemedia.multitallented.civs.util.Constants;
import org.redcastlemedia.multitallented.civs.util.OwnershipUtil;

@CivsCommand(keys = "nation-lore") @SuppressWarnings("unused")
public class NationLoreCommand implements CivCommand {
    @Override
    public boolean runCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Civs.getPrefix() + "only players in game can use this command");
            return true;
        }
        Player player = (Player) commandSender;

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType() != Material.WRITTEN_BOOK) {
            player.sendMessage(Civs.getPrefix() + LocaleManager.getInstance().getTranslationWithPlaceholders(player,
                    "hold-lore"));
            return true;
        }

        Nation nation = NationManager.getInstance().getNation(args[0]);
        if (nation == null) {
            player.sendMessage(Civs.getPrefix() + LocaleManager.getInstance().getTranslationWithPlaceholders(player,
                    "invalid-name"));
            return true;
        }

        if (OwnershipUtil.isAuthorized(player, nation)) {
            return true;
        }

        nation.setLore(itemStack);
        nation.setLastRenamedBy(player.getUniqueId());
        player.sendMessage(Civs.getPrefix() + LocaleManager.getInstance().getTranslationWithPlaceholders(player,
                "lore-set").replace("$1", nation.getName()));
        return true;
    }
}
