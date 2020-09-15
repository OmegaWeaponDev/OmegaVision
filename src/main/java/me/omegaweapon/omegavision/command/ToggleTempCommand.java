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


public class ToggleTempCommand extends GlobalCommand {
  private final MessageHandler messageHandler = new MessageHandler(OmegaVision.getInstance().getMessagesFile().getConfig());

  @Override
  public void execute(final CommandSender commandSender, final String[] strings) {
    playerToggleTemp(commandSender, strings);
  }

  private void playerToggleTemp(final CommandSender commandSender, final String[] strings) {
    if(!(commandSender instanceof Player)) {
      consoleToggleTemp(commandSender, strings);
      return;
    }

    Player player = (Player) commandSender;

    if(strings.length != 2) {
      Utilities.message(player,
        messageHandler.getPrefix() + "&bOmegaVision &cv" + OmegaVision.getInstance().getDescription().getVersion() + " &bBy OmegaWeaponDev",
        messageHandler.getPrefix() + "&bTemp Toggle: &c/nvtemp <player> <time>"
      );
      return;
    }

    if(!Utilities.checkPermissions(player, true, "omegavision.toggle.temp", "omegavision.admin", "omegavision.toggle.all")) {
      Utilities.message(player, messageHandler.string("No_Permission", "&cSorry, you do not have permission to use that command."));
      return;
    }

    int tempTime = Integer.parseInt(strings[1]);
    Player target = Bukkit.getPlayer(strings[0]);

    if(target == null) {
      Utilities.message(player, messageHandler.string("Invalid_Player", "&cSorry, that player is either offline or does not exist."));
      return;
    }

    applyNightVision(tempTime, target);
    Utilities.message(player, messageHandler.string("NightVision_Applied_Temp", "&3 Night vision has now been temporarily applied for %player%").replace("%player%", target.getName()));
  }

  private void consoleToggleTemp(final CommandSender commandSender, final String[] strings) {
    if(!(commandSender instanceof ConsoleCommandSender)) {
      return;
    }

    if(strings.length != 2) {
      Utilities.logInfo(true,
        messageHandler.getPrefix() + "&bOmegaVision &cv" + OmegaVision.getInstance().getDescription().getVersion() + " &bBy OmegaWeaponDev",
        messageHandler.getPrefix() + "&bTemp Toggle: &c/nvtemp <player> <time>"
      );
      return;
    }
    int tempTime = Integer.parseInt(strings[1]);
    Player target = Bukkit.getPlayer(strings[0]);

    if(target == null) {
      Utilities.logInfo(true, "Sorry, that player is either offline or does not exist.");
      return;
    }

    applyNightVision(tempTime, target);
    Utilities.logInfo(true, messageHandler.console("NightVision_Applied_Temp", "&3 Night vision has now been temporarily applied for %player%").replace("%player%", target.getName()));
  }

  private void applyNightVision(final Integer duration, final Player target) {
    if(target.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
      Utilities.removePotionEffect(target, PotionEffectType.NIGHT_VISION);
      if(OmegaVision.getInstance().getPlayerData().getConfig().isConfigurationSection(target.getUniqueId().toString())) {
        OmegaVision.getInstance().getPlayerData().getConfig().set(target.getUniqueId().toString() + ".NightVision.Enabled", false);
        OmegaVision.getInstance().getPlayerData().saveConfig();
      }
    }

    Utilities.addPotionEffect(target, PotionEffectType.NIGHT_VISION, duration, 1, false, false, false);
  }
}
