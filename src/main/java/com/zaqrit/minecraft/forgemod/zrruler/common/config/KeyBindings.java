package com.zaqrit.minecraft.forgemod.zrruler.common.config;

import java.util.List;
import com.google.common.collect.ImmutableList;
import com.zaqrit.minecraft.forgemod.zrruler.common.api.constants.ModIds;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public final class KeyBindings {
    private static final String categoryName =
            ModIds.ZR_RULER_ID + " (" + ModIds.ZR_RULER_NAME + ")";
    public static final KeyBinding toggleRulerMode;
    public static final List<KeyBinding> bindingList;

    static InputMappings.Input getKey(int key) {
        return InputMappings.Type.KEYSYM.getOrMakeInput(key);
    }

    static {
        bindingList = ImmutableList.of(toggleRulerMode = new KeyBinding("ZR RULER TOGGLE",
                KeyConflictContext.IN_GAME, getKey(GLFW.GLFW_KEY_R), categoryName));
    }

    private KeyBindings() {
    }

    public static void init() {
        for (KeyBinding binding : bindingList) {
            ClientRegistry.registerKeyBinding(binding);
        }
    }

}
