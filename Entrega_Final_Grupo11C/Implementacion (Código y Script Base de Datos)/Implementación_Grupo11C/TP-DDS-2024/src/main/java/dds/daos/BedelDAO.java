/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dds.daos;

import dds.entidades.Bedel;
import dds.dtos.BedelDTO;
import dds.enumerados.Turno;
import dds.excepcionesLogicas.BedelNoEncontradoException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author augus
 */
public interface BedelDAO {
    //las busquedas son solo sobre los activos
    
    //principales
    public boolean create(Bedel b);
    public boolean update(Bedel b, boolean cambiopw);//delete tambien se hace con este
    
    public List<Bedel> listar();
    public Bedel buscarPorId (int id) throws BedelNoEncontradoException;
    public Bedel buscarPorIdUsuario(String idbedel)  throws BedelNoEncontradoException;
    public ArrayList<Bedel> buscarPorApelYTurno(String apellido, Turno t);
    
    
    //auxiliar
    public int cuantosActivos(String idUsuario);
    
    
    
 }
