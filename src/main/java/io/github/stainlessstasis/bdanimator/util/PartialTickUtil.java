package io.github.stainlessstasis.bdanimator.util;

import net.minecraft.client.Minecraft;

public class PartialTickUtil {
    public static float get() {
        return Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks();
    }
}
