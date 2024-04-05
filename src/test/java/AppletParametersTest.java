import dev.jcps.AppletParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AppletParametersTest {

    private AppletParameters appletParameters;

    @BeforeEach
    void setUp() {
        appletParameters = new AppletParameters();
    }

    @Test
    void constructor_WithHashMap_PopulatesParamMap() {
        // Arrange
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key1", "value1");
        hashMap.put("key2", "value2");
        hashMap.put("key3", "value3");

        // Act
        AppletParameters appletParameters = new AppletParameters(hashMap);

        // Assert
        assertEquals(3, appletParameters.paramMap.size());
        assertTrue(appletParameters.paramMap.containsKey("key1"));
        assertTrue(appletParameters.paramMap.containsKey("key2"));
        assertTrue(appletParameters.paramMap.containsKey("key3"));
        assertEquals("value1", appletParameters.paramMap.get("key1"));
        assertEquals("value2", appletParameters.paramMap.get("key2"));
        assertEquals("value3", appletParameters.paramMap.get("key3"));
    }

    @Test
    void constructor_WithHashMap_PopulatesParamMap_withInt() {
        // Arrange
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key1", "value1");
        hashMap.put("key2", "value2");
        hashMap.put("key3", "3");

        // Act
        AppletParameters appletParameters = new AppletParameters(hashMap);

        // Assert
        assertEquals(3, appletParameters.paramMap.size());
        assertTrue(appletParameters.paramMap.containsKey("key1"));
        assertTrue(appletParameters.paramMap.containsKey("key2"));
        assertTrue(appletParameters.paramMap.containsKey("key3"));
        assertEquals("value1", appletParameters.paramMap.get("key1"));
        assertEquals("value2", appletParameters.paramMap.get("key2"));
        assertEquals(3, appletParameters.getInt("key3"));
    }

    @Test
    void getInt_ExistingKey_ReturnsIntValue() {
        // Arrange
        appletParameters.putInt("key", 10);

        // Act
        int result = appletParameters.getInt("key");

        // Assert
        assertEquals(10, result);
    }

    @Test
    void getInt_NonExistingKey_ReturnsDefaultValue() {
        // Arrange
        // No key added to the paramMap

        // Act
        int result = appletParameters.getInt("nonExistingKey");

        // Assert
        assertEquals(0, result); // Default value for integer
    }

    @Test
    void putInt_AddingIntValue() {
        // Arrange
        appletParameters.putInt("newKey", 20);

        // Act
        int result = appletParameters.getInt("newKey");

        // Assert
        assertEquals(20, result);
    }

    @Test
    void putInt_UpdatingExistingKey() {
        // Arrange
        appletParameters.putInt("existingKey", 30);

        // Act
        appletParameters.putInt("existingKey", 40);
        int result = appletParameters.getInt("existingKey");

        // Assert
        assertEquals(40, result);
    }
}
