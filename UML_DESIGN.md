# Lucienne: Quest for Quality Education — UML Design Document

**Course:** TMF2954 Java Programming
**Theme:** SDG 4 — Quality Education

This document contains the Unified Modelling Language (UML) diagrams explaining the
structure of the project's classes and interfaces, plus the class/interface
distribution for all project members.

The diagrams are written in **PlantUML** text format. To render them as images:
- Paste the code blocks into https://www.plantuml.com/plantuml
- Or use the "PlantUML" extension in VS Code (Alt+D to preview)

---

## 1. Class / Interface Distribution (by team member)

| Member | Module | Classes | Interface |
|--------|--------|---------|-----------|
| **Aezekiel** (Leader) | Player + open-world map + integration | `Player`, `GamePanel`, `Entity`, `KeyHandler`, `Main`, `AssetSetter`, `EventHandler`, `CollisionChecker`, `UI`, `UtilityTool`, `Sound`, `Tile`, `TileManager` | `Playable` |
| **Lee Yun Zhan** | Learning + NPC interaction | `LearningPage`, `LearningManager`, `NPC_Piercehardt`, `NPC_Villager`, `NPC_Shopkeeper`, `NPC_KingLuin`, `NPC_Lucious`, `NPC_SheenaMemory` | `Interactable`, `Learnable` |
| **Nathanael** | Quiz + battle system | `Question`, `MultipleChoiceQuestion`, `TrueFalseQuestion`, `FillBlankQuestion`, `QuizManager`, `MON_MemoryFragment`, `BOSS_Shona`, `InvalidAnswerException` | `QuizPlayable` |
| **Habib** | Score + progress + endings | `ScoreStorage`, `GameProgress`, `EndingManager`, `Badge`, `LockedAreaException`, `ScoreFileException`, `OBJ_KnowledgeScroll`, `OBJ_KnowledgePotion`, `OBJ_ManaPotion`, `OBJ_MemoryCharm` | `Storable`, `Rewardable` |

**Testing pairs:** Aezekiel→Lee, Lee→Nathanael, Nathanael→Habib, Habib→Aezekiel

---

## 2. Overall Package Diagram

```plantuml
@startuml Package_Overview
skinparam packageStyle rectangle
skinparam backgroundColor #FFFFFF

package "main" {
  class GamePanel
  class Main
  class KeyHandler
  class UI
  class AssetSetter
  class CollisionChecker
  class EventHandler
  class Sound
  class UtilityTool
}

package "entity" {
  class Entity
  class Player
  class MON_MemoryFragment
  class BOSS_Shona
  class NPC_Piercehardt
  class NPC_Villager
  class NPC_Shopkeeper
  class NPC_KingLuin
  class NPC_Lucious
  class NPC_SheenaMemory
}

package "interfaces" {
  interface Playable
  interface Interactable
}

package "quiz" {
  abstract class Question
  class MultipleChoiceQuestion
  class TrueFalseQuestion
  class FillBlankQuestion
  class QuizManager
  interface QuizPlayable
  class InvalidAnswerException
}

package "learning" {
  class LearningPage
  class LearningManager
  interface Learnable
}

package "progress" {
  class GameProgress
  class ScoreStorage
  class EndingManager
  class Badge
  interface Storable
  interface Rewardable
  class LockedAreaException
  class ScoreFileException
}

package "object" {
  class OBJ_KnowledgeScroll
  class OBJ_KnowledgePotion
  class OBJ_ManaPotion
  class OBJ_MemoryCharm
}

package "tile" {
  class Tile
  class TileManager
}

main ..> entity
main ..> quiz
main ..> learning
main ..> progress
main ..> tile
entity ..> interfaces
entity ..> object
@enduml
```

---

## 3. Entity Inheritance Hierarchy (Aezekiel + Lee + Nathanael + Habib)

This is the core inheritance tree. Every game object extends `Entity`.

