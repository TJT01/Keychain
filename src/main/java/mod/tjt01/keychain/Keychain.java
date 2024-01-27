package mod.tjt01.keychain;

import com.mojang.logging.LogUtils;
import mod.tjt01.keychain.action.MappingAction;
import mod.tjt01.keychain.action.SwitchByHeldItemAction;
import mod.tjt01.keychain.action.SwitchByWornArmorAction;
import mod.tjt01.keychain.action.ToggleMappingAction;
import mod.tjt01.keychain.api.IKeychainAPI;
import mod.tjt01.keychain.api.KeychainEvent;
import mod.tjt01.keychain.event.KeyMappingInputEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkConstants;
import org.slf4j.Logger;

import java.util.ArrayList;

@Mod("keychain")
public class Keychain
{
    public static final String MODID = "keychain";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ArrayList<KeychainEvent> events = new ArrayList<>();

    public Keychain() {
        ModLoadingContext context = ModLoadingContext.get();
        context.registerExtensionPoint(
                IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (s, b) -> true)
        );
        /*
        context.registerConfig(ModConfig.Type.CLIENT, KeychainConfig.SPEC);
        //*/
        //ArrayList<KeychainAction> actions = new ArrayList<>();
        //actions.add(new MappingAction(Minecraft.getInstance().options.keyJump));
        //events.add(new KeyMappingInputEvent(KeychainMappings.test, actions));
        IKeychainAPI api = IKeychainAPI.getInstance();

        api.registerEventSerializer(KeyMappingInputEvent.Serializer.INSTANCE);

        api.registerActionSerializer(MappingAction.Serializer.INSTANCE);
        api.registerActionSerializer(ToggleMappingAction.Serializer.INSTANCE);
        api.registerActionSerializer(SwitchByHeldItemAction.Serializer.INSTANCE);
        api.registerActionSerializer(SwitchByWornArmorAction.Serializer.INSTANCE);
    }
}
