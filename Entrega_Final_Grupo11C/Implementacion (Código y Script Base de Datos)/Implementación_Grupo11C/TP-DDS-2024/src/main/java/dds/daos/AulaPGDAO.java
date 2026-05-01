/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.daos;

import dds.dtos.AulaDTO;
import dds.dtos.AulaInformaticaDTO;
import dds.dtos.AulaMultimediosDTO;
import dds.dtos.AulaSinRecursosDTO;
import dds.entidades.Aula;
import dds.entidades.AulaInformatica;
import dds.entidades.AulaMultimedios;
import dds.entidades.AulaSinRecursos;
import dds.enumerados.TipoAula;
import dds.enumerados.TipoPizarron;
import dds.enumerados.Ubicacion;
import dds.excepcionesLogicas.AulaNoEncontradoException;
import dds.util.Pair;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author augus
 */
public class AulaPGDAO implements AulaDAO {

    private final ConexionSql conector;
    
    public AulaPGDAO(){
       conector = new ConexionSql();
    }

    
    //una query por tipo, gestor llama iterando sobre los tipos
    //fciones util que tome un item del resultset y te devuelve el aula dto
    @Override
    public Aula buscarPorIdYTipo(int nrDeAula, TipoAula tipo) {
        String subtabla = mapearTipoANombreDeTabla(tipo);
       Aula ret = null;
        
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        
        // Construir la consulta SQL con el nombre de la tabla dinámico
        String sql = String.format("""
                SELECT * FROM aula
                JOIN %s ON %s.id = aula.id
                WHERE aula.id = ?
            """, subtabla, subtabla);
        
        try(PreparedStatement preparedStatement = con.prepareStatement(sql)){
            preparedStatement.setInt(1, nrDeAula);
            ResultSet resultados = preparedStatement.executeQuery();
            if (resultados.next()){
                ret = armarAula(tipo, resultados);
            }
            
            con.commit(); //o se modifican ambas tablas o ninguna
            return ret;
            }
            catch(SQLException e){
                System.out.println("excepcion en "+this.getClass().getName()+".buscarPorId() al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(SQLException e){
            System.out.println("excepcion en "+this.getClass().getName()+".buscarPorId() al abrir conexion" + e.getMessage());
        } 
        

        return ret;
    
    }

    @Override
    public List<Aula> buscarPorTipoYCapacidad(TipoAula tipo, int capacidad) {
        String subtabla = mapearTipoANombreDeTabla(tipo);
        List<Aula>  ret = new ArrayList<>();
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        
        String sql = String.format("""
                SELECT * FROM aula
                JOIN %s ON %s.id = aula.id
                WHERE aula.capacidad >= ?
            """, subtabla, subtabla);
        
        try(PreparedStatement preparedStatement = con.prepareStatement(sql)){
            preparedStatement.setInt(1, capacidad);
            ResultSet resultados = preparedStatement.executeQuery();
            while (resultados.next()){
                ret.add(armarAula(tipo,resultados)) ;
            }
            
            con.commit(); //o se modifican ambas tablas o ninguna
            return ret;
            }
            catch(SQLException e){
                System.out.println("excepcion en "+this.getClass().getName()+".buscarPorTipoYCapacidad() al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(SQLException e){
            System.out.println("excepcion en "+this.getClass().getName()+".buscarPorTipoYCapacidad() al abrir conexion" + e.getMessage());
        } 

        return ret;
    }

    
    
    @Override
    public boolean update(AulaInformatica aula) {
        update((Aula) aula);
        boolean ret = false;
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        
        try(PreparedStatement preparedStatement = con.prepareStatement("""
                                                                            UPDATE aulainformatica 
                                                                            SET canon = ?, cantidadPCs = ?
                                                                            WHERE id = ?""" );){

            //setear valores
            preparedStatement.setBoolean(1, aula.isCañon());
            preparedStatement.setInt(2, aula.getCantidadPCs());
            preparedStatement.setInt(3, aula.getId());

            preparedStatement.executeUpdate();


            con.commit();

            ret = true;
            }
            catch(Exception e){
                System.out.println("excepcion en "+this.getClass().getName()+".update(AulaInformatica) al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(Exception e){
            System.out.println("excepcion en "+this.getClass().getName()+".update(AulaInformatica) al abrir conexion" + e.getMessage());
        } 
        return ret;
    }

    @Override
    public boolean update(AulaSinRecursos aula) {
       update((Aula) aula);
        boolean ret = false;
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        
        try(PreparedStatement preparedStatement = con.prepareStatement("""
                                                                            UPDATE AulaSinRecursos 
                                                                            SET ventiladores = ?
                                                                            WHERE id = ?""" );){

            //setear valores
            preparedStatement.setBoolean(1, aula.isVentiladores());
            preparedStatement.setInt(2, aula.getId());

            preparedStatement.executeUpdate();


            con.commit();

            ret = true;
            }
            catch(Exception e){
                System.out.println("excepcion en "+this.getClass().getName()+".update(AulaSinRecursos) al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(Exception e){
            System.out.println("excepcion en "+this.getClass().getName()+".update(AulaSinRecursos) al abrir conexion" + e.getMessage());
        } 
        return ret;
    }

    @Override
    public boolean update(AulaMultimedios aula) {
        update((Aula) aula);
        boolean ret = false;
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        
        try(PreparedStatement preparedStatement = con.prepareStatement("""
                                                                            UPDATE AulaMultimedios 
                                                                            SET canon = ?, televisor = ?, computadora = ?, ventiladores = ?
                                                                            WHERE id = ?""" );){

            //setear valores
            preparedStatement.setBoolean(1, aula.isCañon());
            preparedStatement.setBoolean(2, aula.isTelevisor());
            preparedStatement.setBoolean(3, aula.isComputadora());
            preparedStatement.setBoolean(4, aula.isVentiladores());
            preparedStatement.setInt(5, aula.getId());

            preparedStatement.executeUpdate();


            con.commit();

            ret = true;
            }
            catch(Exception e){
                System.out.println("excepcion en "+this.getClass().getName()+".update(AulaMultimedios) al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(Exception e){
            System.out.println("excepcion en "+this.getClass().getName()+".update(AulaMultimedios) al abrir conexion" + e.getMessage());
        } 
        return ret;}
    
    //este para cuestiones generales
    //solo lo que el CU dice que se puede modificar. Habilitado no se puede modificar
    public boolean update(Aula aula) {
        boolean ret = false;
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        
        try(PreparedStatement preparedStatement = con.prepareStatement("""
                                                                            UPDATE aula 
                                                                            SET capacidad = ?,tipopizarron = ?,aireAcondicionado = ? 
                                                                            WHERE id = ?""" );){

            //setear valores
            preparedStatement.setInt(1, aula.getCapacidad());
            preparedStatement.setString(2, aula.getTipoPizarron().toString());
            preparedStatement.setBoolean(3, aula.getAireAcondicionado());
            preparedStatement.setInt(4, aula.getId());

            preparedStatement.executeUpdate();


            con.commit();

            ret = true;
            }
            catch(Exception e){
                System.out.println("excepcion en "+this.getClass().getName()+".update(Aula) al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(Exception e){
            System.out.println("excepcion en "+this.getClass().getName()+".update(Aula) al abrir conexion" + e.getMessage());
        } 
        return ret;
    }
    

    
    
    //utils
   private Aula armarAula(TipoAula tipo, ResultSet resultados) {
        //mapeo
        try{
        switch (tipo.toString()){
            case "MULTIMEDIOS" -> {
                return new AulaMultimedios(resultados.getInt("id"), resultados.getInt("capacidad"), Ubicacion.valueOf(resultados.getString("ubicacion")), 
                        resultados.getBoolean("habilitado"), resultados.getBoolean("aireAcondicionado"), TipoPizarron.valueOf(resultados.getString("TipoPizarron")), 
                        resultados.getBoolean("ventiladores"), resultados.getBoolean("canon"),
                        resultados.getBoolean("computadora"), resultados.getBoolean("televisor"));
            }
            case "INFORMATICA" -> {
                return new AulaInformatica(resultados.getInt("id"), resultados.getInt("capacidad"), Ubicacion.valueOf(resultados.getString("ubicacion")), 
                        resultados.getBoolean("habilitado"), resultados.getBoolean("aireAcondicionado"), TipoPizarron.valueOf(resultados.getString("TipoPizarron")), 
                        resultados.getInt("cantidadPCs"), resultados.getBoolean("canon"));
                
            }
            case "SINRECURSOS" -> {
                return new AulaSinRecursos(resultados.getInt("id"), resultados.getInt("capacidad"), Ubicacion.valueOf(resultados.getString("ubicacion")), 
                        resultados.getBoolean("habilitado"), resultados.getBoolean("aireAcondicionado"), TipoPizarron.valueOf(resultados.getString("TipoPizarron")), 
                        resultados.getBoolean("ventiladores"));
            
            }
        
        }
        }
        catch(SQLException e){
                System.out.println("excepcion al armar aula");
                
                }
        return null;
        
    
    }
	

    private String mapearTipoANombreDeTabla(TipoAula tipo) {
        switch (tipo.toString()){
            case "MULTIMEDIOS" -> {
                return "AulaMultimedios";
            }
            case "INFORMATICA" -> {
                return "AulaInformatica";
            }
            case "SINRECURSOS" -> {
                return "AulaSinRecursos";
            }
        }
        return null;
    }

}
