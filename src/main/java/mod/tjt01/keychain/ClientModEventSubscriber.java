package mod.tjt01.keychain;

import mod.tjt01.keychain.config.KeychainConfig;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = Keychain.MODID)
public class ClientModEventSubscriber {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        List<KeychainConfig.CustomKey> customKeys = KeychainConfig.loadCustomKeys();
        //ClientRegistry.registerKeyBinding(KeychainMappings.test);
        for (KeychainConfig.CustomKey key: customKeys) {
            ClientRegistry.registerKeyBinding(
                    new KeyMapping(key.name, key.default_bind.getType(), key.default_bind.getValue(), key.category)
            );
        }

        KeychainConfig.loadEvents();
    }
}
