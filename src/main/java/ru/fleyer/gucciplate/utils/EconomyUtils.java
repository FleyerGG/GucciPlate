package ru.fleyer.gucciplate.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.milkbowl.vault.economy.Economy;
import ru.fleyer.gucciplate.GucciPlate;

/**
 * @author Fleyer
 * <p> EconomyUtils creation on 01.05.2023 at 14:37
 */
@UtilityClass
public class EconomyUtils {
    @Getter
    Economy economy;

    public void setupEconomy() {
        val economyProvider = GucciPlate.instance.getServer()
                .getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
    }
}
