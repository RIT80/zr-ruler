package com.zaqrit.minecraft.forgemod.zrruler.client;

import java.util.Objects;
import com.mojang.blaze3d.systems.RenderSystem;
import com.zaqrit.minecraft.forgemod.zrruler.common.api.constants.ModIds;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ZrRulerClient {

    public static boolean enabled = false;
    public static BlockPos firstBlockPos = null;
    public static BlockPos secondBlockPos = null;
    private static final KeyBinding ENABLE_RULER;

    static {
        ENABLE_RULER = registerKeybind();
    }

    private static KeyBinding registerKeybind() {
        KeyBinding keyBinding = new KeyBinding("ZR RULER TOGGLE", KeyConflictContext.IN_GAME,
                getKey(GLFW.GLFW_KEY_R), ModIds.ZR_RULER_ID + " (" + ModIds.ZR_RULER_NAME + ")");
        ClientRegistry.registerKeyBinding(keyBinding);
        return keyBinding;
    }

    static InputMappings.Input getKey(int key) {
        return InputMappings.Type.KEYSYM.getOrMakeInput(key);
    }

    @SuppressWarnings("unused")
    public static void register() {
        // Setup
        EntityType<Entity> testingEntityType =
                EntityType.Builder.create(EntityClassification.MONSTER).size(0f, 0f)
                        .disableSerialization().build(null);
        MinecraftForge.EVENT_BUS.register(ZrRulerClient.class);
    }

    public static void renderWorldLast() {

        if (Objects.isNull(firstBlockPos)) {
            return;
        }

        Minecraft client = Minecraft.getInstance();
        ClientPlayerEntity playerEntity = client.player;
        ISelectionContext selectionContext = ISelectionContext.forEntity(playerEntity);
        World world = client.world;
        ActiveRenderInfo info = client.gameRenderer.getActiveRenderInfo();

        RenderSystem.pushMatrix();
        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
        ZrRulerClient.renderLevel(client, info, world, firstBlockPos, selectionContext);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableDepthTest();
        RenderSystem.popMatrix();

    }

    private static void renderLevel(Minecraft minecraft, ActiveRenderInfo info, World world,
            BlockPos pos, ISelectionContext context) {
        String string_1 = "■";
        FontRenderer fontRenderer = minecraft.fontRenderer;

        double double_4 = info.getProjectedView().x;
        double double_5 = info.getProjectedView().y;
        double double_6 = info.getProjectedView().z;
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float) (pos.getX() + 0.5f - double_4),
                (float) (pos.getY() + 1 - double_5) + 0.005f,
                (float) (pos.getZ() + 0.5f - double_6));

        RenderSystem.rotatef(90, 1, 0, 0);
        RenderSystem.normal3f(0.0F, 1.0F, 0.0F);
        float size = 0.07F;
        RenderSystem.scalef(-size, -size, size);
        float float_3 = (float) (-fontRenderer.getStringWidth(string_1)) / 2.0F + 0.4f;
        RenderSystem.enableAlphaTest();
        IRenderTypeBuffer.Impl vertexConsumerProvider$Immediate_1 =
                IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        fontRenderer.renderString(string_1, float_3, -3.5f, 0xff042404, false,
                TransformationMatrix.identity().getMatrix(), vertexConsumerProvider$Immediate_1,
                false, 0, 15728880);

        vertexConsumerProvider$Immediate_1.finish();
        RenderSystem.popMatrix();
    }

    @SubscribeEvent(receiveCanceled = true)
    public static void pressKey(KeyInputEvent event) {

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
    public static void clickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (!enabled || event.getWorld().isRemote) {
            return;
        }

        firstBlockPos = event.getPos();

        event.setCanceled(true);

    }
}
