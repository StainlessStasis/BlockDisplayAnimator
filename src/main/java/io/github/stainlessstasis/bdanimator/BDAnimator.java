package io.github.stainlessstasis.bdanimator;

import io.github.stainlessstasis.bdanimator.easing.Easings;
import io.github.stainlessstasis.bdanimator.entity.BDAnimatorEntities;
import net.minecraft.resources.Identifier;
import net.neoforged.fml.common.EventBusSubscriber;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber
@Mod(BDAnimator.MODID)
public class BDAnimator {
    public static final String MODID = "bdanimator";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BDAnimator(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        BDAnimatorEntities.register(modEventBus);
//        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {}

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
