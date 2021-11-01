package me.omegaweapondev.omegavision.command;

import me.omegaweapondev.omegavision.OmegaVision;
import me.omegaweapondev.omegavision.utils.MessagesHandler;
import me.ou.library.commands.GlobalCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class NightVisionCommand extends GlobalCommand implements TabCompleter {
  private final OmegaVision pluginInstance;
  private final FileConfiguration configFile;
  private final MessagesHandler messagesHandler;

  public NightVisionCommand(final OmegaVision pluginInstance) {
    this.pluginInstance = pluginInstance;
    configFile = pluginInstance.getStorageManager().getConfigFile().getConfig();
    messagesHandler = pluginInstance.getMessagesHandler();
  }

  @Override
  protected void execute(final CommandSender commandSender, final String[] strings) {

  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return Collections.emptyList();
  }
}

