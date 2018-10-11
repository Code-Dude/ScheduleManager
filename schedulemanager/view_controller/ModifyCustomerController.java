/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.view_controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import schedulemanager.ScheduleManager;
import schedulemanager.model.Address;
import schedulemanager.model.City;
import schedulemanager.model.Country;
import schedulemanager.model.Customer;

/**
 * FXML Controller class
 *
 * @author scott
 */
public class ModifyCustomerController implements Initializable {
    
    private Stage dialogStage;
    
    private ScheduleManager mainApp;
    
    private Customer customer;
    
    private Address address;
    
    private City city;
    
    private Country country;
    
    private boolean saveClicked;
    
    @FXML
    private TextField customerNameField;
    
    @FXML
    private TextField activeField;
    
    @FXML
    private TextField addressField;
    
    @FXML
    private TextField address2Field;
    
    @FXML
    private TextField postalCodeField;
    
    @FXML
    private TextField phoneNumberField;
            
    @FXML
    private TextField cityField;
    
    @FXML
    private TextField countryField;
    
    @FXML
    private Button cancelModBtn;
    
    @FXML
    private Button saveModBtn;
            
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setMainApp(ScheduleManager mainApp) {
        this.mainApp = mainApp;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
        address = mainApp.getAddressManager().getAddress(customer.getAddressID());
        city = mainApp.getCityManager().getCity(address.getCityID());
        country = mainApp.getCountryManager().getCountry(city.getCountryID());

        customerNameField.setText(customer.getCustomerName());
        activeField.setText(Integer.toString(customer.getActive()));
        addressField.setText(address.getAddress());
        address2Field.setText(address.getAddress2());
        postalCodeField.setText(address.getPostalCode());
        phoneNumberField.setText(address.getPhoneNumber());
        cityField.setText(city.getCity());
        countryField.setText(country.getCountry());
    }
    
    public boolean isSaveClicked() {
        return this.saveClicked;
    }
    
    @FXML
    private void handleSave() throws SQLException {
        if(validInput()) {
            String userName = mainApp.getLoggedInUser().getUserName();
            
            customer.setCustomerName(customerNameField.getText());
            customer.setActive(Integer.parseInt(activeField.getText()));
            
            address.setAddress(addressField.getText());
            
            if(address2Field.getText().equals("")) {
                address.setAddress2("N/A");
            }
            else {
                address.setAddress2(address2Field.getText());
            }
 
            address.setPostalCode(postalCodeField.getText());
            address.setPhoneNumber(phoneNumberField.getText());
            
            city.setCity(cityField.getText());
            
            country.setCountry(countryField.getText());
            
            mainApp.getAddressManager().updateAddress(mainApp.getConnection(), 
                              address, userName);
            
            mainApp.getCityManager().updateCity(mainApp.getConnection(), city, userName);
            
            mainApp.getCountryManager().updateCountry(mainApp.getConnection(), country, userName);
            
            saveClicked = true;
            dialogStage.close();
        }
        else {
            showWarningAlert("Only Address 2 is optional, please fill out all other" 
                    + " fields.");
        }
    }
    
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    private boolean validInput() {
        boolean valid = true;
        
        if(customerNameField.getText().isEmpty()) {
            valid = false;
        }
        else if(activeField.getText().isEmpty()) {
            valid = false;
        }
        else if(addressField.getText().isEmpty()) {
            valid = false;
        }
        else if(postalCodeField.getText().isEmpty()) {
            valid = false;
        }
        else if(phoneNumberField.getText().isEmpty()) {
            valid = false;
        }
        else if(cityField.getText().isEmpty()) {
            valid = false;
        }
        else if(countryField.getText().isEmpty()) {
            valid = false;
        }
        
        return valid;
    }

    public void showWarningAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Customer");
        alert.setHeaderText(message);

        alert.showAndWait(); 
    }
}
