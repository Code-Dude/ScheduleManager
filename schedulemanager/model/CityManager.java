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
public class CityManager {
    
    private ObservableList<City> cities;
    
    public CityManager() {
        this.cities = FXCollections.observableArrayList();
    }
    
    public ObservableList<City> getCities() {
        return this.cities;
    }
    
    public void loadCities(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("select * from city");
        
        while(rs.next()) {
            int cityID = rs.getInt("cityId");
            String city = rs.getString("city");
            int countryID = rs.getInt("countryId");
            Timestamp createDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            
            City newCity = new City(cityID, city, countryID, createDate, createdBy,
                                 lastUpdate, lastUpdateBy);
            
            cities.add(newCity);
        }
    }
    
    public City getCity(int cityID) {
        Stream<City> cityStream = cities.stream().unordered().parallel();
        Optional<City> wantedCity = cityStream.filter(x -> x.getCityID() == cityID).findFirst();
        
        return wantedCity.get();
    }
    
    public void addCity(Connection conn, City city) throws SQLException {
        String insertCitySQL = "insert into city"
                + "(cityId, city, countryId, createDate, createdBy, lastUpdate,"
                + " lastUpdateBy) values(?,?,?,?,?,?,?)";
        
        PreparedStatement prepStatement = conn.prepareStatement(insertCitySQL);
        
        prepStatement.setInt(1, city.getCityID());
        prepStatement.setString(2, city.getCity());
        prepStatement.setInt(3, city.getCountryID());
        prepStatement.setTimestamp(4, city.getCreateDate());
        prepStatement.setString(5, city.getCreatedBy());
        prepStatement.setTimestamp(6, city.getLastUpdate());
        prepStatement.setString(7, city.getLastUpdatedBy());
        
        prepStatement.executeUpdate();
        
        cities.add(city);
    }
    
    public void updateCity(Connection conn, City city, String userName) throws SQLException {
        String updateCitySQL = "update city set city = ?,"
                + "countryID = ?, lastUpdate = ?, lastUpdateBy = ? where cityId = ?";
        
        city.modifyLastUpdate(userName);
        
        PreparedStatement prepStatement = conn.prepareStatement(updateCitySQL);
        prepStatement.setString(1, city.getCity());
        prepStatement.setInt(2, city.getCountryID());
        prepStatement.setTimestamp(3, city.getLastUpdate());
        prepStatement.setString(4, city.getLastUpdatedBy());
        prepStatement.setInt(5, city.getCityID());
        
        prepStatement.executeUpdate();
    }
    
    public void deleteCity(Connection conn, City city) throws SQLException {
        String deleteCitySQL = "delete from city where cityId = ?";
        PreparedStatement prepStatement = conn.prepareStatement(deleteCitySQL);
        
        prepStatement.setInt(1, city.getCityID());
        
        prepStatement.executeUpdate();
        cities.remove(city);
    }
    
    public void printCities() {
        for(City city : cities) {
            System.out.println(city.getCity());
        }
    }
    
    public int autoGenID() {
        int newID = 1;
        
        for(City city : cities) {
            if(city.getCityID() >= newID) {
                newID = city.getCityID() + 1;
            }
        }

        return newID;
    }
}
