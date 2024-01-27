package mod.tjt01.keychain.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class KeychainEvent {
    public final ArrayList<KeychainAction> actions;

    public KeychainEvent(List<KeychainAction> actions) {
        this.actions = new ArrayList<>(actions);
    }

    public void fire(KeyActionType actionType) {
        for (KeychainAction action: actions) {
            action.fire(actionType);
        }
    }

    public abstract ResourceLocation getId();

    public interface Serializer<T extends KeychainEvent> {
        void toJson(JsonObject object, T event);

        T fromJson(JsonObject object, List<KeychainAction> actions);

        ResourceLocation getId();

        default T loadJson(JsonObject object) {
            //TODO conditions
            //JsonArray conditions = GsonHelper.getAsJsonArray(object, "conditions", new JsonArray());
            JsonArray actions = GsonHelper.getAsJsonArray(object, "actions");
            ArrayList<KeychainAction> deserialized = new ArrayList<>();
            for (JsonElement element: actions) {
                JsonObject object2 = GsonHelper.convertToJsonObject(element, "actions");
                KeychainAction.Serializer<? extends KeychainAction> serializer = IKeychainAPI
                        .getInstance()
                        .getActionSerializer(new ResourceLocation(GsonHelper.getAsString(object2, "type")));
                if (serializer == null) throw new JsonSyntaxException(
                        "Action type \"" + GsonHelper.getAsString(object2, "type") + "\" does not exist"
                );

                deserialized.add(serializer.fromJson(object2));
            }
            return this.fromJson(object, deserialized);
        }

        default JsonObject getJson(T event) {
            JsonObject object = new JsonObject();
            this.toJson(object, event);
            object.addProperty("type", this.getId().toString());
            return object;
        }
    }
}
