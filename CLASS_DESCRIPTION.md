# Lucienne: Quest for Quality Education
# Class & Interface Distribution and Member Roles

**Course:** TMF2954 Java Programming
**Theme:** SDG 4 — Quality Education
**Project type:** Group project (4 members)

This document describes the class and interface distribution for all project
members and clearly explains each member's role in implementing their classes
and interface.

---

## Summary Table

| Member | Module | No. of Classes | Interface(s) |
|--------|--------|:--------------:|--------------|
| **Aezekiel** (Leader) | Player + Open-world Map + Integration | 13 | `Playable` |
| **Lee Yun Zhan** | Learning + NPC Interaction | 8 | `Interactable`, `Learnable` |
| **Nathanael** | Quiz + Battle System | 8 | `QuizPlayable` |
| **Habib** | Score + Progress + Endings | 10 | `Storable`, `Rewardable` |

**Peer-testing pairs:** Aezekiel tests Lee · Lee tests Nathanael · Nathanael tests Habib · Habib tests Aezekiel

---

## 1. Aezekiel (Group Leader) — Player, Map & Integration

**Role:** Aezekiel built the game's core engine and is responsible for combining
every member's module into one working program. He handles Jeff's stats, movement,
the camera, the world map, and the central game loop / state machine.

### Interface
- **`Playable`** — defines the core actions a controllable character must support:
  `move(direction)`, `gainXP(amount)`, `levelUp()`. Implemented by `Player`.

### Classes
| Class | Responsibility |
|-------|----------------|
| `Main` | Application entry point; creates the game window (`JFrame`) and starts the thread. |
| `GamePanel` | Heart of the game. Runs the game loop, holds the state machine (14 states), stores entity arrays, and integrates every subsystem. Also handles save/load. |
| `Entity` | Base class for all game objects (Player, NPCs, Monsters, Objects). Provides shared fields (position, sprites, stats) and behaviour (`draw`, `update`, `setup`). |
| `Player` | Jeff Lionhardt. Manages HP, XP, level, Knowledge Points, scrolls, badges, movement, and item pickup. Implements `Playable`. |
| `KeyHandler` | Reads keyboard input and routes it by game state. |
| `UI` | Draws every screen: title, HUD, dialogue, quiz, shop, endings, score, toasts. |
| `TileManager` | Loads tile images and the world map text file, then renders the world. |
| `Tile` | Simple data holder for a tile (image + collision flag). |
| `CollisionChecker` | Detects collisions between entities, tiles, and objects. |
| `AssetSetter` | Places all NPCs, monsters, and objects at their map positions. |
| `EventHandler` | Triggers zone messages and the castle-gate block check. |
| `Sound` | Plays background music and sound effects. |
| `UtilityTool` | Smoothly scales sprite/tile images (bilinear interpolation). |

**Key contribution:** Integration — Aezekiel merges Lee's, Nathanael's, and Habib's
modules through `GamePanel`, which holds references to `QuizManager`,
`LearningManager`, `EndingManager`, and `ScoreStorage`.

---

## 2. Lee Yun Zhan — Learning Content & NPC Interaction

**Role:** Lee prepared the 10 SDG 4 learning pages and built all the
non-player characters and their dialogue. His module delivers the educational
content of the game and the social/story interactions.

### Interfaces
- **`Learnable`** — contract for managing learning pages: `getPage`,
  `completePage`, `getCompletedCount`, and the overloaded `displayPage(int)` /
  `displayPage(String)`. Implemented by `LearningManager`.
- **`Interactable`** — contract for interaction (`interact(player)`) describing
  how NPCs/objects respond to the player.

### Classes
| Class | Responsibility |
|-------|----------------|
| `LearningPage` | One SDG 4 scroll: page id, title, content, and completion flag. |
| `LearningManager` | Holds all 10 learning pages, tracks completion, displays pages. Implements `Learnable`. |
| `NPC_Piercehardt` | Tutorial mage who explains the mission. |
| `NPC_Villager` | Villagers that teach SDG 4 ideas through dialogue (multiple instances). |
| `NPC_Shopkeeper` | Opens the shop when spoken to. |
| `NPC_KingLuin` | The king inside the castle; story dialogue. |
| `NPC_Lucious` | Castle guard; blocks entry until requirements are met, then opens the gate (throws `LockedAreaException`). |
| `NPC_SheenaMemory` | Hidden secret NPC; finding her unlocks the secret/forgive ending. |