```plantuml
@startuml Entity_Hierarchy
skinparam classAttributeIconSize 0
skinparam backgroundColor #FFFFFF

class Entity {
  + gp : GamePanel
  + worldX, worldY : int
  + direction : String
  + speed, life, maxLife : int
  + attack, defense, exp : int
  + name : String
  + alive, dying, collision : boolean
  + inventory : ArrayList<Entity>
  --
  + draw(g2 : Graphics2D) : void
  + update() : void
  + setup(path, w, h) : BufferedImage
  + speak() : void
  + setAction() : void
  + checkDrop() : void
  + getCol() : int
  + getRow() : int
}

class Player {
  + knowledgePoints : int
  + scrollsCompleted : int
  + enemiesDefeated : int
  + hasDefeatedShona : boolean
  + hasFoundSheenaMemory : boolean
  + badges : ArrayList<Badge>
  --
  + move(direction : String) : void
  + gainXP(amount : int) : void
  + levelUp() : void
  + checkAllFragmentsDefeated() : void
  + unlockBadge(name : String) : void
  + pickUpObject(i : int) : void
}

class MON_MemoryFragment {
  + setDefaultValues() : void
  + setAction() : void
  + checkDrop() : void
  + damageReaction() : void
}

class BOSS_Shona {
  + setAction() : void
  + checkDrop() : void
}

class NPC_Piercehardt
class NPC_Villager
class NPC_Shopkeeper
class NPC_KingLuin
class NPC_Lucious {
  - gateOpened : boolean
  + speak() : void
  + setAction() : void
}
class NPC_SheenaMemory {
  - activated : boolean
  + speak() : void
}

class OBJ_KnowledgeScroll
class OBJ_KnowledgePotion
class OBJ_ManaPotion
class OBJ_MemoryCharm

interface Playable {
  + move(direction : String) : void
  + gainXP(amount : int) : void
  + levelUp() : void
}

Entity <|-- Player
Entity <|-- MON_MemoryFragment
MON_MemoryFragment <|-- BOSS_Shona
Entity <|-- NPC_Piercehardt
Entity <|-- NPC_Villager
Entity <|-- NPC_Shopkeeper
Entity <|-- NPC_KingLuin
Entity <|-- NPC_Lucious
Entity <|-- NPC_SheenaMemory
Entity <|-- OBJ_KnowledgeScroll
Entity <|-- OBJ_KnowledgePotion
Entity <|-- OBJ_ManaPotion
Entity <|-- OBJ_MemoryCharm

Player ..|> Playable
@enduml
```

---

## 4. Quiz System Diagram (Nathanael)

Demonstrates **abstraction** (`Question`), **inheritance** (3 subclasses),
**polymorphism** (`checkAnswer` overridden), and the **`QuizPlayable`** interface.

```plantuml
@startuml Quiz_System
skinparam classAttributeIconSize 0
skinparam backgroundColor #FFFFFF

abstract class Question {
  # questionText : String
  # correctAnswer : String
  # points : int
  --
  + {abstract} checkAnswer(answer : String) : boolean
  + {abstract} getType() : String
  + getQuestionText() : String
  + getPoints() : int
}

class MultipleChoiceQuestion {
  - options : String[]
  + checkAnswer(answer : String) : boolean
  + getType() : String
  + getOptions() : String[]
}

class TrueFalseQuestion {
  + checkAnswer(answer : String) : boolean
  + getType() : String
}

class FillBlankQuestion {
  + checkAnswer(answer : String) : boolean
  + getType() : String
}

interface QuizPlayable {
  + startQuiz() : void
  + checkAnswer(answer : String) : boolean
  + calculateScore() : int
}

class QuizManager {
  - questions : ArrayList<Question>
  - score : int
  - currentIndex : int
  - quizFinished : boolean
  --
  + startQuiz() : void
  + startBattleQuiz(numQ : int) : void
  + checkAnswer(answer : String) : boolean
  + submitAnswer(optionIndex : int) : boolean
  + calculateScore() : int
  + drawQuiz(g2, ...) : void
}

class InvalidAnswerException {
  + InvalidAnswerException(message : String)
}

Question <|-- MultipleChoiceQuestion
Question <|-- TrueFalseQuestion
Question <|-- FillBlankQuestion
QuizManager ..|> QuizPlayable
QuizManager o-- "many" Question : contains
Question ..> InvalidAnswerException : throws
QuizManager ..> InvalidAnswerException : handles
@enduml
```

