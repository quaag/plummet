# Plummet

A mace PvP training mod for Minecraft. Spawn a practice dummy, build an arena, and run drills in singleplayer.

Built for Fabric 1.21.11.

## Status

Working. Bot spawning, hit feedback, a height drill, an arena builder, and session stats all ship. The dummy is a tracked zombie with no despawn and no drops, not a real bot AI yet. Combat AI is still to come.

## Commands

All commands are under `/plummet` and require a player.

| Command | What it does |
| --- | --- |
| `/plummet` | Prints a hint. |
| `/plummet bot spawn [health]` | Spawns a practice dummy 3 blocks in front of you, facing you. Optional health from 1 to 100, default 20. Replaces any existing dummy. |
| `/plummet bot remove` | Despawns the tracked dummy. |
| `/plummet arena` | Builds a 24 by 24 smooth stone platform centered on you at your Y minus 1. Only replaces air. |
| `/plummet drill height` | Starts the height drill. Tracks the highest Y gained after each wind charge and reports the gain on landing. |
| `/plummet drill stop` | Stops the running drill. |
| `/plummet stats` | Shows dummy hits and best fall distance for the session. |
| `/plummet stats reset` | Clears your session stats. |

Hitting the dummy shows the damage dealt and your fall distance at the moment of the hit in the action bar.

## Build

Requires JDK 21.

```
./gradlew build
```

To run the game:

```
./gradlew runClient
```

Then open a world and run `/plummet bot spawn`.

## Versions

- Minecraft 1.21.11
- Yarn 1.21.11+build.6
- Fabric Loader 0.19.3
- Fabric API 0.141.4+1.21.11

## License

MIT
