/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package dds.excepcionesLogicas;

/**
 *
 * @author augus
 */
public class ModificacionDeAulaInvalidaException extends Exception {

    /**
     * Creates a new instance of
     * <code>ModificacionDeAulaInvalidaException</code> without detail message.
     */
    public ModificacionDeAulaInvalidaException() {
    }

    /**
     * Constructs an instance of
     * <code>ModificacionDeAulaInvalidaException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public ModificacionDeAulaInvalidaException(String msg) {
        super(msg);
    }
}
