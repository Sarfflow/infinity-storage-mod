package sarfflow.infstorage;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sarfflow.infstorage.block.InfChestBlock;
import sarfflow.infstorage.block.InfChestBlockEntity;

public class ExampleMod implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("inf-storage");
    public static final String modID = "inf_storage";

    public static final InfChestBlock INF_CHEST = new InfChestBlock(FabricBlockSettings.of(Material.STONE).strength(10.0f, Float.MAX_VALUE).luminance(5));

    public static final BlockEntityType<InfChestBlockEntity> INF_CHEST_BLOCK_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(modID, "inf_chest_block_entity"),
            FabricBlockEntityTypeBuilder.create(InfChestBlockEntity::new, INF_CHEST).build()
    );

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        Registry.register(Registry.BLOCK, new Identifier(modID, "inf_chest"), INF_CHEST);
        Registry.register(Registry.ITEM, new Identifier(modID, "inf_chest"), new BlockItem(INF_CHEST, new FabricItemSettings().fireproof().rarity(Rarity.RARE).group(ItemGroup.SEARCH)));

        LOGGER.debug("Hello Fabric world!");
    }
}