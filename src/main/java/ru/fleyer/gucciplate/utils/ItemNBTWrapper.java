package ru.fleyer.gucciplate.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Fleyer
 * <p> ItemNBTWrapper creation on 01.05.2023 at 13:24
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemNBTWrapper {
    final ItemStack item;
    String version;
    Object nbtTagCompound;
    Object itemNMS;
    Class<?> craftItemStack;

    public ItemNBTWrapper(ItemStack item) {
        this.item = item;
        try {
            this.version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            this.craftItemStack = this.getCraftItemStack();
            this.itemNMS = this.craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            val hasTag = (Boolean) this.itemNMS.getClass().getMethod("hasTag")
                    .invoke(this.itemNMS, new Object[0]);
            val tag = this.itemNMS.getClass().getMethod("getTag").invoke(this.itemNMS);
            this.nbtTagCompound = hasTag ? tag : this.getNBTTagCompound().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNBT(String key, String value) {
        try {
            this.nbtTagCompound.getClass().getMethod("setString", String.class, String.class)
                    .invoke(this.nbtTagCompound, key, value);
            this.itemNMS.getClass().getMethod("setTag", this.getNBTTagCompound())
                    .invoke(this.itemNMS, this.nbtTagCompound);
            val itemStack = this.craftItemStack.getMethod("asBukkitCopy",
                            Class.forName("net.minecraft.server." + this.version + ".ItemStack"))
                    .invoke(this.craftItemStack, this.itemNMS);
            val meta = (ItemMeta) itemStack.getClass().getMethod("getItemMeta"
            ).invoke(itemStack, new Object[0]);
            this.item.setItemMeta(meta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasKey(String key) {
        try {
            val value = (String) this.nbtTagCompound.getClass()
                    .getMethod("getString", String.class).invoke(this.nbtTagCompound, key);
            return value != null && !value.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Class<?> getCraftItemStack() {
        try {
            return Class.forName("org.bukkit.craftbukkit." + this.version + ".inventory.CraftItemStack");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Class<?> getNBTTagCompound() {
        try {
            return Class.forName("net.minecraft.server." + this.version + ".NBTTagCompound");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

