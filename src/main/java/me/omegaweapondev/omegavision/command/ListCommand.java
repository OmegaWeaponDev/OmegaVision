package me.omegaweapondev.omegavision.command;


import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class ListCommand extends GlobalCommand {
  private final MessageHandler messagesHandler = new MessageHandler(OmegaVision.getInstance().getMessagesFile().getConfig());
  
  @Override
  protected void execute(final CommandSender commandSender, final String[] strings) {

    if(!(commandSender instanceof Player)) {
      Utilities.logInfo(true, messagesHandler.getPrefix() + "&bThe following players have nightvision enabled:");

      for(Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
        if(onlinePlayers.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
          Utilities.logInfo(true,  messagesHandler.getPrefix() +  "&c" + onlinePlayers.getName());
        }
      }
      return;
    }

    final Player player = (Player) commandSender;

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
