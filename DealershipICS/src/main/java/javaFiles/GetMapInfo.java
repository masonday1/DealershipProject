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

    public static GetMapInfo getInstance() {
        if (instance == null) {
            instance = new GetMapInfo();
        }
        return instance;
    }

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
