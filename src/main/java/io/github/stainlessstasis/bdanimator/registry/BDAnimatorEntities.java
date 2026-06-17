package io.github.stainlessstasis.bdanimator.registry;

import io.github.stainlessstasis.bdanimator.BDAnimator;
import io.github.stainlessstasis.bdanimator.entity.VfxEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BDAnimatorEntities {
    private static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(BDAnimator.MODID);

    public static final Supplier<EntityType<@NotNull VfxEntity>> VFX_ENTITY = ENTITY_TYPES.register(
            "vfx_entity",
            () -> EntityType.Builder.of(
                            VfxEntity::createDefault,
                            MobCategory.MISC
                    )
                    .noSave()
                    .noSummon()
                    .fireImmune()
                    .canSpawnFarFromPlayer()
                    .clientTrackingRange(16)
                    .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(BDAnimator.MODID, "vfx_entity")
                    ))
    );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
