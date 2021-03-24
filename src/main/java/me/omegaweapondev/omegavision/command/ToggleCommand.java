package me.omegaweapondev.omegavision.command;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessageHandler;
import me.omegaweapondev.omegavision.utils.NightVisionConditions;
import me.omegaweapondev.omegavision.utils.NightVisionToggle;
import me.ou.library.Utilities;
import me.ou.library.builders.TabCompleteBuilder;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToggleCommand extends GlobalCommand implements TabCompleter {
  private final OmegaVision plugin;
  private final FileConfiguration configFile;
  private final MessageHandler messageHandler;

  public ToggleCommand(final OmegaVision plugin) {
    this.plugin = plugin;
    configFile = plugin.getSettingsHandler().getConfigFile().getConfig();
    messageHandler = plugin.getMessageHandler();
  }

  @Override
  protected void execute(final CommandSender commandSender, final String[] strings) {

    if(strings.length == 0) {
      if(!(commandSender instanceof Player)) {
        Utilities.logWarning(true, "Only a player can use this command!");
        return;
      }

      final Player player = (Player) commandSender;
      toggleCommand(player);
      return;
    }

    if(strings.length == 2) {
      toggleOthersCommand(commandSender, strings);
    }

  }

  private void toggleCommand(final Player player) {
    final NightVisionToggle nvToggle = new NightVisionToggle(plugin, player);
    final NightVisionConditions nvCondiitions = new NightVisionConditions(plugin, player);

    if(!Utilities.checkPermissions(player, true, "omegavision.toggle", "omegavision.admin", "omegavision.toggle.all")) {
      Utilities.message(player, messageHandler.string("No_Permission", "&cSorry, you do not have permission to use this command."));
      return;
    }

    if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
      nvToggle.nightVisionDisable();
      return;
    }

    if(!configFile.getBoolean("Night_Vision_Limit.Enabled")) {
      nvToggle.nightVisionEnable();
      return;
    }

    if(nvCondiitions.limitChecker()) {
      Utilities.message(player, messageHandler.string("Night_Vision_Limit.Limit_Reached", "&cSorry, you have reached the limit for the nightvision command!"));
      return;
    }

    nvToggle.nightVisionEnable();
  }

  private void toggleOthersCommand(final CommandSender commandSender, final String[] strings) {

    if(!(commandSender instanceof Player)) {
      final Player target = Bukkit.getPlayer(strings[0]);
      final NightVisionToggle nvToggle = new NightVisionToggle(plugin, target);

      if(target == null) {
        Utilities.logWarning(true, messageHandler.string("Invalid_Player", "&cSorry, but that player does not exist or is offline."));
        return;
      }

      if(strings[1].equalsIgnoreCase("on")) {
        nvToggle.nightVisionEnableOthers(target);
        Utilities.logInfo(true, messageHandler.string("NightVision_Applied", "&9Night Vision has been applied!"));
        return;
      }

      if(strings[1].equalsIgnoreCase("off")) {
        nvToggle.nightVisionDisableOthers(target);
        Utilities.logInfo(true, messageHandler.string("NightVision_Removed", "&9Night Vision has been removed!"));
      }

      return;
    }

    final Player player = (Player) commandSender;
    final NightVisionToggle nvToggle = new NightVisionToggle(plugin, player);

    if(!Utilities.checkPermissions(player, true, "omegavision.toggle.others", "omegavision.toggle.all", "omegavision.admin")) {
      Utilities.message(player, messageHandler.string("No_Permission", "&cSorry, you do not have permission to use this command."));
      return;
    }

    Player target = Bukkit.getPlayer(strings[0]);

    if(target == null) {
      Utilities.message(player, messageHandler.string("Invalid_Player", "&cSorry, but that player does not exist or is offline."));
      return;
    }

    if(strings[1].equalsIgnoreCase("on")) {
      nvToggle.nightVisionEnableOthers(target);
      return;
    }

    if (strings[1].equalsIgnoreCase("off")) {
      nvToggle.nightVisionDisableOthers(target);
    }
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if(strings.length <= 1) {
      List<String> players = new ArrayList<>();
      for(Player player : Bukkit.getOnlinePlayers()) {
        players.add(player.getName());
      }

      return new TabCompleteBuilder(commandSender)
        .checkCommand("on", true, "omegavision.toggle", "omegavision.toggle.all", "omegavision.admin")
        .checkCommand("off", true, "omegavision.toggle", "omegavision.toggle.all", "omegavision.admin")
        .addCommand(players).build(strings[0]);
    }

    for(Player player : Bukkit.getOnlinePlayers()) {
      if(strings.length == 2 && strings[0].equalsIgnoreCase(player.getName())) {
        return new TabCompleteBuilder(commandSender)
          .checkCommand("on", true, "omegavision.toggle.others", "omegavision.toggle.all", "omegavision.admin")
          .checkCommand("off", true, "omegavision.toggle.others", "omegavision.toggle.all", "omegavision.admin")
          .build(strings[1]);
      }
    }

    return Collections.emptyList();
  }
}

