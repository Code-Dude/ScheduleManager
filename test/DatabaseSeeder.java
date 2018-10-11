/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import schedulemanager.model.Address;
import schedulemanager.model.AddressManager;
import schedulemanager.model.AppointmentManager;
import schedulemanager.model.City;
import schedulemanager.model.CityManager;
import schedulemanager.model.Country;
import schedulemanager.model.CountryManager;
import schedulemanager.model.Customer;
import schedulemanager.model.CustomerManager;
import schedulemanager.model.User;
import schedulemanager.model.UserManager;

/**
 *
 * @author scott
 */
public class DatabaseSeeder {
    
    private Connection dbConnection;
    private CountryManager countryManager;
    private CityManager cityManager;
    private AddressManager addressManager;
    private CustomerManager customerManager;
    private AppointmentManager appointmentManager;
    private UserManager userManager;
    
    public DatabaseSeeder(Connection dbConnection, CountryManager countryManager, 
                          CityManager cityManager, AddressManager addressManager, 
                          CustomerManager customerManager, 
                          AppointmentManager appointmentManager,
                          UserManager userManager) {
        
        this.dbConnection = dbConnection;
        this.countryManager = countryManager;
        this.cityManager = cityManager;
        this.addressManager = addressManager;
        this.customerManager = customerManager;
        this.appointmentManager = appointmentManager;
        this.userManager = userManager;
    }
    
    public void seedCountryTable() {
        try {
            ArrayList<String> countryNames = new ArrayList<>();
            countryNames.add("England");
            countryNames.add("United States");
            countryNames.add("Mexico");

            for(int i = 0; i < countryNames.size(); ++i) {
                int countryID = countryManager.autoGenID();
                Country country = new Country(countryID, countryNames.get(i), "System");

                countryManager.addCountry(dbConnection, country);
            }
        } catch(SQLException e) {
            System.out.println("Issue seeding country table.");
        }
    }
    
    public void seedCityTable() {
        try {
            HashMap<String, Integer> cities = new HashMap();
            String[] cityNames = {"New York", "Phoenix", "London", "Mexico City"};
            cities.put(cityNames[0], countryManager.getCountry("United States").getCountryID());
            cities.put(cityNames[1],countryManager.getCountry("United States").getCountryID());
            cities.put(cityNames[2], countryManager.getCountry("England").getCountryID());
            cities.put(cityNames[3], countryManager.getCountry("Mexico").getCountryID());
            
            for(int i = 0; i < cityNames.length; ++i) {
                int cityID = cityManager.autoGenID();
                City city = new City(cityID, cityNames[i], cities.get(cityNames[i]), "System");
                cityManager.addCity(dbConnection, city);
            }
            
        } catch(SQLException e) {
            System.out.println("Issue seeding city table.");
        }
    }
    
    public void seedAddressTable() {
        try {
            for(int i = 0; i < cityManager.getCities().size(); ++i) {
                int addressID = addressManager.autoGenID();
                String address = "000" + i + " Dr";
                String address2 = "111" + i + " Road";
                int cityID = cityManager.getCity(i + 1).getCityID();
                String postalCode = "7777" + i;
                String phoneNumber = "123-456-78" + i;
                Address newAddress = new Address(addressID, address, address2,
                                      cityID, postalCode, phoneNumber, "System");
                addressManager.addAddress(dbConnection, newAddress);
            }
        } catch(SQLException e) {
            System.out.println("Issue seeding address table.");
        }
    }
    
    public void seedCustomerTable() {
        try {
            for(int i = 0; i < addressManager.getAddresses().size(); ++i) {
                int customerID = customerManager.autoGenID();
                String name = "test person " + i;
                int addressID = addressManager.getAddress(i + 1).getAddressID();
                int active = 1;
                Customer customer = new Customer(customerID, name, addressID, active);
                customerManager.addCustomer(dbConnection, customer);
            }
        } catch(SQLException e) {
            System.out.println("Issue seeding customer table.");
        }
    }
    
    public void seedUserTable() {
        try {
            int userID = userManager.autoGenID();
            String userName = "test";
            String password = "test";
            int active = 1;
            User user = new User(userID, userName, password, active, "System");
            userManager.addUser(dbConnection, user);
        } catch(SQLException e) {
            System.out.println("Issue seeding user table.");
        }
    }
    
    public void seedDatabase() {
        seedCountryTable();
        seedCityTable();
        seedAddressTable();
        seedCustomerTable();
        seedUserTable();
    }
}
