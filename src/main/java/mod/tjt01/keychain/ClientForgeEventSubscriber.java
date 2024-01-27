package mod.tjt01.keychain;

import com.mojang.blaze3d.platform.InputConstants;
import mod.tjt01.keychain.api.KeyActionType;
import mod.tjt01.keychain.api.KeychainEvent;
import mod.tjt01.keychain.event.KeyMappingInputEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Keychain.MODID)
public class ClientForgeEventSubscriber {
    private static void onInput(InputConstants.Key key) {
        for (KeychainEvent event: Keychain.events) {
            if (event instanceof KeyMappingInputEvent keyEvent) {
                keyEvent.fire(key);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        InputConstants.Key key = InputConstants.getKey(event.getKey(), event.getScanCode());
        if (event.getAction() == GLFW.GLFW_PRESS || event.getAction() == GLFW.GLFW_RELEASE) {
            onInput(key);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMouseInput(InputEvent.MouseInputEvent event) {
        InputConstants.Key key = InputConstants.Type.MOUSE.getOrCreate(event.getButton());
        if (event.getAction() == GLFW.GLFW_PRESS || event.getAction() == GLFW.GLFW_RELEASE) {
            onInput(key);
        }
    }
}
