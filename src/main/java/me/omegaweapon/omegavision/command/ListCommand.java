package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.commands.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class ListCommand extends PlayerCommand {
  
  @Override
  protected void execute(final Player player, final String[] strings) {
    if(Utilities.checkPermission(player, true, "omegavision.list")) {
      Utilities.message(player, MessageHandler.prefix() + " &bThe following players have nightvision enabled:");
      for(Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
        if(onlinePlayers.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
          Utilities.message(player,  MessageHandler.prefix() +  " &c" + onlinePlayers.getName());
        }
      }
    } else {
      Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.noPermission());
    }

  }
}
