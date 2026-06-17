package io.github.stainlessstasis.bdanimator;

import io.github.stainlessstasis.bdanimator.animation.AnimationTest;
import io.github.stainlessstasis.bdanimator.registry.BDAnimatorEntities;
import io.github.stainlessstasis.bdanimator.vfx.VfxEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

@Mod(value = BDAnimator.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = BDAnimator.MODID, value = Dist.CLIENT)
public class BDAnimatorClient {
    public BDAnimatorClient(ModContainer container) {
//        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {}

    @SubscribeEvent
    static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BDAnimatorEntities.VFX_ENTITY.get(), VfxEntityRenderer::new);
    }

    @SubscribeEvent
    static void onKeyInput(InputEvent.Key event) {
        if (event.getAction() != GLFW.GLFW_PRESS) return;
        if (event.getKey() == GLFW.GLFW_KEY_LEFT_ALT) {
            AnimationTest.runKeyframeTest();
        }
    }
}
