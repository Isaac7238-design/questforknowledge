============================================================
  LUCIENNE: QUEST FOR QUALITY EDUCATION
  TMF2954 Java Programming Assignment
  Theme: SDG 4 - Quality Education
============================================================

PROJECT TITLE
  Lucienne: Quest for Quality Education

SDG THEME
  SDG 4: Quality Education
  Education should be inclusive, fair, accessible, safe, and shared.

SHORT STORYLINE
  Jeff Lionhardt is an ordinary student who falls asleep and dreams
  into the magical kingdom of Lucienne. The kingdom was once full of
  wisdom, but Miss Shona created the Knowledge Crystal to control
  knowledge, trapping it and causing the kingdom to lose its wisdom.
  Evil Memory Fragments now roam the kingdom. Jeff must explore,
  collect Knowledge Scrolls, defeat quiz enemies, and face Miss Shona
  to restore Lucienne's wisdom.

------------------------------------------------------------
HOW TO COMPILE
------------------------------------------------------------
  Windows (double-click):
    compile.bat

  Command line:
    javac -encoding UTF-8 -sourcepath src -d bin src\main\Main.java

------------------------------------------------------------
HOW TO RUN
------------------------------------------------------------
  Windows (double-click):
    run.bat

  Command line:
    java -cp bin main.Main

------------------------------------------------------------
CONTROLS
------------------------------------------------------------
  W / Up Arrow    = Move up
  S / Down Arrow  = Move down
  A / Left Arrow  = Move left
  D / Right Arrow = Move right
  Enter           = Interact / Talk to NPC / Start battle
  Space           = Advance dialogue
  A, B, C, D      = Answer quiz questions
  P               = Pause
  Esc             = Close dialogue / Options / Skip

------------------------------------------------------------
GAME FEATURES
------------------------------------------------------------
  - Prologue cutscene with narrative text
  - Single interconnected world map with 8 zones
  - 8 unique NPCs with multi-line dialogues
  - 10 Knowledge Scrolls teaching SDG 4 concepts
  - 20 multiple choice quiz questions
  - Quiz-based battle system (A/B/C/D answers)
  - Shop system with 3 purchasable items
  - Castle gate locked until 70 KP or 7 scrolls
  - Final boss battle with Miss Shona
  - 4 possible endings (Normal, Good, True, Secret)
  - 6 collectable badges
  - XP and level system
  - Score saving to scores.txt
  - View Scores from title menu

------------------------------------------------------------
OOP CONCEPTS DEMONSTRATED
------------------------------------------------------------
  Inheritance:
    Entity -> Player, NPC_*, MON_MemoryFragment, BOSS_Shona
    Question -> MultipleChoiceQuestion, TrueFalseQuestion, FillBlankQuestion

  Interfaces (6):
    Playable          - implemented by Player
    Interactable      - implemented by NPCs
    QuizPlayable      - implemented by QuizManager
    Storable          - implemented by ScoreStorage
    Rewardable        - implemented by Badge
    Learnable         - implemented by LearningManager

  Abstraction:
    Entity (base class), Question (abstract)

  Polymorphism:
    ArrayList<Question> holds different question types
    Entity[] arrays hold Player, NPCs, Monsters polymorphically

  Encapsulation:
    Private fields + getters/setters in LearningPage, Badge,
    GameProgress, ScoreStorage, QuizManager

  Method Overloading:
    Player.gainXP(int) vs Player.gainXP(int, String)
    LearningManager.displayPage(int) vs displayPage(String)

  Method Overriding:
    @Override in Entity subclasses: setAction(), speak(), checkDrop()
    @Override in QuizManager: startQuiz(), checkAnswer(), calculateScore()

  Exception Handling:
    InvalidAnswerException - empty quiz answer
    LockedAreaException    - castle entry without progress
    ScoreFileException     - file read/write errors

  2D Array:
    mapTileNum[maxMap][col][row] - world map tile grid

  ArrayList:
    questions, pages, badges, messages, inventory, entityList

  File Handling:
    ScoreStorage reads/writes scores.txt (CSV format)

------------------------------------------------------------
SCORE STORAGE
------------------------------------------------------------
  Format: PlayerName,Score,KP,Scrolls,Level,EndingType,Badges
  File: scores.txt (auto-created in project root)
  Scores are appended, never overwritten.

------------------------------------------------------------
PACKAGES
------------------------------------------------------------
  main/        - GamePanel, Main, KeyHandler, UI, Sound, etc.
  entity/      - Entity, Player, NPC_*, MON_*, BOSS_*
  tile/        - Tile, TileManager
  object/      - OBJ_KnowledgeScroll, OBJ_ManaPotion, etc.
  quiz/        - Question, MultipleChoiceQuestion, QuizManager, etc.
  learning/    - LearningPage, LearningManager, Learnable
  progress/    - Badge, GameProgress, EndingManager, ScoreStorage, etc.
  interfaces/  - Playable, Interactable

------------------------------------------------------------
KNOWN LIMITATIONS
------------------------------------------------------------
  - No sprite animation for attack (battles are quiz-based)
  - Single map design (no separate indoor maps)
  - Sound files from Blue Boy Adventure (free open source)
  - Tile graphics from Blue Boy Adventure (free open source)
  - Character sprites from Ninja Adventure (free, itch.io)

============================================================
  Course: TMF2954 Java Programming
  Reference: RyiSnow "How to Make a 2D Game in Java" (YouTube)
============================================================
