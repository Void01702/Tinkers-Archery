package tonite.tinkersarchery.stats;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;
import tonite.tinkersarchery.TinkersArchery;

import java.util.List;

public class ArrowHeadMaterialStats extends HeadMaterialStats {

    public static final MaterialStatsId ID = new MaterialStatsId(TinkersArchery.getResource("arrowhead"));
    public static final ArrowHeadMaterialStats DEFAULT = new ArrowHeadMaterialStats();

    private static final List<ITextComponent> DESCRIPTION = ImmutableList.of(ToolStats.DURABILITY.getDescription(), ToolStats.HARVEST_LEVEL.getDescription(), ToolStats.MINING_SPEED.getDescription(), ToolStats.ATTACK_DAMAGE.getDescription(), BowAndArrowToolStats.ELASTICITY.getDescription(), BowAndArrowToolStats.ACCURACY.getDescription());

    private float speed;
    private float weight;
    private float accuracy;

    public ArrowHeadMaterialStats(int durability, float miningSpeed, int harvestLevel, float attack, float speed, float weight, float accuracy){
        super(durability, miningSpeed, harvestLevel, attack);
        this.speed = speed;
        this.weight = weight;
        this.accuracy = accuracy;
    }

    public ArrowHeadMaterialStats(){
        this (1, 1f, 0, 1f, 1f, 1f, 1f);
    }

    @Override
    public MaterialStatsId getIdentifier() {
        return ID;
    }

    @Override
    public List<ITextComponent> getLocalizedInfo() {
        List<ITextComponent> info = super.getLocalizedInfo();

        info.add(BowAndArrowToolStats.ELASTICITY.formatValue(this.speed));
        info.add(BowAndArrowToolStats.ACCURACY.formatValue(this.accuracy));

        return info;
    }

    @Override
    public List<ITextComponent> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    @Override
    public void encode(PacketBuffer buffer) {

        super.encode(buffer);

        buffer.writeFloat(this.speed);
        buffer.writeFloat(this.weight);
        buffer.writeFloat(this.accuracy);

    }

    @Override
    public void decode(PacketBuffer buffer) {

        super.decode(buffer);

        this.speed = buffer.readFloat();
        this.weight = buffer.readFloat();
        this.accuracy = buffer.readFloat();
    }

    public float getSpeed() {
        return speed;
    }

    public float getWeight() {return weight; }

    public float getAccuracy() {
        return accuracy;
    }

}
