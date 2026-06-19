package io.github.stainlessstasis.bdanimator.easing;

import net.minecraft.util.RandomSource;

import java.util.*;

import static io.github.stainlessstasis.bdanimator.easing.EasingConstants.*;

/**
 * Easings are functions which can drastically affect the way an animation looks, giving it a different feeling.<br>
 * All easings from <a href="https://easings.net/">easings.net</a> are built in, but you
 * can also call {@link #register(String, Easing.EasingFunction)} to register your own easing function.
 * This isn't required, but it will make your easing accessible via map lookup, if you want to get a random easing, for example.
 *
 * <p>Each easing (except linear) has 3 types:
 *  <ul>
 *  <li><b>IN</b> - starts slow, accelerates toward the end</li>
 *  <li><b>OUT</b> - starts fast, decelerates toward the end</li>
 *  <li><b>IN_OUT</b> - slow at both ends, fast in the middle</li>
 *  </ul>
 *
 * <p>Unsure which easing to use? Try matching the easing with the "feeling" of your animation:
 * <ul>
 *   <li><b>LINEAR</b> - constant speed, no acceleration. Feels robotic and unnatural for physical objects.
 *   Best for things that should track a value mechanically, like a constant spin.</li>
 *
 *   <li><b>SINE</b> - the gentlest curve, barely distinguishable from linear at a glance.
 *   Feels organic and calm, like breathing or floating. Good for looping idle animations and subtle ambient motion.</li>
 *
 *   <li><b>QUAD</b> - a noticeable but natural-feeling curve. Feels responsive and grounded, like everyday physical objects.
 *   When in doubt, start here.</li>
 *
 *   <li><b>CUBIC</b> - more pronounced than QUAD. Feels deliberate and weighty, like something with real mass starting or stopping.
 *   Good for larger movements that should feel purposeful.</li>
 *
 *   <li><b>QUART</b> - aggressive curve, especially at the extremes. Feels snappy and high-energy.
 *   OUT_QUART in particular is great for explosions and impacts that need to feel violent.</li>
 *
 *   <li><b>QUINT</b> - very aggressive. Almost all motion is compressed into a tiny window.
 *   Feels explosive and extreme. Use sparingly for maximum dramatic effect.</li>
 *
 *   <li><b>EXPO</b> - the most extreme of the "smooth" curves. IN_EXPO barely moves for most of its duration then launches suddenly.
 *   OUT_EXPO does the reverse - nearly instant movement that fades into stillness.</li>
 *
 *   <li><b>CIRC</b> - follows a circular arc. Feels similar to EXPO but more sharp and precise.
 *   Good for mechanical effects.</li>
 *
 *   <li><b>BACK</b> - overshoots the target slightly before settling. Feels springy and alive, like something with momentum that corrects itself.
 *   Great for objects that should feel like they have personality.</li>
 *
 *   <li><b>ELASTIC</b> - overshoots dramatically and oscillates like a spring before settling.
 *   Feels bouncy and exaggerated. Use for anything that should feel rubbery and alive. Can be overwhelming if overused.</li>
 *
 *   <li><b>BOUNCE</b> - simulates a physical bounce, decelerating in discrete steps.
 *   OUT_BOUNCE feels like dropping a ball and watching it settle. Feels playful and physical.
 *   IN_BOUNCE is rarely useful on its own but can work well when paired with other animations in a queue.</li>
 * </ul>
 */
public class Easings {
    private static final Map<String, Easing> REGISTRY = new LinkedHashMap<>();

    public static Easing register(String id, Easing.EasingFunction formula) {
        Easing easing = new Easing(formula);
        REGISTRY.put(id, easing);
        return easing;
    }

    public static Easing get(String id) {
        return REGISTRY.getOrDefault(id, LINEAR);
    }

    public static Easing random(RandomSource random) {
        List<Easing> values = new ArrayList<>(REGISTRY.values());
        return values.get(random.nextInt(values.size()));
    }

    public static Map<String, Easing> getAll() {
        return Collections.unmodifiableMap(REGISTRY);
    }

