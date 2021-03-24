package me.omegaweapondev.omegavision.command;


import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ListCommand extends GlobalCommand implements TabCompleter {
  private final OmegaVision plugin;
  private final MessageHandler messagesHandler;

  public ListCommand(final OmegaVision plugin) {
    this.plugin = plugin;
    messagesHandler = plugin.getMessageHandler();
  }
  
  @Override
  protected void execute(final CommandSender commandSender, final String[] strings) {

    if(!(commandSender instanceof Player)) {
      Utilities.logInfo(true, messagesHandler.getPrefix() + "#00D4FFThe following players have nightvision enabled:");

      for(Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
        if(onlinePlayers.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
          Utilities.logInfo(true,  messagesHandler.getPrefix() +  "#FF4A4A" + onlinePlayers.getName());
        }
      }
      return;
    }

    final Player player = (Player) commandSender;

    if(!Utilities.checkPermissions(player, true, "omegavision.list", "omegavision.admin")) {
      Utilities.message(player, messagesHandler.string("No_Permission", "#FF4A4ASorry, you do not have permission to use that command."));
      return;
    }

    Utilities.message(player, messagesHandler.getPrefix() + "#00D4FFThe following players have nightvision enabled:");

    for(Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
      if(onlinePlayers.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
        Utilities.message(player,  messagesHandler.getPrefix() +  "#FF4A4A" + onlinePlayers.getName());
      }
    }
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return Collections.emptyList();
  }
}
