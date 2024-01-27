package mod.tjt01.keychain.api;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface KeychainAction {
    void fire(KeyActionType action);

    ResourceLocation getId();

    interface Serializer<T extends KeychainAction> {
        void toJson(T t, JsonObject object);

        T fromJson(JsonObject object);

        ResourceLocation getId();

        default JsonObject getJson(T t) {
            JsonObject object = new JsonObject();
            this.toJson(t, object);
            object.addProperty("type", this.getId().toString());
            return object;
        }
    }

}