/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.daos;

/**
 *
 * @author augus
 */
public abstract class FactoryDAO {
    //codigo directo del pwp
    public static final int PG_FACTORY = 1;
    public abstract BedelDAO getBedelDAO();
    public abstract UsuarioDAO getUsuarioDAO();
    public abstract AulaDAO getAulaDAO();
    public abstract ReservaDAO getReservaDAO();
    public static FactoryDAO getFactory(int claveFactory){
        switch (claveFactory) {
            case PG_FACTORY -> {
                return new PGFactoryDAO();
            }
        }
        return null; //Netbeans exige que retorne
    }

}
