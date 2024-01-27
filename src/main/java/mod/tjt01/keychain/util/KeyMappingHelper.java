package mod.tjt01.keychain.util;

import cpw.mods.modlauncher.api.INameMappingService;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

@MethodsReturnNonnullByDefault
public class KeyMappingHelper {
    static Field countField;

    static {
        try {
            countField = KeyMapping.class.getDeclaredField(ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, "f_90818_"));
            countField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Nullable
    public static KeyMapping find(String name) {
        for (KeyMapping mapping: Minecraft.getInstance().options.keyMappings) {
            if (mapping.getName().equals(name)) return mapping;
        }
        return null;
    }

    public static void click(KeyMapping mapping) {
        try {
            countField.set(mapping, ((int)countField.get(mapping)) + 1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void setState(KeyMapping mapping, boolean down) {
        mapping.setDown(down);
        if (down) click(mapping);
    }
}
