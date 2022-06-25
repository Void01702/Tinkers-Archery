package tonite.tinkersarchery.stats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import slimeknights.tconstruct.library.materials.stats.BaseMaterialStats;
import slimeknights.tconstruct.library.materials.stats.IRepairableMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;
import tonite.tinkersarchery.TinkersArchery;

import java.util.List;

public class ArrowHeadMaterialStats extends BaseMaterialStats implements IRepairableMaterialStats {

    public static final MaterialStatsId ID = new MaterialStatsId(TinkersArchery.getResource("arrowhead"));
    public static final ArrowHeadMaterialStats DEFAULT = new ArrowHeadMaterialStats();

    private static final List<ITextComponent> DESCRIPTION = ImmutableList.of(BowAndArrowToolStats.COUNT.getDescription(), /*ToolStats.HARVEST_LEVEL.getDescription(), ToolStats.MINING_SPEED.getDescription(),*/ ToolStats.ATTACK_DAMAGE.getDescription(), BowAndArrowToolStats.ACCURACY.getDescription(), BowAndArrowToolStats.WEIGHT.getDescription(), BowAndArrowToolStats.STABILITY.getDescription());

    private int count;
    private float miningSpeed;
    private int harvestLevel;
    private float attack;
    private float weight;
    private float stability;
    private float accuracy;

    public ArrowHeadMaterialStats(int count, float miningSpeed, int harvestLevel, float attack, float weight, float stability, float accuracy){
        this.count = count;
        this.miningSpeed = miningSpeed;
        this.harvestLevel = harvestLevel;
        this.attack = attack;
        this.weight = weight;
        this.stability = stability;
        this.accuracy = accuracy;
    }

    public ArrowHeadMaterialStats(int count, float miningSpeed, int harvestLevel, float attack, float weight, float accuracy){
        this(count, miningSpeed, harvestLevel, attack, weight, weight, accuracy);
    }

    public ArrowHeadMaterialStats(){
        this (1, 1f, 0, 1f, 1f, 1f);
    }

    @Override
    public MaterialStatsId getIdentifier() {
        return ID;
    }

    @Override
    public List<ITextComponent> getLocalizedInfo() {
        List<ITextComponent> info = Lists.newArrayList();

        info.add(BowAndArrowToolStats.COUNT.formatValue(this.count));
        info.add(ToolStats.ATTACK_DAMAGE.formatValue(this.attack));
        info.add(BowAndArrowToolStats.ACCURACY.formatValue(this.accuracy));
        info.add(BowAndArrowToolStats.WEIGHT.formatValue(this.weight));
        info.add(BowAndArrowToolStats.STABILITY.formatValue(this.stability));

        return info;
    }

    @Override
    public List<ITextComponent> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    @Override
    public void encode(PacketBuffer buffer) {

        buffer.writeInt(this.count);
        buffer.writeFloat(this.miningSpeed);
        buffer.writeInt(this.harvestLevel);
        buffer.writeFloat(this.attack);
        buffer.writeFloat(this.weight);
        buffer.writeFloat(this.stability);
        buffer.writeFloat(this.accuracy);

    }

    @Override
    public void decode(PacketBuffer buffer) {

        this.count = buffer.readInt();
        this.miningSpeed = buffer.readFloat();
        this.harvestLevel = buffer.readInt();
        this.attack = buffer.readFloat();
        this.weight = buffer.readFloat();
        this.stability = buffer.readFloat();
        this.accuracy = buffer.readFloat();
    }

    public int getCount() {
        return this.count;
    }

    public float getMiningSpeed() {
        return this.miningSpeed;
    }

    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    public float getAttack() {
        return this.attack;
    }

    public float getWeight() {return weight; }

    public float getStability() {return stability; }

    public float getAccuracy() {
        return accuracy;
    }

    @Override
    public int getDurability() {
        return count;
    }

    static ITextComponent formatInvertedColoredMultiplier(String loc, float number) {
        // 0.5 is red, 1.0 should be roughly green, 1.5 is blue
        float hue = MathHelper.positiveModulo(1 / number - 0.5f, 2f);
        return new TranslationTextComponent(loc).append((ITextComponent)new StringTextComponent(Util.MULTIPLIER_FORMAT.format(number)).withStyle(style -> style.withColor(Color.fromRgb(MathHelper.hsvToRgb(hue / 1.5f, 1.0f, 0.75f)))));
    }
}
