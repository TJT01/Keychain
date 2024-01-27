package mod.tjt01.keychain.action;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mod.tjt01.keychain.Keychain;
import mod.tjt01.keychain.util.KeyMappingHelper;
import mod.tjt01.keychain.api.KeyActionType;
import mod.tjt01.keychain.api.KeychainAction;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class MappingAction implements KeychainAction {
    public final KeyMapping mapping;

    public MappingAction(KeyMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    public void fire(KeyActionType action) {
        KeyMappingHelper.setState(mapping, action == KeyActionType.DOWN);
    }

    @Override
    public ResourceLocation getId() {
        return Serializer.INSTANCE.getId();
    }

    public static class Serializer implements KeychainAction.Serializer<MappingAction> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Keychain.MODID, "mapping");

        @Override
        public void toJson(MappingAction action, JsonObject object) {

        }

        @Override
        public MappingAction fromJson(JsonObject object) {
            KeyMapping mapping = KeyMappingHelper.find(GsonHelper.getAsString(object, "mapping"));
            if (mapping == null) {
                throw new JsonSyntaxException("Could not find mapping " + GsonHelper.getAsString(object, "mapping"));
            }
            return new MappingAction(mapping);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
