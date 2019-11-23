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
     */
    private Clip soundFile;

    /**
     * TODO Docs
     *
     */
    private int loops;

    /**
     * TODO Docs
     * @param soundFile
     */
    public SoundManager(String soundFile)
    {
        this(soundFile, 0);
    }

    /**
     * TODO Docs
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

    public void playSound()
    {
        playSound(this.soundFile, loops);
    }
    /**
     * TODO Docs
     * Static to allow access to playing sound files outside of SoundManager objects.
     */
    public static void playSound (Clip soundFile)
    {
        playSound(soundFile, 0);
    }

    /**
     * TODO Docs
     * Static to allow access to playing sound files outside of SoundManager objects.
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
     * TODO Docs
     */
    public void stopSound(){
        stopSound(this.soundFile);
    }
    /**
     * TODO Docs
     */
    public static void stopSound(Clip soundFile){
        if(soundFile != null){
            if(soundFile.isRunning()){
                soundFile.stop();
            }
        }
    }

    /**
     * TODO Docs
     * @return
     */
    public boolean isRunning()
    {
        return this.soundFile.isRunning();
    }
}