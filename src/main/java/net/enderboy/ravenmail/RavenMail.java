package net.enderboy.ravenmail;

import com.mojang.serialization.MapCodec;
import io.github.ciph3rj.cipherlib.Cipher;
import net.enderboy.ravenmail.data.ChanceSpawnCondition;
import net.enderboy.ravenmail.entity.Raven;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.fabric.impl.resource.ResourceLoaderImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.variant.SpawnCondition;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RavenMail implements ModInitializer {
	public static final String MOD_ID = "ravenmail";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Raven Mail");

		Cipher.addModId(MOD_ID);

		RavenMailContent.load();
		ofSpawnCondition("chance", ChanceSpawnCondition.CODEC);

		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> {
			entries.accept(RavenMailContent.RAVEN_SPAWN_EGG);
		});

		FabricDefaultAttributeRegistry.register(RavenMailContent.RAVEN, Raven.createAttributes());

		FabricLoader.getInstance().getModContainer(RavenMail.MOD_ID).ifPresent(modContainer -> ResourceLoaderImpl.registerBuiltinPack(id("spawn_special_ravens"), "datapacks/" + id("spawn_special_ravens").getPath(), modContainer, PackActivationType.NORMAL));

		LOGGER.info("Finished Initializing Raven Mail");
	}

	public static MapCodec<? extends SpawnCondition> ofSpawnCondition(String name, MapCodec<? extends SpawnCondition> codec) {
		return Registry.register(BuiltInRegistries.SPAWN_CONDITION_TYPE, id(name), codec);
	}
}