package tonite.tinkersarchery;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import slimeknights.mantle.registration.ModelFluidAttributes;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.TinkerEffect;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.client.data.material.GeneratorPartTextureJsonGenerator;
import slimeknights.tconstruct.library.client.data.material.MaterialPartTextureGenerator;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.data.sprite.TinkerMaterialSpriteProvider;
import slimeknights.tconstruct.tools.data.sprite.TinkerPartSpriteProvider;
import slimeknights.tconstruct.tools.modifiers.effect.NoMilkEffect;
import tonite.tinkersarchery.data.client.*;
import tonite.tinkersarchery.data.server.*;
import tonite.tinkersarchery.entities.TinkersArrowEntity;
import tonite.tinkersarchery.items.tools.ArrowTool;
import tonite.tinkersarchery.items.tools.BowTool;
import tonite.tinkersarchery.items.tools.CrossbowTool;
import tonite.tinkersarchery.library.ProjectileTrajectory;
import tonite.tinkersarchery.modifiers.abilities.*;
import tonite.tinkersarchery.modifiers.slotless.flares.FlameFlare;
import tonite.tinkersarchery.modifiers.slotless.flares.ShulkerFlare;
import tonite.tinkersarchery.modifiers.traits.*;
import tonite.tinkersarchery.modifiers.upgrades.*;
import tonite.tinkersarchery.stats.*;
import tonite.tinkersarchery.tools.BowAndArrowDefinitions;
import tonite.tinkersarchery.trajectories.*;

