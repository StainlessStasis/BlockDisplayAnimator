package io.github.stainlessstasis.bdanimator;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.stainlessstasis.bdanimator.animation.VfxDemos;
import io.github.stainlessstasis.bdanimator.entity.VfxEntity;
import io.github.stainlessstasis.bdanimator.entity.BDAnimatorEntities;
import io.github.stainlessstasis.bdanimator.entity.VfxEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.List;

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
    static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("bda")
                        .then(Commands.literal("clear")
                                .executes(ctx -> {
                                    ClientLevel level = Minecraft.getInstance().level;
                                    if (level == null) return 0;
                                    Player player = Minecraft.getInstance().player;
                                    if (player == null) return 0;

                                    List<VfxEntity> entities = level.getEntitiesOfClass(VfxEntity.class,
                                            player.getBoundingBox().inflate(1000));
                                    entities.forEach(Entity::discard);

                                    player.sendSystemMessage(Component.translatable(
                                            BDAnimator.MODID + ".cleared_entities",
                                            entities.size()
                                    ));

                                    return entities.size();
                                })
                        )
                        .then(Commands.literal("pause")
                                .executes(ctx -> {
                                    var entities = getAllVfx();
                                    entities.forEach(VfxEntity::pauseAnimation);
                                    return entities.size();
                                })
                        )
                        .then(Commands.literal("resume")
                                .executes(ctx -> {
                                    var entities = getAllVfx();
                                    entities.forEach(VfxEntity::resumeAnimation);
                                    return entities.size();
                                })
                        )
                        .then(Commands.literal("stop")
                                .executes(ctx -> {
                                    var entities = getAllVfx();
                                    entities.forEach(VfxEntity::stopAnimations);
                                    return entities.size();
                                })
                        )
                        .then(Commands.literal("speed")
                                .then(Commands.argument("value", FloatArgumentType.floatArg())
                                        .executes(ctx -> {
                                            float value = FloatArgumentType.getFloat(ctx, "value");
                                            var entities = getAllVfx();
                                            entities.forEach(entity -> entity.setPlaySpeed(value));
                                            return entities.size();
                                        })
                                )
                        )
                        .then(Commands.literal("demo")
                                .then(Commands.argument("name", StringArgumentType.word())
                                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(VfxDemos.getDemoNames(), builder))
                                        .executes(ctx -> {
                                            ClientLevel level = Minecraft.getInstance().level;
                                            LocalPlayer player = Minecraft.getInstance().player;
                                            if (level == null || player == null) return 0;

                                            String demoName = StringArgumentType.getString(ctx, "name");
                                            boolean success = VfxDemos.playDemo(demoName, level, player);

                                            if (success) {
                                                player.sendSystemMessage(Component.literal("Playing demo: " + demoName));
                                                return 1;
                                            } else {
                                                player.sendSystemMessage(Component.literal("Unknown demo: " + demoName).withColor(Color.RED.getRGB()));
                                                return 0;
                                            }
                                        })
                                )
                        )
        );
    }

    private static List<VfxEntity> getAllVfx() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return List.of();
        Player player = Minecraft.getInstance().player;
        if (player == null) return List.of();

        return level.getEntitiesOfClass(VfxEntity.class,
                player.getBoundingBox().inflate(1000));
    }

    @SubscribeEvent
    static void onKeyInput(InputEvent.Key event) {
        if (event.getAction() != GLFW.GLFW_PRESS) return;
        if (event.getKey() == GLFW.GLFW_KEY_LEFT_ALT) {
            if (Minecraft.getInstance().level instanceof ClientLevel level && Minecraft.getInstance().player instanceof LocalPlayer player) {
                VfxDemos.demoKeyframesAndEasings(level, player);
            }
        }
    }
}
