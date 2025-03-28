package javafiles;

import static javafiles.Key.*;

import javafiles.customexceptions.ReadWriteException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KeyTest {
    private void putNonNullObjectTest(Object exampleInstance, Key[] notAddedKeys) {
        Map<Key, Object> map = new HashMap<>();

        int prevLen = 0;

        for (Key key: Key.values()) {
            key.putNonNull(map, exampleInstance);

            boolean added = true;

            for (Key badKey: notAddedKeys) {
                if (key.equals(badKey)) {
                    added = false;
                    break;
                }
            }

            if (added) {assertEquals(prevLen + 1, map.size());} else {assertEquals(prevLen, map.size());}

            prevLen = map.size();
        }
    }

    @Test
    public void putNonNullStringTest() {
        Key[] notAddedKeys = {
                DEALERSHIP_RECEIVING_STATUS,
                DEALERSHIP_RENTING_STATUS,
                VEHICLE_RENTAL_STATUS,
                VEHICLE_PRICE,
                VEHICLE_ACQUISITION_DATE,
                REASON_FOR_ERROR
        };

        putNonNullObjectTest("String", notAddedKeys);
    }

    @Test
    public void putNonNullLongTest() {
        Key[] notAddedKeys = new Key[values().length - 2];

        int i = 0;
        for (Key key: Key.values()) {
            if (!key.equals(VEHICLE_ACQUISITION_DATE) && !key.equals(VEHICLE_PRICE)) {
                notAddedKeys[i] = key;
                i++;
            }
        }
        assertEquals(notAddedKeys.length, i);

        putNonNullObjectTest(1046L, notAddedKeys);
    }

    @Test
    public void putNonNullBooleanTest() {
        Key[] notAddedKeys = new Key[values().length - 3];

        int i = 0;
        for (Key key: Key.values()) {
            if (!key.equals(DEALERSHIP_RECEIVING_STATUS) && !key.equals(DEALERSHIP_RENTING_STATUS)
                    && !key.equals(VEHICLE_RENTAL_STATUS)) {
                notAddedKeys[i] = key;
                i++;
            }
        }
        assertEquals(notAddedKeys.length, i);

        putNonNullObjectTest(true, notAddedKeys);
    }

    @Test
    public void putNonNullTest() {
        putNonNullObjectTest(null, Key.values());
    }

    @Test
    public void putNonNullOtherObjectTest() {
        putNonNullObjectTest(new HashMap<Key, Object>(), Key.values());
    }

    @Test
    public void putNonNullKeyTest() {
        putNonNullObjectTest(VEHICLE_ID, Key.values());
    }

    private Map<Key, Object> getGoodMap() {
        Map<Key, Object> map = new HashMap<>();

        map.put(Key.VEHICLE_PRICE_UNIT, "$");
        map.put(Key.VEHICLE_PRICE, 1000L);
        map.put(Key.VEHICLE_MODEL, "model");
        map.put(Key.DEALERSHIP_RENTING_STATUS, true);
        map.put(Key.VEHICLE_TYPE, "suv");
        map.put(Key.DEALERSHIP_NAME, "d_name");
        map.put(Key.DEALERSHIP_RECEIVING_STATUS, true);
        map.put(Key.DEALERSHIP_ID, "d_id");
        map.put(Key.VEHICLE_MANUFACTURER, "make");
        map.put(Key.VEHICLE_ID, "v_id");
        map.put(Key.VEHICLE_RENTAL_STATUS, true);
        map.put(Key.VEHICLE_ACQUISITION_DATE, 123L);

        map.put(REASON_FOR_ERROR, new ReadWriteException("No error, just need all keys."));

        return map;
    }

    @Test
    public void getValGoodMapGoodValStr() {
        Map<Key, Object> map = getGoodMap();
        String str = DEALERSHIP_NAME.getVal(map, String.class);
        assertNotNull(str);
    }

    @Test
    public void getValGoodMapGoodValLong() {
        Map<Key, Object> map = getGoodMap();
        Long num = VEHICLE_ACQUISITION_DATE.getVal(map, Long.class);
        assertNotNull(num);
    }

    @Test
    public void getValGoodMapGoodValBool() {
        Map<Key, Object> map = getGoodMap();
        Boolean bool = VEHICLE_RENTAL_STATUS.getVal(map, Boolean.class);
        assertNotNull(bool);
    }

    @Test
    public void getValGoodMapAllGoodVal() {
        Map<Key, Object> map = getGoodMap();
        for (Key key: Key.values()) {
            Object obj = null;
            String className = key.getClassName();

            if (className.equals(String.class.getName())) {
                obj = key.getVal(map, String.class);
            } else if (className.equals(Long.class.getName())) {
                obj = key.getVal(map, Long.class);
            } else if (className.equals(Boolean.class.getName())) {
                obj = key.getVal(map, Boolean.class);
            } else if (className.equals(ReadWriteException.class.getName())) {
                obj = key.getVal(map, ReadWriteException.class);
            }

            assertNotNull(obj);
        }
    }

    @Test
    public void getValGoodMapBadValStr() {
        Map<Key, Object> map = getGoodMap();
        Long str = DEALERSHIP_NAME.getVal(map, Long.class);
        assertNull(str);
    }

    @Test
    public void getValGoodMapBadValLong() {
        Map<Key, Object> map = getGoodMap();
        Boolean num = VEHICLE_ACQUISITION_DATE.getVal(map, Boolean.class);
        assertNull(num);
    }

    @Test
    public void getValGoodMapBadValBool() {
        Map<Key, Object> map = getGoodMap();
        String bool = VEHICLE_RENTAL_STATUS.getVal(map, String.class);
        assertNull(bool);
    }

    @Test
    public void getValGoodMapAllBadVal() {
        Map<Key, Object> map = getGoodMap();
        for (Key key: Key.values()) {
            boolean calledFunction = false;
            Object obj = null;
            String className = key.getClassName();

            if (className.equals(String.class.getName())) {
                obj = key.getVal(map, Boolean.class);
                calledFunction = true;
            } else if (className.equals(Long.class.getName())) {
                obj = key.getVal(map, String.class);
                calledFunction = true;
            } else if (className.equals(Boolean.class.getName())) {
                obj = key.getVal(map, Long.class);
                calledFunction = true;
            } else if (className.equals(ReadWriteException.class.getName())) {
                obj = key.getVal(map, Integer.class);
                calledFunction = true;
            }

            assertTrue(calledFunction);
            assertNull(obj);
        }
    }

    private Map<Key, Object> getBadMap() {
        Map<Key, Object> map = new HashMap<>();

        map.put(Key.VEHICLE_PRICE_UNIT, false);
        map.put(Key.VEHICLE_PRICE, "One Hundred");
        map.put(Key.VEHICLE_MODEL, 123L);
        map.put(Key.DEALERSHIP_RENTING_STATUS, "Accepting");
        map.put(Key.VEHICLE_TYPE, 456);
        map.put(Key.DEALERSHIP_NAME, false);
        map.put(Key.DEALERSHIP_RECEIVING_STATUS, "Receiving");
        map.put(Key.DEALERSHIP_ID, 789L);
        map.put(Key.VEHICLE_MANUFACTURER, false);
        map.put(Key.VEHICLE_ID, 111213);
        map.put(Key.VEHICLE_RENTAL_STATUS, 111116543L);
        map.put(Key.VEHICLE_ACQUISITION_DATE, false);

        return map;
    }

    @Test
    public void getValBadMapGoodValStr() {
        Map<Key, Object> map = getBadMap();
        String str = DEALERSHIP_NAME.getVal(map, String.class);
        assertNull(str);
    }

    @Test
    public void getValBadMapGoodValLong() {
        Map<Key, Object> map = getBadMap();
        Long num = VEHICLE_ACQUISITION_DATE.getVal(map, Long.class);
        assertNull(num);
    }

    @Test
    public void getValBadMapGoodValBool() {
        Map<Key, Object> map = getBadMap();
        Boolean bool = VEHICLE_RENTAL_STATUS.getVal(map, Boolean.class);
        assertNull(bool);
    }

    @Test
    public void getValBadMapAllGoodVal() {
        Map<Key, Object> map = getBadMap();
        for (Key key: Key.values()) {
            boolean calledFunction = false;
            Object obj = null;
            String className = key.getClassName();

            if (className.equals(String.class.getName())) {
                obj = key.getVal(map, String.class);
                calledFunction = true;
            } else if (className.equals(Long.class.getName())) {
                obj = key.getVal(map, Long.class);
                calledFunction = true;
            } else if (className.equals(Boolean.class.getName())) {
                obj = key.getVal(map, Boolean.class);
                calledFunction = true;
            } else if (className.equals(ReadWriteException.class.getName())) {
                obj = key.getVal(map, ReadWriteException.class);
                calledFunction = true;
            }

            assertTrue(calledFunction);
            assertNull(obj);
        }
    }

    @Test
    public void getValBadMapBadValStr() {
        Map<Key, Object> map = getBadMap();
        Long str = DEALERSHIP_NAME.getVal(map, Long.class);
        assertNull(str);
    }

    @Test
    public void getValBadMapBadValLong() {
        Map<Key, Object> map = getBadMap();
        Boolean num = VEHICLE_ACQUISITION_DATE.getVal(map, Boolean.class);
        assertNull(num);
    }

    @Test
    public void getValBadMapBadValBool() {
        Map<Key, Object> map = getBadMap();
        String bool = VEHICLE_RENTAL_STATUS.getVal(map, String.class);
        assertNull(bool);
    }

    @Test
    public void getValBadMapAllBadVal() {
        Map<Key, Object> map = getBadMap();
        for (Key key: Key.values()) {
            boolean calledFunction = false;
            Object obj = null;
            String className = key.getClassName();

            if (className.equals(String.class.getName())) {
                obj = key.getVal(map, Boolean.class);
                calledFunction = true;
            } else if (className.equals(Long.class.getName())) {
                obj = key.getVal(map, String.class);
                calledFunction = true;
            } else if (className.equals(Boolean.class.getName())) {
                obj = key.getVal(map, Long.class);
                calledFunction = true;
            } else if (className.equals(ReadWriteException.class.getName())) {
                obj = key.getVal(map, Integer.class);
                calledFunction = true;
            }

            assertTrue(calledFunction);
            assertNull(obj);
        }
    }
}