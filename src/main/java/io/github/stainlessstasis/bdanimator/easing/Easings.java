package io.github.stainlessstasis.bdanimator.easing;

import io.github.stainlessstasis.bdanimator.BDAnimator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.Optional;
import java.util.function.Supplier;

import static io.github.stainlessstasis.bdanimator.easing.EasingConstants.*;

@EventBusSubscriber
public class Easings {
    public static final ResourceKey<Registry<Easing>> EASING_REGISTRY_KEY = ResourceKey.createRegistryKey(BDAnimator.id("easing"));
    public static final Registry<Easing> EASING_REGISTRY = new RegistryBuilder<>(EASING_REGISTRY_KEY)
            .defaultKey(BDAnimator.id("linear"))
            .create();
    public static final DeferredRegister<Easing> EASINGS = DeferredRegister.create(EASING_REGISTRY, BDAnimator.MODID);

    public static Supplier<Easing> random(RandomSource random) {
        Optional<Easing> entry = EASING_REGISTRY.getRandom(random).map(net.minecraft.core.Holder::value);
        return () -> entry.orElseGet(LINEAR);
    }

    private static Supplier<Easing> register(String id, Easing.EasingFunction formula) {
        return EASINGS.register(id, () -> new Easing(formula));
    }

    // Credit --- Easing formulas from: https://easings.net/
    public static final Easing STATIC_LINEAR = new Easing(t -> t);
    public static final Supplier<Easing> LINEAR = register("linear", (t -> t));
    public static final Supplier<Easing> EASE_IN_SINE = register("ease_in_sine", t -> (float) (1 - Math.cos((t * Math.PI) / 2)));
    public static final Supplier<Easing> EASE_OUT_SINE = register("ease_out_sine", t -> (float) Math.sin((t * Math.PI) / 2));
    public static final Supplier<Easing> EASE_IN_OUT_SINE = register("ease_in_out_sine", t -> (float) (-(Math.cos(Math.PI * t) - 1) / 2));
    public static final Supplier<Easing> EASE_IN_QUAD = register("ease_in_quad", t -> t * t);
    public static final Supplier<Easing> EASE_OUT_QUAD = register("ease_out_quad", t -> 1 - (1 - t) * (1 - t));
    public static final Supplier<Easing> EASE_IN_OUT_QUAD = register("ease_in_out_quad", t -> t < 0.5 ? 2 * t * t : (float) (1 - Math.pow(-2 * t + 2, 2) / 2));
    public static final Supplier<Easing> EASE_IN_CUBIC = register("ease_in_cubic", t -> t * t * t);
    public static final Supplier<Easing> EASE_OUT_CUBIC = register("ease_out_cubic", t -> (float) (1 - Math.pow(1 - t, 3)));
    public static final Supplier<Easing> EASE_IN_OUT_CUBIC = register("ease_in_out_cubic", t -> t < 0.5 ? 4 * t * t * t : (float) (1 - Math.pow(-2 * t + 2, 3) / 2));
    public static final Supplier<Easing> EASE_IN_QUART = register("ease_in_quart", t -> t * t * t * t);
    public static final Supplier<Easing> EASE_OUT_QUART = register("ease_out_quart", t -> (float) (1 - Math.pow(1 - t, 4)));
    public static final Supplier<Easing> EASE_IN_OUT_QUART = register("ease_in_out_quart", t -> t < 0.5 ? 8 * t * t * t * t : (float) (1 - Math.pow(-2 * t + 2, 4) / 2));
    public static final Supplier<Easing> EASE_IN_QUINT = register("ease_in_quint", t -> t * t * t * t * t);
    public static final Supplier<Easing> EASE_OUT_QUINT = register("ease_out_quint", t -> (float) (1 - Math.pow(1 - t, 5)));
    public static final Supplier<Easing> EASE_IN_OUT_QUINT = register("ease_in_out_quint", t -> t < 0.5 ? 16 * t * t * t * t * t : (float) (1 - Math.pow(-2 * t + 2, 5) / 2));
    public static final Supplier<Easing> EASE_IN_EXPO = register("ease_in_expo", t -> t == 0 ? 0 : (float) Math.pow(2, 10 * t - 10));
    public static final Supplier<Easing> EASE_OUT_EXPO = register("ease_out_expo", t -> t == 1 ? 1 : (float) (1 - Math.pow(2, -10 * t)));
    public static final Supplier<Easing> EASE_IN_OUT_EXPO = register("ease_in_out_expo", t -> t == 0 ? 0 : (float) (t == 1 ? 1 : t < 0.5 ? Math.pow(2, 20 * t - 10) / 2 : (2 - Math.pow(2, -20 * t + 10)) / 2));
    public static final Supplier<Easing> EASE_IN_CIRC = register("ease_in_circ", t -> (float) (1 - Math.sqrt(1 - Math.pow(t, 2))));
    public static final Supplier<Easing> EASE_OUT_CIRC = register("ease_out_circ", t -> (float) Math.sqrt(1 - Math.pow(t - 1, 2)));
    public static final Supplier<Easing> EASE_IN_OUT_CIRC = register("ease_in_out_circ", t -> (float) (t < 0.5 ? (1 - Math.sqrt(1 - Math.pow(2 * t, 2))) / 2 : (Math.sqrt(1 - Math.pow(-2 * t + 2, 2)) + 1) / 2));
    public static final Supplier<Easing> EASE_IN_BACK = register("ease_in_back", t -> C3 * t * t * t - C1 * t * t);
    public static final Supplier<Easing> EASE_OUT_BACK = register("ease_out_back", t -> (float) (1 + C3 * Math.pow(t - 1, 3) + C1 * Math.pow(t - 1, 2)));
    public static final Supplier<Easing> EASE_IN_OUT_BACK = register("ease_in_out_back", t -> (float) (t < 0.5 ? (Math.pow(2 * t, 2) * ((C2 + 1) * 2 * t - C2)) / 2 : (Math.pow(2 * t - 2, 2) * ((C2 + 1) * (t * 2 - 2) + C2) + 2) / 2));
    public static final Supplier<Easing> EASE_IN_ELASTIC = register("ease_in_elastic", t -> t == 0 ? 0 : (float) (t == 1 ? 1 : -Math.pow(2, 10 * t - 10) * Math.sin((t * 10 - 10.75) * C4)));
    public static final Supplier<Easing> EASE_OUT_ELASTIC = register("ease_out_elastic", t -> t == 0 ? 0 : (float) (t == 1 ? 1 : Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * C4) + 1));
    public static final Supplier<Easing> EASE_IN_OUT_ELASTIC = register("ease_in_out_elastic", t -> t == 0 ? 0 : (float) (t == 1 ? 1 : t < 0.5 ? -(Math.pow(2, 20 * t - 10) * Math.sin((20 * t - 11.125) * C5)) / 2 : (Math.pow(2, -20 * t + 10) * Math.sin((20 * t - 11.125) * C5)) / 2 + 1));
    public static final Supplier<Easing> EASE_OUT_BOUNCE = register("ease_out_bounce", t -> {
        if (t < 1 / D1) { return N1 * t * t; }
        else if (t < 2f / D1) { return (float) (N1 * (t -= 1.5f / D1) * t + 0.75); }
        else if (t < 2.5f / D1) { return (float) (N1 * (t -= 2.25f / D1) * t + 0.9375); }
        else { return N1 * (t -= 2.625f / D1) * t + 0.984375f; }
    });
    public static final Supplier<Easing> EASE_IN_BOUNCE = register("ease_in_bounce", t -> 1 - EASE_OUT_BOUNCE.get().apply(1 - t));
    public static final Supplier<Easing> EASE_IN_OUT_BOUNCE = register("ease_in_out_bounce", t -> t < 0.5 ? (1 - EASE_OUT_BOUNCE.get().apply(1 - 2 * t)) / 2 : (1 + EASE_OUT_BOUNCE.get().apply(2 * t - 1)) / 2);

//    @SubscribeEvent
//    public static void register(RegisterEvent event) {
//
//    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(EASING_REGISTRY);
    }
}
