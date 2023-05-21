package ru.fleyer.gucciplate.plate;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.fleyer.gucciplate.GucciPlate;
import ru.fleyer.gucciplate.utils.ConfigManager;
import ru.fleyer.gucciplate.utils.ItemNBTWrapper;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Fleyer
 * <p> PlateManager creation on 01.05.2023 at 12:27
 */
@AllArgsConstructor
public class PlateManager {
    GucciPlate gucciPlate;
    ConfigManager manager;
    Map<String, PlateInfo> map;

    @SneakyThrows
    public void load() {
        for (val s : manager.getConfigurationSection("type").get().getKeys(false)) {
            val plate = new PlateInfo();

            plate.setType(s);
            plate.setOnlineModel(manager.getBoolean("type." + s + ".onlineModel").get());
            plate.setPlateMaterial(Material.getMaterial(manager.getString("type." + s + ".material").get()));
            plate.setDisplayName(manager.getString("type." + s + ".name").get());
            plate.setLore(manager.getStringList("type." + s + ".lore").get());
            plate.setMoney(manager.getDouble("type." + s + ".money").get());
            plate.setTime(manager.getDouble("type." + s + ".time").get());
            plate.setMessage_type(manager.getString("type." + s + ".message_type").get());
            plate.setMessage(manager.getString("type." + s + ".message").get());

            map.put(s, plate);
        }
    }

    public ItemStack item(String type, int amount) {
        val plateInfo = map.get(type);
        val item = new ItemStack(plateInfo.getPlateMaterial());
        val meta = item.getItemMeta();
        val list = new ArrayList<String>();

        assert meta != null;

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plateInfo.getDisplayName()));
        plateInfo.getLore().forEach(l -> list.add(ChatColor.translateAlternateColorCodes('&', l)));
        meta.setLore(list);
        item.setItemMeta(meta);
        item.setAmount(amount);

        val nbt = new ItemNBTWrapper(item);
        nbt.setNBT("cutom_item_plate_" + plateInfo.getType(), "Fleyer");
        return item;
    }


}
