# VoxelFX

Animate client-side block & item display entities with keyframes, easing functions, and much more!<br>
Make cool VFX like this:
<video src="https://github.com/user-attachments/assets/272ee22a-42d3-4741-9064-bcfc3675386b" autoplay loop muted playsinline width="100%"></video>

# Features

### Animated Block & Item Displays
Animate block and item displays separately or together.

<details>
<summary><b>View Demo</b></summary>
<br>
<img width="1080" height="608" alt="blocks_and_items" src="https://github.com/user-attachments/assets/38d90e3e-2af5-49f4-81d6-7a187c82585c" />
</details>

### Keyframes & Easings
Use keyframes with easings to make super smooth animations for any movement you want. Reference [easings.net](https://easings.net/) to visualize easings, or test them out in game.

<details>
<summary><b>View Demo</b></summary>
<br>
<img width="1080" height="608" alt="keyframes_and_easings" src="https://github.com/user-attachments/assets/b9df9f06-3092-47c2-955e-5bbe10c2a41f"/>
</details>

### Overlays
Tint any block (including weird shapes like lecterns) any color. Intensity controls how opaque the tint is, and overlays support keyframes just like every other property.<br>
*Does not support items.*

<details>
<summary><b>View Demo</b></summary>
<br>
<img width="1080" height="608" alt="overlay" src="https://github.com/user-attachments/assets/11c5b6d9-0930-4702-b50a-a44834ecd323" />
</details>

### Animation Queueing & Inheritance
Queue animations to play one after another, or override the current animation immediately. Animations can inherit properties from whichever animation played before them to reduce boilerplate.

<details>
<summary><b>View Demo</b></summary>
<br>
<img width="1080" height="608" alt="queue_and_inheritance" src="https://github.com/user-attachments/assets/e5c44541-d7a7-4399-9f2a-f805eea9a4a8" />
</details>

### Looping & Callbacks
Loop animations a fixed number of times or infinitely. Hook into callbacks to trigger logic at any point in an animation's lifecycle.

<details>
<summary><b>View Demo</b></summary>
<br>
<img width="1080" height="608" alt="loops_and_callbacks" src="https://github.com/user-attachments/assets/dced5158-a772-4660-96a9-eb994bf59a54" />
</details>

### Per-Frame Modifiers
Apply custom per-frame modifiers on top of the keyframed values, for effects like wobble, sway, or pulsing that aren't easily expressed as keyframes alone.<br>
*Note: These should be scaled by the context's `interpolatedTicks` so everything stays consistent with any framerate.*

<details>
<summary><b>View Demo</b></summary>
<br>
<img width="1080" height="608" alt="per_tick_modifiers" src="https://github.com/user-attachments/assets/e05c1e3b-d92d-4bc9-b318-994a9e74ca42" />
</details>

### Entity Binding
Bind animations to follow entities with an optional offset. Can operate in either global or local space.<br>
*Showcased in above Nova Bomb video.*

### Builder API
A fluent builder that lets you construct animations declaratively, chaining translation, scale, rotation, overlay, and block/item channels with minimal boilerplate.

# Limitations
NeoForge 26.1.2+<br>
This mod is client-only, meaning all effects will have to be triggered with your own code. For instance, sending a packet from server -> client, telling the client to play that animation.<br>
Animations are designed to be purely visual - do not try to alter the game state using them.<br><br>
Also, due to the clent-only nature of the mod, display entities do not natively support being saved to the world/persisting animations. You'd have to write your own system for that.

# General Use:
VoxelFX has some commands that can be run with `/voxelfx`. Most notably, the `demo` command can be used to run built-in demo animations. 
Also important - the `clear` command will clear out the entity cache, removing all VFX entities in case anything breaks.<br>
You can also pause, resume, stop, or set the play speed of all VFX entities.
*This is not a global state - it applies only to the entities that exist when the command is run.*

# Getting Started with VoxelFX
## 1. Add the dependency

VoxelFX is published via [JitPack](https://jitpack.io). Add the repository and dependency to your `build.gradle`:

```groovy
repositories {
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.StainlessStasis:VoxelFX:MOD_VERSION-MINECRAFT_VERSION'
}
```
Replace the placeholders with their respective values. E.g. `1.0.0-26.1.2`.

## 2. Building An Animation

Call `VfxAnimationBuilder.create()` to get a new builder, then chain methods to set properties of the animation. For example, to create an infinitely looping, spinning block:
```java
VfxAnimation animation = VfxAnimationBuilder.create()
  .blockState(Blocks.DIAMOND_BLOCK.defaultBlockState(), builder -> {})
  .scale(0.5f, builder -> {})
  .translation(builder -> builder
          .addKeyframe(0.5f, 0, 4, 0, EasingType.LINEAR) // move up 4 blocks
          .addKeyframe(1f, 0, 0, 0, EasingType.LINEAR)) // move back down
  .loopInfinite()
  .build(80); // animation length defined in ticks
```

For more examples, see `VfxDemos` and `NovaBombDemo`.

## 3. Playing Animations

Animations are played from a `VfxEntity`, which is obtained via `VfxEntity.create`. Once you create the entity, you don't need to set its position or add it to the level. 

Again, **do not add it to the client level!** VFX entities are automatically added to a cache (`VfxEntityCache`) and ticked/rendered there. Adding the entity to the level can break things or even cause crashes.

To play an animation, simply call `playAnimation`. You can also use `playAnimationWithOffset` to start it mid-animation.

Animations can also be queued via `playOrQueueAnimation`. Play two animations in sequence like so:
```java
VfxAnimation animation1 = VfxAnimationBuilder.create()
                .blockState(Blocks.DIAMOND_BLOCK.defaultBlockState(), builder -> {})
                .scale(0.5f, builder -> {})
                .translation(builder -> builder
                        .addKeyframe(0.5f, 0, 4, 0,  EasingType.LINEAR)
                        .addKeyframe(1f, 0, 0, 0,  EasingType.LINEAR))
                // removed the infinite loop here
                .build(80);
        VfxAnimation animation2 = VfxAnimationBuilder.create()
                .blockState(Blocks.NETHERITE_BLOCK.defaultBlockState(), builder -> {})
                .scale(0.5f, builder -> builder
                        .addKeyframe(0.5f, 1f, EasingType.LINEAR))
                .translation(builder -> builder
                        .addKeyframe(0.5f, 0, 4, 0, EasingType.LINEAR)
                        .addKeyframe(1f, 0, 0, 0, EasingType.LINEAR))
                .build(80);
        VfxEntity vfxEntity = VfxEntity.create(level, pos);
        vfxEntity.playOrQueueAnimation(animation1);
        vfxEntity.playOrQueueAnimation(animation2);
```

## 4. Advanced

### Looping
`loop(n)` plays the animation `n + 1` times total, `loopInfinite()` loops forever.
Combine with `onLoop` for periodic effects, like a pulsing effect that plays a sound.

### Callbacks
Hook into the animation lifecycle with `onStart`, `onEnd`, `onLoop`, and
`onKeyframeReached(time, callback)`. Useful for triggering sounds, particles, or
other logic at specific points:

```java
.onKeyframeReached(0.5f, e -> level.playLocalSound(...))
.onLoop(e -> doSomething())
.onEnd(e -> doSomethingElse())
```

### Playback Control
Animations can be paused, resumed, reversed, or sped up/slowed down at any time:

```java
vfxEntity.pauseAnimation();
vfxEntity.resumeAnimation();
vfxEntity.setPlaySpeed(-1f); // reverses the animation
vfxEntity.setPlaySpeed(2f);
```

### Inheritance
When animations are queued, the next animation can inherit properties from wherever the
previous one left off, avoiding hardcoded start values and snapping. Just call the
relevant `inherit*` method instead of declaring a starting value:

```java
VfxAnimation squish = VfxAnimationBuilder.create()
        .inheritTranslation()
        .inheritBlockState()
        .scale(1f, s -> s
                .addKeyframe(0.3f, 2f, 0.2f, 2f, EasingType.OUT_EXPO)
                .addKeyframe(1f, 1f, 1f, 1f, EasingType.OUT_BOUNCE))
        .build(30);
```

Available for translation, scale, rotation, overlay color, overlay intensity, block state,
and item stack. You can also use `inheritAll()`.

### Per-Frame Modifiers
For motion that isn't easily expressed as keyframes - orbiting, wobble, constant spin -
use `onFrame*`. These run *after* the keyframed value is evaluated each frame,
so they combine with whatever the channel is already doing:

```java
.onFrameTranslation((translation, ctx) -> {
    float angle = ctx.interpolatedTicks() * 0.1f;
    translation.x += (float) Math.cos(angle) * radius;
    translation.z += (float) Math.sin(angle) * radius;
})
```

Always scale by `interpolatedTicks()`, not a raw tick counter - this keeps motion
speed consistent regardless of framerate.

### Entity Binding
Bind an entity to follow another entity, optionally with an offset in local or global
space:

```java
VfxEntity trail = VfxEntity.createBoundTo(level, projectile, new Vector3f(), /* localSpace */ true);
trail.setOnBoundEntityRemoved(VfxEntity::stopAnimations);
```

### Snapshots
You can manually capture an entity's current rendered state. Useful when building a
follow-up animation that needs to branch off from wherever the entity currently is,
outside of the normal queue/inheritance flow:

```java
VfxSnapshot snap = entity.captureCurrentSnapshot();
```

## Things To Consider
- **Culling:** VFX entities all have culling disabled by default. You can reenable
  culling by calling `setAffectedByCulling` on the entity, but be warned - it may not
  be desirable.

- **Persistence:** VFX entities despawn automatically once their animation (and queue)
  finishes. Use `setInfinitePersist(true)` for entities you intend to control manually
  forever (e.g. bound to a projectile, or polled in `onTick`), and remember to
  `discard()` or `stopAnimations()` them yourself when done. You can also set the ticks to persist with `setTicksToPersist`.

- **Reversing with a queue:** reversing the *currently playing* animation works
  fine, but reversing back through a queue is not supported since inheritance only flows forward.
   If you need a "play this in reverse" sequence,
  build the reverse stages explicitly rather than trying to reverse a forward queue.

- **`onTick` vs per-frame modifiers:** `entity.setOnTick(...)` runs every tick
  regardless of whether an animation is playing, and is the right place for logic like
  polling a bound entity's state. Per-frame *modifiers* (`onFrameTranslation`, etc.) are
  scoped to a single animation and only run while that animation is active.
- **Overlay limitations:** overlays don't currently support items.
