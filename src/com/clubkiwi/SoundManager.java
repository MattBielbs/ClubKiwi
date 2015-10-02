package com.clubkiwi;
import javax.sound.sampled.*;

/**
 * Created by Mathew on 10/2/2015.
 */
public class SoundManager
{
    private ClubKiwi ck;
    private Clip clip;

    public SoundManager(ClubKiwi ck)
    {
        this.ck = ck;

        try
        {
            Clip clip = AudioSystem.getClip();
        }
        catch(Exception ex)
        {
            System.out.println("Could not load SoundManager: " + ex.getMessage());
        }

        playAudio("Song1");

    }

    private void playAudio(String key)
    {
        try
        {
            clip.open(ck.resMgr.getAudio(key));
            clip.start();
        }
        catch(Exception ex)
        {
            System.out.println("Could not play audio: " + ex.getMessage());
        }
    }
}
