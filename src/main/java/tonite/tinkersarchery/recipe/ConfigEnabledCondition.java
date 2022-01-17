package tonite.tinkersarchery.recipe;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.TinkersArcheryConfig;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class ConfigEnabledCondition implements ICondition, ILootCondition {
    public static final ResourceLocation ID = TinkersArchery.getResource("config");
    public static final Serializer SERIALIZER = new Serializer();
    private static final Map<String, ConfigEnabledCondition> PROPS = new HashMap();
    private final String configName;
    private final BooleanSupplier supplier;
    public static final ConfigEnabledCondition ALWAYS_ALLOW_STEEL_ON_STRING_BOWSTRING_FOR_STEEL_WIRE;
    public static final ConfigEnabledCondition LEGACY_ARROWS;

    public ResourceLocation getID() {
        return ID;
    }

    public boolean test() {
        return this.supplier.getAsBoolean();
    }

    public boolean test(LootContext lootContext) {
        return this.supplier.getAsBoolean();
    }

    public LootConditionType getType() {
        return TinkersArchery.lootConfig;
    }

    private static ConfigEnabledCondition add(String prop, BooleanSupplier supplier) {
        ConfigEnabledCondition conf = new ConfigEnabledCondition(prop, supplier);
        PROPS.put(prop.toLowerCase(Locale.ROOT), conf);
        return conf;
    }

    private static ConfigEnabledCondition add(String prop, ForgeConfigSpec.BooleanValue supplier) {
        supplier.getClass();
        return add(prop, supplier::get);
    }

    public String toString() {
        return "config_setting_enabled(\"" + this.configName + "\")";
    }

    private ConfigEnabledCondition(String configName, BooleanSupplier supplier) {
        this.configName = configName;
        this.supplier = supplier;
    }

    static {
        ALWAYS_ALLOW_STEEL_ON_STRING_BOWSTRING_FOR_STEEL_WIRE = add("always_allow_steel_on_string_bowstring_for_steel_wire", TinkersArcheryConfig.COMMON.alwaysAllowSteelOnStringBowstringForSteelWire);
        LEGACY_ARROWS = add("legacy_arrows", TinkersArcheryConfig.COMMON.legacyArrows);
    }

    private static class Serializer implements ILootSerializer<ConfigEnabledCondition>, IConditionSerializer<ConfigEnabledCondition> {
        private Serializer() {
        }

        public ResourceLocation getID() {
            return ConfigEnabledCondition.ID;
        }

        public void write(JsonObject json, ConfigEnabledCondition value) {
            json.addProperty("prop", value.configName);
        }

        public ConfigEnabledCondition read(JsonObject json) {
            String prop = JSONUtils.getAsString(json, "prop");
            ConfigEnabledCondition config = (ConfigEnabledCondition) ConfigEnabledCondition.PROPS.get(prop.toLowerCase(Locale.ROOT));
            if (config == null) {
                throw new JsonSyntaxException("Invalid property name '" + prop + "'");
            } else {
                return config;
            }
        }

        public void serialize(JsonObject json, ConfigEnabledCondition condition, JsonSerializationContext context) {
            this.write(json, condition);
        }

        public ConfigEnabledCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            return this.read(json);
        }
    }
}

