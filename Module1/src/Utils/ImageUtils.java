package Utils;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

import static Utils.MathUtils.*; // So we can just call the static function in here without the "MathUtils." part

public class ImageUtils { // Ep 123

    private ImageUtils(){}

    // Creates NEW image
    public static BufferedImage changedBrightness (BufferedImage original, int amount) {

        BufferedImage result = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
        result.getGraphics().drawImage(original, 0, 0, null);
        int[] resultPixels = ((DataBufferInt)result.getRaster().getDataBuffer()).getData();

        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {

                int argb = resultPixels[x + y * original.getWidth()]; //always returns TYPE_INT_ARGB
                int red = 0xFF & ( argb >> 16);
                int alpha = 0xFF & (argb >> 24);
                int blue = 0xFF & (argb >> 0 );
                int green = 0xFF & (argb >> 8 );

                red = clamp(red + amount, 0, 255); // Can be called like this thanks to import
                green = clamp(green + amount, 0, 255);
                blue = clamp(blue + amount, 0, 255);

                resultPixels[x + y * result.getWidth()] = (alpha << 24) | (red << 16 ) | (green<<8) | blue;
            }
        }
        return result;
    }

}
