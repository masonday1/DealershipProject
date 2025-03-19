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

    private boolean parseNode(Node node, String prefix, Map<String, String> map, boolean ignoreBody) {
        if (prefix == null) {
            prefix = "";
        }
        if (node.getNodeName().startsWith("#")) {
            return false;
        }
        String name = prefix + node.getNodeName();
        String value =  node.getTextContent();

        if (map.containsKey(name)) {
            return false;
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

        return true;
    }

    private Map<String, Object> readXMLObject(Map<String, String> vehicleMap, Map<String, Object> map) {
        Map<String, Key> mapKeys = new HashMap<>();
        // Dealer Keys
        mapKeys.put("Dealer_id", Key.DEALERSHIP_ID);

        // Vehicle keys
        mapKeys.put("Vehicle_Price", Key.VEHICLE_PRICE);
        mapKeys.put("Vehicle_type", Key.VEHICLE_TYPE);
        mapKeys.put("Vehicle_id", Key.VEHICLE_ID);
        mapKeys.put("Vehicle_Model", Key.VEHICLE_MODEL);
        mapKeys.put("Vehicle_Make", Key.VEHICLE_MANUFACTURER);

        // Not in keys
        mapKeys.put("Vehicle_Price_unit", null);
        mapKeys.put("Dealer_Name", null);

        // Not in data
        mapKeys.put("Vehicle_Acquisition_Date", Key.VEHICLE_ACQUISITION_DATE);

        for (String keyStr : mapKeys.keySet()) {
            if (vehicleMap.containsKey(keyStr)) {
                Key key = mapKeys.get(keyStr);
                if (key != null) {
                    Object val = vehicleMap.get(keyStr);
                    if (key.getClassName().equals(Long.class.getName())) {
                        val = Long.valueOf((String) val);
                    }
                    map.put(key.getKey(), val);

                }
            }
        }

        return map;
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

        Element element = document.getDocumentElement();
        NodeList dealers =  element.getElementsByTagName("Dealer");
        for (int i = 0; i < dealers.getLength(); i++) {
            Map<String, String> nameMap = new HashMap<>();

            Node dealer = dealers.item(i);

            Node dealerName = ((Element) dealer).getElementsByTagName("Name").item(0);
            parseNode(dealerName, "Dealer_", nameMap, false);

            Node dealerId = ((Element) dealer).getAttributeNode("id");
            parseNode(dealerId, "Dealer_", nameMap, false);

            if (dealer.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) dealer;
                NodeList vehicles = e.getElementsByTagName("Vehicle");
                for (int j = 0; j < vehicles.getLength(); j++) {
                    Map<String, String> nameMapTemp = new HashMap<>(nameMap);
                    Map<String, Object> map = new HashMap<>();
                    if (parseNode(vehicles.item(j), null, nameMapTemp, true)) {
                        maps.add(readXMLObject(nameMapTemp, map));
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

    public List<Map<String, Object>> readDealerships() throws ReadWriteException {
        System.out.println("Not implemented (readDealerships).");
        return null;
    }
    public int writeDealerships(List<Map<String, Object>> maps) throws ReadWriteException {
        System.out.println("Not implemented (writeDealerships).");
        return 0;
    }
}
