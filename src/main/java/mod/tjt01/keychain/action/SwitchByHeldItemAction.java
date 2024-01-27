package mod.tjt01.keychain.action;

import com.google.gson.JsonObject;
import mod.tjt01.keychain.api.KeyActionType;
import mod.tjt01.keychain.api.KeychainAction;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class SwitchByHeldItemAction extends AbstractSwitchAction{
    public SwitchByHeldItemAction(Map<Item, KeychainAction> actions) {
        super(actions);
    }

    @Override
    public void fire(KeyActionType action) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (!this.doAction(action, player.getItemInHand(InteractionHand.MAIN_HAND))) {
            this.doAction(action, player.getItemInHand(InteractionHand.OFF_HAND));
        }
    }

    @Override
    public ResourceLocation getId() {
        return Serializer.ID;
    }

    public static class Serializer implements AbstractSwitchAction.Serializer<SwitchByHeldItemAction> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation("keychain", "by_held_item");

        @Override
        public SwitchByHeldItemAction fromJson(JsonObject object, HashMap<Item, KeychainAction> map) {
            return new SwitchByHeldItemAction(map);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
