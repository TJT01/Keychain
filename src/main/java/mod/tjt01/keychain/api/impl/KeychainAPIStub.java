package mod.tjt01.keychain.api.impl;

import mod.tjt01.keychain.api.IKeychainAPI;
import mod.tjt01.keychain.api.KeychainAction;
import mod.tjt01.keychain.api.KeychainEvent;
import mod.tjt01.keychain.impl.KeychainAPIImpl;
import net.minecraft.resources.ResourceLocation;

public class KeychainAPIStub implements IKeychainAPI {
    public static final KeychainAPIStub INSTANCE = new KeychainAPIStub();

    @Override
    public boolean isStub() {
        return true;
    }

    @Override
    public KeychainAction.Serializer<? extends KeychainAction> getActionSerializer(ResourceLocation location) {
        return null;
    }

    @Override
    public void registerActionSerializer(KeychainAction.Serializer<? extends KeychainAction> serializer) {}

    @Override
    public KeychainEvent.Serializer<?> getEventSerializer(ResourceLocation location) {
        return null;
    }

    @Override
    public void registerEventSerializer(KeychainEvent.Serializer<?> serializer) {}
}
