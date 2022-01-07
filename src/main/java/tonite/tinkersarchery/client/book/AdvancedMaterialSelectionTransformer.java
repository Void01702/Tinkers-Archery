package tonite.tinkersarchery.client.book;

import com.google.common.collect.ImmutableSet;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.SectionData;
import slimeknights.mantle.client.book.repository.BookRepository;
import slimeknights.mantle.client.book.transformer.SectionTransformer;
import slimeknights.mantle.client.screen.book.element.ItemElement;
import slimeknights.mantle.client.screen.book.element.SizedBookElement;
import slimeknights.tconstruct.library.book.content.ContentMaterial;
import slimeknights.tconstruct.library.book.content.ContentPageIconList;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;

import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AdvancedMaterialSelectionTransformer extends SectionTransformer {

    private final Set<MaterialStatsId> visibleStats;
    private final Function<IMaterial, ContentMaterial> getContentMaterial;

    public AdvancedMaterialSelectionTransformer(String sectionName, Set<MaterialStatsId> visibleStats, Function<IMaterial, ContentMaterial> getContentMaterial) {
        super(sectionName);
        this.visibleStats = visibleStats;
        this.getContentMaterial = getContentMaterial;
    }

    /**
     * Determines if a material should show in this book
     * @param material  Material to check
     * @return  True if it should show
     */
    protected boolean isValidMaterial(IMaterial material) {
        for (IMaterialStats stats : MaterialRegistry.getInstance().getAllStats(material.getIdentifier())) {
            if (visibleStats.contains(stats.getIdentifier())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the page for the given material, can override if you use a different page type
     * @param material       Material to display
     * @return  Material page
     */
    protected ContentMaterial getPageContent(IMaterial material) {
        return getContentMaterial.apply(material);
    }

    @Override
    public void transform(BookData book, SectionData sectionData) {
        sectionData.source = BookRepository.DUMMY;
        sectionData.parent = book;

        List<IMaterial> materialList = MaterialRegistry.getMaterials().stream()
                .filter(this::isValidMaterial)
                .collect(Collectors.toList());

        if (materialList.isEmpty()) {
            return;
        }

        // calculate pages needed
        List<ContentPageIconList> listPages = ContentPageIconList.getPagesNeededForItemCount(materialList.size(), sectionData, book.translate(this.sectionName), book.strings.get(String.format("%s.subtext", this.sectionName)));

        ListIterator<ContentPageIconList> iter = listPages.listIterator();
        ContentPageIconList overview = iter.next();

        for (IMaterial material : materialList) {
            ContentMaterial contentMaterial = this.getPageContent(material);
            PageData page = this.addPage(sectionData, material.getIdentifier().toString(), ContentMaterial.ID, contentMaterial);

            SizedBookElement icon = new ItemElement(0, 0, 1f, contentMaterial.getDisplayStacks());
            while (!overview.addLink(icon, contentMaterial.getTitle(), page)) {
                overview = iter.next();
            }
        }
    }
}