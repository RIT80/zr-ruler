package com.zaqrit.minecraft.forgemod.zrruler.common.eventhandler;

import com.zaqrit.minecraft.forgemod.zrruler.common.capability.ZrRulerCapability;
import com.zaqrit.minecraft.forgemod.zrruler.common.capability.ZrRulerable;
import com.zaqrit.minecraft.forgemod.zrruler.common.config.KeyBindings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ZrRulerEventHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    @SuppressWarnings("resource")
    @SubscribeEvent(receiveCanceled = true)
    public void pressKey(KeyInputEvent event) {
        if (!KeyBindings.toggleRulerMode.isPressed())
            return;


        PlayerEntity player = Minecraft.getInstance().player;

        player.getCapability(ZrRulerCapability.ZRRULERABLE_CAPABILITY)
                .ifPresent(new NonNullConsumer<ZrRulerable>() {

                    @Override
                    public void accept(ZrRulerable zrRulable) {
                        System.out.println("before toggle presskey:" + zrRulable.getZrRulerable());
                        zrRulable.toggleZrRulerable();
                        System.out.println("after toggle presskey:" + zrRulable.getZrRulerable());

                        // PacketHandler.sendTo(
                        // new CapabilityPacket((byte) (~zrRulable.getZrRulerable())), player);
                    }
                });
    }

    @SuppressWarnings("resource")
    @SubscribeEvent(receiveCanceled = true)
    public void clickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getWorld().isRemote) {
            return;
        }

        LOGGER.info("click block!");
        PlayerEntity player = Minecraft.getInstance().player;
        player.getCapability(ZrRulerCapability.ZRRULERABLE_CAPABILITY)
                .ifPresent(new NonNullConsumer<ZrRulerable>() {

                    @Override
                    public void accept(ZrRulerable zrRulable) {
                        System.out.println("click:" + zrRulable.getZrRulerable());
                        if (!zrRulable.isRulerable()) {
                            return;
                        }
                        event.setCanceled(true);

                    }
                });


    }
}
