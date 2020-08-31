package com.zaqrit.minecraft.forgemod.zrruler.common.capability;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ZrRulerCapability implements ICapabilitySerializable<ByteNBT> {

    @CapabilityInject(ZrRulerable.class)
    public static final Capability<ZrRulerable> ZRRULERABLE_CAPABILITY = null;
    private LazyOptional<ZrRulerable> instance =
            LazyOptional.of(ZRRULERABLE_CAPABILITY::getDefaultInstance);

    public static void register() {
        CapabilityManager.INSTANCE.register(ZrRulerable.class, new ZrRulerStorage(),
                ZrRulerImpl::new);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return ZRRULERABLE_CAPABILITY.orEmpty(cap, instance);

    }

    @Override
    public ByteNBT serializeNBT() {
        return (ByteNBT) ZRRULERABLE_CAPABILITY.getStorage().writeNBT(ZRRULERABLE_CAPABILITY,
                instance.orElseThrow(
                        () -> new IllegalArgumentException("LazyOptional cannot be empty!")),
                null);
    }

    @Override
    public void deserializeNBT(ByteNBT nbt) {
        ZRRULERABLE_CAPABILITY.getStorage().readNBT(ZRRULERABLE_CAPABILITY,
                instance.orElseThrow(
                        () -> new IllegalArgumentException("LazyOptional cannot be empty!")),
                null, nbt);
    }

}