---

## 5. Learning System Diagram (Lee Yun Zhan)

Demonstrates the **`Learnable`** interface, **method overloading**
(`displayPage(int)` and `displayPage(String)`), and **ArrayList** usage.

```plantuml
@startuml Learning_System
skinparam classAttributeIconSize 0
skinparam backgroundColor #FFFFFF

interface Learnable {
  + getPage(index : int) : LearningPage
  + completePage(index : int) : void
  + getCompletedCount() : int
  + displayPage(index : int) : String
  + displayPage(title : String) : String
}

class LearningManager {
  - pages : ArrayList<LearningPage>
  - currentPageIndex : int
  --
  + getPage(index : int) : LearningPage
  + completePage(index : int) : void
  + getCompletedCount() : int
  + displayPage(index : int) : String
  + displayPage(title : String) : String
  + getNextIncompletePage() : LearningPage
}

class LearningPage {
  - pageId : int
  - title : String
  - content : String
  - completed : boolean
  --
  + displayPage() : String
  + markCompleted() : void
  + getSummary() : String
  + isCompleted() : boolean
}

LearningManager ..|> Learnable
LearningManager o-- "10" LearningPage : manages
@enduml
```

---

## 6. NPC Interaction Diagram (Lee Yun Zhan)

NPCs extend `Entity` and override `speak()` for dialogue (polymorphism).
The **`Interactable`** interface defines the interaction contract for the module.

```plantuml
@startuml NPC_Interaction
skinparam classAttributeIconSize 0
skinparam backgroundColor #FFFFFF

interface Interactable {
  + interact(player : Player) : void
}

class Entity {
  + speak() : void
  + facePlayer() : void
  + startDialogue(entity, set) : void
}

class NPC_Piercehardt
class NPC_Villager
class NPC_Shopkeeper {
  + speak() : void
}
class NPC_KingLuin
class NPC_Lucious {
  - gateOpened : boolean
  + speak() : void
}
class NPC_SheenaMemory {
  - activated : boolean
  + speak() : void
}

class LockedAreaException

Entity <|-- NPC_Piercehardt
Entity <|-- NPC_Villager
Entity <|-- NPC_Shopkeeper
Entity <|-- NPC_KingLuin
Entity <|-- NPC_Lucious
Entity <|-- NPC_SheenaMemory

Interactable ..> Player : interaction contract
NPC_Lucious ..> LockedAreaException : throws
@enduml
```

> Note: NPC dialogue is handled through the inherited `Entity.speak()` method
> (runtime polymorphism). The `Interactable` interface documents the interaction
> contract for the NPC module.

---

## 7. Progress / Score / Endings Diagram (Habib)

Demonstrates **`Storable`** and **`Rewardable`** interfaces, **file I/O**,
and **custom exceptions**.

```plantuml
@startuml Progress_System
skinparam classAttributeIconSize 0
skinparam backgroundColor #FFFFFF

interface Storable {
  + saveScore(progress : GameProgress) : void
  + loadScores() : String
}

interface Rewardable {
  + award() : void
  + getBadgeName() : String
  + getDescription() : String
  + isUnlocked() : boolean
}

class ScoreStorage {
  - fileName : String
  + saveScore(progress : GameProgress) : void
  + loadScores() : String
  + displayHighScore() : String
}

class GameProgress {
  - knowledgePoints : int
  - scrollsCompleted : int
  - finalScore : int
  - endingType : String
  - badges : ArrayList<Badge>
  --
  + syncFromPlayer(p : Player) : void
  + unlockBadge(name : String) : void
  + getBadgesAsText() : String
}

class Badge {
  - badgeName : String
  - description : String
  - unlocked : boolean
  --
  + award() : void
  + isUnlocked() : boolean
}

class EndingManager {
  + determineEnding(p : Player) : String
  + getEndingTitle(type : String) : String
  + getEndingStory(type : String) : String
  + drawEnding(g2, ...) : void
}

class ScoreFileException
class LockedAreaException

ScoreStorage ..|> Storable
Badge ..|> Rewardable
GameProgress o-- "many" Badge : holds
ScoreStorage ..> GameProgress : saves
ScoreStorage ..> ScoreFileException : throws
EndingManager ..> GameProgress : reads
ScoreFileException --|> Exception
LockedAreaException --|> Exception
@enduml
```

