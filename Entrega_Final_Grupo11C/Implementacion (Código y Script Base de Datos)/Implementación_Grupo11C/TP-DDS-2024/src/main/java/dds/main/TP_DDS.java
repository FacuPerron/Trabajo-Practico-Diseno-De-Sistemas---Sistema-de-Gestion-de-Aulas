/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package dds.main;


import dds.pantallas.InicioSesion;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.swing.SwingUtilities;

/**
 *
 * @author augus
 */
public class TP_DDS {

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
            SwingUtilities.invokeLater(() -> {
            InicioSesion miVentana = new InicioSesion();
            miVentana.setVisible(true); 
        });
        /*String pw=GestorContraseña.getInstance().generateStrongPasswordHash("GHijk13+-");
        int longitud=pw.length();
        System.out.println(longitud);
        System.out.println(GestorContraseña.getInstance().generateStrongPasswordHash("GHijk13+-"));*/
    }
}
