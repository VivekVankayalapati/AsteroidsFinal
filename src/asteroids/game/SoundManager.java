package asteroids.game;

import java.io.BufferedInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * TODO Docs
 * @author Aidan Copinga
 */
public class SoundManager
{

    /**
     * TODO Docs
     * @param soundFile
     * @return
     */
    public Clip setClip (String soundFile)
    {
        
        try (BufferedInputStream sound = new BufferedInputStream(getClass().getResourceAsStream(soundFile)))
        {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            return clip;
        }
        catch(Exception e){
            return null;
        }
    }

    /**
     * TODO Docs
     */
    public static void playSound (Clip soundFile)
    {
        playSound(soundFile, 1);
    }

    /**
     * TODO Docs
     */
    public static void playSound (Clip soundFile, int loops)
    {
        if(soundFile.isRunning()){
            soundFile.stop();
        }
        soundFile.setFramePosition(0);
        soundFile.loop(loops);
    }
}