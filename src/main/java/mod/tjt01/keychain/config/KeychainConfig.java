package mod.tjt01.keychain.config;

import com.google.gson.*;
import com.mojang.blaze3d.platform.InputConstants;
import mod.tjt01.keychain.Keychain;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import mod.tjt01.keychain.api.KeychainEvent;
import mod.tjt01.keychain.registry.KeychainRegistries;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.fml.loading.FMLPaths;

public class KeychainConfig {
    public static final HashMap<String, String> CATEGORY_NAMES = new HashMap<>();

    private static final GsonBuilder GSON_BUILDER = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping();

    static {
        CATEGORY_NAMES.put("MOVEMENT", KeyMapping.CATEGORY_MOVEMENT);
        CATEGORY_NAMES.put("MISC", KeyMapping.CATEGORY_MISC);
        CATEGORY_NAMES.put("MULTIPLAYER", KeyMapping.CATEGORY_MULTIPLAYER);
        CATEGORY_NAMES.put("GAMEPLAY", KeyMapping.CATEGORY_GAMEPLAY);
        CATEGORY_NAMES.put("INVENTORY", KeyMapping.CATEGORY_INVENTORY);
        CATEGORY_NAMES.put("UI", KeyMapping.CATEGORY_INTERFACE);
        CATEGORY_NAMES.put("CREATIVE", KeyMapping.CATEGORY_CREATIVE);
    }

    public static Path getKeychainPath() {
        return FMLPaths.getOrCreateGameRelativePath(
                FMLPaths.CONFIGDIR.relative().resolve("keychain"), "keychain config folder"
        );
    }

    public static List<CustomKey> loadCustomKeys() {
        try {
            LinkedList<CustomKey> customKeys = new LinkedList<>();

            Path keychainPath = getKeychainPath();
            Path customKeysP = keychainPath.resolve("custom_mappings.json");
            if (!Files.exists(customKeysP)) {
                try (OutputStream stream = Files.newOutputStream(customKeysP);) {
                    Gson json = GSON_BUILDER.create();
                    JsonObject main = new JsonObject();
                    main.addProperty("prefix", "key.keychain.");
                    JsonArray customKeyArray = new JsonArray();
                    JsonObject customKey = new JsonObject();
                    customKey.addProperty("name", "example_mapping");
                    customKey.addProperty("category", "key.categories.misc");
                    customKey.addProperty("default", "key.keyboard.r");
                    customKeyArray.add(customKey);
                    main.add("binds", customKeyArray);
                    stream.write(json.toJson(main).getBytes(StandardCharsets.UTF_8));
                    stream.flush();
                }

            }
            Gson json = GSON_BUILDER.create();
            JsonObject object = json.fromJson(Files.readString(customKeysP), JsonObject.class);
            String prefix = GsonHelper.getAsString(object, "prefix");
            JsonArray keys = GsonHelper.getAsJsonArray(object, "binds");

            for (JsonElement element: keys) {
                JsonObject userBind = GsonHelper.convertToJsonObject(element, "binds");
                String category = GsonHelper.getAsString(userBind, "category", "MISC");
                customKeys.add(
                        new CustomKey(
                                prefix + GsonHelper.getAsString(userBind, "name"),
                                CATEGORY_NAMES.getOrDefault(category, category),
                                InputConstants.getKey(GsonHelper.getAsString(userBind, "default", "key.keyboard.unknown"))
                        )
                );
            }

            return customKeys;
        } catch (IOException e) {
            Keychain.LOGGER.error("Failed to get config: ", e);
            throw new RuntimeException(e);
        }
    }

    public static void loadEvents() {
        Path keychainPath = getKeychainPath();
        Path eventsPath = keychainPath.resolve("events");
        if (!Files.exists(eventsPath)) {
            try {
                Files.createDirectory(eventsPath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        LinkedList<KeychainEvent> events = new LinkedList<>();
        try (Stream<Path> files = Files.list(eventsPath)) {
            files.forEach(path -> {
                Gson json = GSON_BUILDER.create();
                try {
                    String s = Files.readString(path);
                    JsonObject object = json.fromJson(s, JsonObject.class);
                    KeychainEvent.Serializer<?> eventSerializer = KeychainRegistries.EVENTS.get(
                            new ResourceLocation(GsonHelper.getAsString(object, "type"))
                    );
                    if (eventSerializer == null) throw new JsonSyntaxException(
                            path.getFileName().toString() +
                                    ": Event type \"" +
                                    GsonHelper.getAsString(object, "type") + "\" does not exist"
                    );
                    events.add(eventSerializer.loadJson(object));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        Keychain.events.clear();
        Keychain.events.addAll(events);
    }

    public static class CustomKey {
        public final String name;
        public final String category;
        public final InputConstants.Key default_bind;

        public CustomKey(String name, String category, InputConstants.Key default_bind) {
            this.name = name;
            this.category = category;
            this.default_bind = default_bind;
        }
    }
}
