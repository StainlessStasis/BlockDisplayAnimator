package io.github.stainlessstasis.bdanimator.animation;

import io.github.stainlessstasis.bdanimator.easing.Easing;
import io.github.stainlessstasis.bdanimator.easing.Easings;
import io.github.stainlessstasis.bdanimator.entity.VfxEntity;
import io.github.stainlessstasis.bdanimator.task.CancellableRunnable;
import io.github.stainlessstasis.bdanimator.task.ClientTaskScheduler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class VfxDemos {
    private static final Map<String, BiConsumer<ClientLevel, LocalPlayer>> DEMOS = new HashMap<>();
    static {
        DEMOS.put("keyframes_and_easings", VfxDemos::demoKeyframesAndEasings);
        DEMOS.put("blocks_and_items", VfxDemos::demoBlocksAndItems);
        DEMOS.put("playback_controls", VfxDemos::demoPlaybackControls);
        DEMOS.put("overlay", VfxDemos::demoOverlay);
        DEMOS.put("queue_and_inheritance", VfxDemos::demoQueueAndInheritance);
        DEMOS.put("loops_and_callbacks", VfxDemos::demoLoopsAndCallbacks);
        DEMOS.put("tick_modifiers", VfxDemos::demoTickModifiers);
        DEMOS.put("entity_binding", VfxDemos::demoEntityBinding);
    }

    public static Set<String> getDemoNames() {
        return DEMOS.keySet();
    }

    public static boolean playDemo(String name, ClientLevel level, LocalPlayer player) {
        BiConsumer<ClientLevel, LocalPlayer> demoMethod = DEMOS.get(name.toLowerCase());
        if (demoMethod != null) {
            demoMethod.accept(level, player);
            return true;
        }
        return false;
    }

    private static Vec3 getFrontPosition(Player player) {
        Vec3 look = player.getLookAngle();
        return player.position().add(look.scale(4)).add(0, player.getEyeHeight() - 1, 0);
    }

    public static void demoKeyframesAndEasings(ClientLevel level, LocalPlayer player) {
        Vec3 pos = getFrontPosition(player);
        float spacing = 3f;
        List<Supplier<Easing>> easings = List.of(Easings.LINEAR, Easings.EASE_OUT_BOUNCE, Easings.EASE_IN_OUT_ELASTIC, Easings.EASE_OUT_EXPO);
        BlockState[] blocks = { Blocks.RED_CONCRETE.defaultBlockState(), Blocks.YELLOW_CONCRETE.defaultBlockState(), Blocks.LIME_CONCRETE.defaultBlockState(), Blocks.CYAN_CONCRETE.defaultBlockState() };

        for (int i = 0; i < easings.size(); i++) {
            final int idx = i;
            VfxEntity entity = VfxEntity.create(level, pos.add(i * spacing, 0, 0));
            level.addEntity(entity);

            entity.playAnimation(VfxAnimationBuilder.create()
                    .blockState(blocks[i], builder -> {})
                    .scale(0.5f, builder -> {})
                    .translation(builder -> builder
                            .addKeyframe(0.5f, 0, 4, 0, easings.get(idx))
                            .addKeyframe(1f, 0, 0, 0, easings.get(idx)))
                    .loopInfinite()
                    .build(80));
        }
    }

    public static void demoBlocksAndItems(ClientLevel level, LocalPlayer player) {
        Vec3 pos = getFrontPosition(player);
        VfxEntity entity = VfxEntity.create(level, pos);
        level.addEntity(entity);

        entity.playAnimation(VfxAnimationBuilder.create()
                .blockState(Blocks.GLASS.defaultBlockState(), builder -> {})
                .itemStack(new ItemStack(Items.NETHER_STAR), i -> {})
                .scale(1.5f, builder -> {})
                .rotation(0, 0, 0, builder -> builder
                        .addKeyframe(1f, 0, 360, 0, Easings.LINEAR))
                .loopInfinite()
                .build(60));
    }

    // STOP / PAUSE / RESUME / PLAYBACK SPEED
    public static void demoPlaybackControls(ClientLevel level, LocalPlayer player) {
        Vec3 pos = getFrontPosition(player);
        VfxEntity entity = VfxEntity.create(level, pos);
        entity.setInfinitePersist(true);
        level.addEntity(entity);

        entity.playAnimation(VfxAnimationBuilder.create()
                .blockState(Blocks.EMERALD_BLOCK.defaultBlockState(), builder -> {})
                .scale(1f, builder -> {})
                .translation(builder -> builder
                        .addKeyframe(1f, 0, 5, 0, Easings.LINEAR))
                .onKeyframeReached(0.35f, vfxEntity -> {
                    vfxEntity.setPlaySpeed(0.5f);
                    player.sendSystemMessage(Component.literal("Slowed to 0.5x"));
                })
                .onKeyframeReached(0.5f, vfxEntity -> {
                    vfxEntity.pauseAnimation();
                    player.sendSystemMessage(Component.literal("Paused"));
                    ClientTaskScheduler.INSTANCE.runTaskLater(20, new CancellableRunnable() {
                        @Override protected void execute() {
                            vfxEntity.resumeAnimation();
                            vfxEntity.setPlaySpeed(1.5f);
                            player.sendSystemMessage(Component.literal("Resumed at 1.5x"));
                        }
                    });
                })
                .onKeyframeReached(0.9f, vfxEntity -> {
                    vfxEntity.setPlaySpeed(-3f);
                    player.sendSystemMessage(Component.literal("Reversing!"));
                })
                .build(100));
    }

    public static void demoOverlay(ClientLevel level, LocalPlayer player) {
        Vec3 pos = getFrontPosition(player);
        VfxEntity entity = VfxEntity.create(level, pos);
        level.addEntity(entity);

        entity.playAnimation(VfxAnimationBuilder.create()
                .blockState(Blocks.WHITE_CONCRETE.defaultBlockState(), builder -> {})
                .scale(1f, builder -> {})
                .overlay(1f, 0f, 0f, 0f, builder -> builder
                        .addColorKeyframe(0.25f, new Vector3f(1f, 0f, 0f))
                        .addIntensityKeyframe(0.1f, 0f)
                        .addIntensityKeyframe(0.25f, 0.8f, Easings.EASE_OUT_QUAD)
                        .addColorKeyframe(0.5f, new Vector3f(0f, 1f, 0f))
                        .addColorKeyframe(0.75f, new Vector3f(0f, 0f, 1f))
                        .addColorKeyframe(0.9f, new Vector3f(1f, 0f, 0f))
                        .addIntensityKeyframe(0.9f, 0.8f)
                        .addIntensityKeyframe(1f, 0f, Easings.EASE_IN_QUAD))
                .loopInfinite()
                .build(100));
    }

    public static void demoQueueAndInheritance(ClientLevel level, LocalPlayer player) {
        Vec3 pos = getFrontPosition(player);
        VfxEntity entity = VfxEntity.create(level, pos);
        level.addEntity(entity);

        // stage 1: slam down and squish
        VfxAnimation drop = VfxAnimationBuilder.create()
                .blockState(Blocks.ANVIL.defaultBlockState(), builder -> {})
                .scale(1f, builder -> {})
                .translation(0, 5, 0, builder -> builder
                        .addKeyframe(1f, 0, 0, 0, Easings.EASE_IN_QUART))
                .onStart(e -> level.playLocalSound(pos.x, pos.y, pos.z,
                        SoundEvents.ANVIL_FALL, SoundSource.AMBIENT, 1f, 0.8f, false))
                .build(20);

        // stage 2: squish on impact, inheriting where it landed
        VfxAnimation squish = VfxAnimationBuilder.create()
                .inheritBlockState()
                .inheritTranslation()
                .scale(1f, builder -> builder
                        .addKeyframe(0.3f, 2f, 0.2f, 2f, Easings.EASE_OUT_EXPO)
                        .addKeyframe(1f, 1f, 1f, 1f, Easings.EASE_OUT_BOUNCE))
                .onStart(e -> level.playLocalSound(pos.x, pos.y, pos.z,
                        SoundEvents.ANVIL_LAND, SoundSource.AMBIENT, 1f, 1.2f, false))
                .build(30);

        // stage 3: explode outward, inheriting the scale
        VfxAnimation explode = VfxAnimationBuilder.create()
                .inheritBlockState()
                .inheritTranslation()
                .inheritScale()
                .scale(builder -> builder
                        .addKeyframe(1f, 4f, Easings.EASE_OUT_EXPO))
                .overlay(1f, 0.5f, 0f, 0f, builder -> builder
                        .addIntensityKeyframe(0.2f, 0.9f, Easings.EASE_OUT_QUAD)
                        .addIntensityKeyframe(1f, 0f, Easings.EASE_IN_QUAD))
                .onStart(e -> level.playLocalSound(pos.x, pos.y, pos.z,
                        SoundEvents.GENERIC_EXPLODE.value(), SoundSource.AMBIENT, 0.6f, 1.5f, false))
                .build(25);

        entity.playOrQueueAnimation(drop);
        entity.playOrQueueAnimation(squish);
        entity.playOrQueueAnimation(explode);
    }

    public static void demoLoopsAndCallbacks(ClientLevel level, LocalPlayer player) {
        Vec3 pos = getFrontPosition(player);
        VfxEntity entity = VfxEntity.create(level, pos);
        level.addEntity(entity);

        entity.playAnimation(VfxAnimationBuilder.create()
                .blockState(Blocks.BEACON.defaultBlockState(), builder -> {})
                .scale(1f, builder -> {})
                .onLoop(vfxEntity -> level.playLocalSound(
                        pos.x, pos.y, pos.z,
                        SoundEvents.BEACON_ACTIVATE, SoundSource.AMBIENT, 0.4f, 1.2f, false))
                .onKeyframeReached(0.5f, vfxEntity -> level.playLocalSound(
                        pos.x, pos.y, pos.z,
                        SoundEvents.BEACON_POWER_SELECT, SoundSource.AMBIENT, 0.3f, 1.5f, false))
                .overlay(0.4f, 0.8f, 1f, 0f, builder -> builder
                        .addIntensityKeyframe(0.3f, 0.6f, Easings.EASE_OUT_QUAD)
                        .addIntensityKeyframe(0.5f, 0f, Easings.EASE_IN_QUAD)
                        .addIntensityKeyframe(1f, 0f))
                .scale(1f, builder -> builder
                        .addKeyframe(0.3f, 1.3f, Easings.EASE_OUT_QUAD)
                        .addKeyframe(0.5f, 1f, Easings.EASE_IN_QUAD))
                .loopInfinite()
                .build(40));
    }

    public static void demoTickModifiers(ClientLevel level, LocalPlayer player) {
        Vec3 pos = getFrontPosition(player);
        VfxEntity anchor = VfxEntity.create(level, pos);
        anchor.setInfinitePersist(true);
        VfxAnimation anchorAnim = VfxAnimationBuilder.create()
                .blockState(Blocks.END_STONE.defaultBlockState(), builder -> {})
                .scale(0.8f, builder -> {})
                .build(1);
        anchor.playAnimation(anchorAnim);
        level.addEntity(anchor);

        VfxEntity orbiter = VfxEntity.create(level, pos);
        orbiter.setInfinitePersist(true);
        level.addEntity(orbiter);

        float orbitRadius = 3f;
        orbiter.playAnimation(VfxAnimationBuilder.create()
                .blockState(Blocks.PURPUR_BLOCK.defaultBlockState(), builder -> {})
                .onTickTranslation((translation, ctx) -> {
                    float angle = ctx.interpolatedTicks() * 0.1f;
                    translation.x += (float)(Math.cos(angle) * orbitRadius);
                    translation.z += (float)(Math.sin(angle) * orbitRadius);
                })
                .scale(0.5f, builder -> builder
                        .addKeyframe(0.5f, 1f, Easings.EASE_OUT_ELASTIC)
                        .addKeyframe(1f, 0.5f, Easings.EASE_IN_QUAD))
                .loopInfinite()
                .build(60));
    }

    public static void demoEntityBinding(ClientLevel level, LocalPlayer player) {
        Vec3 pos = getFrontPosition(player);
        Snowball snowball = new Snowball(EntityType.SNOWBALL, level);
        snowball.setPos(pos);
        Vec3 look = player.getLookAngle();
        snowball.setDeltaMovement(look.scale(1.5f));
        level.addEntity(snowball);

        VfxEntity trail = VfxEntity.createBoundTo(level, snowball);
        trail.setOnTick(vfxEntity -> {
            if (!level.getBlockState(snowball.blockPosition()).isAir()) {
                snowball.discard();
            }
        });
        trail.setOnBoundEntityRemoved(vfxEntity -> {
            vfxEntity.stopAnimations();
        });
        level.addEntity(trail);

        trail.playAnimation(VfxAnimationBuilder.create()
                .blockState(Blocks.PACKED_ICE.defaultBlockState(), builder -> {})
                .scale(0.4f, builder -> {})
                .rotation(0, 0, 0, builder -> builder
                        .addKeyframe(1f, 360, 360, 0, Easings.LINEAR))
                .onEnd(vfxEntity -> {
                    Vec3 impactPos = vfxEntity.position();
                    level.playLocalSound(impactPos.x, impactPos.y, impactPos.z,
                            SoundEvents.GLASS_BREAK, SoundSource.AMBIENT, 1.0f, 0.6f, false);

                    VfxEntity impact = VfxEntity.create(level, impactPos);
                    level.addEntity(impact);
                    impact.playAnimation(VfxAnimationBuilder.create()
                            .blockState(Blocks.PACKED_ICE.defaultBlockState(), builder -> {})
                            .overlay(1f, 1f, 1f, 0f, builder -> builder
                                    .addIntensityKeyframe(0.0f, 0.9f)
                                    .addIntensityKeyframe(1.0f, 0.0f, Easings.EASE_OUT_QUAD))
                            .scale(1.0f, builder -> builder
                                    .addKeyframe(1.0f, 1.8f, Easings.EASE_OUT_EXPO))
                            .build(12));
                })
                .loopInfinite()
                .build(40));
    }
}