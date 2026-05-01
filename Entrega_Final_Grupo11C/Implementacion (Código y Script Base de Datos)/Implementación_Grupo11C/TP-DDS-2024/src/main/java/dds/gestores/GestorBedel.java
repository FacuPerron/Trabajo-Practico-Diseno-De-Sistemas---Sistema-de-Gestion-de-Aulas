/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.gestores;

import dds.daos.FactoryDAO;
import dds.dtos.BedelDTO;
import dds.entidades.Bedel;
import dds.enumerados.Turno;
import dds.excepcionesLogicas.BedelNoEncontradoException;
import dds.excepcionesLogicas.BedelYaExisteException;
import dds.excepcionesLogicas.PoliticaDeContraseñaException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author augus
 */
public class GestorBedel {
    //singleton
    private static GestorBedel instance;
    public static GestorBedel getInstance(){
        if(GestorBedel.instance == null)GestorBedel.instance =  new GestorBedel();
        return GestorBedel.instance;
    }
    
    private GestorBedel(){
    }

    //usa el dao interfaz
    
    public boolean registro(BedelDTO bdto) throws PoliticaDeContraseñaException, BedelYaExisteException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        //validaciones
	GestorContraseña.getInstance().validarPW(bdto.getPassword(), bdto.getConfirmacion());
        
        if(FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getBedelDAO().cuantosActivos(bdto.getIdUsuario()) > 0) throw new BedelYaExisteException();
        
        
        //crear bedel, obtener dao en una variable y mandar mensaje create
        Bedel b = new Bedel(bdto);
        //hasheamos la pw
        b.setPassword(
        GestorContraseña.getInstance().generateStrongPasswordHash(bdto.getPassword())
        );
        FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getBedelDAO().create(b);
        
        return true;
    }
    
    
    public boolean modificacion(BedelDTO bdtoModificado, boolean cambiopw) throws PoliticaDeContraseñaException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        //this.validarBedel(bdtoModificado);
        
        if (cambiopw){
            GestorContraseña.getInstance().validarPW(bdtoModificado.getPassword(), bdtoModificado.getConfirmacion());
            bdtoModificado.setPassword(
            GestorContraseña.getInstance().generateStrongPasswordHash(bdtoModificado.getPassword())
            );
        }
        
        Bedel b = new Bedel(bdtoModificado); 
       
        FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getBedelDAO().update(b, cambiopw);
        
        return true;
    }
    
    public boolean eliminacionLogica(int id) throws BedelNoEncontradoException{
        
        Bedel b = FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getBedelDAO().buscarPorId(id);
        
        b.setActivo(Boolean.FALSE);
        
        FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getBedelDAO().update(b, false);
        
        return true;
    }
    
   /* public Bedel buscarBedel(int idBedel) throws BedelNoEncontradoException {
        Bedel b = ;
        return b;
    }*/
    
    public ArrayList<BedelDTO> busqueda(String apellido, Turno t) {
        List<Bedel> lista = FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getBedelDAO().buscarPorApelYTurno(apellido,t);
        ArrayList<BedelDTO> ret = new ArrayList<>();
        for(Bedel b:lista) ret.add(convertirADTO(b));
        return ret;
    }
    
    
    private boolean validarBedel(BedelDTO bdto) throws PoliticaDeContraseñaException {
        GestorContraseña gc = GestorContraseña.getInstance(); 
        gc.validarPW(bdto.getPassword(),bdto.getConfirmacion());
        
        return true;
    }
    
    
    /*private Bedel convertirAModelo(BedelDTO bdto){
        return new Bedel(bdto);
    }*/
    private BedelDTO convertirADTO(Bedel b){
        return new BedelDTO(b.getId(),b.getIdUsuario(),b.getNombre(),
                b.getApellido(),b.getEmail(),b.getPassword(),b.getPassword(),b.getTurno());
    }

    
}
