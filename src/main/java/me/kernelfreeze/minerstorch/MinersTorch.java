package me.kernelfreeze.minerstorch;

import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MinersTorch
  extends JavaPlugin
{
  public void onEnable()
  {
    getServer().getPluginManager().registerEvents(new TorchListener(this), this);
  }
}
