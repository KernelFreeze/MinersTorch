package me.kernelfreeze.minerstorch;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TorchListener implements Listener {

    private static final ItemStack TORCH = new ItemStack(Material.TORCH, 1); // Amount of 1 - for implementation

    private static boolean isUsingPickaxe(Material material) {
        return material.name().endsWith("PICKAXE");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        @Nullable ItemStack item = event.getItem();
        @Nullable Block clickedBlock = event.getClickedBlock();
        @NotNull BlockFace face = event.getBlockFace();

        if (item == null || clickedBlock == null || face == BlockFace.DOWN) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || !isUsingPickaxe(item.getType())) {
            return;
        }

        Block placedBlock = clickedBlock.getRelative(face);
        if (placedBlock.getType() != Material.AIR) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.hasPermission("minerstorch.place")) {
            return;
        }

        // If they have the permission, the torch removal will not be called. If they don't and the torch couldn't be removed, they had no torches
        if (!player.hasPermission("minerstorch.unlimited") && !player.getInventory().removeItem(TORCH).isEmpty()) {
            return;
        }

        ItemStack torchItem = new ItemStack(Material.TORCH);

        // Fire block place event
        BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(placedBlock, placedBlock.getState(), clickedBlock, torchItem, player, true, EquipmentSlot.HAND);
        Bukkit.getPluginManager().callEvent(blockPlaceEvent);

        // Should we cancel?
        if (blockPlaceEvent.isCancelled() || !blockPlaceEvent.canBuild()) {
            return;
        }

        if (face == BlockFace.UP) {
            placedBlock.setType(Material.TORCH);
        } else {
            placedBlock.setBlockData(Bukkit.createBlockData(Material.WALL_TORCH, blockData -> ((Directional) blockData).setFacing(face)));
        }

        player.playSound(placedBlock.getLocation(), Sound.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
    }
}
