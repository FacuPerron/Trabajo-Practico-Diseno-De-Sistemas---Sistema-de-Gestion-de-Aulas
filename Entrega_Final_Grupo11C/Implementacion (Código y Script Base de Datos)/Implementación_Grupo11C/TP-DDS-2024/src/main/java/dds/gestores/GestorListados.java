/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.gestores;

/**
 *
 * @author augus
 */
public class GestorListados {
     //singleton
    private static GestorListados instance;
    public static GestorListados getInstance(){
        if(GestorListados.instance == null)GestorListados.instance =  new GestorListados();
        return GestorListados.instance;
    }
    
    private GestorListados(){
    }
    
}
