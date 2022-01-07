package tonite.tinkersarchery.data.server;

import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.fluids.FluidAttributes;
import slimeknights.mantle.recipe.ingredient.IngredientWithout;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.material.CompositeCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.modifiers.ModifierMatch;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IncrementalModifierRecipeBuilder;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipeBuilder;
import slimeknights.tconstruct.library.recipe.partbuilder.PartRecipeBuilder;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.data.TinkersArcheryMaterialIds;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TinkersArcheryRecipes extends RecipeProvider implements IConditionBuilder, IMaterialRecipeHelper, IToolRecipeHelper, ISmelteryRecipeHelper {

    private static final String toolFolder = "tools/building/";
    private static final String partFolder = "tools/parts/";
    private static final String castFolder = "tools/casts/";
    private static final String modifierFolder = "tools/modifiers/";
    private static final String abilityFolder = modifierFolder + "ability/";
    private static final String upgradeFolder = modifierFolder + "upgrade/";
    private static final String slotlessFolder = modifierFolder + "slotless/";
    private static final String salvageFolder = modifierFolder + "salvage/";
    private static final String castingFolder = "smeltery/casting/metal/";
    private static final String meltingFolder = "smeltery/melting/metal/";
    private static final String materialFolder = "tools/materials/";
    private static final String alloyFolder = "smeltery/alloys/";

    public TinkersArcheryRecipes(DataGenerator gen) {
        super(gen);
    }

    @Override
    public String getModId() {
        return TinkersArchery.MOD_ID;
    }

    protected void partBuilderRecipe (Consumer<IFinishedRecipe> consumer, IMaterialItem part, int cost, String partFolder) {
        String name = Objects.requireNonNull(part.asItem().getRegistryName()).getPath();

        // Part Builder
        PartRecipeBuilder.partRecipe(part)
                .setPattern(modResource(name))
                .setCost(cost)
                .build(consumer, modResource(partFolder + "builder/" + name));

    }

    protected void partBuilderRecipe (Consumer<IFinishedRecipe> consumer, Supplier<? extends IMaterialItem> part, int cost, String partFolder) {
        partBuilderRecipe(consumer, part.get(), cost, partFolder);
    }

    protected void partBuilderCompositeRecipe (Consumer<IFinishedRecipe> consumer, IMaterialItem part, int cost, String partFolder) {
        String name = Objects.requireNonNull(part.asItem().getRegistryName()).getPath();

        // Part Builder
        PartRecipeBuilder.partRecipe(part)
                .setPattern(modResource(name))
                .setCost(cost)
                .build(consumer, modResource(partFolder + "builder/" + name));

        // Composite
        CompositeCastingRecipeBuilder.table(part, cost)
                .build(consumer, modResource(castingFolder + name + "_composite"));
    }

    protected void partBuilderCompositeRecipe (Consumer<IFinishedRecipe> consumer, Supplier<? extends IMaterialItem> part, int cost, String partFolder) {
        partBuilderCompositeRecipe(consumer, part.get(), cost, partFolder);
    }

    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> finishedRecipeConsumer) {

        // Book Recipe
        ConditionalRecipe.builder().addCondition(this.TRUE())
                .addRecipe( ShapelessRecipeBuilder.shapeless(() -> TinkersArchery.awesome_archery.get(), 1)
                        .requires(Items.BOOK)
                        .requires(Items.FEATHER)
                        .group("")
                        .unlockedBy("has_book", has(TinkerCommons.materialsAndYou))
                        ::save )
                .generateAdvancement()
                .build(finishedRecipeConsumer, new ResourceLocation(TinkersArchery.MOD_ID, TinkersArchery.awesome_archery.getId().getPath()));

        // Smelting Recipes
        ConditionalRecipe.builder().addCondition(this.TRUE())
                .addRecipe( CookingRecipeBuilder.smelting(Ingredient.of(TinkersArchery.tantalum_ore_item.get()), TinkersArchery.tantalum_ingot.get(), 1.0F, 200)
                        .unlockedBy("has_ore", has(TinkersArchery.tantalum_ore_item.get())):: save)
                .generateAdvancement()
                .build(finishedRecipeConsumer, new ResourceLocation(TinkersArchery.MOD_ID, "tantalum_ingot"));
        ConditionalRecipe.builder().addCondition(this.TRUE())
                .addRecipe( CookingRecipeBuilder.blasting(Ingredient.of(TinkersArchery.tantalum_ore_item.get()), TinkersArchery.tantalum_ingot.get(), 1.0F, 100)
                        .unlockedBy("has_ore", has(TinkersArchery.tantalum_ore_item.get())):: save)
                .generateAdvancement()
                .build(finishedRecipeConsumer, new ResourceLocation(TinkersArchery.MOD_ID, "tantalum_ingot_from_blasting"));

        // Compression Recipes
        compressionRecipes(finishedRecipeConsumer,
                TinkersArchery.tantalum_block_item.get(),
                TinkersArchery.tantalum_ingot.get(),
                TinkersArchery.tantalum_nugget.get(),
                "tantalum");

        compressionRecipes(finishedRecipeConsumer,
                TinkersArchery.cobalt_tantalum_block_item.get(),
                TinkersArchery.cobalt_tantalum_ingot.get(),
                TinkersArchery.cobalt_tantalum_nugget.get(),
                "cobalt_tantalum");

        compressionRecipes(finishedRecipeConsumer,
                TinkersArchery.galaxy_alloy_block_item.get(),
                TinkersArchery.galaxy_alloy_ingot.get(),
                TinkersArchery.galaxy_alloy_nugget.get(),
                "galaxy_alloy");

        // Alloy Recipes
        AlloyRecipeBuilder.alloy(TinkersArchery.molten_cobalt_tantalum.get(), FluidValues.INGOT * 2)
                .addInput(TinkersArchery.molten_tantalum.get(), FluidValues.INGOT)
                .addInput(TinkerFluids.moltenCobalt.get(), FluidValues.INGOT)
                .build(finishedRecipeConsumer, prefix(TinkersArchery.molten_cobalt_tantalum, alloyFolder));

        AlloyRecipeBuilder.alloy(TinkersArchery.molten_galaxy_alloy.get(), FluidValues.INGOT * 3)
                .addInput(TinkersArchery.molten_tantalum.get(), FluidValues.INGOT * 2)
                .addInput(TinkerFluids.liquidSoul.get(), FluidValues.GLASS_BLOCK)
                .addInput(TinkerFluids.moltenEmerald.get(), FluidValues.GEM)
                .build(finishedRecipeConsumer, prefix(TinkersArchery.molten_galaxy_alloy, alloyFolder));

        // Material Recipes
        metalCasting(finishedRecipeConsumer, TinkersArchery.molten_tantalum, TinkersArchery.tantalum_block_item.get(), TinkersArchery.tantalum_ingot.get(), TinkersArchery.tantalum_nugget.get(), castingFolder, "tantalum");
        metalMelting(finishedRecipeConsumer, TinkersArchery.molten_tantalum.get(), "tantalum", true, meltingFolder, false);
        metalMaterialRecipe(finishedRecipeConsumer, TinkersArcheryMaterialIds.tantalum, materialFolder, "tantalum", false);
        materialMeltingCasting(finishedRecipeConsumer, TinkersArcheryMaterialIds.tantalum, TinkersArchery.molten_tantalum, materialFolder);

        metalCasting(finishedRecipeConsumer, TinkersArchery.molten_cobalt_tantalum, TinkersArchery.cobalt_tantalum_block_item.get(), TinkersArchery.cobalt_tantalum_ingot.get(), TinkersArchery.cobalt_tantalum_nugget.get(), castingFolder, "cobalt_tantalum");
        metalMelting(finishedRecipeConsumer, TinkersArchery.molten_cobalt_tantalum.get(), "cobalt_tantalum", false, meltingFolder, false);
        metalMaterialRecipe(finishedRecipeConsumer, TinkersArcheryMaterialIds.cobalt_tantalum, materialFolder, "cobalt_tantalum", false);
        materialMeltingCasting(finishedRecipeConsumer, TinkersArcheryMaterialIds.cobalt_tantalum, TinkersArchery.molten_cobalt_tantalum, materialFolder);

        metalCasting(finishedRecipeConsumer, TinkersArchery.molten_galaxy_alloy, TinkersArchery.galaxy_alloy_block_item.get(), TinkersArchery.galaxy_alloy_ingot.get(), TinkersArchery.galaxy_alloy_nugget.get(), castingFolder, "galaxy_alloy");
        metalMelting(finishedRecipeConsumer, TinkersArchery.molten_galaxy_alloy.get(), "galaxy_alloy", false, meltingFolder, false);
        metalMaterialRecipe(finishedRecipeConsumer, TinkersArcheryMaterialIds.galaxy_alloy, materialFolder, "galaxy_alloy", false);
        materialMeltingCasting(finishedRecipeConsumer, TinkersArcheryMaterialIds.galaxy_alloy, TinkersArchery.molten_galaxy_alloy, materialFolder);

        materialRecipe(finishedRecipeConsumer, TinkersArcheryMaterialIds.silky_cloth, Ingredient.of(TinkerModifiers.silkyCloth), 1, 1, materialFolder + "silky_cloth");
        materialRecipe(finishedRecipeConsumer, TinkersArcheryMaterialIds.slime, Ingredient.of(Tags.Items.SLIMEBALLS), 1, 1, materialFolder + "slime");

        materialRecipe(withCondition(finishedRecipeConsumer, new NotCondition(new TagEmptyCondition("forge", "wires/steel"))), TinkersArcheryMaterialIds.steel_wire, Ingredient.of(getTag("forge", "wires/steel")), 1, 1, materialFolder + "steel_wire");

        materialRecipe(finishedRecipeConsumer, TinkersArcheryMaterialIds.feather, Ingredient.of(Tags.Items.FEATHERS), 1, 1, materialFolder + "feather");
        materialRecipe(finishedRecipeConsumer, TinkersArcheryMaterialIds.leaf, new IngredientWithout(Ingredient.of(ItemTags.LEAVES), Ingredient.of(TinkerTags.Items.SLIMY_LEAVES)), 1, 1, materialFolder + "leaf");
        materialRecipe(finishedRecipeConsumer, TinkersArcheryMaterialIds.slime_leaf, Ingredient.of(TinkerTags.Items.SLIMY_LEAVES), 1, 1, materialFolder + "slime_leaf");
        materialRecipe(finishedRecipeConsumer, TinkersArcheryMaterialIds.paper, Ingredient.of(Items.PAPER), 1, 1, materialFolder + "paper");

        materialComposite(finishedRecipeConsumer, MaterialIds.string, TinkersArcheryMaterialIds.blazing_string, TinkerFluids.blazingBlood, FluidAttributes.BUCKET_VOLUME / 10, false, materialFolder);

        // Part Recipes
        partRecipes(finishedRecipeConsumer, TinkersArchery.bowshaft, TinkersArchery.bowshaft_cast, 2, partFolder, castFolder);
        partBuilderCompositeRecipe(finishedRecipeConsumer, TinkersArchery.bowstring, 1, partFolder);
        partRecipes(finishedRecipeConsumer, TinkersArchery.bowguide, TinkersArchery.bowguide_cast, 1, partFolder, castFolder);
        partRecipes(finishedRecipeConsumer, TinkersArchery.large_bowshaft, TinkersArchery.large_bowshaft_cast, 4, partFolder, castFolder);
        partRecipes(finishedRecipeConsumer, TinkersArchery.crossbow_arm, TinkersArchery.crossbow_arm_cast, 2, partFolder, castFolder);
        partRecipes(finishedRecipeConsumer, TinkersArchery.arrowhead, TinkersArchery.arrowhead_cast, 2, partFolder, castFolder);
        partRecipes(finishedRecipeConsumer, TinkersArchery.arrow_shaft, TinkersArchery.arrow_shaft_cast, 1, partFolder, castFolder);
        partBuilderRecipe(finishedRecipeConsumer, TinkersArchery.arrow_fletching, 1, partFolder);

        // Tool Recipes
        toolBuilding(finishedRecipeConsumer, TinkersArchery.shortbow, toolFolder);
        toolBuilding(finishedRecipeConsumer, TinkersArchery.crossbow, toolFolder);
        toolBuilding(finishedRecipeConsumer, TinkersArchery.longbow, toolFolder);
        toolBuilding(finishedRecipeConsumer, TinkersArchery.arrow, toolFolder);

        // Modifier Recipes
        ModifierRecipeBuilder.modifier(TinkersArchery.MULTISHOT_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE)
                .addInput(getTag("forge", "gems/emerald"))
                .addInput(getTag("forge", "ingots/galaxy_alloy"))
                .addInput(getTag("forge", "gems/emerald"))
                .addInput(Items.SPECTRAL_ARROW)
                .addInput(Items.SPECTRAL_ARROW)
                .addSalvage(Items.EMERALD, 0.7f)
                .addSalvage(Items.SPECTRAL_ARROW, 0.5f)
                .addSalvage(TinkersArchery.galaxy_alloy_ingot.get(), 0.3f)
                .setSlots(SlotType.ABILITY, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.MULTISHOT_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, prefix(TinkersArchery.MULTISHOT_MODIFIER, abilityFolder));

        ModifierRecipeBuilder.modifier(TinkersArchery.AUTOAIM_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE)
                .addInput(Items.ENDER_EYE)
                .addInput(TinkersArcheryTags.TinkersArcheryItemTags.TANTALUM_INGOT)
                .addInput(Items.ENDER_EYE)
                .addInput(Items.COMPARATOR)
                .addInput(Items.COMPARATOR)
                .addSalvage(Items.COMPARATOR, 0.7f)
                .addSalvage(Items.ENDER_EYE, 0.5f)
                .addSalvage(TinkersArchery.tantalum_ingot.get(), 0.3f)
                .setMaxLevel(1)
                .setSlots(SlotType.ABILITY, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.AUTOAIM_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, prefix(TinkersArchery.AUTOAIM_MODIFIER, abilityFolder));

        ModifierRecipeBuilder.modifier(TinkersArchery.PIERCING_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE)
                .addInput(getTag("forge", "gems/quartz"))
                .addInput(TinkerMaterials.blazingBone)
                .addInput(getTag("forge", "gems/quartz"))
                .addInput(Items.FLINT)
                .addInput(Items.FLINT)
                .addSalvage(Items.FLINT, 0.7f)
                .addSalvage(Items.QUARTZ, 0.5f)
                .addSalvage(TinkerMaterials.blazingBone.get(), 0.3f)
                .setSlots(SlotType.ABILITY, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.PIERCING_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, prefix(TinkersArchery.PIERCING_MODIFIER, abilityFolder));

        ModifierRecipeBuilder.modifier(TinkersArchery.EXPLOSIVE_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE)
                .addInput(Items.GUNPOWDER)
                .addInput(Items.TNT)
                .addInput(Items.GUNPOWDER)
                .addInput(Items.GUNPOWDER)
                .addInput(Items.GUNPOWDER)
                .addSalvage(Items.GUNPOWDER, 0.7f)
                .addSalvage(Items.TNT, 0.3f)
                .setSlots(SlotType.ABILITY, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.EXPLOSIVE_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, prefix(TinkersArchery.EXPLOSIVE_MODIFIER, abilityFolder));

        IncrementalModifierRecipeBuilder.modifier(TinkersArchery.HASTE_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE)
                .setInput(Tags.Items.DUSTS_REDSTONE, 1, 45)
                .setSalvage(Items.REDSTONE, false)
                .setMaxLevel(5)
                .setSlots(SlotType.UPGRADE, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.HASTE_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, wrap(TinkersArchery.HASTE_MODIFIER, upgradeFolder, "_from_dust"));
        IncrementalModifierRecipeBuilder.modifier(TinkersArchery.HASTE_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE)
                .setInput(Tags.Items.STORAGE_BLOCKS_REDSTONE, 9, 45)
                .setLeftover(new ItemStack(Items.REDSTONE))
                .setMaxLevel(5)
                .setSlots(SlotType.UPGRADE, 1)
                .build(finishedRecipeConsumer, wrap(TinkersArchery.HASTE_MODIFIER, upgradeFolder, "_from_block"));

        IncrementalModifierRecipeBuilder.modifier(TinkersArchery.POWER_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE)
                .setInput(Tags.Items.GEMS_QUARTZ, 1, 36)
                .setSalvage(Items.QUARTZ, false)
                .setMaxLevel(5)
                .setSlots(SlotType.UPGRADE, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.POWER_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, wrap(TinkersArchery.POWER_MODIFIER, upgradeFolder, "_from_shard"));
        IncrementalModifierRecipeBuilder.modifier(TinkersArchery.POWER_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE)
                .setInput(Tags.Items.STORAGE_BLOCKS_QUARTZ, 4, 36)
                .setLeftover(new ItemStack(Items.QUARTZ))
                .setMaxLevel(5)
                .setSlots(SlotType.UPGRADE, 1)
                .build(finishedRecipeConsumer, wrap(TinkersArchery.POWER_MODIFIER, upgradeFolder, "_from_block"));

        IncrementalModifierRecipeBuilder.modifier(TinkersArchery.LAUNCHING_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE)
                .setInput(Items.BAMBOO, 1, 64)
                .setSalvage(Items.BAMBOO, false)
                .setMaxLevel(5)
                .setSlots(SlotType.UPGRADE, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.LAUNCHING_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, prefix(TinkersArchery.LAUNCHING_MODIFIER, upgradeFolder));

        IncrementalModifierRecipeBuilder.modifier(TinkersArchery.PINPOINTER_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE)
                .setInput(Items.TRIPWIRE_HOOK, 1, 8)
                .setSalvage(Items.TRIPWIRE_HOOK, false)
                .setMaxLevel(5)
                .setSlots(SlotType.UPGRADE, 1)
                .buildSalvage(finishedRecipeConsumer, wrap(TinkersArchery.PINPOINTER_MODIFIER, salvageFolder,"_bow"))
                .build(finishedRecipeConsumer, wrap(TinkersArchery.PINPOINTER_MODIFIER, upgradeFolder, "_bow"));

        ModifierRecipeBuilder.modifier(TinkersArchery.BURST_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE)
                .addInput(Items.GHAST_TEAR)
                .addSalvage(Items.GHAST_TEAR, 0.5f)
                .setSlots(SlotType.UPGRADE, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.BURST_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, prefix(TinkersArchery.BURST_MODIFIER, upgradeFolder));

        IncrementalModifierRecipeBuilder.modifier(TinkersArchery.HIGHLANDER_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE)
                .setInput(Items.FEATHER, 1, 12)
                .setSalvage(Items.FEATHER, false)
                .setMaxLevel(5)
                .setSlots(SlotType.UPGRADE, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.HIGHLANDER_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, prefix(TinkersArchery.HIGHLANDER_MODIFIER, upgradeFolder));

        IncrementalModifierRecipeBuilder.modifier(TinkersArchery.VELOCITY_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE)
                .setInput(Items.FEATHER, 1, 12)
                .setSalvage(Items.FEATHER, false)
                .setMaxLevel(5)
                .setSlots(SlotType.UPGRADE, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.VELOCITY_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, prefix(TinkersArchery.VELOCITY_MODIFIER, upgradeFolder));

        IncrementalModifierRecipeBuilder.modifier(TinkersArchery.HEAVY_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE)
                .setInput(Items.CLAY_BALL, 1, 36)
                .setSalvage(Items.CLAY_BALL, false)
                .setMaxLevel(5)
                .setSlots(SlotType.UPGRADE, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.HEAVY_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, wrap(TinkersArchery.HEAVY_MODIFIER, upgradeFolder, "_from_ball"));
        IncrementalModifierRecipeBuilder.modifier(TinkersArchery.HEAVY_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE)
                .setInput(Items.CLAY, 4, 36)
                .setLeftover(new ItemStack(Items.CLAY_BALL))
                .setMaxLevel(5)
                .setSlots(SlotType.UPGRADE, 1)
                .build(finishedRecipeConsumer, wrap(TinkersArchery.HEAVY_MODIFIER, upgradeFolder, "_from_block"));

        IncrementalModifierRecipeBuilder.modifier(TinkersArchery.AQUADYNAMIC_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE)
                .setInput(Items.PRISMARINE_SHARD, 1, 36)
                .setSalvage(Items.PRISMARINE_SHARD, false)
                .setMaxLevel(5)
                .setSlots(SlotType.UPGRADE, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.AQUADYNAMIC_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, wrap(TinkersArchery.AQUADYNAMIC_MODIFIER, upgradeFolder, "_from_shard"));
        IncrementalModifierRecipeBuilder.modifier(TinkersArchery.AQUADYNAMIC_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE)
                .setInput(Items.PRISMARINE, 4, 36)
                .setLeftover(new ItemStack(Items.PRISMARINE_SHARD))
                .setMaxLevel(5)
                .setSlots(SlotType.UPGRADE, 1)
                .build(finishedRecipeConsumer, wrap(TinkersArchery.AQUADYNAMIC_MODIFIER, upgradeFolder, "_from_block"));
        IncrementalModifierRecipeBuilder.modifier(TinkersArchery.AQUADYNAMIC_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE)
                .setInput(Items.PRISMARINE_BRICKS, 9, 36)
                .setLeftover(new ItemStack(Items.PRISMARINE_SHARD))
                .setMaxLevel(5)
                .setSlots(SlotType.UPGRADE, 1)
                .build(finishedRecipeConsumer, wrap(TinkersArchery.AQUADYNAMIC_MODIFIER, upgradeFolder, "_from_bricks"));

        IncrementalModifierRecipeBuilder.modifier(TinkersArchery.PINPOINTER_ARROW_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE)
                .setInput(Items.OBSIDIAN, 2, 8)
                .setSalvage(Items.OBSIDIAN, false)
                .setMaxLevel(5)
                .setSlots(SlotType.UPGRADE, 1)
                .buildSalvage(finishedRecipeConsumer, wrap(TinkersArchery.PINPOINTER_MODIFIER, salvageFolder, "_arrow"))
                .build(finishedRecipeConsumer, wrap(TinkersArchery.PINPOINTER_MODIFIER, upgradeFolder, "_arrow"));

        ModifierRecipeBuilder.modifier(TinkersArchery.FLAME_FLARE_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE)
                .addInput(Items.TORCH)
                .setMaxLevel(1)
                .build(finishedRecipeConsumer, wrap(TinkersArchery.FLAME_FLARE_MODIFIER, slotlessFolder, "_level_1"));
        ModifierRecipeBuilder.modifier(TinkersArchery.FLAME_FLARE_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE)
                .addInput(Items.SOUL_TORCH)
                .setRequirements(ModifierMatch.entry(TinkersArchery.FLAME_FLARE_MODIFIER.get()))
                .setMaxLevel(2)
                .build(finishedRecipeConsumer, wrap(TinkersArchery.FLAME_FLARE_MODIFIER, slotlessFolder, "_level_2"));

        ModifierRecipeBuilder.modifier(TinkersArchery.SHULKER_FLARE_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE)
                .addInput(Items.END_ROD)
                .setMaxLevel(1)
                .build(finishedRecipeConsumer, prefix(TinkersArchery.SHULKER_FLARE_MODIFIER, slotlessFolder));
    }

    private void compressionRecipes(Consumer<IFinishedRecipe> consumer, Item block, Item ingot, Item nugget, String name) {
        ConditionalRecipe.builder().addCondition(this.TRUE())
                .addRecipe( ShapedRecipeBuilder.shaped(block, 1)
                        .pattern("XXX")
                        .pattern("XYX")
                        .pattern("XXX")
                        .define('X', ItemTags.bind("forge:ingots/" + name))
                        .define('Y', ingot)
                        .group("")
                        .unlockedBy("has_ingot", has(ingot))
                        ::save )
                .generateAdvancement()
                .build(consumer, new ResourceLocation(TinkersArchery.MOD_ID, ingot.getRegistryName().getPath() + "_to_block"));

        ConditionalRecipe.builder().addCondition(this.TRUE())
                .addRecipe( ShapedRecipeBuilder.shaped(ingot, 1)
                        .pattern("XXX")
                        .pattern("XYX")
                        .pattern("XXX")
                        .define('X', ItemTags.bind("forge:nuggets/" + name))
                        .define('Y', nugget)
                        .group("")
                        .unlockedBy("has_nugget", has(nugget))
                        ::save )
                .generateAdvancement()
                .build(consumer, new ResourceLocation(TinkersArchery.MOD_ID, nugget.getRegistryName().getPath() + "_to_ingot"));

        ConditionalRecipe.builder().addCondition(this.TRUE())
                .addRecipe( ShapelessRecipeBuilder.shapeless(ingot, 9)
                        .requires(block)
                        .group("")
                        .unlockedBy("has_block", has(block))
                        ::save )
                .generateAdvancement()
                .build(consumer, new ResourceLocation(TinkersArchery.MOD_ID, block.getRegistryName().getPath() + "_to_ingot"));

        ConditionalRecipe.builder().addCondition(this.TRUE())
                .addRecipe( ShapelessRecipeBuilder.shapeless(nugget, 9)
                        .requires(ingot)
                        .group("")
                        .unlockedBy("has_ingot", has(ingot))
                        ::save )
                .generateAdvancement()
                .build(consumer, new ResourceLocation(TinkersArchery.MOD_ID, ingot.getRegistryName().getPath() + "_to_nugget"));

    }

}
