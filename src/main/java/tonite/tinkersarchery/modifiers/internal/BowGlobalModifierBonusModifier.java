package tonite.tinkersarchery.modifiers.internal;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.context.ToolRebuildContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.tools.TinkerModifiers;
import tonite.tinkersarchery.data.server.TinkersArcheryTags;
import tonite.tinkersarchery.stats.BowAndArrowToolStats;

public class BowGlobalModifierBonusModifier extends Modifier {

    public BowGlobalModifierBonusModifier() {
        super(0);
    }

    @Override
    public boolean shouldDisplay(boolean advanced) {
        return false;
    }

    @Override
    public void addToolStats(ToolRebuildContext context, int level, ModifierStatsBuilder builder) {
        if (context.getItem().is(TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_SHOOTABLE)) {
            int diamond = context.getModifierLevel(TinkerModifiers.diamond.get());
            if (diamond > 0) {
                BowAndArrowToolStats.ELASTICITY.add(builder, diamond * 0.5f);
            }

            int emerald = context.getModifierLevel(TinkerModifiers.emerald.get());
            if (emerald > 0) {
                BowAndArrowToolStats.ACCURACY.add(builder, emerald * 0.25f);
                BowAndArrowToolStats.ELASTICITY.add(builder, emerald * 0.25f);
            }

            int netherite = context.getModifierLevel(TinkerModifiers.netherite.get());
            if (netherite > 0) {
                BowAndArrowToolStats.ELASTICITY.add(builder, netherite * 0.25f);
            }
        }
    }
}
