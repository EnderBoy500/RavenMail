package net.enderboy.ravenmail.data;

import net.enderboy.ravenmail.RavenMailContent;
import net.enderboy.ravenmail.data.provider.RavenMailModelProvider;
import net.enderboy.ravenmail.data.provider.RavenVariantProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;

public class RavenMailDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(RavenMailModelProvider::new);
		pack.addProvider(RavenVariantProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(RavenMailContent.RAVEN_VARIANT, RavenVariantProvider::register);
	}
}
