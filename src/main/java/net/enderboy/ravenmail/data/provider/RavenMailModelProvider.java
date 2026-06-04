package net.enderboy.ravenmail.data.provider;

import net.enderboy.ravenmail.RavenMailContent;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class RavenMailModelProvider extends FabricModelProvider {
    public RavenMailModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(RavenMailContent.RAVEN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
    }
}
