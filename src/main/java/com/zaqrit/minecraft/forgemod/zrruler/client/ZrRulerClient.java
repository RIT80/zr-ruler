package com.zaqrit.minecraft.forgemod.zrruler.client;

import java.util.Objects;
import java.util.stream.Stream;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.zaqrit.minecraft.forgemod.zrruler.common.api.constants.ModIds;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ZrRulerClient {

    public static boolean enabled = false;
    public static BlockPos firstBlockPos = null;
    public static BlockPos secondBlockPos = null;
    private KeyBinding ENABLE_RULER;

    public ZrRulerClient() {
        KeyBinding keyBinding = new KeyBinding("ZR RULER TOGGLE", KeyConflictContext.IN_GAME,
                getKey(GLFW.GLFW_KEY_R), ModIds.ZR_RULER_ID + " (" + ModIds.ZR_RULER_NAME + ")");
        ClientRegistry.registerKeyBinding(keyBinding);
        ENABLE_RULER = keyBinding;
    }

    private InputMappings.Input getKey(int key) {
        return InputMappings.Type.KEYSYM.getOrMakeInput(key);
    }

    enum Directions {
        // @formatter:off
        UP(0.5f, 0.405f, 0.5f, 1, 0, 0, 90, TextFormatting.RED.getColor()),
        DOWN(0.5f, -0.650f, 0.5f, 1, 0, 0, -90, TextFormatting.BLUE.getColor()),
        NORTH(0.5f, -0.1f, -0.005f, 0, 0, 0, 0, TextFormatting.GREEN.getColor()),
        SOUTH(0.5f, -0.1f, 1 + 0.005f, 0, 1, 0, 180, TextFormatting.YELLOW.getColor()),
        EAST(1.005f, -0.1f, 0.5f, 0, 1, 0, 270, TextFormatting.DARK_PURPLE.getColor()),
        WEST(-0.005f, -0.1f, 0.5f, 0, 1, 0, 90, TextFormatting.AQUA.getColor());
        // @formatter:on

        private final float translateX;
        private final float translateY;
        private final float translateZ;
        private final float rotateX;
        private final float rotateY;
        private final float rotateZ;
        private final float rotateAngle;
        private final int color;

        private Directions(final float translateX, final float translateY, final float translateZ,
                final float rotateX, final float rotateY, final float rotateZ,
                final float rotateAngle, int color) {
            this.translateX = translateX;
            this.translateY = translateY;
            this.translateZ = translateZ;
            this.rotateX = rotateX;
            this.rotateY = rotateY;
            this.rotateZ = rotateZ;
            this.rotateAngle = rotateAngle;
            this.color = color;
        }

        public float getTranslateX() {
            return this.translateX;
        }

        public float getTranslateY() {
            return this.translateY;
        }

        public float getTranslateZ() {
            return this.translateZ;
        }

        public float getRotateX() {
            return rotateX;
        }

        public float getRotateY() {
            return rotateY;
        }

        public float getRotateZ() {
            return rotateZ;
        }

        public float getRotateAngle() {
            return rotateAngle;
        }

        public int getColor() {
            return color;
        }

    }

    @SuppressWarnings("resource")
    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {

        if (Objects.isNull(firstBlockPos)) {
            return;
        }

        MatrixStack matrixStack = event.getMatrixStack();
        PlayerEntity player = Minecraft.getInstance().player;
        double x = player.lastTickPosX
                + (player.getPosX() - player.lastTickPosX) * event.getPartialTicks();
        double y = player.lastTickPosY
                + (player.getPosY() - player.lastTickPosY) * event.getPartialTicks();
        double z = player.lastTickPosZ
                + (player.getPosZ() - player.lastTickPosZ) * event.getPartialTicks();
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;

        String mark = "■";
        float startPosX = (float) (-fontRenderer.getStringWidth(mark)) / 2.0F + 0.4f;
        float startPosY = -3.5f;
        Stream.of(Directions.values()).forEach(direction -> {
            matrixStack.push();
            matrixStack.translate(-x + firstBlockPos.getX() + direction.getTranslateX(),
                    -y + firstBlockPos.getY() - 1 + direction.getTranslateY(),
                    -z + firstBlockPos.getZ() + direction.getTranslateZ());
            matrixStack.rotate(
                    new Quaternion(new Vector3f(direction.getRotateX(), direction.getRotateY(),
                            direction.getRotateZ()), direction.getRotateAngle(), true));
            matrixStack.scale(0.07F, 0.07F, 0.07F);
            IRenderTypeBuffer.Impl vertexConsumerProvider$Immediate_1 =
                    IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
            fontRenderer.renderString(mark, startPosX, startPosY, direction.getColor(), false,
                    matrixStack.getLast().getMatrix(), vertexConsumerProvider$Immediate_1, false, 0,
                    15728880);
            vertexConsumerProvider$Immediate_1.finish();
            matrixStack.pop();
        });

    }

    @SubscribeEvent(receiveCanceled = true)
    public void pressKey(KeyInputEvent event) {

        if (!ENABLE_RULER.isPressed()) {
            return;
        }
        enabled = !enabled;
        if (!enabled) {
            firstBlockPos = null;
            secondBlockPos = null;
        }
    }

    @SuppressWarnings("resource")
    @SubscribeEvent(receiveCanceled = true)
    public void clickBlock(PlayerInteractEvent.LeftClickBlock event) {

        if (!enabled || event.getWorld().isRemote) {
            return;
        }

        firstBlockPos = event.getPos();
        event.setCanceled(true);

    }
}
