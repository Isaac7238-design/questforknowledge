package main;

import java.net.URL;
import javax.sound.sampled.*;

/**
 * Sound - audio manager.
 * RyiSnow Blue Boy Adventure Sound.java structure.
 * Maps sound indices to Lucienne-themed sound files.
 * All WAV files loaded from the shared res/sound/ folder.
 *
 * Created by: Aezekiel
 * Tested by: Habib
 * Purpose: Manage background music and sound effects playback.
 */
public class Sound {

    Clip clip;
    URL soundURL[] = new URL[30];
    FloatControl fc;
    public int volumeScale = 3;
    float volume;

    public Sound() {
        // Pre-load all sound URLs at construction time
        soundURL[0]  = getClass().getResource("/sound/BlueBoyAdventure.wav");
        soundURL[1]  = getClass().getResource("/sound/coin.wav");
        soundURL[2]  = getClass().getResource("/sound/powerup.wav");
        soundURL[3]  = getClass().getResource("/sound/unlock.wav");
        soundURL[4]  = getClass().getResource("/sound/fanfare.wav");
        soundURL[5]  = getClass().getResource("/sound/hitmonster.wav");
        soundURL[6]  = getClass().getResource("/sound/receivedamage.wav");
        soundURL[7]  = getClass().getResource("/sound/levelup.wav");
        soundURL[8]  = getClass().getResource("/sound/speak.wav");
        soundURL[9]  = getClass().getResource("/sound/cursor.wav");
        soundURL[10] = getClass().getResource("/sound/gameover.wav");
        soundURL[11] = getClass().getResource("/sound/fanfare.wav");
        soundURL[12] = getClass().getResource("/sound/FinalBattle.wav");
        soundURL[13] = getClass().getResource("/sound/Dungeon.wav");
        soundURL[14] = getClass().getResource("/sound/Merchant.wav");
        soundURL[15] = getClass().getResource("/sound/blocked.wav");
        soundURL[16] = getClass().getResource("/sound/parry.wav");
        soundURL[17] = getClass().getResource("/sound/swingweapon.wav");
        soundURL[18] = getClass().getResource("/sound/burning.wav");
        soundURL[19] = getClass().getResource("/sound/stairs.wav");
        soundURL[20] = getClass().getResource("/sound/sleep.wav");
    }

    public void setFile(int i) {
        try {
            if (soundURL[i] == null) return;
            // Stop and close any previously playing clip
            if (clip != null) {
                clip.stop();
                clip.close();
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();
        } catch (Exception e) {
            // Sound file not available - silently skip
            clip = null;
            fc = null;
        }
    }

    public void play() {
        if (clip != null) { clip.setFramePosition(0); clip.start(); }
    }

    public void loop() {
        if (clip != null) clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null) { clip.stop(); clip.close(); clip = null; }
    }

    public void checkVolume() {
        if (fc == null) return;
        switch (volumeScale) {
            case 0: volume = -80f; break;
            case 1: volume = -20f; break;
            case 2: volume = -12f; break;
            case 3: volume =  -5f; break;
            case 4: volume =   1f; break;
            case 5: volume =   6f; break;
        }
        fc.setValue(volume);
    }
}
