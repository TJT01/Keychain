package mod.tjt01.keychain.registry;

import mod.tjt01.keychain.api.KeychainAction;
import mod.tjt01.keychain.api.KeychainEvent;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.ConcurrentHashMap;

public class KeychainRegistries {
    public static final ConcurrentHashMap<ResourceLocation, KeychainAction.Serializer<? extends KeychainAction>> ACTIONS
            = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<ResourceLocation, KeychainEvent.Serializer<? extends KeychainEvent>> EVENTS
            = new ConcurrentHashMap<>();
}
