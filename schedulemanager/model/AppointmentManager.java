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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author scott
 */
public class AppointmentManager {
    ObservableList<Appointment> appointments;
    
    public AppointmentManager() {
        this.appointments = FXCollections.observableArrayList();
    }
    
    public ObservableList<Appointment> getAppointments() {
        return this.appointments;
    }
    
    public void loadAppointments(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("select * from appointment");
        
        while(rs.next()) {
            int appointmentID = rs.getInt("appointmentId");
            int customerID = rs.getInt("customerId");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String contact = rs.getString("contact");
            String url = rs.getString("url");
            Timestamp start = rs.getTimestamp("start");
            Timestamp end = rs.getTimestamp("end");
            Timestamp createDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            
            Appointment appointment = new Appointment(appointmentID, customerID,
                                          title, description, location,
                                          contact, url, start, end, createDate,
                                          createdBy, lastUpdate, lastUpdateBy);
            
            appointments.add(appointment);
        }
    }
    
    public void addAppointment(Connection conn, Appointment appointment) throws SQLException {
        appointments.add(appointment);
        
        String appointmentInsertSQL = "insert into appointment"
                + " (appointmentId, customerId, title, description, location, "
                + "contact, url, start, end, createDate, createdBy, lastUpdate, "
                + "lastUpdateBy) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement prepStatement = conn.prepareStatement(appointmentInsertSQL);
        prepStatement.setInt(1, appointment.getAppointmentID());
        prepStatement.setInt(2, appointment.getCustomerID());
        prepStatement.setString(3, appointment.getTitle());
        prepStatement.setString(4, appointment.getDescription());
        prepStatement.setString(5, appointment.getLocation());
        prepStatement.setString(6, appointment.getContact());
        prepStatement.setString(7, appointment.getURL());
        prepStatement.setTimestamp(8, appointment.getStart());
        prepStatement.setTimestamp(9, appointment.getEnd());
        prepStatement.setTimestamp(10, appointment.getCreateDate());
        prepStatement.setString(11, appointment.getCreatedBy());
        prepStatement.setTimestamp(12, appointment.getLastUpdate());
        prepStatement.setString(13, appointment.getLastUpdateBy());

        prepStatement.executeUpdate();
    }
    
    public void updateAppointment(Connection conn, Appointment appointment, 
                                  String userName) throws SQLException {
        
        String updateAppointmentSQL = "update appointment set customerId = ?,"
                + " title = ?, description = ?, location = ?,"
                + " contact = ?, url = ?, start = ?, end = ?,"
                + " lastUpdate = ?, lastUpdateBy = ? where appointmentId = ?";
        
        PreparedStatement prepStatement = conn.prepareStatement(updateAppointmentSQL);
        
        appointment.modifyLastUpdate(userName);
        
        prepStatement.setInt(1, appointment.getCustomerID());
        prepStatement.setString(2, appointment.getTitle());
        prepStatement.setString(3, appointment.getDescription());
        prepStatement.setString(4, appointment.getLocation());
        prepStatement.setString(5, appointment.getContact());
        prepStatement.setString(6, appointment.getURL());
        prepStatement.setTimestamp(7, appointment.getStart());
        prepStatement.setTimestamp(8, appointment.getEnd());
        prepStatement.setTimestamp(9, appointment.getLastUpdate());
        prepStatement.setString(10, appointment.getLastUpdateBy());
        prepStatement.setInt(11, appointment.getAppointmentID());
        
        prepStatement.executeUpdate();
    }
    
    public void deleteAppointment(Connection conn, Appointment appointment) throws SQLException {
        String appointmentDeleteSQL = "delete from appointment where appointmentId = ?";
        PreparedStatement deleteStatement = conn.prepareStatement(appointmentDeleteSQL);
        
        deleteStatement.setInt(1, appointment.getAppointmentID());
        
        deleteStatement.executeUpdate();
        appointments.remove(appointment);
    }
    
    public void printAppointments() {
        for(Appointment appt : appointments) {
            System.out.println(appt);
        }
    }
    
    public Appointment getAppointment(int appointmentID) {
        Stream<Appointment> appointmentStream = appointments.stream().unordered().parallel();
        Optional<Appointment> appointment = appointmentStream.filter(x -> x.getAppointmentID() == appointmentID).findFirst();
        
        return appointment.get();
    }

    public int autoGenID() {
        int newID = 1;
        for(Appointment appointment : appointments) {
            if(appointment.getAppointmentID() >= newID) {
                newID = appointment.getAppointmentID() + 1;
            }
        }

        return newID;
    }
    
    public IntegerProperty getNumAppointmentsOnDay(LocalDateTime dateTime) {
        int numAppts = 0;
        LocalDate date = dateTime.toLocalDate();
        for(int i = 0; i < appointments.size(); ++i) {
            if(appointments.get(i).getStart().toLocalDateTime().toLocalDate().compareTo(date) == 0) {
                ++numAppts;
            }
        }
        
        return new SimpleIntegerProperty(numAppts);
    }
    
    public boolean appointmentSoon(User user) {
        boolean apptSoon = false;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusFifteen = now.plusMinutes(15);
        
        for(Appointment appt : appointments) {
            if(appt.getLastUpdateBy().equals(user.getUserName())) {
                if(appt.getStart().toLocalDateTime().isBefore(nowPlusFifteen) &&
                        appointmentToday(appt)) {
                    
                    apptSoon = true;
                    return apptSoon;
                }
            }
        }
        
        return apptSoon;
    }
    
    public boolean appointmentToday(Appointment appt) {
        boolean apptToday = false;
        LocalDateTime today = LocalDateTime.now();
        
        if(appt.getStart().toLocalDateTime().getYear() == today.getYear() &&
                                appt.getStart().toLocalDateTime().getDayOfYear() == today.getDayOfYear()) {
            apptToday = true;
        }
        
        return apptToday;
    }
}
