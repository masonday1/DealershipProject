package javafiles.dataaccessfiles;

import javafiles.Key;
import javafiles.customexceptions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafiles.Key.*;
import static javafiles.dataaccessfiles.FileIOBuilderTest.*;
import static org.junit.jupiter.api.Assertions.*;

class XMLIOTest {
    private XMLIO getXMLIO(String partialPath, char mode, boolean failExpected) throws ReadWriteException {
        return (XMLIO) FileIOBuilderTest.getFileIOForTest(partialPath, "xmlIOTests", ".xml", mode, failExpected);
    }

    @Test
    void fileDNERead() {
        try {
            XMLIO xmlIO = getXMLIO("DNE", 'r', true);
            fail(xmlIO.toString());
        } catch (ReadWriteException e) {
            PathNotFoundException cause = new PathNotFoundException("Path not found.");
            assertTrue(FileIOBuilderTest.isSameCauseType(new ReadWriteException(cause), e));
        }
    }

    @Test
    void fileDNEWrite() {
        try {
            XMLIO xmlIO = getXMLIO("DNE", 'w', true);
            fail(xmlIO.toString());
        } catch (ReadWriteException e) {
            BadExtensionException cause = new BadExtensionException("Bad character.");
            assertTrue(FileIOBuilderTest.isSameCauseType(new ReadWriteException(cause), e));
        }
    }

    @Test
    void fileDNEBadChar() {
        try {
            XMLIO xmlIO = getXMLIO("DNE", 'x', true);
            fail(xmlIO.toString());
        } catch (ReadWriteException e) {
            BadCharException cause = new BadCharException("Bad character.");
            assertTrue(FileIOBuilderTest.isSameCauseType(new ReadWriteException(cause), e));
        }
    }

