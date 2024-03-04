package org.jcps;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * The {@code JavaAppletAdapter} interface defines methods for adapting Java applet functionality to standard applications.
 * <p>
 * This interface serves as a bridge between Java applet-based code and standard Java applications.
 * Implementations of this interface provide functionality to emulate applet-specific behaviors such as retrieving document base and applet parameters.
 * </p>
 * <p>
 * Implementations of this interface can be used to facilitate the migration of Java applet-based code to standalone applications.
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
    /**
     * Retrieves an audio clip from the specified location relative to the document base.
     * <p>
     * This method attempts to load an audio clip from the specified location {@code s} relative to the document base {@code documentBase}.
     * It first checks if the document base path ends with a file separator (\ or /), and if not, appends the appropriate file separator.
     * It then attempts to load the audio clip using {@link javax.sound.sampled.AudioSystem#getClip()} and opens it with the audio input stream obtained from the specified file path.
     * If loading the audio clip fails due to unsupported audio file format or unavailability of audio resources, an attempt is made to load the audio clip from the specified location using a URL obtained from the class's resource and appending the specified path.
     * If loading the audio clip still fails, an error message is printed to the console.
     * </p>
     *
     * @param documentBase a {@code String} representing the document base directory where the audio clip is located.
     * @param fileName a {@code String} representing the file name of the audio clip relative to the document base.
     * @return a {@code Clip} object representing the loaded audio clip, or {@code null} if loading fails.
     */
    default Clip getAudioClip(String documentBase, String fileName) {
        Clip clip = null;
        if (documentBase.lastIndexOf("\\") != documentBase.length() - 1 ||
                documentBase.lastIndexOf("/") != documentBase.length() - 1) {
            documentBase = documentBase + File.separator;
        }
        try {
            clip = AudioSystem.getClip();
            String fullPath = documentBase + fileName;
            clip.open(AudioSystem.getAudioInputStream(new File(fullPath)));

        } catch (UnsupportedAudioFileException | LineUnavailableException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } catch (IOException e) {
            try {
                clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(new URL(this.getClass().getResource("/") + fileName)));
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
                System.out.println("getAudioClip error: " + fileName + "\n" + ex.getMessage());
            }
        }
        return clip;
    }

    /**
     * Replacement function for the applet API {@code getDocumentBase()} method.
     * <p>
     * This method is used to retrieve the document base of the application.
     * It is used to resolve relative paths to resources such as images and sound effects.
     * The document base is the directory in which the application is stored and is used to resolve relative paths to resources.
     * The default implementation returns the current working directory.
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
}
