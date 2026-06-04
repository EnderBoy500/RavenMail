package net.enderboy.ravenmail.data.provider;

import net.enderboy.ravenmail.RavenMailContent;
import net.enderboy.ravenmail.data.ChanceSpawnCondition;
import net.enderboy.ravenmail.entity.RavenVariant;
import net.enderboy.ravenmail.entity.RavenVariants;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;

import java.util.concurrent.CompletableFuture;

public class RavenVariantProvider extends FabricDynamicRegistryProvider {
    public RavenVariantProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(RavenMailContent.RAVEN_VARIANT));
    }

    @Override
    public String getName() {
        return "";
    }

    public static void register(BootstrapContext<RavenVariant> registerable) {
        of(registerable, RavenVariants.BLACK,0x222130, 20/50f);
        of(registerable, RavenVariants.GRAY, 0x393B43,15/50f);
        of(registerable, RavenVariants.WHITE, 0xB4B1B4, 7/50f);
        of(registerable, RavenVariants.ALBINO, 0xE3E0E3,2/50f);
        of(registerable, RavenVariants.BROWN, 0x2F2829,6/50f);
        of(registerable, RavenVariants.BLUE, 0x171942,2/50f);
        of(registerable, RavenVariants.PURPLE, 0x442A54,0f);
        of(registerable, RavenVariants.RED, 0x54201F,0f);
    }


/*    public static SpawnPrioritySelectors createSpawnConditions(HolderSet<Biome> requiredBiomes) {
        return SpawnPrioritySelectors.single(new BiomeCheck(requiredBiomes), 1);
    }
    public static SpawnPrioritySelectors createChanceConditions(float f) {
        return SpawnPrioritySelectors.single(new ChanceSpawnCondition(f), 1);
    }

    private static void of(BootstrapContext<RavenVariant> registry, ResourceKey<RavenVariant> key, TagKey<Biome> biomeTag, float f) {
        registry.register(key, new RavenVariant(new ClientAsset.ResourceTexture(key.identifier().withPrefix("entity/raven/")), new SpawnPrioritySelectors(List.of(createSpawnConditions(registry.lookup(Registries.BIOME).getOrThrow(biomeTag)), createChanceConditions(f)))));
    }*/
    private static void of(BootstrapContext<RavenVariant> registry, ResourceKey<RavenVariant> key,int color,float f) {
        registry.register(key, new RavenVariant(new ClientAsset.ResourceTexture(key.identifier().withPrefix("entity/raven/")), color, SpawnPrioritySelectors.single(new ChanceSpawnCondition(f), 1)));
    }
}
