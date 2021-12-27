package tonite.tinkersarchery.data.server;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.data.recipe.ICommonRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IncrementalModifierRecipeBuilder;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipeBuilder;
import slimeknights.tconstruct.library.recipe.partbuilder.PartRecipeBuilder;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerModifiers;
import tonite.tinkersarchery.TinkersArchery;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TinkersArcheryRecipes extends RecipeProvider implements IConditionBuilder, IMaterialRecipeHelper, IToolRecipeHelper, ISmelteryRecipeHelper, ICommonRecipeHelper {

    private static String toolFolder = "tools/building/";
    private static String partFolder = "tools/parts/";
    private static String castFolder = "tools/casts/";
    private static String modifierFolder = "tools/modifiers/";
    private static String abilityFolder = modifierFolder + "ability/";
    private static String upgradeFolder = modifierFolder + "upgrade/";
    private static String salvageFolder = modifierFolder + "salvage/";

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

    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> finishedRecipeConsumer) {

        // Part Recipes
        partRecipes(finishedRecipeConsumer, TinkersArchery.bowshaft, TinkersArchery.bowshaft_cast, 2, partFolder, castFolder);
        partBuilderRecipe(finishedRecipeConsumer, TinkersArchery.bowstring, 1, partFolder);
        partRecipes(finishedRecipeConsumer, TinkersArchery.bowguide, TinkersArchery.bowguide_cast, 1, partFolder, castFolder);
        partRecipes(finishedRecipeConsumer, TinkersArchery.large_bowshaft, TinkersArchery.large_bowshaft_cast, 1, partFolder, castFolder);
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
                .addInput(Items.SPECTRAL_ARROW)
                .addInput(getTag("forge", "gems/emerald"))
                .addInput(getTag("forge", "ingots/galaxy_alloy"))
                .addInput(getTag("forge", "gems/emerald"))
                .addInput(Items.SPECTRAL_ARROW)
                .addSalvage(Items.EMERALD, 0.7f)
                .addSalvage(Items.SPECTRAL_ARROW, 0.5f)
                .addSalvage(TinkersArchery.galaxy_alloy_ingot.get(), 0.3f)
                .setSlots(SlotType.ABILITY, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.MULTISHOT_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, prefix(TinkersArchery.MULTISHOT_MODIFIER, abilityFolder));

        ModifierRecipeBuilder.modifier(TinkersArchery.PIERCING_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE)
                .addInput(Items.FLINT)
                .addInput(getTag("forge", "gems/quartz"))
                .addInput(TinkerMaterials.blazingBone)
                .addInput(getTag("forge", "gems/quartz"))
                .addInput(Items.FLINT)
                .addSalvage(Items.FLINT, 0.7f)
                .addSalvage(Items.QUARTZ, 0.5f)
                .addSalvage(TinkerMaterials.blazingBone.get(), 0.3f)
                .setSlots(SlotType.ABILITY, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.PIERCING_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, prefix(TinkersArchery.PIERCING_MODIFIER, abilityFolder));

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
        IncrementalModifierRecipeBuilder.modifier(TinkersArchery.PINPOINTER_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE)
                .setInput(Items.OBSIDIAN, 1, 4)
                .setSalvage(Items.OBSIDIAN, false)
                .setMaxLevel(5)
                .setSlots(SlotType.UPGRADE, 1)
                .buildSalvage(finishedRecipeConsumer, wrap(TinkersArchery.PINPOINTER_MODIFIER, salvageFolder, "_arrow"))
                .build(finishedRecipeConsumer, wrap(TinkersArchery.PINPOINTER_MODIFIER, upgradeFolder, "_arrow"));

        ModifierRecipeBuilder.modifier(TinkersArchery.BURST_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE)
                .addInput(Items.GHAST_TEAR)
                .addSalvage(Items.GHAST_TEAR, 0.5f)
                .setSlots(SlotType.UPGRADE, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.BURST_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, prefix(TinkersArchery.BURST_MODIFIER, upgradeFolder));

        ModifierRecipeBuilder.modifier(TinkersArchery.HIGHLANDER_MODIFIER.get())
                .setTools(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE)
                .addInput(Items.FEATHER)
                .addSalvage(Items.FEATHER, 0.5f)
                .setSlots(SlotType.UPGRADE, 1)
                .buildSalvage(finishedRecipeConsumer, prefix(TinkersArchery.HIGHLANDER_MODIFIER, salvageFolder))
                .build(finishedRecipeConsumer, prefix(TinkersArchery.HIGHLANDER_MODIFIER, upgradeFolder));
    }
}
