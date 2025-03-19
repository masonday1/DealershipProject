package javafiles.dataaccessfiles;

import java.util.Map;

public enum Key {
    DEALERSHIP_ID ("dealership_id", String.class, false),
    DEALERSHIP_NAME ("dealership_name", String.class, false),
    DEALERSHIP_RENTAL_STATUS ("dealership_rental_status", String.class, false),

    VEHICLE_TYPE ("vehicle_type", String.class, true),
    VEHICLE_MANUFACTURER ("vehicle_manufacturer", String.class, false),
    VEHICLE_MODEL ("vehicle_model", String.class, true),

    VEHICLE_ID ("vehicle_id", String.class, true),
    VEHICLE_RENTAL_STATUS ("vehicle_rental_status", String.class, false),

    VEHICLE_PRICE ("price", Long.class, true),
    VEHICLE_PRICE_UNIT ("price_unit", String.class, false),

    VEHICLE_ACQUISITION_DATE ("acquisition_date", Long.class, false);

    private final String KEY; // should be the same as JSONIO key
    private final Class<?> CLASS;
    // ^would love to make it a generic instead, but can't seem to do that with enum
    private final boolean needed;

    Key(String key, Class<?> c, boolean needed) {
        KEY = key;
        CLASS = c;
        this.needed = needed;
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

    public boolean getNeeded() {
        return needed;
    }

    /**
     * Confirms that the Object in map at KEY is an instance of the correct type.
     * (As given by CLASS)
     *
     * @param map The {@link Map} whose contents type is being checked
     * @return Whether object is of the correct type.
     */
    public boolean validObjectType(Map<String, Object> map) {
        if (!map.containsKey(KEY)) {return true;}
        Object object = map.get(KEY);
        if (CLASS.equals(Long.class)) {
            return object instanceof Long;
        }
        if (CLASS.equals(String.class)){
            return object instanceof String;
        }

        return false;
    }

    public String getClassName() {
        return CLASS.getName();
    }
}