import dev.jcps.JavaAppletAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;

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
        jpTest t = new jpTest();
        String testWav = "https://github.com/Java-Code-Phoenix-Society/ATrain/raw/v1.0.0/src/main/resources/pin.wav";
        Clip wav = t.getAudioClip(testWav.substring(0, testWav.lastIndexOf("/") + 1), "pin.wav");
        assertNotNull(wav);
    }

    @Test
    public void test_getDB3() {
        String testWav = "./pin.wav";
        Clip wav = t.getAudioClip(testWav.substring(0, testWav.lastIndexOf("/") + 1), "");
        assertNotNull(wav);
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
    public void test_getImage() {
        // can't load images from URLs
        String testImg = "https://jcps.dev/assets/jcps-logo.png";
        Image img = t.getImage(testImg.substring(0, testImg.lastIndexOf("/") + 1),
                testImg.substring(testImg.lastIndexOf("/") + 1));
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

    static class jpTest extends JPanel implements JavaAppletAdapter {
        public jpTest() {
            setLayout(null);
            setBounds(0, 0, 100, 100);
            add(new JLabel("Hello World"));
        }
    }
}