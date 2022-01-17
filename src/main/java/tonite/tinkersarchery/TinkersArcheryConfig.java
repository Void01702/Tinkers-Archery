package tonite.tinkersarchery;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

public class TinkersArcheryConfig {

    public static class Common {

        public final BooleanValue generateTantalum;
        public final ConfigValue<Integer> veinCountTantalum;

        public final BooleanValue alwaysAllowSteelOnStringBowstringForSteelWire;

        public final BooleanValue legacyArrows;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Everything to do with gameplay").push("gameplay");

            this.generateTantalum = builder
                    .comment("Generate Tantalum")
                    .translation("tinkerarchery.configgui.generateTantalum")
                    .worldRestart()
                    .define("generateTantalum", true);
            this.veinCountTantalum = builder
                    .comment("Approx Ores per Chunk")
                    .translation("tinkerarchery.configgui.veinCountTantalum")
                    .worldRestart()
                    .define("veinCountTantalum", 10);

            this.alwaysAllowSteelOnStringBowstringForSteelWire = builder
                    .comment("This will always allow you to pour Molten Steel on a String Bowstring to make the Steel Wire Bowstring.\nSome mods make acquiring Steel Wire very difficult, so this option is here for that case.\nThis option won't be taken into account if it has no reason to (there is no steel wire item)")
                    .translation("tinkerarchery.configgui.alwaysAllowSteelOnStringBowstringForSteelWire")
                    .worldRestart()
                    .define("alwaysAllowSteelOnStringBowstringForSteelWire", false);

            this.legacyArrows = builder
                    .comment("Use the older more legacy arrows\nNote: this is not the intended way to use the mod. This option exists for those who prefer the older version of arrows.")
                    .translation("tinkerarchery.configgui.legacyArrows")
                    .worldRestart()
                    .define("legacyArrows", false);

        }

    }

    public static final ForgeConfigSpec commonSpec;
    public static final TinkersArcheryConfig.Common COMMON;

    static {
        final Pair<TinkersArcheryConfig.Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(TinkersArcheryConfig.Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    /** Registers any relevant listeners for config */
    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TinkersArcheryConfig.commonSpec);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(TinkersArcheryConfig::configChanged);
    }

    /** Called when config reloaded to update cached settings */
    private static void configChanged(ModConfig.Reloading event) {
        ModConfig config = event.getConfig();
        if (config.getModId().equals(TinkersArchery.MOD_ID)) {
            ForgeConfigSpec spec = config.getSpec();
            if (spec == TinkersArcheryConfig.commonSpec) {
                //TinkerStructures.addStructureSeparation();
            }
        }
    }

}
