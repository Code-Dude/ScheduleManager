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
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

/**
 *
 * @author scott
 */
public class AddressManager {
    ArrayList<Address> addresses;
    
    public AddressManager() {
        this.addresses = new ArrayList<>();
    }
    
    public ArrayList<Address> getAddresses() {
        return this.addresses;
    }
    
    public void loadAddresses(Connection conn) throws SQLException {
       Statement statement = conn.createStatement();
       ResultSet rs = statement.executeQuery("select * from address");
       while(rs.next()) {
           int addressID = rs.getInt("addressID");
           String address = rs.getString("address");
           String address2 = rs.getString("address2");
           int cityID = rs.getInt("cityId");
           String postalCode = rs.getString("postalCode");
           String phoneNumber = rs.getString("phone");
           Timestamp createDate = rs.getTimestamp("createDate");
           String createdBy = rs.getString("createdBy");
           Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
           String lastUpdateBy = rs.getString("lastUpdateBy");
           
           Address newAddress = new Address(addressID, address, address2,
                             cityID, postalCode, phoneNumber, createDate,
                             createdBy, lastUpdate, lastUpdateBy);
           
           addresses.add(newAddress);       
       }
    }
    
    public Address getAddress(int addressID) {
        Stream<Address> addressStream = addresses.stream().unordered().parallel();
        Optional<Address> address = addressStream.filter(x -> x.getAddressID() == addressID).findFirst();
        
        return address.get();
    }
    
    public int getAddressIndex(Address address) {
        return addresses.indexOf(address);
    }
    
    public void setAddress(int indexOfAddress, Address address) {
        if(address != null) {
            this.addresses.set(indexOfAddress, address);
        }
    }
    
    public void addAddress(Connection conn, Address address) throws SQLException {
        addresses.add(address);
        
        String addressInsertSQL = "insert into address"
                + " (addressId, address, address2, cityId, postalCode, "
                + "phone, createDate, createdBy,lastUpdate, lastUpdateBy) "
                + "values(?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement prepStatement = conn.prepareStatement(addressInsertSQL);
        prepStatement.setInt(1, address.getAddressID());
        prepStatement.setString(2, address.getAddress());
        prepStatement.setString(3, address.getAddress2());
        prepStatement.setInt(4, address.getCityID());
        prepStatement.setString(5, address.getPostalCode());
        prepStatement.setString(6, address.getPhoneNumber());
        prepStatement.setTimestamp(7, address.getCreatedDate());
        prepStatement.setString(8, address.getCreatedBy());
        prepStatement.setTimestamp(9, address.getLastUpdate());
        prepStatement.setString(10, address.getLastUpdateBy());

        prepStatement.executeUpdate();
    }
    
    public void updateAddress(Connection conn, Address address, String userName) throws SQLException {
        String addressUpdateSQL = "update address set address = ?, address2 = ?, cityId = ?,"
                + " postalCode = ?, phone = ?, lastUpdate = ?, lastUpdateBy = ? where addressId = ?";
        
        PreparedStatement prepStatement = conn.prepareStatement(addressUpdateSQL);
       
        address.modifyLastUpdate(userName);
        
        prepStatement.setString(1, address.getAddress());
        prepStatement.setString(2,address.getAddress2());
        prepStatement.setInt(3, address.getCityID());
        prepStatement.setString(4, address.getPostalCode());
        prepStatement.setString(5, address.getPhoneNumber());
        prepStatement.setTimestamp(6, address.getLastUpdate());
        prepStatement.setString(7, address.getLastUpdateBy());
        prepStatement.setInt(8, address.getAddressID());
        
        prepStatement.executeUpdate();
    }
    
    public void deleteAddress(Connection conn, Address address) throws SQLException {
        String deleteAddressSQL = "delete from address where addressId = ?";
        PreparedStatement prepStatement = conn.prepareStatement(deleteAddressSQL);
        prepStatement.setInt(1, address.getAddressID());
        
        prepStatement.executeUpdate();
        
        addresses.remove(address);
    }
    
    public void printAddresses() {
        for(Address address : addresses) {
            System.out.println(address);
        }
    }

    public int autoGenID() {
        int newID = 1;
        for(Address address : addresses) {
            if(address.getAddressID() >= newID) {
                newID = address.getAddressID() + 1;
            }
        }

        return newID;
    }
}
