package com.clubkiwi;

import sun.audio.AudioStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Mathew on 10/2/2015.
 * Control all the loading of resources since theres getting too many now.
 */
public class ResourceManager
{
    private ClubKiwi ck;
    private ClassLoader cldr;
    private BufferedImage missingImage;
    private AudioInputStream missingAudio;
    private HashMap<String, BufferedImage> loadedImages = new HashMap<>();
    private HashMap<String, AudioInputStream> loadedSounds = new HashMap<>();

    public ResourceManager(ClubKiwi ck)
    {
        this.ck = ck;
        cldr = this.getClass().getClassLoader();

        try
        {
            loadImages();
            System.out.println("All images loaded.");
        }
        catch(IOException ex)
        {
            System.out.println("Error loading image " + ex.getMessage());
        }

        try
        {
            loadSounds();
            System.out.println("All sounds loaded.");
        }
        catch (Exception ex)
        {
            System.out.println("Error loading sound " + ex.getMessage());
        }
    }

    private void loadImages() throws IOException
    {
        //Load our missing image.
        missingImage = ImageIO.read(cldr.getResource("Image/missing.png"));

        //Load all other images.
        loadedImages.put("kiwi", ImageIO.read(cldr.getResource("Image/kiwi.png")));
        loadedImages.put("poof", ImageIO.read(cldr.getResource("Image/poof.png")));
        loadedImages.put("bg", ImageIO.read(cldr.getResource("Image/bg.png")));
        loadedImages.put("bg2", ImageIO.read(cldr.getResource("Image/bg2.png")));
        loadedImages.put("login", ImageIO.read(cldr.getResource("Image/login.png")));
        loadedImages.put("apple", ImageIO.read(cldr.getResource("Image/apple.png")));
        loadedImages.put("bush", ImageIO.read(cldr.getResource("Image/bush.png")));
    }

    private void loadSounds() throws UnsupportedAudioFileException, IOException
    {
        //Load missing sound
        missingAudio =  AudioSystem.getAudioInputStream(cldr.getResourceAsStream("Audio/ding.wav"));

        //Load the rest
        loadedSounds.put("Song1", AudioSystem.getAudioInputStream(cldr.getResourceAsStream("Audio/AdhesivewombatHourglass.wav")));
        loadedSounds.put("Song2", AudioSystem.getAudioInputStream(cldr.getResourceAsStream("Audio/DualtraxCorostheme.wav")));
        loadedSounds.put("Song3", AudioSystem.getAudioInputStream(cldr.getResourceAsStream("Audio/ScribbleSwann.wav")));
        loadedSounds.put("Song4", AudioSystem.getAudioInputStream(cldr.getResourceAsStream("Audio/SkrjuWavesToLullaby.wav")));
    }

    public BufferedImage getImage(String key)
    {
        //Return a missing image to handle it nicely.
        return loadedImages.getOrDefault(key, missingImage);
    }

    public AudioInputStream getAudio(String key)
    {
        return loadedSounds.getOrDefault(key, missingAudio);
    }
}
