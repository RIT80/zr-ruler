package com.zaqrit.minecraft.forgemod.zrruler.common.capability;

public interface ZrRulerable {

    /**
     * @return state of ZrRulerable({@code 0}:off, {@code 1}:on)
     */
    public byte getZrRulerable();

    /**
     *
     * @param zrRulerable
     */
    public void setZrRulerable(byte zrRulerable);

    public void toggleZrRulerable();

    public boolean isRulerable();
}
