/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.daos;

/**
 *
 * @author augus
 */
public class PGFactoryDAO extends FactoryDAO{

    @Override
    //usa los daos que implementan los daos de entidades(ej BedelPGDAO)
    public BedelDAO getBedelDAO() {return new BedelPGDAO();}

    @Override
    public UsuarioDAO getUsuarioDAO() {return new UsuarioPGDAO();}

    @Override
    public AulaDAO getAulaDAO() {return new AulaPGDAO();}

    @Override
    public ReservaDAO getReservaDAO() {return new ReservaPGDAO();}
    
}
