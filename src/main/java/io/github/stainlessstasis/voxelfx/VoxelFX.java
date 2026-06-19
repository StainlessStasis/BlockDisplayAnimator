package io.github.stainlessstasis.voxelfx;

import io.github.stainlessstasis.voxelfx.entity.VoxelFXEntities;
import net.minecraft.resources.Identifier;
import net.neoforged.fml.common.EventBusSubscriber;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber
@Mod(VoxelFX.MODID)
public class VoxelFX {
    public static final String MODID = "voxelfx";
    public static final Logger LOGGER = LogUtils.getLogger();

    public VoxelFX(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        VoxelFXEntities.register(modEventBus);
//        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {}

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
