package io.github.stainlessstasis.bdanimator;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(BDAnimator.MODID)
public class BDAnimator {
    public static final String MODID = "bdanimator";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BDAnimator(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
//        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {}

}
