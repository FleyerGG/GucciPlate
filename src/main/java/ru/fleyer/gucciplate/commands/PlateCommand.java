package ru.fleyer.gucciplate.commands;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.fleyer.gucciplate.plate.PlateInfo;
import ru.fleyer.gucciplate.plate.PlateManager;
import ru.fleyer.gucciplate.utils.ConfigManager;
import ru.fleyer.gucciplate.utils.MessageUtils;

import java.util.Map;

/**
 * @author Fleyer
 * <p> PlateCommand creation on 01.05.2023 at 14:49
 */
@AllArgsConstructor
public class PlateCommand implements CommandExecutor {
    ConfigManager manager;
    Map<String, PlateInfo> map;
    PlateManager plateManager;

    @SneakyThrows
    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command cmd,
            @NotNull String arg,
            @NotNull String[] args
    ) {
        val player = (Player) sender;
        if (!player.hasPermission(manager.getString("plate_permission_command_use").get())) {
            MessageUtils.formatterPath("plate_no_permissions");
            return true;
        }
        if (args.length < 4) {
            MessageUtils.helper(player);
            return false;
        }
        val target_name = args[1];
        val target = Bukkit.getPlayer(target_name);

        if (target == null || !target.isOnline()) {
            player.sendMessage(MessageUtils.formatterPath("message_not_online").replace("%target%", target_name));
            return true;
        }
        val plate_type = args[2];
        val amount_plate = args[3];

        if (map.get(plate_type) == null){
            player.sendMessage(MessageUtils.formatterPath("message_item_not").replace("%item%",plate_type));
            return true;
        }
        if (!MessageUtils.isNumeric(amount_plate)){
            player.sendMessage(MessageUtils.formatterPath("message_amount_not_itnteger"));
            return true;
        }
        target.getInventory().addItem(plateManager.item(plate_type, Integer.parseInt(amount_plate)));
        player.sendMessage(MessageUtils.formatterPath("plate_give")
                .replace("%player_name%", target_name)
                .replace("%plate_name%",
                        ChatColor.translateAlternateColorCodes(
                                '&', map.get(plate_type).getDisplayName())));

        return true;
    }
}
