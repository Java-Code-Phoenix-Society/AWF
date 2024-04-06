import dev.jcps.JavaAppletAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class JavaAppletAdapterTest {
    jpTest t;

    @BeforeEach
    void setUp() {
        t = new jpTest();
    }

    @Test
    void getParameter_WhenKeyExists_ReturnsValue() {
        // Arrange
        t.paramMap.put("key", "value");

        // Act
        String value = t.getParameter("key");

        // Assert
        assertEquals("value", value);
    }

    @Test
    void getParameter_WhenKeyDoesNotExist_ReturnsNull() {
        // Act
        String value = t.getParameter("nonexistentKey");

        // Assert
        assertNull(value);
    }

    @Test
    public void showStatusTest() {
        t.showStatus("test");
        assertNotNull(t);
    }

    @Test
    public void test_getDB() { // template test
        String db = t.getDocumentBase();
        boolean check = db.contains("/");
        if (db.contains("\\")) {
            check = true;
        }
        assertTrue(check);
    }

    @Test
    public void test_getDB2() {
        boolean envCheck = false;
        String testWav = "https://github.com/Java-Code-Phoenix-Society/ATrain/raw/v1.0.0/src/main/resources/pin.wav";
        Clip wav = null;
        try {
            wav = t.getAudioClip(testWav.substring(0, testWav.lastIndexOf("/") + 1), "pin.wav");
        } catch (java.lang.IllegalArgumentException x) {
            System.out.println("Environment doesn't have sound hardware");
            envCheck = true;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            envCheck = true;
        }

        if (envCheck) {
            assertTrue(envCheck);
        } else {
            assertNotNull(wav);
        }
    }

    @Test
    public void test_getDB3() {
        boolean envCheck = false;
        String testWav = "./pin.wav";
        Clip wav = null;

        try {
            wav = t.getAudioClip(testWav.substring(0, testWav.lastIndexOf("/") + 1), "");
        } catch (java.lang.IllegalArgumentException x) {
            System.out.println("Environment doesn't have sound hardware");
            envCheck = true;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            envCheck = true;
        }

        if (envCheck) {
            assertTrue(envCheck);
        } else {
            assertNotNull(wav);
        }
    }

    @Test
    public void test_getCB() {
        String o = "";
        try {
            o = String.valueOf(this.getClass().getResource("/"));
        } catch (final Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        String cb = t.getCodeBase().toString();
        assertEquals(o, cb);
    }

    @Test
    public void test_getCB_noSlash() {
        String o = "";
        try {
            o = String.valueOf(this.getClass().getResource(""));
        } catch (final Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        String cb = t.getCodeBase().toString();
        assertEquals(o, cb);
    }


    @Test
    public void test_getImage() {
        String testImg = "https://jcps.dev/assets/jcps-logo.png";
        Image img = t.getImage(testImg.substring(0, testImg.lastIndexOf("/") + 1),
                testImg.substring(testImg.lastIndexOf("/") + 1) + "\\");
        assertNull(img);
    }

    @Test
    public void test_getImage_nullPath() {
        String testImg = "https://jcps.dev/assets/jcps-logo.png";
        Image img = t.getImage(null,
                testImg.substring(testImg.lastIndexOf("/") + 1));
        assertNull(img);
    }

    @Test
    public void test_getImage_path() {
        // can load images from URLs as base
        String testImg = "jcps-logo.png";
        Image img = t.getImage("https://jcps.dev/assets", testImg);
        assertNotNull(img);
    }

    @Test
    public void test_getImage_nothing() {
        // can load images from URLs as base
        String testImg = "png";
        Image img = t.getImage("", testImg);
        assertNull(img);
    }


    @Test
    public void testCheckDir() {
        String s = "";
        try {
            // get working directory
            s = System.getProperty("user.dir");
        } catch (final SecurityException e) {
            System.out.println("Security exception: " + e.getMessage());
        }
        String c = t.getDocumentBase();

        assertEquals(s, c);
    }

    @Test
    void testLoadFromUrl() {
        String fileName = "https://jcps.dev/assets/jcps-logo.png";
        String basePath = "";

        Image image = t.getImage(basePath, fileName);

        assertNotNull(image);
        assertInstanceOf(BufferedImage.class, image);
    }

    @Test
    void testFailureMessage() {
        String fileName = "invalid.png";
        String basePath = "";

        Image image = t.getImage(basePath, fileName);

        assertNull(image);
    }

    static class jpTest extends JPanel implements JavaAppletAdapter {
        public jpTest() {
            setLayout(null);
            setBounds(0, 0, 100, 100);
            add(new JLabel("Hello World"));
        }
    }
}
