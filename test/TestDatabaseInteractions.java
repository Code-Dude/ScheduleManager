/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import schedulemanager.model.Address;
import schedulemanager.model.AddressManager;
import schedulemanager.model.Appointment;
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
public class TestDatabaseInteractions {
    
    private Connection dbConnection;
    private UserManager userManager;
    private AppointmentManager appointmentManager;
    private CustomerManager customerManager;
    private AddressManager addressManager;
    private CityManager cityManager;
    private CountryManager countryManager;
    
    public TestDatabaseInteractions(Connection dbConnection, UserManager userManager,
           AppointmentManager appointmentManager, CustomerManager customerManager,
           AddressManager addressManager, CityManager cityManager,
           CountryManager countryManager) {
        
        this.dbConnection = dbConnection;
        this.userManager = userManager;
        this.appointmentManager = appointmentManager;
        this.customerManager = customerManager;
        this.addressManager = addressManager;
        this.cityManager = cityManager;
        this.countryManager = countryManager;
    }
    
    public void addUser(int userID) {
        try {
            User newUser = new User(userID, "test", "test", 1, "System");
            userManager.addUser(dbConnection, newUser);
        } catch(SQLException e) {
            System.out.println("Issue adding a new user");
        }
    }
    
    public void deleteUser(int userID) {
        try {
            User user = userManager.getUser(userID);
            userManager.deleteUser(dbConnection, user);
        } catch(SQLException e) {
            System.out.println("Issue deleting a user");
        }
    }
    
    public void testUserCRUD() {
        int userID = userManager.autoGenID();
        addUser(userID);
        deleteUser(userID);
    }
    
    public void addAddress(int addressID) {
        try {
            Address newAddress = new Address(addressID, "111 one dr", "222 two dr", 123,
                                             "99999", "123-456-7890", "System");
            addressManager.addAddress(dbConnection, newAddress);
        } catch(SQLException e) {
            System.out.println("Issue adding a new Address");
        }
    }
    
    public void updateAddress(int addressID) {
        try {
            Address modifiedAddress = addressManager.getAddress(addressID);
            modifiedAddress.setPhoneNumber("555-666-7777");
            addressManager.updateAddress(dbConnection, modifiedAddress, "System");
        } catch(SQLException e) {
            System.out.println("Issue updating an address");
        }
    }
    
    public void deleteAddress(int addressID) {
        try {
            Address address = addressManager.getAddress(addressID);
            addressManager.deleteAddress(dbConnection, address);
        } catch(SQLException e) {
            System.out.println("Issue deleting address");
        }
    }
    
    public void testAddressCRUD() {
        int addressID = addressManager.autoGenID();
        addAddress(addressID);
        updateAddress(addressID);
        deleteAddress(addressID);
    }
    
    public void addNewCustomer(int customerID) {
        try {
            Customer newCustomer = new Customer(customerID, "new guy", 5555, 1);
            customerManager.addCustomer(dbConnection, newCustomer);
        } catch(SQLException e) {
            System.out.println("Issue adding a new customer.");
        }
    }
    
    public void modifyCustomer(int customerID) {
        try {
            Customer modifiedCustomer = customerManager.getCustomer(customerID);
            modifiedCustomer.setCustomerName("New Super Name");
            customerManager.updateCustomer(dbConnection, modifiedCustomer, "new User");
        System.out.println();
        } catch(SQLException e) {
            System.out.println("Issue modifying a customer.");
        }
    }
    
    public void deleteCustomer(int customerID) {
        try {
            Customer customer = customerManager.getCustomer(customerID);
            customerManager.deleteCustomer(dbConnection, customer);
        } catch(SQLException e) {
            System.out.println("Issue deleting a customer.");
        }
    }
    
    public void testCustomerCRUD() {
        int customerID = customerManager.autoGenID();
        addNewCustomer(customerID);
        modifyCustomer(customerID);
        deleteCustomer(customerID);
    }
    
