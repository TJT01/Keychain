package mod.tjt01.keychain.impl;

import mod.tjt01.keychain.Keychain;
import mod.tjt01.keychain.api.IKeychainAPI;
import mod.tjt01.keychain.api.KeychainAction;
import mod.tjt01.keychain.api.KeychainEvent;
import mod.tjt01.keychain.registry.KeychainRegistries;
import net.minecraft.resources.ResourceLocation;

public class KeychainAPIImpl implements IKeychainAPI {
    public static final KeychainAPIImpl INSTANCE = new KeychainAPIImpl();

    @Override
    public boolean isStub() {
        return false;
    }

    @Override
    public KeychainAction.Serializer<? extends KeychainAction> getActionSerializer(ResourceLocation location) {
        return KeychainRegistries.ACTIONS.get(location);
    }

    @Override
    public void registerActionSerializer(KeychainAction.Serializer<? extends KeychainAction> serializer) {
        KeychainRegistries.ACTIONS.put(serializer.getId(), serializer);
    }

    @Override
    public KeychainEvent.Serializer<?> getEventSerializer(ResourceLocation location) {
        return KeychainRegistries.EVENTS.get(location);
    }

    @Override
    public void registerEventSerializer(KeychainEvent.Serializer<? extends KeychainEvent> serializer) {
        KeychainRegistries.EVENTS.put(serializer.getId(), serializer);
    }
}
