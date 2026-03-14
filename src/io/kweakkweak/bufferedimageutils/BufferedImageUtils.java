package io.kweakkweak.bufferedimageutils;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.OptionalDouble;

public final class BufferedImageUtils {
  public static BufferedImage negate(BufferedImage sourceImage) {
    BufferedImage image = sourceImage;

    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        image.setRGB(x, y, ~sourceImage.getRGB(x, y));
      }
    }

    return sourceImage;
  }

  public static BufferedImage toBlackAndWhite(BufferedImage sourceImage) {
    // TODO: Optimize it
    BufferedImage image = sourceImage;

    int[] keyColors = getLowestAndHighestColors(sourceImage);
    int lowColor = keyColors[0];
    int highColor = keyColors[1];

    // Placing black and white colors
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        int rgb = sourceImage.getRGB(x, y);
        if (rgb - lowColor < highColor - rgb) image.setRGB(x, y, 0x000000);
        else image.setRGB(x, y, 0xFFFFFF);
      }
    }
    return image;
  }

  public static BufferedImage duplicate(BufferedImage sourceImage, int xCount, int yCount) {
    if (xCount < 0 || yCount < 0)
      throw new IllegalArgumentException("Axises must be natural numbers");
    else if (xCount == 0 || yCount == 0)
      return sourceImage;

    BufferedImage image = new BufferedImage(sourceImage.getWidth() * xCount, sourceImage.getHeight() * yCount, sourceImage.getType());

    // Duplicate
    for (int y = 0; y < sourceImage.getHeight(); y++) {
      for (int x = 0; x < sourceImage.getWidth(); x++) {
        for (int toY = 0; toY < yCount; toY++) {
          for (int toX = 0; toX < xCount; toX++) {
            image.setRGB(x + (toX * sourceImage.getWidth()), y + (toY * sourceImage.getHeight()), sourceImage.getRGB(x, y));
          }
        }
      }
    }

    return image;
  }

  public static BufferedImage pixelize(BufferedImage sourceImage, int level) {
    if (sourceImage.getWidth() < level || sourceImage.getHeight() < level)
      throw new IllegalArgumentException("Level must be lower than width and height of image");
    else if (level <= 0)
      throw new IllegalArgumentException("Level must be fewer than zero");

    BufferedImage image = new BufferedImage(sourceImage.getWidth() / level, sourceImage.getHeight() / level, sourceImage.getType());

    // Pixelize image
    for (int imageX = 0; imageX < image.getWidth(); imageX++) {
      for (int imageY = 0; imageY < image.getHeight(); imageY++) {
        image.setRGB(imageX, imageY, sourceImage.getRGB(imageX * level, imageY * level));
      }
    }

    return image;
  }

  public static BufferedImage[] spread(BufferedImage sourceImage, int toX, int toY) {
    // TODO: Make a massive of massives with rows and columns
    int maxX = sourceImage.getWidth() / toX;
    int maxY = sourceImage.getHeight() / toY;
    int imageNum = 0;

    BufferedImage[] images = new BufferedImage[toX * toY];

    for (int iY = 0; iY < toY; iY++) {
      for (int iX = 0; iX < toX; iX++) {
        images[imageNum++] = sourceImage.getSubimage(maxX * iX, maxY * iY, maxX, maxY);
      }
    }
    return images;
  }

  public static BufferedImage reverseInX(BufferedImage sourceImage) {
    BufferedImage image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());

    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        image.setRGB(x, y, sourceImage.getRGB(image.getWidth() - (x + 1), y));
      }
    }

    return image;
  }

  public static BufferedImage reverseInY(BufferedImage sourceImage) {
    BufferedImage image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());

    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        image.setRGB(x, y, sourceImage.getRGB(x, image.getHeight() - (y + 1)));
      }
    }

    return image;
  }

  public static BufferedImage toWaves(BufferedImage sourceImage, int parts) {
    BufferedImage image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());

    for (byte part = 1; part < parts; part++) {
      for (int x = 0; x < image.getWidth(); x++) {
        int partHeight = (sourceImage.getHeight() / parts);
        int[] pixels = new int[partHeight];
        int y = (part - 1) * (partHeight);
        for (int pixel = 0; pixel < partHeight; pixel++) {
          pixels[pixel] = sourceImage.getRGB(x, y);
          y++;
        }

        OptionalDouble average = Arrays.stream(pixels).average();
        int func = (int) (((sourceImage.getHeight() / parts) * part) + (Math.round(Math.sin(0.5 * x) * (average.getAsDouble() / (0xFFFFFF / 10)))));

        // Где 0.5, вполне рабочий коэффициент растажения вдоль оси x

        image.setRGB(x, func, 0xFFFFFF);
      }
    }

    return image;
  }

  private static int[] getLowestAndHighestColors(BufferedImage sourceImage) {
    int lowColor = 0xFFFFFF;
    int highColor = 0x000000;

    for (int x = 0; x < sourceImage.getWidth(); x++) {
      for (int y = 0; y < sourceImage.getHeight(); y++) {
        int rgb = sourceImage.getRGB(x, y);
        if (rgb < lowColor) lowColor = rgb;
        if (rgb > highColor) highColor = rgb;
      }
    }

    return new int[]{lowColor, highColor};
  }

  private BufferedImageUtils() {}
}