    public void addAppointment(int apptID, int userID, int customerID) {
        try {
            User user = userManager.getUser(userID);
            Customer customer = customerManager.getCustomer(customerID);
            String title = "test title";
            String description = "Simple Description";
            String location = "London";
            String contact = "phone";
            String apptURL = "meeting.url";
            Instant instant = Instant.now();
            long millis = instant.toEpochMilli();
            Timestamp apptStart = new Timestamp(millis);
            instant = instant.now();
            millis = instant.toEpochMilli();
            Timestamp apptEnd = new Timestamp(millis);

            Appointment appointment = new Appointment(apptID, customer.getCustomerID(),
                                          title, description,
                                          location, contact, apptURL, apptStart,
                                          apptEnd, "System");

            appointmentManager.addAppointment(dbConnection, appointment);
        } catch(SQLException e) {
            System.out.println("Issue adding an appointment.");
        }
    }
    
    public void modifyAppointment(int apptID, int userID) {
        try {
            Appointment appointment = appointmentManager.getAppointment(apptID);
            User user = userManager.getUser(userID);
            appointment.setTitle("New appt title");
            appointmentManager.updateAppointment(dbConnection, appointment, user.getUserName());
        } catch(SQLException e) {
            System.out.println("Issue modifying an appointment.");
        }
    }
    
    public void deleteAppointment(int apptID) {
        try {
            Appointment appointment = appointmentManager.getAppointment(apptID);
            appointmentManager.deleteAppointment(dbConnection, appointment);
            appointmentManager.printAppointments();
        } catch(SQLException e) {
            System.out.println("Issue deleting an appointment.");
        }
    }
    
    public void testAppointmentCRUD() {
        int userID = userManager.autoGenID();
        int customerID = customerManager.autoGenID();
        int appointmentID = appointmentManager.autoGenID();
        addUser(userID);
        addNewCustomer(customerID);
        addAppointment(appointmentID, userID, customerID);
        modifyAppointment(appointmentID, userID);
        deleteAppointment(appointmentID);
        deleteCustomer(customerID);
        deleteUser(userID);
    }
    
    public void addCity(int cityID) {
        try {
            City city = new City(cityID, "London", 1, "System");
            cityManager.addCity(dbConnection, city);
        } catch(SQLException e) {
            System.out.println("Issue adding a city.");
        }
    }
    
    public void updateCity(int cityID) {
        try {
            City city = cityManager.getCity(cityID);
            city.setCity("Manchester");
            cityManager.updateCity(dbConnection, city, "System");
        } catch(SQLException e) {
            System.out.println("Issue updating a city.");
        }
    }
    
    public void deleteCity(int cityID) {
        try {
            City city = cityManager.getCity(cityID);
            cityManager.deleteCity(dbConnection, city);
        } catch(SQLException e) {
            System.out.println("Issue deleting a city.");
        }
    }
    
    public void testCityCRUD() {
        int cityID = cityManager.autoGenID();
        addCity(cityID);
        updateCity(cityID);
        deleteCity(cityID);
    }
    
    public void addCountry(int countryID) {
        try {
            Country country = new Country(countryID, "England", "System");
            countryManager.addCountry(dbConnection, country);
        } catch(SQLException e) {
                System.out.println("Issue adding a country.");
        }
    }
    
    public void updateCountry(int countryID) {
        try {
            Country country = countryManager.getCountry(countryID);
            country.setCountry("Scotland");
            countryManager.updateCountry(dbConnection, country, "System");
        } catch (SQLException e) {
            System.out.println("Issue updating a country.");
        }
    }
    
    public void deleteCountry(int countryID) {
        try {
            Country country = countryManager.getCountry(countryID);
            countryManager.deleteCountry(dbConnection, country);
        } catch(SQLException e) {
            System.out.println("Issue deleting country.");
        }
    }
    
    public void testCountryCRUD() {
        int countryID = countryManager.autoGenID();
        addCountry(countryID);
        updateCountry(countryID);
        deleteCountry(countryID);
    }
    
    public void runTests() {
        testUserCRUD();
        testAddressCRUD();
        testCustomerCRUD();
        testAppointmentCRUD();
        testCityCRUD();
        testCountryCRUD();
    }
}
