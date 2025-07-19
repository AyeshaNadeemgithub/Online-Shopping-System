/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validation;

/**
 *
 * @author Ayesha Nadeem
 */
public class Validation {

    public boolean isQuantityValid(char input) {
        return !Character.isDigit(input) && input != '\b';
    }
}
