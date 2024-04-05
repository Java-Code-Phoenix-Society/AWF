package dev.jcps;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

/**
 * The {@code JavaAppletAdapter} interface defines methods for adapting Java applet functionality to standard applications.
 * <p>
 * This interface serves as a bridge between Java applet-based code and standard Java applications.
 * Implementations of this interface provide functionality to emulate applet-specific behaviors such as
 * retrieving document base and applet parameters.
 * </p>
 * <p>
 * Implementations of this interface can be used to facilitate the migration of
 * Java applet-based code to standalone applications.
 * </p>
 * <p>
 * Example usage:
 * <pre>{@code
 * public class MyAppletAdapterImpl implements JavaAppletAdapter {
 *     // Implement methods of the JavaAppletAdapter interface
 *     // ...
 * }
 * }</pre>
 * </p>
 *
 * @since 1.0
 */
public interface JavaAppletAdapter {
    HashMap<String, String> paramMap = new HashMap<>();

    /**
     * Retrieves an audio clip from the specified location relative to the document base.
     * <p>
     * This method attempts to load an audio clip from the specified location {@code s} relative tothe document base {@code documentBase}.
     * It first checks if the document base path ends with a file separator (\ or /), and if not, appends the appropriate file separator.
     * It then attempts to load the audio clip using {@link AudioSystem#getClip()}
     * and opens it with the audio input stream obtained from the specified file path.
     * If loading the audio clip fails due to unsupported audio file format or unavailability of audio resources,
     * an attempt is made to load the audio clip from the specified location using a URL obtained from
     * the class's resource and appending the specified path.
     * If loading the audio clip still fails, an error message is printed to the console.
     * </p>
     *
     * @param documentBase a {@code String} representing the document base directory where the audio clip is located.
     * @param fileName     a {@code String} representing the file name of the audio clip relative to the document base.
     * @return A {@code Clip} object representing the loaded audio clip. If the audio clip cannot be loaded, {@code null} is returned.
     */
    default Clip getAudioClip(String documentBase, String fileName) {
        Clip clip = null;
        String errors = "";
        try {
            URL url = new URL(documentBase + fileName);
            // If documentBase is a valid URL, load the audio clip directly from the URL
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
        } catch (MalformedURLException e) {
            // If documentBase is not a valid URL, treat it as a file path
            try {
                // Ensure the document base ends with the appropriate file separator
                if (!documentBase.endsWith(File.separator)) {
                    documentBase = documentBase + File.separator;
                }

                // Attempt to load the audio clip from the specified file path
                String fullPath = documentBase + fileName;
                clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(new File(fullPath)));
            } catch (UnsupportedAudioFileException | LineUnavailableException ex) {
                // Handle exceptions related to unsupported audio files or unavailable lines
                errors = "ERROR A1: " + ex.getMessage();
            } catch (IOException ex) {
                // Handle IO errors
                errors = "IO Error I1: " + ex.getMessage();
                clip = tryClipLoad(fileName);
            }
        } catch (UnsupportedAudioFileException | LineUnavailableException ex) {
            // Handle exceptions related to unsupported audio files or unavailable lines
            errors = "ERROR A2: " + ex.getMessage();
        } catch (IOException ex) {
            // Handle IO errors
            errors = "IO Error I2: " + ex.getMessage();
            clip = tryClipLoad(fileName);
        }
        System.out.printf(errors);
        return clip;
    }

    /**
     * Tries to load an audio clip from the specified location.
     * <p>
     * This method attempts to load an audio clip from the specified location {@code s}.
     * It first checks if the document base path ends with a file separator (\ or /), and if not, appends the appropriate file separator.
     * It then attempts to load the audio clip using {@link AudioSystem#getClip()}
     * and opens it with the audio input stream obtained from the specified file path.
     * If loading the audio clip fails due to unsupported audio file format or unavailability of audio resources,
     * an attempt is made to load the audio clip from the specified location using a URL obtained from
     * the class's resource and appending the specified path.
     *
     * @param fileName a {@code String} representing the file name of the audio clip relative to the document base.
     * @return A {@code Clip} object representing the loaded audio clip. If the audio clip cannot be loaded, {@code null} is returned.
     */
    private Clip tryClipLoad(String fileName) {
        Clip clip = null;
        URL url = null;
        String errors = "";
        try {
            clip = AudioSystem.getClip();
            url = new URL(this.getClass().getResource("") + fileName);
            clip.open(AudioSystem.getAudioInputStream(url));
        } catch (Exception e) {
            String urlString = "";
            errors = "ERROR A3: " + this.getClass().getResource("") + fileName + "\n" + e.getMessage();
            // Find the index of "!/" in the URL
            try {
                urlString = Objects.requireNonNull(url).toString();
            } catch (Exception ignored) {
            }
            int index = urlString.indexOf("!/");
            if (index != -1) {
                // Find the last occurrence of "/" before "!/"
                int lastSlashIndex = urlString.lastIndexOf("/");
                if (lastSlashIndex != -1) { // If "/" is found before "!/"
                    String trimmedUrl = urlString.substring(0, index + 1) + urlString.substring(lastSlashIndex);
                    try {
                        clip = AudioSystem.getClip();
                        clip.open(AudioSystem.getAudioInputStream(new URL(trimmedUrl)));
                    } catch (Exception ex) {
                        errors = "ERROR A4: " + ex.getMessage();
                    }
                }
            }
        }
        System.out.println(errors);
        return clip;
    }

    /**
     * Replacement function for the applet API {@code getDocumentBase()} method.
     * <p>
     * This method is used to retrieve the document base of the application.
     * It is used to resolve relative paths to resources such as images and sound effects.
     * The document base is the directory in which the application is stored and is used to resolve relative
     * paths to resources. The default implementation returns the current working directory.
     * </p>
     * <p>
     * This method is used by the {@link JavaAppletAdapter}.
     * </p>
     *
     * @return a {@code String} representing the document base, which is the current working directory.
     */
    default String getDocumentBase() {
        String s = "";
        try {
            // get working directory
            s = System.getProperty("user.dir");
        } catch (final SecurityException e) {
            System.out.println("Security exception: " + e.getMessage());
        }
        return s;
    }

    /**
     * Returns the code base location of the current class.
     *
     * @return The code base location as a {@code java.lang.Object}. If an exception occurs during
     * retrieval, {@code null} is returned.
     */
    default Object getCodeBase() {
        Object o = null;
        try {
            o = this.getClass().getResource("/");
        } catch (final Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        if (o == null) {
            String p;
            try {
               p = Objects.requireNonNull(this.getClass().getResource("")).toString();
            } catch (Exception e) {
                p = "/";
            }
            o = p.substring(0, p.lastIndexOf("!/") + 2);
        }
        return o;
    }

    /**
     * Loads an image from either a file path or a resource within the classpath.
     * <p>
     * It first checks if the document base path ends with a file separator (\ or /), and if not, appends the appropriate file separator.
     * It then attempts to load the image from the specified file path using {@link ImageIO#read(File)}.
     * If loading the image fails, an attempt is made to load the image from the specified location using a URL obtained from
     * the class's resource and appending the specified path.
     * If loading the image still fails, an error message is printed to the console.
     * </p>
     *
     * @param o        The base path or directory where the image file is located. Can be {@code null}.
     * @param fileName The name of the image file.
     * @return An {@code Image} object representing the loaded image. If the image cannot be loaded,
     * {@code null} is returned.
     */
    default Image getImage(String o, String fileName) {
        Image image = null;
        String msg = "";
        boolean loaded = false;

        // If the base path is null, set it to an empty string
        if (o == null) {
            o = "";
        }

        // Ensure the base path ends with a slash
        if (o.lastIndexOf("/") != o.length() - 1) {
            o = o + "/";
        }

        // Attempt to load the image from a file
        try {
            image = ImageIO.read(new File(o + fileName));
            loaded = true;
        } catch (final IOException e) {
            msg = "Failed to load image: " + o + fileName;
            try {
                // If loading from file fails, attempt to load from the classpath resources
                image = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/" + fileName)));
                loaded = true;
            } catch (final Exception ex) {
                msg += " & couldn't load resource: ";
            }
        }

        // If the image loading fails, print an error message
        if (!msg.isEmpty() && !loaded) {
            System.out.println(msg);
        }
        return image;
    }

    /**
     * <p>
     * This method is used to retrieve the value of a specific parameter .
     * </p>
     *
     * @param key a {@code String} representing the name of the parameter to retrieve.
     * @return String value associated to the {@code key}, or {@code null}
     */
    default String getParameter(String key) {
        return paramMap.get(key);
    }

    /**
     * Replacement function for the applet API {@code showStatus()} method.
     * <p>
     * This method is used to display a status message for the application.
     * The default implementation prints the specified message to the console.
     * </p>
     *
     * @param s the status message to be displayed.
     */
    default void showStatus(String s) {
        System.out.println(s);
    }
}
