package javafiles.dataaccessfiles;

import javafiles.customexceptions.ReadWriteException;

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
    D_ID (Key.DEALERSHIP_ID, "id"),
    D_NAME (Key.DEALERSHIP_NAME,  "Name"),

    TYPE (Key.VEHICLE_TYPE, "type"),
    V_ID (Key.VEHICLE_ID, "id"),

    PRICE_UNIT (Key.VEHICLE_PRICE_UNIT, "price_unit"),
    PRICE (Key.VEHICLE_PRICE, "price"),

    MODEL (Key.VEHICLE_MODEL, "model"),
    MAKE (Key.VEHICLE_MANUFACTURER, "make"),

    REASON (Key.REASON_FOR_ERROR, "Reason");

    private final Key KEY;
    private final String NAME;

    XMLKey(Key key, String xmlName) {
        KEY = key;
        NAME = xmlName;
    }

    public Key getKey() {return KEY;}
    public String getName() {return NAME;}
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

    private void parseNode(XMLKey[] keys, Map<Key, Object> map, String tagName, String nodeValue) {
        if (map == null || keys == null) {return;}

        for (XMLKey key: keys) {

            if (key.getName().equalsIgnoreCase(tagName)) {
                Object nodeValCast;
                if (key.getKey().getClassName().equals(Long.class.getName())) {
                    nodeValCast = Long.parseLong(nodeValue);
                } else {
                    nodeValCast = nodeValue;
                }
                if (map.containsKey(key.getKey())) {
                    System.out.println("Already has key: " + key.getKey());
                }
                map.put(key.getKey(), nodeValCast);
                return;
            }

        }
    }

    private void readXMLObject(Node node, XMLKey[] keys, Map<Key, Object> map, String stopTag, List<Node> haltedNodes) {
        String tagName = node.getNodeName();
        String nodeValue = node.getTextContent();

        if (tagName.equalsIgnoreCase(stopTag)) {
            if (haltedNodes != null) {
                haltedNodes.add(node);
            }
            return;
        }

        parseNode(keys, map, tagName, nodeValue);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node newNode = childNodes.item(i);
                if (!newNode.getNodeName().startsWith("#text")) {
                    readXMLObject(newNode, keys, map, stopTag, haltedNodes);
                }
            }
            NamedNodeMap attributes = element.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);
                String atrTagName = attribute.getNodeName();
                String atrValue = attribute.getNodeValue();
                parseNode(keys, map, atrTagName, atrValue);
            }
        }
    }

    private List<Map<Key, Object>> parseDocument(Document document) {
        List<Map<Key, Object>> maps = new ArrayList<>();

        Element documentElement = document.getDocumentElement();

        List<Node> haltedNodesDealer = new ArrayList<>();

        readXMLObject(documentElement, null, null, "Dealer", haltedNodesDealer);

        XMLKey[] dealerKeys = {XMLKey.D_ID, XMLKey.D_NAME};
        XMLKey[] vehicleKeys = {XMLKey.TYPE, XMLKey.V_ID, XMLKey.PRICE,
                XMLKey.PRICE_UNIT, XMLKey.MAKE, XMLKey.MODEL};

        for (Node dealerNode : haltedNodesDealer) {

            List<Node> haltedNodesVehicle = new ArrayList<>();
            Map<Key, Object> dealerMap = new HashMap<>();
            readXMLObject(dealerNode, dealerKeys, dealerMap, "Vehicle", haltedNodesVehicle);

            for (Node vehicleNode : haltedNodesVehicle) {
                Map<Key, Object> vehicleMap = new HashMap<>(dealerMap);
                readXMLObject(vehicleNode, vehicleKeys, vehicleMap, null, null);
                maps.add(vehicleMap);
            }

        }

        return maps;
    }

    public List<Map<Key, Object>> readInventory() throws ReadWriteException {
        if (mode != 'r') {
            throw new ReadWriteException("Must be mode 'r', not mode '" + mode + "'.");
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;

        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            ArrayList<Map<Key, Object>> reasonList = new ArrayList<>();
            Map<Key, Object> reasonMap = new HashMap<>();
            reasonMap.put(XMLKey.REASON.getKey(), e);
            reasonList.add(reasonMap);
            return reasonList;
        }

        return parseDocument(document);
    }

    public void writeInventory(List<Map<Key, Object>> maps) throws ReadWriteException {
        throw new ReadWriteException("Can not write to XML Files.");
    }
}
