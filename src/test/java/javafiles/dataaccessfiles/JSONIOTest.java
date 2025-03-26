package javafiles.dataaccessfiles;

import javafiles.Key;
import javafiles.customexceptions.BadCharException;
import javafiles.customexceptions.PathNotFoundException;
import javafiles.customexceptions.ReadWriteException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafiles.dataaccessfiles.FileIOBuilderTest.getMaps;
import static javafiles.dataaccessfiles.FileIOBuilderTest.testMaps;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JSONIOTest {
    private JSONIO getJSONIO(String partialPath, char mode, boolean failExpected) throws ReadWriteException {
        return (JSONIO) FileIOBuilderTest.getFileIOForTest(partialPath, "jsonIOTests",".json", mode, failExpected);
    }

    @Test
    void fileDNERead() {
        try {
            JSONIO jsonIO = getJSONIO("DNE_R", 'r', true);
            fail(jsonIO.toString());
        } catch (ReadWriteException e) {
            PathNotFoundException cause = new PathNotFoundException("Path not found.");
            assertTrue(FileIOBuilderTest.isSameCauseType(new ReadWriteException(cause), e));
        }
    }

    @Test
    void fileDNEWrite() {
        try {
            getJSONIO("DNE_W", 'w', false);
        } catch (ReadWriteException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void fileDNEBadChar() {
        try {
            JSONIO jsonIO = getJSONIO("DNE_X", 'x', true);
            fail(jsonIO.toString());
        } catch (ReadWriteException e) {
            BadCharException cause = new BadCharException("Bad character.");
            assertTrue(FileIOBuilderTest.isSameCauseType(new ReadWriteException(cause), e));
        }
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
        JSONIO jsonIO = null;
        try {
            jsonIO = getJSONIO("r1", 'r', false);
        } catch (ReadWriteException e) {
            fail(e.getMessage());
        }

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
        JSONIO jsonIO = null;
        try {
            jsonIO = getJSONIO("r2", 'r', false);
        } catch (ReadWriteException e) {
            fail(e.getMessage());
        }


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
        JSONIO jsonIO = null;
        try {
            jsonIO = getJSONIO("r3", 'r', false);
        } catch (ReadWriteException e) {
            fail(e.getMessage());
        }


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
    void createJSONIOWithXMLFile() {
        FileIO fileIO = null;
        try {
            fileIO = FileIOBuilderTest.getFileIOForTest("json", "jsonIOTests",".xml", 'r', false);
        } catch (ReadWriteException e) {
            fail(e.getMessage());
        }

        try {
            JSONIO jsonIO = (JSONIO) fileIO;
            assertNotNull(jsonIO);
            fail(jsonIO.toString());
        } catch (ClassCastException _) {

        }
    }

    @Test
    void readWithWriteJSONIO() {
        char mode = 'w';

        JSONIO writeJSONIO = null;
        try {
            writeJSONIO = getJSONIO("r1", mode, false);
        } catch (ReadWriteException e) {
            fail(e.getMessage());
        }
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

        JSONIO readJSONIO = null;
        try {
            readJSONIO = getJSONIO("w6", mode, false);
        } catch (ReadWriteException e) {
            fail(e.getMessage());
        }
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

    private void writeInventoryWithBadKeys(String partialPath, int[] readMaps, Map<Key, Object> badKeys) {
        JSONIO jsonIO = null;
        try {
            jsonIO = getJSONIO(partialPath, 'w', false);
        } catch (ReadWriteException e) {
            fail(e.getMessage());
        }

        assertNotNull(jsonIO);


        List<Map<Key, Object>> targetLst = new ArrayList<>();
        for (int mapKey: readMaps) {
            Map<Key, Object> target = switch (mapKey) {
                case 1 -> getMapPartialMap();
                case 2 -> getMapFullMap();
                case 3 -> getMapExtra();
                default -> null;
            };
            assertNotNull(target);
            target.putAll(badKeys);
            targetLst.add(target);
        }

        try {
            jsonIO.writeInventory(targetLst);
        } catch (ReadWriteException e) {
            fail(e.toString());
        }

        for (Map<Key, Object> map : targetLst) {
            for (Key key : badKeys.keySet()) {
                map.remove(key);
            }
        }

        JSONIO readFile = null;
        try {
            readFile = getJSONIO(partialPath, 'r', false);
        } catch (ReadWriteException e) {
            fail(e.getMessage());
        }
        assertNotNull(readFile);

        List<Map<Key, Object>> maps = getMaps(readFile, readMaps.length);
        assertNotNull(maps);

        testMaps(targetLst, maps);
    }

    private void writeInventoryGood(String partialPath, int[] readMaps) {
        writeInventoryWithBadKeys(partialPath, readMaps, new HashMap<>());
    }

    @Test
    void writeInventoryPartialMap() {
        writeInventoryGood("w1", new int[]{1});
    }

    @Test
    void writeInventoryFullMap() {
        writeInventoryGood("w2", new int[]{2});
    }

    @Test
    void writeInventoryMultipleMaps() {
        writeInventoryGood("w3", new int[]{1, 2, 3});
    }

    @Test
    void writeInventoryNullKey() {
        Map<Key, Object> badKey = new HashMap<>();
        badKey.put(null, "Null Key Val");

        writeInventoryWithBadKeys("w4", new int[]{1}, badKey);
    }


    @Test
    void writeInventoryNullVal() {
        Map<Key, Object> badKey = new HashMap<>();
        badKey.put(Key.REASON_FOR_ERROR, null);

        writeInventoryWithBadKeys("w5", new int[]{1}, badKey);
    }
}