**Key contribution:** Wrote the SDG 4 educational text (10 pages) and gave every
NPC distinct dialogue using polymorphism (each overrides `speak()`).

---

## 3. Nathanael — Quiz & Battle System

**Role:** Nathanael prepared the 20 quiz questions and built the entire
quiz-based combat system, including the different question types, score
calculation, and the enemy/boss battle logic.

### Interface
- **`QuizPlayable`** — contract for a quiz session: `startQuiz()`,
  `checkAnswer(answer)`, `calculateScore()`. Implemented by `QuizManager`.

### Classes
| Class | Responsibility |
|-------|----------------|
| `Question` | Abstract base class for all question types (abstraction). |
| `MultipleChoiceQuestion` | A/B/C/D question; overrides `checkAnswer`. |
| `TrueFalseQuestion` | True/False question type. |
| `FillBlankQuestion` | Fill-in-the-blank question type. |
| `QuizManager` | Holds 20 questions, runs quiz sessions, calculates score, draws the quiz UI. Implements `QuizPlayable`. |
| `InvalidAnswerException` | Custom exception thrown for empty/invalid answers. |
| `MON_MemoryFragment` | Quiz enemy; touching it starts a quiz battle. Extends `Entity`. |
| `BOSS_Shona` | Final boss; extends `MON_MemoryFragment`, longer battle, triggers the ending choice. |

**Key contribution:** Designed the question hierarchy (abstraction + polymorphism)
so new question types can be added easily, and linked quizzes to combat damage.

---

## 4. Habib — Score, Progress & Endings

**Role:** Habib implemented the save/load system using text files, the badge
and progress tracking, and the alternative endings. His module records the
player's achievements and decides which ending they reach.

### Interfaces
- **`Storable`** — file I/O contract: `saveScore(progress)`, `loadScores()`.
  Implemented by `ScoreStorage`.
- **`Rewardable`** — badge contract: `award()`, `getBadgeName()`,
  `getDescription()`, `isUnlocked()`. Implemented by `Badge`.

### Classes
| Class | Responsibility |
|-------|----------------|
| `ScoreStorage` | Saves/loads scores to `scores.txt`; computes the high score. Implements `Storable`. |
| `GameProgress` | Snapshot of player progress (KP, scrolls, ending, badges) synced before saving. |
| `EndingManager` | Determines the ending (Normal/Good/True/Secret) and draws the ending screen. |
| `Badge` | An achievement with name, description, unlocked flag. Implements `Rewardable`. |
| `LockedAreaException` | Custom exception when entering a locked area without enough progress. |
| `ScoreFileException` | Custom exception for score-file read/write errors. |
| `OBJ_KnowledgeScroll` | Collectible scroll that opens a learning page. |
| `OBJ_KnowledgePotion` | Consumable that gives a quiz hint. |
| `OBJ_ManaPotion` | Consumable that restores health. |
| `OBJ_MemoryCharm` | Pickup that grants bonus XP after the next battle. |

**Key contribution:** Built the persistence layer (text-file save/load with
exception handling) and the branching ending logic based on player choices.

---

## OOP Concepts Demonstrated (cross-member)

| Concept | Example in code |
|---------|-----------------|
| Inheritance | `Entity` → Player/NPC/Monster/Object; `Question` → MC/TF/Fill; `MON_MemoryFragment` → `BOSS_Shona` |
| Abstraction | abstract `Question`; base `Entity` |
| Polymorphism | `checkAnswer()`, `speak()`, `setAction()`, `checkDrop()` overridden per subclass |
| Encapsulation | private fields + getters in `Badge`, `GameProgress`, `LearningPage`, `Question` |
| Interfaces | `Playable`, `Interactable`, `Learnable`, `QuizPlayable`, `Storable`, `Rewardable` |
| Exception handling | `LockedAreaException`, `InvalidAnswerException`, `ScoreFileException` |
| File I/O | `ScoreStorage` (scores.txt), `GamePanel` save/load (progress.dat) |
| Collections | `ArrayList` for badges, inventory, questions, learning pages, entities |
| Method overloading | `LearningManager.displayPage(int/String)`; `Player.gainXP(int / int,String)` |
