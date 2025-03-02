package javaFiles;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * A class that reads and writes to JSON files
 *
 * @author Dylan Browne
 */
public class JSONIO
{
    private final File file;
    private final char mode;
    private final static String[] keys = {
            "dealership_id", "vehicle_type",
            "vehicle_manufacturer", "vehicle_model",
            "vehicle_id", "price", "acquisition_date" };

    /**
     * Creates or opens a JSON file with name fileName in read ('r') or write ('w') mode.
     * Read mode allows the reading, but not writing of files, write mode allows for the
     * writing, but not reading of files.
     *
     * @param filePath The path of the file to be opened or created
     * @param mode A char representation of the type of file this is (read 'r' or write 'w')
     * @throws ReadWriteException Thrown if the mode is an invalid char
     */
    public JSONIO(String filePath, char mode) throws ReadWriteException {
        this.mode = getMode(mode);
        if (filePath.endsWith(".json")) {
            file = new File(filePath);
            if (!file.exists() && mode == 'r') {
                throw new ReadWriteException("filePath \"" + filePath +"\" does not exist "
                        + "or could not be found where indicated.");
            }
        } else {
            throw new ReadWriteException("filePath \"" + filePath +"\" is not a .json file. "
                                        + "Make sure to include .json at the end for filePath.");
        }
    }

    /**
     * Takes a char and converts it to an appropriate lowercase version of the mode
     *
     * @param mode A char representation of the type of file this is to be converted to correct form
     * @return The correct form of the char representation (read 'r', write 'w')
     * @throws ReadWriteException Thrown if the mode is an invalid char
     */
    public static char getMode(char mode)  throws ReadWriteException{
        mode = Character.toLowerCase(mode);
        char[] valid_modes = {'r', 'w'};
        for (char mode_lower : valid_modes) {
            if (mode == mode_lower) {
                return mode;
            }
        }

        String message = "Mode '" + mode +
                "' is not in {'r', 'R', 'w', 'W'}, file not opened.";
        throw new ReadWriteException(message);
    }

    /**
     * Returns the given key. Helps avoid repeating / misspelling.
     */
    public static String getDealIdKey() {       return keys[0];}
    public static String getTypeKey() {         return keys[1];}
    public static String getManufacturerKey() { return keys[2];}
    public static String getModelKey() {        return keys[3];}
    public static String getVehicleIdKey() {    return keys[4];}
    public static String getPriceKey() {        return keys[5];}
    public static String getDateKey() {         return keys[6];}

    public static String getDealIdVal(Map<String, Object> map) {
        return (String) map.get(getDealIdKey());
    }
    public static String getTypeVal(Map<String, Object> map) {
        return (String) map.get(getTypeKey());
    }
    public static String getManufacturerVal(Map<String, Object> map) {
        return (String) map.get(getManufacturerKey());
    }
    public static String getModelVal(Map<String, Object> map) {
        return (String) map.get(getModelKey());
    }
    public static String getVehicleIdVal(Map<String, Object> map) {
        return (String) map.get(getVehicleIdKey());
    }
    public static long getPriceVal(Map<String, Object> map) {
        return (long) map.get(getPriceKey());
    }
    public static long getDateVal(Map<String, Object> map) {
        return (long) map.get(getDateKey());
    }

    /**
     * Confirms that the given Object is an instance of the correct type.
     * (Long for price and acquisition date, String otherwise)
     * Assumes that the key is a valid key (should be confirmed before calling).
     *
     * @param key The key value of the object in the data Map
     * @param object The Object whose type is being checked
     * @return Whether object is of the correct type.
     */
    private boolean validJSONObjectType(String key, Object object) {
        if (key.equals(getDateKey()) || key.equals(getPriceKey())) {
            return object instanceof Long;
        }
        return object instanceof String;
    }

    /**
     * Takes a JSONObject and creates and returns a Map. Fills the Map with the
     * data from the JSONObject with the same keys as keys. If any keys are absent,
     * null is returned.
     *
     * @param jObj The JSONObject that data is being extracted from.
     */
    private Map<String, Object> readJSONObject(JSONObject jObj) {
        Map<String, Object> map = new HashMap<>();

        for (String key : keys) {
            Object dataPoint = jObj.get(key);
            if (dataPoint == null) {return null;}
            if (!validJSONObjectType(key, dataPoint)) {
                System.out.println("Key \"" + key + "\" with value (" + dataPoint + ")" +
                        " is the wrong type of Object.\n" +
                        "(Long for price or acquisition date and String otherwise).\n" +
                        "Vehicle not added to inventory.\n");
                return null;
            }
            map.put(key, dataPoint);

        }
        return map;
    }

