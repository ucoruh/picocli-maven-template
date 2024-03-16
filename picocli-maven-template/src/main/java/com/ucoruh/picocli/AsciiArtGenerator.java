package com.ucoruh.picocli;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.WHITE;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.Color.BLACK;
import org.fusesource.jansi.AnsiConsole;

/**
 * ASCII-ART Generator for Console
 *
 * <p>
 * This class provides methods to generate ASCII art from images for display in
 * the console.
 * </p>
 *
 * <p>
 * This class contains two public methods for generating ASCII art:
 * {@link #generateAsciiArtColorized(String, int, int)} and
 * {@link #generateAsciiArtMonochrome(String, int, int)}.
 * </p>
 *
 * <p>
 * It also contains a private method, {@link #mapGrayToColor(int)}, which maps
 * grayscale values to ANSI color codes for the colorized ASCII art.
 * </p>
 *
 * <p>
 * This class depends on the <a href="https://github.com/fusesource/jansi">Jansi
 * library</a> for ANSI color support in the console.
 * </p>
 *
 * @author ugur.coruh
 */
public class AsciiArtGenerator {

  // Define the characters to use for the ASCII art
  private static final char[] ASCII_CHARS = { '@', '#', 'S', '%', '?', '*', '+', ';', ':', ',', ' ' };

  /**
   * Generate Colorful ASCII Art for Startup for Console Application
   *
   * <p>
   * This method generates colorful ASCII art from the provided image path. The
   * image is resized to fit the specified width and height, and each pixel's
   * grayscale value is mapped to an ANSI color code for colorized output in the
   * console.
   * </p>
   *
   * @param imagePath [input] image path relative or absolute
   * @param width     [input] console image width as character count
   * @param height    [input] console image height as character count
   * @throws IOException if an error occurs while reading the image file
   */
  public static void generateAsciiArtColorized(String imagePath, int width, int height) throws IOException {
    BufferedImage image = ImageIO.read(new File(imagePath));
    BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    resizedImage.createGraphics().drawImage(image, 0, 0, width, height, null);

    StringBuilder asciiArtBuilder = new StringBuilder();

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int pixel = resizedImage.getRGB(x, y);
        int r = (pixel >> 16) & 0xff;
        int g = (pixel >> 8) & 0xff;
        int b = pixel & 0xff;
        int gray = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
        int charIndex = Math.round(((float) gray / 255) * (ASCII_CHARS.length - 1));
        char asciiChar = ASCII_CHARS[charIndex];
        Ansi colorCode = mapGrayToColor(gray);
        asciiArtBuilder.append(colorCode).append(asciiChar);
      }

      asciiArtBuilder.append(ansi().reset()).append('\n');
    }

    AnsiConsole.systemInstall();
    System.out.println(asciiArtBuilder.toString());
    AnsiConsole.systemUninstall();
  }

  /**
   * Generate Monochrome ASCII Art for Startup for Console Application
   *
   * <p>
   * This method generates monochrome ASCII art from the provided image path. The
   * image is resized to fit the specified width and height, and each pixel's
   * grayscale value is mapped to a character representing the intensity of that
   * pixel.
   * </p>
   *
   * @param imagePath [input] image path relative or absolute
   * @param width     [input] console image width as character count
   * @param height    [input] console image height as character count
   * @throws IOException if an error occurs while reading the image file
   */
  public static void generateAsciiArtMonochrome(String imagePath, int width, int height) throws IOException {

    // Read the PNG image
    BufferedImage image = ImageIO.read(new File(imagePath));

    // Resize the image to fit the desired width and height
    BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    resizedImage.createGraphics().drawImage(image, 0, 0, width, height, null);

    // Generate the ASCII art
    StringBuilder asciiArtBuilder = new StringBuilder();

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int pixel = resizedImage.getRGB(x, y);
        int r = (pixel >> 16) & 0xff;
        int g = (pixel >> 8) & 0xff;
        int b = pixel & 0xff;
        int gray = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
        int charIndex = Math.round(((float) gray / 255) * (ASCII_CHARS.length - 1));
        asciiArtBuilder.append(ASCII_CHARS[charIndex]);
      }

      asciiArtBuilder.append('\n');
    }

    // Print the ASCII art to the console
    System.out.println(asciiArtBuilder.toString());
  }

  /**
   * Map Grayscale to ANSI Color
   *
   * <p>
   * This private method maps grayscale values to ANSI color codes for the
   * colorized ASCII art.
   * </p>
   *
   * @param gray [input] grayscale value (0-255)
   * @return ANSI color code corresponding to the grayscale value
   */
  private static Ansi mapGrayToColor(int gray) {
    if (gray <= 30) {
      return ansi().fg(BLACK);
    } else if (gray <= 60) {
      return ansi().fg(RED);
    } else if (gray <= 120) {
      return ansi().fg(GREEN);
    } else if (gray <= 180) {
      return ansi().fg(YELLOW);
    } else if (gray <= 210) {
      return ansi().fg(BLUE);
    } else if (gray <= 240) {
      return ansi().fg(MAGENTA);
    } else {
      return ansi().fg(WHITE);
    }
  }

}
