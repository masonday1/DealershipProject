package javafiles.dataaccessfiles;

import javafiles.Key;
import static javafiles.Key.*;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class XMLKeyTest {
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
                VEHICLE_ACQUISITION_DATE
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
    public void putNonNullNullTest() {
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
}