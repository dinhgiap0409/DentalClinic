/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author admin
 */
public class ProgramException extends Exception {

    public ProgramException(String message) {
        super(message);
    }

    public static void throwException(String message) throws ProgramException {
        throw new ProgramException(message);
    }

}
