# Radial Keybind Menu

A mod that adds a radial menu system for triggering keybinds without needing a free key for each one. Designed for heavily modded packs (e.g. Create, Botania, etc.) where you run out of keyboard space.

Fabric support covers Minecraft 1.20.4 through 26.2, and NeoForge support covers 1.21.1 through 26.2 (see [Compatibility](#compatibility) below). A legacy Forge port (1.8–1.20.1) is planned — not available yet.

This repo is a monorepo: Fabric lives in [`fabric/`](fabric/), NeoForge lives in [`neoforge/`](neoforge/), Forge will live in `forge/` once it's added.

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

Built with [Stonecutter](https://stonecutter.kikugie.dev) — one source tree targeting Minecraft 1.20.4 through 26.2. Grab the jar matching your Minecraft version from [Modrinth](https://modrinth.com/mod/simpler-keybinds).

| Minecraft version      | Fabric API | Fabric Loader |
| ---------------------- | ---------- | ------------- |
| 1.20.4                 | 0.97.3+    | 0.19.3+       |
| 1.21.5                 | 0.128.2+   | 0.19.3+       |
| 1.21.6                 | 0.128.2+   | 0.19.3+       |
| 1.21.8                 | 0.136.1+   | 0.19.3+       |
| 1.21.9                 | 0.134.1+   | 0.19.3+       |
| 1.21.11                | 0.141.5+   | 0.18.4+       |
| 26.1 / 26.1.1 / 26.1.2 | 0.155.2+   | 0.19.3+       |
| 26.2                   | 0.155.2+   | 0.19.3+       |

Not supported: anything before 1.20.4, or 26.3+ (Mojang replaced GLFW with SDL3 that version, which needs its own rework).

Requires Java 21.

### NeoForge

Also built with Stonecutter — one source tree targeting Minecraft 1.21.1 through 26.2.

| Minecraft version | NeoForge |
| ------------------ | -------- |
| 1.21.1              | 21.1.99  |
| 1.21.5              | 21.5.98  |
| 1.21.8              | 21.8.9   |
| 1.21.11             | 21.11.44 |
| 26.1                | 26.1.2.84 |
| 26.2                | 26.2.0.28-beta |

Not supported: 1.20.2–1.20.4 (NeoForge's earliest builds don't publish artifact metadata the current tooling can resolve), or 26.3+ (same SDL3 cutoff as Fabric).

Requires Java 21 for 1.21.x, Java 25 for 26.1+.

### Forge

A separate legacy Forge port (1.8–1.20.1) is planned, not available yet. This will be a distinct codebase from the Fabric/NeoForge one, since Forge's API and tooling changed too much across that range to share source.
