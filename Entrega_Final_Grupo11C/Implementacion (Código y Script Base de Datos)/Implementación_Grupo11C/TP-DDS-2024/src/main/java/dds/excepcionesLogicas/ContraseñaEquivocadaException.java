/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package dds.excepcionesLogicas;

/**
 *
 * @author augus
 */
public class ContraseñaEquivocadaException extends Exception{

    /**
     * Creates a new instance of <code>ContraseñaEquivocadaException</code>
     * without detail message.
     */
    public ContraseñaEquivocadaException() {
    }

    /**
     * Constructs an instance of <code>ContraseñaEquivocadaException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ContraseñaEquivocadaException(String msg) {
        super(msg);
    }
}
