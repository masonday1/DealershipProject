package javaFiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Company {
    private String company_id;
    private String company_name;
    private ArrayList<Dealership> list_dealerships;


    public Company(String company_id, String company_name) {
        this.company_id = company_id;
        this.company_name = company_name;
        this.list_dealerships = new ArrayList<>();
    }

    public void add_dealership(Dealership dealership) {list_dealerships.add(dealership);}

    public ArrayList<Dealership> get_list_dealerships() {return list_dealerships;}

    public Dealership find_dealership(String dealer_id) {
        for (Dealership dealership : list_dealerships) {
            if (dealership.getDealerId().equals(dealer_id)) {
                return dealership;
            }
        }
        return null;
    }

    public String getCompanyId() {return company_id;}

    public String getCompanyName() {return company_name;}

    public void dataToInventory(List<Map<String, Object>> data) {
        for (Map<String, Object> map: data) {
            Dealership dealership = find_dealership(JSONIO.getDealIDVal(map));
            if (dealership == null) {
                dealership = new Dealership(JSONIO.getDealIDVal(map));
                add_dealership(dealership);
            }
            dealership.dataToInventory(map);
        }
    }

    /**
     * Retrieves Vehicle data for all Dealerships within the Company.
     * <p>
     * This method gathers Vehicle information from all Dealerships associated with the
     * Company and compiles it into a single list of Maps. Each Map in the List
     * represents a Vehicle and contains its attributes.
     *
     * @return A {@link List} of {@link Map} Objects. Each {@link Map} represents a Vehicle
     *         and contains its attributes (dealership ID, vehicle type, manufacturer, model,
     *         vehicle ID, price, and acquisition date) as key-value pairs. Returns all vehicles from each
     *         Dealership in the Company. Returns an empty list if the Company has no Dealerships
     *         or if none of the Dealerships have any Vehicles.
     */
    public List<Map<String, Object>> getDataMap() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Dealership dealership : list_dealerships) {
            list.addAll(dealership.getDataMap());
        }
        if (list.isEmpty()) {return null;}
        return list;
    }
}