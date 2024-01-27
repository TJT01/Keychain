package mod.tjt01.keychain.action;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mod.tjt01.keychain.Keychain;
import mod.tjt01.keychain.api.KeyActionType;
import mod.tjt01.keychain.api.KeychainAction;
import mod.tjt01.keychain.util.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class ToggleMappingAction implements KeychainAction {
    public final KeyMapping mapping;

    public ToggleMappingAction(KeyMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    public void fire(KeyActionType action) {
        if (action == KeyActionType.DOWN) {
            KeyMappingHelper.setState(mapping, !mapping.isDown());
        }
    }

    @Override
    public ResourceLocation getId() {
        return Serializer.INSTANCE.getId();
    }

    public static class Serializer implements KeychainAction.Serializer<ToggleMappingAction> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Keychain.MODID, "toggle_mapping");

        @Override
        public void toJson(ToggleMappingAction action, JsonObject object) {

        }

        @Override
        public ToggleMappingAction fromJson(JsonObject object) {
            KeyMapping mapping = KeyMappingHelper.find(GsonHelper.getAsString(object, "mapping"));
            if (mapping == null) {
                throw new JsonSyntaxException("Could not find mapping " + GsonHelper.getAsString(object, "mapping"));
            }
            return new ToggleMappingAction(mapping);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
