package main;

/**
 * EventHandler - zone messages and castle gate check.
 * SINGLE MAP design - no teleporting between maps.
 * Castle gate at row 12 is blocked until player meets requirements.
 */
public class EventHandler {

    GamePanel gp;
    boolean[] areaShown = new boolean[20];
    public int tempMap = 0, tempCol = 0, tempRow = 0;

    public EventHandler(GamePanel gp) { this.gp = gp; }

    public void checkEvent() {
        int px = gp.player.getCol();
        int py = gp.player.getRow();

        // Zone messages (show once)
        checkZoneMessages(px, py);

        // Castle gate check at row 12, col 25
        checkCastleGate(px, py);
    }

    private void checkZoneMessages(int px, int py) {
        // Safe Area (Y:33-39, X:22-28)
        if (!areaShown[0] && py >= 33 && py <= 38 && px >= 22 && px <= 28) {
            areaShown[0] = true;
            gp.ui.addMessage("Safe Area - Talk to Piercehardt!");
        }
        // Village (Y:18-30, X:18-32)
        if (!areaShown[1] && py >= 18 && py <= 30 && px >= 18 && px <= 32) {
            areaShown[1] = true;
            gp.ui.addMessage("Village of Lucienne");
        }
        // Knowledge Garden (Y:15-30, X:2-15)
        if (!areaShown[2] && py >= 15 && py <= 30 && px >= 2 && px <= 15) {
            areaShown[2] = true;
            gp.ui.addMessage("Knowledge Garden - Find the scrolls!");
        }
        // Battleground (Y:12-30, X:34-48)
        if (!areaShown[3] && py >= 12 && py <= 30 && px >= 34 && px <= 48) {
            areaShown[3] = true;
            gp.ui.addMessage("Battleground - Evil Memory Fragments!");
        }
        // Castle Hall (Y:3-10, X:20-30)
        if (!areaShown[4] && py >= 3 && py <= 10 && px >= 20 && px <= 30) {
            areaShown[4] = true;
            gp.ui.addMessage("Castle Hall - Ancient records surround you.");
        }
        // Secret Forest (Y:3-8, X:1-6)
        if (!areaShown[5] && py >= 3 && py <= 8 && px >= 1 && px <= 6) {
            areaShown[5] = true;
            gp.ui.addMessage("Hidden Memory... You sense something ancient.");
        }
        // Final Chamber (Y:1-2, X:23-27)
        if (!areaShown[6] && py <= 2 && px >= 23 && px <= 27) {
            areaShown[6] = true;
            gp.ui.addMessage("Shona's Chamber - The Knowledge Crystal pulses!");
        }
    }

    private void checkCastleGate(int px, int py) {
        // When player reaches row 12 at the road (cols 24-26), check KP
        if (py == 12 && px >= 24 && px <= 26) {
            boolean unlocked = gp.player.knowledgePoints >= 70
                            || gp.player.scrollsCompleted >= 7;
            if (unlocked) {
                // Teleport player past the gate wall into castle (row 9)
                gp.player.worldY = gp.tileSize * 9;
                if (!areaShown[10]) {
                    areaShown[10] = true;
                    gp.player.unlockBadge("Castle Scholar");
                    gp.ui.addMessage("The gate opens! You may enter.");
                }
            } else {
                // Only show message once until player leaves the gate area
                if (!areaShown[11]) {
                    areaShown[11] = true;
                    gp.ui.addMessage("Castle locked! Need 70 KP or 7 Scrolls.");
                }
                // Push player back
                gp.player.worldY = gp.tileSize * 13;
            }
        } else {
            // Player left the gate area, allow message again
            areaShown[11] = false;
        }
    }

    public void resetEvents() { areaShown = new boolean[20]; }
}
