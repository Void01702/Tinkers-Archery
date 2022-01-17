package tonite.tinkersarchery.data.server;

import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import tonite.tinkersarchery.TinkersArchery;

import java.util.Objects;
import java.util.stream.Collectors;


public class TinkersArcheryBlockLootTables extends BlockLootTables {

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream()
                .filter((block) -> TinkersArchery.MOD_ID.equals(Objects.requireNonNull(block.getRegistryName()).getNamespace()))
                .collect(Collectors.toList());
    }

    @Override
    protected void addTables() {
        dropSelf(TinkersArchery.tantalum_ore.get());

        dropSelf(TinkersArchery.tantalum_block.get());
        dropSelf(TinkersArchery.tungstantalum_block.get());
        dropSelf(TinkersArchery.raw_luxtum_block.get());
        dropSelf(TinkersArchery.luxtum_block.get());
        dropSelf(TinkersArchery.cobalt_tantalum_block.get());
        dropSelf(TinkersArchery.galaxy_alloy_block.get());
    }
}
