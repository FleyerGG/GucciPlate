package ru.fleyer.gucciplate.plate;

import lombok.Data;
import org.bukkit.Material;

import java.util.List;

/**
 * @author Fleyer
 * <p> PlateInfo creation on 01.05.2023 at 11:41
 */
@Data
public class PlateInfo {
    String type;
    boolean onlineModel;
    Material plateMaterial;
    String displayName;
    List<String> lore;
    double money;
    double time;
    String message;
    String message_type;
}
