package me.omegaweapon.omegavision.command;

import me.omegaweapon.omegavision.OmegaVision;
import me.omegaweapon.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.commands.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class ListCommand extends PlayerCommand {
  private final MessageHandler messagesHandler = new MessageHandler(OmegaVision.getInstance().getMessagesFile().getConfig());
  
  @Override
  protected void execute(final Player player, final String[] strings) {

    if(!Utilities.checkPermissions(player, true, "omegavision.list", "omegavision.admin")) {
      Utilities.message(player, messagesHandler.string("No_Permission", "&cSorry, you do not have permission to use that command."));
      return;
    }

    Utilities.message(player, messagesHandler.getPrefix() + "&bThe following players have nightvision enabled:");

    for(Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
      if(onlinePlayers.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
        Utilities.message(player,  messagesHandler.getPrefix() +  "&c" + onlinePlayers.getName());
      }
    }
  }
}
