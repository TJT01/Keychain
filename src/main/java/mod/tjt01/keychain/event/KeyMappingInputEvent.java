package mod.tjt01.keychain.event;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.platform.InputConstants;
import mod.tjt01.keychain.Keychain;
import mod.tjt01.keychain.api.KeyActionType;
import mod.tjt01.keychain.api.KeychainAction;
import mod.tjt01.keychain.api.KeychainEvent;
import mod.tjt01.keychain.util.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.List;

public class KeyMappingInputEvent extends KeychainEvent {
    public final KeyMapping mapping;

    public KeyMappingInputEvent(KeyMapping mapping, List<KeychainAction> actions) {
        super(actions);
        this.mapping = mapping;
    }

    public void fire(InputConstants.Key key) {
        if (mapping.getKey().equals(key)) {
            this.fire(mapping.isDown() ? KeyActionType.DOWN : KeyActionType.UP);
        }
    }

    @Override
    public ResourceLocation getId() {
        return Serializer.ID;
    }

    public static class Serializer implements KeychainEvent.Serializer<KeyMappingInputEvent> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation("keychain", "mapping_input");

        @Override
        public void toJson(JsonObject object, KeyMappingInputEvent event) {
            object.addProperty("key", event.mapping.getName());
        }

        @Override
        public KeyMappingInputEvent fromJson(JsonObject object, List<KeychainAction> actions) {
            String name = GsonHelper.getAsString(object, "mapping");
            KeyMapping mapping = KeyMappingHelper.find(name);
            if (mapping == null) {
                throw new JsonSyntaxException("Key mapping \"" + name + "\" could not be found");
            }
            return new KeyMappingInputEvent(mapping, actions);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
