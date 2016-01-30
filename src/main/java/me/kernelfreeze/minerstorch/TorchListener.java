package me.kernelfreeze.minerstorch;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.Torch;

public class TorchListener
  implements Listener
{
  private final MinersTorch plugin;
  
  public TorchListener(MinersTorch plugin)
  {
    this.plugin = plugin;
  }
  
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event)
  {
    if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
    {
      Player player = event.getPlayer();
      switch (player.getItemInHand().getType())
      {
      case GOLD_PICKAXE: 
      case IRON_PICKAXE: 
      case STONE_PICKAXE: 
      case WOOD_PICKAXE: 
      case DIAMOND_PICKAXE: 
        if (!player.hasPermission("minerstorch.place")) {
          return;
        }
        if (player.hasPermission("minerstorch.unlimited"))
        {
          BlockFace face = event.getBlockFace();
          Block block = event.getClickedBlock().getRelative(face);
          if (block.getType() != Material.AIR) {
            return;
          }
          Torch torch = new Torch();
          torch.setFacingDirection(face);
          player.playSound(block.getLocation(), Sound.DIG_WOOD, 10.0F, 1.0F);
          
          block.setTypeIdAndData(torch.getItemTypeId(), torch.getData(), true);
        }
        else if (player.getInventory().contains(Material.TORCH))
        {
          BlockFace face = event.getBlockFace();
          Block block = event.getClickedBlock().getRelative(face);
          if (block.getType() != Material.AIR) {
            return;
          }
          Torch torch = new Torch();
          torch.setFacingDirection(face);
          
          PlayerInventory inventory = player.getInventory();
          int slot = inventory.first(Material.TORCH);
          ItemStack stack = inventory.getItem(slot);
          if (stack.getAmount() < 1) {
            return;
          }
          if (stack.getAmount() <= 1) {
            inventory.remove(stack);
          } else {
            inventory.setItem(slot, new ItemStack(Material.TORCH, stack.getAmount() - 1));
          }
          player.playSound(block.getLocation(), Sound.DIG_WOOD, 10.0F, 1.0F);
          block.setTypeIdAndData(torch.getItemTypeId(), torch.getData(), true);
        }
        break;
      }
    }
  }
}
