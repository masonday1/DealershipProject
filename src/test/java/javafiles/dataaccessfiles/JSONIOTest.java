package javafiles.dataaccessfiles;

import javafiles.Key;
import javafiles.customexceptions.ReadWriteException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JSONIOTest {

    private JSONIO getJSONIO(String partialPath, char mode, boolean failExpected) {
        FileIOBuilder.setupFileIOBuilders();

        String dir = System.getProperty("user.dir");
        String path = dir + "\\src\\test\\resources\\read_inventory_" + partialPath + ".json";

        try {
            JSONIO jsonIO = (JSONIO) FileIOBuilder.buildNewFileIO(path, mode);
            assertFalse(failExpected);
            return jsonIO;
        } catch (ReadWriteException | ClassCastException e) {
            assertTrue(failExpected);
        }
        return null;
    }

    private List<Map<Key, Object>> getMaps(JSONIO jsonIO, int mapNum) {
        List<Map<Key, Object>> maps;
        try {
            maps = jsonIO.readInventory();
            assertEquals(mapNum, maps.size());
            return maps;
        } catch (ReadWriteException e) {
            fail(e);
        }
        return null;
    }

    private void testMaps(List<Map<Key, Object>> maps, List<Map<Key, Object>> testingMaps) {
        assertEquals(maps.size(), testingMaps.size());
        for (int i = 0; i < maps.size(); i++) {
            Map<Key, Object> map = maps.get(i);
            Map<Key, Object> testingMap = testingMaps.get(i);
            assertEquals(map.size(), testingMap.size());
            for (Key key : map.keySet()) {
                Object mapVal = map.get(key);
                Object testVal = testingMap.get(key);
                assertAll(
                        () -> assertEquals(mapVal, testVal),
                        () -> assertNotNull(mapVal),
                        () -> assertNotNull(testVal),
                        () -> assertEquals(mapVal.getClass(), testVal.getClass())
                );
            }
        }
    }

    @Test
    void fileDNERead() {
        JSONIO jsonIO = getJSONIO("DNE", 'r', true);

        assertNull(jsonIO);
    }

    @Test
    void fileDNEWrite() {
        JSONIO jsonIO = getJSONIO("DNE", 'w', false);

        assertNotNull(jsonIO);
    }

    @Test
    void fileDNEBadChar() {
        JSONIO jsonIO = getJSONIO("DNE", 'x', true);

        assertNull(jsonIO);
    }

    private Map<Key, Object> getMapPartialMap() {
        Map<Key, Object> map = new HashMap<>();

        map.put(Key.DEALERSHIP_ID, "12513");
        map.put(Key.VEHICLE_ID, "48934j");
        map.put(Key.VEHICLE_TYPE, "suv");
        map.put(Key.VEHICLE_MANUFACTURER, "Ford");
        map.put(Key.VEHICLE_MODEL, "Explorer");
        map.put(Key.VEHICLE_PRICE, 20123L);
        map.put(Key.VEHICLE_ACQUISITION_DATE, 1515354694451L);

        return map;
    }

    @Test
    void readInventoryPartialMap() {
        JSONIO jsonIO = getJSONIO("1", 'r', false);

        assertNotNull(jsonIO);

        List<Map<Key, Object>> maps = getMaps(jsonIO, 1);
        assertNotNull(maps);

        List<Map<Key, Object>> validMaps = new ArrayList<>();
        validMaps.add(getMapPartialMap());

        testMaps(validMaps, maps);
    }

    private Map<Key, Object> getMapFullMap() {
        Map<Key, Object> map = new HashMap<>();

        map.put(Key.DEALERSHIP_ID, "d_id");
        map.put(Key.DEALERSHIP_NAME, "d_name");

        map.put(Key.VEHICLE_ID, "v_id");
        map.put(Key.VEHICLE_TYPE, "SUV");
        map.put(Key.VEHICLE_MANUFACTURER, "manufacture");
        map.put(Key.VEHICLE_MODEL, "Model");
        map.put(Key.VEHICLE_PRICE, 10000L);

        map.put(Key.DEALERSHIP_RENTING_STATUS, false);
        map.put(Key.DEALERSHIP_RECEIVING_STATUS, true);
        map.put(Key.VEHICLE_RENTAL_STATUS, false);

        map.put(Key.VEHICLE_PRICE_UNIT, "dollar");
        map.put(Key.VEHICLE_ACQUISITION_DATE, 100L);

        assertEquals(Key.values().length - 1, map.size());

        return map;
    }

    @Test
    void readInventoryFullMap() {
        JSONIO jsonIO = getJSONIO("2", 'r', false);

        assertNotNull(jsonIO);

        List<Map<Key, Object>> maps = getMaps(jsonIO, 1);
        assertNotNull(maps);

        List<Map<Key, Object>> validMaps = new ArrayList<>();
        validMaps.add(getMapFullMap());

        testMaps(validMaps, maps);
    }

    private Map<Key, Object> getMapExtra() {
        Map<Key, Object> map = new HashMap<>();

        map.put(Key.VEHICLE_PRICE_UNIT, "dollars");
        map.put(Key.VEHICLE_PRICE, 50444L);
        map.put(Key.VEHICLE_MODEL, "Model 3");
        map.put(Key.DEALERSHIP_RENTING_STATUS, false);
        map.put(Key.VEHICLE_TYPE, "Sedan");
        map.put(Key.DEALERSHIP_NAME, "l");
        map.put(Key.DEALERSHIP_RECEIVING_STATUS, false);
        map.put(Key.DEALERSHIP_ID, "12513");
        map.put(Key.VEHICLE_MANUFACTURER, "Tesla");
        map.put(Key.VEHICLE_ID, "83883");
        map.put(Key.VEHICLE_RENTAL_STATUS, false);
        map.put(Key.VEHICLE_ACQUISITION_DATE, 1515354694451L);

        return map;
    }

    @Test
    void readInventoryMultipleMaps() {
        JSONIO jsonIO = getJSONIO("3", 'r', false);

        assertNotNull(jsonIO);

        List<Map<Key, Object>> maps = getMaps(jsonIO, 3);
        assertNotNull(maps);

        List<Map<Key, Object>> mapSolutionMaps = new ArrayList<>();
        mapSolutionMaps.add(getMapPartialMap());
        mapSolutionMaps.add(getMapFullMap());
        mapSolutionMaps.add(getMapExtra());

        testMaps(mapSolutionMaps, maps);
    }

    @Test
    void readWithWriteJSONIO() {
        char mode = 'w';

        JSONIO writeJSONIO = getJSONIO("1", mode, false);
        assertNotNull(writeJSONIO);

        try {
            writeJSONIO.readInventory();
            fail("Read from write file.");
        } catch (ReadWriteException e) {
            assertEquals("Must be mode 'r', not mode '" + mode + "'.", e.getMessage());
        }
    }

    @Test
    void writeWithReadJSONIO() {
        char mode = 'r';

        JSONIO readJSONIO = getJSONIO("4", mode, false);
        assertNotNull(readJSONIO);

        List<Map<Key, Object>> maps = new ArrayList<>();
        maps.add(getMapExtra());

        try {
            readJSONIO.writeInventory(maps);
            fail("Wrote to read file.");
        } catch (ReadWriteException e) {
            assertEquals("Must be mode 'w', not mode '" + mode + "'.", e.getMessage());
        }
    }

    @Test
    void writeInventoryPartialMap() {
        JSONIO jsonIO = getJSONIO("4", 'w', false);
        assertNotNull(jsonIO);

        Map<Key, Object> target = getMapPartialMap();
        List<Map<Key, Object>> targetLst = new ArrayList<>();
        targetLst.add(target);

        try {
            jsonIO.writeInventory(targetLst);
        } catch (ReadWriteException e) {
            fail(e.toString());
        }

        JSONIO readFile = getJSONIO("4", 'r', false);
        assertNotNull(readFile);

        List<Map<Key, Object>> maps = getMaps(readFile, 1);
        assertNotNull(maps);

        testMaps(targetLst, maps);
    }

    @Test
    void writeInventoryFullMap() {
        JSONIO jsonIO = getJSONIO("5", 'w', false);
        assertNotNull(jsonIO);

        Map<Key, Object> target = getMapFullMap();
        List<Map<Key, Object>> targetLst = new ArrayList<>();
        targetLst.add(target);

        try {
            jsonIO.writeInventory(targetLst);
        } catch (ReadWriteException e) {
            fail(e.toString());
        }

        JSONIO readFile = getJSONIO("5", 'r', false);
        assertNotNull(readFile);

        List<Map<Key, Object>> maps = getMaps(readFile, 1);
        assertNotNull(maps);

        testMaps(targetLst, maps);
    }

    @Test
    void writeInventoryMultipleMaps() {
        JSONIO jsonIO = getJSONIO("6", 'w', false);
        assertNotNull(jsonIO);

        List<Map<Key, Object>> targetLst = new ArrayList<>();
        targetLst.add(getMapPartialMap());
        targetLst.add(getMapFullMap());
        targetLst.add(getMapExtra());

        try {
            jsonIO.writeInventory(targetLst);
        } catch (ReadWriteException e) {
            fail(e.toString());
        }

        JSONIO readFile = getJSONIO("6", 'r', false);
        assertNotNull(readFile);

        List<Map<Key, Object>> maps = getMaps(readFile, 3);
        assertNotNull(maps);

        testMaps(targetLst, maps);
    }
}

/*
    "price_unit": map.put(Key.VEHICLE_PRICE_UNIT,
    "price": map.put(Key.VEHICLE_PRICE,
    "vehicle_model": map.put(Key.VEHICLE_MODEL,
    "dealership_rental_status": map.put(Key.DEALERSHIP_RENTING_STATUS,
    "vehicle_type": map.put(Key.VEHICLE_TYPE,
    "dealership_name": map.put(Key.DEALERSHIP_NAME,
    "dealership_receiving_status": map.put(Key.DEALERSHIP_RECEIVING_STATUS,
    "dealership_id": map.put(Key.DEALERSHIP_ID,
    "vehicle_manufacturer": map.put(Key.VEHICLE_MANUFACTURER,
    "vehicle_id": map.put(Key.VEHICLE_ID,
    "vehicle_rental_status": map.put(Key.VEHICLE_RENTAL_STATUS,
    "acquisition_date": map.put(Key.VEHICLE_ACQUISITION_DATE,
 */