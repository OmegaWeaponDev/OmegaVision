package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class ToggleAllCommand extends GlobalCommand {

  @Override
  protected void execute(CommandSender commandSender, String[] strings) {
    playerToggleAll(commandSender, strings);
  }

  private void playerToggleAll(final CommandSender commandSender, final String[] strings) {
    if(!(commandSender instanceof Player)) {
      consoleToggleAll(commandSender, strings);
      return;
    }

    Player player = (Player) commandSender;

    if(!Utilities.checkPermissions(player, true, "omegavision.toggle.all", "omegavision.*", "omegavision.toggle.*")) {
      Utilities.message(player, MessageHandler.playerMessage("No_Permission", "&cSorry, you do not have permission to use that command"));
      return;
    }

    if(strings[0].equalsIgnoreCase("add")) {
      for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        addNightVision(onlinePlayer);
      }
      Utilities.broadcast(MessageHandler.playerMessage("NightVision_Applied_Global", "&9Night Vision has been applied for all players!"));
    }

    if(strings[0].equalsIgnoreCase("remove")) {
      for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        removeNightVision(onlinePlayer);
      }
      Utilities.broadcast(MessageHandler.playerMessage("NightVision_Removed_Global", "&9Night Vision has been removed for all players!"));
    }
  }

  private void consoleToggleAll(final CommandSender commandSender, final String[] strings) {
    if(!(commandSender instanceof ConsoleCommandSender)) {
      return;
    }

    for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      if(strings[0].equalsIgnoreCase("add")) {
        addNightVision(onlinePlayer);
      }

      if(strings[0].equalsIgnoreCase("remove")) {
        removeNightVision(onlinePlayer);
      }
    }
  }

  private void addNightVision(Player player) {
    if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
      return;
    }

    Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100, 1, false, false, false);

    if(OmegaVision.getInstance().getPlayerData().getConfig().isConfigurationSection(player.getUniqueId().toString())) {
      OmegaVision.getInstance().getPlayerData().getConfig().set(player.getUniqueId().toString() + ".NightVision.Enabled", true);
      OmegaVision.getInstance().getPlayerData().saveConfig();
    }
  }

  private void removeNightVision(Player player) {
    Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);

    if(OmegaVision.getInstance().getPlayerData().getConfig().isConfigurationSection(player.getUniqueId().toString())) {
      OmegaVision.getInstance().getPlayerData().getConfig().set(player.getUniqueId().toString() + ".NightVision.Enabled", false);
      OmegaVision.getInstance().getPlayerData().saveConfig();
    }
  }
}
