package tonite.tinkersarchery.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import slimeknights.tconstruct.library.materials.stats.BaseMaterialStats;
import slimeknights.tconstruct.library.materials.stats.IRepairableMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import tonite.tinkersarchery.TinkersArchery;

import java.util.List;

public class BowMaterialStats extends BaseMaterialStats implements IRepairableMaterialStats {
    public static final MaterialStatsId ID = new MaterialStatsId(TinkersArchery.getResource("bow"));
    public static final BowMaterialStats DEFAULT = new BowMaterialStats(1, 1f, 1f);

    private static final List<ITextComponent> DESCRIPTION = ImmutableList.of(ToolStats.DURABILITY.getDescription(), BowAndArrowToolStats.ELASTICITY.getDescription(), BowAndArrowToolStats.DRAW_SPEED.getDescription());

    private int durability;
    private float elasticity;
    private float drawSpeed;

    public BowMaterialStats(int durability, float elasticity, float drawSpeed){
        this.durability = durability;
        this.elasticity = elasticity;
        this.drawSpeed = drawSpeed;
    }

    public BowMaterialStats(){
        this (1,  0, 0);
    }

    @Override
    public MaterialStatsId getIdentifier() {
        return ID;
    }

    @Override
    public List<ITextComponent> getLocalizedInfo() {
        List<ITextComponent> info = Lists.newArrayList();

        info.add(ToolStats.DURABILITY.formatValue(this.durability));
        info.add(BowAndArrowToolStats.ELASTICITY.formatValue(this.elasticity));
        info.add(BowAndArrowToolStats.DRAW_SPEED.formatValue(this.drawSpeed));

        return info;
    }

    @Override
    public List<ITextComponent> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    @Override
    public void encode(PacketBuffer buffer) {

        buffer.writeInt(this.durability);
        buffer.writeFloat(this.elasticity);
        buffer.writeFloat(this.drawSpeed);

    }

    @Override
    public void decode(PacketBuffer buffer) {

        this.durability = buffer.readInt();
        this.elasticity = buffer.readFloat();
        this.drawSpeed = buffer.readFloat();
    }

    @Override
    public int getDurability() {
        return durability;
    }

    public float getElasticity() {
        return elasticity;
    }

    public float getDrawSpeed() {
        return drawSpeed;
    }
}
