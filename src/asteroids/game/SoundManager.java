package asteroids.game;

import java.io.BufferedInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Manages the sound file for usage by participants
 */
public class SoundManager
{

    /**
     * Records sounds
     */
    private Clip soundFile;

    /**
     * Determines numbers of loops for soundfiles
     *
     */
    private int loops;

    /**
     * Calls main constructor with loops being 0
     * @param soundFile
     */
    public SoundManager(String soundFile)
    {
        this(soundFile, 0);
    }

    /**
     * Manages audio gathering and playback 
     * @param soundFile
     * @param loops
     */
    public SoundManager(String soundFile, int loops)
    {
        try (BufferedInputStream sound = new BufferedInputStream(getClass().getResourceAsStream(soundFile)))
        {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            this.soundFile = clip;

        }
        catch(Exception e)
        {
            e.printStackTrace();
            this.soundFile = null;
        }
        this.loops = loops;
    }

    /**Plays the sound */
    public void playSound()
    {
        playSound(this.soundFile, loops);
    }
    /**
     * 
     * Static version of playSound with loops of 0 to allow access to playing sound files outside of SoundManager objects.
     */
    public static void playSound (Clip soundFile)
    {
        playSound(soundFile, 0);
    }

    /**
     * 
     * Static version of playSound to allow access to playing sound files outside of SoundManager objects.
     */
    public static void playSound (Clip soundFile, int loops)
    {
        if(soundFile != null)
        {
            if(soundFile.isRunning()){
                soundFile.stop();
            }
            soundFile.setFramePosition(0);
            soundFile.loop(loops);
        }
        else{
            System.out.println("Sound file is null");
        }

    }

    /**
     * Stops playback of soundfile
     */
    public void stopSound(){
        stopSound(this.soundFile);
    }
    /**
     * Stops playback of soundfile
     */
    public static void stopSound(Clip soundFile){
        if(soundFile != null){
            if(soundFile.isRunning()){
                soundFile.stop();
            }
        }
    }

    /**
     * Records existence state of soundFile
     * @return
     */
    public boolean isRunning()
    {
        return this.soundFile.isRunning();
    }
}