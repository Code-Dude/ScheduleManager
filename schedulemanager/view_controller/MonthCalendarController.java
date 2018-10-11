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
import schedulemanager.ScheduleManager;
import schedulemanager.model.MonthViewManager;
import schedulemanager.model.WeeklyView;

/**
 * FXML Controller class
 *
 * @author scott
 */
public class MonthCalendarController implements Initializable {
  @FXML
    private DatePicker monthPicker;

    @FXML
    private TableView<WeeklyView> monthCalendarTable;

    @FXML
    private Button backBtn;

    @FXML
    private TableColumn<WeeklyView, String> dateColumn;

    @FXML
    private TableColumn<WeeklyView, Integer> numApptsColumn;
    
    private ScheduleManager mainApp;
    
    private Stage dialogStage;
    
    private MonthViewManager monthManager;
    
    private ObservableList<WeeklyView> month;
    
    private LocalDateTime anchorDate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateAsStringProperty());
        numApptsColumn.setCellValueFactory(cellData -> cellData.getValue().getNumApptsProperty().asObject());
    }    
    
    @FXML
    private void viewMonth() {
        LocalDate date = monthPicker.getValue();
        LocalTime time = LocalTime.now();
        
        monthCalendarTable.getItems().clear();
        
        month = monthManager.getMonth(LocalDateTime.of(date, time));
        
        monthCalendarTable.setItems(month);
        monthCalendarTable.refresh();
    }
    
    @FXML
    private void handleGoBack() {
        dialogStage.close();
    }
    
    public void setMainApp(ScheduleManager mainApp) {
        this.mainApp = mainApp;
        
        monthManager = new MonthViewManager(mainApp.getAppointmentManager());
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setDate(LocalDateTime anchorDate) {
        month = monthManager.getMonth(anchorDate);
        
        monthCalendarTable.setItems(month);
        monthPicker.setValue(anchorDate.toLocalDate());
    }
}