    public static final Easing LINEAR = register("linear", t -> t);
    public static final Easing EASE_IN_SINE = register("ease_in_sine", t -> (float)(1 - Math.cos((t * Math.PI) / 2)));
    public static final Easing EASE_OUT_SINE = register("ease_out_sine", t -> (float) Math.sin((t * Math.PI) / 2));
    public static final Easing EASE_IN_OUT_SINE = register("ease_in_out_sine", t -> (float)(-(Math.cos(Math.PI * t) - 1) / 2));
    public static final Easing EASE_IN_QUAD = register("ease_in_quad", t -> t * t);
    public static final Easing EASE_OUT_QUAD = register("ease_out_quad", t -> 1 - (1 - t) * (1 - t));
    public static final Easing EASE_IN_OUT_QUAD = register("ease_in_out_quad", t -> t < 0.5 ? 2 * t * t : (float)(1 - Math.pow(-2 * t + 2, 2) / 2));
    public static final Easing EASE_IN_CUBIC = register("ease_in_cubic", t -> t * t * t);
    public static final Easing EASE_OUT_CUBIC = register("ease_out_cubic", t -> (float)(1 - Math.pow(1 - t, 3)));
    public static final Easing EASE_IN_OUT_CUBIC = register("ease_in_out_cubic", t -> t < 0.5 ? 4 * t * t * t : (float)(1 - Math.pow(-2 * t + 2, 3) / 2));
    public static final Easing EASE_IN_QUART = register("ease_in_quart", t -> t * t * t * t);
    public static final Easing EASE_OUT_QUART = register("ease_out_quart", t -> (float)(1 - Math.pow(1 - t, 4)));
    public static final Easing EASE_IN_OUT_QUART = register("ease_in_out_quart", t -> t < 0.5 ? 8 * t * t * t * t : (float)(1 - Math.pow(-2 * t + 2, 4) / 2));
    public static final Easing EASE_IN_QUINT = register("ease_in_quint", t -> t * t * t * t * t);
    public static final Easing EASE_OUT_QUINT = register("ease_out_quint", t -> (float)(1 - Math.pow(1 - t, 5)));
    public static final Easing EASE_IN_OUT_QUINT = register("ease_in_out_quint", t -> t < 0.5 ? 16 * t * t * t * t * t : (float)(1 - Math.pow(-2 * t + 2, 5) / 2));
    public static final Easing EASE_IN_EXPO = register("ease_in_expo", t -> t == 0 ? 0 : (float) Math.pow(2, 10 * t - 10));
    public static final Easing EASE_OUT_EXPO = register("ease_out_expo", t -> t == 1 ? 1 : (float)(1 - Math.pow(2, -10 * t)));
    public static final Easing EASE_IN_OUT_EXPO = register("ease_in_out_expo", t -> t == 0 ? 0 : (float)(t == 1 ? 1 : t < 0.5 ? Math.pow(2, 20 * t - 10) / 2 : (2 - Math.pow(2, -20 * t + 10)) / 2));
    public static final Easing EASE_IN_CIRC = register("ease_in_circ", t -> (float)(1 - Math.sqrt(1 - Math.pow(t, 2))));
    public static final Easing EASE_OUT_CIRC = register("ease_out_circ", t -> (float) Math.sqrt(1 - Math.pow(t - 1, 2)));
    public static final Easing EASE_IN_OUT_CIRC = register("ease_in_out_circ", t -> (float)(t < 0.5 ? (1 - Math.sqrt(1 - Math.pow(2 * t, 2))) / 2 : (Math.sqrt(1 - Math.pow(-2 * t + 2, 2)) + 1) / 2));
    public static final Easing EASE_IN_BACK = register("ease_in_back", t -> C3 * t * t * t - C1 * t * t);
    public static final Easing EASE_OUT_BACK = register("ease_out_back", t -> (float)(1 + C3 * Math.pow(t - 1, 3) + C1 * Math.pow(t - 1, 2)));
    public static final Easing EASE_IN_OUT_BACK = register("ease_in_out_back", t -> (float)(t < 0.5 ? (Math.pow(2 * t, 2) * ((C2 + 1) * 2 * t - C2)) / 2 : (Math.pow(2 * t - 2, 2) * ((C2 + 1) * (t * 2 - 2) + C2) + 2) / 2));
    public static final Easing EASE_IN_ELASTIC = register("ease_in_elastic", t -> t == 0 ? 0 : (float)(t == 1 ? 1 : -Math.pow(2, 10 * t - 10) * Math.sin((t * 10 - 10.75) * C4)));
    public static final Easing EASE_OUT_ELASTIC = register("ease_out_elastic", t -> t == 0 ? 0 : (float)(t == 1 ? 1 : Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * C4) + 1));
    public static final Easing EASE_IN_OUT_ELASTIC = register("ease_in_out_elastic", t -> t == 0 ? 0 : (float)(t == 1 ? 1 : t < 0.5 ? -(Math.pow(2, 20 * t - 10) * Math.sin((20 * t - 11.125) * C5)) / 2 : (Math.pow(2, -20 * t + 10) * Math.sin((20 * t - 11.125) * C5)) / 2 + 1));
    public static final Easing EASE_OUT_BOUNCE = register("ease_out_bounce", t -> {
        if (t < 1 / D1) return N1 * t * t;
        else if (t < 2f / D1) return (float)(N1 * (t -= 1.5f / D1) * t + 0.75);
        else if (t < 2.5f / D1) return (float)(N1 * (t -= 2.25f / D1) * t + 0.9375);
        else return N1 * (t -= 2.625f / D1) * t + 0.984375f;
    });
    public static final Easing EASE_IN_BOUNCE = register("ease_in_bounce", t -> 1 - EASE_OUT_BOUNCE.apply(1 - t));
    public static final Easing EASE_IN_OUT_BOUNCE = register("ease_in_out_bounce", t -> t < 0.5 ? (1 - EASE_OUT_BOUNCE.apply(1 - 2 * t)) / 2 : (1 + EASE_OUT_BOUNCE.apply(2 * t - 1)) / 2);
}