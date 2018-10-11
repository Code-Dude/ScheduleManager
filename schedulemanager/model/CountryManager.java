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
public class CountryManager {
    
    private ObservableList<Country> countries;
    
    public CountryManager() {
        this.countries = FXCollections.observableArrayList();
    }
    
    public ObservableList<Country> getCountries() {
        return this.countries;
    }
    
    public void addCountry(Connection conn, Country country) throws SQLException {
        String insertCountrySQL = "insert into country"
                + "(countryId, country, createDate, createdBy, lastUpdate,"
                + " lastUpdateBy) values(?,?,?,?,?,?)";
        
        PreparedStatement prepStatement = conn.prepareStatement(insertCountrySQL);
        
        prepStatement.setInt(1, country.getCountryID());
        prepStatement.setString(2, country.getCountry());
        prepStatement.setTimestamp(3, country.getCreatedDate());
        prepStatement.setString(4, country.getCreatedBy());
        prepStatement.setTimestamp(5, country.getLastUpdate());
        prepStatement.setString(6, country.getLastUpdateBy());
        
        prepStatement.executeUpdate();
        
        countries.add(country);
    }
    
    public void deleteCountry(Connection conn, Country country) throws SQLException {
        String deleteCountrySQL = "delete from country where countryId = ?";
        PreparedStatement prepStatement = conn.prepareStatement(deleteCountrySQL);
        
        prepStatement.setInt(1, country.getCountryID());
        
        prepStatement.executeUpdate();
        countries.remove(country);
    }
    
    public void loadCountries(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("select * from country");
        
        while(rs.next()) {
            int countryID = rs.getInt("countryId");
            String country = rs.getString("country");
            Timestamp createDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            
            Country newCountry = new Country(countryID, country, createDate, createdBy,
                                          lastUpdate, lastUpdateBy);
            
            countries.add(newCountry);
        }
    }
    
    public void updateCountry(Connection conn, Country country, String userName) throws SQLException {
        String updateCountrySQL = "update country set country = ?,"
                + "lastUpdate = ?, lastUpdateBy = ? where countryId = ?";
        
        PreparedStatement prepStatement = conn.prepareStatement(updateCountrySQL);
        
        country.modifyLastUpdate(userName);
        
        prepStatement.setString(1, country.getCountry());
        prepStatement.setTimestamp(2, country.getLastUpdate());
        prepStatement.setString(3, country.getLastUpdateBy());
        prepStatement.setInt(4, country.getCountryID());
        
        prepStatement.executeUpdate();
    }
    
    public Country getCountry(int countryID) {
        Stream<Country> countryStream = countries.stream().unordered().parallel();
        Optional<Country> country = countryStream.filter(x -> x.getCountryID() == countryID).findFirst();
        
        return country.get();
    }
    
    public Country getCountry(String countryName) {
        Stream<Country> countryStream = countries.stream().unordered().parallel();
        Optional<Country> country = countryStream.filter(x -> x.getCountry().equals(countryName)).findFirst();
        
        return country.get();
    }
    
    public void printCountries() {
        for(Country country : countries) {
            System.out.println(country.getCountry());
        }
    }
    
    public int autoGenID() {
        int newID = 1;
        for(Country country : countries) {
            if(country.getCountryID() >= newID) {
                newID = country.getCountryID() + 1;
            }
        }

        return newID;
    }
}
