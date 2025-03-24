package javafiles.dataaccessfiles;

import javafiles.Key;
import javafiles.customexceptions.ReadWriteException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JSONIOTest {

    private JSONIO getJSONIO(String partialPath, char mode, boolean failExpected) {
        FileIOBuilder.setupFileIOBuilders();

        String dir = System.getProperty("user.dir");
        String path = dir + "\\src\\test\\datafiles\\read_inventory_" + partialPath + ".json";

        try {
            JSONIO jsonIO = (JSONIO) FileIOBuilder.buildNewFileIO(path, mode);
            assertFalse(failExpected);
            return jsonIO;
        } catch (ReadWriteException | ClassCastException e) {
            assertTrue(failExpected);
        }
        return null;
    }

    private void testMap(Map<Key, Object> map, Map<Key, Object> testingMap) {
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

    @Test
    void readInventory1() {
        JSONIO jsonIO = getJSONIO("1", 'r', false);

        assertNotNull(jsonIO);

        List<Map<Key, Object>> maps = null;

        try {
            maps = jsonIO.readInventory();
            assertNotNull(maps);
            assertEquals(1, maps.size());
        } catch (ReadWriteException e) {
            fail(e);
        }

        Map<Key, Object> testingMap = maps.getFirst();
        assertNotNull(testingMap);

        Map<Key, Object> map = new HashMap<>();

        map.put(Key.DEALERSHIP_ID, "12513");
        map.put(Key.VEHICLE_ID, "48934j");
        map.put(Key.VEHICLE_TYPE, "suv");
        map.put(Key.VEHICLE_MANUFACTURER, "Ford");
        map.put(Key.VEHICLE_MODEL, "Explorer");
        map.put(Key.VEHICLE_PRICE, 20123L);
        map.put(Key.VEHICLE_ACQUISITION_DATE, 1515354694451L);

        testMap(map, testingMap);
    }

    @Test
    void readInventory2() {
        JSONIO jsonIO = getJSONIO("2", 'r', true);

        assertNull(jsonIO);
    }

    @Test
    void writeInventory() {
    }
}