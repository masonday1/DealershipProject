package javaFiles;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum XMLKey {
    D_ID (Key.DEALERSHIP_ID, "Dealer_id"),
    PRICE (Key.VEHICLE_PRICE, "Vehicle_Price"),
    TYPE (Key.VEHICLE_TYPE, "Vehicle_type"),
    V_ID (Key.VEHICLE_ID, "Vehicle_id"),
    MODEL (Key.VEHICLE_MODEL, "Vehicle_Model"),
    MAKE (Key.VEHICLE_MANUFACTURER, "Vehicle_Make"),
    PRICE_UNIT (Key.VEHICLE_PRICE_UNIT, "Vehicle_Price_unit"),
    D_NAME (Key.DEALERSHIP_NAME, "Dealer_Name"),
    DATE ( Key.VEHICLE_ACQUISITION_DATE, "Vehicle_Acquisition_Date");

    private final Key KEY;
    private final String NAME;

    XMLKey(Key key, String xmlName) {
        KEY = key;
        NAME = xmlName;
    }

    public Key getKey() {
        return KEY;
    }

    public String getName() {
        return NAME;
    }
}

/**
 * A class that reads and writes to XML files
 *
 * @author Dylan Browne
 */
public class XMLIO extends FileIO {
    public XMLIO(String path, char mode) throws ReadWriteException {
        super(path, mode);
        if (path.endsWith("pom.xml")) {
            throw new ReadWriteException("Can't read or write to Maven's pom.xml file.\n" +
                    "If this is not Maven's pom.xml file, rename it and try again.");
        }
    }

    private void parseNode(Node node, String prefix, Map<String, String> map, boolean ignoreBody) {
        if (prefix == null) {
            prefix = "";
        }
        if (node == null || node.getNodeName().startsWith("#")) {
            return;
        }
        String name = prefix + node.getNodeName();
        String value =  node.getTextContent();

        if (map.containsKey(name)) {
            return;
        }
        if (!ignoreBody) {
            map.put(name, value);
        }

        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                parseNode(attributes.item(i), name + "_", map, false);
            }
        }

        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            parseNode(nodes.item(i), name + "_", map, false);
        }
    }

    private void readXMLObject(Map<String, String> vehicleMap, Map<String, Object> map) {
        for (XMLKey xmlKey : XMLKey.values()) {
            String keyStr = xmlKey.getName();
            if (vehicleMap.containsKey(keyStr)) {
                Key key = xmlKey.getKey();
                if (key != null) {
                    Object val = vehicleMap.get(keyStr);
                    if (key.getClassName().equals(Long.class.getName())) {
                        val = Long.valueOf((String) val);
                    }
                    map.put(key.getKey(), val);

                }
            }
        }
    }

    public List<Map<String, Object>> readInventory() throws ReadWriteException {
        if (mode != 'r') {
            throw new ReadWriteException("Must be mode 'r', not mode '" + mode + "'.");
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        try {
            document = builder.parse(file);

        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        List<Map<String, Object>> maps = new ArrayList<>();

        Element documentElement = document.getDocumentElement();
        NodeList dealers =  documentElement.getElementsByTagName("Dealer");
        for (int i = 0; i < dealers.getLength(); i++) {
            Map<String, String> dealerInfoMap = new HashMap<>();

            Node dealer = dealers.item(i);

            if (dealer.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) dealer;
                NodeList vehicles = e.getElementsByTagName("Vehicle");

                Node dealerName = e.getElementsByTagName("Name").item(0);
                parseNode(dealerName, "Dealer_", dealerInfoMap, false);

                Node dealerId = e.getAttributeNode("id");
                parseNode(dealerId, "Dealer_", dealerInfoMap, false);

                for (int j = 0; j < vehicles.getLength(); j++) {
                    Map<String, String> nameMap = new HashMap<>(dealerInfoMap);
                    Map<String, Object> map = new HashMap<>();
                    parseNode(vehicles.item(j), null, nameMap, true);
                    readXMLObject(nameMap, map);

                    if (validMap(map)) {
                        maps.add(map);
                    }
                }
            }

        }

        return maps;
    }

    public int writeInventory(List<Map<String, Object>> maps) throws ReadWriteException {
        System.out.println("Not implemented (writeInventory).");
        return 0;
    }
}
