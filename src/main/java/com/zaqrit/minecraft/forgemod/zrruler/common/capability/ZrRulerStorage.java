package com.zaqrit.minecraft.forgemod.zrruler.common.capability;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ZrRulerStorage implements IStorage<ZrRulerable> {

    @Override
    public INBT writeNBT(Capability<ZrRulerable> capability, ZrRulerable instance, Direction side) {

        return ByteNBT.valueOf(instance.getZrRulerable());

    }

    @Override
    public void readNBT(Capability<ZrRulerable> capability, ZrRulerable instance, Direction side,
            INBT nbt) {
        if (!(instance instanceof ZrRulerImpl)) {
            throw new IllegalArgumentException();
        }
        instance.setZrRulerable(((ByteNBT) nbt).getByte());

    }

}
