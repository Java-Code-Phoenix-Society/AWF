package dev.jcps;

import java.util.HashMap;


/**
 * The {@code AppletParameters} class serves as a central repository for managing application parameters,
 * originally received from the HTML page when was originally an applet. It maintains a HashMap
 * to store variables and parameters.
 * <p>
 * The parameters managed by this class control various aspects of the applet's behaviour and
 * appearance.
 * </p>
 *
 * @author neoFuzz
 * @since 1.0
 */
public class AppletParameters {
    /**
     * A HashMap that stores key-value pairs representing various applet parameters.
     * The keys are String identifiers for the parameters, and the values are their corresponding settings.
     */
    public HashMap<String, String> paramMap;

    /**
     * Constructs a {@code AppletParameters} object and initialises it with a blank HashMap,
     * represented as key-value pairs in the {@code paramMap}.
     */
    public AppletParameters() {
        paramMap = new HashMap<>();
    }

    /**
     * Constructs a {@code AppletParameters} object and initialises it with a given set of settings.
     * The settings are provided as a HashMap of key-value pairs, which are then stored in the paramMap.
     *
     * @param hashMap The HashMap containing key-value pairs representing various parameters.
     */
    public AppletParameters(HashMap<String, String> hashMap) {
        paramMap = new AppletParameters().paramMap;
        paramMap.putAll(hashMap);
    }

    /**
     * Retrieves an integer value associated with a specific key from the paramMap.
     *
     * @param key The key associated with the integer value to be retrieved.
     * @return The integer value associated with the specified key.
     * @throws NumberFormatException If the value associated with the key cannot be parsed as an integer.
     */
    public int getInt(String key) {
        String value = paramMap.get(key);
        if (value == null) return 0;
        return Integer.parseInt(value);
    }

    /**
     * Associates a specific integer value with a specific key in the paramMap.
     * If the key is already present in the paramMap, this will update its associated value.
     *
     * @param key The key with which the specified integer value is to be associated.
     * @param i   The integer value to be associated with the specified key.
     */
    public void putInt(String key, int i) {
        paramMap.put(key, String.valueOf(i));
    }
}
