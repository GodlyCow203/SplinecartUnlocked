# SplinecartUnlocked

Minecart tracks that curve.

**SplinecartUnlocked** is a continuation fork of the original **[Splinecart](https://github.com/FoundationGames/Splinecart)** mod by **FoundationGames**, ported to and rebuilt for **Minecraft 26.2** (Fabric).

## Credits & Fork Notice

This mod is **only a fork / continuation** of the original mod, **[Splinecart](https://github.com/FoundationGames/Splinecart)**.

- All gameplay design, code, art and assets were originally created by **FoundationGames**.
- **All credit goes to FoundationGames** for the original Splinecart mod.
- SplinecartUnlocked exists to keep the mod alive and updated: it is a continuation that has been ported to the latest version of the game (26.2).

If FoundationGames would like this fork to be taken down, please contact the maintainer ( GodlyCow203 on GIthub) and it will be removed immediately:

**godlycowcow@gmail.com**

## What It Does

SplinecartUnlocked lets you build smooth, curving tracks for minecarts to ride. Instead of being limited to straight or 90° turns, tracks are fitted through a cubic spline, so you can gently bank, dip, twist and climb - and normal minecarts (and other compatible rideable carts) will follow them.

### Features

- **Curved tracks** - build smooth spline tracks that connect two points with gentle curves and banks.
- **Full minecart support** - standard minecarts automatically travel along tracks, forwards and backwards, and can stop cleanly.
- **Compatible carts** - any entity in the `splinecartunlocked:carts` entity tag can also ride the tracks.
- **Three track types:**
  - **Track** - a standard track with natural friction.
  - **Chain Drive Track** - a powered variant that pulls minecarts along at a constant speed, even uphill.
  - **Magnetic Track** - reacts to redstone power: accelerate or slow minecarts by supplying redstone signal strength.
- **Inversions & banks** - riders are correctly positioned through loops, and won't suffocate in supporting blocks on inverted or sideways sections.
- **No fall damage** - riders don't take fall damage when leaving a track.
- **Smooth physics** - velocity is interpolated so carts no longer stutter at segment boundaries and respond appropriately to slopes.
- **Survival-friendly** - all items are craftable and usable in survival, with hint tooltips.
- **Client-side visual options** - track render distance, resolution and VBO rendering, plus a camera-rotation accessibility option.

## Getting Started

1. **Place Track Ties** - craft and place `Track Ties` blocks. Right-click a placed tie to rotate it.
2. **Link two ties** - craft a `Track` and right-click two Track Ties blocks in sequence to draw a smooth spline track between them.
   - The HUD shows your current selection. Use the track item on a non-track block to cancel the first selection.
   - Adventure-mode players can't modify tracks or ties.
3. **Ride** - push a minecart (or a compatible cart) onto the track. It will follow the spline.
4. **Powered variants** - use `Chain Drive Track` to pull carts, or `Magnetic Track` and power it with redstone (signal strength controls speed) to speed up or slow down carts.

### Crafting

| Item | Recipe |
| --- | --- |
| Track Ties ×8 | 1 stick in the center, wooden slabs around the edges |
| Track ×4 | rail with an iron block above and below |
| Chain Drive Track ×4 | activator rail, iron chains on the sides, iron blocks above and below |
| Magnetic Track ×4 | powered rail, copper blocks on the sides, iron blocks above and below |

## Configuration

All options are client-side and can be changed with the in-game command:

```
/splinecartc config <option> [value]
```

Running `/splinecartc config` lists all options and their current values.

| Option | Default | Description |
| --- | --- | --- |
| `rotate_camera` | `true` | Follow the track's rotation with the camera while riding. Set to `false` for users prone to motion sickness. |
| `vbos` | `false` | Render tracks using vertex buffer objects. Faster, but can cause visual artifacts. |
| `track_resolution` | `3` | Number of segments tracks are cut into when rendering (1–16). Higher = smoother, lower performance. |
| `track_render_distance` | `8` | Distance (in chunks) at which tracks stop rendering (4–32). |

## Requirements

- Minecraft 26.2
- Fabric Loader 0.19.3+
- Fabric API

**Minecraft:** 26.2
**Fabric API:** 0.157.0+

## Building from Source

```
./gradlew build
```

The built mod jar is placed in `build/libs/`.

## License

MIT - see the original project for details. All original code and assets belong to FoundationGames.