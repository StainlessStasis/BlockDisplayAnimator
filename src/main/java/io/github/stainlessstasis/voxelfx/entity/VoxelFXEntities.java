package io.github.stainlessstasis.voxelfx.entity;

import io.github.stainlessstasis.voxelfx.VoxelFX;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class VoxelFXEntities {
    private static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(VoxelFX.MODID);

    public static final Supplier<EntityType<@NotNull VfxEntity>> VFX_ENTITY = ENTITY_TYPES.register(
            "vfx_entity",
            () -> EntityType.Builder.of(
                            VfxEntity::new,
                            MobCategory.MISC
                    )
                    .sized(0, 0)
                    .clientTrackingRange(16)
                    .updateInterval(1)
                    .noSave()
                    .noSummon()
                    .fireImmune()
                    .canSpawnFarFromPlayer()
                    .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(VoxelFX.MODID, "vfx_entity")
                    ))
    );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
