package javaFiles;

import java.util.Map;

public enum Key {
        DEALERSHIP_ID ("dealership_id"),
        VEHICLE_TYPE ("vehicle_type"),
        VEHICLE_MANUFACTURER ("vehicle_manufacturer"),
        VEHICLE_MODEL ("vehicle_model"),
        VEHICLE_ID ("vehicle_id"),
        VEHICLE_PRICE ("price"),
        VEHICLE_ACQUISITION_DATE ("acquisition_date");

    private static String[] keys = null;
    private final String KEY;

    Key(String key) {KEY = key;}

    public String getKey() {return KEY;}

    /**
     * Returns the String value in the Map with the given KEY, null if
     * that Object is not supposed to be a String.
     *
     * @param map The Map that is being evaluated
     * @return The String value in the Map at key: KEY
     */
    public String getValString(Map<String, Object> map) {
        return switch (this) {
            case DEALERSHIP_ID, VEHICLE_TYPE, VEHICLE_MANUFACTURER,
                 VEHICLE_MODEL, VEHICLE_ID -> (String) map.get(KEY);
            default -> null;
        };
    }

    /**
     * Returns the Long value in the Map with the given KEY, null if
     * that Object is not supposed to be a Long.
     *
     * @param map The Map that is being evaluated.
     * @return The Long value in the Map at key: KEY
     */
    public Long getValLong(Map<String, Object> map) {
        return switch (this) {
            case VEHICLE_PRICE, VEHICLE_ACQUISITION_DATE -> (Long) map.get(KEY);
            default -> null;
        };
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