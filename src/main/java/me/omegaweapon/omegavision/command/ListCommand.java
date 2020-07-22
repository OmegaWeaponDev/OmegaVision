package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.commands.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class ListCommand extends PlayerCommand {
  
  @Override
  protected void execute(final Player player, final String[] strings) {

    if(!Utilities.checkPermissions(player, true, "omegavision.*", "omegavision.list")) {
      Utilities.message(player, MessageHandler.playerMessage("No_Permission", "&cSorry, you do not have permission to use that command."));
      return;
    }

    Utilities.message(player, MessageHandler.pluginPrefix() + " " + " &bThe following players have nightvision enabled:");

    for(Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
      if(onlinePlayers.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
        Utilities.message(player,  MessageHandler.pluginPrefix() +  " &c" + onlinePlayers.getName());
      }
    }
  }
}
