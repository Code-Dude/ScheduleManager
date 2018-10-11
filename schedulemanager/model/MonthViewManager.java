/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author scott
 */
public class MonthViewManager {
    
    private ObservableList<WeeklyView> month;
    
    private AppointmentManager appAppointmentManager;
    
    public MonthViewManager(AppointmentManager appAppointmentManager) {
        month = FXCollections.observableArrayList();
        this.appAppointmentManager = appAppointmentManager;
    }
    
    public ObservableList<WeeklyView> getMonth(LocalDateTime dateTime) {
        YearMonth yearMonth = YearMonth.of(dateTime.getYear(), dateTime.getMonth());
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate date;
        LocalDateTime dateOfMonth;
        IntegerProperty numAppts;
        
        for(int i = 1; i <= daysInMonth; ++i) {
            date = LocalDate.of(dateTime.getYear(), dateTime.getMonth(), i);
            dateOfMonth = LocalDateTime.of(date, dateTime.toLocalTime());
            numAppts = appAppointmentManager.getNumAppointmentsOnDay(dateOfMonth);
            
            month.add(new WeeklyView(dateOfMonth, numAppts));
        }
        
        return month;
    }
}
