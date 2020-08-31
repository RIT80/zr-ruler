package com.zaqrit.minecraft.forgemod.zrruler.common.capability;

import com.google.common.base.Objects;

public class ZrRulerImpl implements ZrRulerable {

    byte zrRulerable = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getZrRulerable() {
        return this.zrRulerable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setZrRulerable(final byte zrRulerable) {
        this.zrRulerable = zrRulerable;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleZrRulerable() {
        if (Objects.equal(this.zrRulerable, (byte) 0)) {
            this.zrRulerable = (byte) 1;
        } else {
            this.zrRulerable = (byte) 0;
        }
    }

    @Override
    public boolean isRulerable() {
        return Objects.equal(this.zrRulerable, (byte) 1);
    }


}
