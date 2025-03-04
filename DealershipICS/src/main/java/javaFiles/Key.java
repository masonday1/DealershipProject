package javaFiles;

import java.util.Map;

public enum Key {
        DEALERSHIP_ID ("dealership_id", String.class),
        VEHICLE_TYPE ("vehicle_type", String.class),
        VEHICLE_MANUFACTURER ("vehicle_manufacturer", String.class),
        VEHICLE_MODEL ("vehicle_model", String.class),
        VEHICLE_ID ("vehicle_id", String.class),
        VEHICLE_PRICE ("price", Long.class),
        VEHICLE_ACQUISITION_DATE ("acquisition_date", Long.class);

    private static String[] keys = null;
    private final String KEY;
    private final Class<?> CLASS;
    // ^would love to make it a generic instead, but can't seem to do that with enum


    Key(String key, Class<?> c) {
        KEY = key;
        CLASS = c;
    }

    // Getters and Setters (only one true Getter / Setter);
    public String getKey() {return KEY;}

    /**
     * Returns the String value in the Map with the given KEY, null if
     * that Object is not supposed to be a String.
     *
     * @param map The Map that is being evaluated
     * @return The String value in the Map at key: KEY
     */
    public String getValString(Map<String, Object> map) {
        if (CLASS.equals(String.class)) {
            return (String) map.get(KEY);
        }
        return null;
    }

    /**
     * Returns the Long value in the Map with the given KEY, null if
     * that Object is not supposed to be a Long.
     *
     * @param map The Map that is being evaluated.
     * @return The Long value in the Map at key: KEY
     */
    public Long getValLong(Map<String, Object> map) {
        if (CLASS.equals(Long.class)) {
            return (Long) map.get(KEY);
        }
        return null;
    }

    /**
     * Returns a String[] of all KEY values in Key.
     *
     * @return A String[] of all KEY values in Key
     */
    public static String[] getKeys() {
        if (keys == null) {
            Key[] keyEnums = Key.values();
            keys = new String[keyEnums.length];
            for (int i = 0; i < keys.length; i++) {
                keys[i] = keyEnums[i].getKey();
            }
        }
        return keys;
    }
}