package tonite.tinkersarchery.client.book;

import com.google.common.collect.ImmutableSet;
import net.minecraft.util.ResourceLocation;
import slimeknights.mantle.client.book.BookLoader;
import slimeknights.mantle.client.book.BookTransformer;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.repository.FileRepository;
import slimeknights.tconstruct.library.book.sectiontransformer.ModifierSectionTransformer;
import slimeknights.tconstruct.library.book.sectiontransformer.ToolSectionTransformer;
import tonite.tinkersarchery.TinkersArchery;
import tonite.tinkersarchery.stats.*;

public class TinkersArcheryBook extends BookData {

    private static final ResourceLocation AWESOME_ARCHERY_ID = TinkersArchery.getResource("awesome_archery");

    public static final BookData AWESOME_ARCHERY = BookLoader.registerBook(AWESOME_ARCHERY_ID.toString(), false, false);

    public static void initBook() {

        // register page types
        BookLoader.registerPageType(ContentBowHeadMaterial.ID, ContentBowHeadMaterial.class);
        BookLoader.registerPageType(ContentBowStringMaterial.ID, ContentBowStringMaterial.class);
        BookLoader.registerPageType(ContentArrowMaterial.ID, ContentArrowMaterial.class);

        // tool transformers
        AWESOME_ARCHERY.addTransformer(ToolSectionTransformer.INSTANCE);
        AWESOME_ARCHERY.addTransformer(new AdvancedMaterialSelectionTransformer("bow_materials", ImmutableSet.of(BowMaterialStats.ID, BowGuideMaterialStats.ID), ContentBowHeadMaterial::new));
        AWESOME_ARCHERY.addTransformer(new AdvancedMaterialSelectionTransformer("bowstring_materials", ImmutableSet.of(BowStringMaterialStats.ID), ContentBowStringMaterial::new));
        AWESOME_ARCHERY.addTransformer(new ModifierSectionTransformer("modifier_bow"));
        AWESOME_ARCHERY.addTransformer(new ToolSectionTransformer("arrows"));
        AWESOME_ARCHERY.addTransformer(new AdvancedMaterialSelectionTransformer("arrow_materials", ImmutableSet.of(ArrowHeadMaterialStats.ID, ArrowShaftMaterialStats.ID), ContentArrowMaterial::new));
        AWESOME_ARCHERY.addTransformer(new AdvancedMaterialSelectionTransformer("arrow_fletching_materials", ImmutableSet.of(ArrowFletchingMaterialStats.ID), ContentArrowFletchingMaterial::new));
        AWESOME_ARCHERY.addTransformer(new ModifierSectionTransformer("modifier_arrow"));

        addStandardData(AWESOME_ARCHERY, AWESOME_ARCHERY_ID);

    }

    private static void addStandardData(BookData book, ResourceLocation id) {
        book.addRepository(new FileRepository(id.getNamespace() + ":book/" + id.getPath()));
        book.addTransformer(BookTransformer.indexTranformer());
        // padding needs to be last to ensure page counts are right
        book.addTransformer(BookTransformer.paddingTransformer());
    }
}
