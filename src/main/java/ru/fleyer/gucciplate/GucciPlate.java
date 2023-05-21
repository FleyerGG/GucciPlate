package ru.fleyer.gucciplate;

import lombok.Getter;
import lombok.val;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ru.fleyer.gucciplate.commands.PlateCommand;
import ru.fleyer.gucciplate.events.BaseListener;
import ru.fleyer.gucciplate.plate.PlateInfo;
import ru.fleyer.gucciplate.plate.PlateManager;
import ru.fleyer.gucciplate.utils.ConfigManager;
import ru.fleyer.gucciplate.utils.EconomyUtils;
import ru.fleyer.gucciplate.utils.MessageUtils;

import java.util.*;

public final class GucciPlate extends JavaPlugin {
    @Getter
    public static GucciPlate instance;
    @Getter
    private ConfigManager manager;

    Map<String, PlateInfo> map = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        manager = new ConfigManager(this, "config.yml", false);

        EconomyUtils.setupEconomy();

        val plateManager = new PlateManager(this, manager, map);
        plateManager.load();

        getCommand("gucciplate")
                .setExecutor(new PlateCommand(manager, map, plateManager));
        Bukkit.getPluginManager().registerEvents(new BaseListener(
                this,
                manager,
                plateManager,
                map
        ), this);

        for (val plateInfo : map.values()) {
            getServer().getScheduler().runTaskTimerAsynchronously(this,
                    () -> giveMoneyToPlayers(plateInfo),
                    0L,
                    (long) (plateInfo.getTime() * 20L));
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getScheduler().cancelTasks(this);
    }

    public void giveMoneyToPlayers(PlateInfo plate) {
        for (val player : Bukkit.getOnlinePlayers()) {
            val location = player.getLocation();
            val block = location.getBlock();

            if (block.getType() != plate.getPlateMaterial()
                    || !block.hasMetadata("custom_plate_" + plate.getType())) continue;
            if (plate.isOnlineModel()){
                EconomyUtils.getEconomy().depositPlayer(player, plate.getMoney() * Bukkit.getOnlinePlayers().size());
                MessageUtils.message(player, plate);
                return;
            }
            EconomyUtils.getEconomy().depositPlayer(player, plate.getMoney());
            MessageUtils.message(player, plate);
        }
    }
}
