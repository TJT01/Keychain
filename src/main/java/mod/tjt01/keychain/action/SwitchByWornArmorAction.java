package mod.tjt01.keychain.action;

import com.google.gson.JsonObject;
import mod.tjt01.keychain.api.KeyActionType;
import mod.tjt01.keychain.api.KeychainAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class SwitchByWornArmorAction extends AbstractSwitchAction{
    public final EquipmentSlot slot;

    public SwitchByWornArmorAction(Map<Item, KeychainAction> actions, EquipmentSlot slot) {
        super(actions);
        this.slot = slot;
    }

    @Override
    public void fire(KeyActionType action) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            this.doAction(action, Minecraft.getInstance().player.getItemBySlot(slot));
        }
    }

    @Override
    public ResourceLocation getId() {
        return Serializer.ID;
    }

    public static class Serializer implements AbstractSwitchAction.Serializer<SwitchByWornArmorAction> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation("keychain", "by_worn_armor");

        private static String fromEquipmentSlot(EquipmentSlot slot) {
            return switch (slot) {
                case HEAD -> "head";
                case CHEST -> "chest";
                case LEGS -> "legs";
                case FEET -> "feet";
                default -> throw new IllegalArgumentException("Unsupported slot type " + slot);
            };
        }

        private static EquipmentSlot toEquipmentSlot(String s) {
            return switch (s) {
                case "head" -> EquipmentSlot.HEAD;
                case "chest" -> EquipmentSlot.CHEST;
                case "legs" -> EquipmentSlot.LEGS;
                case "feet" -> EquipmentSlot.FEET;
                default -> throw new IllegalArgumentException("Unsupported slot type " + s);
            };
        }

        @Override
        public void toJson(SwitchByWornArmorAction switchByWornArmorAction, JsonObject object) {
            AbstractSwitchAction.Serializer.super.toJson(switchByWornArmorAction, object);
            object.addProperty("slot", fromEquipmentSlot(switchByWornArmorAction.slot));
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public SwitchByWornArmorAction fromJson(JsonObject object, HashMap<Item, KeychainAction> map) {
            return new SwitchByWornArmorAction(map, toEquipmentSlot(GsonHelper.getAsString(object, "slot")));
        }
    }
}
