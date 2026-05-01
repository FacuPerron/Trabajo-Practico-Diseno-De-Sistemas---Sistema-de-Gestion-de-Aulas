/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package dds.excepcionesLogicas;

/**
 *
 * @author augus
 */
public class AulaNoEncontradoException extends Exception {

    /**
     * Creates a new instance of <code>AulaNoEncontradoException</code> without
     * detail message.
     */
    public AulaNoEncontradoException() {
    }

    /**
     * Constructs an instance of <code>AulaNoEncontradoException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AulaNoEncontradoException(String msg) {
        super(msg);
    }
}
