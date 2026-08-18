# SplinecartUnlocked

**SplinecartUnlocked** is a continuation fork of **[Splinecart](https://github.com/FoundationGames/Splinecart)** by **FoundationGames**, ported to and rebuilt for **Minecraft 26.2** (Fabric).

Minecarts that ride smooth, curving tracks. SplinecartUnlocked lets you lay tracks that gently bank, dip, twist, and climb - built with proper cubic-spline interpolation instead of the usual straights and 90° turns.

## Features

- **Curved tracks** - build smooth spline tracks connecting two Track Ties blocks, with natural curves and banks.
- **Automatic minecart support** - standard minecarts travel along tracks forwards and backwards, and stop cleanly.
- **Compatible carts** - any entity in the `splinecartunlocked:carts` entity tag can ride the tracks.
- **Three track types:**
  - **Track** - standard track with natural friction.
  - **Chain Drive Track** - powered track that pulls minecarts along at a constant speed, even uphill.
  - **Magnetic Track** - responds to redstone; signal strength accelerates or slows minecarts.
- **Inversions & banks** - riders are positioned correctly through loops and never suffocate in supporting blocks on inverted or sideways sections.
- **No fall damage** - riders survive leaving the track without fall damage.
- **Smooth physics** - no more stuttering at segment boundaries; carts respond appropriately to slopes.
- **Survival-friendly** - everything is craftable with hint tooltips and a HUD showing track placement status.

## Getting Started

1. Craft and place **Track Ties**. Right-click a placed tie to rotate it.
2. Craft a **Track** and right-click two ties in sequence to draw the spline track between them (use it on any non-track block to cancel the first selection).
3. Push a minecart or compatible cart onto the track and let it ride.
4. Use **Chain Drive Track** to pull carts, or power a **Magnetic Track** with redstone (signal strength controls speed) to speed up or slow down carts.

### Crafting

| Item | Recipe |
| --- | --- |
| Track Ties x8 | 1 stick in the center, wooden slabs around the edges |
| Track x4 | rail with an iron block above and below |
| Chain Drive Track x4 | activator rail, iron chains on the sides, iron blocks above and below |
| Magnetic Track x4 | powered rail, copper blocks on the sides, iron blocks above and below |

## Configuration

Client-side options, changeable in-game:

```
/splinecartc config <option> [value]
```

| Option | Default | Description |
| --- | --- | --- |
| `rotate_camera` | `true` | Follow the track's rotation with the camera while riding. Set to `false` for users prone to motion sickness. |
| `vbos` | `false` | Render tracks using vertex buffer objects. Faster, but can cause visual artifacts. |
| `track_resolution` | `3` | Number of segments tracks are cut into when rendering (1-16). Higher = smoother, lower performance. |
| `track_render_distance` | `8` | Distance (in chunks) at which tracks stop rendering (4-32). |

## Requirements

- Minecraft 26.2
- Fabric Loader 0.19.3+
- Fabric API

## Credits & Disclaimer

This is **only a fork / continuation** of the original **[Splinecart](https://github.com/FoundationGames/Splinecart)** mod. All gameplay design, code, art, and assets were originally created by **FoundationGames** - all credit goes to them. SplinecartUnlocked exists to keep the mod alive by porting it to the latest version of the game.

If FoundationGames would like this fork to be taken down, please contact the maintainer and it will be removed immediately: **godlycowcow@gmail.com**