---

## 8. Core Engine / Integration Diagram (Aezekiel)

Shows how `GamePanel` (the controller) wires all subsystems together —
this is the **integration** module.

```plantuml
@startuml Core_Engine
skinparam classAttributeIconSize 0
skinparam backgroundColor #FFFFFF

class Main {
  + {static} main(args : String[]) : void
  + {static} window : JFrame
}

class GamePanel {
  + tileSize : int
  + gameState : int
  + player : Player
  + npc : Entity[][]
  + monster : Entity[][]
  + obj : Entity[][]
  --
  + setupGame() : void
  + resetGame(restart : boolean) : void
  + update() : void
  + run() : void
  + saveProgress() : void
  + loadProgress() : boolean
}

class KeyHandler {
  + upPressed, downPressed : boolean
  + keyPressed(e : KeyEvent) : void
}
class UI {
  + draw(g2 : Graphics2D) : void
  + showToast(msg : String) : void
}
class TileManager {
  + mapTileNum : int[][][]
  + loadMap(path : String, map : int) : void
  + draw(g2 : Graphics2D) : void
}
class CollisionChecker {
  + checkTile(entity : Entity) : void
  + checkObject(entity, player) : int
}
class AssetSetter {
  + setObject() : void
  + setNPC() : void
  + setMonster() : void
}
class EventHandler {
  + checkEvent() : void
}
class Sound {
  + play() : void
  + loop() : void
}
class UtilityTool {
  + scaleImage(img, w, h) : BufferedImage
}

Main --> GamePanel : creates
GamePanel *-- KeyHandler
GamePanel *-- UI
GamePanel *-- TileManager
GamePanel *-- CollisionChecker
GamePanel *-- AssetSetter
GamePanel *-- EventHandler
GamePanel *-- Sound
GamePanel --> Player
TileManager ..> UtilityTool : uses
@enduml
```

---

## 9. Game State Machine Diagram

The game uses a **state machine** managed by `GamePanel.gameState`.
Below is the state transition flow.

```plantuml
@startuml State_Machine
skinparam backgroundColor #FFFFFF

[*] --> TitleState

TitleState --> PrologueState : New Game
TitleState --> PlayState : Continue (load save)
TitleState --> ScoreState : View Scores
TitleState --> [*] : Quit

PrologueState --> PlayState : finish intro

PlayState --> DialogueState : talk to NPC
PlayState --> QuizState : touch enemy
PlayState --> LearningState : read scroll
PlayState --> ShopState : talk to shopkeeper
PlayState --> PauseState : press P
PlayState --> OptionsState : press Esc
PlayState --> GameOverState : life = 0
PlayState --> EndingChoiceState : defeat Shona

DialogueState --> PlayState
QuizState --> PlayState : quiz done
LearningState --> PlayState
ShopState --> PlayState
PauseState --> PlayState
OptionsState --> PlayState : resume
OptionsState --> TitleState : End Game (save)

EndingChoiceState --> EndingState : choose SHARE/KEEP/FORGIVE
EndingState --> TitleState : save score + progress
GameOverState --> PlayState : Retry
GameOverState --> TitleState : Quit
ScoreState --> TitleState
@enduml
```

---

## 10. Use Case Diagram (Player Actions)

