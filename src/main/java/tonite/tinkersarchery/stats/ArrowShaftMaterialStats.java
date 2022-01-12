package tonite.tinkersarchery.stats;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.stats.BaseMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import tonite.tinkersarchery.TinkersArchery;

import java.util.ArrayList;
import java.util.List;

public class ArrowShaftMaterialStats extends BaseMaterialStats {

    public static final MaterialStatsId ID = new MaterialStatsId(TinkersArchery.getResource("arrow_shaft"));
    public static final ArrowShaftMaterialStats DEFAULT = new ArrowShaftMaterialStats();

    // tooltip prefixes
    private static final String COUNT_PREFIX = makeTooltipKey(TinkersArchery.getResource("count"));
    private static final String ACCURACY_PREFIX = makeTooltipKey(TinkersArchery.getResource("accuracy"));
    private static final String WEIGHT_PREFIX = makeTooltipKey(TinkersArchery.getResource("weight"));
    private static final String STABILITY_PREFIX = makeTooltipKey(TinkersArchery.getResource("stability"));

    // tooltip descriptions
    private static final ITextComponent DURABILITY_DESCRIPTION = makeTooltip(TinkersArchery.getResource("count.multiplier_description"));
    private static final ITextComponent ATTACK_DAMAGE_DESCRIPTION = makeTooltip(TConstruct.getResource("handle.attack_damage.description"));
    private static final ITextComponent ATTACK_SPEED_DESCRIPTION = makeTooltip(TConstruct.getResource("handle.attack_speed.description"));
    private static final ITextComponent MINING_SPEED_DESCRIPTION = makeTooltip(TConstruct.getResource("handle.mining_speed.description"));
    private static final ITextComponent ACCURACY_DESCRIPTION = makeTooltip(TinkersArchery.getResource("accuracy.multiplier_description"));
    private static final ITextComponent WEIGHT_DESCRIPTION = makeTooltip(TinkersArchery.getResource("weight.multiplier_description"));
    private static final ITextComponent STABILITY_DESCRIPTION = makeTooltip(TinkersArchery.getResource("stability.multiplier_description"));
    private static final List<ITextComponent> DESCRIPTION = ImmutableList.of(DURABILITY_DESCRIPTION, ATTACK_DAMAGE_DESCRIPTION, /*ATTACK_SPEED_DESCRIPTION, MINING_SPEED_DESCRIPTION,*/ ACCURACY_DESCRIPTION, WEIGHT_DESCRIPTION, STABILITY_DESCRIPTION);

    private float count;
    private float miningSpeed;
    private float attackSpeed;
    private float attackDamage;
    private float weight;
    private float stability;
    private float accuracy;

    public ArrowShaftMaterialStats(float count, float miningSpeed, float attackSpeed, float attackDamage, float weight, float stability, float accuracy){
        this.count = count;
        this.miningSpeed = miningSpeed;
        this.attackSpeed = attackSpeed;
        this.attackDamage = attackDamage;
        this.weight = weight;
        this.stability = stability;
        this.accuracy = accuracy;
    }

    public ArrowShaftMaterialStats(float count, float miningSpeed, float attackSpeed, float attackDamage, float weight, float accuracy){
        this(count, miningSpeed, attackSpeed, attackDamage, weight, weight, accuracy);
    }

    public ArrowShaftMaterialStats(){
        this (1f, 1f, 1, 1f, 1f, 1f);
    }

    @Override
    public MaterialStatsId getIdentifier() {
        return ID;
    }

    @Override
    public List<ITextComponent> getLocalizedInfo() {
        List<ITextComponent> info = new ArrayList<>();

        info.add(format(COUNT_PREFIX, this.count));
        info.add(HandleMaterialStats.formatAttackDamage(this.attackDamage));
        info.add(format(ACCURACY_PREFIX, this.accuracy));
        info.add(format(WEIGHT_PREFIX, this.weight));
        info.add(format(STABILITY_PREFIX, this.stability));

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

        buffer.writeFloat(this.count);
        buffer.writeFloat(this.attackDamage);
        buffer.writeFloat(this.attackSpeed);
        buffer.writeFloat(this.miningSpeed);
        buffer.writeFloat(this.weight);
        buffer.writeFloat(this.stability);
        buffer.writeFloat(this.accuracy);

    }

    @Override
    public void decode(PacketBuffer buffer) {

        this.count = buffer.readFloat();
        this.attackDamage = buffer.readFloat();
        this.attackSpeed = buffer.readFloat();
        this.miningSpeed = buffer.readFloat();
        this.weight = buffer.readFloat();
        this.stability = buffer.readFloat();
        this.accuracy = buffer.readFloat();
    }

    public float getCount() { return count; }

    public float getMiningSpeed() {
        return this.miningSpeed;
    }

    public float getAttackSpeed() {
        return this.attackSpeed;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    public float getWeight() {return weight; }

    public float getStability() {return stability; }

    public float getAccuracy() {
        return accuracy;
    }

}
