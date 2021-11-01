package me.omegaweapondev.omegavision.command;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessagesHandler;
import me.ou.library.Utilities;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListCommand extends GlobalCommand implements TabCompleter {
  private final OmegaVision pluginInstance;
  private final MessagesHandler messagesHandler;

  public ListCommand(final OmegaVision pluginInstance) {
    this.pluginInstance = pluginInstance;
    messagesHandler = pluginInstance.getMessagesHandler();
  }
  
  @Override
  protected void execute(final CommandSender commandSender, final String[] strings) {
    if(commandSender instanceof Player) {
      Player player = (Player) commandSender;

      if(!Utilities.checkPermissions(player, true, "omegavision.nightvision.list", "omegavision.nightvision.admin", "omegavision.admin")) {
        Utilities.message(player, messagesHandler.string("No_Permission", "#f63e3eSorry, but you don't have permission to do that."));
        return;
      }

      Utilities.message(player, messagesHandler.getPrefix() + "#00D4FFThe following players have night vision enabled:", getPlayerList());
      return;
    }

    Utilities.logInfo(true, "The following players have night vision enabled:", getPlayerList());
  }

  private String getPlayerList() {
    List<String> nightVisionList = new ArrayList<>();
    for(Player playerName : Bukkit.getOnlinePlayers()) {
      if(playerName.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
        nightVisionList.add(playerName.getName());
      }
    }

    StringBuilder playerList = new StringBuilder();
    for(String playerName : nightVisionList) {
      playerList.append(playerName).append(", ");
    }

    return playerList.toString();
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return Collections.emptyList();
  }
}
