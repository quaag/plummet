# Plummet tasks

One task per run, committed directly to main. Author quaag, plain commit messages, no code comments, no emojis or em dashes.

## Build rule

Fabric 1.21.11, yarn mappings, Java 21. If a JDK 21 is installed, ./gradlew build must pass before any commit. Never commit code that fails the build. If no JDK is available and it cannot be installed, keep the change minimal and note the task as unverified in TASKS.md instead of checking it off.

## Design direction

Keep the module architecture: command, bot, behavior, gamemode, scenario, stats packages under dev.quaag.plummet. Small focused changes, match the existing code style, no new dependencies beyond Fabric API.

The mod is a MACE training tool. Prefer work that makes it measurably better at that specific job over generic features.

## Tasks

- [ ] Bump mod_version to 0.2.0 in gradle.properties and update the README status and command table to match everything shipped.
      commit: bump to 0.2.0

## Done

- [x] Add a test source set with plain JUnit tests for the pure logic that does not need a running game: health clamping, stats accumulation and reset, and drill state transitions. Wire the test task into the build.
      commit: add unit tests
- [x] Persist session stats across a restart by writing them into the world save directory on server stop and loading them on server start.
      commit: persist session stats
- [x] Drill difficulty: /plummet drill height with easy, normal, and hard options that set a target height threshold and report pass or fail against it on each landing.
      commit: drill difficulty
- [x] Combo drill: /plummet drill combo tracks a full wind charge launch into mace hit sequence and reports height gained, airtime in ticks, and damage dealt on the landing hit.
      commit: combo drill
- [x] Dummy behavior modes: /plummet bot mode with static and strafe options. Static clears the zombie AI goals so it stands still as a pure target, strafe keeps simple side to side movement. Default new dummies to static.
      commit: bot behavior modes
- [x] Mace awareness: detect whether the hit was made with a mace, label mace hits distinctly in the action bar feedback, and track best mace hit damage separately from other hits in SessionStats.
      commit: mace aware hit tracking
- [x] Fix fall distance capture: vanilla zeroes fallDistance during a mace attack, so reading player.fallDistance in the AFTER_DAMAGE handler can report 0. Capture the attacker fall distance in an AttackEntityCallback (which fires before damage) into a short lived per player value, and have HitFeedback and SessionStats read that captured value instead. Keep the action bar output format.
      commit: fix fall distance capture
- [x] Update the README to reflect the shipped commands and current status.
      commit: update readme
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
