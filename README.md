# Lucienne: Quest for Quality Education

**Course:** TMF2954 Java Programming  
**Theme:** SDG 4 - Quality Education  
**Genre:** 2D Open-World Educational RPG

## Team Members

| Name | Module | Classes | Interface |
|------|--------|---------|-----------|
| **Aezekiel** (Leader) | Player + Map + Integration | Player, Entity, GamePanel, KeyHandler, Main, AssetSetter, EventHandler, CollisionChecker, UI, UtilityTool, Sound, Tile, TileManager | Playable |
| **Lee Yun Zhan** | Learning + NPC Interaction | LearningPage, LearningManager, NPC_Piercehardt, NPC_Villager, NPC_Shopkeeper, NPC_KingLuin, NPC_Lucious, NPC_SheenaMemory | Interactable, Learnable |
| **Nathanael** | Quiz + Battle System | Question, QuizManager, MultipleChoiceQuestion, TrueFalseQuestion, FillBlankQuestion, InvalidAnswerException, MON_MemoryFragment, BOSS_Shona | QuizPlayable |
| **Habib** | Score + Progress + Endings | ScoreStorage, GameProgress, EndingManager, Badge, LockedAreaException, ScoreFileException, OBJ_KnowledgeScroll, OBJ_KnowledgePotion, OBJ_ManaPotion, OBJ_MemoryCharm | Storable, Rewardable |

## Testing Pairs

- Aezekiel tests Lee Yun Zhan
- Lee Yun Zhan tests Nathanael
- Nathanael tests Habib
- Habib tests Aezekiel

## Game Concept

Jeff Lionhardt wakes in the kingdom of Lucienne where knowledge has been stolen by Miss Shona. Players explore an open world, collect scrolls (SDG 4 learning content), battle Memory Fragments through quiz combat, and choose their ending based on progress and moral choices.

### Progression Flow
1. **Collect scrolls** (Knowledge Garden) - Learn SDG 4 content, earn XP
2. **Battle Memory Fragments** (Battleground) - Answer quiz questions for KP
3. **Defeat all 5 fragments** to earn 70 KP
4. **Enter the castle** (requires 70 KP + 7 scrolls + talk to guard)
5. **Face Miss Shona** - Final boss quiz battle
6. **Choose your ending** - SHARE, KEEP, or FORGIVE (secret)

### Endings
- **Normal Ending** - Incomplete progress
- **Good Ending** - Defeat Shona with 70+ KP
- **True Ending** - All scrolls + high KP + SHARE choice
- **Secret Ending** - Find Sheena in hidden maze + FORGIVE choice

## How to Build and Run

### Requirements
- Java JDK 14+ (JDK 26 recommended)

### Quick Start
```
build.bat        (compile + package JAR)
run.bat          (run the game)
```

### Create Standalone EXE
```
build.bat            (must run first)
package_exe.bat      (creates dist\QuestForKnowledge\QuestForKnowledge.exe)
```
The EXE version bundles a Java runtime - users don't need Java installed.

## Project Structure

```
questforknowledge/
├── src/
│   ├── main/           # Core engine (GamePanel, KeyHandler, UI, etc.)
│   ├── entity/         # Player, NPCs, Monsters, Boss
│   ├── quiz/           # Quiz system (Question types, QuizManager)
│   ├── learning/       # SDG 4 learning pages
│   ├── progress/       # Score, badges, endings, exceptions
│   ├── object/         # Collectible items (scrolls, potions)
│   ├── interfaces/     # Playable, Interactable
│   ├── tile/           # Tile system and world map renderer
│   ├── maps/           # World map data
│   ├── font/           # Custom fonts
│   ├── sound/          # Sound effects and music
│   ├── player/         # Player sprite images
│   ├── npc/            # NPC sprite images
│   ├── monster/        # Monster sprite images
│   ├── tiles/          # Tile texture images
│   └── objects/        # Item sprite images
├── bin/                # Compiled output
├── dist/               # Packaged EXE output
├── Nath_Assets/        # Custom sprite assets
├── build.bat           # Full build script
├── run.bat             # Run the game
├── package_exe.bat     # Create standalone EXE
├── QuestForKnowledge.jar   # Runnable JAR
├── MANIFEST.MF         # JAR manifest
└── scores.txt          # Saved scores
```

## Controls

| Key | Action |
|-----|--------|
| W/A/S/D or Arrows | Move |
| Enter/Space | Interact / Advance dialogue |
| A/B/C/D (in quiz) | Select answer |
| Escape | Flee quiz / Open options |
| P | Pause |
| F3 | Debug mode |

## OOP Concepts Demonstrated

- **Inheritance:** Entity → Player, NPC, Monster, Boss
- **Interfaces:** Playable, Interactable, Learnable, QuizPlayable, Storable, Rewardable
- **Polymorphism:** Question types (MC, TF, Fill), NPC dialogue variations
- **Abstraction:** Abstract Question class, Entity base class
- **Encapsulation:** Private fields with getters/setters in all data classes
- **Exception Handling:** LockedAreaException, InvalidAnswerException, ScoreFileException
- **File I/O:** ScoreStorage reads/writes scores.txt
- **Collections:** ArrayList for badges, inventory, entities
- **Method Overloading:** LearningManager.displayPage(int) / displayPage(String)
