/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dds.daos;

import dds.excepcionesLogicas.ContraseñaEquivocadaException;
import dds.excepcionesLogicas.UsuarioInexistenteException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 *
 * @author augus
 */
public interface UsuarioDAO {
    public String validarLogin(String idUsuario, boolean adminObedel) throws UsuarioInexistenteException;    
}
