/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.view_controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import resources.ClockLister;
import schedulemanager.ScheduleManager;
import schedulemanager.model.Appointment;
import schedulemanager.model.WeeklyView;
import schedulemanager.model.WeeklyViewManager;

/**
 * FXML Controller class
 *
 * @author scott
 */
public class WeeklyCalendarController implements Initializable {
    @FXML
    private Button backBtn;

    @FXML
    private TableColumn<WeeklyView, String> dateColumn;

    @FXML
    private DatePicker weekPicker;

    @FXML
    private TableView<WeeklyView> weeklyCalendarTable;

    @FXML
    private TableColumn<WeeklyView, Integer> numApptsColumn;
    
    private Stage dialogStage;
    
    private ScheduleManager mainApp;
    
    private LocalDateTime anchorDate;
    
    private ObservableList<WeeklyView> week;
    
    private WeeklyViewManager weekManager;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        dateColumn.setCellValueFactory(cellData -> ClockLister.getDateAsStringProperty(cellData.getValue()));
          dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateAsStringProperty());
          numApptsColumn.setCellValueFactory(cellData -> cellData.getValue().getNumApptsProperty().asObject());
    }    
    
    @FXML
    private void handleGoBack() {
        dialogStage.close();
    }
    
    public void setMainApp(ScheduleManager mainApp) {
        this.mainApp = mainApp;
        
        this.weekManager = new WeeklyViewManager(mainApp.getAppointmentManager());
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setDate(LocalDateTime dateTime) {
        anchorDate = dateTime;
       
        week = weekManager.getWeek(anchorDate);
        weeklyCalendarTable.setItems(week);
        weekPicker.setValue(anchorDate.toLocalDate());
    }
    
    @FXML
    public void viewWeek() {
        LocalDate date = weekPicker.getValue();
        LocalTime time = LocalTime.now();
        
        weeklyCalendarTable.getItems().clear();
        
        week = weekManager.getWeek(LocalDateTime.of(date, time));
        
        weeklyCalendarTable.setItems(week);
        weeklyCalendarTable.refresh();
    }
}
