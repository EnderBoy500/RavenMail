package net.enderboy.ravenmail.entity;

import net.enderboy.ravenmail.RavenMail;
import net.enderboy.ravenmail.RavenMailContent;
import net.minecraft.core.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.variant.*;

import java.util.Optional;

public interface RavenVariants {
    ResourceKey<RavenVariant> BLACK = of("black");
    ResourceKey<RavenVariant> GRAY = of("gray");
    ResourceKey<RavenVariant> WHITE = of("white");
    ResourceKey<RavenVariant> ALBINO = of("albino");
    ResourceKey<RavenVariant> BROWN = of("brown");
    ResourceKey<RavenVariant> BLUE = of("blue");
    ResourceKey<RavenVariant> RED = of("red");
    ResourceKey<RavenVariant> PURPLE = of("purple");

    private static ResourceKey<RavenVariant> of(String path) {
        return of(Identifier.fromNamespaceAndPath(RavenMail.MOD_ID, "raven_" + path));
    }

    static ResourceKey<RavenVariant> of(Identifier id) {
        return ResourceKey.create(RavenMailContent.RAVEN_VARIANT, id);
    }
    static Optional<Holder.Reference<RavenVariant>> select(RandomSource random, RegistryAccess registries, SpawnContext context) {
        return PriorityProvider.pick(registries.lookupOrThrow(RavenMailContent.RAVEN_VARIANT).listElements(), Holder::value, random, context);
    }
}
