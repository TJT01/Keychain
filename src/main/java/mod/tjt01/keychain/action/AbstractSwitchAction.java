package mod.tjt01.keychain.action;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mod.tjt01.keychain.api.IKeychainAPI;
import mod.tjt01.keychain.api.KeyActionType;
import mod.tjt01.keychain.api.KeychainAction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSwitchAction implements KeychainAction {
    public final Map<Item, KeychainAction> actions;

    @Nullable
    public KeychainAction last = null;

    public AbstractSwitchAction(Map<Item, KeychainAction> actions) {
        this.actions = new HashMap<>(actions);
    }

    protected boolean doAction(KeyActionType type, ItemStack stack) {
        if (type == KeyActionType.UP) {
            if (last != null) {
                last.fire(type);
                return true;
            } else {
                return false;
            }
        }
        KeychainAction action = this.actions.get(stack.getItem());
        if (action == null) {
            return false;
        }
        action.fire(type);
        last = action;
        return true;
    }

    public interface Serializer<T extends AbstractSwitchAction> extends KeychainAction.Serializer<T> {
        @Override
        default void toJson(T t, JsonObject object) {
            IKeychainAPI api = IKeychainAPI.getInstance();
            JsonObject actionObject = new JsonObject();
            t.actions.forEach(
                    (item, keychainAction) -> actionObject.add(
                            item.getRegistryName().toString(),
                            api.getActionSerializer(keychainAction).getJson(keychainAction)
                    )
            );
            object.add("actions", actionObject);
        }

        T fromJson(JsonObject object, HashMap<Item, KeychainAction> map);

        @Override
        default T fromJson(JsonObject object) {
            IKeychainAPI api = IKeychainAPI.getInstance();
            HashMap<Item, KeychainAction> newMap = new HashMap<>();
            JsonObject actionObject = GsonHelper.getAsJsonObject(object, "actions");
            actionObject.entrySet().forEach(stringJsonElementEntry -> {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(stringJsonElementEntry.getKey()));
                if (item == null) {
                    throw new JsonSyntaxException("Item \"" + stringJsonElementEntry.getKey() + "\" does not exist");
                }
                JsonObject object2 = GsonHelper.convertToJsonObject(stringJsonElementEntry.getValue(), stringJsonElementEntry.getKey());
                KeychainAction.Serializer<? extends KeychainAction> serializer = api.getActionSerializer(new ResourceLocation(GsonHelper.getAsString(object2, "type")));
                if (serializer == null) throw new JsonSyntaxException(
                        "Action type \"" + GsonHelper.getAsString(object2, "type") + "\" does not exist"
                );
                KeychainAction action = serializer.fromJson(object2);
                newMap.put(item, action);
            });
            return this.fromJson(object, newMap);
        }
    }
}
