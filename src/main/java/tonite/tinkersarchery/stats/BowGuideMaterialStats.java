package tonite.tinkersarchery.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.stats.BaseMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import tonite.tinkersarchery.TinkersArchery;

import java.util.List;

public class BowGuideMaterialStats  extends BaseMaterialStats {
    public static final MaterialStatsId ID = new MaterialStatsId(TinkersArchery.getResource("bowguide"));
    public static final BowGuideMaterialStats DEFAULT = new BowGuideMaterialStats(1f, 1f);

    // tooltip prefixes
    private static final String ELASTICITY_PREFIX = makeTooltipKey(TinkersArchery.getResource("elasticity"));
    private static final String ACCURACY_PREFIX = makeTooltipKey(TinkersArchery.getResource("accuracy"));

    // tooltip descriptions
    private static final ITextComponent ELASTICITY_DESCRIPTION = makeTooltip(TinkersArchery.getResource("elasticity.multiplier_description"));
    private static final ITextComponent ACCURACY_DESCRIPTION = makeTooltip(TinkersArchery.getResource("accuracy.multiplier_description"));
    private static final List<ITextComponent> DESCRIPTION = ImmutableList.of(ELASTICITY_DESCRIPTION, ACCURACY_DESCRIPTION);

    private float elasticity;
    private float accuracy;

    public BowGuideMaterialStats(float elasticity, float accuracy){
        this.elasticity = elasticity;
        this.accuracy = accuracy;
    }

    public BowGuideMaterialStats(){
        this (1f, 1f);
    }

    @Override
    public MaterialStatsId getIdentifier() {
        return ID;
    }

    @Override
    public List<ITextComponent> getLocalizedInfo() {
        List<ITextComponent> info = Lists.newArrayList();

        info.add(format(ELASTICITY_PREFIX, this.elasticity));
        info.add(format(ACCURACY_PREFIX, this.accuracy));

        return info;
    }

    public static ITextComponent format(String prefix, float quality) {
        return IToolStat.formatColoredMultiplier(prefix, quality);
    }

    @Override
    public List<ITextComponent> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    @Override
    public void encode(PacketBuffer buffer) {

        buffer.writeFloat(this.elasticity);
        buffer.writeFloat(this.accuracy);

    }

    @Override
    public void decode(PacketBuffer buffer) {

        this.elasticity = buffer.readFloat();
        this.accuracy = buffer.readFloat();
    }

    public float getElasticity() {
        return elasticity;
    }

    public float getAccuracy() {
        return accuracy;
    }
}
