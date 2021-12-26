package tonite.tinkersarchery.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import slimeknights.tconstruct.library.materials.stats.BaseMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import tonite.tinkersarchery.TinkersArchery;

import java.util.List;

public class BowStringMaterialStats extends BaseMaterialStats {
    public static final MaterialStatsId ID = new MaterialStatsId(TinkersArchery.getResource("bowstring"));
    public static final BowStringMaterialStats DEFAULT = new BowStringMaterialStats(1f, 1f, 1f, 1f);

    // tooltip prefixes
    private static final String ELASTICITY_PREFIX = makeTooltipKey(TinkersArchery.getResource("elasticity"));
    private static final String DRAW_SPEED_PREFIX = makeTooltipKey(TinkersArchery.getResource("draw_speed"));

    private static final List<ITextComponent> DESCRIPTION = ImmutableList.of(ToolStats.DURABILITY.getDescription(), BowAndArrowToolStats.ELASTICITY.getDescription(), BowAndArrowToolStats.DRAW_SPEED.getDescription(), BowAndArrowToolStats.ACCURACY.getDescription());

    private float durability;
    private float elasticity;
    private float drawSpeed;
    private float accuracy;

    public BowStringMaterialStats(float durability, float elasticity, float drawSpeed, float accuracy){
        this.durability = durability;
        this.elasticity = elasticity;
        this.drawSpeed = drawSpeed;
        this.accuracy = accuracy;
    }

    public BowStringMaterialStats(){
        this (1f, 1f, 1f, 1f);
    }

    @Override
    public MaterialStatsId getIdentifier() {
        return ID;
    }

    @Override
    public List<ITextComponent> getLocalizedInfo() {
        List<ITextComponent> info = Lists.newArrayList();

        info.add(HandleMaterialStats.formatDurability(this.durability));
        info.add(format(ELASTICITY_PREFIX, this.elasticity));
        info.add(format(DRAW_SPEED_PREFIX, this.drawSpeed));
        info.add(BowAndArrowToolStats.ACCURACY.formatValue(this.accuracy));

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

        buffer.writeFloat(this.durability);
        buffer.writeFloat(this.elasticity);
        buffer.writeFloat(this.drawSpeed);
        buffer.writeFloat(this.accuracy);

    }

    @Override
    public void decode(PacketBuffer buffer) {

        this.durability = buffer.readFloat();
        this.elasticity = buffer.readFloat();
        this.drawSpeed = buffer.readFloat();
        this.accuracy = buffer.readFloat();
    }

    public float getDurability() {
        return durability;
    }

    public float getElasticity() {
        return elasticity;
    }

    public float getDrawSpeed() {
        return drawSpeed;
    }

    public float getAccuracy() {
        return accuracy;
    }
}
