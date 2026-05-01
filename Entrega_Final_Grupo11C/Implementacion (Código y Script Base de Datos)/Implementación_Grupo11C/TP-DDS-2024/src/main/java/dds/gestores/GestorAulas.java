/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.gestores;

import dds.daos.AulaDAO;
import dds.daos.FactoryDAO;
import dds.dtos.AulaDTO;
import dds.dtos.AulaInformaticaDTO;
import dds.dtos.AulaMultimediosDTO;
import dds.dtos.AulaSinRecursosDTO;
import dds.entidades.AulaInformatica;
import dds.entidades.AulaMultimedios;
import dds.entidades.AulaSinRecursos;
import dds.enumerados.TipoAula;
import dds.excepcionesLogicas.AulaNoEncontradoException;
import dds.excepcionesLogicas.ModificacionDeAulaInvalidaException;
import dds.util.Pair;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author augus
 */
public class GestorAulas {
     //singleton
    private static GestorAulas instance;
    public static GestorAulas getInstance(){
        if(GestorAulas.instance == null)GestorAulas.instance =  new GestorAulas();
        return GestorAulas.instance;
    }
    
    private GestorAulas(){
    }
    
    //se retorna el tipo de cada aula para que front downcastee(ver en integracion, quiza quedan de util estas funciones?)
    
    //otra alternativa: tambien se podria trabajar con una matriz de 3 renglones, uno por tipo. Se podrian dejar estas de util y armar esta matriz tambien
    
    public Pair<AulaDTO,TipoAula> buscar(int numeroDeAula) throws AulaNoEncontradoException{
        
        
        AulaDTO res = null;
        TipoAula tipoRes = null;
        /*
        for(TipoAula tipo : TipoAula.values()){
            tipoRes = tipo;
            res = FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getAulaDAO().buscarPorIdYTipo(numeroDeAula,tipo);
            if(res != null) break;
        }
        
        if(res == null){
            throw new AulaNoEncontradoException();
        }
        */
        return new Pair<>(res,tipoRes);
    }
    
    public List<Pair<AulaDTO,TipoAula>> buscar(TipoAula tipoAula, int capacidadMinima){
        /*AulaDAO dao = FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getAulaDAO();
        if(tipoAula!=null){
            List<AulaDTO> lista = dao.buscarPorTipoYCapacidad(tipoAula, capacidadMinima);
            List<Pair<AulaDTO,TipoAula>> res = new ArrayList<>();
            
            for(AulaDTO adto : lista) res.add(new Pair<>(adto,tipoAula));
            
            return res;
        }
        else{
            
            List<Pair<AulaDTO,TipoAula>> res = new ArrayList<>();
            //TipoAula tipoRes = null;
            for(TipoAula tipo : TipoAula.values()){
                List<AulaDTO> lista = dao.buscarPorTipoYCapacidad(tipo, capacidadMinima);
                for(AulaDTO adto : lista) res.add(new Pair<>(adto,tipo));
            }
            
            return res;
        */
        return null;
        
        

    }
    
    
    
    
    //para estos, validar reglas de negocio y luego enviar a dao
    public boolean modificar(AulaMultimediosDTO adto) throws ModificacionDeAulaInvalidaException{
        //if(adto.getCapacidad()<=0)throw new ModificacionDeAulaInvalidaException();
        
        //crear el objeto y mandarselo a dao
        //AulaMultimedios aula = new AulaMultimedios(adto);
        
        //FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getAulaDAO().update(aula);
        
        return true;
    }
    public boolean modificar(AulaInformaticaDTO adto)throws ModificacionDeAulaInvalidaException{
        
        //crear el objeto y mandarselo a dao
        //AulaInformatica aula = new AulaInformatica(adto);
        
        //FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getAulaDAO().update(aula);
        
        return true;
    }
    public boolean modificar(AulaSinRecursosDTO adto)throws ModificacionDeAulaInvalidaException{
        //el aula no puede tener ventiladores y aires al mismo tiempo
        //if(adto.getCapacidad()<=0) throw new ModificacionDeAulaInvalidaException("Error: no puede ingresar capacidad negativa o nula");
        
        //crear el objeto y mandarselo a dao
        //AulaSinRecursos aula = new AulaSinRecursos(adto);
        
        //FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getAulaDAO().update(aula);
        
        return true;
    }


    //eliminacionLogica(int nroDeAula) metodo aparte de modficar?
    
    //List<ReservaDTO> reservasAsociadas(int nroDeAula) para pasarselo al dao
    
    
    
    
    
    
    
}
