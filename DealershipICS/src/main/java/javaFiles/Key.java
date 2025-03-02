package javaFiles;

import java.util.Map;

public enum Key {
        DEALERSHIP_ID ("dealership_id", String.class.getName()),
        VEHICLE_TYPE ("vehicle_type", String.class.getName()),
        VEHICLE_MANUFACTURER ("vehicle_manufacturer", String.class.getName()),
        VEHICLE_MODEL ("vehicle_model", String.class.getName()),
        VEHICLE_ID ("vehicle_id", String.class.getName()),
        VEHICLE_PRICE ("price", Long.class.getName()),
        VEHICLE_ACQUISITION_DATE ("acquisition_date", Long.class.getName());

    private static String[] keys = null;
    private final String KEY;
    private final String CLASS;
    // ^would love to make it a generic instead, but can't seem to do that with enum

    Key(String key, String c) {
        KEY = key;
        CLASS = c;
    }

    public String getKey() {return KEY;}

    /**
     * Returns the String value in the Map with the given KEY, null if
     * that Object is not supposed to be a String.
     *
     * @param map The Map that is being evaluated
     * @return The String value in the Map at key: KEY
     */
    public String getValString(Map<String, Object> map) {
        if (CLASS.equals(String.class.getName())) {
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
        if (CLASS.equals(Long.class.getName())) {
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