/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.model;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author scott
 */
public class WeeklyViewManager {
    
    private ObservableList<WeeklyView> week;
    
    private AppointmentManager appAppointmentManager;
    
    public WeeklyViewManager(AppointmentManager appAppointmentManager) {
        week = FXCollections.observableArrayList();
        this.appAppointmentManager = appAppointmentManager;
    }
    
    public ObservableList<WeeklyView> getWeek(LocalDateTime dateTime) {
        for(int i = 1; i < 8; ++i) {
            if(i < dateTime.getDayOfWeek().getValue()) {
                LocalDateTime weekDay = dateTime.minusDays(dateTime.getDayOfWeek().getValue() - i);
                IntegerProperty numAppts = appAppointmentManager.getNumAppointmentsOnDay(weekDay);
                WeeklyView weekDayView = new WeeklyView(weekDay, numAppts);
                week.add(weekDayView);
            }
            else if(i > dateTime.getDayOfWeek().getValue()) {
                LocalDateTime weekDay = dateTime.plusDays(i - dateTime.getDayOfWeek().getValue());
                IntegerProperty numAppts = appAppointmentManager.getNumAppointmentsOnDay(weekDay);
                WeeklyView weekDayView = new WeeklyView(weekDay, numAppts);
                week.add(weekDayView);
            }
            else {
                IntegerProperty numAppts = appAppointmentManager.getNumAppointmentsOnDay(dateTime);
                week.add(new WeeklyView(dateTime, numAppts));
            }
        }
        
        return week;
    }
}
