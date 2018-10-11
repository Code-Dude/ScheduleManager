/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulemanager.model;

/**
 *
 * @author scott
 */
public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException() {
        super("Invalid Username or Password");
    }
}
