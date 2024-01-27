package mod.tjt01.keychain.api;

import mod.tjt01.keychain.impl.KeychainAPIImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Objects;

public interface IKeychainAPI {
    Lazy<IKeychainAPI> lazyInstance = Lazy.concurrentOf(() -> {
        try {
            Class<?> clazz = Class.forName("mod.tjt01.keychain.impl.KeychainAPIImpl");
            Object object = FieldUtils.getField(clazz, "INSTANCE").get(null);
            if (object instanceof IKeychainAPI api) {
                return api;
            } else throw new IllegalStateException("Unexpected " + object.getClass().toString());
        } catch (ClassNotFoundException e) {
            return KeychainAPIImpl.INSTANCE;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    });

    static IKeychainAPI getInstance() {
        return lazyInstance.get();
    }

    /**
     * @return <code>true</code> if this implementation is a stub i.e. Keychain is not installed.
     */
    boolean isStub();

    KeychainAction.Serializer<? extends KeychainAction> getActionSerializer(ResourceLocation location);

    @SuppressWarnings("unchecked")
    default <T extends KeychainAction> KeychainAction.Serializer<T> getActionSerializer(KeychainAction action) {
        return (KeychainAction.Serializer<T>) getActionSerializer(action.getId());
    }

    void registerActionSerializer(KeychainAction.Serializer<? extends KeychainAction> serializer);

    KeychainEvent.Serializer<?> getEventSerializer(ResourceLocation location);

    @SuppressWarnings("unchecked")
    default <T extends KeychainEvent> KeychainEvent.Serializer<T> getEventSerializer(KeychainEvent event) {
        return (KeychainEvent.Serializer<T>) getEventSerializer(event.getId());
    }

    void registerEventSerializer(KeychainEvent.Serializer<?> serializer);
}
