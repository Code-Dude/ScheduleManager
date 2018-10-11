/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.view_controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import schedulemanager.ScheduleManager;
import schedulemanager.model.Appointment;
import schedulemanager.model.InvalidCredentialsException;
import schedulemanager.model.User;
import schedulemanager.model.WeeklyView;
import schedulemanager.model.YearManager;

/**
 * FXML Controller class
 *
 * @author scott
 */
public class LoginFormController implements Initializable {
    
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginBtn, exitBtn;
    
    @FXML
    private Label locationLabel;
    
    private ScheduleManager mainApp;
    private User user;
    private Stage dialogStage;
    private boolean successfulLogin;
    private String localizedErrorMessage;
    private String localizedErrorTitle;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        successfulLogin = false;
    }    
    
    @FXML
    private void handleLogin() {
        try {
            String userName = usernameField.getText();
            String password = passwordField.getText();
            getLocalizedInvalidCredentialsError();
            if(usernameField.getText().equals("")) {
                throw new InvalidCredentialsException();
            }
            else {
                if(mainApp.getUserManager().checkPassword(userName, password)) {
                    this.user = mainApp.getUserManager().getUser(userName);
                    logLogin();
                    successfulLogin = true;
                    dialogStage.close();
                }
                else {
                    throw new InvalidCredentialsException();
                }
            }
        } catch (Exception e) {
            showWarningAlert(localizedErrorMessage);
        }
    }
    
    @FXML
    private void handleCancel() {
       System.exit(0);
    }
    
    public void setMainApp(ScheduleManager mainApp) {
        this.mainApp = mainApp;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }
    
    public User getUser() {
        return this.user;
    } 
    
    public boolean getSuccessfulLogin() {
        return this.successfulLogin;
    }
    
    public void setUserLocation() {
        locationLabel.setText(mainApp.getUserLocale().toString());
    }

    public void showWarningAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(localizedErrorTitle);
        alert.setHeaderText(message);

        alert.showAndWait(); 
    }
    
    public void getLocalizedInvalidCredentialsError() {
        ResourceBundle rb;
        
        if(mainApp.getUserLocale() == Locale.US) {
            rb = ResourceBundle.getBundle("config.language_en");
            localizedErrorMessage = rb.getString("InvalidCredentialsError");
            localizedErrorTitle = rb.getString("InvalidCredentialsTitle");
        } 
        else {
            rb = ResourceBundle.getBundle("config.language_fr");
            localizedErrorMessage = rb.getString("InvalidCredentialsError");
            localizedErrorTitle = rb.getString("InvalidCredentialsTitle");
        }
    }
    
    public void logLogin() {
        try {
            File dir = new File("logs");
            File file = new File("logs/userLoginLog.txt");
            
            if(!dir.exists()) {
                dir.mkdirs();  
            }
            
            if(!file.exists()) {
                file.createNewFile();
            }
            
            PrintWriter pw = new PrintWriter(new FileOutputStream(file, true));
            
            pw.println(user.getUserName() + " logged in at: " + LocalDateTime.now());
            
            pw.close();
        } catch(IOException e) {
            System.out.println("Issue creating report.");
        }
    }
}
