/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author scott
 */
public class CustomerManager {
    private ObservableList<Customer> customers;
    
    public CustomerManager() {
        this.customers = FXCollections.observableArrayList();
    }
    
    public ObservableList<Customer> getCustomers() {
        return this.customers;
    }
    
    public void loadCustomers(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("select * from customer");
        while(rs.next()) {
            int customerID = rs.getInt("customerId");
            String customerName = rs.getString("customerName");
            int address = rs.getInt("addressId");
            int active = rs.getInt("active");
            Timestamp created = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdateTime = rs.getTimestamp("lastUpdate");
            String updatedBy = rs.getString("lastUpdateBy");

            Customer customer = new Customer(customerID, customerName,
                                    address, active, created, createdBy,
                                    lastUpdateTime, updatedBy);

            customers.add(customer);
        }
    }
    
    // Here is an example of a lambda expression, lambda expressions allow for
    // very expressive code that is easy to read when used properly. Here I use
    // a lambda expression to filter out customers who don't match the given
    // criteria using streams. The lambda expression allowed me to write a very
    // simple function that checked the customerID of every customer object that
    // was being passed to it. The use of streams also allows for very easy
    // parallelization of this operation.
    public Customer getCustomer(int customerID) {
        Stream<Customer> customerStream = customers.stream().unordered().parallel();
        Optional<Customer> customer = customerStream.filter(x -> x.getCustomerID() == customerID).findFirst();
        
        return customer.get();
    }
    
    public int getCustomerIndex(Customer customer) {
        return customers.indexOf(customer);
    }
    
    public void setCustomer(int indexOfCustomer, Customer customer) {
        if(customer != null) {
            this.customers.set(indexOfCustomer, customer);
        }
    }
    
    public void addCustomer(Connection conn, Customer customer) throws SQLException {
        customers.add(customer);
        
        String customerInsertSQL = "insert into customer"
                + " (customerId, customerName, addressId, active, createDate, createdBy, "
                + "lastUpdate, lastUpdateBy)"
                + "values(?,?,?,?,?,?,?,?)";

        PreparedStatement prepStatement = conn.prepareStatement(customerInsertSQL);
        prepStatement.setInt(1, customer.getCustomerID());
        prepStatement.setString(2, customer.getCustomerName());
        prepStatement.setInt(3, customer.getAddressID());
        prepStatement.setInt(4, customer.getActive());
        prepStatement.setTimestamp(5, customer.getCreatedDate());
        prepStatement.setString(6, customer.getCreatedBy());
        prepStatement.setTimestamp(7, customer.getLastUpdate());
        prepStatement.setString(8, customer.getLastUpdateBy());

        prepStatement.executeUpdate();
    }
    
    public void updateCustomer(Connection conn, Customer customer, String userName) throws SQLException {
        String customerUpdateSQL = "update customer set customerName = ? "
                + ", addressID = ?, active = ?, lastUpdate = ?, "
                + "lastUpdateBy = ? where customerId = ?";
        PreparedStatement updateStatement = conn.prepareStatement(customerUpdateSQL);
        
        customer.modifyLastUpdate(userName);
        
        updateStatement.setString(1, customer.getCustomerName());
        updateStatement.setInt(2, customer.getAddressID());
        updateStatement.setInt(3, customer.getActive());
        updateStatement.setTimestamp(4, customer.getLastUpdate());
        updateStatement.setString(5, customer.getLastUpdateBy());
        updateStatement.setInt(6, customer.getCustomerID());

        updateStatement.executeUpdate();
    }
    
    public void deleteCustomer(Connection conn, Customer customer) throws SQLException {
        String customerDeleteSQL = "delete from customer where customerId = ?";
        PreparedStatement deleteStatement = conn.prepareStatement(customerDeleteSQL);
        
        deleteStatement.setInt(1, customer.getCustomerID());
        
        deleteStatement.executeUpdate();
        customers.remove(customer);
    }
    
    public void printCustomers() {
        for(Customer customer : customers) {
            System.out.println(customer);
        }
    }
    
    public int autoGenID() {
        int newID = 1;
        for(Customer customer : customers) {
            if(customer.getCustomerID() >= newID) {
                newID = customer.getCustomerID() + 1;
            }
        }
        
        return newID;
    }
}
