# Plummet tasks

One task per run, committed directly to main. Author quaag, plain commit messages, no code comments, no emojis or em dashes.

## Build rule

Fabric 1.21.11, yarn mappings, Java 21. If a JDK 21 is installed, ./gradlew build must pass before any commit. Never commit code that fails the build. If no JDK is available and it cannot be installed, keep the change minimal and note the task as unverified in TASKS.md instead of checking it off.

## Design direction

Keep the module architecture: command, bot, behavior, gamemode, scenario, stats packages under dev.quaag.plummet. Small focused changes, match the existing code style, no new dependencies beyond Fabric API.

## Tasks

- [ ] Update the README to reflect the shipped commands and current status.
      commit: update readme

## Done

- [x] Session stats: a stats module counting hits on the dummy and the max recorded fall distance hit, shown by /plummet stats.
      commit: session stats
- [x] Height drill: /plummet drill height starts a drill that tracks the highest Y gained after each wind charge use and reports the number on landing.
      commit: height drill
- [x] Practice arena: /plummet arena builds a flat 24 by 24 smooth stone platform centered on the player at their current Y minus 1, only replacing air.
      commit: practice arena
- [x] Hit feedback: when a player damages the dummy, show damage dealt and the attacker fall distance at the moment of the hit in the action bar.
      commit: hit feedback
- [x] Bot options: optional health argument on /plummet bot spawn that sets the dummy max health attribute, clamped 1 to 100.
      commit: bot options
- [x] Bot manager: a PracticeBot class in the bot package that spawns one tracked dummy (zombie with a custom name, no despawn, no daylight burning, no drops) and a /plummet bot remove subcommand that despawns it. Replace the raw summon in BotCommand.
      commit: bot spawn and remove
- [x] Build setup: if java 21 is missing run brew install --cask temurin@21. Run ./gradlew build and fix any compile errors until green, checking calls against 1.21.11 yarn mappings.
      commit: fix build
