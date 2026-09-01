package net.enderboy.ravenmail.block;

import net.enderboy.ravenmail.RavenMailContent;
import net.enderboy.ravenmail.entity.RavenVariant;
import net.enderboy.ravenmail.entity.RavenVariants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class RavenEggBlockEntity extends BlockEntity {
    private Holder<RavenVariant> ravenVariant;

    public RavenEggBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(RavenMailContent.RAVEN_EGG_BE, blockPos, blockState);
    }

    public void setRavenVariant(Holder<RavenVariant> ravenVariant) {
        this.ravenVariant = ravenVariant;
    }

    public Holder<RavenVariant> getRavenVariant() {
        return ravenVariant;
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        valueInput.read("RavenVariant", RavenVariant.ENTRY_CODEC).ifPresent(this::setRavenVariant);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        if (ravenVariant != null) valueOutput.store("RavenVariant", RavenVariant.ENTRY_CODEC, getRavenVariant());
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }
}
