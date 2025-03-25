package javafiles.dataaccessfiles;

import javafiles.Key;
import javafiles.customexceptions.ReadWriteException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafiles.Key.*;
import static javafiles.dataaccessfiles.FileIOBuilderTest.getMaps;
import static javafiles.dataaccessfiles.FileIOBuilderTest.testMaps;
import static org.junit.jupiter.api.Assertions.*;

class XMLIOTest {
    private XMLIO getXMLIO(String partialPath, char mode, boolean failExpected) throws ReadWriteException {
        return (XMLIO) FileIOBuilderTest.getFileIOForTest(partialPath, ".xml", mode, failExpected);
    }

    @Test
    void fileDNERead() {
        try {
            XMLIO xmlIO = getXMLIO("DNE", 'r', true);
            fail(xmlIO.toString());
        } catch (ReadWriteException e) {
            assertTrue(FileIOBuilderTest.isPathNotFoundException(e));
        }
    }

    @Test
    void fileDNEWrite() {
        try {
            XMLIO xmlIO = getXMLIO("DNE", 'w', true);
            fail(xmlIO.toString());
        } catch (ReadWriteException e) {
            assertTrue(FileIOBuilderTest.isBadExtensionException(e));
        }
    }

    @Test
    void fileDNEBadChar() {
        try {
            XMLIO xmlIO = getXMLIO("DNE", 'x', true);
            fail(xmlIO.toString());
        } catch (ReadWriteException e) {
            assertTrue(FileIOBuilderTest.isBadCharException(e));
        }
    }

    private Map<Key, Object> getMinMap() {
        Map<Key, Object> map = new HashMap<>();

        map.put(DEALERSHIP_ID, "d_id");
        map.put(Key.VEHICLE_ID, "v_id0");
        map.put(Key.VEHICLE_TYPE, "pickup");
        map.put(Key.VEHICLE_MODEL, "model");
        map.put(Key.VEHICLE_PRICE, 17000L);

        int neededKeys = 0;
        for (Key key: Key.values()) {
            if (key.getNeeded()) {
                neededKeys++;
            }
        }
        assertEquals(neededKeys, map.size());

        return map;
    }

    private Map<Key, Object> getFullMap() {
        Map<Key, Object> map = new HashMap<>();

        map.put(DEALERSHIP_ID, "d_id");
        map.put(Key.DEALERSHIP_NAME, "name");
        map.put(Key.VEHICLE_ID, "v_id1");
        map.put(Key.VEHICLE_TYPE, "sports car");
        map.put(Key.VEHICLE_MANUFACTURER, "make");
        map.put(Key.VEHICLE_MODEL, "model");
        map.put(Key.VEHICLE_PRICE, 17000L);
        map.put(Key.VEHICLE_PRICE_UNIT, "pounds");

        return map;
    }

    private Map<Key, Object> getExtraMap() {
        Map<Key, Object> map = new HashMap<>();

        map.put(DEALERSHIP_ID, "d_id");
        map.put(Key.DEALERSHIP_NAME, "Name");
        map.put(Key.VEHICLE_ID, "v_id2");
        map.put(Key.VEHICLE_TYPE, "suv");
        map.put(Key.VEHICLE_MODEL, "model");
        map.put(Key.VEHICLE_PRICE, 16500L);
        map.put(Key.VEHICLE_PRICE_UNIT, "dollars");

        return map;
    }

    private Map<Key, Object> getMinMapNewDealer() {
        Map<Key, Object> map = new HashMap<>();

        map.put(DEALERSHIP_ID, "d_id2");
        map.put(Key.VEHICLE_ID, "v_id3");
        map.put(Key.VEHICLE_TYPE, "sports car");
        map.put(Key.VEHICLE_MODEL, "model2");
        map.put(Key.VEHICLE_PRICE, 18000L);

        int neededKeys = 0;
        for (Key key: Key.values()) {
            if (key.getNeeded()) {
                neededKeys++;
            }
        }
        assertEquals(neededKeys, map.size());

        return map;
    }

    private Map<Key, Object> getMapFromIndex(int index) {
        return switch (index) {
            case 0 -> getMinMap();
            case 1 -> getFullMap();
            case 2 -> getExtraMap();
            case 3 -> getMinMapNewDealer();
            default -> null;
        };
    }

    private void setDealerName(int startIndex, int endIndex, List<Map<Key, Object>> maps, String name) {
        for (int i = startIndex; i < endIndex; i++) {
            DEALERSHIP_NAME.putNonNull(maps.get(i), name);
        }
    }

    private List<Map<Key, Object>> getUpdatedMaps(int[] mapIndexes) {
        List<Map<Key, Object>> validMaps = new ArrayList<>();

        String dealerID = null;
        String dealerName = null;
        int dealerStartIndex = -1;
        for (int i = 0; i < mapIndexes.length; i++) {
            Map<Key, Object> map = getMapFromIndex(mapIndexes[i]);
            assertNotNull(map);

            if (dealerID == null) {
                dealerStartIndex = i;
                dealerID = DEALERSHIP_ID.getVal(map, String.class);
                dealerName = DEALERSHIP_NAME.getVal(map, String.class);
            } else if (!dealerID.equals(DEALERSHIP_ID.getVal(map, String.class))) {
                setDealerName(dealerStartIndex, i, validMaps, dealerName);
                dealerStartIndex = i;
                dealerID = DEALERSHIP_ID.getVal(map, String.class);
                dealerName = DEALERSHIP_NAME.getVal(map, String.class);
            } else if (dealerName == null) {
                dealerName = DEALERSHIP_NAME.getVal(map, String.class);
            }

            validMaps.add(map);
        }
        setDealerName(dealerStartIndex, validMaps.size(), validMaps, dealerName);

        return validMaps;
    }

    void readInventory(String partialPath, int[] mapIndexes, int[] errorIndex) {
        XMLIO xmlIO = null;
        try {
            xmlIO = getXMLIO(partialPath, 'r', false);
        } catch (ReadWriteException e) {
            fail(e.getMessage());
        }

        assertNotNull(xmlIO);

        List<Map<Key, Object>> maps = getMaps(xmlIO, mapIndexes.length);
        assertNotNull(maps);

        List<Map<Key, Object>> validMaps = getUpdatedMaps(mapIndexes);

        if (errorIndex != null) {
            for (int index : errorIndex) {
                REASON_FOR_ERROR.putNonNull(validMaps.get(index), "Some error");
            }
        }

        testMaps(validMaps, maps);
    }

    @Test
    void readInventoryMinMap() {
        readInventory("r1", new int[]{0}, null);
    }

    @Test
    void readInventoryFullMap() {
        readInventory("r2", new int[]{1}, null);
    }

    @Test
    void readInventoryMultipleMaps() {
        readInventory("r3", new int[]{0, 1, 2}, null);
    }

    @Test
    void readInventoryMultipleDealers() {
        readInventory("r4", new int[]{1, 3}, null);
    }

    @Test
    void readInventoryDealerInDealerSameID() {
        readInventory("r5", new int[]{0}, null);
    }

    @Test
    void readInventoryDealerInDealerDifferentID() {
        readInventory("r6", new int[]{1}, new int[]{0});
    }

    @Test
    void writeInventory() {
    }
}