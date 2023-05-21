package ru.fleyer.gucciplate.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import ru.fleyer.gucciplate.GucciPlate;
import ru.fleyer.gucciplate.plate.PlateInfo;

/**
 * @author Fleyer
 * <p> MessageUtils creation on 09.05.2023 at 12:19
 */
@UtilityClass
public class MessageUtils {
    GucciPlate gucciPlate = GucciPlate.getInstance();
    ConfigManager manager = gucciPlate.getManager();

    public void message(Player player, PlateInfo plateInfo) {
        val text = ChatColor.translateAlternateColorCodes(
                '&', plateInfo.getMessage().replace("%moneyOnline%",
                        String.valueOf(plateInfo.getMoney() * Bukkit.getOnlinePlayers().size())));
        switch (plateInfo.getMessage_type()) {
            case "actionbar": {
                player.sendActionBar(Component.text(text));
                return;
            }
            case "chat": {
                player.sendMessage(text);
                return;
            }
            case "title": {
                player.sendTitle(text, "", 10, 20, 10);
                return;
            }
            case "bossbar": {
                BossBar bossBar = Bukkit.createBossBar(text, BarColor.WHITE, BarStyle.SOLID);
                bossBar.addPlayer(player);
                Bukkit.getScheduler().runTaskLater(gucciPlate, () -> bossBar.removePlayer(player), 20L);
            }
        }
    }

    @SneakyThrows
    public String formatterPath(String path) {
        return ChatColor.translateAlternateColorCodes('&',
                manager.getString(path).get());
    }

    public boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @SneakyThrows
    public void helper(Player player) {
        manager.getStringList("plate_help").get().forEach(s ->
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', s)));
    }
}
