package net.enderboy.ravenmail.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.enderboy.ravenmail.RavenMailContent;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnCondition;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;

import java.util.List;

public record RavenVariant(ClientAsset.ResourceTexture assetInfo, ClientAsset.ResourceTexture assetInfoBaby, int color , SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final Codec<RavenVariant> NETWORK_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC.forGetter(RavenVariant::assetInfo), ClientAsset.ResourceTexture.CODEC.fieldOf("asset_id_baby").forGetter(RavenVariant::assetInfoBaby),
            Codec.INT.fieldOf("color").forGetter(RavenVariant::color)).apply(instance, RavenVariant::new));
    public static final Codec<RavenVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ClientAsset.ResourceTexture.CODEC.fieldOf("asset_id").forGetter(RavenVariant::assetInfo), ClientAsset.ResourceTexture.CODEC.fieldOf("asset_id_baby").forGetter(RavenVariant::assetInfoBaby)
            , Codec.INT.fieldOf("color").forGetter(RavenVariant::color)
            ,SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(RavenVariant::spawnConditions)
    ).apply(instance, RavenVariant::new));;
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<RavenVariant>> STREAM_CODEC;
    public static final Codec<Holder<RavenVariant>> ENTRY_CODEC = RegistryFixedCodec.create(RavenMailContent.RAVEN_VARIANT);

    private RavenVariant(ClientAsset.ResourceTexture resourceTexture, ClientAsset.ResourceTexture resourceTextureBaby, int color) {
        this(resourceTexture, resourceTextureBaby,color, SpawnPrioritySelectors.EMPTY);
    }

    public List<Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }

    static {
        STREAM_CODEC = ByteBufCodecs.holderRegistry(RavenMailContent.RAVEN_VARIANT);
    }
}
