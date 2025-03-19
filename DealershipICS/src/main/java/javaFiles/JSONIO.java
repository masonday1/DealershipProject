package javaFiles;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A class that reads and writes to JSON files
 *
 * @author Dylan Browne
 */
public class JSONIO extends FileIO {
    /**
     * Creates or opens a JSON file with name filePath in read ('r') or write ('w') mode.
     * Read mode allows the reading, but not writing of files, write mode allows for the
     * writing, but not reading of files.
     *
     * @param filePath The path of the file to be opened or created
     * @param mode A char representation of the type of file this is (read 'r' or write 'w')
     * @throws ReadWriteException Thrown if the mode is an invalid char
     */
    public JSONIO(String filePath, char mode) throws ReadWriteException {
        super(filePath, mode);
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

        for (Key key : Key.values()) {
            String keyStr = key.getKey();

            Object dataPoint = jObj.get(keyStr);
            if (dataPoint == null) {return null;}

            if (!key.validObjectType(dataPoint)) {
                System.out.println("Key \"" + keyStr + "\" with value (" + dataPoint + ")" +
                        " is the wrong type of Object.\n" +
                        "(Long for price or acquisition date and String otherwise).\n" +
                        "Vehicle not added to inventory.\n");
                return null;
            }
            map.put(keyStr, dataPoint);

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
    public List<Map<String, Object>> readInventory() throws ReadWriteException {
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
        for (String key : Key.getKeys()) {
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
    public int writeInventory(List<Map<String, Object>> data) throws ReadWriteException {
        int added = 0;
        if (mode != 'w') {
            throw new ReadWriteException("Must be mode 'w', not mode '" + mode + "'.");
        }

        JSONArray jArray = new JSONArray();
        for (Map<String, Object> carData : data) {
            if (carData.size() == Key.getKeys().length) {
                JSONObject jObj = makeJSONObject(carData);
                if (jObj != null) {
                    jArray.add(jObj);
                    added++;
                } else {
                    System.out.println("Did not add. (invalid key)");
                }
            } else {
                System.out.println("Did not add. " + carData.size() + " != " + Key.getKeys().length);
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

    @Override
    public List<Map<String, Object>> readDealerships() {
        System.out.println("Not implemented (readDealerships).");
        return null;
    }

    @Override
    public int writeDealerships(List<Map<String, Object>> maps) {
        System.out.println("Not implemented (writeDealerships).");
        return 0;
    }
}