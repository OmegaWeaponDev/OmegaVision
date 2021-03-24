package me.omegaweapondev.omegavision.command;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.builders.TabCompleteBuilder;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ToggleTempCommand extends GlobalCommand implements TabCompleter {
  private final OmegaVision plugin;
  private final MessageHandler messageHandler;
  private final FileConfiguration playerData;

  public ToggleTempCommand(final OmegaVision plugin) {
    this.plugin = plugin;
    messageHandler = plugin.getMessageHandler();
    playerData = plugin.getUserData().getPlayerData();
  }

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
        messageHandler.getPrefix() + "#00D4FFOmegaVision #FF4A4Av" + plugin.getDescription().getVersion() + " #00D4FFBy OmegaWeaponDev",
        messageHandler.getPrefix() + "#00D4FFTemp Toggle: #FF4A4A/nvtemp <player> <time>"
      );
      return;
    }

    if(!Utilities.checkPermissions(player, true, "omegavision.toggle.temp", "omegavision.admin", "omegavision.toggle.all")) {
      Utilities.message(player, messageHandler.string("No_Permission", "#FF4A4ASorry, you do not have permission to use that command."));
      return;
    }

    int tempTime = Integer.parseInt(strings[1]);
    Player target = Bukkit.getPlayer(strings[0]);

    if(target == null) {
      Utilities.message(player, messageHandler.string("Invalid_Player", "#FF4A4ASorry, that player is either offline or does not exist."));
      return;
    }

    applyNightVision(tempTime, target);
    Utilities.message(player, messageHandler.string("NightVision_Applied_Temp", "#027ea3 Night vision has now been temporarily applied for %player%").replace("%player%", target.getName()));
  }

  private void consoleToggleTemp(final CommandSender commandSender, final String[] strings) {
    if(!(commandSender instanceof ConsoleCommandSender)) {
      return;
    }

    if(strings.length != 2) {
      Utilities.logInfo(true,
        messageHandler.getPrefix() + "#00D4FFOmegaVision #FF4A4Av" + plugin.getDescription().getVersion() + " #00D4FFBy OmegaWeaponDev",
        messageHandler.getPrefix() + "#00D4FFTemp Toggle: #FF4A4A/nvtemp <player> <time>"
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
    Utilities.logInfo(true, messageHandler.console("NightVision_Applied_Temp", "#027ea3 Night vision has now been temporarily applied for %player%").replace("%player%", target.getName()));
  }

  private void applyNightVision(final Integer duration, final Player target) {
    if(target.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
      Utilities.removePotionEffect(target, PotionEffectType.NIGHT_VISION);
      if(playerData.isConfigurationSection(target.getUniqueId().toString())) {
        playerData.set(target.getUniqueId().toString() + ".NightVision.Enabled", false);
       plugin.getUserData().saveUserFile();
      }
    }

    Utilities.addPotionEffect(target, PotionEffectType.NIGHT_VISION, duration, 1, false, false, false);
    Utilities.logInfo(true, messageHandler.string("NightVision_Applied", "#027ea3Night Vision has been applied!"));
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if(strings.length <= 1) {
      List<String> players = new ArrayList<>();
      for(Player player : Bukkit.getOnlinePlayers()) {
        players.add(player.getName());
      }

      return new TabCompleteBuilder(commandSender)
        .addCommand(players).build(strings[0]);
    }

    return Collections.emptyList();
  }
}