    /**
     * Reads and returns the data stored in the file of this object.
     *
     * @return A List of Map<String, Object>s that correspond to the
     *         JSONArray of data stored in the JSON file for this object.
     *         The Map has data in the same keys as keys.
     * @throws ReadWriteException Thrown if not in read ('r') mode.
     */
    public List<Map<String, Object>> read() throws ReadWriteException {
        if (mode != 'r') {
            throw new ReadWriteException("Must be mode 'r', not mode '" + mode + "'.");
        }
        JSONParser parser = new JSONParser();
        Reader fileReader;
        JSONObject jFile = null;
        JSONArray jArray;
        try {
            fileReader = new FileReader(file);
            jFile = (JSONObject) parser.parse(fileReader);
            fileReader.close();
        } catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
        }

        assert jFile != null;

        jArray = (JSONArray)jFile.get("car_inventory");

        List< Map<String, Object> > maps = new ArrayList<>();

        for (Object jObj : jArray) {
            Map<String, Object> map = readJSONObject((JSONObject) jObj);
            if (map != null) {maps.add(map);}
        }

        return maps;
    }

    /**
     * Takes a Map<String, Object> of data with the same keys as keys
     * and converts it to a JSONObject and returns it.
     *
     * @param data The Map of items to be ordered in a JSONObject with the keys for
     *             the data the same as the keys in keys.
     * @return The newly created JSONObject
     */
    private JSONObject makeJSONObject(Map<String, Object> data) {
        JSONObject jObj = new JSONObject();
        for (String key : keys) {
            Object dataPoint = data.get(key);
            if (dataPoint == null) {return null;}
            jObj.put(key, dataPoint);
        }
        return jObj;
    }

    /**
     * Takes a List of Maps to write to the file stored in this object.
     *
     * @param data List of Maps to write to a file.
     *             The array of String should have the keys in key.
     * @return The number of entries written to the file
     * @throws ReadWriteException Thrown if not in write ('w') mode.
     */
    public int write(List<Map<String, Object>> data) throws ReadWriteException {
        int added = 0;
        if (mode != 'w') {
            throw new ReadWriteException("Must be mode 'w', not mode '" + mode + "'.");
        }

        JSONArray jArray = new JSONArray();
        for (Map<String, Object> carData : data) {
            if (carData.size() == keys.length) {
                JSONObject jObj = makeJSONObject(carData);
                if (jObj != null) {
                    jArray.add(jObj);
                    added++;
                } else {
                    System.out.println("Did not add. (invalid key)");
                }
            } else {
                System.out.println("Did not add. " + carData.size() + " != " + keys.length);
            }
        }

        Writer fileWriter;
        JSONObject jFile = new JSONObject();
        jFile.put("car_inventory", jArray);
        try {
            fileWriter = new FileWriter(file);
            jFile.writeJSONString(fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return added;
    }

    /**
     * Opens a file chooser dialog to allow the user to select a JSON file.
     * The file chooser will start in the current user's working directory
     * and will filter files to only show those with a ".json" extension.
     *
     * @return The selected JSON file if the user selects a file and confirms the dialog,
     *         or null if the user cancels or closes the dialog without selecting a file.
     *
     * @author Christopher Engelhart
     */
    public static File selectJsonFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));
        int result = fileChooser.showOpenDialog(null);
        return result == 0 ? fileChooser.getSelectedFile() : null;
    }

    /**
     * Opens a file chooser dialog to allow the user to select a JSON file.
     * The file chooser will start in the current user's working directory
     * and will filter files to only show those with a ".json" extension.
     *
     * @return The selected path to the JSON file if the user selects a file and confirms
     *         the dialog, or null if the user cancels or closes the dialog without
     *         selecting a file.
     */
    public static String selectJsonFilePath() {
        File file = selectJsonFile();
        if (file == null) {return null;}
        return file.toString();
    }

    public String toString() {
        return "Opened file \"" + file.toString() + "\" in (" + mode + ") mode.";
    }
}