# Radial Keybind Menu

A mod that adds a radial menu system for triggering keybinds without needing a free key for each one. Designed for heavily modded packs (e.g. Create, Botania, etc.) where you run out of keyboard space.

Fabric support covers Minecraft 1.20.4 through 26.2 (see [Compatibility](#compatibility) below). A Forge port is planned — not available yet.

This repo is a monorepo: Fabric lives in [`fabric/`](fabric/), Forge will live in `forge/` once it's added.

## How it works

Press the **open key** (default: `R`) to bring up a radial wheel. Move your mouse into one of the four quadrants and release `R` (or left-click) to fire that slot's assigned action. The action is triggered directly — it does not simulate a keypress, so it works even if the underlying action has no key bound.

You can configure up to **3 wheels**, each with **4 slots** (top-right, bottom-right, bottom-left, top-left).

## Controls

| Key                       | Action                                         |
| ------------------------- | ---------------------------------------------- |
| `R`                       | Open / close the radial wheel                  |
| Move mouse                | Highlight a quadrant                           |
| Release `R` or left-click | Fire the highlighted slot                      |
| `1` / `2` / `3`           | Switch between wheels while open               |
| `C`                       | Open the config screen while the wheel is open |
| `ESC`                     | Close without firing                           |

## Configuring your binds

1. Press `R` to open the wheel, then `C` to open the config screen (or bind a separate config key in Controls).
2. Click a slot (Top-Right, Bottom-Right, Bottom-Left, Top-Left) to select it.
3. A list of all registered keybinds appears on the right — includes vanilla and all mod keybinds. Click one to assign it.
4. Optionally type a **custom display name** in the text box that appears below the slot. If left blank the binding's own name is shown on the wheel.
5. Press `Del` to clear a slot's binding.
6. Press `ESC` to go back to the wheel.

Changes save automatically to `.minecraft/config/radialkeybindmenu.json`.

## Compatibility

Works with any mod that registers standard `KeyMapping` bindings (Create, Botania, Applied Energistics, etc.), since actions are dispatched directly to the keybind rather than through simulated key input.

### Fabric

Built with [Stonecutter](https://stonecutter.kikugie.dev) — one source tree targeting Minecraft 1.20.4 through 26.2. Grab the jar matching your Minecraft version from [Releases](../../releases) or Modrinth.

| Minecraft version | Fabric API | Fabric Loader |
| ------------------ | ---------- | -------------- |
| 1.20.4              | 0.97.3+    | 0.19.3+        |
| 1.21.5              | 0.128.2+   | 0.19.3+        |
| 1.21.6              | 0.128.2+   | 0.19.3+        |
| 1.21.8              | 0.136.1+   | 0.19.3+        |
| 1.21.9              | 0.134.1+   | 0.19.3+        |
| 1.21.11             | 0.141.5+   | 0.18.4+        |
| 26.1 / 26.1.1 / 26.1.2 | 0.155.2+ | 0.19.3+      |
| 26.2                | 0.155.2+   | 0.19.3+        |

Not supported: anything before 1.20.4, or 26.3+ (Mojang replaced GLFW with SDL3 that version, which needs its own rework).

Requires Java 21.

### Forge

Planned, not available yet.
