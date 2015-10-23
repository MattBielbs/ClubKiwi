package com.clubkiwi.Managers;

import com.clubkiwi.ClubKiwi;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mathew on 10/2/2015.
 * Control all the loading of resources since theres getting too many now.
 */
public class ResourceManager
{
    private ClassLoader cldr;
    private Image missingImage;
    private AudioInputStream missingAudio;
    private HashMap<String, Image> loadedImages = new HashMap<>();
    private HashMap<String, AudioInputStream> loadedSounds = new HashMap<>();

    public ResourceManager()
    {
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
        missingImage = makeColorTransparent(ImageIO.read(cldr.getResource("Image/missing.png")), Color.WHITE);

        //Load all other images.
        loadedImages.put("kiwi", makeColorTransparent(ImageIO.read(cldr.getResource("Image/kiwi.png")), Color.WHITE));
        loadedImages.put("poof", makeColorTransparent(ImageIO.read(cldr.getResource("Image/poof.png")), Color.WHITE));
        loadedImages.put("bg", makeColorTransparent(ImageIO.read(cldr.getResource("Image/bg.png")), Color.WHITE));
        loadedImages.put("bg2", makeColorTransparent(ImageIO.read(cldr.getResource("Image/bg2.png")), Color.WHITE));
        loadedImages.put("login", makeColorTransparent(ImageIO.read(cldr.getResource("Image/login.png")), Color.WHITE));
        loadedImages.put("apple", makeColorTransparent(ImageIO.read(cldr.getResource("Image/apple.png")), Color.WHITE));
        loadedImages.put("bush", makeColorTransparent(ImageIO.read(cldr.getResource("Image/bushGONESEXUAL.png")), Color.WHITE));
    }

    private void loadSounds() throws UnsupportedAudioFileException, IOException
    {
        //Load missing sound
        missingAudio =  AudioSystem.getAudioInputStream(new BufferedInputStream(cldr.getResourceAsStream("Audio/ding.wav")));

        //Load the rest
        loadedSounds.put("Song1", AudioSystem.getAudioInputStream(new BufferedInputStream(cldr.getResourceAsStream("Audio/SkrjuWavesToLullaby.wav"))));
        loadedSounds.put("Song2", AudioSystem.getAudioInputStream(new BufferedInputStream(cldr.getResourceAsStream("Audio/DualtraxCorostheme.wav"))));
        loadedSounds.put("Song3", AudioSystem.getAudioInputStream(new BufferedInputStream(cldr.getResourceAsStream("Audio/ScribbleSwann.wav"))));
        loadedSounds.put("Song4", AudioSystem.getAudioInputStream(new BufferedInputStream(cldr.getResourceAsStream("Audio/AdhesivewombatHourglass.wav"))));
    }

    public Image getImage(String key)
    {
        //Return a missing image to handle it nicely.
        return loadedImages.getOrDefault(key, missingImage);
    }

    public AudioInputStream getAudio(String key)
    {
        return loadedSounds.getOrDefault(key, missingAudio);
    }

    public ArrayList<AudioInputStream> getSoundByKeyContains(String key)
    {
        ArrayList<AudioInputStream> temp = new ArrayList<>();
        for(Map.Entry<String, AudioInputStream> entry : loadedSounds.entrySet())
        {
            if(entry.getKey().contains(key))
                temp.add(entry.getValue());
        }

        return temp;
    }


    //borrowed function
    private Image makeColorTransparent (Image im, final Color color)
    {
        ImageFilter filter = new RGBImageFilter() {
            // the color we are looking for... Alpha bits are set to opaque
            public final int markerRGB = color.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) {
                if ( ( rgb | 0xFF000000 ) == markerRGB ) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                }
                else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
}
