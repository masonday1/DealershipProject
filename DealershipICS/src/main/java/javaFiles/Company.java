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
}