import java.util.Locale;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("tinkersarchery")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TinkersArchery
{
    public static final String MOD_ID = "tinkersarchery";

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    private static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, MOD_ID);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MOD_ID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    private static final ItemDeferredRegisterExtension ITEMS_EXTENDED = new ItemDeferredRegisterExtension(MOD_ID);
    private static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(MOD_ID);
    private static final DeferredRegister<Modifier> MODIFIERS = DeferredRegister.create(Modifier.class, MOD_ID);
    private static final DeferredRegister<ProjectileTrajectory> PROJECTILE_TRAJECTORIES = DeferredRegister.create(ProjectileTrajectory.class, MOD_ID);

    protected static final Supplier<IForgeRegistry<ProjectileTrajectory>> PROJECTILE_TRAJECTORY_REGISTRY = PROJECTILE_TRAJECTORIES.makeRegistry("projectile_trajectory", () -> new RegistryBuilder<ProjectileTrajectory>()
            .setDefaultKey(getResource("gravity"))
            /*.add((IForgeRegistry.AddCallback<ProjectileTrajectory>) (owner, stage, id, obj, oldObj) -> {
                TinkerRegistries.MODIFIERS.register(new TrajectoryApplier(obj).setRegistryName(obj.getRegistryName()));
            })*/);

    private static final ItemGroup TAB_TINKERS_ARCHERY = new ItemGroup(MOD_ID) {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return shortbow.get().getRenderTool();
        }
    };

    private static final Item.Properties TINKERS_ARCHERY_PROPS = new Item.Properties().tab(TAB_TINKERS_ARCHERY);

    private static final Supplier<Item.Properties> TOOL = () -> new Item.Properties().tab(TinkerTools.TAB_TOOLS);
    private static final Item.Properties PARTS_PROPS = new Item.Properties().tab(TinkerToolParts.TAB_TOOL_PARTS);

    public static final ItemObject<ToolPartItem> bowshaft = ITEMS_EXTENDED.register("bowshaft", () -> new ToolPartItem(PARTS_PROPS, BowMaterialStats.ID));
    public static final ItemObject<ToolPartItem> bowstring = ITEMS_EXTENDED.register("bowstring", () -> new ToolPartItem(PARTS_PROPS, BowStringMaterialStats.ID));
    public static final ItemObject<ToolPartItem> bowguide = ITEMS_EXTENDED.register("bowguide", () -> new ToolPartItem(PARTS_PROPS, BowGuideMaterialStats.ID));
    public static final ItemObject<ToolPartItem> large_bowshaft = ITEMS_EXTENDED.register("large_bowshaft", () -> new ToolPartItem(PARTS_PROPS, BowMaterialStats.ID));
    public static final ItemObject<ToolPartItem> crossbow_arm = ITEMS_EXTENDED.register("crossbow_arm", () -> new ToolPartItem(PARTS_PROPS, BowGuideMaterialStats.ID));
    public static final ItemObject<ToolPartItem> arrowhead = ITEMS_EXTENDED.register("arrowhead", () -> new ToolPartItem(PARTS_PROPS, ArrowHeadMaterialStats.ID));
    public static final ItemObject<ToolPartItem> arrow_shaft = ITEMS_EXTENDED.register("arrow_shaft", () -> new ToolPartItem(PARTS_PROPS, ArrowShaftMaterialStats.ID));
    public static final ItemObject<ToolPartItem> arrow_fletching = ITEMS_EXTENDED.register("arrow_fletching", () -> new ToolPartItem(PARTS_PROPS, ArrowFletchingMaterialStats.ID));

    public static final RegistryObject<BowTool> shortbow = ITEMS.register("shortbow", () -> new BowTool(TOOL.get().addToolType(BowTool.TOOL_TYPE, 0), BowAndArrowDefinitions.SHORTBOW));
    public static final RegistryObject<BowTool> longbow = ITEMS.register("longbow", () -> new BowTool(TOOL.get().addToolType(BowTool.TOOL_TYPE, 0), BowAndArrowDefinitions.LONGBOW));
    public static final RegistryObject<CrossbowTool> crossbow = ITEMS.register("crossbow", () -> new CrossbowTool(TOOL.get().addToolType(BowTool.TOOL_TYPE, 0), BowAndArrowDefinitions.CROSSBOW));
    public static final RegistryObject<ArrowTool> arrow = ITEMS.register("arrow", () -> new ArrowTool(TOOL.get().addToolType(ArrowTool.TOOL_TYPE, 0), BowAndArrowDefinitions.ARROW));

    public static final RegistryObject<Block> tantalum_ore = BLOCKS.register("tantalum_ore", () -> new OreBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F).harvestLevel(1)));

    public static final RegistryObject<Block> tantalum_block = BLOCKS.register("tantalum_block", () -> new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.ICE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).harvestLevel(1).sound(SoundType.METAL)));
    public static final RegistryObject<Block> cobalt_tantalum_block = BLOCKS.register("cobalt_tantalum_block", () -> new Block(AbstractBlock.Properties.of(Material.METAL, MaterialColor.ICE).requiresCorrectToolForDrops().strength(6.0F, 8.0F).harvestLevel(2).sound(SoundType.METAL)));
    public static final RegistryObject<Block> galaxy_alloy_block = BLOCKS.register("galaxy_alloy_block", () -> new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL, MaterialColor.ICE).requiresCorrectToolForDrops().strength(8.0F, 10.0F).harvestLevel(3).sound(SoundType.NETHERITE_BLOCK)));

    public static final RegistryObject<Item> tantalum_ore_item = ITEMS.register("tantalum_ore", () -> new BlockItem(tantalum_ore.get(), TINKERS_ARCHERY_PROPS));

    public static final RegistryObject<Item> tantalum_block_item = ITEMS.register("tantalum_block", () -> new BlockItem(tantalum_block.get(), TINKERS_ARCHERY_PROPS));
    public static final RegistryObject<Item> cobalt_tantalum_block_item = ITEMS.register("cobalt_tantalum_block", () -> new BlockItem(cobalt_tantalum_block.get(), TINKERS_ARCHERY_PROPS));
    public static final RegistryObject<Item> galaxy_alloy_block_item = ITEMS.register("galaxy_alloy_block", () -> new BlockItem(galaxy_alloy_block.get(), TINKERS_ARCHERY_PROPS));

    public static final RegistryObject<Item> tantalum_ingot = ITEMS.register("tantalum_ingot", () -> new Item(TINKERS_ARCHERY_PROPS));
    public static final RegistryObject<Item> cobalt_tantalum_ingot = ITEMS.register("cobalt_tantalum_ingot", () -> new Item(TINKERS_ARCHERY_PROPS));
    public static final RegistryObject<Item> galaxy_alloy_ingot = ITEMS.register("galaxy_alloy_ingot", () -> new Item(TINKERS_ARCHERY_PROPS));

    public static final RegistryObject<Item> tantalum_nugget = ITEMS.register("tantalum_nugget", () -> new Item(TINKERS_ARCHERY_PROPS));
    public static final RegistryObject<Item> cobalt_tantalum_nugget = ITEMS.register("cobalt_tantalum_nugget", () -> new Item(TINKERS_ARCHERY_PROPS));
    public static final RegistryObject<Item> galaxy_alloy_nugget = ITEMS.register("galaxy_alloy_nugget", () -> new Item(TINKERS_ARCHERY_PROPS));

    public static final FluidObject molten_tantalum = FLUIDS.register("molten_tantalum", hotBuilder().temperature(900), Material.LAVA, 12);
    public static final FluidObject molten_cobalt_tantalum = FLUIDS.register("molten_cobalt_tantalum", hotBuilder().temperature(1200), Material.LAVA, 12);
    public static final FluidObject molten_galaxy_alloy = FLUIDS.register("molten_galaxy_alloy", hotBuilder().temperature(1700), Material.LAVA, 15);

    public static final CastItemObject bowshaft_cast = ITEMS_EXTENDED.registerCast("bowshaft", TINKERS_ARCHERY_PROPS);
    public static final CastItemObject bowguide_cast = ITEMS_EXTENDED.registerCast("bowguide", TINKERS_ARCHERY_PROPS);
    public static final CastItemObject large_bowshaft_cast = ITEMS_EXTENDED.registerCast("large_bowshaft", TINKERS_ARCHERY_PROPS);
    public static final CastItemObject crossbow_arm_cast = ITEMS_EXTENDED.registerCast("crossbow_arm", TINKERS_ARCHERY_PROPS);
    public static final CastItemObject arrowhead_cast = ITEMS_EXTENDED.registerCast("arrowhead", TINKERS_ARCHERY_PROPS);
    public static final CastItemObject arrow_shaft_cast = ITEMS_EXTENDED.registerCast("arrow_shaft", TINKERS_ARCHERY_PROPS);

    public static final RegistryObject<Modifier> ACCURATE_MODIFIER = MODIFIERS.register("accurate", Accurate::new);
    public static final RegistryObject<Modifier> FINISHING_MODIFIER = MODIFIERS.register("finishing", Finishing::new);
    public static final RegistryObject<Modifier> WEIGHTY_MODIFIER = MODIFIERS.register("weighty", Weighty::new);
    public static final RegistryObject<Modifier> SWIFTSTRIKE_MODIFIER = MODIFIERS.register("swiftstrike", Swiftstrike::new);
    public static final RegistryObject<Modifier> GROOVY_MODIFIER = MODIFIERS.register("groovy", Groovy::new);
    public static final RegistryObject<Modifier> CHAINING_MODIFIER = MODIFIERS.register("chaining", Chaining::new);
    public static final RegistryObject<Modifier> CLEAN_MODIFIER = MODIFIERS.register("clean", Clean::new);
    public static final RegistryObject<Modifier> SUPERSLIME_MODIFIER = MODIFIERS.register("superslime", Superslime::new);
    public static final RegistryObject<Modifier> UPLIFTING_MODIFIER = MODIFIERS.register("uplifting", Uplifting::new);
    public static final RegistryObject<Modifier> BLAZING_MODIFIER = MODIFIERS.register("blazing", Blazing::new);
    public static final RegistryObject<Modifier> SLICING_MODIFIER = MODIFIERS.register("slicing", Slicing::new);
    public static final RegistryObject<Modifier> AIRBORNE_MODIFIER = MODIFIERS.register("airborne", Airborne::new);

    public static final RegistryObject<Modifier> MULTISHOT_MODIFIER = MODIFIERS.register("multishot", Multishot::new);
    public static final RegistryObject<Modifier> AUTOAIM_MODIFIER = MODIFIERS.register("autoaim", Autoaim::new);
    public static final RegistryObject<Modifier> PIERCING_MODIFIER = MODIFIERS.register("piercing", Piercing::new);
    public static final RegistryObject<Modifier> EXPLOSIVE_MODIFIER = MODIFIERS.register("explosive", Explosive::new);

    public static final RegistryObject<Modifier> HASTE_MODIFIER = MODIFIERS.register("haste", Haste::new);
    public static final RegistryObject<Modifier> POWER_MODIFIER = MODIFIERS.register("power", Power::new);
    public static final RegistryObject<Modifier> LAUNCHING_MODIFIER = MODIFIERS.register("launching", Launching::new);
    public static final RegistryObject<Modifier> PINPOINTER_MODIFIER = MODIFIERS.register("pinpointer", Pinpointer::new);
    public static final RegistryObject<Modifier> PINPOINTER_ARROW_MODIFIER = MODIFIERS.register("pinpointer_arrow", Pinpointer::new);
    public static final RegistryObject<Modifier> BURST_MODIFIER = MODIFIERS.register("burst", Burst::new);
    public static final RegistryObject<Modifier> HIGHLANDER_MODIFIER = MODIFIERS.register("highlander", Highlander::new);
    public static final RegistryObject<Modifier> MOBILE_MODIFIER = MODIFIERS.register("mobile", Mobile::new);
    public static final RegistryObject<Modifier> VELOCITY_MODIFIER = MODIFIERS.register("velocity", Velocity::new);
    public static final RegistryObject<Modifier> HEAVY_MODIFIER = MODIFIERS.register("heavy", Heavy::new);
    public static final RegistryObject<Modifier> AQUADYNAMIC_MODIFIER = MODIFIERS.register("aquadynamic", Aquadynamic::new);

    public static final RegistryObject<Modifier> FLAME_FLARE_MODIFIER = MODIFIERS.register("flame_flare", FlameFlare::new);
    public static final RegistryObject<Modifier> SHULKER_FLARE_MODIFIER = MODIFIERS.register("shulker_flare", ShulkerFlare::new);

    private static final IntFunction<Supplier<TinkerEffect>> MARKER_EFFECT = color -> () -> new NoMilkEffect(EffectType.BENEFICIAL, color, true);
    public static RegistryObject<TinkerEffect> burstEffect = EFFECTS.register("burst", MARKER_EFFECT.apply(0xFFFC921C));
    public static RegistryObject<TinkerEffect> groovyEffect = EFFECTS.register("groovy", MARKER_EFFECT.apply(0xFF21007F));
    public static RegistryObject<TinkerEffect> chainingEffect = EFFECTS.register("chaining", MARKER_EFFECT.apply(0xFF21007F));

    public static final RegistryObject<EntityType<TinkersArrowEntity>> TINKERS_ARROW =
            ENTITY_TYPES.register("tinkers_arrow",
                    () -> EntityType.Builder.<TinkersArrowEntity>of(TinkersArrowEntity::new, EntityClassification.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .setCustomClientFactory((spawnEntity, world) -> new TinkersArrowEntity(TinkersArchery.TINKERS_ARROW.get(), world))
                            .build(getResource("tinkers_arrow").toString()));

    public static final RegistryObject<ProjectileTrajectory> GRAVITY = PROJECTILE_TRAJECTORIES.register("gravity", GravityTrajectory::new);
    public static final RegistryObject<ProjectileTrajectory> FLYING = PROJECTILE_TRAJECTORIES.register("flying", FlyingTrajectory::new);
    public static final RegistryObject<ProjectileTrajectory> ANTIGRAVITY = PROJECTILE_TRAJECTORIES.register("antigravity", AntigravityTrajectory::new);
    public static final RegistryObject<ProjectileTrajectory> TWIRLING = PROJECTILE_TRAJECTORIES.register("twirling", TwirlingTrajectory::new);
    public static final RegistryObject<ProjectileTrajectory> BOUNCING = PROJECTILE_TRAJECTORIES.register("bouncing", BouncingTrajectory::new);
    public static final RegistryObject<ProjectileTrajectory> LOOPING = PROJECTILE_TRAJECTORIES.register("looping", LoopingTrajectory::new);

    public static final RegistryObject<Modifier> GRAVITY_TRAJECTORY_MODIFIER = MODIFIERS.register("gravity_trajectory", () -> new TrajectoryApplier(0xFFADADAD, GRAVITY::get));
    public static final RegistryObject<Modifier> FLYING_TRAJECTORY_MODIFIER = MODIFIERS.register("flying_trajectory", () -> new TrajectoryApplier(0xFFF7CDBB, FLYING::get));
    public static final RegistryObject<Modifier> ANTIGRAVITY_TRAJECTORY_MODIFIER = MODIFIERS.register("antigravity_trajectory", () -> new TrajectoryApplier(0xFFC3B9A1, ANTIGRAVITY::get));
    public static final RegistryObject<Modifier> TWIRLING_TRAJECTORY_MODIFIER = MODIFIERS.register("twirling_trajectory", () -> new TrajectoryApplier(0xFF4AD718, TWIRLING::get));
    public static final RegistryObject<Modifier> BOUNCING_TRAJECTORY_MODIFIER = MODIFIERS.register("bouncing_trajectory", () -> new TrajectoryApplier(0xFF36FFFC, BOUNCING::get));
    public static final RegistryObject<Modifier> LOOPING_TRAJECTORY_MODIFIER = MODIFIERS.register("looping_trajectory", () -> new TrajectoryApplier(0xFFFFFFFF, LOOPING::get));

    public static ConfiguredFeature<?, ?> TANTALUM_ORE_FEATURE;

    public TinkersArchery() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the setup method for modloading
        bus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        bus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        bus.addListener(this::processIMC);

        ENTITY_TYPES.register(bus);
        EFFECTS.register(bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);
        ITEMS_EXTENDED.register(bus);
        FLUIDS.register(bus);
        PROJECTILE_TRAJECTORIES.register(bus);
        MODIFIERS.register(bus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            TANTALUM_ORE_FEATURE = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, getResource("tantalum_ore"),
                    Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, TinkersArchery.tantalum_ore.get().defaultBlockState(), 9))
                            .range(48)
                            .squared()
                            .count(18));
        });
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        MaterialRegistry.getInstance().registerStatType(BowMaterialStats.DEFAULT, BowMaterialStats.class);
        MaterialRegistry.getInstance().registerStatType(BowStringMaterialStats.DEFAULT, BowStringMaterialStats.class);
        MaterialRegistry.getInstance().registerStatType(BowGuideMaterialStats.DEFAULT, BowGuideMaterialStats.class);
        MaterialRegistry.getInstance().registerStatType(ArrowHeadMaterialStats.DEFAULT, ArrowHeadMaterialStats.class);
        MaterialRegistry.getInstance().registerStatType(ArrowShaftMaterialStats.DEFAULT, ArrowShaftMaterialStats.class);
        MaterialRegistry.getInstance().registerStatType(ArrowFletchingMaterialStats.DEFAULT, ArrowFletchingMaterialStats.class);
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("tinkersarchery", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }

    }

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            generator.addProvider(new TinkersArcheryLootTables(generator));
            generator.addProvider(new TinkersArcheryRecipes(generator));
            AbstractMaterialDataProvider materials = new TinkersArcheryMaterialDefinitions(generator);
            generator.addProvider(materials);
            generator.addProvider(new TinkersArcheryMaterialDefinitions.TinkersArcheryMaterialTraitDataProvider(generator, materials));
            generator.addProvider(new TinkersArcheryMaterialDefinitions.TinkersArcheryMaterialStatsDataProvider(generator, materials));
            AbstractMaterialDataProvider tinkertraits = new TinkerExtendedTraitsMaterialDefinitionsProvider(generator);
            generator.addProvider(new TinkerExtendedTraitsMaterialDefinitionsProvider.TinkerExtendedMaterialTraitDataProvider(generator, tinkertraits));
            AbstractMaterialDataProvider tinkerstats = new TinkerExtendedStatsMaterialDefinitionsProvider(generator);
            generator.addProvider(new TinkerExtendedStatsMaterialDefinitionsProvider.TinkerExtendedStatsMaterialStatsDataProvider(generator, tinkerstats));
            generator.addProvider(new TinkersArcheryToolDefinitions(generator));
            generator.addProvider(new TinkersArcheryToolSlotLayouts(generator));
            BlockTagsProvider blockTags = new TinkersArcheryTags.TinkersArcheryBlockTags(generator, existingFileHelper);
            generator.addProvider(blockTags);
            generator.addProvider(new TinkersArcheryTags.TinkersArcheryItemTags(generator, blockTags, existingFileHelper));
            generator.addProvider(new TinkersArcheryTags.TinkersArcheryFluidTags(generator, existingFileHelper));
        }
        if (event.includeClient()) {
            generator.addProvider(new TinkersArcheryLang(generator));
            generator.addProvider(new TinkersArcheryItemModels(generator, existingFileHelper));
            generator.addProvider(new TinkersArcheryBlockStates(generator, existingFileHelper));
            TinkerMaterialSpriteProvider tinkerMaterialSprites = new TinkerExtendedMaterialSpriteProvider();
            TinkersArcheryMaterialSpriteProvider tinkersArcheryMaterialSprites = new TinkersArcheryMaterialSpriteProvider();
            TinkerPartSpriteProvider tinkerPartSprites = new TinkerPartSpriteProvider();
            TinkersArcheryPartSpriteProvider tinkersArcheryPartSprites = new TinkersArcheryPartSpriteProvider();
            generator.addProvider(new TinkersArcheryMaterialRenderInfoProvider(generator, tinkersArcheryMaterialSprites));
            generator.addProvider(new GeneratorPartTextureJsonGenerator(generator, TinkersArchery.MOD_ID, tinkersArcheryPartSprites));
            generator.addProvider(new MaterialPartTextureGenerator(generator, existingFileHelper, tinkerPartSprites, tinkersArcheryMaterialSprites));
            generator.addProvider(new MaterialPartTextureGenerator(generator, existingFileHelper, tinkersArcheryPartSprites, tinkerMaterialSprites, tinkersArcheryMaterialSprites));
        }
    }

    public static ResourceLocation getResource(String name){
        return new ResourceLocation(MOD_ID, name);
    }

    public static String prefix(String name) {
        return String.format("%s.%s", MOD_ID, name.toLowerCase(Locale.US));
    }

    private static FluidAttributes.Builder hotBuilder() {
        return ModelFluidAttributes.builder().density(2000).viscosity(10000).temperature(1000).sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA);
    }
}
