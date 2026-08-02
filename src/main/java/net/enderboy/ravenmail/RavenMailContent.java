package net.enderboy.ravenmail;

import net.enderboy.ravenmail.entity.Raven;
import net.enderboy.ravenmail.entity.RavenVariant;
import net.enderboy500.enderlib.helper.RegistryHelper;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;


public interface RavenMailContent {
    EntityType<Raven> RAVEN = RegistryHelper.registerEntity("raven", EntityType.Builder.of(Raven::new, MobCategory.CREATURE)
            .sized(0.5F, 0.5F)
            .eyeHeight(0.321F)
            .passengerAttachments(new Vec3(0.0, 0.7, -0.1))
            .clientTrackingRange(10));

    Item RAVEN_SPAWN_EGG = Items.registerSpawnEgg(RAVEN);

    ResourceKey<Registry<RavenVariant>> RAVEN_VARIANT = createRegistryKey("raven_variant");
    EntityDataSerializer<Holder<RavenVariant>> RAVEN_VARIANT_DATA = of("raven_variant", RavenVariant.STREAM_CODEC);
    DataComponentType<Holder<RavenVariant>> RAVEN_VARIANT_COMPONENT = RegistryHelper.registerDataComponent("raven/variant", (builder) -> builder.persistent(RavenVariant.ENTRY_CODEC).networkSynchronized(RavenVariant.STREAM_CODEC));

    TagKey<Item> RAVEN_FOOD = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RavenMail.MOD_ID, "raven_food"));
    TagKey<Item> RAVEN_SHINY = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RavenMail.MOD_ID, "raven_shiny"));
    TagKey<Item> RAVEN_ATTRACTED_TO = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RavenMail.MOD_ID, "raven_attracted_to"));
    TagKey<Item> CAN_BE_GIVEN_TO_RAVEN = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RavenMail.MOD_ID, "can_be_given_to_raven"));

    TagKey<Biome> HAS_RAVENS = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(RavenMail.MOD_ID, "has_ravens"));

    SoundEvent RAVEN_AMBIENCE = RegistryHelper.registerSound("raven_hurt");
    SoundEvent RAVEN_HURT = RegistryHelper.registerSound("raven_ambience");

    static void addRavenSpawns() {
        BiomeModifications.addSpawn(biomeSelector -> biomeSelector.hasTag(HAS_RAVENS), MobCategory.CREATURE, RAVEN,
                100, 1, 5);
        SpawnPlacements.register(RAVEN, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
    }

    static <T> EntityDataSerializer<T> of(String name, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        var handler = EntityDataSerializer.forValueType(codec);
        FabricTrackedDataRegistry.register(RavenMail.id(name), handler);
        return handler;
    }
    static <T> ResourceKey<Registry<T>> createRegistryKey(String string) {
        return ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(RavenMail.MOD_ID,string));
    }

    static void load() {
        addRavenSpawns();
        DynamicRegistries.registerSynced(RavenMailContent.RAVEN_VARIANT, RavenVariant.CODEC, RavenVariant.NETWORK_CODEC);
    }
}
