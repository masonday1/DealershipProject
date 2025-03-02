package javaFiles;

import java.util.Map;

public class GetMapInfo {
    private static GetMapInfo instance = null;
    private final String[] keys;

    private GetMapInfo() {
        keys = new String[] {
                "dealership_id", "vehicle_type",
                "vehicle_manufacturer", "vehicle_model",
                "vehicle_id", "price", "acquisition_date"};
    }

    /**
     * Return the singleton instance of the Class.
     *
     * @return The singleton instance of the Class
     */
    public static GetMapInfo getInstance() {
        if (instance == null) {
            instance = new GetMapInfo();
        }
        return instance;
    }

    /**
     * Return the list of all keys used for Vehicle Maps
     *
     * @return All keys used for Vehicle Maps.
     */
    public String[] getAllKeys() {
        return keys;
    }

    /**
     * Returns the given key. Helps avoid repeating / misspelling.
     */
    public String getDealIDKey() {       return keys[0];}
    public String getTypeKey() {         return keys[1];}
    public String getManufacturerKey() { return keys[2];}
    public String getModelKey() {        return keys[3];}
    public String getVehicleIDKey() {    return keys[4];}
    public String getPriceKey() {        return keys[5];}
    public String getDateKey() {         return keys[6];}

    /**
     * Returns the value at the given key. Helps avoid repeating / misspelling.
     */
    public String getDealIDVal(Map<String, Object> map) {
        return (String) map.get(getDealIDKey());
    }
    public String getTypeVal(Map<String, Object> map) {
        return (String) map.get(getTypeKey());
    }
    public String getManufacturerVal(Map<String, Object> map) {
        return (String) map.get(getManufacturerKey());
    }
    public String getModelVal(Map<String, Object> map) {
        return (String) map.get(getModelKey());
    }
    public String getVehicleIDVal(Map<String, Object> map) {
        return (String) map.get(getVehicleIDKey());
    }
    public long getPriceVal(Map<String, Object> map) {
        return (long) map.get(getPriceKey());
    }
    public long getDateVal(Map<String, Object> map) {
        return (long) map.get(getDateKey());
    }
}
