package net.enderboy.ravenmail;

import com.mojang.authlib.Environment;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.authlib.yggdrasil.YggdrasilGameProfileRepository;
import com.mojang.authlib.yggdrasil.response.NameAndId;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.enderboy.ravenmail.data.ChanceSpawnCondition;
import net.enderboy.ravenmail.entity.Raven;
import net.enderboy.ravenmail.entity.RavenVariant;
import net.enderboy.ravenmail.entity.RavenVariants;
import net.enderboy500.enderlib.ELib;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.fabric.impl.resource.ResourceLoaderImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.VarInt;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.variant.SpawnCondition;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Proxy;
import java.util.Optional;
import java.util.UUID;

public class RavenMail implements ModInitializer {
	public static final String MOD_ID = "ravenmail";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Raven Mail");

		ELib.addModId(MOD_ID);

		RavenMailContent.load();
		of("chance", ChanceSpawnCondition.CODEC);

		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> {
			entries.accept(RavenMailContent.RAVEN_SPAWN_EGG);
		});

		FabricDefaultAttributeRegistry.register(RavenMailContent.RAVEN, Raven.createAttributes());

		FabricLoader.getInstance().getModContainer(RavenMail.MOD_ID).ifPresent(modContainer -> ResourceLoaderImpl.registerBuiltinPack(id("spawn_special_ravens"), "datapacks/" + id("spawn_special_ravens").getPath(), modContainer, PackActivationType.NORMAL));

		LOGGER.info("Finished Initializing Raven Mail");
	}

	public static MapCodec<? extends SpawnCondition> of(String name, MapCodec<? extends SpawnCondition> codec) {
		return Registry.register(BuiltInRegistries.SPAWN_CONDITION_TYPE, id(name), codec);
	}
}