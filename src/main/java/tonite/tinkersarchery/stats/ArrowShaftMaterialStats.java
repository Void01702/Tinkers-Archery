package tonite.tinkersarchery.stats;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import tonite.tinkersarchery.TinkersArchery;

import java.util.ArrayList;
import java.util.List;

public class ArrowShaftMaterialStats extends HandleMaterialStats {

    public static final MaterialStatsId ID = new MaterialStatsId(TinkersArchery.getResource("arrow_shaft"));
    public static final ArrowShaftMaterialStats DEFAULT = new ArrowShaftMaterialStats();

    // tooltip prefixes
    private static final String SPEED_PREFIX = makeTooltipKey(TinkersArchery.getResource("speed"));
    private static final String ACCURACY_PREFIX = makeTooltipKey(TinkersArchery.getResource("accuracy"));
    private static final String WEIGHT_PREFIX = makeTooltipKey(TinkersArchery.getResource("weight"));

    // tooltip descriptions
    private static final ITextComponent DURABILITY_DESCRIPTION = makeTooltip(TConstruct.getResource("handle.durability.description"));
    private static final ITextComponent ATTACK_DAMAGE_DESCRIPTION = makeTooltip(TConstruct.getResource("handle.attack_damage.description"));
    private static final ITextComponent ATTACK_SPEED_DESCRIPTION = makeTooltip(TConstruct.getResource("handle.attack_speed.description"));
    private static final ITextComponent MINING_SPEED_DESCRIPTION = makeTooltip(TConstruct.getResource("handle.mining_speed.description"));
    private static final ITextComponent SPEED_DESCRIPTION = makeTooltip(TinkersArchery.getResource("speed.multiplier_description"));
    private static final ITextComponent ACCURACY_DESCRIPTION = makeTooltip(TinkersArchery.getResource("accuracy.multiplier_description"));
    private static final ITextComponent WEIGHT_DESCRIPTION = makeTooltip(TinkersArchery.getResource("weight.multiplier_description"));
    private static final List<ITextComponent> DESCRIPTION = ImmutableList.of(DURABILITY_DESCRIPTION, ATTACK_DAMAGE_DESCRIPTION, ATTACK_SPEED_DESCRIPTION, /*MINING_SPEED_DESCRIPTION,*/ SPEED_DESCRIPTION, ACCURACY_DESCRIPTION, WEIGHT_DESCRIPTION);

    private float speed;
    private float weight;
    private float accuracy;

    public ArrowShaftMaterialStats(float durability, float miningSpeed, float attackSpeed, float attack, float speed, float weight, float accuracy){
        super(durability, miningSpeed, attackSpeed, attack);
        this.speed = speed;
        this.weight = weight;
        this.accuracy = accuracy;
    }

    public ArrowShaftMaterialStats(){
        this (1f, 1f, 0, 1f, 1f, 1f, 1f);
    }

    @Override
    public MaterialStatsId getIdentifier() {
        return ID;
    }

    @Override
    public List<ITextComponent> getLocalizedInfo() {
        List<ITextComponent> info = new ArrayList<>();

        info.add(formatDurability(this.getDurability()));
        info.add(formatAttackDamage(this.getAttackDamage()));
        info.add(formatAttackSpeed(this.getAttackSpeed()));

        info.add(format(SPEED_PREFIX, this.speed));
        info.add(format(ACCURACY_PREFIX, this.accuracy));
        info.add(format(WEIGHT_PREFIX, this.weight));

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
