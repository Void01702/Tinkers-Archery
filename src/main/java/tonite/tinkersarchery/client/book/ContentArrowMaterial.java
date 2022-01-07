package tonite.tinkersarchery.client.book;

import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ForgeI18n;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.element.TextData;
import slimeknights.mantle.client.screen.book.BookScreen;
import slimeknights.mantle.client.screen.book.element.BookElement;
import slimeknights.mantle.client.screen.book.element.ItemElement;
import slimeknights.mantle.client.screen.book.element.TextElement;
import slimeknights.tconstruct.library.book.content.ContentMaterial;
import slimeknights.tconstruct.library.book.elements.TinkerItemElement;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.definition.PartRequirement;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import tonite.tinkersarchery.data.server.TinkersArcheryTags;
import tonite.tinkersarchery.stats.*;

import java.util.ArrayList;
import java.util.List;

public class ContentArrowMaterial extends ContentMaterial {

    public static final String ID = "arrowmaterial";

    public ContentArrowMaterial(IMaterial material) {
        super(material, true);
    }

    @Override
    public void build(BookData book, ArrayList<BookElement> list, boolean rightSide) {
        IMaterial material = getMaterial();
        this.addTitle(list, getTitle().getString(), true, material.getColor().getValue());

        // the cool tools to the left/right
        this.addDisplayItems(list, rightSide ? BookScreen.PAGE_WIDTH - 18 : 0, material.getIdentifier());

        int col_margin = 22;
        int top = getTitleHeight();
        int left = rightSide ? 0 : col_margin;

        int y = top + 5;
        int x = left + 5;
        int w = BookScreen.PAGE_WIDTH / 2 - 5;

        // bowshaft stats
        int bowshaftTraits = this.addStatsDisplay(x, y, w, list, material, ArrowHeadMaterialStats.ID);
        // handle
        int bowguideTraits = this.addStatsDisplay(x + w, y, w - 10, list, material, ArrowShaftMaterialStats.ID);

        // extra
        y+= 75;
        //this.addStatsDisplay(x, y + 10 * headTraits, w, list, material, ExtraMaterialStats.ID);

        // inspirational quote
        MaterialId id = material.getIdentifier();

        String flavorKey = String.format("material.%s.%s.flavor", id.getNamespace(), id.getPath());
        if (I18n.exists(flavorKey)) {
            // using forge instead of I18n.format as that prevents % from being interpreted as a format key
            String translated = ForgeI18n.getPattern(flavorKey);
            translated = '"' + translated + '"';
            TextData flavourData = new TextData(translated);
            flavourData.italic = true;
            list.add(new TextElement(x + w, y + 10 * bowguideTraits, w - 22, 60, flavourData));
        }

        // boring description text
        String descriptionKey = String.format("material.%s.%s.arrow", id.getNamespace(), id.getPath());
        if (!I18n.exists(descriptionKey)) {
            descriptionKey = String.format("material.%s.%s.encyclopedia", id.getNamespace(), id.getPath());
        }

        if (I18n.exists(descriptionKey)) {
            // using forge instead of I18n.format as that prevents % from being interpreted as a format key
            String translated = ForgeI18n.getPattern(descriptionKey);
            TextData descriptionData = new TextData(translated);
            descriptionData.italic = false;
            list.add(new TextElement(x, y + 10 * bowshaftTraits, w - 22, 60, descriptionData));
        }
    }

    @Override
    protected void addDisplayItems(ArrayList<BookElement> list, int x, MaterialId materialId) {
        List<ItemElement> displayTools = Lists.newArrayList();

        // add display items
        displayTools.add(new TinkerItemElement(0, 0, 1f, getRepairStacks()));
        addPrimaryDisplayItems(displayTools, materialId);

        // fill in leftover space
        if (displayTools.size() < 9) {
            toolLoop:
            for (Item item : TinkersArcheryTags.TinkersArcheryItemTags.MODIFIABLE_PROJECTILE.getValues()) {
                if (item instanceof IModifiable) {
                    IModifiable tool = ((IModifiable)item);
                    List<PartRequirement> requirements = tool.getToolDefinition().getData().getParts();
                    // start building the tool with the given material
                    List<IMaterial> materials = new ArrayList<>(requirements.size());
                    IMaterial material = MaterialRegistry.getMaterial(materialId);
                    boolean usedMaterial = false;
                    for (PartRequirement part : requirements) {
                        // if any stat type of the tool is not supported by this page, skip the whole tool
                        if (!supportsStatType(part.getStatType())) {
                            continue toolLoop;
                        }
                        // if the stat type is not supported by the material, substitute
                        if (hasStatType(materialId, part.getStatType())) {
                            materials.add(material);
                            usedMaterial = true;
                        } else {
                            materials.add(getFirstMaterialWithType(part.getStatType()));
                        }
                    }

                    // only add a stack if our material showed up
                    if (usedMaterial) {
                        ItemStack display = ToolBuildHandler.buildItemFromMaterials(tool, materials);
                        displayTools.add(new TinkerItemElement(display));
                        if (displayTools.size() == 9) {
                            break;
                        }
                    }
                }
            }
        }

        // built tools
        if (!displayTools.isEmpty()) {
            int y = getTitleHeight() - 5;
            for (ItemElement element : displayTools) {
                element.x = x;
                element.y = y;
                element.scale = 1f;
                y += ItemElement.ITEM_SIZE_HARDCODED;

                list.add(element);
            }
        }
    }

    private static boolean hasStatType(MaterialId materialId, MaterialStatsId statsId) {
        return MaterialRegistry.getInstance().getMaterialStats(materialId, statsId).isPresent();
    }

    private static IMaterial getFirstMaterialWithType(MaterialStatsId statsId) {
        for (IMaterial material : MaterialRegistry.getMaterials()) {
            if (material.getIdentifier().equals(MaterialIds.phantom)){
                continue;
            }
            if (hasStatType(material.getIdentifier(), statsId)) {
                return material;
            }
        }
        if (hasStatType(MaterialRegistry.getMaterial(MaterialIds.phantom).getIdentifier(), statsId)) {
            return MaterialRegistry.getMaterial(MaterialIds.phantom);
        }
        return IMaterial.UNKNOWN;
    }

    protected boolean supportsStatType(MaterialStatsId statsId) {
        return statsId.equals(ArrowHeadMaterialStats.ID) || statsId.equals(ArrowShaftMaterialStats.ID)|| statsId.equals(ArrowFletchingMaterialStats.ID);
    }

}
