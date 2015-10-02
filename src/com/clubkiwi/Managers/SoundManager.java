package com.clubkiwi.Managers;
import com.clubkiwi.ClubKiwi;

import javax.sound.sampled.*;
import java.util.ArrayList;

/**
 * Created by Mathew on 10/2/2015.
 */
public class SoundManager implements Runnable
{
    private ClubKiwi ck;
    private Clip bgClip;
    private int bgCurrent = 0;
    private ArrayList<AudioInputStream> bgSongs;


    public SoundManager(ClubKiwi ck)
    {
        this.ck = ck;

        //Load songs
        bgSongs = ck.resMgr.getSoundByKeyContains("Song");

        //Start up
        Thread thread = new Thread(this);
        thread.start();
    }

    public void playAudio(String key, Float volumeMod)
    {
        try
        {
            Clip clip = AudioSystem.getClip();
            clip.open(ck.resMgr.getAudio(key));

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volumeMod);

            clip.start();
        }
        catch(Exception ex)
        {
            System.out.println("Could not play audio: " + ex.getMessage());
        }
    }

    @Override
    public void run()
    {
        while(ck.running)
        {
            try
            {
                if(bgClip == null)
                    bgClip = AudioSystem.getClip();
                else
                {
                    if (!bgClip.isOpen() || bgClip.getMicrosecondLength() == bgClip.getMicrosecondPosition())
                    {
                        bgClip.close();

                        if(bgCurrent >= bgSongs.size() - 1)
                            bgCurrent = 0;
                        else
                            bgCurrent++;

                        bgClip.open(bgSongs.get(bgCurrent));

                        FloatControl gainControl = (FloatControl) bgClip.getControl(FloatControl.Type.MASTER_GAIN);
                        gainControl.setValue(-30.0f);

                        bgClip.start();
                    }
                }
            }
            catch(Exception ex)
            {
                System.out.println("Could not play bgaudio: " + ex.getMessage());
                break;
            }
        }
    }
}
