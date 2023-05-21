package ru.fleyer.gucciplate.events;

import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import ru.fleyer.gucciplate.GucciPlate;
import ru.fleyer.gucciplate.plate.PlateInfo;
import ru.fleyer.gucciplate.plate.PlateManager;
import ru.fleyer.gucciplate.utils.ConfigManager;
import ru.fleyer.gucciplate.utils.ItemNBTWrapper;
import ru.fleyer.gucciplate.utils.MessageUtils;

import java.util.Map;

/**
 * @author Fleyer
 * <p> BaseListener creation on 01.05.2023 at 13:13
 */
@AllArgsConstructor
public class BaseListener implements Listener {
    GucciPlate gucciPlate;
    ConfigManager configManager;
    PlateManager plateManager;
    Map<String, PlateInfo> map;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        val player = event.getPlayer();
        val from = event.getFrom();
        val to = event.getTo();
        for (val plate : map.values()){
            // Проверяем, находится ли игрок на плитке
            if (!from.getBlock().hasMetadata(
                    "custom_plate_" + plate.getType()) && to.getBlock().hasMetadata(
                    "custom_plate_" + plate.getType())){
                if (plate.isOnlineModel()){
                    player.sendMessage(MessageUtils.formatterPath("message_welcome")
                            //.replace("%plate_money%", String.valueOf((int) plate.getMoney()))
                            .replace("%plate_money%",
                                    String.valueOf((int) plate.getMoney() * Bukkit.getOnlinePlayers().size()))
                            .replace("%plate_time%", String.valueOf(plate.getTime())));
                    return;
                }
                player.sendMessage(MessageUtils.formatterPath("message_welcome")
                        .replace("%plate_money%", String.valueOf((int) plate.getMoney()))
                        //.replace("%plate_online_money%",
                        //        String.valueOf((int) plate.getMoney() * Bukkit.getOnlinePlayers().size()))
                        .replace("%plate_time%", String.valueOf(plate.getTime())));
            }
            // Проверяем, покинул ли игрок плитку
            if (from.getBlock().hasMetadata(
                    "custom_plate_" + plate.getType()) && !to.getBlock().hasMetadata(
                    "custom_plate_" + plate.getType())) {
                player.sendMessage(MessageUtils.formatterPath("message_goodbye"));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        val player = event.getPlayer();
        val block = event.getBlock();
        for (val plate : map.values()) {
            if (block.getType() != plate.getPlateMaterial()
                    || !block.hasMetadata("custom_plate_" + plate.getType())) continue;

            block.removeMetadata("custom_plate_" + plate.getType(), gucciPlate);
            player.sendMessage(MessageUtils.formatterPath("message_break_plate"));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        val player = event.getPlayer();
        val block = event.getBlock();
        val nbt = new ItemNBTWrapper(event.getItemInHand());

        for (val plate : map.values()) {
            if (block.getType() != plate.getPlateMaterial()
                    || !nbt.hasKey("cutom_item_plate_" + plate.getType())) continue;

            block.setMetadata("custom_plate_" + plate.getType(),
                    new FixedMetadataValue(gucciPlate, true));
            player.sendMessage(MessageUtils.formatterPath("message_place_plate"));
        }
    }
}
