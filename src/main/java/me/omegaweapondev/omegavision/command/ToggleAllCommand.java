package me.omegaweapondev.omegavision.command;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class ToggleAllCommand extends GlobalCommand {
  private final MessageHandler messagesHandler = new MessageHandler(OmegaVision.getInstance().getMessagesFile().getConfig());
  private final FileConfiguration playerData = OmegaVision.getInstance().getPlayerData().getConfig();

  @Override
  protected void execute(CommandSender commandSender, String[] strings) {

    if(strings.length == 0) {
      Utilities.logWarning(true,
        "You need to include add/remove to the command!"
      );

      if(commandSender instanceof Player) {
        Player player = (Player) commandSender;
        Utilities.message(player, "&cYou need to include add/remove to the command!");
      }
      return;
    }

    playerToggleAll(commandSender, strings);
  }

  private void playerToggleAll(final CommandSender commandSender, final String[] strings) {
    if(!(commandSender instanceof Player)) {
      consoleToggleAll(commandSender, strings);
      return;
    }

    Player player = (Player) commandSender;

    if(!Utilities.checkPermissions(player, true, "omegavision.toggle.global", "omegavision.admin", "omegavision.toggle.all")) {
      Utilities.message(player, messagesHandler.string("No_Permission", "&cSorry, you do not have permission to use that command"));
      return;
    }

    if(strings[0].equalsIgnoreCase("add")) {
      for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        addNightVision(onlinePlayer);
      }
      Utilities.broadcast(messagesHandler.string("NightVision_Applied_Global", "&9Night Vision has been applied for all players!"));
    }

    if(strings[0].equalsIgnoreCase("remove")) {
      for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        removeNightVision(onlinePlayer);
      }
      Utilities.broadcast(messagesHandler.string("NightVision_Removed_Global", "&9Night Vision has been removed for all players!"));
    }
  }

  private void consoleToggleAll(final CommandSender commandSender, final String[] strings) {
    if(!(commandSender instanceof ConsoleCommandSender)) {
      return;
    }

    for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      if(strings[0].equalsIgnoreCase("add")) {
        addNightVision(onlinePlayer);
        Utilities.logInfo(true, messagesHandler.string("NightVision_Applied", "&9Night Vision has been applied!"));
      }

      if(strings[0].equalsIgnoreCase("remove")) {
        removeNightVision(onlinePlayer);
        Utilities.logInfo(true, messagesHandler.string("NightVision_Removed", "&9Night Vision has been removed!"));
      }
    }
  }

  private void addNightVision(Player player) {
    if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
      return;
    }

    Utilities.addPotionEffect(player, PotionEffectType.NIGHT_VISION, 60 * 60 * 24 * 100, 1, false, false, false);

    if(playerData.isConfigurationSection(player.getUniqueId().toString())) {
      playerData.set(player.getUniqueId().toString() + ".NightVision.Enabled", true);
      OmegaVision.getInstance().getPlayerData().saveConfig();
    }
  }

  private void removeNightVision(Player player) {
    Utilities.removePotionEffect(player, PotionEffectType.NIGHT_VISION);

    if(playerData.isConfigurationSection(player.getUniqueId().toString())) {
      playerData.set(player.getUniqueId().toString() + ".NightVision.Enabled", false);
      OmegaVision.getInstance().getPlayerData().saveConfig();
    }
  }
}
