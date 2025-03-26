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
        // Expected: Creation of XMLIO throws an exception
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
        // Expected: Creation of XMLIO throws an exception
        try {
            XMLIO xmlIO = getXMLIO("DNE", 'w', true);
            fail(xmlIO.toString());
        } catch (ReadWriteException e) {
            BadExtensionException cause = new BadExtensionException("Bad extension.");
            assertTrue(FileIOBuilderTest.isSameCauseType(new ReadWriteException(cause), e));
        }
    }

    @Test
    void fileDNEBadChar() {
        // Expected: Creation of XMLIO throws an exception
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
        // Expected: No issues, all Vehicles read.
        List<Map<Key, Object>> mapsToTest = readInventory("r1", 1);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        mapsToTestAgainst.add(getMinMap());

        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readInventoryFullMap() {
        // Expected: No issues, all Vehicles read.
        List<Map<Key, Object>> mapsToTest = readInventory("r2", 1);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        mapsToTestAgainst.add(getFullMap());

        testMaps(mapsToTestAgainst, mapsToTest);

    }

    @Test
    void readInventoryMultipleMaps() {
        // Expected: No issues, all Vehicles read.
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
        // Expected: No issues, all Vehicles read.
        List<Map<Key, Object>> mapsToTest = readInventory("r4", 2);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        mapsToTestAgainst.add(getFullMap());
        mapsToTestAgainst.add(getMinMapNewDealer());

        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readInventoryDealerInDealerSameID() {
        // Expected: No issues, all Vehicles read.
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
        // Expected: Key.REASON_FOR_ERROR is added to map, marking last duplicate.
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
        // Expected: Key.REASON_FOR_ERROR is added to maps, marking last duplicate for each.
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
        // Expected: Key.REASON_FOR_ERROR is added to map, marking last invalid Long.
        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        mapsToTestAgainst.add(getBadPriceMap());

        List<Map<Key, Object>> mapsToTest = readInventory("r8", 1);
        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readPomFile() {
        // Expected: Creation of XMLIO throws an exception
        String reason = "Can't read or write to Maven's pom.xml file.\n" +
                        "If this is not Maven's pom.xml file, rename it and try again.";
        try {
            XMLIO xmlIO = getXMLIO("pom", 'r', true);
            fail(xmlIO.toString());
        } catch (ReadWriteException e) {
            assertInstanceOf(ReadWriteException.class, e);
            assertEquals(reason, e.getMessage());
        }
    }

    @Test
    void readInventoryInvalidXML() {
        // Expected: XMLIO.readInventory() throws a ReadWriteException
        XMLIO xmlIO = null;
        try {
            xmlIO = getXMLIO("missing_data", 'r', false);
        } catch (ReadWriteException e) {
            fail(e);
        }

        try {
            xmlIO.readInventory();
            fail(xmlIO.toString());
        } catch (ReadWriteException e) {
            assertInstanceOf(Exception.class, e.getCause());
        }
    }

    @Test
    void readInventoryNoOverallDealersRoot() {
        // Expected: No issues, all Vehicles read.
        List<Map<Key, Object>> mapsToTest = readInventory("r9", 3);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();

        Map<Key, Object> minMap = getMinMap();
        minMap.put(DEALERSHIP_NAME, "name");
        mapsToTestAgainst.add(minMap);
        mapsToTestAgainst.add(getFullMap());
        mapsToTestAgainst.add(getExtraMap());

        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readInventoryTwoDealersRoots() {
        // Expected: XMLIO.readInventory() throws a ReadWriteException
        XMLIO xmlIO = null;
        try {
            xmlIO = getXMLIO("r10", 'r', false);
        } catch (ReadWriteException e) {
            fail(e);
        }

        try {
            xmlIO.readInventory();
            fail(xmlIO.toString());
        } catch (ReadWriteException e) {
            assertInstanceOf(Exception.class, e.getCause());
        }
    }

    @Test
    void readInventoryNoDealerTags() {
        // Expected: No data found.
        List<Map<Key, Object>> mapsToTest = readInventory("r11", 0);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();

        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readInventoryLowerCaseTags() {
        // Expected: No issues, all Vehicles read.
        List<Map<Key, Object>> mapsToTest = readInventory("r12", 3);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();

        Map<Key, Object> minMap = getMinMap();
        minMap.put(DEALERSHIP_NAME, "name");
        mapsToTestAgainst.add(minMap);
        mapsToTestAgainst.add(getFullMap());
        mapsToTestAgainst.add(getExtraMap());

        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readInventoryAllAttributes() {
        // Expected: No issues, all Vehicles read.
        List<Map<Key, Object>> mapsToTest = readInventory("r13", 4);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        mapsToTestAgainst.add(getMinMap());
        mapsToTestAgainst.add(getFullMap());
        mapsToTestAgainst.add(getExtraMap());
        mapsToTestAgainst.add(getMinMapNewDealer());

        mapsToTestAgainst.getFirst().put(DEALERSHIP_NAME, "name");
        mapsToTestAgainst.getLast().put(DEALERSHIP_NAME, "name2");

        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readInventoryAllTags() {
        // Expected: No issues, all Vehicles read.
        List<Map<Key, Object>> mapsToTest = readInventory("r14", 4);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        mapsToTestAgainst.add(getMinMap());
        mapsToTestAgainst.add(getFullMap());
        mapsToTestAgainst.add(getExtraMap());
        mapsToTestAgainst.add(getMinMapNewDealer());

        mapsToTestAgainst.getFirst().put(DEALERSHIP_NAME, "name");
        mapsToTestAgainst.getLast().put(DEALERSHIP_NAME, "name2");

        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readInventoryAllTagsAndAttributes() {
        // Expected: No issues, all Vehicles read.
        List<Map<Key, Object>> mapsToTest = readInventory("r16", 4);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        mapsToTestAgainst.add(getMinMap());
        mapsToTestAgainst.add(getFullMap());
        mapsToTestAgainst.add(getExtraMap());
        mapsToTestAgainst.add(getMinMapNewDealer());

        mapsToTestAgainst.getFirst().put(DEALERSHIP_NAME, "name");
        mapsToTestAgainst.getLast().put(DEALERSHIP_NAME, "name2");

        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readInventoryTagsWithinTags() {
        /*
         * So long as the tag is in the correct region, tags within tags is fine.
         * It is possible to parse values from a String with a tag within that
         * String (though it has a possibility of white space errors).
         *
         * Region: <Any Tag Name> -> <Dealer>
         * Tag / Attributes: None
         *
         * Region: <Dealer> -> <Vehicle>
         * Tag / Attributes: ID, Name
         *
         * Region: <Vehicle> -> <Rest of the Tags>
         * Tag / Attributes: ID, Type, Unit, Price, Make, Model
         */
        List<Map<Key, Object>> mapsToTest = readInventory("r17", 4);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        mapsToTestAgainst.add(getMinMap());
        mapsToTestAgainst.add(getFullMap());
        mapsToTestAgainst.add(getExtraMap());
        mapsToTestAgainst.add(getMinMapNewDealer());

        mapsToTestAgainst.getFirst().put(DEALERSHIP_NAME, "name");
        mapsToTestAgainst.getLast().put(DEALERSHIP_NAME, "name2");

        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readInventoryVehicleWithinVehicle() {
        // Expected: Key.REASON_FOR_ERROR is added to map, marking last duplicate.
        List<Map<Key, Object>> mapsToTest = readInventory("r18", 1);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        Map<Key, Object> fullMap = getFullMap();
        mapsToTestAgainst.add(fullMap);
        VEHICLE_ID.putNonNull(fullMap, "v_id3");

        putReasonDuplicate(XMLKey.V_ID, "v_id3", "v_id1", fullMap);

        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void readInventoryVehicleOutSideDealership() {
        // Expected: Vehicle outside of Dealership is discarded. Other Vehicle is read fine.
        List<Map<Key, Object>> mapsToTest = readInventory("r19", 1);

        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
        mapsToTestAgainst.add(getExtraMap());

        testMaps(mapsToTestAgainst, mapsToTest);
    }

    @Test
    void writeInventoryThroughFileIOBuilder() {
        // Expected: FileIOBuilder.buildNewFileIO() throws exception.
        XMLIO xmlIO;
        try {
            xmlIO = getXMLIO("w1", 'w', true);
            fail(xmlIO.toString());
        } catch (ReadWriteException e) {
            BadExtensionException cause = new BadExtensionException("Bad extension.");
            assertTrue(FileIOBuilderTest.isSameCauseType(new ReadWriteException(cause), e));
        }
    }

    @Test
    void writeInventoryManually() {
        // Expected: writeInventory() throws exception.
        String path = FileIOBuilderTest.getPath("w1", "xmlIOTests", ".xml");
        XMLIO xmlIO = null;
        try {
            xmlIO = new XMLIO(path, 'w');
        } catch (ReadWriteException e) {
            fail(e);
        }

        assertNotNull(xmlIO);
        List<Map<Key, Object>> maps = new ArrayList<>();
        maps.add(getMinMap());
        try {
            xmlIO.writeInventory(maps);
        } catch (ReadWriteException e) {
            String reason = "Can not write to XML Files.";
            assertInstanceOf(ReadWriteException.class, e);
            assertEquals(reason, e.getMessage());
        }
    }

    @Test
    void writeInventoryReadFromWriteXML() {
        // Expected: readInventory() throws exception.
        String path = FileIOBuilderTest.getPath("w1", "xmlIOTests", ".xml");
        XMLIO xmlIO = null;
        try {
            xmlIO = new XMLIO(path, 'w');
        } catch (ReadWriteException e) {
            fail(e);
        }

        try {
            xmlIO.readInventory();
        } catch (ReadWriteException e) {
            String reason = "Must be mode 'r', not mode 'w'.";
            assertInstanceOf(ReadWriteException.class, e);
            assertEquals(reason, e.getMessage());
        }
    }

    @Test
    void writeInventoryWriteFromReadXML() {
        // Expected: writeInventory() throws exception.
        XMLIO xmlIO = null;
        try {
            xmlIO = getXMLIO("w1", 'r', false);
        } catch (ReadWriteException e) {
            fail(e);
        }

        assertNotNull(xmlIO);
        List<Map<Key, Object>> maps = new ArrayList<>();
        maps.add(getMinMap());
        try {
            xmlIO.writeInventory(maps);
        } catch (ReadWriteException e) {
            String reason = "Can not write to XML Files.";
            assertInstanceOf(ReadWriteException.class, e);
            assertEquals(reason, e.getMessage());
        }
    }
}