```plantuml
@startuml Use_Case
left to right direction
skinparam backgroundColor #FFFFFF
actor "Player (Jeff)" as P

rectangle "Lucienne: Quest for Quality Education" {
  usecase "Start New Game" as UC1
  usecase "Continue Saved Game" as UC2
  usecase "Read SDG 4 Scrolls" as UC3
  usecase "Battle Memory Fragments\n(answer quiz)" as UC4
  usecase "Talk to NPCs" as UC5
  usecase "Buy Items at Shop" as UC6
  usecase "Enter Castle\n(needs 70 KP + 7 scrolls)" as UC7
  usecase "Defeat Boss Shona" as UC8
  usecase "Choose Ending" as UC9
  usecase "Find Sheena\n(secret)" as UC10
  usecase "View High Scores" as UC11
}

P --> UC1
P --> UC2
P --> UC3
P --> UC4
P --> UC5
P --> UC6
P --> UC7
P --> UC8
P --> UC9
P --> UC10
P --> UC11

UC7 ..> UC4 : <<requires>>
UC7 ..> UC3 : <<requires>>
UC9 ..> UC8 : <<requires>>
UC10 ..> UC9 : <<unlocks FORGIVE>>
@enduml
```

---

## 11. Sequence Diagram — Quiz Battle Flow (Nathanael's module)

```plantuml
@startuml Sequence_QuizBattle
skinparam backgroundColor #FFFFFF
actor Player
participant KeyHandler
participant GamePanel
participant QuizManager
participant "MON_MemoryFragment" as Monster
participant UI

Player -> KeyHandler : press ENTER (near enemy)
KeyHandler -> GamePanel : start quiz battle
GamePanel -> QuizManager : startBattleQuiz(life)
GamePanel -> GamePanel : gameState = quizState

loop until quiz finished
  Player -> KeyHandler : press A/B/C/D
  KeyHandler -> QuizManager : submitAnswer(option)
  QuizManager -> QuizManager : checkAnswer()
  alt correct
    KeyHandler -> Player : gainXP(15), +10 KP
    KeyHandler -> Monster : life--
  else wrong
    KeyHandler -> Player : life--
    KeyHandler -> UI : screen shake
  end
end

KeyHandler -> Player : checkAllFragmentsDefeated()
Player -> UI : showToast("KP maxed to 70")
GamePanel -> GamePanel : gameState = playState
@enduml
```

---

## 12. Sequence Diagram — Castle Gate Unlock (Lee + Aezekiel)

```plantuml
@startuml Sequence_CastleGate
skinparam backgroundColor #FFFFFF
actor Player
participant NPC_Lucious
participant GamePanel
participant TileManager
participant UI

Player -> NPC_Lucious : speak()
NPC_Lucious -> NPC_Lucious : check KP>=70 AND scrolls>=7
alt requirements met
  NPC_Lucious -> TileManager : open gate tiles (6 -> 4)
  NPC_Lucious -> GamePanel : playSE(door)
  NPC_Lucious -> Player : unlockBadge("Castle Scholar")
  NPC_Lucious -> NPC_Lucious : walk aside
else not enough
  NPC_Lucious -> UI : addMessage("Need 70 KP AND 7 scrolls")
  NPC_Lucious --> NPC_Lucious : throw LockedAreaException
end
@enduml
```

---

## Notes on OOP Concepts Demonstrated

| Concept | Where |
|---------|-------|
| **Inheritance** | `Entity` → Player/NPCs/Monsters/Objects; `Question` → MC/TF/Fill; `MON_MemoryFragment` → `BOSS_Shona` |
| **Abstraction** | abstract class `Question`; base class `Entity` |
| **Polymorphism** | `checkAnswer()`, `speak()`, `setAction()`, `checkDrop()` overridden per subclass |
| **Encapsulation** | private fields + getters/setters in `Badge`, `GameProgress`, `LearningPage`, `Question` |
| **Interfaces** | `Playable`, `Interactable`, `Learnable`, `QuizPlayable`, `Storable`, `Rewardable` |
| **Exception Handling** | `LockedAreaException`, `InvalidAnswerException`, `ScoreFileException` |
| **File I/O** | `ScoreStorage` (scores.txt), `GamePanel.saveProgress/loadProgress` (progress.dat) |
| **Collections** | `ArrayList` for badges, inventory, questions, learning pages, entities |
| **Method Overloading** | `LearningManager.displayPage(int)` / `displayPage(String)`; `Player.gainXP(int)` / `gainXP(int, String)` |
