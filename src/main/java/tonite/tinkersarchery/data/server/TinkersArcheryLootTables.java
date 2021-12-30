package tonite.tinkersarchery.data.server;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import tonite.tinkersarchery.TinkersArchery;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TinkersArcheryLootTables extends LootTableProvider {
    public TinkersArcheryLootTables(DataGenerator p_i50789_1_) {
        super(p_i50789_1_);
    }

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> lootTables = ImmutableList.of(Pair.of(TinkersArcheryBlockLootTables::new, LootParameterSets.BLOCK));

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return lootTables;
    }

    @Override
    protected void validate(Map<ResourceLocation,LootTable> map, ValidationTracker validationtracker) {
        map.forEach((loc, table) -> LootTableManager.validate(validationtracker, loc, table));
        // Remove vanilla's tables, which we also loaded so we can redirect stuff to them.
        // This ensures the remaining generator logic doesn't write those to files.
        map.keySet().removeIf((loc) -> !loc.getNamespace().equals(TinkersArchery.MOD_ID));
    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    @Override
    public String getName() {
        return "Tinkers Archery LootTables";
    }
}
