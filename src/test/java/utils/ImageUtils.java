package utils;

import aquality.selenium.browser.AqualityServices;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageUtils {
    public static BufferedImage getImageFromUrl(String URL) {
        try {
            URL url = new URL(URL);
            return ImageIO.read(url);
        } catch (Exception e) {
            AqualityServices.getLogger().error(e.getMessage());
            return null;
        }
    }

    public static boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
        // The images must be the same size.
        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
            return false;
        }

        int width  = imgA.getWidth();
        int height = imgA.getHeight();

        // Loop over every pixel.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Compare the pixels for equality.
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static BufferedImage getImageFromTheFile(String filePath) {
       try {
           return ImageIO.read(new File(filePath));
       } catch (Exception e) {
           AqualityServices.getLogger().error(e.getMessage());
           return null;
       }
    }
}
