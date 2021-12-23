package tonite.tinkersarchery.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import slimeknights.tconstruct.library.materials.stats.BaseMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import tonite.tinkersarchery.TinkersArchery;

import java.util.List;

public class BowGuideMaterialStats  extends BaseMaterialStats {
    public static final MaterialStatsId ID = new MaterialStatsId(TinkersArchery.getResource("bowguide"));
    public static final BowGuideMaterialStats DEFAULT = new BowGuideMaterialStats(1f, 1f);

    private static final List<ITextComponent> DESCRIPTION = ImmutableList.of(BowAndArrowToolStats.ELASTICITY.getDescription(), BowAndArrowToolStats.ACCURACY.getDescription());

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

        info.add(BowAndArrowToolStats.ELASTICITY.formatValue(this.elasticity));
        info.add(BowAndArrowToolStats.ACCURACY.formatValue(this.accuracy));

        return info;
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