    private Map<Key, Object> getMinMap() {
        Map<Key, Object> map = new HashMap<>();

        map.put(DEALERSHIP_ID, "d_id");
        map.put(VEHICLE_ID, "v_id0");
        map.put(VEHICLE_TYPE, "pickup");
        map.put(VEHICLE_MODEL, "model");
        map.put(VEHICLE_PRICE, 17000L);

        int neededKeys = 0;
        for (Key key: values()) {
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
        map.put(DEALERSHIP_NAME, "name");
        map.put(VEHICLE_ID, "v_id1");
        map.put(VEHICLE_TYPE, "sports car");
        map.put(VEHICLE_MANUFACTURER, "make");
        map.put(VEHICLE_MODEL, "model");
        map.put(VEHICLE_PRICE, 17000L);
        map.put(VEHICLE_PRICE_UNIT, "pounds");

        return map;
    }

    private Map<Key, Object> getExtraMap() {
        Map<Key, Object> map = new HashMap<>();

        map.put(DEALERSHIP_ID, "d_id");
        map.put(DEALERSHIP_NAME, "name");
        map.put(VEHICLE_ID, "v_id2");
        map.put(VEHICLE_TYPE, "suv");
        map.put(VEHICLE_MODEL, "model");
        map.put(VEHICLE_PRICE, 16500L);
        map.put(VEHICLE_PRICE_UNIT, "dollars");

        return map;
    }

    private Map<Key, Object> getMinMapNewDealer() {
        Map<Key, Object> map = new HashMap<>();

        map.put(DEALERSHIP_ID, "d_id2");
        map.put(VEHICLE_ID, "v_id3");
        map.put(VEHICLE_TYPE, "sports car");
        map.put(VEHICLE_MODEL, "model2");
        map.put(VEHICLE_PRICE, 18000L);

        int neededKeys = 0;
        for (Key key: values()) {
            if (key.getNeeded()) {
                neededKeys++;
            }
        }
        assertEquals(neededKeys, map.size());

        return map;
    }

    private Map<Key, Object> getBadPriceMap() {
        Map<Key, Object> map = new HashMap<>();

        String reason = "Invalid number format on " + XMLKey.PRICE.getName() + ". [" + 45.2 + "]";
        NumberFormatException cause = new NumberFormatException(reason);

        map.put(DEALERSHIP_ID, "d_id2");
        map.put(VEHICLE_ID, "v_id4");
        map.put(VEHICLE_TYPE, "SUV");
        map.put(VEHICLE_MODEL, "model3");
        map.put(REASON_FOR_ERROR, new ReadWriteException(cause));

        return map;
    }

    private List<Map<Key, Object>> readInventory(String partialPath, int mapNum) {
        XMLIO xmlIO = null;
        try {
            xmlIO = getXMLIO(partialPath, 'r', false);
        } catch (ReadWriteException e) {
            fail(e.getMessage());
        }

        assertNotNull(xmlIO);

        List<Map<Key, Object>> maps = getMaps(xmlIO, mapNum);
        assertNotNull(maps);

        return maps;
    }

    @Test
    void readInventoryMinMap() {
        List<Map<Key, Object>> mapsToTest = readInventory("r1", 1);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        mapsToTestAgainst.add(getMinMap());

        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readInventoryFullMap() {
        List<Map<Key, Object>> mapsToTest = readInventory("r2", 1);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        mapsToTestAgainst.add(getFullMap());

        testMaps(mapsToTestAgainst, mapsToTest);

    }

    @Test
    void readInventoryMultipleMaps() {
        List<Map<Key, Object>> mapsToTest = readInventory("r3", 3);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();

        Map<Key, Object> minMap = getMinMap();
        minMap.put(DEALERSHIP_NAME, "name"); // dealership has name this time
        mapsToTestAgainst.add(minMap);
        mapsToTestAgainst.add(getFullMap());
        mapsToTestAgainst.add(getExtraMap());

        testMaps(mapsToTestAgainst, mapsToTest);

    }

    @Test
    void readInventoryMultipleDealers() {
        List<Map<Key, Object>> mapsToTest = readInventory("r4", 2);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        mapsToTestAgainst.add(getFullMap());
        mapsToTestAgainst.add(getMinMapNewDealer());

        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readInventoryDealerInDealerSameID() {
        List<Map<Key, Object>> mapsToTest = readInventory("r5", 1);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        mapsToTestAgainst.add(getMinMap());

        testMaps(mapsToTestAgainst, mapsToTest);

    }

    private void putReasonDuplicate(XMLKey xmlKey, String firstFound, String prevVal, Map<Key, Object> map) {
        String reason = "Key " + xmlKey.getName() + " already has a value and [";
        reason += firstFound  + "] != [" + prevVal + "].";
        ReadDuplicateKeyException cause = new ReadDuplicateKeyException(reason);
        map.put(REASON_FOR_ERROR, new ReadWriteException(cause));
    }

    @Test
    void readInventoryDealerInDealerDifferentID() {
        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        Map<Key, Object> fullMap = getFullMap();

        XMLKey key = XMLKey.D_ID;
        String firstFound = "d_id2"; // Child nodes are fully evaluated before attributes of node,
        // thus d_id2 is found first.

        putReasonDuplicate(key, firstFound, (String) fullMap.get(DEALERSHIP_ID), fullMap);
        fullMap.put(XMLKey.D_ID.getKey(), firstFound);

        mapsToTestAgainst.add(fullMap);


        List<Map<Key, Object>> mapsToTest = readInventory("r6", 1);
        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readInventoryMultipleDealerInsideDealer() {
        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        mapsToTestAgainst.add(getMinMap());
        mapsToTestAgainst.add(getFullMap());
        mapsToTestAgainst.add(getExtraMap());
        mapsToTestAgainst.add(getMinMapNewDealer());

        mapsToTestAgainst.getFirst().put(DEALERSHIP_NAME, "name");
        for (int i = 0; i < 4; i++) {
            Map<Key, Object> map = mapsToTestAgainst.get(i);
            putReasonDuplicate(XMLKey.D_ID, "d_id", "d_id2", map);
            map.put(DEALERSHIP_ID, "d_id");
        }
        mapsToTestAgainst.getLast().put(DEALERSHIP_NAME, "name");


        List<Map<Key, Object>> mapsToTest = readInventory("r7", 4);
        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readInventoryBadNumber() {
        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        mapsToTestAgainst.add(getBadPriceMap());

        List<Map<Key, Object>> mapsToTest = readInventory("r8", 1);
        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readPomFile() {
        String reason = "Can't read or write to Maven's pom.xml file.\n" +
                        "If this is not Maven's pom.xml file, rename it and try again.";
        try {
            XMLIO xmlIO = getXMLIO("pom", 'r', true);
            fail(xmlIO.toString());
        } catch (ReadWriteException e) {
            assertEquals(reason, e.getMessage());
        }
    }

    @Test
    void readInventoryInvalidXML() {
        XMLIO xmlIO = null;
        try {
            xmlIO = getXMLIO("missing_data", 'r', false);
        } catch (ReadWriteException e) {
            fail(e.getMessage());
        }

        assertNotNull(xmlIO);

        List<Map<Key, Object>> maps = null;
        try {
            maps = xmlIO.readInventory();
            assertEquals(1, maps.size());
            assertEquals(1, maps.getFirst().size());
        } catch (ReadWriteException e) {
            fail(e);
        }

        assertNotNull(maps);
        assertInstanceOf(Exception.class, maps.getFirst().get(REASON_FOR_ERROR));

    }

    @Test
    void writeInventoryThroughFileIOBuilder() {
        XMLIO xmlIO;
        try {
            xmlIO = getXMLIO("w1", 'w', true);
            fail(xmlIO.toString());
        } catch (ReadWriteException e) {
            BadExtensionException cause = new BadExtensionException("Bad extension.");
            assertTrue(FileIOBuilderTest.isSameCauseType(new ReadWriteException(cause), e));
        }
    }
}