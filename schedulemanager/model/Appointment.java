/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author scott
 */
public class Appointment {
    private IntegerProperty appointmentID;
    private IntegerProperty customerID;
    private StringProperty title;
    private StringProperty description;
    private StringProperty location;
    private StringProperty contact;
    private StringProperty url;
    private Timestamp start;
    private Timestamp end;
    private Timestamp createDate;
    private StringProperty createdBy;
    private Timestamp lastUpdate;
    private StringProperty lastUpdateBy;
    
    public Appointment(int appointmentID, int customerID, String title,
                       String description, String location, String contact,
                       String url, Timestamp start, Timestamp end, Timestamp createDate,
                       String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        
        this.appointmentID = new SimpleIntegerProperty(appointmentID);
        this.customerID = new SimpleIntegerProperty(customerID);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.location = new SimpleStringProperty(location);
        this.contact = new SimpleStringProperty(contact);
        this.url = new SimpleStringProperty(url);
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = new SimpleStringProperty(createdBy);
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = new SimpleStringProperty(lastUpdateBy);
    }
    
    public Appointment(int appointmentID, int customerID, String title,
                       String description, String location, String contact,
                       String url, Timestamp start, Timestamp end, String userName) {
        
        this.appointmentID = new SimpleIntegerProperty(appointmentID);
        this.customerID = new SimpleIntegerProperty(customerID);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.location = new SimpleStringProperty(location);
        this.contact = new SimpleStringProperty(contact);
        this.url = new SimpleStringProperty(url);
        this.start = start;
        this.end = end;
        stampCreation(userName);
        modifyLastUpdate(userName);
    }
    
    public int getAppointmentID() {
        return this.appointmentID.get();
    }
    
    public IntegerProperty getAppointmentIDProperty() {
        return this.appointmentID;
    }
    
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = new SimpleIntegerProperty(appointmentID);
    }
    
    public int getCustomerID() {
        return this.customerID.get();
    }
    
    public IntegerProperty getCustomerIDProperty() {
        return this.customerID;
    }
    
    public void setCustomerID(int customerID) {
        this.customerID = new SimpleIntegerProperty(customerID);
    }
    
    public String getTitle() {
        return this.title.get();
    }
    
    public StringProperty getTitleProperty() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = new SimpleStringProperty(title);
    }
    
    public String getDescription() {
        return this.description.get();
    }
    
    public StringProperty getDescriptionProperty() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = new SimpleStringProperty(description);
    }
    
    public String getLocation() {
        return this.location.get();
    }
    
    public StringProperty getLocationProperty() {
        return this.location;
    }
    
    public void setLocation(String location) {
        this.location = new SimpleStringProperty(location);
    }
    
    public String getContact() {
        return this.contact.get();
    }
    
    public StringProperty getContactProperty() {
        return this.contact;
    }
    
    public void setContact(String contact) {
        this.contact = new SimpleStringProperty(contact);
    }
    
    public String getURL() {
        return this.url.get();
    }
    
    public StringProperty getURLProperty() {
        return this.url;
    }
    
    public void setURL(String url) {
        this.url = new SimpleStringProperty(url);
    }
    
    public Timestamp getStart() {
        return this.start;
    }
    
    public void setStart(Timestamp start) {
        this.start = start;
    }
    
    public Timestamp getEnd() {
        return this.end;
    }
    
    public void setEnd(Timestamp end) {
        this.end = end;
    }
    
    public Timestamp getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
    
    public String getCreatedBy() {
        return this.createdBy.get();
    }
    
    public StringProperty getCreatedByProperty() {
        return this.createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = new SimpleStringProperty(createdBy);
    }
    
    public Timestamp getLastUpdate() {
        return this.lastUpdate;
    }
    
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    public String getLastUpdateBy() {
        return this.lastUpdateBy.get();
    }
    
    public StringProperty getLastUpdateByProperty() {
        return this.lastUpdateBy;
    }
    
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = new SimpleStringProperty(lastUpdateBy);
    }
    
    public void modifyLastUpdate(String userName) {
        Instant instant = Instant.now();
        long millis = instant.toEpochMilli();
        setLastUpdate(new Timestamp(millis));
        setLastUpdateBy(userName);
    }
    
    private void stampCreation(String userName) {
        Instant instant = Instant.now();
        long millis = instant.toEpochMilli();
        
        setCreateDate(new Timestamp(millis));
        setCreatedBy(userName);
    }
    
    public StringProperty getStartStringProperty() {
        StringProperty timeString = new SimpleStringProperty();
        timeString.setValue(start.toString());
        
        return timeString;
    }
    
    public StringProperty getStartWithTimeZoneStringProperty() {
        StringProperty timeString = new SimpleStringProperty();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a z");
        ZonedDateTime timeZoneDate = ZonedDateTime.of(start.toLocalDateTime(), ZoneId.systemDefault());
        timeString.setValue(timeZoneDate.format(format).toString());
        
        return timeString;
    }
    
    @Override
    public String toString() {
        String apptString = "Appointment ID: " + getAppointmentID() + " Title: "
                + getTitle() + " Description: " + getDescription();
        
        return apptString;
    }
}
