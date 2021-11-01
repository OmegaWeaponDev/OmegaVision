package me.omegaweapondev.omegavision.command;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.NightVisionToggle;
import me.ou.library.Utilities;
import me.ou.library.builders.TabCompleteBuilder;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NightVisionCommand extends GlobalCommand implements TabCompleter {
  private final OmegaVision pluginInstance;

  private NightVisionToggle nightVisionToggle;

  public NightVisionCommand(final OmegaVision pluginInstance) {
    this.pluginInstance = pluginInstance;
  }

  @Override
  protected void execute(final CommandSender commandSender, final String[] strings) {
    if(!(commandSender instanceof Player) && strings.length == 1) {
      Utilities.logWarning(true, "Only a player can use that command!");
      return;
    }

    Player player = (Player) commandSender;
    nightVisionToggle = new NightVisionToggle(pluginInstance, player);

    if(strings.length == 0) {
      nightVisionToggle.nightVisionToggle();
     return;
    }

    if(strings.length == 1) {
      Player target = Bukkit.getPlayer(strings[0]);
      nightVisionToggle.nightVisionToggleOthers(target);
    }

    if(strings.length == 2) {
      nightVisionToggle.nightVisionToggleGlobal(strings[1]);
      return;
    }

    if(strings.length == 3) {
      nightVisionToggle.nightVisionToggleTemp(Bukkit.getPlayer(strings[1]), Integer.getInteger(strings[2]));
    }
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if(strings.length == 0) {
      return Collections.emptyList();
    }

    List<String> onlinePlayers = new ArrayList<>();
    for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      onlinePlayers.add(onlinePlayer.getName());
    }

    if(strings.length == 1) {

      return new TabCompleteBuilder(commandSender)
        .checkCommand("global", true, "omegavision.nightvision.toggle.global", "omegavision.nightvision.admin", "omegavision.admin")
        .checkCommand("temp", true, "omegavision.nightvision.toggle.temp", "omegavision.nightvision.admin", "omegavision.admin")
        .checkCommand(onlinePlayers, true, "omegavision.nightvision.toggle.others", "omegavision.nightvision.admin", "omegavision.admin")
        .build(strings[0]);
    }

    if(strings.length == 2) {
      if(strings[0].equalsIgnoreCase("temp")) {
        return new TabCompleteBuilder(commandSender)
          .checkCommand(onlinePlayers, true, "omegavision.nightvision.toggle.temp", "omegavision.nightvision.admin", "omegavision.admin")
          .build(strings[1]);
      }

      if(strings[0].equalsIgnoreCase("global")) {
        return new TabCompleteBuilder(commandSender)
          .checkCommand("add", true, "omegavision.nightvision.toggle.global", "omegavision.nightvision.admin", "omegavision.admin")
          .checkCommand("remove", true, "omegavision.nightvision.toggle.global", "omegavision.nightvision.admin", "omegavision.admin")
          .build(strings[1]);
      }
    }

    return Collections.emptyList();
  }
}

