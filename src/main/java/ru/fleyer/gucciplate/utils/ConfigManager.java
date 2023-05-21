package ru.fleyer.gucciplate.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author Fleyer
 * <p> ConfigManager creation on 01.05.2023 at 12:27
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConfigManager {

    FileConfiguration config;
    File configFile;

    public ConfigManager(JavaPlugin plugin, String name, boolean replace) {
        this.configFile = new File(plugin.getDataFolder(), name);
        this.config = YamlConfiguration.loadConfiguration(configFile);
        if (!configFile.exists()) {
            plugin.saveResource(name, replace);
        }
    }

    public void saveConfig() {
        val future = new CompletableFuture<Void>();
        try {
            config.save(configFile);
            future.complete(null);
        } catch (IOException e) {
            future.completeExceptionally(e);
        }
    }

    public void reloadConfig() {
        val future = new CompletableFuture<Void>();
        try {
            config.load(configFile);
            future.complete(null);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            future.completeExceptionally(e);
        }
    }

    public CompletableFuture<Boolean> contains(String path) {
        val future = new CompletableFuture<Boolean>();
        val result = config.contains(path);
        future.complete(result);
        return future;
    }

    public CompletableFuture<String> getString(String path) {
        val future = new CompletableFuture<String>();
        val result = config.getString(path);
        future.complete(result);
        return future;
    }

    public CompletableFuture<Integer> getInt(String path) {
        val future = new CompletableFuture<Integer>();
        val result = config.getInt(path);
        future.complete(result);
        return future;
    }

    public CompletableFuture<ConfigurationSection> getConfigurationSection(String path) {
        val future = new CompletableFuture<ConfigurationSection>();
        val result = config.getConfigurationSection(path);
        future.complete(result);
        return future;
    }

    public CompletableFuture<Set<String>> getAllKeys() {
        val future = new CompletableFuture<Set<String>>();
        val keys = config.getKeys(false);
        future.complete(keys);
        return future;
    }

    public CompletableFuture<List<String>> getStringList(String path) {
        val future = new CompletableFuture<List<String>>();
        try {
            val result = config.getStringList(path);
            future.complete(result);
        } catch (Throwable e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public CompletableFuture<Double> getDouble(String path) {
        CompletableFuture<Double> future = new CompletableFuture<>();
        double result = config.getDouble(path);
        future.complete(result);
        return future;
    }

    public CompletableFuture<Object> get(String path) {
        CompletableFuture<Object> future = new CompletableFuture<>();
        Object result = config.get(path);
        future.complete(result);
        return future;
    }

    public CompletableFuture<Boolean> getBoolean(String path) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        boolean result = config.getBoolean(path);
        future.complete(result);
        return future;
    }

}
